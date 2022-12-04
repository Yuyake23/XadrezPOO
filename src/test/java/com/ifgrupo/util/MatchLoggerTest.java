package com.ifgrupo.util;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Test;

import com.ifgrupo.chess.Move;

public class MatchLoggerTest {

    MatchLogger matchLogger = new MatchLogger("log.json");

    @Test(expected = IOException.class)
    public void logWriterShouldNotReadNonExistantFile() throws IOException {

        MatchLogger matchLogger = new MatchLogger("gol.json");
        this.matchLogger.read();

    }

    @Test(expected = IOException.class)
    public void loggerShoudNotFindLogfile() throws IOException {

        MatchLogger matchLogger = new MatchLogger("gol.json");
        Deque<Move> moveDeque = new ArrayDeque<>();

        matchLogger.log(moveDeque);

    }

    @Test
    public void matchLoggerShouldLogMatch() throws IOException {

        Deque<Move> moveDeque = new ArrayDeque<>();

        matchLogger.log(moveDeque);

    }

}
