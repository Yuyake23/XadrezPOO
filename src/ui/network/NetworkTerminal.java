package ui.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;
import ui.network.enums.Description;

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
			System.err.println("\nDeu ruim aqui:");
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		ChessPosition chessPosition = null;
		try {
//			saida.flush();
			saida.reset();
			saida.writeUnshared(new Object[] { Description.SOURCE_POSITION, chessMatch, capturedPieces });
//			saida.writeObject(new Object[] { Description.READ_SOURCE_POSITION, chessMatch, capturedPieces });

			Object[] obj = (Object[]) entrada.readObject();
			if ((Description) obj[0] == Description.SOURCE_POSITION) {
				chessPosition = (ChessPosition) obj[1];
			}
		} catch (IOException | ClassNotFoundException e) {
			try {
				saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
//				saida.writeObject(new Object[] {Description.EXCEPTION, e});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
//			saida.flush();
			saida.reset();
			saida.writeUnshared(
					new Object[] { Description.TARGET_POSITION, chessMatch, capturedPieces, possibleMoves });
//			saida.writeObject(new Object[] { Description.READ_TARGET_POSITION, chessMatch, capturedPieces, possibleMoves });

			Object[] obj = (Object[]) entrada.readObject();
			if ((Description) obj[0] == Description.TARGET_POSITION) {
				chessPosition = (ChessPosition) obj[1];
			}
		} catch (IOException | ClassNotFoundException e) {
			try {
				saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
//				saida.writeObject(new Object[] {Description.EXCEPTION, e});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(3);
		}
		return chessPosition;
	}

	@Override
	public String chosePieceTypeToPromotion() throws IOException, ClassNotFoundException, NullPointerException {
		String pieceType = null;
		try {
//			saida.flush();
			saida.reset();
			saida.writeUnshared(new Object[] { Description.PIECE_TYPE_TO_PROMOTION });
//			saida.writeObject(new Object[] { Description.CHOSE_PIECE_TYPE_TO_PROMOTION });

			Object[] obj = (Object[]) entrada.readObject();
			if ((Description) obj[0] == Description.PIECE_TYPE_TO_PROMOTION) {
				pieceType = (String) obj[1];
			}
		} catch (IOException | ClassNotFoundException | NullPointerException e) {
			try {
				saida.reset();
				saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
//				saida.writeObject(new Object[] {Description.EXCEPTION, e});
			} catch (IOException e1) {
				throw e;
			}
			throw e;
		}
		return pieceType;
	}

	@Override
	public void message(String s) {
		try {
//			saida.flush();
			saida.reset();
			saida.writeUnshared(new Object[] { Description.MESSAGE, s });
//			saida.writeObject(new Object[] { Description.MESSAGE, s });
		} catch (IOException e) {
			try {
				saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
//				saida.writeObject(new Object[] {Description.EXCEPTION, e});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		try {
//			saida.flush();
			saida.reset();
			saida.writeUnshared(new Object[] { Description.MESSAGE, chessMatch, capturedPieces, winner });
//			saida.writeObject(new Object[] { Description.MESSAGE, chessMatch, capturedPieces, winner });

			entrada.close();
			saida.close();
			client.close();
			host.close();
		} catch (IOException e) {
			try {
				saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
//				saida.writeObject(new Object[] {Description.EXCEPTION, e});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.exit(3);
		}
	}

	@Override
	public void exceptionMessage(Exception e) {
		try {
//			saida.flush();
			saida.reset();
			saida.writeUnshared(new Object[] { Description.EXCEPTION, e });
//			saida.writeObject(new Object[] { Description.EXCEPTION, e });
		} catch (IOException e1) {
			e.printStackTrace();
			System.exit(3);
		}

	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		try {
//			saida.flush();
			saida.reset();
			saida.writeUnshared(new Object[] { Description.UPDATE, chessMatch, capturedPieces, possibleMoves });
//			saida.writeObject(new Object[] { Description.READ_TARGET_POSITION, chessMatch, capturedPieces, possibleMoves });
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(3);
		}
	}

}
