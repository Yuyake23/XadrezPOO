package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	public Pawn(Board board, ChessMatch chessMatch, Color color) {
		super(board, chessMatch, color);
	}

	@Override
	public Pawn clone() {
		Pawn pawn = new Pawn(this.getBoard(), chessMatch, this.getColor());
		pawn.moveCount = this.moveCount;
		return pawn;
	}

	@Override
	public String toString() {
		return "P";
	}

	@Override
	public boolean[][] getAllPossibleMoves() {
		boolean[][] pm = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, this.position.getColumn());
		// wildcard modifier it will be negative if the piece color is white and
		// positive if not
		// This will help to know which way the piece should move
		int wm = this.getColor() == Color.WHITE ? -1 : 1;

		// in front
		p.setRow(this.position.getRow() + wm);
		if (!getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			if (this.getMoveCount() == 0) {
				p.setRow(this.position.getRow() + wm * 2);
				if (!getBoard().thereIsAPiece(p))
					pm[p.getRow()][p.getColumn()] = true;
			}
		}

		// in diagonal
		p.setRow(this.position.getRow() + wm);
		// left
		p.setColumn(this.position.getColumn() + wm);
		if (getBoard().positionExists(p) && getBoard().thereIsAPiece(p)
				&& ((ChessPiece) getBoard().piece(p)).getColor() != this.getColor()) {
			pm[p.getRow()][p.getColumn()] = true;
		}
		// right
		p.setColumn(this.position.getColumn() - wm);
		if (getBoard().positionExists(p) && getBoard().thereIsAPiece(p)
				&& ((ChessPiece) getBoard().piece(p)).getColor() != this.getColor()) {
			pm[p.getRow()][p.getColumn()] = true;
		}

		// #specialmove en passant
		if (chessMatch.getEnPassantVulnerable() != null) {
//			System.out.println("tem peça vulneralvel a en passant");
			if (position.getRow() == 3 || position.getRow() == 4) {
				p.setValues(position.getRow(), position.getColumn() - 1); // left
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)
						&& chessMatch.getEnPassantVulnerable() == getBoard().piece(p)) {
					pm[p.getRow() + wm][p.getColumn()] = true;
				}
				p.setValues(position.getRow(), position.getColumn() + 1); // right
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)
						&& chessMatch.getEnPassantVulnerable() == getBoard().piece(p)) {
					pm[p.getRow() + wm][p.getColumn()] = true;
				}
			}
		}

		return pm;
	}
}
