package com.ifgrupo.ui.ai;

import java.util.List;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class AITerminal extends Terminal {

	private ChessMatch chessMatch;
//	private Terminal terminalVisual;

	private HeuristicAI heuristicAI;
	private Movement movement;

	public AITerminal(Color playerColor) {
		super(playerColor);

		this.heuristicAI = new HeuristicAI(6, 2000); // 6 2000
//		this.terminalVisual = new GraphicTerminal(playerColor);
	}

	@Override
	public String readSourcePosition() {
		this.movement = this.heuristicAI.chooseMovement(this.chessMatch);

		return this.movement.toString();
	}

	@Override
	public String readTargetPosition() {
		return movement.getTargetPosition().toString();
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
	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		this.chessMatch = chessMatch;
//		this.terminalVisual.update(chessMatch, capturedPieces, possibleMoves);
	}

}
