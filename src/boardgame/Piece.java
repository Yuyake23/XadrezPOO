package boardgame;

import chess.ChessMatch;

public abstract class Piece {
	protected Position position;
	protected ChessMatch chessMatch;
	private Board board;

	public Piece(Board board, ChessMatch chessMatch) {
		this.board = board;
		this.position = null;
		this.chessMatch = chessMatch;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	protected Board getBoard() {
		return board;
	}

	public abstract boolean[][] getAllPossibleMoves();
	
	// hook method
	public boolean[][] getPossibleMoves(){
		boolean[][] pm = getAllPossibleMoves();
		chessMatch.validadePossibleMoves(pm, position);
		return pm;
	}

	// hook method
	public boolean possibleMove(Position position) {
		return getAllPossibleMoves()[position.getRow()][position.getColumn()];
	}

	public boolean isThereAnyPossibleMove() {
		boolean possibleMoves[][] = getPossibleMoves();

		for (int i = 0; i < possibleMoves.length; i++)
			for (int j = 0; j < possibleMoves[0].length; j++)
				if (possibleMoves[i][j])
					return true;

		return false;
	}

}
