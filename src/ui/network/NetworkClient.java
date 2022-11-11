package ui.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;

public class NetworkClient {

	private Terminal localTerminal;
	private Socket client;

	public NetworkClient(Terminal localTerminal, Socket client) {
		this.localTerminal = localTerminal;
		this.client = client;
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
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			localTerminal.finish(chessMatch, capturedPieces, winner);
		}
	}

	private void exceptionMessage(RuntimeException e) {
		localTerminal.exceptionMessage(e);
	}

	private void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		localTerminal.update(chessMatch, capturedPieces, possibleMoves);
	}

	@SuppressWarnings("unchecked")
	public void start() {
		try (ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
				ObjectOutputStream saida = new ObjectOutputStream(client.getOutputStream())) {

			while (true) {

				Object[] obj = (Object[]) entrada.readUnshared();
//				Object[] obj = (Object[]) entrada.readObject();

				switch ((Description) obj[0]) {
					case READ_SOURCE_POSITION -> {
						saida.flush();
						saida.writeObject(readSourcePosition((ChessMatch) obj[1], (List<ChessPiece>) obj[2]));
					}

					case READ_TARGET_POSITION -> {
						saida.flush();
						saida.writeObject(readTargetPosition((ChessMatch) obj[1], (List<ChessPiece>) obj[2],
								(boolean[][]) obj[3]));
					}

					case CHOSE_PIECE_TYPE_TO_PROMOTION -> {
						saida.flush();
						saida.writeObject(chosePieceTypeToPromotion());
					}

					case MESSAGE -> message((String) obj[1]);

					case FINISH -> finish((ChessMatch) obj[1], (List<ChessPiece>) obj[2], (Color) obj[3]);

					case EXCEPTION -> exceptionMessage((RuntimeException) obj[1]);

					case UPDATE -> update((ChessMatch) obj[1], (List<ChessPiece>) obj[2], (boolean[][]) obj[3]);

					default -> throw new ChessException("Tipo de pacote inválido");
				}
				obj[1] = null;
				// amor e carinho
			}
		} catch (IOException | ClassNotFoundException | ChessException e) {
			localTerminal.exceptionMessage(e);
			System.exit(3);
		}
	}

}
