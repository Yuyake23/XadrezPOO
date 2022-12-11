package com.ifgrupo.application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import com.ifgrupo.chess.ChessException;
import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.pieces.PieceType;
import com.ifgrupo.ui.Terminal;

public class LocalGame extends Game {

	private List<ChessPiece> capturedPieces;
	private Terminal terminal;

	public LocalGame(Terminal terminal, String namePlayer1, String namePlayer2) {
		super.chessMatch = new ChessMatch();
		this.capturedPieces = new ArrayList<>();
		this.terminal = terminal;
	}

	@Override
	public void start() {
		ChessPosition source, target;
		PieceType pieceTypeToPromote;
		ChessPiece capturedPiece;
		String r[];

		while (!chessMatch.matchIsOver()) {
			source = null;
			target = null;
			pieceTypeToPromote = null;
			try {
				terminal.update(chessMatch, capturedPieces, null);

				r = this.terminal.readSourcePosition().split(" ");

				if (r.length > 2)
					pieceTypeToPromote = PieceType.pieceTypeByChar(r[2]);
				if (r.length > 1)
					target = new ChessPosition(r[1]);
				source = new ChessPosition(r[0]);

				if (r.length == 1) {
					this.terminal.update(chessMatch, capturedPieces, chessMatch.possibleMovies(source));
					r = this.terminal.readTargetPosition().split(" ");
					target = new ChessPosition(r[0]);
					if (r.length > 1)
						pieceTypeToPromote = PieceType.pieceTypeByChar(r[1]);
				}

				capturedPiece = chessMatch.performChessMove(source, target, pieceTypeToPromote);
				if (capturedPiece != null)
					capturedPieces.add(capturedPiece);

			} catch (InputMismatchException | NumberFormatException | ChessException e) {
				if (target == null || !target.equals(source))
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
