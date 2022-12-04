package com.ifgrupo.util;

import java.util.Deque;

import com.ifgrupo.chess.Move;

public record MatchLog(int matchId, Deque<Move> moveDeque) {
}