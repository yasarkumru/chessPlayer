package recognition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Board {
	private ChessPiece[][] pieces = new ChessPiece[8][8];
	private ChessPiece[][] oldPieces = new ChessPiece[8][8];
	private BufferedImage chessBoard;
	private int size;
	private int square;
	private ChessPiece wPawnL;
	private ChessPiece wPawnD;
	private ChessPiece wRookD;
	private ChessPiece wRookL;
	private ChessPiece wKnightL;
	private ChessPiece wKnightD;
	private ChessPiece wBishopD;
	private ChessPiece wBishopL;
	private ChessPiece wQueen;
	private ChessPiece wKing;

	private ChessPiece bPawnD;
	private ChessPiece bPawnL;
	private ChessPiece bRookL;
	private ChessPiece bRookD;
	private ChessPiece bKnightD;
	private ChessPiece bKnightL;
	private ChessPiece bBishopL;
	private ChessPiece bBishopD;
	private ChessPiece bQueen;
	private ChessPiece bKing;

	private ChessPiece emptyD;
	private ChessPiece emptyL;
	private List<ChessPiece> piecesList = new ArrayList<>(30);
	private List<String> moves = new ArrayList<>();
	private boolean boardChanged = true;
	private int scanCount = 0;
	private static final int border = 2;
	private String castlingRights = "KQkq";

	public Board(BufferedImage chessBoard) {
		this.chessBoard = chessBoard;
		initChessBoard();
	}

	private void initChessBoard() {
		size = chessBoard.getHeight();
		square = size / 8;
		initPieces();
	}

	private void initPieces() {
		BufferedImage wPawnL = getSquare(0, 1);
		BufferedImage wPawnD = getSquare(7, 1);
		BufferedImage wRookD = getSquare(0, 0);
		BufferedImage wRookL = getSquare(7, 0);
		BufferedImage wKnightL = getSquare(1, 0);
		BufferedImage wKnightD = getSquare(6, 0);
		BufferedImage wBishopD = getSquare(2, 0);
		BufferedImage wBishopL = getSquare(5, 0);
		BufferedImage wQueen = getSquare(3, 0);
		BufferedImage wKing = getSquare(4, 0);

		BufferedImage bPawnD = getSquare(0, 6);
		BufferedImage bPawnL = getSquare(7, 6);
		BufferedImage bRookL = getSquare(0, 7);
		BufferedImage bRookD = getSquare(7, 7);
		BufferedImage bKnightD = getSquare(1, 7);
		BufferedImage bKnightL = getSquare(6, 7);
		BufferedImage bBishopL = getSquare(2, 7);
		BufferedImage bBishopD = getSquare(5, 7);
		BufferedImage bQueen = getSquare(3, 7);
		BufferedImage bKing = getSquare(4, 7);

		BufferedImage emptyD = getSquare(0, 2);
		BufferedImage emptyL = getSquare(0, 3);

		this.wPawnL = new ChessPiece(wPawnL, "P");
		this.wPawnD = new ChessPiece(wPawnD, "P");
		this.wRookD = new ChessPiece(wRookD, "R");
		this.wRookL = new ChessPiece(wRookL, "R");
		this.wKnightL = new ChessPiece(wKnightL, "N");
		this.wKnightD = new ChessPiece(wKnightD, "N");
		this.wBishopD = new ChessPiece(wBishopD, "B");
		this.wBishopL = new ChessPiece(wBishopL, "B");
		this.wQueen = new ChessPiece(wQueen, "Q");
		this.wKing = new ChessPiece(wKing, "K");

		this.bPawnD = new ChessPiece(bPawnD, "p");
		this.bPawnL = new ChessPiece(bPawnL, "p");
		this.bRookL = new ChessPiece(bRookL, "r");
		this.bRookD = new ChessPiece(bRookD, "r");
		this.bKnightD = new ChessPiece(bKnightD, "n");
		this.bKnightL = new ChessPiece(bKnightL, "n");
		this.bBishopL = new ChessPiece(bBishopL, "b");
		this.bBishopD = new ChessPiece(bBishopD, "b");
		this.bQueen = new ChessPiece(bQueen, "q");
		this.bKing = new ChessPiece(bKing, "k");

		this.emptyD = new ChessPiece(emptyD, " ");
		this.emptyL = new ChessPiece(emptyL, " ");

		piecesList.add(this.wPawnL);
		piecesList.add(this.wPawnD);
		piecesList.add(this.wRookD);
		piecesList.add(this.wRookL);
		piecesList.add(this.wKnightL);
		piecesList.add(this.wKnightD);
		piecesList.add(this.wBishopD);
		piecesList.add(this.wBishopL);
		piecesList.add(this.wQueen);
		piecesList.add(this.wKing);

		piecesList.add(this.bPawnD);
		piecesList.add(this.bPawnL);
		piecesList.add(this.bRookL);
		piecesList.add(this.bRookD);
		piecesList.add(this.bKnightD);
		piecesList.add(this.bKnightL);
		piecesList.add(this.bBishopL);
		piecesList.add(this.bBishopD);
		piecesList.add(this.bQueen);
		piecesList.add(this.bKing);
		
		

	}

	public int getSquare() {
		return square;
	}

	public BufferedImage getSquare(int x, int y) {
		int startX = x * square;
		int startY = (7 - y) * square;
		return chessBoard.getSubimage(startX, startY, square, square);
	}

	private void scanBoard() throws ChessPlayerException {
		scanCount++;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				oldPieces[i][j] = pieces[i][j];
				BufferedImage square = getSquare(i, j);
				pieces[i][j] = getClosestPiece(square);
			}
		}
		if (scanCount > 1)
			addCurrentMove();

	}

	private String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h" };

	private void addCurrentMove() throws ChessPlayerException {
		String move = "";
		int otheri = 0;
		int otherj = 0;
		int numberOfOldPieces = 0;
		int numberOfNewPieces = 0;

		scanEmpty: {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if ((pieces[i][j].equals(emptyD) || pieces[i][j]
							.equals(emptyL))
							&& !(oldPieces[i][j].equals(emptyD) || oldPieces[i][j]
									.equals(emptyL))) {
						move += letters[i] + (j + 1);
						otheri = i;
						otherj = j;
						break scanEmpty;
					}
				}
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!oldPieces[i][j].equals(emptyD)) {
					numberOfOldPieces++;
				}
				if (!pieces[i][j].equals(emptyD)) {
					numberOfNewPieces++;
				}

				if (i == otheri && j == otherj)
					continue;
				if (!oldPieces[i][j].equals(pieces[i][j])) {
					move += letters[i] + (j + 1);
				}
			}
		}
		checkCastlingRights(move);

		if (move.length() == 8) {
			if (move.equals("e1f1g1h1")) {
				move = "e1g1";

			} else if (move.equals("a1c1d1e1")) {
				move = "e1d1";

			} else if (move.equals("e8f8g8h8")) {
				move = "e8g8";

			} else if (move.equals("a8c8d8e8")) {
				move = "e8d8";
			}
		}

		if (move.length() == 6 || move.length() == 8 || move.length() == 12
				|| move.length() == 16
				|| (numberOfOldPieces - numberOfNewPieces == 2)) {
			moves.add("dummy");
		}

		if (move.length() == 0) {
			boardChanged = false;
			return;
		}
		boardChanged = true;
		if (move.length() > 16)
			throw new ChessPlayerException("Illegal move: " + move);
		moves.add(move);
		System.out.println("Move: " + move);
		System.out.println(moves);
	}
	
	private void checkCastlingRights(String move){
		if(move.contains("e1")){
			castlingRights=castlingRights.replaceAll("KQ", "");
		}
		if(move.contains("a1")){
			castlingRights=castlingRights.replaceAll("Q", "");
		}
		if(move.contains("h1")){
			castlingRights=castlingRights.replaceAll("K", "");
		}
		if(move.contains("e8")){
			castlingRights=castlingRights.replaceAll("kq", "");
		}
		if(move.contains("a8")){
			castlingRights=castlingRights.replaceAll("q", "");
		}
		if(move.contains("h8")){
			castlingRights=castlingRights.replaceAll("k", "");
		}
	}

	private ChessPiece getClosestPiece(BufferedImage image) {

		double percantage = 0;
		double biggestPercantage = 0;
		int blackColor = new Color(0, 0, 0).getRGB();
		ChessPiece closestPiece = null;
		for (ChessPiece piece : piecesList) {
			double same = 0;
			int black = 1;
			int blackCount = 0;
			for (int i = border; i < image.getWidth() - border; i++) {
				for (int j = border; j < image.getHeight() - border; j++) {
					int pieceColor = piece.getImage().getRGB(i, j);
					int imageColor = image.getRGB(i, j);
					if (imageColor== blackColor && pieceColor == blackColor)
						same++;
					if (imageColor == blackColor || pieceColor == blackColor)
						black++;
					if (imageColor == blackColor ){
						blackCount++;
					}
						
				}
			}
			if (blackCount == 0) {
				return emptyD;
			}

			percantage = same/black;
			if (percantage > biggestPercantage) {
				biggestPercantage = percantage;
				closestPiece = piece;
			}
			

		}
		return closestPiece;
	}

	public String getPositionString() {
		StringBuilder pos = new StringBuilder();
		for (String move : moves) {
			pos.append(move).append(" ");
		}
		return pos.toString();
	}

	public void setChessBoard(BufferedImage chessBoard)
			throws ChessPlayerException {
		this.chessBoard = chessBoard;
		scanBoard();
	}

	public void showBoard() {
		for (int j = 7; j >= 0; j--) {
			for (int i = 0; i < 8; i++) {
				System.out.print(pieces[i][j].getType() + " ");
			}
			System.out.println();
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isBoardChanged() {
		return boardChanged;
	}

	public List<String> getMoves() {
		return moves;
	}

	public String getFenString() {
		StringBuilder fen = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			int emptyCount = 0;
			for (int j = 0; j < 8; j++) {
				ChessPiece chessPiece = pieces[j][7 - i];
				if (chessPiece.getType().equals(" ")) {
					emptyCount++;
					continue;
				}
				if (!chessPiece.getType().equals(" ") && emptyCount > 0) {
					fen.append(emptyCount).append(chessPiece.getType());
					emptyCount = 0;
					continue;
				}
				fen.append(chessPiece.getType());
			}
			if (emptyCount > 0)
				fen.append(emptyCount);
			if (i != 7)
				fen.append("/");

		}

		return fen.toString();
	}
	
	public void addMove(String move){
		moves.add(move);
	}
	
	public void clearMoves(){
		boardChanged = true;
		moves = new ArrayList<>();
		pieces = new ChessPiece[8][8];
		oldPieces = new ChessPiece[8][8];
		scanCount = 0;
		castlingRights ="KQkq";
	}

	public List<ChessPiece> getPiecesList() {
		return piecesList;
	}
	
	public String getCastlingRights() {
		return castlingRights;
	}
	public void setCastlingRights(String castlingRights) {
		this.castlingRights = castlingRights;
	}

}
