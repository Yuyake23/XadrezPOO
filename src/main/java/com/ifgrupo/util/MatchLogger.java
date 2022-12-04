package com.ifgrupo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ifgrupo.chess.Move;

public class MatchLogger {

	private Gson gson;
	private String logFile;

	public MatchLogger(String logFile) {
		this.logFile = logFile;
		this.gson = new Gson();
	}

	public void log(Deque<Move> moveDeque) throws IOException {

		ArrayList<MatchLog> matchList = new ArrayList<>();

		if (this.read() != null) {
			try {
				matchList = this.read();
			} catch (IOException e) {
				matchList.add(new MatchLog(0, moveDeque));
				this.write(matchList);
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
		this.write(matchList);

	}

	public void write(ArrayList<MatchLog> matchList) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFile));

		writer.write(gson.toJson(matchList));

		writer.close();

	}

	public ArrayList<MatchLog> read() throws IOException {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.logFile));
			String jsonText = reader.readLine();
			reader.close();
			return gson.fromJson(jsonText, TypeToken.getParameterized(ArrayList.class, MatchLog.class).getType());
		} catch (FileNotFoundException fnfe) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFile));
			BufferedReader reader = new BufferedReader(new FileReader(this.logFile));
			writer.close();
			String jsonText = reader.readLine();
			reader.close();
			return gson.fromJson(jsonText, TypeToken.getParameterized(ArrayList.class, MatchLog.class).getType());
		}

	}

}
