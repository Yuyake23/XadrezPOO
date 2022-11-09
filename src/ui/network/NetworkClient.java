package ui.network;

import java.util.List;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;

public class NetworkClient {

	private Terminal localTerminal;

	public NetworkClient(Terminal localTerminal) {
		this.localTerminal = localTerminal;
	}

	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		return localTerminal.readSourcePosition(chessMatch, capturedPieces);
	}

	public ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves) {
		return localTerminal.readTargetPosition(chessMatch, capturedPieces, possibleMoves);
	}

	public String chosePieceTypeToPromotion() {
		return localTerminal.chosePieceTypeToPromotion();
	}

	public void message(String s) {
		localTerminal.message(s);
	}

	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		localTerminal.finish(chessMatch, capturedPieces, winner);
	}

}
