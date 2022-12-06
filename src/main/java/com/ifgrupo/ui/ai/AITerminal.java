package com.ifgrupo.ui.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.ifgrupo.boardgame.Piece;
import com.ifgrupo.boardgame.Position;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

@SuppressWarnings("unused")
public class AITerminal extends Terminal {

	private static final int pawnValue = 10;
	private static final int knightValue = 30;
	private static final int bishopValue = 30;
	private static final int rookValue = 50;
	private static final int queenValue = 90;
	private static final int kingValue = 900;
	
	
	private ChessMatch chessMatch;
	private List<ChessPiece> capturedPieces;
	private boolean[][] possibleMoves;
	private ChessPosition source;
	private ChessPosition target;
	
	private Stack<Movement> movements = new Stack<>();
	private Random r = new Random();
	private int depth;
	
	public AITerminal(Color playerColor, int depth, String name) {
		super(playerColor, name);
		this.depth = depth;
	}

	@Override
	public String readSourcePosition() {
		List<Piece> piecesOnTheBoard = chessMatch.getPiecesOnTheBoard();
		List<ChessPiece> movablePieces = new ArrayList<>();

		for (int i = 0; i < piecesOnTheBoard.size(); i++) {
			if (piecesOnTheBoard.get(i) instanceof ChessPiece k && k.getColor() == super.playerColor
					&& k.isThereAnyPossibleMove())
				movablePieces.add(k);
		}

		chooseMoviment(depth, movablePieces);

		System.out.printf("Movimento: %s para %s%n", source, target);
		return source.toString();
	}

	private void chooseMoviment(int n, List<ChessPiece> movablePieces) {
		if (n == 0)
			return;

		ChessPiece chosenPiece = movablePieces.get(r.nextInt(movablePieces.size()));
		List<ChessPosition> possibleMoves = possibleMovesMatrixToList(chosenPiece.getPossibleMoves());

		this.source = chosenPiece.getChessPosition();
		this.target = possibleMoves.get(r.nextInt(possibleMoves.size()));
	}

	private List<ChessPosition> possibleMovesMatrixToList(boolean[][] pm) {
		List<ChessPosition> list = new ArrayList<>();
		for (int i = 0; i < pm.length; i++) {
			for (int j = 0; j < pm[i].length; j++) {
				if (pm[i][j])
					list.add(ChessPosition.fromPosition(new Position(i, j)));
			}
		}
		return list;
	}

	@Override
	public String readTargetPosition() {
		return this.target.toString();
	}

	@Override
	public String chosePieceTypeToPromotion() {
		return String.valueOf("BNQR".charAt((int) (Math.random() * 4)));
	}

	@Override
	public void message(String s) {
		System.out.println(s);

	}

	@Override
	public void exceptionMessage(Exception e) {
		e.printStackTrace();
		System.exit(-5);
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		System.out.println("CABOU");

	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		this.chessMatch = chessMatch;
		this.capturedPieces = capturedPieces;
		this.possibleMoves = possibleMoves;
	}

}
