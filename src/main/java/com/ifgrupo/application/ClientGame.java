package com.ifgrupo.application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.List;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
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
				try {
					Object[] obj = (Object[]) entrada.readObject();
					switch ((Description) obj[0]) {
						case SOURCE_POSITION -> {
							saida.writeObject(new Object[] { Description.SOURCE_POSITION,
									this.localTerminal.readSourcePosition() });
						}
						case TARGET_POSITION -> {
							saida.writeObject(new Object[] { Description.TARGET_POSITION,
									this.localTerminal.readTargetPosition() });
						}
						case PIECE_TYPE_TO_PROMOTION -> {
							String pieceType = this.chosePieceTypeToPromotion();
							saida.writeObject(new Object[] { Description.PIECE_TYPE_TO_PROMOTION, pieceType });
						}
						case UPDATE -> {
							this.localTerminal.update((ChessMatch) obj[1], (List<ChessPiece>) obj[2],
									(boolean[][]) obj[3]);
						}
						case MESSAGE -> {
							this.localTerminal.message((String) obj[1]);
						}
						case EXCEPTION -> {
							this.localTerminal.exceptionMessage((RuntimeException) obj[1]);
						}
						case FINISH -> {
							super.chessMatch = (ChessMatch) obj[1];
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
				} catch (InputMismatchException | NullPointerException e) {
					saida.writeObject(new Object[] { Description.EXCEPTION, e });
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
