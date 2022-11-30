package com.ifgrupo.ui.ai;

import java.util.ArrayList;
import java.util.List;

import com.ifgrupo.boardgame.Piece;
import com.ifgrupo.boardgame.Position;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class AITerminal extends Terminal {

	private ChessPosition target;

	public AITerminal(Color playerColor, String name) {
		super(playerColor, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		// pega as pecas que podem se mover
		System.out.println("VEZ DA IA");
		if (chessMatch == null)
			System.out.println("chessMatch NULL");
		List<ChessPiece> cps = new ArrayList<>();
		try {
			List<Piece> todasPecas = chessMatch.getPiecesOnTheBoard();
			for (int i = 0; i < todasPecas.size(); i++) {
				if (todasPecas.get(i) instanceof ChessPiece k && k.getColor() == super.playerColor
						&& k.isThereAnyPossibleMove())
					cps.add(k);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("PEGOU AS PEÇAS QUE PODEM MOVER");
		ChessPiece cp = cps.get((int) (Math.random() * cps.size()));

		boolean[][] pm = cp.getPossibleMoves();

		List<ChessPosition> pml = new ArrayList<>();
		for (int i = 0; i < pm.length; i++) {
			for (int j = 0; j < pm[i].length; j++) {
				if (pm[i][j])
					pml.add(ChessPosition.fromPosition(new Position(i, j)));
			}
		}

		this.target = pml.get((int) (Math.random() * pml.size()));

		System.out.println(cp.getChessPosition());
		return cp.getChessPosition();
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
