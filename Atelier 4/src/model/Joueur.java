package model;

import nutsAndBolts.PieceSquareColor;

public class Joueur {
	private int score;
	PieceSquareColor couleur;
	
	Joueur(PieceSquareColor couleur){
		this.couleur = couleur;
		this.score = 0;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void upScore(){
		this.score+=1;
	}
	
	public PieceSquareColor getColor() {
		return this.couleur;
	}
}
