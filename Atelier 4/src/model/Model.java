package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.OutputModelData;
import nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 *
 * Cette classe gère les aspects métiers du jeu de dame
 * indépendamment de toute vue
 * 
 * Elle délègue à son objet ModelImplementor 
 * le stockage des PieceModel dans une collection
 * 
 * Les pièces sont capables de se déplacer d'une case en diagonale 
 * si la case de destination est vide
 * 
 * Ne sont pas gérés les prises, les rafles, les dames, 
 * 
 * N'est pas géré le fait que lorsqu'une prise est possible
 * une autre pièce ne doit pas être jouée
 * 
 */
public class Model implements BoardGame<Coord> {

	private PieceSquareColor currentGamerColor;	// couleur du joueur courant
 
	private ModelImplementor implementor;		// Cet objet sait communiquer avec les PieceModel
	
	
	private HashMap<PieceSquareColor, Joueur> liste_joueur;
	
	//private Coord coordRafle = null;
	//this.listJoueur.add(J1);
	
	//listJoueur.add(J1);
			
//	this.listeJoueur.add(new Joueur(PieceSquareColor.WHITE, 0));
	//this.listeJoueur.add(new Joueur(PieceSquareColor.BLACK, 0));
	

	public Model() {
		super();
		this.implementor = new ModelImplementor();
		this.currentGamerColor = ModelConfig.BEGIN_COLOR;
		liste_joueur = new HashMap<PieceSquareColor, Joueur>();
		
		// Créer les joueurs et les ajouter dans la liste de joueur (couleur : joueur)
		Joueur J1 = new Joueur(PieceSquareColor.WHITE);
		Joueur J2 = new Joueur(PieceSquareColor.BLACK);
		this.liste_joueur.put(J1.getColor(), J1);
		this.liste_joueur.put(J2.getColor(), J2);
		
		System.out.println(this);
	}

	@Override
	public String toString() {
		return implementor.toString();
	}



	/**
	 * Actions potentielles sur le model : move, capture, promotion pion, rafles
	 */
	@Override
	public OutputModelData<Coord> moveCapturePromote(Coord toMovePieceCoord, Coord targetSquareCoord) {

		OutputModelData<Coord> outputModelData = null;
		
		boolean isMoveDone = false;
		Coord toCapturePieceCoord = null;
		Coord toPromotePieceCoord = null;
		PieceSquareColor toPromotePieceColor = this.currentGamerColor;
		Joueur joueurCourant = this.liste_joueur.get(currentGamerColor);
		int score = this.getGamerScore(joueurCourant);
		
		// Si la pièce est déplaçable (couleur du joueur courant et case arrivée disponible)
		if (this.isPieceMoveable(toMovePieceCoord, targetSquareCoord)) {
			// S'il n'existe pas plusieurs pièces sur le chemin
			if (this.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord)) {
				//Recherche coord de l'éventuelle pièce à prendre
				toCapturePieceCoord = this.getToCapturePieceCoord(toMovePieceCoord, targetSquareCoord);
				System.out.println("Piece a capturer = "+ toCapturePieceCoord);
				// System.out.println("to capture coord = "+toCapturePieceCoord);
				// si le déplacement est légal (en diagonale selon algo pion ou dame)
				boolean isPieceToCapture = toCapturePieceCoord != null;
				
				System.out.println("Mouvement possible = "+ this.isMovePiecePossible(toMovePieceCoord, targetSquareCoord, isPieceToCapture));
				if (this.isMovePiecePossible(toMovePieceCoord, targetSquareCoord, isPieceToCapture)) {
				//	if(this.isLePlusRentable(toMovePieceCoord, targetSquareCoord)) {
						// déplacement effectif de la pièce
						this.movePiece(toMovePieceCoord, targetSquareCoord);
						isMoveDone = true;
					//	System.out.println("isMoveDone = "+isMoveDone);
						// suppression effective de la pièce prise 
						this.remove(toCapturePieceCoord);
						// Si deplacement OK et qu'il y a une piece sur le chemin on augmente le score du joueur courant
						if(isPieceToCapture) {
							this.increaseScorePlayer(joueurCourant);
							score = this.getGamerScore(joueurCourant);
						}
						// promotion éventuelle de la pièce après déplacement 
						if (this.isPromotable(targetSquareCoord)) {	
							// TODO : Test à changer atelier 3
							toPromotePieceCoord = this.promote(targetSquareCoord);
							// ajouter to promot piece coord et to promot piece color a l'outputcontroller
							// TODO atelier 3
						}
						
						// S'il n'y a pas eu de prise
						// ou si une rafle n'est pas possible alors changement de joueur 
						if (!this.isRafle(targetSquareCoord, isPieceToCapture)) {	// TODO : Test à changer atelier 4
							System.out.println("Pas de rafle !");
							this.setNextPieceToTake(null);
							this.switchGamer();
						}
						// Si il y a une rafle possible on enregistre la piece à prendre pour le prochain tour
						else {
							System.out.println("Rafle OK");
							this.setNextPieceToTake(this.searchPieceToTake(targetSquareCoord));
						}
	
					}
				}
		//	}
		}
		System.out.println(this);

		// Constitution objet de données avec toutes les infos nécessaires à la view
		outputModelData = new OutputModelData<Coord>(
				isMoveDone, 
				toCapturePieceCoord, 
				toPromotePieceCoord, 
				toPromotePieceColor,
				score);

		return outputModelData;

	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return true si la PieceModel à déplacer est de la couleur du joueur courant 
	 * et que les coordonnées d'arrivées soient dans les limites du tableau
	 * et qu'il n'y ait pas de pièce sur la case d'arrivée
	 */
	boolean isPieceMoveable(Coord toMovePieceCoord, Coord targetSquareCoord) { // TODO : remettre en "private" après test unitaires
		boolean bool = false;
		
		// TODO : à compléter atelier 4 pour gérer les rafles 
		
		bool = 	this.implementor.isPiecehere(toMovePieceCoord) 
				&& this.implementor.getPieceColor(toMovePieceCoord) == this.currentGamerColor 
				&& Coord.coordonnees_valides(targetSquareCoord) 
				&& !this.implementor.isPiecehere(targetSquareCoord) ;

		return bool ;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return true s'il n'existe qu'1 seule pièce à prendre d'une autre couleur sur la trajectoire
	 * ou pas de pièce à prendre
	 */
	private boolean isThereMaxOnePieceOnItinerary(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean isThereMaxOnePieceOnItinerary = false; // TODO Atelier 2 - initialiser à false

		// TODO Atelier 2
		isThereMaxOnePieceOnItinerary = this.implementor.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord);
		
		return isThereMaxOnePieceOnItinerary;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return les coord de la pièce à prendre, null sinon
	 */
	private Coord getToCapturePieceCoord(Coord toMovePieceCoord, Coord targetSquareCoord) {
		Coord toCapturePieceCoord = null;
		// TODO Atelier 2
		// Recuperer la piece sur le chemin parmis la liste des coordonnées a traverser
		for(Coord coordVerif : this.implementor.getCoordsOnItinerary(toMovePieceCoord, targetSquareCoord)) {
			if(this.implementor.isPiecehere(coordVerif)) {
				toCapturePieceCoord = coordVerif;
				break;
			}
		}
		return toCapturePieceCoord;
	}

	/**
	 * @param initCoord
	 * @param targetCoord
	 * @param isPieceToCapture
	 * @return true si le déplacement est légal
	 * (s'effectue en diagonale, avec ou sans prise)
	 * La PieceModel qui se trouve aux coordonnées passées en paramètre 
	 * est capable de répondre à cette question (par l'intermédiare du ModelImplementor)
	 */
	boolean isMovePiecePossible(Coord toMovePieceCoord, Coord targetSquareCoord, boolean isPieceToCapture) { // TODO : remettre en "private" après test unitaires
		boolean isMovePossible = false;
		boolean isLePlusRentable = false;
		boolean result = false;
		isMovePossible = this.implementor.isMovePieceOk(toMovePieceCoord, targetSquareCoord, isPieceToCapture);
		
		// Si le deplacement est possible
		if(isMovePossible) {
			// Si ce deplacement est le plus rentable
			isLePlusRentable = this.isLePlusRentable(toMovePieceCoord, targetSquareCoord);
			System.out.println("isLePlusRentable = "+isLePlusRentable);
			if(isLePlusRentable) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * Déplacement effectif de la PieceModel
	 */
	 void movePiece(Coord toMovePieceCoord, Coord targetSquareCoord) { // TODO : remettre en "private" après test unitaires
		this.implementor.movePiece(toMovePieceCoord, targetSquareCoord);
	}

	/**
	 * @param toCapturePieceCoord
	 * Suppression effective de la pièce capturée
	 */
	private void remove(Coord toCapturePieceCoord) { 
		this.implementor.removePiece(toCapturePieceCoord);
	}

	
	 void switchGamer() { // TODO : remettre en "private" après test unitaires
		this.currentGamerColor = (PieceSquareColor.WHITE).equals(this.currentGamerColor) ?
				PieceSquareColor.BLACK : PieceSquareColor.WHITE;
		
	}
	 
	 
	 public boolean isPromotable(Coord coord) {
		 boolean result = this.implementor.isPromotable(coord);
		 return result;
	 }

	 public Coord promote(Coord coord) {
		 this.implementor.promote(coord);
		 return coord;
	 }
	 
	 // Augmenter le score du joueur
	 public void increaseScorePlayer(Joueur joueur) {
		 joueur.upScore();
	 }
	 
	 public int getGamerScore(Joueur joueur) {
		 int score = joueur.getScore();
		 return score;
	 }
	 
	 // Indiquer si il y a une rafle possible ou pas lorsqu'on a mangé un pion
	 public boolean isRafle(Coord coord, boolean isMoveDone) {
		 boolean result = false;
		 Coord coordRafle = null;
		 // Si il y a eu une piece de prise
		 if(isMoveDone) {
			 coordRafle = this.searchPieceToTake(coord);
		//	 System.out.println("isRafle : "+coordRafle);
			 // On cherche si il y a une rafle possible
			 if(coordRafle != null) {
				 result = true;
			 }
		 }
		 return result;
	 }
	 
	 // Chercher l'éventuelle pièce à prendre pour la piece courante
	 public Coord searchPieceToTake(Coord coord) {
		 Coord coordRafle;
		coordRafle = this.implementor.searchRafle(coord);
		return coordRafle;
	 }
	 
	// Indiqué si le coup souhaité est le plus rentable
	public boolean isLePlusRentable(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean result = false;
	
		// Chercher l'éventuelle piece à prendre
		Coord coordToTake = this.searchPieceToTake(toMovePieceCoord);
		Coord coordNextPieceToTake = this.implementor.getNextPieceToTake(); // Récupérer les coordonnées de la prochaine piece à prendre obligatoirement
		
		// Si cela correspond à la rafle possible ou si il n'y a pas de rafle
		if(coordNextPieceToTake == null || coordNextPieceToTake.equals(targetSquareCoord)) {
			// Si aucune pieces à prendre pour la piece courante
			if(coordToTake == null) {
				result = true;
			}
			// Si la piece à prendre possède les mêmes coordonnées que la cible
			else if (coordToTake.equals(targetSquareCoord)) {
				result = true;
			}
		}
		else {
			result = false;
		}
		
		return result;
	}
	
	// Changer les coordonnées de la prochaine piece a prendre obligatoirement
	public void setNextPieceToTake(Coord coord) {
		this.implementor.setNextPieceToTake(coord);
	}
	
	
}