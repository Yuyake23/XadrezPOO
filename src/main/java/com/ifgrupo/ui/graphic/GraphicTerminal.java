package com.ifgrupo.ui.graphic;

import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class GraphicTerminal extends Terminal {

	private Frame frame;
	Semaphore sem = new Semaphore(1);

	public GraphicTerminal(Color playerColor) {
		super(playerColor);

		this.frame = new Frame(sem);
	}

	@Override
	public String readSourcePosition() {
		return waitValue();
	}

	@Override
	public String readTargetPosition() {
		return waitValue() + " Q";
	}

	private String waitValue() {
		try {
			sem.acquire();
			sem.acquire();
			sem.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String r = frame.returnString;
		frame.returnString = null;
		return r;
	}

	@Override
	public String chosePieceTypeToPromotion() {
		return JOptionPane.showInputDialog(frame, "Enter piece for promotion (B/N/R/Q):");
	}

	@Override
	public void message(String s) {
		JOptionPane.showMessageDialog(frame, s);
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		this.update(chessMatch, capturedPieces, null);
		JOptionPane.showMessageDialog(frame,
				"FIM DO JOGO\n%s".formatted(winner == null ? "Empate!" : "Vencedor: " + winner));
		this.frame.dispose();
	}

	@Override
	public void exceptionMessage(Exception e) {
		JOptionPane.showConfirmDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		if (possibleMoves != null)
			this.frame.board.update(chessMatch, possibleMoves);
		else
			this.frame.board.update(chessMatch);
	}

}
