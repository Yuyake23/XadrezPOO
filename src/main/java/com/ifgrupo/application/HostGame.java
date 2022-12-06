package com.ifgrupo.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.pieces.PieceType;
import com.ifgrupo.ui.Terminal;

public class HostGame extends Game {

	private List<ChessPiece> capturedPieces;

	private Terminal whiteTerminal;
	private Terminal blackTerminal;
	private Terminal currentPlayer;

	public HostGame(Terminal whiteTerminal, Terminal blackTerminal) {
		super.chessMatch = new ChessMatch();
		this.capturedPieces = new ArrayList<>();
		this.whiteTerminal = whiteTerminal;
		this.blackTerminal = blackTerminal;
	}

	@Override
	public void start() {
		ChessPosition source = null, target = null;
		PieceType pieceTypeToPromote;
		ChessPiece capturedPiece;
		String[] r;

		currentPlayer = whiteTerminal;

		while (!chessMatch.matchIsOver()) {
			pieceTypeToPromote = null;
			try {
				currentPlayer.update(chessMatch, capturedPieces, null);
				opponent(currentPlayer).update(chessMatch, capturedPieces, null);

				r = currentPlayer.readSourcePosition().split(" ");

				System.out.println(Arrays.toString(r));

				if (r.length > 2) {
					System.out.println("MAIOR QUE DOIS AQUIIIIIIIIIIIIIIIIIIIIIIIIIIII");
					pieceTypeToPromote = PieceType.pieceTypeByChar(r[2]);
					System.out.println("Peça: " + pieceTypeToPromote);
				}

				if (r.length > 1)
					target = new ChessPosition(r[1]);
				source = new ChessPosition(r[0]);

				if (r.length == 1) {
					currentPlayer.update(chessMatch, capturedPieces, chessMatch.possibleMovies(source));
					opponent(currentPlayer).update(chessMatch, capturedPieces, chessMatch.possibleMovies(source));
					r = currentPlayer.readTargetPosition().split(" ");
					target = new ChessPosition(r[0]);
					if (r.length > 1)
						pieceTypeToPromote = PieceType.pieceTypeByChar(r[1]);
				}

				capturedPiece = chessMatch.performChessMove(source, target, pieceTypeToPromote);

				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);

				currentPlayer = opponent(currentPlayer);
			} catch (NullPointerException | InputMismatchException | NumberFormatException | ChessException
					| ClassCastException e) {
				currentPlayer.exceptionMessage(e);
			}
		}

		whiteTerminal.finish(chessMatch, capturedPieces, chessMatch.getWinner());
		blackTerminal.finish(chessMatch, capturedPieces, chessMatch.getWinner());
	}

	private Terminal opponent(Terminal terminal) {
		return terminal == blackTerminal ? whiteTerminal : blackTerminal;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		return currentPlayer.chosePieceTypeToPromotion();
	}

}
