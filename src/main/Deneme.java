package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class Deneme {
	public static void main(String[] args) throws IOException{
		Runtime rt = Runtime.getRuntime();
		String engine = "/home/yasar/Dropbox/eclipseProjects/chessPlayer/stockfish";
		Process process = rt.exec(engine);

		BufferedReader engineInput = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		BufferedWriter engineOutput = new BufferedWriter(
				new OutputStreamWriter(process.getOutputStream()));
		engineOutput.flush();
		engineOutput.flush();
		engineOutput.flush();
		String position = "position startpos\n";
		engineOutput.flush();
		engineOutput.write(position);
		engineOutput.flush();
		String command = "go movetime 1000\n";
		engineOutput.write(command);
		engineOutput.flush();
		String readLine = null;
		
		int count = 0;
		long start = Calendar.getInstance().getTimeInMillis();
		while (true) {
			count++;
			System.out.print(count + ":");
			if (engineInput.ready())
				readLine = engineInput.readLine();

			if (readLine == null)
				System.out.println("readLine = null");
			System.out.println(count + ": " + readLine);
			long check = Calendar.getInstance().getTimeInMillis();
			if (check - start > 1000){
				System.out.println("timeout");
				break;
			}
				
		}
		System.out.println(count);
		
	}
}
