package ui.bash;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;

public class BashTerminal extends Terminal {
	private Scanner sc;

	public BashTerminal(Color playerColor, String name) {
		super(playerColor, name);
		this.sc = new Scanner(System.in);
	}

	@Override
	public ChessPosition readSourcePosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
		UI.clearScreen();
		UI.printMatch(chessMatch, capturedPieces);
		System.out.print("\nOrigem: ");
		try {
			String s = sc.nextLine();
			char column = s.charAt(0);
			int row = Integer.parseInt(s.substring(1));
			return new ChessPosition(column, row);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
		}
	}

	@Override
	public ChessPosition readTargetPosition(ChessMatch chessMatch, List<ChessPiece> capturedPieces,
			boolean[][] possibleMoves) {
		UI.clearScreen();
		UI.printMatch(chessMatch, capturedPieces, possibleMoves);
		System.out.print("\nDestino: ");
		try {
			String s = sc.nextLine();
			char column = s.charAt(0);
			int row = Integer.parseInt(s.substring(1));
			return new ChessPosition(column, row);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
		}
	}

	@Override
	public String chosePieceTypeToPromotion() {
		System.out.print("Enter piece for promotion (B/N/R/Q): ");
		return sc.nextLine().toUpperCase();
	}

	@Override
	public void message(String s) {
		System.out.println(s);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		UI.clearScreen();
		UI.printMatch(chessMatch, capturedPieces);
	}

	@Override
	public void exceptionMessage(Exception e) {
		System.err.println(UI.RED + e.getMessage() + UI.RESET);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		UI.clearScreen();
		if (possibleMoves != null) {
			UI.printMatch(chessMatch, capturedPieces, possibleMoves);
		} else {
			UI.printMatch(chessMatch, capturedPieces);
		}

	}

}
