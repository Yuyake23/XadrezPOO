package ui;

import java.util.List;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public abstract class Terminal {
	
	protected Color playerColor;

	public Terminal(Color playerColor) {
		this.playerColor = playerColor;
	}
	
	public abstract ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces);

	public abstract ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves);

	public abstract String chosePieceTypeToPromotion();

	public abstract void message(String s);

	public abstract void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner);

}
