package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nutsAndBolts.PieceSquareColor;


/**
 * @author francoise.perrin
 * 
 * Cette classe permet de donner une image aux pièces
 *
 */

public class PieceGui extends ImageView implements CheckersPieceGui {
	private PieceSquareColor color;
	
	public PieceGui(Image image, PieceSquareColor color) {
		// ToDo Atelier 2
		this.color = color;
		this.setImage(image);
		

	}
	

	@Override
	public void promote(Image image) {
		// ToDo Atelier 2, utile pour Atelier 3
		this.setImage(image);
		System.out.println("Piece gui promote !");
		
	}

	@Override
	public boolean hasSameColorAsGamer(PieceSquareColor gamerColor) {
		// ToDo Atelier 2, utile pour Atelier 4
		boolean resultat = false;
		if(this.color.equals(gamerColor)) {
			resultat = true;
		}
		return resultat; // à changer 
	}

}