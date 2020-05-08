package Indy;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**Setting up panes & start screen, then passing the info from the start screen
 * to the Fight class */

public class PaneOrganizer {

	private BorderPane _root;
	private Fight _game;
	private Pane _startPane;
	private Pane _fightPane;
	private Pane _pointsPane;
	private Pane _barPane;
	private int _narutoIs;
	private int _sasukeIs;
	private ToggleGroup _group1;
	private ToggleGroup _group2;
	private RadioButton _nComputer;
	private RadioButton _sComputer;
	
	public PaneOrganizer() {
		
		_root = new BorderPane();
		_fightPane = new Pane();
		_pointsPane = new Pane();
		_barPane = new Pane();
		_startPane = new Pane();
		Image img = new Image(this.getClass().getResourceAsStream("chuninbackground.jpg"));
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
				false, false, true, true);
		BackgroundImage backgroundimg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
		BackgroundImage[] images = new BackgroundImage[1];
		images[0] = backgroundimg;
		Background background = new Background(images);
		_fightPane.setBackground(background);
		BackgroundFill fill1 = new BackgroundFill(Color.BLACK, 
	    		CornerRadii.EMPTY, Insets.EMPTY);
		Background background1 = new Background(fill1);
		_pointsPane.setBackground(background1);
		_pointsPane.setPrefHeight(Constants.POINTS_HEIGHT);
		_barPane.setBackground(background1);
		_barPane.setPrefWidth(Constants.BARPANE_WIDTH);
		_startPane.setBackground(background);
		_root.setRight(_barPane);
		_root.setTop(_pointsPane);
		_root.setCenter(_startPane);
		_narutoIs = 0; //initializing Naruto to player
		_sasukeIs = 0; //initializing Sasuke to player
		_group1 = new ToggleGroup();
		_group2 = new ToggleGroup();
		_nComputer = new RadioButton("Computer");
		_sComputer = new RadioButton("Computer");
		this.setupStart();
		this.setupButtons();
	}
	
	//for App class
	public Pane getRoot() {
		return _root;
	}
	
	//setting up character images & label on start screen
	private void setupStart() {
		Label label = new Label("PICK");
		label.setTextFill(Color.BLACK);
		label.setLayoutX(Constants.START);
		label.setLayoutY(Constants.START_W);
		label.setMaxWidth(Constants.START);
		Font font = new Font("times new roman", Constants.FONT1);
		label.setFont(font);
		label.setFocusTraversable(false);
		_startPane.getChildren().add(label);
		Image img1 = new Image(this.getClass().getResourceAsStream("/images/narutoimage.png"));
		Image img2 = new Image(this.getClass().getResourceAsStream("/images/sasukeimage.png"));
		ImageView viewChar1 = new ImageView();
		ImageView viewChar2 = new ImageView();
		viewChar1.setImage(img1);
		viewChar2.setImage(img2);
		viewChar1.setFitWidth(Constants.CHAR_STARTW);
		viewChar1.setFitHeight(Constants.CHAR_STARTH);
		viewChar2.setFitWidth(Constants.CHAR_STARTW);
		viewChar2.setFitHeight(Constants.CHAR_STARTH);
		viewChar1.setPreserveRatio(true);
		viewChar2.setPreserveRatio(true);
		viewChar1.setSmooth(true);
		viewChar2.setSmooth(true);
		_startPane.getChildren().addAll(viewChar1, viewChar2);
		viewChar1.setX(Constants.N_STARTX);
		viewChar1.setY(Constants.FONT1);
		viewChar2.setX(Constants.S_STARTX);
		viewChar2.setY(Constants.FONT1);
		this.setupStartButtons();
	}
	
	//setting up all start buttons 
	private void setupStartButtons() {
		HBox buttonPane = new HBox();
		Button b1 = new Button("GO!");
		buttonPane.getChildren().add(b1);
		b1.setOnAction(new GoHandler());
		b1.setFocusTraversable(false);
		buttonPane.setLayoutX(Constants.BUTTON);
		buttonPane.setLayoutY(Constants.START);
		_startPane.getChildren().add(buttonPane);
		RadioButton n1 = new RadioButton("Player");
		RadioButton s1 = new RadioButton("Player");
		n1.setToggleGroup(_group1);
		_nComputer.setToggleGroup(_group1);
		s1.setToggleGroup(_group2);
		_sComputer.setToggleGroup(_group2);
		n1.setOnAction(new DecisionHandler(0)); //0 means naruto
		_nComputer.setOnAction(new ChoiceHandler(0));
		s1.setOnAction(new DecisionHandler(1)); //1 means sasuke
		_sComputer.setOnAction(new ChoiceHandler(1));
		n1.setFocusTraversable(false);
		_nComputer.setFocusTraversable(false);
		s1.setFocusTraversable(false);
		_sComputer.setFocusTraversable(false);
		VBox narutoButtons = new VBox();
		narutoButtons.getChildren().addAll(n1, _nComputer);
		narutoButtons.setLayoutX(Constants.BXN);
		narutoButtons.setLayoutY(Constants.BY);
		VBox sasukeButtons = new VBox();
		sasukeButtons.getChildren().addAll(s1, _sComputer);
		sasukeButtons.setLayoutX(Constants.BXS);
		sasukeButtons.setLayoutY(Constants.BY);
		_startPane.getChildren().add(narutoButtons);
		_startPane.getChildren().add(sasukeButtons);	
	}
	
	private class GoHandler implements EventHandler<ActionEvent> {
		
		public GoHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_game = new Fight(_fightPane, _pointsPane, _barPane, _narutoIs, _sasukeIs);	
			_root.getChildren().remove(_startPane);
			_root.setCenter(_fightPane);
			//constructing Fight pane & passing in info
		}
	}
	
	//Computer selection employs this because only 1 character can be computer
	private class ChoiceHandler implements EventHandler<ActionEvent> {
		
		private int _whichChar;
		
		public ChoiceHandler(int whichChar) {	
			_whichChar = whichChar;
		}
		
		@Override 
		public void handle(ActionEvent event) { 
			ToggleGroup group = new ToggleGroup();
			_nComputer.setToggleGroup(group);
			_sComputer.setToggleGroup(group);
			//making another toggleGroup to make sure only on Computer
			//selection is chosen
			
			if (_whichChar == 0) {
				_narutoIs = 1; 
				//1 for this _Is instance variables means computer,
			}
			if (_whichChar == 1) {
				_sasukeIs = 1;
			}
			
			_nComputer.setToggleGroup(_group1);
			_sComputer.setToggleGroup(_group2);
		}
	}
	
	private class DecisionHandler implements EventHandler<ActionEvent> {
		
		private int _whichChar;
		
		public DecisionHandler(int whichChar) {
			_whichChar = whichChar;
		}
		
		@Override 
		public void handle(ActionEvent event) { 
			if (_whichChar == 0) {
				_narutoIs = 0; //0 for this _Is instance variable means player
			}
			if (_whichChar == 1) {
				_sasukeIs = 0;
			}
		}
	}
	
	//quit button
	private void setupButtons() {
		HBox buttonPane = new HBox();
		Button b1 = new Button("Quit");
		buttonPane.getChildren().add(b1);
		b1.setOnAction(new QuitHandler());
		b1.setFocusTraversable(false);
		_root.setBottom(buttonPane);
	}
	
	private class QuitHandler implements EventHandler<ActionEvent> {
		
		public QuitHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			Platform.exit();		
		}
	}
}
