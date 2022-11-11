package application;

import java.io.IOException;
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
import ui.network.NetworkClient;
import ui.network.NetworkTerminal;

public class Program {

	private static ChessMatch chessMatch = new ChessMatch();
	private static List<ChessPiece> capturedPieces = new ArrayList<>();

	private static Terminal whitePlayer;
	private static Terminal blackPlayer;

	private static Terminal currentPlayer;
	private static NetworkClient networkClient;

	/**
	 * TODO Estudar interface Serializable
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		boolean rede = false;
		char hostOrClient = ' ';

		/*
		 * -nh : Host -nc : Client
		 */

		if (args.length == 0) {
			System.err.println("Quais são os argumentos, querido?");
			System.exit(1);
		} else if (args[0].contains("-n")) {
			rede = true;
			if (args[0].equalsIgnoreCase("-nh")) {
				hostOrClient = 'h';
			} else if (args[0].equals("-nc")) {
				hostOrClient = 'c';
			}
		}

		if (!rede) {
			configureLocalGame(args);
			localGame();
		} else {
			switch (hostOrClient) {
				case 'h' -> {
					configureNetworkGameAsHost(args);
					localGame();
				}
				case 'c' -> {
					configureNetworkGameAsClient(args);
					networkGame();
				}

				default -> throw new IllegalArgumentException("Unexpected value: " + hostOrClient);
			}
		}
	}

	private static void networkGame() {
		networkClient.start();
	}

	private static void configureNetworkGameAsClient(String[] args) {
		try {
			Terminal localTerminal = null;
			String socket = args[1];
			String name = null;
			if (!args[2].equalsIgnoreCase("-p2")) {
				System.out.println("-nc ip:porta -p2 (-b or -g) name");
				System.exit(1);
			} else {
				name = args[4];
				if (args[3].equalsIgnoreCase("-b")) {
					localTerminal = new BashTerminal(Color.BLACK, name);
				} else if (args[3].equalsIgnoreCase("-g")) {
					localTerminal = new GraphicTerminal(Color.BLACK, name);
				} else {
					System.out.println("-nc ip:porta -p2 (-b or -g) name");
					System.exit(1);
				}
			}
			Socket client = new Socket(socket.split(":")[0], Integer.parseInt(socket.split(":")[1]));
//			ObjectOutputStream saida = new ObjectOutputStream(client.getOutputStream());
//			saida.writeObject(name);
//			saida.close();

			networkClient = new NetworkClient(localTerminal, client);

		} catch (IOException e) {
			System.out.println("Program.configureNetworkGameAsClient()");
			e.printStackTrace();
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			System.out.println("-nc ip:porta -p2 (-b or -g) name");
			System.exit(1);
		}
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

//			ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
//			String clientName = entrada.readObject().toString();
//			entrada.close();
			String clientName = "jubiscreia";

			whitePlayer.message("Nome do cliente: " + clientName);
			Program.blackPlayer = new NetworkTerminal(Color.BLACK, clientName, host, client);

		} catch (IOException e) {
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
				currentPlayer.update(chessMatch, capturedPieces, null);
				opponent(currentPlayer).update(chessMatch, capturedPieces, null);
				
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
				opponent(currentPlayer).update(chessMatch, capturedPieces, possibleMovies);
				
				target = currentPlayer.readTargetPosition(chessMatch, capturedPieces, possibleMovies);
				
				capturedPiece = chessMatch.performChessMove(source, target);

				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);

			} catch (InputMismatchException | NumberFormatException | ChessException e) {
				currentPlayer.exceptionMessage(e);
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
	
	private static Terminal opponent(Terminal a) {
		return a == blackPlayer ? whitePlayer : blackPlayer;
	}
}
