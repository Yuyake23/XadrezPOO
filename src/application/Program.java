package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;
import ui.bash.BashTerminal;

public class Program {

	private static ChessMatch chessMatch = new ChessMatch();
	private static List<ChessPiece> capturedPieces = new ArrayList<>();

	private static Terminal whitePlayer = new BashTerminal(Color.WHITE);
	private static Terminal blackPlayer = new BashTerminal(Color.WHITE);

	private static Terminal currentPlayer;

	public static void main(String[] args) {

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

//				currentPlayer.printMatch(chessMatch, capturedPieces);

//				currentPlayer.message("\nSource: ");
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
