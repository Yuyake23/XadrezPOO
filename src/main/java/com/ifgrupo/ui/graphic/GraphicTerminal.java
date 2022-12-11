package com.ifgrupo.ui.graphic;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.ChessPiece;
import com.ifgrupo.chess.Color;
import com.ifgrupo.ui.Terminal;

public class GraphicTerminal extends Terminal {

	private Frame frame;
	Scanner sc = new Scanner(System.in);
	Semaphore sem = new Semaphore(1);

	public GraphicTerminal(Color playerColor, String name) {
		super(playerColor, name);

//		new Thread(() -> frame = new Frame(sem)).start();
		this.frame = new Frame(sem);
	}

	@Override
	public String readSourcePosition() {
		try {
			sem.acquire();
			sem.acquire();
			sem.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String r = frame.returnString;
		frame.returnString = null;
		System.out.println(r);
		return r;
	}

	@Override
	public String readTargetPosition() {
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
	public void message(String s) {
		System.out.println(s);
	}

	@Override
	public void finish(ChessMatch chessMatch, List<ChessPiece> capturedPieces, Color winner) {
		System.out.println("Cabo. Ganhador: " + winner);
		sc.close();
	}

	@Override
	public void exceptionMessage(Exception e) {
		System.out.println("Excecao: " + e.getMessage());
	}

	@Override
	public void update(ChessMatch chessMatch, List<ChessPiece> capturedPieces, boolean[][] possibleMoves) {
		if (possibleMoves != null)
			this.frame.board.update(chessMatch.getPieces(), possibleMoves);
		else
			this.frame.board.update(chessMatch.getPieces());
	}

}
