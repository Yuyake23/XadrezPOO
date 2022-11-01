package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	public King(Board board, ChessMatch chessMatch, Color color) {
		super(board, chessMatch, color);
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Position position) {
		ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
		return chessPiece == null || !chessPiece.getColor().equals(this.getColor());
	}

	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] getAllPossibleMoves() {
		boolean[][] pm = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

//		int v1[] = { +1, -1 };
//		for (int i : v1) {
//			p.setValues(position.getRow() + i, position.getColumn());
//			if (getBoard().positionExists(p) && canMove(p))
//				pm[p.getRow()][p.getColumn()] = true;
//
//			p.setValues(position.getRow(), position.getColumn() + i);
//			if (getBoard().positionExists(p) && canMove(p))
//				pm[p.getRow()][p.getColumn()] = true;
//
//			p.setValues(position.getRow() + i, position.getColumn() + i);
//			if (getBoard().positionExists(p) && canMove(p))
//				pm[p.getRow()][p.getColumn()] = true;
//
//			p.setValues(position.getRow() + i, position.getColumn() - i);
//			if (getBoard().positionExists(p) && canMove(p))
//				pm[p.getRow()][p.getColumn()] = true;
//		}

		// north
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// south
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// east
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// west
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// nw
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// ne
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// sw
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// se
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p))
			pm[p.getRow()][p.getColumn()] = true;

		// #specialmove castling
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			// #specialmove castling kingside rook
			if (testRookCastling(new Position(position.getRow(), position.getColumn() + 3))) {
				if (getBoard().piece(position.getRow(), position.getColumn() + 1) == null
						&& getBoard().piece(position.getRow(), position.getColumn() + 2) == null) {
					pm[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			// #specialmove castling queenside rook
			if (testRookCastling(new Position(position.getRow(), position.getColumn() - 4))) {
				if (getBoard().piece(position.getRow(), position.getColumn() - 1) == null
						&& getBoard().piece(position.getRow(), position.getColumn() - 2) == null
						&& getBoard().piece(position.getRow(), position.getColumn() - 3) == null) {
					pm[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}

		return pm;
	}
}
