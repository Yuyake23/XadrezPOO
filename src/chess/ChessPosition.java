package chess;

import java.io.Serial;
import java.io.Serializable;

import boardgame.Position;

public class ChessPosition implements Serializable {
	@Serial
	private static final long serialVersionUID = -5903965210856179209L;
	
	private char column;
	private int row;

	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8)
			throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");

		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}

	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char) (position.getColumn() + 'a'), 8 - position.getRow());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ChessPosition(column, row);
	}

	@Override
	public String toString() {
		return "" + column + row;
	}
}
