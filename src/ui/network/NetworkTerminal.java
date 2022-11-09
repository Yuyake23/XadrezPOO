package ui.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;

public class NetworkTerminal extends Terminal {

	private ServerSocket host;
	private Socket client;

	public NetworkTerminal(Color playerColor, String name, ServerSocket host, Socket client) {
		super(playerColor, name);
		this.host = host;
		this.client = client;
	}

	@Override
	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void message(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		// TODO Auto-generated method stub

	}

}
