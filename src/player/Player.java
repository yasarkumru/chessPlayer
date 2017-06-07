package player;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import main.MainScreen;
import recognition.Board;
import recognition.Recognizer;

public class Player {

	private BufferedReader engineInput;
	private BufferedWriter engineOutput;
	private Board board;
	private String bestMove;
	private Robot robot;
	private Recognizer recognizer;
	public boolean isPlayerWhite;
	private Process process;
	private static int timeToThink = 500;

	public Player(Board board) {
		this.board = board;
		this.recognizer = MainScreen.getRecognizer();
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void init() throws IOException {

		Runtime rt = Runtime.getRuntime();
		String engine = "/home/yasar/Dropbox/eclipseProjects/chessPlayer/stockfish";
		process = rt.exec(engine);

		engineInput = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		engineOutput = new BufferedWriter(new OutputStreamWriter(
				process.getOutputStream()));
		System.out.println("player inited");
	}

	public void end() throws IOException {
		engineOutput.write("quit\n");
	}

	public void sendCommandAndGetBestMove(String player, int i)
			throws IOException {

		System.out.println("-------------------------");
		if (board.getMoves().size() % 2 == 1) {
			return;
		}
		timeToThink = i;

		sendCommandAndGetBestMove(player);
	}

	private void sendCommandAndGetBestMove(String player) throws IOException {
		isPlayerWhite = player.equals("w") ? true : false;
		String fenString = board.getFenString();
		String position = "position fen " + fenString + " " + player + " "
				+ board.getCastlingRights() + "\n";
		System.out.println(fenString);
		engineOutput.flush();
		engineOutput.write(position);
		engineOutput.flush();
		String command = "go movetime " + timeToThink + "\n";
		engineOutput.write(command);
		engineOutput.flush();
		long start = Calendar.getInstance().getTimeInMillis();
		String readLine = null;
		while (true) {
			if (engineInput.ready())
				readLine = engineInput.readLine();

			if ((bestMove = getBestMove(readLine)) != null) {
				System.err.println(bestMove);
				MainScreen.lblBestmove.setText(bestMove);
				break;
			}
			long check = Calendar.getInstance().getTimeInMillis();
			if (check - start > 2 * timeToThink) {
				System.err.println("TIMEOUT!!!!!!!!!");
				return;
			}

		}
		playBestMove();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void playBestMove() {
		int[] from = getCoordinateForTheMove(bestMove.substring(0, 2));
		int[] to = getCoordinateForTheMove(bestMove.substring(2, 4));
		System.out.println("from: " + from[0] + " " + from[1]);
		System.out.println("to: " + to[0] + " " + to[1]);
		robot.mouseMove(from[0], from[1]);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseMove(to[0], to[1]);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	private int[] getCoordinateForTheMove(String move) {
		int x = recognizer.getBoardX();
		int y = recognizer.getBoardY();
		int square = board.getSquare();
		int[] coor = new int[2];
		int charAtX = move.codePointAt(0) - 97;
		int charAtY = move.codePointAt(1) - 49;
		coor[0] = x + (square / 2) + (charAtX * square);
		coor[1] = y + (square / 2) + ((7 - charAtY) * square);
		return coor;
	}
	
	public void setEngineSkillLevel(String level) throws IOException{
		engineOutput.write("setoption name Skill Level value "+level+"\n");
		engineOutput.flush();
	}

	private static String getBestMove(String s) {
		if (s == null)
			return null;
		int indexOf = s.indexOf(" ");
		if (indexOf < 0)
			return null;
		if (s.substring(0, indexOf).equalsIgnoreCase("bestmove")) {
			s = s.substring(indexOf + 1);
			indexOf = s.indexOf(" ");
			s = s.substring(0, indexOf);
			return new String(s);
		}

		return null;
	}
}
