package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
	protected Color color;
	protected int moveCount;

	public ChessPiece(Board board, ChessMatch chessMatch, Color color) {
		super(board, chessMatch);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void increseMoveCount() {
		moveCount++;
	}

	public void decreseMoveCount() {
		moveCount--;
	}

	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}

	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && !p.getColor().equals(color);
	}

}
