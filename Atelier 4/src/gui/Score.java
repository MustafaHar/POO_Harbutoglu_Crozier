package gui;

import controller.InputViewData;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import nutsAndBolts.PieceSquareColor;

public class Score extends GridPane{
	
	// Conteneur de l'affichage du score
	BorderPane checkersBoard;	
	
	// Les zones d'affichages du score
	private Label label_WHITE;
	private Label label_BLACK;
	private Label label_win;
	
	// Conteneur de la zone du score
	public Score () {
		super();
		
		GridPane pane = new GridPane();
		// Mettre un score par d�faut
		pane = this.defaultScore(0);
		// L'ajouter dans le conteneur
		this.add(pane, 0, 0);
		
	}
	
	
	
	// Mettre un score par d�faut
	private GridPane defaultScore(int score) {
		// Affichage sur grille
		this.label_WHITE = new Label("WHITE = "+score);
		this.label_BLACK = new Label("BLACK = "+ score);
		this.label_win = new Label("");
		
		this.label_WHITE.setMinWidth(60);
		this.label_BLACK.setMinWidth(60);
		this.label_win.setMinWidth(100);
		
		GridPane pane = new GridPane();
		
		// Le score est afficher dans le m�me conteneur que l'axe horizontal haut
		// Creer l'axe horizontal
		pane = createHorizontalAxisScore();
	
		this.label_WHITE .prefHeightProperty().bind(pane.prefHeightProperty().divide(GuiConfig.SIZE));
		this.label_BLACK.prefHeightProperty().bind(pane.prefHeightProperty().divide(GuiConfig.SIZE));
		
		// Emplacement de l'affichage
		pane.add(label_WHITE, 0, 0);
		pane.add(label_BLACK, 1, 0);
		pane.add(label_win, 4, 0);
		
		return pane;
	}
	
	// Axe horizontal qui va dans la m�me zone que le score
	private GridPane createHorizontalAxisScore() {
		GridPane pane = new GridPane();	
	//	pane = createScore();
	
		pane.prefWidthProperty().bind(this.widthProperty());
		for (char c = 'a'; c<='j'; c++){
			Label label1 = new Label(String.valueOf(c));
			label1.setAlignment(Pos.CENTER);
			label1.prefWidthProperty().bind(pane.prefWidthProperty().divide(GuiConfig.SIZE));
			pane.add(label1, c-'a', 10);
		}
		return pane;
	}
	
	// Placer le nouveau score
	private void setScore(int score, PieceSquareColor couleur) {
		// this.checkersBoard.setTop(createScore(score, couleur));
		this.createScore(score, couleur);
		
		// Si l'un des joueurs a pris tous les pions adverses il a gagn�
		if(is_game_win(score)) {
			setGameWin(couleur);
		}
	}
	
	// Cr�er le score
	private void createScore(int score, PieceSquareColor couleur) {
		// Couleur de l'adversaire
		PieceSquareColor couleur_adverse = couleur.equals(PieceSquareColor.WHITE) ? PieceSquareColor.BLACK : PieceSquareColor.WHITE;

		// Choisir o� afficher le score re�ue (carr� blanc ou noir)
		if(couleur.equals(PieceSquareColor.WHITE)) {
			this.label_WHITE.setText("WHITE = "+ score);
		}
		else {
			this.label_BLACK.setText("BLACK = "+ score);
		}
	}
	
	// Informer si la partie est gagn�e ou pas
	private boolean is_game_win(int score) {
		boolean result = false;
		// Nombre de jetons � prendre pour gagner
		int gagner = GuiConfig.SIZE *2;
		//int gagner = 2;
		// Si le score max est gagn�
		if(score == gagner) {
			result = true;
		}
		return result;
	}
		
	// Afficher que le joueur � gagn�
	public void setGameWin(PieceSquareColor couleur) {
		// On affiche qu'il a gagn�
		String message_win = ("Bravo "+couleur);
		this.label_win.setText(message_win);
		System.out.println(message_win);
		
		// Image "gagn� !" a afficher
		ImageView image;
		//Image imageWin;
		System.out.println("affichage gagn�");
		image = GuiFactory.createImageWin();
		
		GridPane zone_win = (GridPane) this.getChildren().get(0);
		zone_win.add(image,0,0);
		
		// Emp�cher la poursuite du jeu apr�s qu'un joueur ait gagner
		this.setDisable(true);
	}
	
	
	
	public void actionOnGui(InputViewData<Integer> dataToRefreshView) {
		
		if (dataToRefreshView != null) {
			
			////////////////////////////////////////////////////
			// la PieceGui de la vue est effectivement d�plac�e
			////////////////////////////////////////////////////
			if (dataToRefreshView.toMovePieceIndex != -1 && dataToRefreshView.targetSquareIndex != -1) {
				// On raffraichie le score
				this.setScore(dataToRefreshView.score, dataToRefreshView.promotedPieceColor);
			}

			////////////////////////////////////////////////////
			// la PieceGui de la vue est �ventuellement promue
			////////////////////////////////////////////////////
			if (dataToRefreshView.promotedPieceIndex != -1) {
				;
			}

			////////////////////////////////////////////////////
			// l'�ventuelle pi�ce interm�diaire est supprim�e 
			////////////////////////////////////////////////////
			if (dataToRefreshView.capturedPieceIndex != -1) {
				;
			}
			
		}

	}

}
