package com.ifgrupo.ui.graphic;

import java.util.List;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class GraphicTerminal extends Terminal {

	public GraphicTerminal(Color playerColor, String name) {
		super(playerColor, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String readSourcePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String readTargetPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void message(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionMessage(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		// TODO Auto-generated method stub

	}

}
