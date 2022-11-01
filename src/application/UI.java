package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import chess.pieces.King;

public class UI {
	public enum FontType {
		PLAIN, BOLD, UNDERLINED, BRIGHT, BACKGROUND, BOLD_BRIGHT, BACKGROUNG_BRIGHT
	}

//	// Reset
//	public static final String RESET = "\033[0m"; // Text Reset
//
//	// Regular Colors
//	public static final String BLACK = "\033[0;30m"; // BLACK
//	public static final String RED = "\033[0;31m"; // RED
//	public static final String GREEN = "\033[0;32m"; // GREEN
//	public static final String YELLOW = "\033[0;33m"; // YELLOW
//	public static final String BLUE = "\033[0;34m"; // BLUE
//	public static final String PURPLE = "\033[0;35m"; // PURPLE
//	public static final String CYAN = "\033[0;36m"; // CYAN
//	public static final String WHITE = "\033[0;37m"; // WHITE
//	
//	// Background
//	public static final String BLACK_BACKGROUND = "\033[40m"; // BLACK
//	public static final String RED_BACKGROUND = "\033[41m"; // RED
//	public static final String GREEN_BACKGROUND = "\033[42m"; // GREEN
//	public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
//	public static final String BLUE_BACKGROUND = "\033[44m"; // BLUE
//	public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
//	public static final String CYAN_BACKGROUND = "\033[46m"; // CYAN
//	public static final String WHITE_BACKGROUND = "\033[47m"; // WHITE

	// Bold
	public static final String BLACK_BOLD = "\033[1;30m"; // BLACK
	public static final String RED_BOLD = "\033[1;31m"; // RED
	public static final String GREEN_BOLD = "\033[1;32m"; // GREEN
	public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
	public static final String BLUE_BOLD = "\033[1;34m"; // BLUE
	public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
	public static final String CYAN_BOLD = "\033[1;36m"; // CYAN
	public static final String WHITE_BOLD = "\033[1;37m"; // WHITE

	// Underline
	public static final String BLACK_UNDERLINED = "\033[4;30m"; // BLACK
	public static final String RED_UNDERLINED = "\033[4;31m"; // RED
	public static final String GREEN_UNDERLINED = "\033[4;32m"; // GREEN
	public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
	public static final String BLUE_UNDERLINED = "\033[4;34m"; // BLUE
	public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
	public static final String CYAN_UNDERLINED = "\033[4;36m"; // CYAN
	public static final String WHITE_UNDERLINED = "\033[4;37m"; // WHITE

	// High Intensity
	public static final String BLACK_BRIGHT = "\033[0;90m"; // BLACK
	public static final String RED_BRIGHT = "\033[0;91m"; // RED
	public static final String GREEN_BRIGHT = "\033[0;92m"; // GREEN
	public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
	public static final String BLUE_BRIGHT = "\033[0;94m"; // BLUE
	public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
	public static final String CYAN_BRIGHT = "\033[0;96m"; // CYAN
	public static final String WHITE_BRIGHT = "\033[0;97m"; // WHITE

	// Bold High Intensity
	public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
	public static final String RED_BOLD_BRIGHT = "\033[1;91m"; // RED
	public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
	public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
	public static final String BLUE_BOLD_BRIGHT = "\033[1;94m"; // BLUE
	public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
	public static final String CYAN_BOLD_BRIGHT = "\033[1;96m"; // CYAN
	public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

	// High Intensity backgrounds
	public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
	public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
	public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
	public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
	public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
	public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
	public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m"; // CYAN
	public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m"; // WHITE

	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";

	public static final String BLACK_BACKGROUND = "\u001B[40m";
	public static final String RED_BACKGROUND = "\u001B[41m";
	public static final String GREEN_BACKGROUND = "\u001B[42m";
	public static final String YELLOW_BACKGROUND = "\u001B[43m";
	public static final String BLUE_BACKGROUND = "\u001B[44m";
	public static final String PURPLE_BACKGROUND = "\u001B[45m";
	public static final String CYAN_BACKGROUND = "\u001B[46m";
	public static final String WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String s = sc.nextLine();
			char column = s.charAt(0);
			int row = Integer.parseInt(s.substring(1));
			return new ChessPosition(column, row);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
		}
	}

	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		printBoard(chessMatch);
		System.out.println();
		printCapturedPieces(capturedPieces);
		System.out.println("\nTurn: " + chessMatch.getTurn());
		if (!chessMatch.getCheckMate()) {
			System.out.println("Waiting player: " + getUIColor(chessMatch.getCurrentPlayer(), FontType.PLAIN)
					+ chessMatch.getCurrentPlayer() + RESET);
			if (chessMatch.getCheck()) {
				System.out.println(RED + "CHECK!" + RESET);
			}
		} else {
			System.out.println("CHECKMATE!");
			System.out.println("Winner: " + getUIColor(chessMatch.getCurrentPlayer(), FontType.PLAIN)
					+ chessMatch.getCurrentPlayer() + RESET);
		}
	}

	public static void printBoard(ChessMatch chessMatch) {
		printBoard(chessMatch, new boolean[chessMatch.getRows()][chessMatch.getColumns()]);
//		ChessPiece[][] pieces = chessMatch.getPieces();
//		for (int i = 0; i < pieces.length; i++) {
//			System.out.printf("%d ", 8 - i);
//			for (int j = 0; j < pieces[0].length; j++) {
//				printPiece(pieces[i][j], chessMatch, false);
//			}
//			System.out.println();
//		}
//		System.out.println("  a b c d e f g h ");
	}

	public static void printBoard(ChessMatch chessMatch, boolean[][] possibleMovies) {
		ChessPiece[][] pieces = chessMatch.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			// White view
			System.out.printf("%d ", 8 - i);
			for (int j = 0; j < pieces[0].length; j++) {
				printPiece(pieces[i][j], chessMatch, possibleMovies[i][j]);
			}

			// Black view
			System.out.print(" ".repeat(10));
			System.out.printf("%d ", i + 1);
			for (int j = pieces[0].length - 1; j >= 0; j--) {
				printPiece(pieces[7 - i][j], chessMatch, possibleMovies[7 - i][j]);
			}

			System.out.println();
		}
		System.out.println("  a b c d e f g h" + " ".repeat(13) + "h g f e d c b a  ");
	}

//	public static void printBoard(ChessMatch chessMatch, boolean[][] possibleMovies) {
//		ChessPiece[][] pieces = chessMatch.getPieces();
//		for (int i = 0; i < pieces.length; i++) {
//			// White view
//			if (chessMatch.getCurrentPlayer() == Color.WHITE) {
//				System.out.printf("%d ", 8 - i);
//				for (int j = 0; j < pieces[0].length; j++) {
//					printPiece(pieces[i][j], chessMatch, possibleMovies[i][j]);
//				}
//			} else
//			// Black view
//			{
//				System.out.printf("%d ", i + 1);
//				for (int j = pieces[0].length - 1; j >= 0; j--) {
//					printPiece(pieces[7 - i][j], chessMatch, possibleMovies[7 - i][j]);
//				}
//			}
//			System.out.println();
//		}
//		if (chessMatch.getCurrentPlayer() == Color.WHITE)
//			System.out.println("  a b c d e f g h");
//		else
//			System.out.println("  h g f e d c b a");
//	}

	private static void printPiece(ChessPiece piece, ChessMatch chessMatch, boolean highlight) {
		if (highlight)
			System.out.print(PURPLE_BACKGROUND);

		if (piece == null) {
			System.out.print("-");
		} else {
			if (chessMatch.testCheck(piece.getColor()) && piece.isThereAnyPossibleMove()) {
				System.out.print(piece instanceof King ? RED_UNDERLINED : getUIColor(piece.getColor(), FontType.UNDERLINED));
			}
			if (piece instanceof King k && chessMatch.testCheck(k)) {
				// if it's in check
				System.out.print(RED);
			} else {
				System.out.print(getUIColor(piece.getColor(), FontType.PLAIN));
			}
			System.out.print(piece);
		}
		System.out.print(RESET + " ");
	}

	private static void printCapturedPieces(List<ChessPiece> capturedPieces) {
		List<ChessPiece> whites = capturedPieces.stream().filter(x -> x.getColor() == Color.WHITE)
				.collect(Collectors.toList());
		List<ChessPiece> blacks = capturedPieces.stream().filter(x -> x.getColor() == Color.BLACK)
				.collect(Collectors.toList());

		System.out.println("Captured pieces:");
		System.out.println("White: " + getUIColor(Color.WHITE, FontType.PLAIN) + Arrays.toString(whites.toArray()) + RESET);
		System.out.println("Black: " + getUIColor(Color.BLACK, FontType.PLAIN) + Arrays.toString(blacks.toArray()) + RESET);
	}

	public static String getUIColor(Color color, FontType type) {
		switch (type) {
			case PLAIN:
				return color == Color.WHITE ? BLUE : YELLOW;
			case BOLD:
				return color == Color.WHITE ? BLUE_BOLD : YELLOW_BOLD;
			case BRIGHT:
				return color == Color.WHITE ? BLUE_BRIGHT : YELLOW_BRIGHT;
			case BACKGROUND:
				return color == Color.WHITE ? BLUE_BACKGROUND : YELLOW_BACKGROUND;
			case UNDERLINED:
				return color == Color.WHITE ? BLUE_UNDERLINED : YELLOW_UNDERLINED;
			case BACKGROUNG_BRIGHT:
				return color == Color.WHITE ? BLUE_BACKGROUND_BRIGHT : YELLOW_BACKGROUND_BRIGHT;
			case BOLD_BRIGHT:
				return color == Color.WHITE ? BLUE_BOLD_BRIGHT : YELLOW_BOLD_BRIGHT;
			default:
				throw new IllegalArgumentException("The font type doens's exist");

		}

	}

}
