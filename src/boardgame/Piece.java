package boardgame;

import java.io.Serial;
import java.io.Serializable;

import chess.ChessMatch;

public abstract class Piece implements Serializable{
	@Serial
	private static final long serialVersionUID = -6453738799351240322L;
	
	protected Position position;
	private Board board;

	public Piece(Board board) {
		this.board = board;
		this.position = null;
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
	
	public abstract boolean[][] getPossibleMoves();
	
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
