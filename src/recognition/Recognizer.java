package recognition;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Recognizer {

	private int boardX = 260;// chesscube
	private int boardY = 129;// chesscube
	private int size = 496;// chesscube
//	private int boardX = 24;// arena
//	private int boardY = 92;// arena
//	private int size = 448;//arena
	private BufferedImage chessBoard;
	private BufferedImage capture;
	private Board board;

	public void init() throws AWTException{
		capture = new Robot().createScreenCapture(new Rectangle(Toolkit
				.getDefaultToolkit().getScreenSize()));
		makeImageBlackAndWhite(capture);

		chessBoard = capture.getSubimage(boardX, boardY, size, size);
		board = new Board(chessBoard);
	}

	public void refreshChessBoard() throws AWTException, ChessPlayerException {
		Rectangle boardRectangle = new Rectangle(boardX, boardY, size, size);
		chessBoard = new Robot().createScreenCapture(boardRectangle);
		makeImageBlackAndWhite(chessBoard);
		board.setChessBoard(chessBoard);
		board.showBoard();
	}

	public static void makeImageBlackAndWhite(BufferedImage capture) {
		int width = capture.getWidth();
		int height = capture.getHeight();
		int middle = new Color(100, 100, 100).getRGB();
		int white = new Color(255, 255, 255).getRGB();
		int black = new Color(0, 0, 0).getRGB();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = capture.getRGB(i, j);
				if (rgb > middle) {
					capture.setRGB(i, j, white);
				} else {
					capture.setRGB(i, j, black);
				}
			}
		}

	}

	public Board getBoard() {
		return board;
	}

	public int getBoardX() {
		return boardX;
	}

	public int getBoardY() {
		return boardY;
	}

}
