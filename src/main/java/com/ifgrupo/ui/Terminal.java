package com.ifgrupo.ui;

import java.util.List;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.Color;

public abstract class Terminal {

	protected String name;
	protected Color playerColor;

	public Terminal(Color playerColor, String name) {
		this.playerColor = playerColor;
		this.name = name;
	}

	public abstract String readSourcePosition();

	public abstract String readTargetPosition();

	public abstract String chosePieceTypeToPromotion();

	public abstract void message(String s);

	public abstract void exceptionMessage(Exception e);

	public abstract void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner);

	public abstract void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves);

	public String getName() {
		return name;
	}

	public Color getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}
}
