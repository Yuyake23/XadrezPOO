package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.Terminal;

public class LocalGame implements Game {

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

//	@Override
//	public void message(String s) {
//		terminal.message(s);
//	}
}
