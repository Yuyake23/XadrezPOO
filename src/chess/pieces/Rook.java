package chess.pieces;

import java.io.Serial;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {
	@Serial
	private static final long serialVersionUID = 2737208292316407438L;

	public Rook(Board board, ChessMatch chessMatch, Color color) {
		super(board, chessMatch, color);
	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] getAllPossibleMoves() {
		boolean[][] pm = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		// above
		p.setValues(this.position.getRow() - 1, this.position.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		// bellow
		p.setValues(this.position.getRow() + 1, this.position.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		// left
		p.setValues(this.position.getRow(), this.position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;

		// rigth
		p.setValues(this.position.getRow(), this.position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			pm[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			pm[p.getRow()][p.getColumn()] = true;
		return pm;
	}
}
