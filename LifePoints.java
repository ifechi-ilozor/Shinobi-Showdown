package Indy;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The LifePoints Class controls displaying attack &
 * superAttack effects through a health bar and number.
 */

public class LifePoints {
 
	private Pane _root;
	private Label _sasukeLabel;
	private Label _narutoLabel;
	private Label _label1;
	private Label _label2;
	private int _naruto;
	private int _sasuke;
	private Rectangle _narutoRect;
	private Rectangle _sasukeRect;
	
	public LifePoints(Pane root, int narutoIs, int sasukeIs) {
		
		_naruto = narutoIs;
		_sasuke = sasukeIs;
		//using this to make sure the correct label & rectangle is updated
		_sasukeRect = new Rectangle(Constants.HEALTHS, Constants.DOWN, 
				Constants.FULL, Constants.BAR_WIDTH);
		_sasukeRect.setFill(Color.CYAN);
		Rectangle sBorderRect = new Rectangle(Constants.HEALTHS, Constants.DOWN, 
				Constants.FULL, Constants.BAR_WIDTH);
		sBorderRect.setStrokeWidth(1);
		sBorderRect.setStroke(Color.CYAN);
		_narutoRect = new Rectangle(Constants.HEALTHN, Constants.DOWN, 
				Constants.FULL, Constants.BAR_WIDTH);
		_narutoRect.setFill(Color.DARKORANGE);
		Rectangle nBorderRect = new Rectangle(Constants.HEALTHN, Constants.DOWN, 
				Constants.FULL, Constants.BAR_WIDTH);
		nBorderRect.setStrokeWidth(1);
		nBorderRect.setStroke(Color.DARKORANGE);
		//setting up health bars
		_sasukeLabel = new Label("100");
		_narutoLabel = new Label("100");
		//making separate labels because I only want to update the number
		_label1 = new Label("Sasuke Chakra: ");
		_label2 = new Label("Naruto Chakra: ");
		_sasukeLabel.setLayoutX(Constants.SASUKE_LABEL);
		_sasukeLabel.setLayoutY(Constants.DOWN);
		_narutoLabel.setLayoutX(Constants.NARUTO_LABEL);
		_narutoLabel.setLayoutY(Constants.DOWN);
		_label1.setLayoutX(Constants.LABEL1);
		_label1.setLayoutY(Constants.DOWN);
		_label2.setLayoutX(Constants.LABEL2);
		_label2.setLayoutY(Constants.DOWN);
		_sasukeLabel.setTextFill(Color.CYAN);
		_narutoLabel.setTextFill(Color.DARKORANGE);
		_label1.setTextFill(Color.CYAN);
		_label2.setTextFill(Color.DARKORANGE);
		_root = root;
		root.getChildren().addAll(sBorderRect, nBorderRect, _narutoLabel, _sasukeLabel, 
				_label1, _label2, _narutoRect, _sasukeRect);
		//adding labels and health bars
	}
	
	//updating labels & health bars depending on who is main & who is opponent
	//0 & 1 here correspond to main & opponent so that label updated takes in the 
	//right Chakra type
	public void update(int opponentChakra, int mainChakra) {
		
		if(_naruto == 0 && _sasuke == 0 || _sasuke == 1) {
		
			_root.getChildren().remove(_sasukeLabel);
			_root.getChildren().remove(_sasukeRect);
			_sasukeLabel = new Label(Integer.toString(opponentChakra));
			_sasukeLabel.setLayoutX(Constants.SASUKE_LABEL);
			_sasukeLabel.setLayoutY(Constants.DOWN);
			_sasukeLabel.setTextFill(Color.CYAN);
			_sasukeRect = new Rectangle(Constants.HEALTHS, Constants.DOWN, 
					opponentChakra, Constants.BAR_WIDTH);
			_sasukeRect.setFill(Color.CYAN);
			_root.getChildren().add(_sasukeRect);
			_root.getChildren().add(_sasukeLabel);
			
			_root.getChildren().remove(_narutoLabel);
			_root.getChildren().remove(_narutoRect);
			_narutoLabel = new Label(Integer.toString(mainChakra));
			_narutoLabel.setLayoutX(Constants.NARUTO_LABEL);
			_narutoLabel.setLayoutY(Constants.DOWN);
			_narutoLabel.setTextFill(Color.DARKORANGE);
			_narutoRect = new Rectangle(Constants.HEALTHN, Constants.DOWN, 
					mainChakra, Constants.BAR_WIDTH);
			_narutoRect.setFill(Color.DARKORANGE);
			_root.getChildren().add(_narutoRect);
			_root.getChildren().add(_narutoLabel);
		}
		
		if(_naruto == 1) {
			
			_root.getChildren().remove(_sasukeLabel);
			_root.getChildren().remove(_sasukeRect);
			_sasukeLabel = new Label(Integer.toString(mainChakra));
			_sasukeLabel.setLayoutX(Constants.SASUKE_LABEL);
			_sasukeLabel.setLayoutY(Constants.DOWN);
			_sasukeLabel.setTextFill(Color.CYAN);
			_sasukeRect = new Rectangle(Constants.HEALTHS, Constants.DOWN, 
					mainChakra, Constants.BAR_WIDTH);
			_sasukeRect.setFill(Color.CYAN);
			_root.getChildren().add(_sasukeRect);
			_root.getChildren().add(_sasukeLabel);
			
			_root.getChildren().remove(_narutoLabel);
			_root.getChildren().remove(_narutoRect);
			_narutoLabel = new Label(Integer.toString(opponentChakra));
			_narutoLabel.setLayoutX(Constants.NARUTO_LABEL);
			_narutoLabel.setLayoutY(Constants.DOWN);
			_narutoLabel.setTextFill(Color.DARKORANGE);
			_narutoRect = new Rectangle(Constants.HEALTHN, Constants.DOWN, 
					opponentChakra, Constants.BAR_WIDTH);
			_narutoRect.setFill(Color.DARKORANGE);
			_root.getChildren().add(_narutoRect);
			_root.getChildren().add(_narutoLabel);
		}
	}

}
