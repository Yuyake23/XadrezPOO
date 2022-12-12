package com.ifgrupo.application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.chess.pieces.PieceType;
import com.ifgrupo.ui.Terminal;
import com.ifgrupo.ui.ai.AITerminal;
import com.ifgrupo.ui.bash.BashTerminal;
import com.ifgrupo.ui.graphic.GraphicTerminal;

public class LocalGameVsPC {

	private static Terminal ai;
	private static Terminal playerTerminal;
	private static Terminal currentTerminal;

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in);) {
			if (args.length == 0) {
				System.out.println("===== ESCOLHA UMA OPÇÃO =====");
				System.out.println("1 - Jogo no bash");
				System.out.println("2 - Abrir no ambiente gráfico");

				args = new String[] { switch (sc.nextInt()) {
					case 1 -> "-b";
					case 2 -> "-g";
					default -> throw new IllegalArgumentException("Unexpected value");
				} };
				sc.nextLine();
			}

			configMatch(args[0]);
			match();
		}
	}

	private static void configMatch(String terminalType) {
		ai = new AITerminal(Color.random());

		if (terminalType.equalsIgnoreCase("-b")) {
			playerTerminal = new BashTerminal(ai.getPlayerColor().opponent());
		} else if (terminalType.equalsIgnoreCase("-g")) {
			playerTerminal = new GraphicTerminal(ai.getPlayerColor().opponent());
		} else {
			throw new IllegalArgumentException("Unexpected value");
		}

		currentTerminal = ai.getPlayerColor() == Color.WHITE ? ai : playerTerminal;
	}

	private static void match() {
		ChessMatch chessMatch = new ChessMatch();
		;
		List<ChessPiece> capturedPieces = new ArrayList<>();

		ChessPosition source, target;
		PieceType pieceTypeToPromote;
		ChessPiece capturedPiece;
		String r[];

		while (!chessMatch.matchIsOver()) {
			source = null;
			target = null;
			pieceTypeToPromote = null;
			try {
				playerTerminal.update(chessMatch, capturedPieces, null);
				ai.update(chessMatch, capturedPieces, null);

				r = currentTerminal.readSourcePosition().split(" ");

				if (r.length > 2)
					pieceTypeToPromote = PieceType.pieceTypeByChar(r[2]);
				if (r.length > 1)
					target = new ChessPosition(r[1]);
				source = new ChessPosition(r[0]);

				if (r.length == 1) {
					currentTerminal.update(chessMatch, capturedPieces, chessMatch.possibleMovies(source));
					r = currentTerminal.readTargetPosition().split(" ");
					target = new ChessPosition(r[0]);
					if (r.length > 1)
						pieceTypeToPromote = PieceType.pieceTypeByChar(r[1]);
				}

				capturedPiece = chessMatch.performChessMove(source, target, pieceTypeToPromote);
				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);

				currentTerminal = currentTerminal == ai ? playerTerminal : ai;
			} catch (InputMismatchException | NumberFormatException | ChessException e) {
				if (target == null || !target.equals(source))
					currentTerminal.exceptionMessage(e);
			}
		}

		playerTerminal.finish(chessMatch, capturedPieces, chessMatch.getCurrentPlayer());
		ai.finish(chessMatch, capturedPieces, chessMatch.getCurrentPlayer());
	}
}
