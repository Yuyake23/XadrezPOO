package com.ifgrupo.chess;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import com.ifgrupo.boardgame.Position;

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

	public ChessPosition(String position) {
		try {
			char column = position.charAt(0);
			int row = Character.getNumericValue(position.charAt(1));

			if (column < 'a' || column > 'h' || row < 1 || row > 8)
				throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");

			this.column = column;
			this.row = row;
		} catch (ChessException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
		}
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

	public static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char) (position.getColumn() + 'a'), 8 - position.getRow());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChessPosition other = (ChessPosition) obj;
		return column == other.column && row == other.row;
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
