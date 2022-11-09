package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;
import ui.bash.BashTerminal;
import ui.graphic.GraphicTerminal;
import ui.network.NetworkTerminal;

public class Program {

	private static ChessMatch chessMatch = new ChessMatch();
	private static List<ChessPiece> capturedPieces = new ArrayList<>();

	private static Terminal whitePlayer;
	private static Terminal blackPlayer;

	private static Terminal currentPlayer;

	public static void main(String[] args) {
		boolean rede = false;
		char hostOrServer = ' ';

		/*
		 * -nh : Host -nc : Client
		 */

		if (args.length == 0) {
			System.err.println("Quais são os argumentos, querido?");
			System.exit(1);
		} else if (args[0].contains("-n")) {
			rede = true;
			if (args[0].equalsIgnoreCase("-nh")) {
				hostOrServer = 'h';
			} else if (args[0].equals("-nc")) {
				hostOrServer = 'c';
			}
		}

		if (!rede) {
			configureLocalGame(args);
			localGame();
		} else {
			switch (hostOrServer) {
				case 'h' -> {
					configureNetworkGameAsHost(args);
					localGame();
				}
				case 'c' -> {
					configureNetworkGameAsClient(args);
					networkGame();
				}

				default -> throw new IllegalArgumentException("Unexpected value: " + hostOrServer);
			}
		}
	}

	private static void networkGame() {
		// TODO Auto-generated method stub
		currentPlayer = whitePlayer;
	}

	private static void configureNetworkGameAsClient(String[] args) {
		// TODO Auto-generated method stub

	}

	private static void configureNetworkGameAsHost(String[] args) {
		if (args[1].equalsIgnoreCase("-p1") && args[2].equalsIgnoreCase("-b")) {
			Program.whitePlayer = new BashTerminal(Color.WHITE, args[3]);
		} else if (args[2].equalsIgnoreCase("-g")) {
			Program.whitePlayer = new GraphicTerminal(Color.WHITE, args[3]);
		} else {
			System.out.println("-p1 (-b or -g) namePlayer1");
			System.exit(1);
		}
		// TODO connect client and get its name
		try {
			ServerSocket host = new ServerSocket(0);
			whitePlayer.message("Servidor " + host.getInetAddress().getHostAddress() + ":" + host.getLocalPort()
					+ " esperando cliente");

			Socket client = host.accept();
			System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());

			ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
			String clientName = entrada.readObject().toString();
			entrada.close();

			whitePlayer.message("Nome do cliente: " + clientName);
			Program.blackPlayer = new NetworkTerminal(Color.BLACK, clientName, host, client);

		} catch (IOException | ClassNotFoundException e) {
			whitePlayer.message("Deu problema com a conexão com o cliente");
			e.printStackTrace();
			System.exit(2);
		}
	}

	private static void configureLocalGame(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-p1")) {
					System.out.println("configurando p1");
					i++;
					if (args[i].equalsIgnoreCase("-b")) {
						i++;
						Program.whitePlayer = new BashTerminal(Color.WHITE, args[i]);
					} else if (args[i].equalsIgnoreCase("-g")) {
						i++;
						Program.whitePlayer = new GraphicTerminal(Color.WHITE, args[i]);
					} else {
						System.out.println("-p1 (-b or -g) namePlayer1 -p2 (-b or -g) namePlayer2");
					}
				} else if (args[i].equalsIgnoreCase("-p2")) {
					System.out.println("configurando p2");
					i++;
					if (args[i].equalsIgnoreCase("-b")) {
						i++;
						Program.blackPlayer = new BashTerminal(Color.BLACK, args[i]);
					} else if (args[i].equalsIgnoreCase("-g")) {
						i++;
						Program.blackPlayer = new GraphicTerminal(Color.BLACK, args[i]);
					} else {
						System.out.println("-p1 (-b or -g) namePlayer1 -p2 (-b or -g) namePlayer2");
						System.exit(1);
					}
				} else {
					System.out.println("Confira os argumentos: " + Arrays.toString(args));
					System.exit(1);
				}
			}
		} catch (NullPointerException e) {
			System.out.println("-p1 (-b or -g) namePlayer1 -p2 (-b or -g) namePlayer2");
			System.exit(1);
		}
	}

	private static void localGame() {
		currentPlayer = whitePlayer;
		while (!chessMatch.getCheckMate()) {
			try {
				if (chessMatch.getCurrentPlayer() == Color.WHITE) {
					currentPlayer = whitePlayer;
				} else {
					currentPlayer = blackPlayer;
				}

				ChessPosition source, target;
				ChessPiece capturedPiece;
				boolean possibleMovies[][];

				source = currentPlayer.readSourcePosition(chessMatch, capturedPieces);

				possibleMovies = chessMatch.possibleMovies(source);

				target = currentPlayer.readTargetPosition(chessMatch, capturedPieces, possibleMovies);

				capturedPiece = chessMatch.performChessMove(source, target);

				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);

			} catch (ChessException e) {
				currentPlayer.message(e.getMessage());
			} catch (InputMismatchException e) {
				currentPlayer.message(e.getMessage());
			}
		}

		whitePlayer.finish(chessMatch, capturedPieces, chessMatch.getWinner());
		blackPlayer.finish(chessMatch, capturedPieces, chessMatch.getWinner());
	}

	public static String chosePieceTypeToPromotion() {
		while (true) {
			String type = currentPlayer.chosePieceTypeToPromotion();
			if (type.length() == 1 && "BNQR".contains(type)) {
				return type;
			} else {
				currentPlayer.message("Invalid Type! ");
			}
		}
	}
}
