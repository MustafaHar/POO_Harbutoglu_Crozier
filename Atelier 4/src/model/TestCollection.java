package model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 * 
 
 */
public class TestCollection {
	
	public static void main(String[] args) {
		
		// la collection de pièces en jeu - mélange noires et blanches
		Collection<PieceModel> pieces = null ;	
	
		
		/* Implémentation de la Collection par une LinkedList 
		
		* Remplissage 
		* Affichage selon l'ordre d'insertion dans collection - 5 pièces par ligne
		* Puis tests de différents algos de tri avec affichage
		* */
		
		pieces = new LinkedList<PieceModel>();
		pieces = fillCollection(pieces);
		System.out.println("Affichage LinkedList selon l'ordre d'insertion dans collection");
		System.out.println(getRender(pieces)); 
		
		
		
		TestSortList(pieces); // Test de tri de la liste implémentée par une LinkedList
//
//		
//		/* Implémentation de la Collection par une ArrayList 
//		
//		* Remplissage 
//		* Affichage selon l'ordre d'insertion dans collection
//		* Puis tests de différents algos de tri avec affichage
//		* */
//		pieces = new ArrayList<PieceModel>();
//		pieces = fillCollection(pieces);
//		System.out.println("\nAffichage ArrayList selon l'ordre d'insertion dans collection");
//		System.out.println(getRender(pieces));
//		TestSortList(pieces);
//		
//		/* Implémentation de la Collection par un HashSet  
//		
//		* Remplissage 1 série de pions blancs et de pions noirs
//		*    puis 2 séries de pions noirs
//		* Affichage selon l'ordre d'insertion dans collection 
//		*    <-- KO avant MAJ classe AbstractPiece
//		* Puis tests tri <-- KO
//		* */
//		pieces = new HashSet<PieceModel>();
//		pieces = fillCollection(pieces);
//		System.out.println("\nAffichage HashSet selon l'ordre d'insertion dans collection");
//		System.out.println(getRender(pieces));
////		Collections.sort((Set<PieceModel>) pieces); // --> KO compilation : pas tri sur Set
//	
//		
//		/* Implémentation de la Collection par un TreeSet  
//		
//		* Remplissage 
//		* Affichage selon l'ordre d'insertion dans collection 
//		* */
//		pieces = new TreeSet<PieceModel>();
		pieces = new TreeSet<PieceModel>(new PieceComparator());
		pieces = fillCollection(pieces);
		System.out.println("\nAffichage TreeSet selon l'ordre d'insertion dans collection");
		System.out.println(getRender(pieces));
	}

	
	private static Collection<PieceModel> fillCollection(Collection<PieceModel> pieces) {
		
		// Création des pion blancs et ajout dans la collection de pièces
		for ( Coord coord : ModelConfig.WHITE_PIECE_COORDS){
			pieces.add(new PawnModel(coord, PieceSquareColor.WHITE));
		}

		// Création des pions noirs et ajout dans la collection de pièces
		for ( Coord coord : ModelConfig.BLACK_PIECE_COORDS){
			pieces.add(new PawnModel(coord, PieceSquareColor.BLACK));
		}

		//	// Création des pions noirs et ajout à nouveau dans la collection de pièces
		//  // Utile pour vérifier pb hashcode sur les hashSet
//			for ( Coord coord : ModelConfig.BLACK_PIECE_COORDS){
//				pieces.add(new PawnModel(coord, PieceSquareColor.BLACK));
//			}

		return pieces;
	}

	
	private static void TestSortList(Collection<PieceModel> pieces) {

//		// Affichage dans ordre croissant des cases sur le damier (de 0 à 99)
//		// Tri selon ordre naturel des PieceModel
		// pieces = (List<PieceModel>) pieces;
	//	List<PieceModel> list = new ArrayList<> (pieces);
	//	pieces = new ArrayList<> (pieces);
		
		Collections.sort((List<PieceModel>) pieces); 
		System.out.println("Affichage selon l'ordre croissant des cases");
		System.out.println(getRender(pieces));
//
//		// Affichage dans ordre croissant des col puis des lignes
//		// Tri avec un autre comparateur
		Collections.sort((List<PieceModel>)pieces, new PieceComparator()); 
		System.out.println("Affichage selon l'ordre croissant des col puis des lignes");
		System.out.println(getRender(pieces));	
	}
	
	private static String getRender(Collection<PieceModel> pieces) {

		String st = "";
		int i = 1;
		
		// Parcours collection en utilisant boucle for-each
		for(PieceModel piece : pieces ) {
			st += piece;
			if (i++%5 == 0)
				st+= "\n";
		} 
		
		
		
		// Parcours collection en utilisant un itérateur ou un ListIterator
		// TODO
	/*	Iterator<PieceModel> iterator = pieces.iterator();
		// tant qu'il y a un élement suivant
		while(iterator.hasNext()) {
			// on l'ajoute
			st+=iterator.next();
			if (i++%5 == 0)
				st+= "\n";
		} */
		
		return st;	
	}




}

class PieceComparator  implements Comparator<PieceModel>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * 
	 * Permet de comparer en 1er sur les colonnes puis sur les lignes
	 */
	@Override
	public int compare(PieceModel o1, PieceModel o2) {

		int comp = 0;
		
		// TODO
		comp = compareTo(o2, o1); // Appel compareTo de la classe AbstactPieceModel
		
		return comp;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * La méthode compareTo() indique comment comparer un objet à l'objet courant
	 * selon l'ordre dit naturel
	 * Permet de comparer en 1er sur les colonnes puis sur les lignes
	 */
	public int compareTo(PieceModel o, PieceModel current) {
		int res = 0;
		
		int colonne = current.getColonne()-'a'+1;
		int ligne = current.getLigne();
		int oColonne = o.getColonne()-'a'+1;
		int oLigne = o.getLigne();
		
		
		int diffCol = colonne - oColonne;
		int diffLigne = ligne -  oLigne;
		// Sur des colonnes differentes avec suivante superieur
		if((diffCol >= 1)) {
			res = 1;
				
		}
		// Sur meme colonne comparer alors les lignes
		else if(diffCol == 0) {
			// Ligne suivante superieur
			if(diffLigne >= 1) {
				res = 1;
			}
			// Ligne suivante inferieur
			else {
				res = -1;
			}
		}
		// Colonne differentes et suivante inférieur
		else {
			res = -1;
		}
		return res ;
		// TODO Auto-generated method stub
	}
}


