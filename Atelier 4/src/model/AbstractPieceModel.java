package model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import nutsAndBolts.PieceSquareColor;

// Classe abstraite pour factoriser PawnModel et QueenModel
public abstract class AbstractPieceModel implements PieceModel {
	
		protected Coord coord;
		protected PieceSquareColor pieceColor; 
		
		public AbstractPieceModel(Coord coord, PieceSquareColor pieceColor) {
			this.coord = coord;
			this.pieceColor = pieceColor;
		}
		
		@Override
		public char getColonne() {
			return coord.getColonne();
		}
		@Override
		public int getLigne() {
			return coord.getLigne();
		}
		@Override
		public boolean hasThisCoord(Coord coord) {
			return this.coord.equals(coord);
		}
	
		@Override
		public void move(Coord coord) {
			if(coord != null) {
				this.coord = coord;
			}	 
		}
		
		@Override
		public PieceSquareColor getPieceColor() {
			return pieceColor;
		}
		
		@Override
		public List<Coord> getCoordsOnItinerary(Coord targetCoord) {

			List<Coord> coordsOnItinery = new LinkedList<Coord>(); 
			
			// TODO Atelier 2 coordonnées entre celles perso et celle cible
			int colDistance = targetCoord.getColonne() - this.getColonne();
			int ligDistance = targetCoord.getLigne() - this.getLigne();
			
			// deltaLig renvoie -1 si c'est aux noirs et +1 si c'est aux blancs
			int deltaLig = (int) Math.signum(ligDistance);
			int colonne = this.getColonne();
			int ligne = this.getLigne();
			
			// Pour chaque coordonnées sur le chemin on récupère sa ligne+colonne
			for(int i = 0; i <  Math.abs(colDistance) ; i++ ) {
				// deltaLig renvoie -1 si c'est aux noirs et +1 si c'est aux blancs
				// Si c'est un mouvement vers la gauche
				if(colDistance < 0) {
					colonne = colonne - 1; 
				}
				// Si c'est un mouvement vers la droite
				else {
					colonne = colonne + 1; 
				}
				
				ligne = ligne + deltaLig;
				Coord currentCoord = new Coord( (char) colonne, ligne);
				//on l'ajoute dans la liste
				coordsOnItinery.add(currentCoord);
			}
			return coordsOnItinery;
		}
		
	
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 * 
		 * La méthode compareTo() indique comment comparer un objet à l'objet courant
		 * selon l'ordre dit naturel
		 * Permet de comparer selon l'ordre croissant des cases
		 */
		@Override
		public int compareTo(PieceModel o) {
			int MAX = this.coord.MAX;
			// basé sur compareTo de la classe Coord
			int thisValue = (MAX-this.getLigne())*MAX + (this.getColonne()-'a'+1);
			int oValue = (MAX-o.getLigne())*MAX + (o.getColonne()-'a'+1);
			return thisValue - oValue ;
		}
		
		
		// Générer automatiquement
//		@Override
//		public int hashCode() {
//			return Objects.hash(coord, pieceColor);
//		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((coord == null) ? 0 : coord.hashCode());
			result = prime * result + ((pieceColor == null) ? 0 : pieceColor.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof AbstractPieceModel)) {
				return false;
			}
			AbstractPieceModel other = (AbstractPieceModel) obj;
			if (coord == null) {
				if (other.coord != null) {
					return false;
				}
			} else if (!coord.equals(other.coord)) {
				return false;
			}
			if (pieceColor != other.pieceColor) {
				return false;
			}
			return true;
		} 
		
		
		
//		public abstract boolean isMoveOk(Coord targetCoord, boolean isPieceToCapture);
		
}
