package ui.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;
import ui.network.enums.Description;

public class NetworkClient {

	private Terminal localTerminal;
	private Socket client;

	public NetworkClient(Terminal localTerminal, Socket client) {
		this.localTerminal = localTerminal;
		this.client = client;
	}

	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) throws SocketException {
		return localTerminal.readSourcePosition(chessMatch, capturedPieces);
	}

	public ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves) throws SocketException {
		return localTerminal.readTargetPosition(chessMatch, capturedPieces, possibleMoves);
	}

	public String chosePieceTypeToPromotion() throws ClassNotFoundException, NullPointerException, IOException {
		return localTerminal.chosePieceTypeToPromotion();
	}

	public void message(String s) throws SocketException {
		localTerminal.message(s);
	}

	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) throws SocketException {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			localTerminal.finish(chessMatch, capturedPieces, winner);
		}
	}

	private void exceptionMessage(RuntimeException e) throws SocketException {
		localTerminal.exceptionMessage(e);
	}

	private void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) throws SocketException {
		localTerminal.update(chessMatch, capturedPieces, possibleMoves);
	}

	@SuppressWarnings("unchecked")
	public void start() throws SocketException {

		try (ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
				ObjectOutputStream saida = new ObjectOutputStream(client.getOutputStream())) {
			while (true) {

//				Object[] obj = (Object[]) entrada.readUnshared();
				Object[] obj = (Object[]) entrada.readObject();

				try {
					switch ((Description) obj[0]) {
						case SOURCE_POSITION -> {
							ChessPosition sourcePosition = readSourcePosition((ChessMatch) obj[1],
									(List<ChessPiece>) obj[2]);
//							saida.flush();
							saida.writeObject(new Object[] { Description.SOURCE_POSITION, sourcePosition });
						}

						case TARGET_POSITION -> {
							ChessPosition targetPosition = readTargetPosition((ChessMatch) obj[1],
									(List<ChessPiece>) obj[2], (boolean[][]) obj[3]);
//							saida.flush();
							saida.writeObject(new Object[] { Description.TARGET_POSITION, targetPosition });
						}

						case PIECE_TYPE_TO_PROMOTION -> {
							String pieceType = chosePieceTypeToPromotion();
//							saida.flush();
							saida.writeObject(new Object[] { Description.PIECE_TYPE_TO_PROMOTION, pieceType });
						}

						case UPDATE -> {
							update((ChessMatch) obj[1], (List<ChessPiece>) obj[2], (boolean[][]) obj[3]);
						}

						case MESSAGE -> {
							message((String) obj[1]);
						}

						case EXCEPTION -> {
							exceptionMessage((RuntimeException) obj[1]);
						}

						case FINISH -> {
							finish((ChessMatch) obj[1], (List<ChessPiece>) obj[2], (Color) obj[3]);
							System.exit(0);
						}

						default -> throw new ChessException("Tipo de pacote inválido");
					}
				} catch (Exception e) {
					saida.writeObject(new Object[] { Description.EXCEPTION, e });
				}
			}
		} catch (SocketException e) {
			localTerminal.exceptionMessage(new SocketException("O servidor caiu."));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
