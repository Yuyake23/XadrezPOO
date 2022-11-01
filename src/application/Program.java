package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	private static Scanner sc = new Scanner(System.in);
	private static ChessMatch chessMatch = new ChessMatch();
	private static List<ChessPiece> capturedPieces = new ArrayList<>();

	public static void main(String[] args) {

		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();
				ChessPosition source, target;
				ChessPiece capturedPiece;
				UI.printMatch(chessMatch, capturedPieces);

				System.out.print("\nOrigem: ");
				source = UI.readChessPosition(sc);

				boolean possibleMovies[][] = chessMatch.possibleMovies(source);
				UI.clearScreen();
				UI.printBoard(chessMatch, possibleMovies);

				System.out.print("\nDestino: ");
				target = UI.readChessPosition(sc);

				capturedPiece = chessMatch.performChessMove(source, target);
				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);

			} catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, capturedPieces);
	}

	public static String chosePieceType() {
		while (true) {
			UI.clearScreen();
			UI.printBoard(chessMatch);
			System.out.print("Enter piece for promotion (B/N/R/Q): ");
			String type = sc.nextLine().toUpperCase();
			if (type.length() == 1 && "BNQR".contains(type))
				return type;
			else
				System.out.print("Invalid Type! ");
		}
	}
}
