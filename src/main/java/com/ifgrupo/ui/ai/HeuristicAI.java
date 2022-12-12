package com.ifgrupo.ui.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ifgrupo.boardgame.Piece;
import com.ifgrupo.boardgame.Position;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.pieces.Bishop;
import com.ifgrupo.chess.pieces.Knight;
import com.ifgrupo.chess.pieces.Pawn;
import com.ifgrupo.chess.pieces.PieceType;
import com.ifgrupo.chess.pieces.Queen;
import com.ifgrupo.chess.pieces.Rook;

public class HeuristicAI {

	private static final int pawnValue = 10;
	private static final int knightValue = 30;
	private static final int bishopValue = 30;
	private static final int rookValue = 50;
	private static final int queenValue = 90;
	private static final int kingValue = 900;

	private int depth;
	private int rho;

	private Random r;

	private HeuristicAI next;

	public HeuristicAI(int depth, int rho) {
		this.depth = depth;
		this.rho = rho;

		this.r = new Random();
		if (depth != 1)
			this.next = new HeuristicAI(depth - 1, rho);
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getRho() {
		return rho;
	}

	public void setRho(int rho) {
		this.rho = rho;
	}

	public Random getR() {
		return r;
	}

	public void setR(Random r) {
		this.r = r;
	}

	public HeuristicAI() {
		this(10, 1000);
	}

	public Movement chooseMovement(final ChessMatch chessMatch) {
		ArrayList<Movement> movements = getMoviments(chessMatch);

		for (Movement movement : movements) {
			chooseMovement0(depth, chessMatch.clone(), movement, movement, true);
		}

		movements.sort((m1, m2) -> m1.getValue() - m2.getValue());

		movements.removeIf(x -> x.getValue() < movements.get(movements.size() - 1).getValue());

		Movement chosenMovement = movements.get(r.nextInt(movements.size()));

		return chosenMovement;
	}

	private void chooseMovement0(int n, ChessMatch cm, Movement moviment, Movement ancestralMovement, boolean myTurn) {
		if (cm.matchIsOver())
			return;
		ChessPiece piece = cm.performChessMove(moviment.getSourcePosition(), moviment.getTargetPosition(),
				PieceType.QUEEN);

		int v = pieceValue(piece);

		if (piece instanceof Pawn)
			v += 5;

		ancestralMovement.increseValue(myTurn ? v : -v);

		if (cm.getCheckMate()) {
			if (myTurn)
				ancestralMovement.increseValue(900);
			else
				ancestralMovement.increseValue(-900);
			return;
		}

		if (ancestralMovement.getValue() < this.rho)
			return;

		if (n != 1) {
			Movement nextPred = this.next.chooseMovement(cm);
			ancestralMovement.increseValue(-(int) (nextPred.getValue() * .8));
		}

	}

	private static ArrayList<Movement> getMoviments(ChessMatch cm) {
		List<ChessPiece> movablePieces = getMovablePieces(cm);
		ArrayList<Movement> movements = new ArrayList<>();
		
		for (ChessPiece s : movablePieces) {
			List<ChessPosition> possibleMoves = possibleMovesMatrixToList(s.getPossibleMoves());
			for (ChessPosition t : possibleMoves) {
				movements.add(new Movement(s.getChessPosition(), t));
			}
		}
		return movements;
	}

	private static List<ChessPiece> getMovablePieces(ChessMatch chessMatch) {
		List<Piece> piecesOnTheBoard = chessMatch.getPiecesOnTheBoard();
		List<ChessPiece> movablePieces = new ArrayList<>();

		for (int i = 0; i < piecesOnTheBoard.size(); i++) {
			if (piecesOnTheBoard.get(i) instanceof ChessPiece k && k.getColor() == chessMatch.getCurrentPlayer()
					&& k.isThereAnyPossibleMove()) {
				movablePieces.add(k);
			}
		}
		return movablePieces;
	}

	private static List<ChessPosition> possibleMovesMatrixToList(boolean[][] pm) {
		List<ChessPosition> list = new ArrayList<>();
		for (int i = 0; i < pm.length; i++) {
			for (int j = 0; j < pm[i].length; j++) {
				if (pm[i][j])
					list.add(ChessPosition.fromPosition(new Position(i, j)));
			}
		}
		return list;
	}

	public static int pieceValue(Piece capturedPiece) {
		return capturedPiece == null ? 0
				: capturedPiece instanceof Pawn ? pawnValue
						: capturedPiece instanceof Knight ? knightValue
								: capturedPiece instanceof Bishop ? bishopValue
										: capturedPiece instanceof Rook ? rookValue
												: capturedPiece instanceof Queen ? queenValue : kingValue;
	}
}
