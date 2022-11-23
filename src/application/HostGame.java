package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import ui.Terminal;

public class HostGame implements Game {

	private ChessMatch chessMatch;
	private List<ChessPiece> capturedPieces;

	private Terminal whiteTerminal;
	private Terminal blackTerminal;

	public HostGame(Terminal whiteTerminal, Terminal blackTerminal) {
		this.chessMatch = new ChessMatch();
		this.capturedPieces = new ArrayList<>();
		this.whiteTerminal = whiteTerminal;
		this.blackTerminal = blackTerminal;
	}

	@Override
	public void start() {
		Terminal currentPlayer = whiteTerminal;

		currentPlayer.update(chessMatch, capturedPieces, null);
		opponent(currentPlayer).update(chessMatch, capturedPieces, null);

		while (!chessMatch.getCheckMate()) {
			try {

				if (chessMatch.getCurrentPlayer() == Color.WHITE) {
					currentPlayer = whiteTerminal;
				} else {
					currentPlayer = blackTerminal;
				}

				ChessPosition source, target;
				ChessPiece capturedPiece;
				boolean possibleMovies[][];
				try {
					source = currentPlayer.readSourcePosition(chessMatch, capturedPieces);
					possibleMovies = chessMatch.possibleMovies(source);
					opponent(currentPlayer).update(chessMatch, capturedPieces, possibleMovies);

					target = currentPlayer.readTargetPosition(chessMatch, capturedPieces, possibleMovies);

					capturedPiece = chessMatch.performChessMove(source, target);

					if (capturedPiece != null)
						capturedPieces.add(capturedPiece);
				} catch (NullPointerException e) {
					throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
				}
			} catch (InputMismatchException | NumberFormatException | ChessException | ClassCastException e) {
				currentPlayer.exceptionMessage(e);
			}
			currentPlayer.update(chessMatch, capturedPieces, null);
			opponent(currentPlayer).update(chessMatch, capturedPieces, null);
		}

		whiteTerminal.finish(chessMatch, capturedPieces, chessMatch.getWinner());
		blackTerminal.finish(chessMatch, capturedPieces, chessMatch.getWinner());
	}

	private Terminal opponent(Terminal terminal) {
		return terminal == blackTerminal ? whiteTerminal : blackTerminal;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		try {
			return whiteTerminal.chosePieceTypeToPromotion();
		} catch (ClassNotFoundException | NullPointerException | IOException e) {
			whiteTerminal.exceptionMessage(e);
		}
		return null;
	}

}
