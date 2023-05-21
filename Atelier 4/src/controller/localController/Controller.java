package controller.localController;


import controller.InputViewData;
import controller.Mediator;
import controller.OutputModelData;
import gui.CheckersSquareGui;
import gui.View;
import model.BoardGame;
import model.Coord;
import model.ModelConfig;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


/**
 * @author francoiseperrin
 *
 * Le controller a 2 responsabilités :
 * 	- il écoute les clics de souris de l'utilisateur sur la vue
 * 	- il invoque la méthode moveCapturePromote() du model
 * 	  si actions (move + prise + promotion) OK sur model alors elles sont propagées sur view 
 *    (invoque méthode moveCapturePromote() de la view)
 *    
 * La view et le model ne gérant pas les coordonnées des cases de la même manière
 * le controller assure la conversion :
 * 	- index de 0 à  99 pour la view
 * 	- Coord (col, ligne) pour le model ['a'..'j'][10..1]
 * 
 */
public class Controller implements Mediator, BoardGame<Integer>, EventHandler<MouseEvent>  {


	private BoardGame<Coord> model;
	private View view;

	// Cette valeur est MAJ chaque fois que l'utilisateur clique sur une pièce
	// Elle doit être conservée pour être utilisée lorsque l'utilisateur clique sur une case
	private int toMovePieceIndex;	

	public Controller() {
		this.model =  null;
		this.view = null;
		this.setToMovePieceIndex(-1);
	}

	private void setToMovePieceIndex(int toMovePieceIndex) {
		this.toMovePieceIndex = toMovePieceIndex;
	}

	public int getToMovePieceIndex() {
		return toMovePieceIndex;
	}

	//////////////////////////////////////////////////////////////////
	//
	// Controller vu comme un médiateur entre la view et le model
	//
	//////////////////////////////////////////////////////////////////

	public void setView(View view) {
		this.view = view;
	}
	public void setModel(BoardGame<Coord> model) {
		this.model =  model;
	}

	////////////////////////////////////////////////////////////////////
	//
	// Controller vu comme un Ecouteur des évènement souris sur la view
	//
	////////////////////////////////////////////////////////////////////

	@Override
	public void handle(MouseEvent mouseEvent) {
	//	try {
			if(mouseEvent.getSource() instanceof CheckersSquareGui)
				 checkersSquareGuiHandle(mouseEvent);
			else
				checkersPieceGuiHandle(mouseEvent);
	//	}
	//	catch (Exception e) {
			// Try - Catch pour empêcher pgm de planter tant que les interfaces
			// CheckersSquareGui et CheckersPieceGui n'existent pas
		//	System.out.println("CheckersSquareGui et CheckerPieceGui existe pas");
			// System.out.println(e);
	//	}
	}

	/**
	 * @param mouseEvent
	 * Ecoute les événements sur les PieceGui
	 */
	private void checkersPieceGuiHandle(MouseEvent mouseEvent) {

		// Recherche PieceGui sélectionnée
		ImageView selectedPiece = (ImageView) mouseEvent.getSource();
		
		if(selectedPiece != null) {
			// Recherche et fixe coordonnée de la pièce sélectionnée 
			CheckersSquareGui parentSquare = (CheckersSquareGui)  selectedPiece.getParent();
			if(parentSquare != null) {
				this.setToMovePieceIndex(parentSquare.getSquareCoord());
			}
			mouseEvent.consume();
		}
		
	}
	/**
	 * @param mouseEvent
	 * Ecoute les événements sur les SquareGui
	 */
	private void checkersSquareGuiHandle(MouseEvent mouseEvent) {

		// Recherche SquareGUI sélectionné
		CheckersSquareGui square = (CheckersSquareGui) mouseEvent.getSource();
		if(square != null) {
			int targetSquareIndex = square.getSquareCoord();

			// Le controller va invoquer la méthode moveCapturePromotion() du model
			// et si le model confirme que la pièce a bien été déplacée à cet endroit, 
			// il invoquera une méthode de la view pour la rafraichir
			this.moveCapturePromote(this.getToMovePieceIndex(), targetSquareIndex);

			// il n'y a plus de pièce à déplacer
			this.setToMovePieceIndex(-1);

			// On évite que le parent ne récupère l'event
			mouseEvent.consume();
		}
		
	}


	//////////////////////////////////////////////////////////////////
	//
	// Controller vu comme un Substitut du model 
	// il invoque les méthodes du model 
	// après actions de l'utilisateur sur la vue
	//
	//////////////////////////////////////////////////////////////////

	/**
	 * Invoque méthode moveCapturePromote() du model (après transformation des coordonnées)
	 * Si déplacement effectif sur model, invoque méthode actionOnGui() de la view
	 * pour rafraichir affichage en fonction des données retournées par le model
	 */
	@Override
	public OutputModelData<Integer> moveCapturePromote(Integer toMovePieceIndex, Integer targetSquareIndex) {

		OutputModelData<Integer> outputControllerData = null;
		InputViewData<Integer> intputControllerData = null;
		
		// Atelier 2
		Coord toMovePieceCoord = transformIndexToCoord(toMovePieceIndex);
		Coord targetSquareCoord = transformIndexToCoord(targetSquareIndex);
		Integer promoteSquareIndex;
		Integer capturePieceIndex;
		//int score = 0;
		
		OutputModelData<Coord>outputCoordData = this.model.moveCapturePromote(toMovePieceCoord, targetSquareCoord);
		
		if(outputCoordData.isMoveDone) {
			boolean isMoveDone = outputCoordData.isMoveDone;
			// Passer de coord a index
			capturePieceIndex = transformCoordToIndex(outputCoordData.capturedPieceCoord);
			promoteSquareIndex = transformCoordToIndex(outputCoordData.promotedPieceCoord);
			
			
			//intputControllerData = new controller.InputViewData(toMovePieceIndex, targetSquareIndex, capturePieceIndex,promoteSquareIndex);
			intputControllerData = new controller.InputViewData<Integer>(toMovePieceIndex, targetSquareIndex, capturePieceIndex, promoteSquareIndex, outputCoordData.promotedPieceColor, outputCoordData.score);
			this.view.actionOnGui(intputControllerData);
		}
		// Inutile de reconstituer un objetOutputModelData<Integer>, aucun client ne le récupère en mode local
		return outputControllerData;
	}


	/**
	 * @param squareIndex
	 * @param length
	 * @return les coordonnées métier calculées à  partir de l'index du SquareGUI sous la PieceGUI
	 */
	private Coord transformIndexToCoord (int squareIndex) {
		Coord coord = null;
		int  length = ModelConfig.LENGTH;
		char col = (char) ((squareIndex)%length + 'a');
		int ligne = length - (squareIndex)/length;
		coord = new Coord(col, ligne);
		return coord;
	}

	private int transformCoordToIndex (Coord coord) {
		int squareIndex = -1;
		int  length = ModelConfig.LENGTH;
		if (coord != null) {
			squareIndex = (length - coord.getLigne()) * length + (coord.getColonne()-'a');
		}
		return squareIndex;
	}


}




