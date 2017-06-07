package recognition;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ChessPiece {
	private String type;
	private double hash;
	private static final int dif = 14;
	private BufferedImage image;

	public ChessPiece(BufferedImage piece, String type) {
		this.type = type;
		image = piece;
		hash = getHashOfSquare(piece);
		
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChessPiece))
			return false;
		ChessPiece cp = (ChessPiece) obj;
		return cp.getType().equals(type);
	}

	public static double getHashOfSquare(BufferedImage square) {
		double hash1 = 0;
		int black = new Color(0, 0, 0).getRGB();
		int blackDif = new Color(50, 50, 50).getRGB();

		for (int i = 0; i < square.getHeight() - dif; i++) {
			for (int j = 0; j < square.getWidth(); j++) {
				int color = square.getRGB(i, j);
				if (color >= black && color <= blackDif) {
					hash1++;
				} 
			}
		}
		return hash1;
	}

	public String getType() {
		return type;
	}

	public double getHash() {
		return hash;
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
