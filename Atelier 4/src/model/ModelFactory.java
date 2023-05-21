package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import nutsAndBolts.PieceSquareColor;

public class ModelFactory {

	public static Collection<PieceModel> createPieceModelCollection() {
		
//		Collection<PieceModel> pieces = new LinkedList<PieceModel>();
		Collection<PieceModel> pieces = new ArrayList<PieceModel>();
//		 Collection<PieceModel> pieces = new HashSet<PieceModel>();
//		 Collection<PieceModel> pieces = new TreeSet<PieceModel>();
				
		// Création des pion blancs et ajout dans la collection de pièces
		for ( Coord coord : ModelConfig.WHITE_PIECE_COORDS){
			pieces.add(new PawnModel(coord, PieceSquareColor.WHITE));
		}

		// Création des pions noirs et ajout dans la collection de pièces
		for ( Coord coord : ModelConfig.BLACK_PIECE_COORDS){
			pieces.add(new PawnModel(coord, PieceSquareColor.BLACK));
		}
		return pieces;
	}
}
