package com.ifgrupo.ui.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serial;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ifgrupo.chess.ChessPiece;

public class Frame extends JFrame {
	@Serial
	private static final long serialVersionUID = 752462626850585382L;

	public static final int W = 720;
	public static final int H = 720;

	Board board;
	String returnString;
	Semaphore sem;

	public Frame(Semaphore sem) {
		this.sem = sem;
		this.setTitle("Xadrez");
		this.setSize(W, H);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fechar quando clicar no X
		this.setLocationRelativeTo(null);
		this.setPreferredSize(new Dimension(W, H));

		this.board = new Board();

		this.add(board);

		this.setVisible(true);
	}

	class Board extends JPanel {
		@Serial
		private static final long serialVersionUID = -6417242596301502214L;

		GridLayout boardLayout;
		JButton pieces[][];

		Board() {
			this.boardLayout = new GridLayout(8, 8);

			this.pieces = new JButton[8][8];
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces.length; j++) {
					JButton b = new JButton();
					final int aj = j, ai = i;
					b.setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.GRAY);
					b.addActionListener(x -> {
						if (sem.availablePermits() == 0) {
							returnString = "" + (char) (aj + 97) + (8 - ai);
							sem.release();
						}
					});

					this.pieces[i][j] = b;
					this.add(b);
				}
			}

			this.setLayout(this.boardLayout);
		}

		void update(ChessPiece[][] pieces) {
			System.out.println("ATUALIZANDO TELA");
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[i].length; j++) {
					if (pieces[i][j] != null) {
						ImageIcon icon = new ImageIcon(pieces[i][j].getImagePath());
						this.pieces[i][j].setIcon(icon);
					} else {
						this.pieces[i][j].setIcon(null);
					}
					this.pieces[i][j].setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.GRAY);
				}
			}
			this.setLayout(this.boardLayout);
		}

		void update(ChessPiece[][] pieces, boolean possibleMoves[][]) {
			update(pieces);
			for (int i = 0; i < possibleMoves.length; i++) {
				for (int j = 0; j < possibleMoves[i].length; j++) {
					if (possibleMoves[i][j]) {
						this.pieces[i][j].setBackground(Color.MAGENTA);
					}
				}
			}
		}

	}

}
