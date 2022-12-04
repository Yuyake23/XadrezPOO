package com.ifgrupo.application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class HostGame implements Game {

	private ChessMatch chessMatch;
	private List<ChessPiece> capturedPieces;

	private Terminal whiteTerminal;
	private Terminal blackTerminal;
	private Terminal currentPlayer;

	public HostGame(Terminal whiteTerminal, Terminal blackTerminal) {
		this.chessMatch = new ChessMatch();
		this.capturedPieces = new ArrayList<>();
		this.whiteTerminal = whiteTerminal;
		this.blackTerminal = blackTerminal;
	}

	@Override
	public void start() {
		currentPlayer = whiteTerminal;

		currentPlayer.update(chessMatch, capturedPieces, null);
		opponent(currentPlayer).update(chessMatch, capturedPieces, null);

		while (!chessMatch.matchIsOver()) {
			try {

				if (chessMatch.getCurrentPlayer() == Color.WHITE) {
					currentPlayer = whiteTerminal;
				} else {
					currentPlayer = blackTerminal;
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
			} catch (NullPointerException | InputMismatchException | NumberFormatException | ChessException
					| ClassCastException e) {
				e.printStackTrace();
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
		return whiteTerminal.chosePieceTypeToPromotion();
	}

}
