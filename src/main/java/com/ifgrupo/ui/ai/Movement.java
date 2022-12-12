package com.ifgrupo.ui.ai;

import com.ifgrupo.chess.ChessPosition;
import com.ifgrupo.chess.pieces.PieceType;

public class Movement {

	private ChessPosition sourcePosition;
	private ChessPosition targetPosition;
	private PieceType pieceTypeToPromotion;

	private int value;

	Movement(ChessPosition sourcePosition, ChessPosition targetPosition) {
		this.sourcePosition = sourcePosition;
		this.targetPosition = targetPosition;

		this.value = 0;
	}

	Movement(ChessPosition sourcePosition, ChessPosition targetPosition, PieceType pieceTypeToPromotion) {
		this(sourcePosition, targetPosition);
		this.pieceTypeToPromotion = pieceTypeToPromotion;
	}

	public void setPieceTypeToPromotion(PieceType pieceTypeToPromotion) {
		this.pieceTypeToPromotion = pieceTypeToPromotion;
	}

	public PieceType getPieceTypeToPromotion() {
		return pieceTypeToPromotion;
	}

	public ChessPosition getSourcePosition() {
		return sourcePosition;
	}

	public ChessPosition getTargetPosition() {
		return targetPosition;
	}

	public int getValue() {
		return value;
	}

	public void increseValue(int value) {
		this.value += value;
	}

	@Override
	public String toString() {
		if (pieceTypeToPromotion == null)
			return sourcePosition + " " + targetPosition;
		else
			return sourcePosition + " " + targetPosition + " " + pieceTypeToPromotion;
	}

}
