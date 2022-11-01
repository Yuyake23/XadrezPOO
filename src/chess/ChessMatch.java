package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.Program;
import application.UI;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check; // is false by default
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		this.board = new Board(8, 8);
		this.turn = 1;
		this.currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getColumns() {
		return board.getColumns();
	}

	public int getRows() {
		return board.getRows();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++)
			for (int j = 0; j < board.getColumns(); j++)
				chessPieces[i][j] = (ChessPiece) board.piece(i, j);

		return chessPieces;
	}

	public boolean[][] possibleMovies(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		boolean[][] pm = board.piece(position).getPossibleMoves();
		for (int i = 0; i < pm.length; i++) {
			for (int j = 0; j < pm[i].length; j++) {
				if (pm[i][j]) {

				}
			}
		}
		return pm;
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		ChessPiece movedPiece = (ChessPiece) board.piece(target);

		// #specialmove promotion
		if (movedPiece instanceof Pawn pawn) {
			if (target.getRow() == 0 || target.getRow() == 7) {
				ChessPiece pawnBackup = pawn.clone();
				String type = Program.chosePieceType(); // I don´t like it
				movedPiece = replacePromotedPiece(movedPiece, type);
				if (testCheck(currentPlayer)) {
					undoMove(source, target, capturedPiece);
					movedPiece = pawnBackup;
					throw new ChessException("You can't put yourself in check");
				}
			}
		}

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			if (check)
				throw new ChessException("You must defend your king");
			else
				throw new ChessException("You can't put yourself in check");
		}

		// #specialmove en passant
		if (movedPiece instanceof Pawn) {
			if (source.getRow() - 2 == target.getRow() || source.getRow() + 2 == target.getRow()) {
				this.enPassantVulnerable = movedPiece;
			} else {
				this.enPassantVulnerable = null;
			}
		}

		this.check = testCheck(opponent(currentPlayer));

		if (this.check) {
			this.checkMate = testCheckMate(opponent(currentPlayer));
		}
		if (!this.checkMate) {
			nextTurn();
		}
		return (ChessPiece) capturedPiece;
	}

	public ChessPiece replacePromotedPiece(ChessPiece piece, String type) {
		type = type.toUpperCase();
		ChessPiece newPiece = switch (type) {
			case "B" -> new Bishop(board, this, piece.getColor());
			case "N" -> new Knight(board, this, piece.getColor());
			case "Q" -> new Queen(board, this, piece.getColor());
			case "R" -> new Rook(board, this, piece.getColor());
			default -> throw new IllegalArgumentException("Invalid type for promotion");
		};
		Position pos = (Position) piece.getPosition().clone();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece piece = (ChessPiece) board.removePiece(source);
		piece.increseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(piece, target);

		// #specialmove castling
		if (piece instanceof King) {
			// kingside rook
			if (target.getColumn() == source.getColumn() + 2) {
				makeMove(new Position(source.getRow(), source.getColumn() + 3),
						new Position(source.getRow(), source.getColumn() + 1));
			} else // queenside rook
			if (target.getColumn() == source.getColumn() - 2) {
				makeMove(new Position(source.getRow(), source.getColumn() - 4),
						new Position(source.getRow(), source.getColumn() - 1));
			}
		} else
		// #specialmove en passant
		if (piece instanceof Pawn && source.getColumn() != target.getColumn() && capturedPiece == null) {
			if (piece.getColor() == Color.WHITE) {
				capturedPiece = board
						.removePiece(new Position(piece.getPosition().getRow() + 1, piece.getPosition().getColumn()));
			} else {
				capturedPiece = board
						.removePiece(new Position(piece.getPosition().getRow() - 1, piece.getPosition().getColumn()));
			}
		}

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece piece = (ChessPiece) board.removePiece(target);
		piece.decreseMoveCount();
		board.placePiece(piece, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			this.capturedPieces.remove(capturedPiece);
			this.piecesOnTheBoard.add(capturedPiece);
		}

		// #specialmove castling
		if (piece instanceof King) {
			// kingside rook
			if (target.getColumn() == source.getColumn() + 2) {
				undoMove(new Position(source.getRow(), source.getColumn() + 3),
						new Position(source.getRow(), source.getColumn() + 1), null);
			} else // queenside rook
			if (target.getColumn() == source.getColumn() - 2) {
				undoMove(new Position(source.getRow(), source.getColumn() - 4),
						new Position(source.getRow(), source.getColumn() - 1), null);
			}
		} else
		// #specialmove en passant
		if (piece instanceof Pawn pawn && source.getColumn() != target.getColumn()
				&& capturedPiece == enPassantVulnerable) {
			if ((pawn.getColor() == Color.WHITE && pawn.getPosition().getRow() == 3)
					|| (pawn.getColor() == Color.BLACK && pawn.getPosition().getRow() == 4)) {
				pawn = (Pawn) board.removePiece(target);
				
				if (piece.getColor() == Color.WHITE) {
					board.placePiece(pawn, new Position(3, target.getColumn()));
				} else {
					board.placePiece(pawn, new Position(4, target.getColumn()));
				}
			}
		}

	}

	private void validateSourcePosition(Position source) {
		if (!board.thereIsAPiece(source))
			throw new ChessException("There is no piece on source position");
		if (currentPlayer != ((ChessPiece) board.piece(source)).getColor())
			throw new ChessException("The chose piece is not yours");
		if (!board.piece(source).isThereAnyPossibleMove())
			throw new ChessException("There is no possible moves for the chosen piece");
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target))
			throw new ChessException("The chosen piece can't move to target position");
	}

	private void nextTurn() {
		this.turn++;
		this.currentPlayer = this.currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
	}

	public Color opponent(Color color) {
		return color == Color.WHITE ? Color.BLACK : Color.WHITE;
	}

	private King king(Color color) {
		for (Piece piece : this.piecesOnTheBoard) {
			if (((ChessPiece) piece).getColor() == color && piece instanceof King king) {
				return king;
			}
		}
		throw new IllegalStateException(
				"There is no " + UI.getUIColor(color, UI.FontType.PLAIN) + color + UI.RESET + " king on the board");
	}

	public void validadePossibleMoves(boolean[][] pm, Position source) {
		for (int i = 0; i < pm.length; i++) {
			for (int j = 0; j < pm[i].length; j++) {
				if (pm[i][j]) {
					if (thisMovePutCurrentPlayerInCheck(source, new Position(i, j)))
						pm[i][j] = false;
				}
			}
		}
	}

	private boolean thisMovePutCurrentPlayerInCheck(Position source, Position target) {
		Piece capturedPiece = makeMove(source, target);
		boolean check = testCheck(currentPlayer);
		undoMove(source, target, capturedPiece);
		return check;
	}

	public boolean testCheck(Color color) {
		return testCheck(king(color));
	}

	public boolean testCheck(King king) {
		Position kingPosition = king.getPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(p -> ((ChessPiece) p).getColor() != king.getColor()).collect(Collectors.toList());
		for (Piece opponentPiece : opponentPieces) {
			if (opponentPiece.getAllPossibleMoves()[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		List<Piece> pieces = piecesOnTheBoard.stream().filter(p -> ((ChessPiece) p).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : pieces) {
			boolean[][] mat = p.getPossibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = (Position) p.getPosition().clone();
						Position target = new Position(i, j);

						Piece capturedPiece = makeMove(source, target);
						boolean remainsInCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!remainsInCheck)
							return false;
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, this, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, this, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, this, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, this, Color.WHITE));
		placeNewPiece('e', 1, new King(board, this, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, this, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, this, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, this, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('b', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('c', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('d', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('e', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('f', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('g', 2, new Pawn(board, this, Color.WHITE));
		placeNewPiece('h', 2, new Pawn(board, this, Color.WHITE));

		placeNewPiece('a', 8, new Rook(board, this, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, this, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, this, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, this, Color.BLACK));
		placeNewPiece('e', 8, new King(board, this, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, this, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, this, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, this, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('b', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('c', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('d', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('e', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('f', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('g', 7, new Pawn(board, this, Color.BLACK));
		placeNewPiece('h', 7, new Pawn(board, this, Color.BLACK));
	}
}
