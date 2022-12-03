package com.ifgrupo.chess;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.ifgrupo.application.Program;
import com.ifgrupo.boardgame.Board;
import com.ifgrupo.boardgame.Piece;
import com.ifgrupo.boardgame.Position;
import com.ifgrupo.chess.pieces.Bishop;
import com.ifgrupo.chess.pieces.King;
import com.ifgrupo.chess.pieces.Knight;
import com.ifgrupo.chess.pieces.Pawn;
import com.ifgrupo.chess.pieces.Queen;
import com.ifgrupo.chess.pieces.Rook;

public class ChessMatch implements Serializable {
	@Serial
	private static final long serialVersionUID = 2272696391384483565L;

	private Board board;
	private int turn;
	private Color currentPlayerColor;
	private boolean check; // is false by default
	private boolean checkMate;
	private Pawn enPassantVulnerable;

	private int movesWithOutCaptureAndPawnMove;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private Deque<Move> moveDeque = new ArrayDeque<>();

	public ChessMatch() {
		this.board = new Board(8, 8);
		this.turn = 1;
		this.currentPlayerColor = Color.WHITE;
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
		return currentPlayerColor;
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

	public Deque<Move> getMoveDeque() {
		return this.moveDeque;
	}

	public boolean matchIsOver() {
		if (checkMate)
			return true;
		if (drawn())
			return true;
		return false;
	}

	public boolean drawn() {
		// TODO: condições de empate e finalizações de jogo incomuns
		if (stalemale() || fiftyMoveRule())
			return true;
		return false;
	}

	public boolean stalemale() {
		if (check)
			return false;
		for (Piece piece : piecesOnTheBoard)
			if (piece.isThereAnyPossibleMove())
				return false;
		return true;
	}

	public boolean fiftyMoveRule() {
		return this.movesWithOutCaptureAndPawnMove >= 10;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				chessPieces[i][j] = (ChessPiece) board.getPiece(i, j);
			}
		}

		return chessPieces;
	}

	public boolean[][] possibleMovies(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.getPiece(position).getPossibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		ChessPiece movedPiece = (ChessPiece) board.getPiece(target);

		// #specialmove promotion
		String type = null;
		if (movedPiece instanceof Pawn pawn) {
			if (target.getRow() == 0 || target.getRow() == 7) {
				ChessPiece pawnBackup = pawn.clone();
				type = Program.chosePieceTypeToPromotion(); // I don´t like it
				try {
					movedPiece = replacePromotedPiece(movedPiece, type);
				} catch (ChessException e) {
					undoMove(source, target, capturedPiece);
					throw e;
				}
				if (testCheck(currentPlayerColor)) {
					undoMove(source, target, capturedPiece);
					movedPiece = pawnBackup;
					throw new ChessException("You can't put yourself in check");
				}
			}
		}

		if (testCheck(currentPlayerColor)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException(check ? "You must defend your king" : "You can't put yourself in check");
		}

		// #specialmove en passant
		if (movedPiece instanceof Pawn pawn
				&& (source.getRow() - 2 == target.getRow() || source.getRow() + 2 == target.getRow())) {
			this.enPassantVulnerable = pawn;
		} else {
			this.enPassantVulnerable = null;
		}

		this.check = testCheck(opponent(currentPlayerColor));

		if (this.check) {
			this.checkMate = testCheckMate(opponent(currentPlayerColor));
		}

		if (!this.checkMate) {
			nextTurn();
		}

		// movement logging
		this.moveDeque.add(new Move(sourcePosition, targetPosition, capturedPiece.getClass().getName(), type));

		// a tie logic
		if (capturedPiece == null && !(movedPiece instanceof Pawn)) {
			this.movesWithOutCaptureAndPawnMove++;
		} else {
			this.movesWithOutCaptureAndPawnMove = 0;
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
			default -> throw new ChessException("Invalid type for promotion");
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
			board.placePiece(capturedPiece, capturedPiece.getPosition());
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
		if (currentPlayerColor != ((ChessPiece) board.getPiece(source)).getColor())
			throw new ChessException("The chose piece is not yours");
		if (!board.getPiece(source).isThereAnyPossibleMove())
			throw new ChessException("There is no possible moves for the chosen piece");
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.getPiece(source).possibleMove(target))
			throw new ChessException("The chosen piece can't move to target position");
	}

	private void nextTurn() {
		this.turn++;
		this.currentPlayerColor = this.currentPlayerColor == Color.WHITE ? Color.BLACK : Color.WHITE;
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

		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	public void validadePossibleMoves(boolean[][] pm, Position source) {
		for (int i = 0; i < pm.length; i++) {
			for (int j = 0; j < pm[i].length; j++) {
				if (pm[i][j] && putCurrentPlayerInCheck(source, new Position(i, j))) {
					pm[i][j] = false;
				}
			}
		}
	}

	private boolean putCurrentPlayerInCheck(Position source, Position target) {
		Piece capturedPiece = makeMove(source, target);
		boolean check = testCheck(currentPlayerColor);
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
						if (!remainsInCheck) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece chessPiece) {
		board.placePiece(chessPiece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(chessPiece);
	}

	public Color getWinner() {
		return currentPlayerColor;
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

		// placeNewPiece('a', 1, new Rook(board, this, Color.WHITE));
		// placeNewPiece('b', 1, new Knight(board, this, Color.WHITE));
		// placeNewPiece('c', 1, new Bishop(board, this, Color.WHITE));
		// placeNewPiece('d', 1, new Queen(board, this, Color.WHITE));
		// placeNewPiece('e', 1, new King(board, this, Color.WHITE));
		// placeNewPiece('f', 1, new Bishop(board, this, Color.WHITE));
		// placeNewPiece('g', 1, new Knight(board, this, Color.WHITE));
		// placeNewPiece('h', 1, new Rook(board, this, Color.WHITE));
		// placeNewPiece('a', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('b', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('c', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('d', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('e', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('f', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('g', 2, new Pawn(board, this, Color.WHITE));
		// placeNewPiece('h', 2, new Pawn(board, this, Color.WHITE));
		//
		// placeNewPiece('a', 8, new Rook(board, this, Color.BLACK));
		// placeNewPiece('b', 8, new Knight(board, this, Color.BLACK));
		// placeNewPiece('c', 8, new Bishop(board, this, Color.BLACK));
		// placeNewPiece('d', 8, new Queen(board, this, Color.BLACK));
		// placeNewPiece('e', 8, new King(board, this, Color.BLACK));
		// placeNewPiece('f', 8, new Bishop(board, this, Color.BLACK));
		// placeNewPiece('g', 8, new Knight(board, this, Color.BLACK));
		// placeNewPiece('h', 8, new Rook(board, this, Color.BLACK));
		// placeNewPiece('a', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('b', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('c', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('d', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('e', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('f', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('g', 7, new Pawn(board, this, Color.BLACK));
		// placeNewPiece('h', 7, new Pawn(board, this, Color.BLACK));
	}
}
