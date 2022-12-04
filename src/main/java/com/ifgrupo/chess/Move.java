package com.ifgrupo.chess;

import java.io.Serializable;

public record Move(
        ChessPosition sourcePosition,
        ChessPosition targetPosition,
        String promotedPiece) implements Serializable {
}
