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
 * Cette classe g�re les aspects m�tiers du jeu de dame
 * ind�pendamment de toute vue
 * 
 * Elle d�l�gue � son objet ModelImplementor 
 * le stockage des PieceModel dans une collection
 * 
 * Les pi�ces sont capables de se d�placer d'une case en diagonale 
 * si la case de destination est vide
 * 
 * Ne sont pas g�r�s les prises, les rafles, les dames, 
 * 
 * N'est pas g�r� le fait que lorsqu'une prise est possible
 * une autre pi�ce ne doit pas �tre jou�e
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
		
		// Cr�er les joueurs et les ajouter dans la liste de joueur (couleur : joueur)
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
		
		// Si la pi�ce est d�pla�able (couleur du joueur courant et case arriv�e disponible)
		if (this.isPieceMoveable(toMovePieceCoord, targetSquareCoord)) {
			// S'il n'existe pas plusieurs pi�ces sur le chemin
			if (this.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord)) {
				//Recherche coord de l'�ventuelle pi�ce � prendre
				toCapturePieceCoord = this.getToCapturePieceCoord(toMovePieceCoord, targetSquareCoord);
				System.out.println("Piece a capturer = "+ toCapturePieceCoord);
				// System.out.println("to capture coord = "+toCapturePieceCoord);
				// si le d�placement est l�gal (en diagonale selon algo pion ou dame)
				boolean isPieceToCapture = toCapturePieceCoord != null;
				
				System.out.println("Mouvement possible = "+ this.isMovePiecePossible(toMovePieceCoord, targetSquareCoord, isPieceToCapture));
				if (this.isMovePiecePossible(toMovePieceCoord, targetSquareCoord, isPieceToCapture)) {
				//	if(this.isLePlusRentable(toMovePieceCoord, targetSquareCoord)) {
						// d�placement effectif de la pi�ce
						this.movePiece(toMovePieceCoord, targetSquareCoord);
						isMoveDone = true;
					//	System.out.println("isMoveDone = "+isMoveDone);
						// suppression effective de la pi�ce prise 
						this.remove(toCapturePieceCoord);
						// Si deplacement OK et qu'il y a une piece sur le chemin on augmente le score du joueur courant
						if(isPieceToCapture) {
							this.increaseScorePlayer(joueurCourant);
							score = this.getGamerScore(joueurCourant);
						}
						// promotion �ventuelle de la pi�ce apr�s d�placement 
						if (this.isPromotable(targetSquareCoord)) {	
							// TODO : Test � changer atelier 3
							toPromotePieceCoord = this.promote(targetSquareCoord);
							// ajouter to promot piece coord et to promot piece color a l'outputcontroller
							// TODO atelier 3
						}
						
						// S'il n'y a pas eu de prise
						// ou si une rafle n'est pas possible alors changement de joueur 
						if (!this.isRafle(targetSquareCoord, isPieceToCapture)) {	// TODO : Test � changer atelier 4
							System.out.println("Pas de rafle !");
							this.setNextPieceToTake(null);
							this.switchGamer();
						}
						// Si il y a une rafle possible on enregistre la piece � prendre pour le prochain tour
						else {
							System.out.println("Rafle OK");
							this.setNextPieceToTake(this.searchPieceToTake(targetSquareCoord));
						}
	
					}
				}
		//	}
		}
		System.out.println(this);

		// Constitution objet de donn�es avec toutes les infos n�cessaires � la view
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
	 * @return true si la PieceModel � d�placer est de la couleur du joueur courant 
	 * et que les coordonn�es d'arriv�es soient dans les limites du tableau
	 * et qu'il n'y ait pas de pi�ce sur la case d'arriv�e
	 */
	boolean isPieceMoveable(Coord toMovePieceCoord, Coord targetSquareCoord) { // TODO : remettre en "private" apr�s test unitaires
		boolean bool = false;
		
		// TODO : � compl�ter atelier 4 pour g�rer les rafles 
		
		bool = 	this.implementor.isPiecehere(toMovePieceCoord) 
				&& this.implementor.getPieceColor(toMovePieceCoord) == this.currentGamerColor 
				&& Coord.coordonnees_valides(targetSquareCoord) 
				&& !this.implementor.isPiecehere(targetSquareCoord) ;

		return bool ;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return true s'il n'existe qu'1 seule pi�ce � prendre d'une autre couleur sur la trajectoire
	 * ou pas de pi�ce � prendre
	 */
	private boolean isThereMaxOnePieceOnItinerary(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean isThereMaxOnePieceOnItinerary = false; // TODO Atelier 2 - initialiser � false

		// TODO Atelier 2
		isThereMaxOnePieceOnItinerary = this.implementor.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord);
		
		return isThereMaxOnePieceOnItinerary;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return les coord de la pi�ce � prendre, null sinon
	 */
	private Coord getToCapturePieceCoord(Coord toMovePieceCoord, Coord targetSquareCoord) {
		Coord toCapturePieceCoord = null;
		// TODO Atelier 2
		// Recuperer la piece sur le chemin parmis la liste des coordonn�es a traverser
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
	 * @return true si le d�placement est l�gal
	 * (s'effectue en diagonale, avec ou sans prise)
	 * La PieceModel qui se trouve aux coordonn�es pass�es en param�tre 
	 * est capable de r�pondre �cette question (par l'interm�diare du ModelImplementor)
	 */
	boolean isMovePiecePossible(Coord toMovePieceCoord, Coord targetSquareCoord, boolean isPieceToCapture) { // TODO : remettre en "private" apr�s test unitaires
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
	 * D�placement effectif de la PieceModel
	 */
	 void movePiece(Coord toMovePieceCoord, Coord targetSquareCoord) { // TODO : remettre en "private" apr�s test unitaires
		this.implementor.movePiece(toMovePieceCoord, targetSquareCoord);
	}

	/**
	 * @param toCapturePieceCoord
	 * Suppression effective de la pi�ce captur�e
	 */
	private void remove(Coord toCapturePieceCoord) { 
		this.implementor.removePiece(toCapturePieceCoord);
	}

	
	 void switchGamer() { // TODO : remettre en "private" apr�s test unitaires
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
	 
	 // Indiquer si il y a une rafle possible ou pas lorsqu'on a mang� un pion
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
	 
	 // Chercher l'�ventuelle pi�ce � prendre pour la piece courante
	 public Coord searchPieceToTake(Coord coord) {
		 Coord coordRafle;
		coordRafle = this.implementor.searchRafle(coord);
		return coordRafle;
	 }
	 
	// Indiqu� si le coup souhait� est le plus rentable
	public boolean isLePlusRentable(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean result = false;
	
		// Chercher l'�ventuelle piece � prendre
		Coord coordToTake = this.searchPieceToTake(toMovePieceCoord);
		Coord coordNextPieceToTake = this.implementor.getNextPieceToTake(); // R�cup�rer les coordonn�es de la prochaine piece � prendre obligatoirement
		
		// Si cela correspond � la rafle possible ou si il n'y a pas de rafle
		if(coordNextPieceToTake == null || coordNextPieceToTake.equals(targetSquareCoord)) {
			// Si aucune pieces � prendre pour la piece courante
			if(coordToTake == null) {
				result = true;
			}
			// Si la piece � prendre poss�de les m�mes coordonn�es que la cible
			else if (coordToTake.equals(targetSquareCoord)) {
				result = true;
			}
		}
		else {
			result = false;
		}
		
		return result;
	}
	
	// Changer les coordonn�es de la prochaine piece a prendre obligatoirement
	public void setNextPieceToTake(Coord coord) {
		this.implementor.setNextPieceToTake(coord);
	}
	
	
}