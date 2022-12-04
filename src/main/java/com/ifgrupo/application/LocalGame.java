package com.ifgrupo.application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.ui.Terminal;

public class LocalGame extends Game {

	private ChessMatch chessMatch;
	private List<ChessPiece> capturedPieces;
	private Terminal terminal;

	public LocalGame(Terminal terminal, String namePlayer1, String namePlayer2) {
		this.chessMatch = new ChessMatch();
		this.capturedPieces = new ArrayList<>();
		this.terminal = terminal;
	}

	@Override
	public void start() {
		ChessPosition source, target;
		ChessPiece capturedPiece;
		boolean possibleMovies[][];

		while (!chessMatch.matchIsOver()) {
			try {
				source = this.terminal.readSourcePosition(chessMatch, capturedPieces);
				possibleMovies = chessMatch.possibleMovies(source);
				target = this.terminal.readTargetPosition(chessMatch, capturedPieces, possibleMovies);
				capturedPiece = chessMatch.performChessMove(source, target);
				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);
			} catch (InputMismatchException | NumberFormatException | ChessException e) {
				terminal.exceptionMessage(e);
			}
		}
		terminal.finish(chessMatch, capturedPieces, chessMatch.getCurrentPlayer());
		
	}

	@Override
	public String chosePieceTypeToPromotion() {
		return terminal.chosePieceTypeToPromotion();
	}

}
