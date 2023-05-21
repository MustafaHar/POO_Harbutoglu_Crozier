package model;


import java.util.LinkedList;
import java.util.List;

import nutsAndBolts.PieceSquareColor;

public class PawnModel extends AbstractPieceModel implements Promotable{

	private int direction;
	
	public PawnModel(Coord coord, PieceSquareColor pieceColor) {
		// Envoyer les attributs à la classes abstraites
		super(coord, pieceColor);
		this.direction = PieceSquareColor.BLACK.equals(this.getPieceColor()) ? -1 : 1;
	}

	@Override
	public boolean isMoveOk(Coord targetCoord, boolean isPieceToCapture) {
		boolean ret = false;
		
		if(targetCoord != null) {
			int colDistance = targetCoord.getColonne() - this.getColonne();
			int ligDistance = targetCoord.getLigne() - this.getLigne();
			int deltaLig = (int) Math.signum(ligDistance);
			
			// Cas d'un déplacement en diagonale
			if (Math.abs(colDistance) == Math.abs(ligDistance)){
				
				// sans prise
				if (!isPieceToCapture) {
					if (deltaLig == this.direction && Math.abs(colDistance) == 1) {
						ret = true;
					}
				}
				// avec prise
				else {
					if (Math.abs(colDistance) == 2) {
						ret = true;
					}
				}
			}
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " ["+pieceColor.toString().charAt(0) + coord + "]";
	}

	// Vérifier si on peut promouvoir la piece en dame
	@Override
	public boolean isPromotable() {
		// TODO Auto-generated method stub
		boolean result = false;
		// Ligne la plus haut et ligne la plus basse
		int lastLigne = Coord.MAX;
		int firstLigne = Coord.MAX -(Coord.MAX-1);
		
		// Si c'est un blanc sur la ligne du haut
		if((this.direction == 1)&&(this.getLigne() == lastLigne)) {
			result = true;
		}
		// Si c'est un noir sur la ligne du bas
		else if ((this.direction == -1)&&(this.getLigne() == firstLigne)){
			result = true;
		}
		return result;
	}

	@Override
	public void promote() {
		// TODO Auto-generated method stub
		// Ne fait rien selon documentation TP
		
	}



	

	
}

