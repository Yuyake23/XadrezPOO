package com.ifgrupo.ui.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;
import com.ifgrupo.ui.bash.UI;
import com.ifgrupo.ui.network.enums.Description;

public class NetworkTerminal extends Terminal {

	private ServerSocket host;
	private Socket client;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;

	public NetworkTerminal(Color playerColor, String name, ServerSocket host, Socket client) {
		super(playerColor, name);
		this.host = host;
		this.client = client;
		try {
			this.saida = new ObjectOutputStream(client.getOutputStream());
			this.entrada = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			System.err.println("\nDeu ruim aqui no NetworkTerminal:");
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		ChessPosition chessPosition = null;
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.SOURCE_POSITION, chessMatch, capturedPieces });

			Object[] obj = (Object[]) entrada.readObject();
			if ((Description) obj[0] == Description.SOURCE_POSITION) {
				chessPosition = (ChessPosition) obj[1];
			}
		} catch (IOException | ClassNotFoundException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
		return chessPosition;
	}

	@Override
	public ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves) {
		ChessPosition chessPosition = null;
		try {
			saida.reset();
			saida.writeUnshared(
					new Object[] { Description.TARGET_POSITION, chessMatch, capturedPieces, possibleMoves });

			Object[] obj = (Object[]) entrada.readObject();
			if ((Description) obj[0] == Description.TARGET_POSITION) {
				chessPosition = (ChessPosition) obj[1];
			}
		} catch (IOException | ClassNotFoundException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
		return chessPosition;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		String pieceType = null;
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.PIECE_TYPE_TO_PROMOTION });

			Object[] obj = (Object[]) entrada.readObject();
			if ((Description) obj[0] == Description.PIECE_TYPE_TO_PROMOTION) {
				pieceType = (String) obj[1];
			} else {
				System.exit(1);
			}
		} catch (IOException | ClassNotFoundException | NullPointerException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
		return pieceType;
	}

	@Override
	public void message(String s) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.MESSAGE, s });
		} catch (IOException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.MESSAGE, chessMatch, capturedPieces, winner });

			releaseResources();
		} catch (IOException e) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void exceptionMessage(Exception e) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
		} catch (IOException e1) {
			releaseResources();
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		try {
			saida.reset();
			saida.writeUnshared(new Object[] { Description.UPDATE, chessMatch, capturedPieces, possibleMoves });
		} catch (IOException e) {
			releaseResources();
			System.err.println(UI.RED + "O cliente caiu" + UI.RESET);
			System.exit(3);
		}
	}

	private void releaseResources() {
		try {
			entrada.close();
			saida.close();
			client.close();
			host.close();
		} catch (IOException e) {
			System.out.println("Falha ao liberar recursos: " + e.getMessage());
		}
	}
	
}
