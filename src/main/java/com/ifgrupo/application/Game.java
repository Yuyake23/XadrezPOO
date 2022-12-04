package com.ifgrupo.application;

import com.ifgrupo.chess.ChessMatch;

public abstract class Game {
	
	protected ChessMatch chessMatch;

	public abstract String chosePieceTypeToPromotion();

	public abstract void start();
	
	public void save() {
//		chessMatch.getMoveDeque();
	}

}
