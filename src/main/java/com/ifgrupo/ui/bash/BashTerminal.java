package com.ifgrupo.ui.bash;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class BashTerminal extends Terminal {
	private Scanner sc;

	public BashTerminal(Color playerColor, String name) {
		super(playerColor, name);
		this.sc = new Scanner(System.in);
	}

	@Override
	public String readSourcePosition() {
		System.out.print("\nOrigem: ");
		try {
			return sc.nextLine().trim();
		} catch (RuntimeException e) {
			throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
		}
	}

	@Override
	public String readTargetPosition() {
		System.out.print("\nDestino: ");
		try {
			return sc.nextLine().trim();
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
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		UI.clearScreen();
		UI.printMatch(chessMatch, capturedPieces);
	}

	@Override
	public void exceptionMessage(Exception e) {
		System.err.println(UI.RED + e.getMessage() + UI.RESET);
		System.out.println("Type <Enter>");
		sc.nextLine();
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
