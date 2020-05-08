package Indy;

import java.util.ArrayList;
import java.util.LinkedList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * The Fight Class handles all game logic including computer generated
 * fighting, attack effects, pausing, game over, etc.
 */

public class Fight {
	private Shinobi _main; 
	private Shinobi _opponent; 
	//Shinobi will either be opponent or main based on chosen mode
	private KeyHandler _keyHandler;
	private OtherKeyHandler _otherKeyHandler;
	//employed only when multiplayer is selected
	private Timeline _timeline;
	private Timeline _runTimeline;
	private Timeline _overTimeline;
	private ArrayList<Direction> _directionNames;
	private Queue<Integer> _choices;
	private boolean _moveOrNot;
	private int _unpredictable;
	private boolean _pause;
	private int _mainChakra;
	private int _opponentChakra;
	private LifePoints _chakras;
	private Pane _root;
	private Label _label;
	private int _narutoIs;
	private int _sasukeIs;
	
	public Fight(Pane root, Pane pointsRoot, Pane superRoot, int narutoIs, int sasukeIs) {
		_root = root;
		_narutoIs = narutoIs;
		_sasukeIs = sasukeIs;
		_root.setFocusTraversable(true);
		superRoot.setFocusTraversable(false);
		pointsRoot.setFocusTraversable(false);
		_label = new Label("PAUSE");
		_label.setTextFill(Color.RED);
		_label.setFocusTraversable(false);
		_label.setLayoutX(Constants.PAUSEX);
		_label.setLayoutY(Constants.PAUSEY);
		_label.setMaxWidth(Constants.SCENE_HEIGHT);
		Font font = new Font("arial", Constants.FULL);
		_label.setFont(font);
		root.getChildren().add(_label);
		_label.setVisible(false); //setting up pause label
		_timeline = null;
		_runTimeline = null;
		_overTimeline = null;
		_otherKeyHandler = null;
		_moveOrNot = true;
		_unpredictable = 1;
		_pause = false;
		_mainChakra = Constants.FULL;
		_opponentChakra = Constants.FULL; 
		_choices = new Queue<Integer>();
		_directionNames = new ArrayList<Direction>();
		_keyHandler = new KeyHandler();
		root.addEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
		if (narutoIs == 0 && sasukeIs == 0) {
			_main = new Shinobi(root, Constants.STEP, Constants.NSY, 0, superRoot);
			_opponent = new Shinobi(root, Constants.SSX, Constants.ROOT_LOWBOUNDY, 1, superRoot);
			_otherKeyHandler = new OtherKeyHandler();
			root.addEventHandler(KeyEvent.KEY_PRESSED, _otherKeyHandler);
			_chakras = new LifePoints(pointsRoot, 0, 0);
		} else if (narutoIs == 0 && sasukeIs == 1) {
			_main = new Shinobi(root, Constants.STEP, Constants.NSY, 0, superRoot);
			_opponent = new Shinobi(root, Constants.SSX, Constants.ROOT_LOWBOUNDY, 1, superRoot);
			this.setupTimeline();
			_chakras = new LifePoints(pointsRoot, 0, 1);
		} else if (narutoIs == 1 && sasukeIs == 0) {
			_main = new Shinobi(root, Constants.STEP, Constants.NSY, 1, superRoot);
			_opponent = new Shinobi(root, Constants.SSX, Constants.ROOT_LOWBOUNDY, 0, superRoot);
			this.setupTimeline();
			_chakras = new LifePoints(pointsRoot, 1, 0);
		}
		//setting up characters based on start screen
		this.setupOverTimeline();
	}
	
	// Main keyHandler. Always Naruto unless Naruto is slected to be computer.
	private class KeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event){
			KeyCode keyPressed = event.getCode();
				if(_pause && keyPressed == KeyCode.P) {
					_pause = false;
					Fight.this.pause();
				}
				else if(!_pause) {
				switch (keyPressed) {
				case RIGHT:
					_main.moveRight();
					break;
				case LEFT:
					_main.moveLeft();
					break;
				case UP:
					_main.moveUp();
					break;
				case DOWN:
					_main.moveDown();
					break;
				case SPACE:
					if(_main.getCanAttack() == true) {
						int[] place = Fight.this.attackSide(0, 0);
						_main.attack(place);
						Fight.this.loseOrNot(0, 0);
					}
					break;
				case ENTER:
					if(_main.getCanSuper() == true) {
						int[] place1 = Fight.this.attackSide(0, 1);
						_main.superAttack(place1);
						Fight.this.loseOrNot(0, 1);
					}
					break;
				case P:
					_pause = true;
					Fight.this.pause();
					break;
				}
			}
			event.consume();
		}
	}
	
	// Only used if multiplayer is selected. Sasuke always uses this keyHandler.
	private class OtherKeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event){
			KeyCode keyPressed = event.getCode();
				switch (keyPressed) {
				case D:
					_opponent.moveRight();
					break;
				case A:
					_opponent.moveLeft();
					break;
				case W:
					_opponent.moveUp();
					break;
				case S:
					_opponent.moveDown();
					break;
				case TAB:
					if(_opponent.getCanAttack() == true) {
						int[] place = Fight.this.attackSide(1, 0);
						_opponent.attack(place);
						Fight.this.loseOrNot(1, 0);
					}
					break;
				case R:
					if(_opponent.getCanSuper() == true) {
						int[] place1 = Fight.this.attackSide(1, 1);
						_opponent.superAttack(place1);
						Fight.this.loseOrNot(1, 1);
					}
					break;
				}
			event.consume();
		}
	}
	
	// Fades are for visual effect of attack on receiving character
	public void fadeOut(Node who) {
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(Constants.MOVE_REG), who);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(Constants.FADE);
		fadeOut.play();
	}
	
	public void fadeIn(Node who) {
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(Constants.MOVE_REG), who);
		fadeIn.setFromValue(Constants.FADE);
		fadeIn.setToValue(1);
		fadeIn.play();
	}
	
	// Used for the computer search
	public boolean moveOrNot() {
		if (this.distanceBetween(_main.getLoc(), _opponent.getLoc()) >= Constants.MOVE) {
			return true;
		}
		return false;
	}
	
	// Case for computer search if computer ever gets very close to target
	public boolean veryClose() {
		if (this.distanceBetween(_main.getLoc(), _opponent.getLoc()) <= Constants.CLOSE) {
			return true;
		}
		return false;
	}

	public int distanceBetween(int[] main, int[] opponent) {
		int distBetween = (int) Math.sqrt(Math.pow(main[0] - opponent[0], 2)
				+ Math.pow(main[1] - opponent[1], 2));
		return distBetween;
	}
	
	// Calculating points depending on whichCharacter (main or opponent) attacked and whichAttack
	public void loseOrNot(int whichChar, int whichAttack) {
		int distBetween = 0;
		
		if (whichChar == 0 && whichAttack == 0) {
			//main & regular attack
			distBetween = this.distanceBetween(_main.getLoc(), _opponent.getLoc());
			
			if (_mainChakra - Constants.ATTACK_DEDUCT >= 0) {
				_mainChakra -= Constants.ATTACK_DEDUCT;
			} else {
				_mainChakra = 0;
			}
			
			if (distBetween <= Constants.CHAR_WIDTH) {
				
				if (_opponentChakra - Constants.ROOT_LOWBOUNDY >= 0) {
					_opponentChakra -= Constants.ROOT_LOWBOUNDY;
				} else {
					_opponentChakra = 0;
				}
	
				this.fadeOut(_opponent.getNode());
				this.fadeIn(_opponent.getNode());
			}
			_chakras.update(_opponentChakra, _mainChakra);
		} else if (whichChar == 0 && whichAttack == 1) {
			// main & superAttack
			distBetween = this.distanceBetween(_main.getLoc(), _opponent.getLoc());
			
			if (_mainChakra - Constants.SUPER_DEDUCT >= 0) {
				_mainChakra -= Constants.SUPER_DEDUCT;
			} else {
				_mainChakra = 0;
			}
			
			if (distBetween <= Constants.SCENE_WIDTH) {
				
				if (_opponentChakra - Constants.BAR_WIDTH >= 0) {
					_opponentChakra -= Constants.BAR_WIDTH;
				} else {
					_opponentChakra = 0;
				}
			
				this.fadeOut(_opponent.getNode());
				this.fadeIn(_opponent.getNode());
			}
			
			_chakras.update(_opponentChakra, _mainChakra);
		} else if (whichChar == 1 && whichAttack == 0) {
			//opponent & regular attack
			distBetween = this.distanceBetween(_main.getLoc(), _opponent.getLoc());
			
			if (_opponentChakra - Constants.ATTACK_DEDUCT >= 0) {
				_opponentChakra -= Constants.ATTACK_DEDUCT;
			} else {
				_opponentChakra = 0;
			}
			
			if (distBetween <= Constants.CHAR_WIDTH) {
				
				if(_mainChakra - Constants.ROOT_LOWBOUNDY >= 0) {
					_mainChakra -= Constants.ROOT_LOWBOUNDY;
				} else {
					_mainChakra = 0;
				}
			
				this.fadeOut(_main.getNode());
				this.fadeIn(_main.getNode());
			}
			_chakras.update(_opponentChakra, _mainChakra);
		} else if (whichChar == 1 && whichAttack == 1) {
			//opponent & superAttack
			distBetween = this.distanceBetween(_main.getLoc(), _opponent.getLoc());
			
			if (_opponentChakra - Constants.SUPER_DEDUCT >= 0) {
				_opponentChakra -= Constants.SUPER_DEDUCT;
			} else {
				_opponentChakra = 0;
			}
			
			if (distBetween <= Constants.SCENE_WIDTH) {
				
				if((_mainChakra - Constants.BAR_WIDTH) >= 0) {
					_mainChakra -= Constants.BAR_WIDTH;
				} else if((_mainChakra - Constants.BAR_WIDTH) < 0) {
					_mainChakra = 0;
				}
		
				this.fadeOut(_main.getNode());
				this.fadeIn(_main.getNode());
			}
			_chakras.update(_opponentChakra, _mainChakra);
		}
	}
	
	public void setupOverTimeline(){
		KeyFrame kf = new KeyFrame(Duration.seconds(1), new OverHandler());
		_overTimeline = new Timeline(kf);
		_overTimeline.setCycleCount(Animation.INDEFINITE);
		_overTimeline.play();		
	}
	
	private class OverHandler implements EventHandler<ActionEvent> {
		
		@Override 
		public void handle(ActionEvent event) {
			Fight.this.gameOver();
		}
	}
	
	//game Over if someone hits zero
	public void gameOver() {
		if (_mainChakra == 0 || _opponentChakra == 0) {
			Label label = new Label("GAME OVER!");
			label.setTextFill(Color.RED);
			label.setLayoutX(Constants.PAUSEY);
			label.setLayoutY(Constants.SSUPER);
			label.setMaxWidth(Constants.SCENE_HEIGHT);
			Font font = new Font("times new roman", Constants.FULL);
			Font font1 = new Font("times new roman", Constants.FONT1);
			label.setFont(font);
			label.setFocusTraversable(false);
			_root.getChildren().add(label);
			_root.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
			if (_otherKeyHandler != null) {
				_root.removeEventHandler(KeyEvent.KEY_PRESSED, _otherKeyHandler); 
			}
			_opponent.stopAll();
			_main.stopAll();
			if (_timeline != null) {
				_timeline.stop();
			}
			if (_runTimeline != null) {
				_runTimeline.stop();
			}
			if (_mainChakra == 0 && _opponentChakra != 0) {
				Label label1 = null;
				if(_narutoIs == 0) {		
					label1 = new Label("Sasuke Wins!");
					label1.setTextFill(Color.CYAN);
				} else if (_narutoIs == 1) {
					label1 = new Label("Naruto Wins!");
					label1.setTextFill(Color.DARKORANGE);
				}
				label1.setLayoutX(Constants.CHAR_WIN);
				label1.setLayoutY(Constants.NSUPER_L);
				label1.setMaxWidth(Constants.START);
				label1.setFont(font1);
				label1.setFocusTraversable(false);
				_root.getChildren().add(label1);
			} else if (_opponentChakra == 0 && _mainChakra != 0) {
				Label label2 = null;
				if(_narutoIs == 0) {
					label2 = new Label("Naruto Wins!");
					label2.setTextFill(Color.DARKORANGE);
				} else if(_narutoIs == 1) {
					label2 = new Label("Sasuke Wins!");
					label2.setTextFill(Color.CYAN);
				}
				label2.setLayoutX(Constants.CHAR_WIN);
				label2.setLayoutY(Constants.NSUPER_L);
				label2.setMaxWidth(Constants.START);
				label2.setFont(font1);
				label2.setFocusTraversable(false);
				_root.getChildren().add(label2);
			} else if (_opponentChakra == 0 && _mainChakra == 0) {
				Label label3 = new Label("DRAW!");
				label3.setTextFill(Color.WHITE);
				label3.setLayoutX(Constants.DRAW);
				label3.setLayoutY(Constants.NSUPER_L);
				label3.setMaxWidth(Constants.START);
				label3.setFont(font1);
				label3.setFocusTraversable(false);
				_root.getChildren().add(label3);
			}
		}
	} 
	
	//pausing
	public void pause() {	
		
		if (_pause){
			if(_timeline != null) {
				if (_timeline.getStatus() == Status.RUNNING) {
					_timeline.pause();
				}
			}
			if (_runTimeline != null) {
				if (_runTimeline.getStatus() == Status.RUNNING) {
					_runTimeline.pause();
				}
			}
			_overTimeline.pause();
			_main.pauseAll(_pause);
			_opponent.pauseAll(_pause);
			if (_otherKeyHandler != null) {
				_root.removeEventHandler(KeyEvent.KEY_PRESSED, _otherKeyHandler);
			}
			_label.setVisible(true);
		} else {
			if (_timeline != null) {
				if (_timeline.getStatus() == Status.PAUSED) {
					_timeline.playFromStart();
				}
			}
			_overTimeline.playFromStart();
			_main.pauseAll(_pause);
			_opponent.pauseAll(_pause);
			if (_runTimeline != null) {
				if (_runTimeline.getStatus() == Status.PAUSED) {
					_runTimeline.playFromStart();
				}
			}
			if (_otherKeyHandler != null) {
				_root.addEventHandler(KeyEvent.KEY_PRESSED, _otherKeyHandler);
			}
			_label.setVisible(false);
		}
	} 
	
	//figuring out which side the attack should go on 
	public int[] attackSide(int whichChar, int whichAttack) {
		
		if (whichChar == 0 && whichAttack == 0) {
			int side = _main.getLoc()[0] - _opponent.getLoc()[0];
			if (side < 0) {
				int[] right = new int[2];
				right[0] = Constants.SIDE_R;
				right[1] = Constants.SIDE_R;
				return right;
			}
			if (side > 0) {
				int[] left = new int[2];
				left[0] = Constants.SIDE_LI;
				left[1] = Constants.SIDE_LIL;
				return left;
			}
			if (side == 0) {
				int[] center = new int[2];
				center[0] = 40;
				center[1] = Constants.STEP;
				return center;
			}
		} else if (whichChar == 0 && whichAttack == 1) {
			int side = _main.getLoc()[0] - _opponent.getLoc()[0];
			if (side < 0) {
				int[] right = new int[2];
				right[0] = Constants.SIDE_R;
				right[1] = Constants.POINTS_HEIGHT;
				return right;
			}
			if (side > 0) {
				int[] left = new int[2];
				left[0] = Constants.FULLI;
				left[1] = Constants.SIDE_YI;
				return left;
			}
			if (side == 0) {
				int[] center = new int[2];
				center[0] = -Constants.START_W;
				center[1] = -Constants.START_W;
				return center;
			}
		} else if (whichChar == 1 && whichAttack == 0) {
			int side = _opponent.getLoc()[0] - _main.getLoc()[0];
			if (side <= 0) {
				int[] left = new int[2];
				left[0] = Constants.FONT1;
				left[1] = Constants.SIDE_Y;
				return left;
			}
			if (side > 0) {
				int[] right = new int[2];
				right[0] = Constants.SIDE_LI;
				right[1] = Constants.SIDE_Y;
				return right;
			}
			if (side == 0) {
				int[] center = new int[2];
				center[0] = Constants.POINTS_HEIGHT;
				center[1] = Constants.SIDE_Y;
				return center;
			}
		} else if (whichChar == 1 && whichAttack == 1) {
			int side = _opponent.getLoc()[0] - _main.getLoc()[0];
			if (side <= 0) {
				int[] left = new int[2];
				left[0] = Constants.SIDE_Y;
				left[1] = Constants.BAR_WIDTH;
				return left;
			}
			if (side > 0) {
				int[] right = new int[2];
				right[0] = Constants.SIDE_RI;
				right[1] = Constants.BAR_WIDTH;
				return right;
			}
			if (side == 0) {
				int[] center = new int[2];
				center[0] = Constants.SIDE_YI;
				center[1] = Constants.BAR_WIDTH;
				return center;
			}
		}
		int[] none = new int[2];
		none[0] = Constants.FONT1;
		none[1] = Constants.SIDE_Y;
		return none;
	}
	
	//using a queue to fix the computers responses in a semi-random order
	private class Queue<Integer> {
		
		private LinkedList<Integer> _options;
		
		public Queue() {
			_options = new LinkedList<Integer>();
		}
		
		public void enqueue(Integer number) {
			_options.addLast(number);
		}
		
		public Integer deqeue() {
			return _options.removeFirst();
		}
		
		public boolean isEmpty() { 
			return _options.isEmpty();
		}
		
		public int size() {
			return _options.size();
		}
	}
	
	//timeline is only used for computer search
	public void setupTimeline(){
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.SEARCH_TIME),
				new TimeHandler());
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();		
	}
	
	private class TimeHandler implements EventHandler<ActionEvent> {

		@Override 
		public void handle(ActionEvent event) {
    		boolean moveOrNot = _moveOrNot;
    		if (moveOrNot == true) {
    			Fight.this.searchAndMove(); 
    			_moveOrNot = Fight.this.moveOrNot();
    			//resetting move or not every time
    		}
    		if (Fight.this.veryClose() == true && Fight.this.mainDirection() == true){
    			Fight.this.run();
    		}
    		else {
    			if (!_choices.isEmpty()) {
    				int decision = _choices.deqeue();
    				//if superAttack is available I want the computer to attack
    				//because that's what a regular player would do
    				if (_opponent.getCanSuper() == true) {
    					Fight.this.actOut(3);
    				} else if(_opponent.getCanSuper() == false) {
    					Fight.this.actOut(decision);
    				}	
    			} else if (_choices.isEmpty()) {
    				Fight.this.unpredictable(_unpredictable);
    				_unpredictable++;
    				int decision = _choices.deqeue();
    				//same as above but enqeueing and passing in unpredictable 
    				if (_opponent.getCanSuper() == true) {
    					Fight.this.actOut(3);
    				} else if(_opponent.getCanSuper() == false) {
    					Fight.this.actOut(decision);
    				}	
    			}
    		}
		}
	}
	
	//this is to queue actions up in reverse order each time because I think it would
	//be a lot easier for the player to predict what the AI would do next if it was the same order
	//every time
	public void unpredictable(int a) {
		
		if (a % 2 == 0) {
			for (int x = 0; x < 7; x++) {
				_choices.enqueue(x);
			}
		} else if (a % 2 != 0) {
			for (int x = 6; x >= 0; x--) {
				_choices.enqueue(x);
			}
		}
	}
	
	//computer runs for 7 seconds 
	public void setupRunTimeline(){
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.SEARCH_TIME), new DoneHandler());
		_runTimeline = new Timeline(kf);
		_runTimeline.setCycleCount(Constants.RUN_CYCLE);
		_runTimeline.play();		
	}
	
	private class DoneHandler implements EventHandler<ActionEvent> {
		
		public DoneHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_timeline.pause();
			Fight.this.run();
			_timeline.playFromStart();
		}
	}
	
	//acting out whatever was dequeued 
	public void actOut(int decision) {
		
		switch(decision) {
		
		case 0:
			//if statements add more unpredictability and I thought these mimic
			//what a human player would do
			if(this.mainDirection() == true || this.veryClose() == true || this.mainAttack() == true) {
				this.setupRunTimeline();
			}
			_moveOrNot = true;
			break;
		case 1:
			this.searchAndMove();
			_moveOrNot = true;
			break;
		case 2:
			this.searchAndMove();
			if(_opponent.getCanAttack()) {	
				if (this.mainAttack() == true || this.distanceBetween(_main.getLoc(), 
						_opponent.getLoc()) <= Constants.CHAR_WIDTH) {
					int[] place = this.attackSide(1, 0);
					_opponent.attack(place);
					this.loseOrNot(1, 0);
				}
			}
			_moveOrNot = true;
			break;
		case 3:
			if(_opponent.getCanSuper() == true) {
				int[] place1 = this.attackSide(1, 1);
				_opponent.superAttack(place1);
				this.loseOrNot(1, 1);
			}
			_moveOrNot = Fight.this.moveOrNot();
			break;
		case 4:
			if(_opponent.getCanAttack()) {		
				if (this.mainAttack() == true || this.distanceBetween(_main.getLoc(), 
						_opponent.getLoc()) <= Constants.CHAR_WIDTH) {
					int[] place2 = this.attackSide(1, 0);
					_opponent.attack(place2);
					this.loseOrNot(1, 0);
				}
			}
			_moveOrNot = Fight.this.moveOrNot();
			break;
		case 5:
			this.setupRunTimeline();
			_moveOrNot = Fight.this.moveOrNot();
			break;
		case 6:
			this.searchAndMove();
			_moveOrNot = Fight.this.moveOrNot();
			break;
		}
	}
	
	//trying to see if the main character is moving closer to the opponent
	//this is used for if statements
	public boolean mainDirection() {
		int dist = this.distanceBetween(_main.getLoc(), _opponent.getLoc());
		int oldDist = this.distanceBetween(_main.getPrevLoc(), _opponent.getPrevLoc());
		if (dist < oldDist) {
			return true;
		} else if (dist >= oldDist) {
			return false;
		}
		return false;
	}
	
	//trying to see if main is attacking
	public boolean mainAttack() {
		if(_main.getAttack() == true || _main.getSuper() == true) {
			return true;
		}
		return false;
	}
	
	//running algorithm that basically is the opposite of searchAndMove (finds point farthest
	//away)
	public void run() {
		
		ArrayList<int[]> possibleMoves = this.possibleLocation(_opponent.getLoc(), 
				_opponent.getPrevLoc(), 1);
			int Dist = 0;
			int[] target = new int[2];
			target = _main.getLoc(); 
			int[] moveSquareCoor = new int[2];
	 		int moveSquareName = 0;
			for (int i = 0; i < 1; i++) {
				Dist = this.distanceBetween(possibleMoves.get(i), target);
				moveSquareCoor = possibleMoves.get(i);
				moveSquareName = i;		
	 		}
				for (int i = 1; i < possibleMoves.size(); i++) {
					int dist = this.distanceBetween(possibleMoves.get(i), target);
						if (dist > Dist) { 
								Dist = dist;
								moveSquareCoor = possibleMoves.get(i);
								moveSquareName = i;		
						}
				}
			
			//I have a directionNames list so that the movement methods in Shinobi are used
			//even when the Character is searching. The directionNames list is ordered corresponding 
			//to the respected determined location
			switch(_directionNames.get(moveSquareName)) {
			
			case UP:
				_opponent.moveUp();
				break;
			case DOWN:
				_opponent.moveDown();
				break;
			case RIGHT:
				_opponent.moveRight();
				break;
			case LEFT:
				_opponent.moveLeft();
				break;
			case TOPLEFT:
				_opponent.topLeft();
				break;
			case BOTTOMLEFT:
				_opponent.bottomLeft();
				break;
			case TOPRIGHT:
				_opponent.topRight();
				break;
			case BOTTOMRIGHT:
				_opponent.bottomRight();
				break;
			}
			_directionNames.clear();
	}
	
	//finding the closest point to the target & moving there
	public void searchAndMove() {
		
		ArrayList<int[]> possibleMoves = this.possibleLocation(_opponent.getLoc(), 
			_opponent.getPrevLoc(), 0);
		int Dist = 0;
		int[] target = new int[2];
		target = _main.getLoc(); 
		int[] moveSquareCoor = new int[2];
 		int moveSquareName = 0;
		for (int i = 0; i < 1; i++) {
			Dist = this.distanceBetween(possibleMoves.get(i), target);
			moveSquareCoor = possibleMoves.get(i);
			moveSquareName = i;		
 		}
			for (int i = 1; i < possibleMoves.size(); i++) {
				int dist = this.distanceBetween(possibleMoves.get(i), target);
					if (dist < Dist) { 
							Dist = dist;
							moveSquareCoor = possibleMoves.get(i);
							moveSquareName = i;		
					}
			}
		
		switch(_directionNames.get(moveSquareName)) {
		
		case UP:
			_opponent.moveUp();
			break;
		case DOWN:
			_opponent.moveDown();
			break;
		case RIGHT:
			_opponent.moveRight();
			break;
		case LEFT:
			_opponent.moveLeft();
			break;
		case TOPLEFT:
			_opponent.topLeft();
			break;
		case BOTTOMLEFT:
			_opponent.bottomLeft();
			break;
		case TOPRIGHT:
			_opponent.topRight();
			break;
		case BOTTOMRIGHT:
			_opponent.bottomRight();
			break;
		}
		
		_directionNames.clear();
	}
	
	//this makes and returns an arrayList that gives all of the in bounds and not-opposite directions
	//based on the opponents current & previous locations
	public ArrayList<int[]> possibleLocation(int[] currentOpponentLocation, 
		int[] previousOpponentLocation, int opposite) {
		
		int[] up = {currentOpponentLocation[0], currentOpponentLocation[1] - Constants.STEP};
		int[] down = {currentOpponentLocation[0], currentOpponentLocation[1] + Constants.STEP};
		int[] left = {currentOpponentLocation[0] - Constants.STEP, currentOpponentLocation[1]};
		int[] right = {currentOpponentLocation[0] + Constants.STEP, currentOpponentLocation[1]};
		int[] bottomRight = {currentOpponentLocation[0] + Constants.STEP, 
				currentOpponentLocation[1] + Constants.STEP};
		int[] topLeft = {currentOpponentLocation[0] - Constants.STEP, 
				currentOpponentLocation[1] - Constants.STEP};
		int[] topRight = {currentOpponentLocation[0] + Constants.STEP, 
				currentOpponentLocation[1] - Constants.STEP};
		int[] bottomLeft = {currentOpponentLocation[0] - Constants.STEP, 
			currentOpponentLocation[1] + Constants.STEP};
	
		ArrayList<int[]> directions = new ArrayList<int[]>();
		
		if (opposite == 1) {
			//added this if statement because I want the opponent to be able to move 
			//the opposite direction when running away
			
			if (up[0] >= 0 && up[0] <= Constants.ROOT_BOUNDX && up[1] >= Constants.START_W && 
					up[1] <= Constants.ROOT_BOUNDY) {
					directions.add(up);
					_directionNames.add(Direction.UP);	
				}
				if (down[0] >= 0 && down[0] <= Constants.ROOT_BOUNDX && down[1] >= Constants.START_W 
					&& down[1] <= Constants.ROOT_BOUNDY) {
					directions.add(down);
					_directionNames.add(Direction.DOWN);	
				}
				if (left[0] >= 0 && left[0] <= Constants.ROOT_BOUNDX && left[1] >= Constants.START_W 
					&& left[1] <= Constants.ROOT_BOUNDY) {
					directions.add(left);
					_directionNames.add(Direction.LEFT);	
				}
				if (right[0] >= 0 && right[0] <= Constants.ROOT_BOUNDX && right[1] >= Constants.START_W 
					&& right[1] <= Constants.ROOT_BOUNDY) {
					directions.add(right);
					_directionNames.add(Direction.RIGHT);	
				}
				if (bottomRight[0] >= 0 && bottomRight[0] <= Constants.ROOT_BOUNDX 
					&& bottomRight[1] >= Constants.START_W 
					&& bottomRight[1] <= Constants.ROOT_BOUNDY) {
					directions.add(bottomRight);
					_directionNames.add(Direction.BOTTOMRIGHT);	
				}
				if (topLeft[0] >= 0 && topLeft[0] <= Constants.ROOT_BOUNDX 
					&& topLeft[1] >= Constants.START_W 
					&& topLeft[1] <= Constants.ROOT_BOUNDY) {
					directions.add(topLeft);
					_directionNames.add(Direction.TOPLEFT);	
				}
				if (topRight[0] >= 0 && topRight[0] <= Constants.ROOT_BOUNDX 
					&& topRight[1] >= Constants.START_W 
					&& topRight[1] <= Constants.ROOT_BOUNDY) {
					directions.add(topRight);
					_directionNames.add(Direction.TOPRIGHT);	
				}
				if (bottomLeft[0] >= 0 && bottomLeft[0] <= Constants.ROOT_BOUNDX 
					&& bottomLeft[1] >= Constants.START_W 
					&& bottomLeft[1] <= Constants.ROOT_BOUNDY) {
					directions.add(bottomLeft);
					_directionNames.add(Direction.BOTTOMLEFT);	
				}	
			
		} else if (opposite == 0) {
			//this is for when the character is searching & can't move the opposite direction
			//and uses DirectionTest to fidn out which is the opposite direction
		
			int differenceX = currentOpponentLocation[0] - previousOpponentLocation[0];
			int differenceY = currentOpponentLocation[1] - previousOpponentLocation[1];
		
			DirectionTest findOut = new DirectionTest(Direction.RIGHT);

			if (differenceX < 0 && differenceY == 0) {
				findOut = new DirectionTest(Direction.RIGHT);
			}
			if (differenceX > 0 && differenceY == 0) { 
				findOut = new DirectionTest(Direction.LEFT);
			}	
			if (differenceX == 0 && differenceY > 0) {
				findOut = new DirectionTest(Direction.UP);
			}
			if (differenceX == 0 && differenceY < 0) {
				findOut = new DirectionTest(Direction.DOWN);
			}
			if (differenceX < 0 && differenceY < 0) {
				findOut = new DirectionTest(Direction.BOTTOMRIGHT);
			}
			if (differenceX > 0 && differenceY > 0) {
				findOut = new DirectionTest(Direction.TOPLEFT);
			}
			if (differenceX < 0 && differenceY > 0) { 
				findOut = new DirectionTest(Direction.TOPRIGHT);
			}
			if (differenceX > 0 && differenceY < 0) {
				findOut = new DirectionTest(Direction.BOTTOMLEFT);
			}
	
			if (up[0] >= 0 && up[0] <= Constants.ROOT_BOUNDX && up[1] >= Constants.START_W && 
				up[1] <= Constants.ROOT_BOUNDY && !findOut.test(Direction.UP)) {
				directions.add(up);
				_directionNames.add(Direction.UP);	
			}
			if (down[0] >= 0 && down[0] <= Constants.ROOT_BOUNDX && down[1] >= Constants.START_W 
				&& down[1] <= Constants.ROOT_BOUNDY && !findOut.test(Direction.DOWN)) {
				directions.add(down);
				_directionNames.add(Direction.DOWN);	
			}
			if (left[0] >= 0 && left[0] <= Constants.ROOT_BOUNDX && left[1] >= Constants.START_W 
				&& left[1] <= Constants.ROOT_BOUNDY && !findOut.test(Direction.LEFT)) {
				directions.add(left);
				_directionNames.add(Direction.LEFT);	
			}
			if (right[0] >= 0 && right[0] <= Constants.ROOT_BOUNDX && right[1] >= Constants.START_W 
				&& right[1] <= Constants.ROOT_BOUNDY && !findOut.test(Direction.RIGHT)) {
				directions.add(right);
				_directionNames.add(Direction.RIGHT);	
			}
			if (bottomRight[0] >= 0 && bottomRight[0] <= Constants.ROOT_BOUNDX 
				&& bottomRight[1] >= Constants.START_W && bottomRight[1] <= Constants.ROOT_BOUNDY 
				&& !findOut.test(Direction.BOTTOMRIGHT)) {
				directions.add(bottomRight);
				_directionNames.add(Direction.BOTTOMRIGHT);	
			}
			if (topLeft[0] >= 0 && topLeft[0] <= Constants.ROOT_BOUNDX 
				&& topLeft[1] >= Constants.START_W && topLeft[1] <= Constants.ROOT_BOUNDY 
				&& !findOut.test(Direction.TOPLEFT)) {
				directions.add(topLeft);
				_directionNames.add(Direction.TOPLEFT);	
			}
			if (topRight[0] >= 0 && topRight[0] <= Constants.ROOT_BOUNDX 
				&& topRight[1] >= Constants.START_W && topRight[1] <= Constants.ROOT_BOUNDY 
				&& !findOut.test(Direction.TOPRIGHT)) {
				directions.add(topRight);
				_directionNames.add(Direction.TOPRIGHT);	
			}
			if (bottomLeft[0] >= 0 && bottomLeft[0] <= Constants.ROOT_BOUNDX 
				&& bottomLeft[1] >= Constants.START_W && bottomLeft[1] <= Constants.ROOT_BOUNDY 
				&& !findOut.test(Direction.BOTTOMLEFT)) {
				directions.add(bottomLeft);
				_directionNames.add(Direction.BOTTOMLEFT);	
			}	
		}
		return directions;
	}
	
}
