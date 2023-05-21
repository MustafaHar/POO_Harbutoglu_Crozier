package model;


import java.util.Collection;
import java.util.List;

import nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 * 
 * Cete classe fabrique et stocke toutes les PieceModel du Model dans une collection 
 * elle est donc responsable de rechercher et mettre � jour les PieceModel (leur position)
 * En r�alit�, elle d�l�gue � une fabrique le soin de fabriquer les bonnes PieceModel 
 * avec les bonnes coordonn�es
 * 
 * En revanche, elle n'est pas responsable des algorithme m�tiers li�s au d�placement des pi�ces
 * (responsabilit� de la classe Model)
 */
public class ModelImplementor {

	// la collection de pi�ces en jeu - m�lange noires et blanches
	private Collection<PieceModel> pieces ;	
	private Coord coordRafle = null;
	
	public ModelImplementor() {
		super();

		pieces = ModelFactory.createPieceModelCollection();
	}

	public PieceSquareColor getPieceColor(Coord coord) {
		PieceSquareColor color = null;
		PieceModel piece = this.findPiece(coord);

		if (piece != null) {
			color = piece.getPieceColor();
		}
		return color;
	}

	public boolean isPiecehere(Coord coord) {
		return this.findPiece(coord) != null;
	}

	public boolean isMovePieceOk(Coord initCoord, Coord targetCoord, boolean isPieceToTake) {

		boolean isMovePieceOk = false;
		PieceModel initPiece = this.findPiece(initCoord);
		if (initPiece != null) {
			isMovePieceOk = initPiece.isMoveOk(targetCoord, isPieceToTake ) ;
		}
		return isMovePieceOk;
	}


	public boolean movePiece(Coord initCoord, Coord targetCoord) {

		boolean isMovePieceDone = false;
		PieceModel initPiece = this.findPiece(initCoord);
		if (initPiece != null) {

			// d�placement pi�ce
			initPiece.move(targetCoord) ;
			isMovePieceDone = true;
		}
		return isMovePieceDone;
	}

	public void removePiece(Coord pieceToTakeCoord) {

		// TODO Atelier 2
		PieceModel initPiece = this.findPiece(pieceToTakeCoord);
		if (initPiece != null) {
			// supression de la piece dans la liste de piece model (remove = contraire de append ou add)
			this.pieces.remove(initPiece);
		}
	}

	
	public List<Coord> getCoordsOnItinerary(Coord initCoord, Coord targetCoord) {
		List<Coord> coordsOnItinerary = null;
		
		// TODO Atelier 2
		// On r�cup�re la piece correspondant aux coordonn�es et on r�cup�re toutes les coordonn�es sur son chemin cible
		PieceModel initPiece = this.findPiece(initCoord);
		if (initPiece != null) {
			coordsOnItinerary = initPiece.getCoordsOnItinerary(targetCoord);
		//	System.out.println("implementor coord itinerary = " + initPiece.getCoordsOnItinerary(targetCoord).size());
		}else{
			System.out.println("piece introuvable ");
		}
		
		
		
		return coordsOnItinerary;
	}

	
	/**
	 * @param coord
	 * @return la pi�ce qui se trouve aux coordonn�es indiqu�es
	 */
	 PieceModel findPiece(Coord coord) {		// TODO : remettre en "private" apr�s test unitaires
		PieceModel findPiece = null;

		for(PieceModel piece : this.pieces) {

			if(coord != null && piece.hasThisCoord(coord)) {
				findPiece = piece;
				break;
			}
		}
		return findPiece;
	}


	 
	 public boolean isPromotable(Coord coord) {
		 boolean result = false;
		 PawnModel piece = null;
		 // Recuperer la piece a verifier
		 if(coord != null) {
			 // Si c'est une instance de Promotable (si �a correspond a un PawnModel)
			 if(this.findPiece(coord) instanceof Promotable) {
				 piece = (PawnModel) this.findPiece(coord);
			 }
			 
		 }
		
		 // Verifier si on peut la promouvoir
		if((piece != null)&&(piece.isPromotable())) {
			result = true;
		}
		
		return result;
	 }
	 
	 public void promote(Coord coord) {
		 // Recuperer la couleur avant de supprimer la piece de la liste
		 PieceSquareColor color = this.getPieceColor(coord);
		 // Supprimer la piece de la liste
		 this.removePiece(coord);
		 pieces.add(new QueenModel(coord, color));
	 }

	 
	 
	
	public boolean isThereMaxOnePieceOnItinerary(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean isThereMaxOnePieceOnItinerary = false; // TODO Atelier 2 - initialiser � false

		// TODO Atelier 2
		// Compter combien de pieces sur le chemin
		int compteur = 0;
	//	System.out.println("itineraire : "+this.implementor.getCoordsOnItinerary(toMovePieceCoord, targetSquareCoord));
		for(Coord coord : this.getCoordsOnItinerary(toMovePieceCoord, targetSquareCoord)) {
			if(this.isPiecehere(coord)) {
				compteur++;
			}
		}
		// Si il y a une piece max sur le chemin
		if(compteur <= 1) {
			isThereMaxOnePieceOnItinerary = true;
		}
		
		return isThereMaxOnePieceOnItinerary;
	}
		
	// Chercher l'�ventuelle pi�ce � prendre pour la piece courante
	 public Coord searchRafle(Coord coord) {
		 int len = ModelConfig.LENGTH;
		 Coord coord_temp;
		 Coord coordRafle = null;
			
		 // Pour chaque coordonn�es du tableau on teste si on peut se deplacer dessus en prenant une piece (rafle)
		 for(int ligne = 1; ligne <= len; ligne++) {
			 for(int colonne = 1; colonne <= len; colonne ++) {
				// Cr�er la coordonn�es
				coord_temp = new Coord((char) (colonne+'a'), ligne);
				// R�cup�rer les coordonn�es de la pi�ce � rafler
				coordRafle = this.searchPieceToTake(coord, coord_temp);
				// On renvois la rafle si il y en a une
				if(coordRafle != null) {
					//System.out.println("Rafle OK");
					return coordRafle;
					// break;
				}
			 }
		 }
		return coordRafle;
	 }
		 
	 // indiquer si le joueur peut continuer � prendre des pi�ces
	 public Coord searchPieceToTake(Coord coord, Coord coord_cible) {
		 PieceModel myPiece;
		 // Pour chaque cases
		 Coord coord_temp = coord_cible;
		 Coord coordRafle = null;
		 int number = 0;
		 // Si il n'y a pas de piece sur la case de destination
		 if(!isPiecehere(coord_cible) && (isPiecehere(coord))) {
			 myPiece = this.findPiece(coord);
			 
			// On teste si il y a des pi�ces � prendre sur le chemin de destination
			 for(Coord coordVerif : getCoordsOnItinerary(coord, coord_temp)) {
				 		// Si il y a une piece et qu'on peut la prendre
						if(isPiecehere(coordVerif)&& isMovePieceOk(coord, coord_temp, true)) {
							number++;
							if(number>1) {
								return null;
							}
							// Si piece pas de la meme couleur que le joueur courant
						 	if(!(myPiece.getPieceColor().equals(this.findPiece(coordVerif).getPieceColor()))){
								// On renvoie les coordonn�es de la piece � rafler
								 System.out.println("Piece � prendre : "+ coordVerif+" destination : "+coord_temp+ " pour piece "+coord);
								 coordRafle = coord_temp;
								 return coordRafle;
								 // break;
						}
					}
				}
			}
		 return coordRafle;
	 }
	
	 // R�cup�rer les coordonn�es de la pi�ces � rafler
	 public Coord getNextPieceToTake() {
		 Coord coordRafle = this.coordRafle;
		 return coordRafle;
	 }
	 
	 // Changer les coordonn�es de la prochaine piece � prendre obligatoirement
	 public void setNextPieceToTake(Coord coord) {
			this.coordRafle = coord;
	 }
	 
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 
	 * La m�thode toStrong() retourne une repr�sentation 
	 * de la liste de pi�ces sous forme d'un tableau 2D
	 * 
	 */
	public String toString() {


		String st = "";
		String[][] damier = new String[ModelConfig.LENGTH][ModelConfig.LENGTH];

		// cr�ation d'un tableau 2D avec les noms des pi�ces � partir de la liste de pi�ces
		for(PieceModel piece : this.pieces) {

			PieceSquareColor color = piece.getPieceColor();
			String stColor = (PieceSquareColor.WHITE.equals(color) ? "--B--" : "--N--" );

			int col = piece.getColonne() -'a';
			int lig = piece.getLigne() -1;
			damier[lig][col ] = stColor ;
		}

		// Affichage du tableau formatt�
		st = "     a      b      c      d      e      f      g      h      i      j\n";
		for ( int lig = 9; lig >=0 ; lig--) {
			st += (lig+1) + "  ";
			for ( int col = 0; col <= 9; col++) {					 
				String stColor = damier[lig][col];				
				if (stColor != null) {						
					st += stColor + "  ";	
				} 
				else {
					st += "-----  ";
				}
			}
			st +="\n";
		}
		
		return "\nDamier du model \n" + st;	
	}

}
