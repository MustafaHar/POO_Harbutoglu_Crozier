package gui;

import controller.InputViewData;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 * 
 * Cette classe est la fenêtre du jeu de dames
 * Elle délègue a un objet Board la gestion de l'affichage du damier
 * et affiche les axes le long du damier
 * 
 */

public class View extends BorderPane {

	// le damier composé de carrés noirs et blancs
	// sur lesquels sont positionnés des pièces noires ou blanches
	Pane board ;
	Pane score;
	BorderPane checkersBoard;	
	
	public View (EventHandler<MouseEvent> clicListener) {
		super();

		// les cases et le pièces sur le damier seront écoutées par l'objet
		// passé en paramètre au constructeur
		board = new Board(clicListener);
		score = new Score();
		
		// création d'un fond d'écran qui contient le damier + les axes 
		this.checkersBoard = new BorderPane();	
		
		// la taille du damier est fonction de taille de la Scene
		board.prefWidthProperty().bind(this.widthProperty());
		board.prefHeightProperty().bind(this.heightProperty());

		// ajout du damier au centre du fond d'écran
		this.checkersBoard.setCenter(board);
		// ajout de la zone de score en haut de l'écran
		this.checkersBoard.setTop(score);
		
		// ajout des axes sur les cotés du damier
		//checkersBoard.setTop(createHorizontalAxisScore());
		this.checkersBoard.setBottom(createHorizontalAxis());
		this.checkersBoard.setLeft(createVerticalAxis());
		this.checkersBoard.setRight(createVerticalAxis());
		
		// ajout du fond d'écran à la vue
		this.setCenter(this.checkersBoard);
		
	}

	///////////////////////////////////////////////////////////////////////////////////// 
	// Méthode invoquée depuis le Controller pour propager les déplacements
	// effectués sur le model sur la vue
	/////////////////////////////////////////////////////////////////////////////////////
	
	public void actionOnGui(InputViewData<Integer> dataToRefreshView) {
		((Board)this.board).actionOnGui(dataToRefreshView);
		((Score)this.score).actionOnGui(dataToRefreshView);
	//	this.setScore(dataToRefreshView.score, dataToRefreshView.promotedPieceColor);
	}
	
	//////////////////////////////////////////////////////////////////////////
	

	private GridPane createHorizontalAxis() {
		GridPane pane = new GridPane();
		pane.prefWidthProperty().bind(this.widthProperty());
		for (char c = 'a'; c<='j'; c++){
			Label label1 = new Label(String.valueOf(c));
			label1.setAlignment(Pos.CENTER);
			label1.prefWidthProperty().bind(pane.prefWidthProperty().divide(GuiConfig.SIZE));
			pane.add(label1, c-'a', 0);
		}
		return pane;
	}

	private GridPane createVerticalAxis() {
		GridPane pane = new GridPane();
		pane.prefHeightProperty().bind(this.heightProperty());
		for (int c = 10; c>=1; c--){
			Label label1 = new Label(String.valueOf(c));
			label1.prefHeightProperty().bind(pane.prefHeightProperty().divide(GuiConfig.SIZE));
			pane.add(label1, 0, 10-c+1);
		}
		
		
		
		return pane;
	}
	
	
}


