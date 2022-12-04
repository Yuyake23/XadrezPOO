package com.ifgrupo.application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;
import com.ifgrupo.ui.network.enums.Description;

public class ClientGame extends Game {

	private Terminal localTerminal;
	private Socket client;

	public ClientGame(Terminal localTerminal, Socket client) {
		this.localTerminal = localTerminal;
		this.client = client;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start() {
		try (ObjectInputStream entrada = new ObjectInputStream(this.client.getInputStream());
				ObjectOutputStream saida = new ObjectOutputStream(this.client.getOutputStream())) {
			while (true) {
				Object[] obj = (Object[]) entrada.readObject();
				switch ((Description) obj[0]) {
					case SOURCE_POSITION -> {
						ChessPosition sourcePosition = this.localTerminal.readSourcePosition((ChessMatch) obj[1],
								(List<ChessPiece>) obj[2]);
						saida.writeObject(new Object[] { Description.SOURCE_POSITION, sourcePosition });
					}
					case TARGET_POSITION -> {
						ChessPosition targetPosition = this.localTerminal.readTargetPosition((ChessMatch) obj[1],
								(List<ChessPiece>) obj[2], (boolean[][]) obj[3]);
						saida.writeObject(new Object[] { Description.TARGET_POSITION, targetPosition });
					}
					case PIECE_TYPE_TO_PROMOTION -> {
						String pieceType = this.chosePieceTypeToPromotion();
						saida.writeObject(new Object[] { Description.PIECE_TYPE_TO_PROMOTION, pieceType });
					}
					case UPDATE -> {
						chessMatch = (ChessMatch) obj[1];
						this.localTerminal.update(chessMatch, (List<ChessPiece>) obj[2], (boolean[][]) obj[3]);
					}
					case MESSAGE -> {
						this.localTerminal.message((String) obj[1]);
					}
					case EXCEPTION -> {
						this.localTerminal.exceptionMessage((RuntimeException) obj[1]);
					}
					case FINISH -> {
						this.localTerminal.finish((ChessMatch) obj[1], (List<ChessPiece>) obj[2], (Color) obj[3]);
						try {
							client.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.exit(0);
					}
					default -> throw new ChessException("Tipo de pacote inválido");
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String chosePieceTypeToPromotion() {
		return localTerminal.chosePieceTypeToPromotion();
	}

}
