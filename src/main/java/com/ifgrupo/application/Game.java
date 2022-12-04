package com.ifgrupo.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;

import com.ifgrupo.chess.ChessMatch;
import com.ifgrupo.chess.Move;
import com.ifgrupo.util.MatchLog;
import com.ifgrupo.util.MatchLogger;

public abstract class Game {

    protected ChessMatch chessMatch;

    public abstract String chosePieceTypeToPromotion();

    public abstract void start();

    public void save() {
        MatchLogger matchLogger = new MatchLogger("log.json");
        Deque<Move> moveDeque = chessMatch.getMoveDeque();

        ArrayList<MatchLog> matchList = new ArrayList<>();

        try {

            if (matchLogger.read() != null) {
                try {
                    matchList = matchLogger.read();
                } catch (IOException e) {
                    matchList.add(new MatchLog(0, moveDeque));
                    matchLogger.write(matchList);
                    return;
                }
            }

            int listSize = 0;

            try {
                listSize = matchList.size();
            } catch (NullPointerException npe) {
                listSize = 0;
            }
            matchList.add(new MatchLog(listSize, moveDeque));
            matchLogger.write(matchList);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
