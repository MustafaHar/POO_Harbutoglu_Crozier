package model;


import java.util.LinkedList;
import java.util.List;

import nutsAndBolts.PieceSquareColor;
/**
 * @author francoiseperrin
 *
 *le mode de déplacement et de prise de la reine est différent de celui du pion
 */
// Get colonne et tout... dans AbstractPieceModel
public class QueenModel extends AbstractPieceModel {
	
	public QueenModel(Coord coord, PieceSquareColor pieceColor) {
		super(coord, pieceColor);
	}

	
	@Override
	// A faire
	public boolean isMoveOk(Coord targetCoord, boolean isPieceToCapture) {
		boolean ret = false;
		
		// TODO atelier 3
		if(targetCoord != null) {
			int colDistance = targetCoord.getColonne() - this.getColonne();
			int ligDistance = targetCoord.getLigne() - this.getLigne();
			int deltaLig = (int) Math.signum(ligDistance);
			
			// Cas d'un déplacement en diagonale
			if (Math.abs(colDistance) == Math.abs(ligDistance)){
				ret = true;
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

}

