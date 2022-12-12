package com.ifgrupo.chess;

public enum Color {
	BLACK, WHITE;

	public static Color random() {
		return (int) (Math.random() * 2) == 0 ? WHITE : BLACK;
	}

	public Color opponent() {
		return this == WHITE ? BLACK : WHITE;
	}
}
