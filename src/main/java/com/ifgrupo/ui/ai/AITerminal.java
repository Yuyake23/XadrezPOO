package com.ifgrupo.ui.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ifgrupo.boardgame.Piece;
import com.ifgrupo.boardgame.Position;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class AITerminal extends Terminal {

	private Random r = new Random();
	private ChessPosition source;
	private ChessPosition target;

	public AITerminal(Color playerColor, String name) {
		super(playerColor, name);
	}

	@Override
	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		List<Piece> piecesOnTheBoard = chessMatch.getPiecesOnTheBoard();
		List<ChessPiece> movablePieces = new ArrayList<>();

		for (int i = 0; i < piecesOnTheBoard.size(); i++) {
			if (piecesOnTheBoard.get(i) instanceof ChessPiece k && k.getColor() == super.playerColor
					&& k.isThereAnyPossibleMove())
				movablePieces.add(k);
		}

		chooseMoviment(movablePieces);

		System.out.printf("Movimento: %s para %s%n", source, target);
		return source;
	}

	private void chooseMoviment(List<ChessPiece> movablePieces) {
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
	public ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves) {
		return this.target;
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
		// TODO Auto-generated method stub

	}

}
