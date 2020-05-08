package Indy;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/** The Shinobi Class takes care of all functionality for any character.
 */

public class Shinobi {

	private int[] _loc;
	private int[] _prevLoc;
	private ImageView _viewChar;
	private Timeline _canSuperTimeline;
	private Timeline _superTimeline;
	private Timeline _attackTimeline;
	private Timeline _moveSuperTimeline;
	private Timeline _moveAttackTimeline;
	private Timeline _showSuperTimeline;
	private Timeline _canAttackTimeline;
	private Pane _root;
	private Pane _superRoot;
	private Attack _attack;
	private SuperAttack _superAttack;
	private int _whichChar;
	private Label _canSuper;
	private boolean _canSuperAttack;
	private int _canSuperCount;
	private boolean _canAttack;
	private Rectangle _superRect;
	
	public Shinobi(Pane root, int x, int y, int whichChar, Pane superRoot) {
		
		_root = root;
		_superRoot = superRoot;
		_whichChar = whichChar; //0 for naruto & 1 for sasuke
		//Generally, for any whichCharacter instance variables 0 means naruto & 1 means sasuke
		//and for CharacterIs variables, 0 means main & 1 means opponent
		Image img = null;
		if(whichChar == 0) {
			img = new Image(this.getClass().getResourceAsStream("/images/narutoimage.png"));
		} else if(whichChar == 1) {
			img = new Image(this.getClass().getResourceAsStream("/images/sasukeimage.png"));
		}
		_viewChar = new ImageView();
		_viewChar.setImage(img);
		_viewChar.setFitWidth(Constants.CHAR_WIDTH);
		_viewChar.setFitHeight(180);
		_viewChar.setPreserveRatio(true);
		_viewChar.setSmooth(true);
		root.getChildren().add(_viewChar);
		_viewChar.setX(x);
		_viewChar.setY(y);
		_loc = new int[2];
		_prevLoc = new int[2];
		_loc[0] = x;
		_loc[1] = y;
		_canSuperTimeline = null;
		_superTimeline = null;
		_attackTimeline = null;
		_moveSuperTimeline = null;
		_moveAttackTimeline = null;
		_showSuperTimeline = null;
		_canAttackTimeline = null;
		_attack = null;
		_superAttack = null;
		Rectangle narutoRect = new Rectangle(Constants.BAR_WIDTH, Constants.NSUPER, 
				Constants.ATTACK, Constants.BAR_WIDTH);
		Rectangle sasukeRect = new Rectangle(Constants.BAR_WIDTH, Constants.SSUPER, 
				Constants.ATTACK, Constants.BAR_WIDTH);
		narutoRect.setStrokeWidth(1);
		narutoRect.setStroke(Color.DARKORANGE);
		sasukeRect.setStrokeWidth(1);
		sasukeRect.setStroke(Color.CYAN);
		_superRoot.getChildren().addAll(narutoRect, sasukeRect);
		//setting up super attack bars because shinobi also has control over the
		//_superRoot because making a new class would require it to know about
		//shinobi
		_canSuperAttack = false;
		_canSuper = null;
		_canSuperCount = 0;
		_canAttack = true;
		_superRect = new Rectangle();
		_superRoot.getChildren().add(_superRect);
		this.setupCanSuperTimeline();
		this.setupCanAttackTimeline();
	}
	
	//this is called when pause is pressed
	public void pauseAll(boolean pauseOrPlay) {
		
		if(!pauseOrPlay) {
			_canSuperTimeline.playFromStart();
			_canAttackTimeline.playFromStart();
			if(_attackTimeline != null) {
				if (_attackTimeline.getStatus() == Status.PAUSED) {
					_attackTimeline.playFromStart();
				}
			}
			if(_moveAttackTimeline != null) {
				if (_moveAttackTimeline.getStatus() == Status.PAUSED) {
					_moveAttackTimeline.playFromStart();
				}
			}
			if(_superTimeline != null) {	
				if (_superTimeline.getStatus() == Status.PAUSED) {
					_superTimeline.playFromStart();
				}
			}
			if(_moveSuperTimeline != null) {
				if (_moveSuperTimeline.getStatus() == Status.PAUSED) {
					_moveSuperTimeline.playFromStart();
				}
			}
		} else if (pauseOrPlay) {
			_canSuperTimeline.pause();
			_canAttackTimeline.pause();
			if(_attackTimeline != null) {
				if (_attackTimeline.getStatus() == Status.RUNNING) {
					_attackTimeline.pause();
				}
			}
			if(_moveAttackTimeline != null) {
				if (_moveAttackTimeline.getStatus() == Status.RUNNING) {
					_moveAttackTimeline.pause();
				}
			}
			if(_superTimeline != null) {
				if (_superTimeline.getStatus() == Status.RUNNING) {
					_superTimeline.pause();
				}
			}
			if(_moveSuperTimeline != null) {
				if (_moveSuperTimeline.getStatus() == Status.RUNNING) {
					_moveSuperTimeline.pause();
				}
			}
		}
	}
	//this is called in gameOver
	public void stopAll() {
		_canSuperTimeline.stop();
		if(_attackTimeline != null) {
				_attackTimeline.stop();
		}
		if(_moveAttackTimeline != null) {
				_moveAttackTimeline.stop();
		}
		if(_superTimeline != null) {
				_superTimeline.stop();
		}
		if(_moveSuperTimeline != null) {
				_moveSuperTimeline.stop();
		}
	}
	
	//setting up timeline to keep track of when char can superAttack
	public void setupCanSuperTimeline() {
		
		KeyFrame kf = new KeyFrame(Duration.seconds(1), new CanHandler());
		_canSuperTimeline = new Timeline(kf);
		_canSuperTimeline.setCycleCount(Animation.INDEFINITE);
		_canSuperTimeline.play();
	}
	
	private class CanHandler implements EventHandler<ActionEvent> {
		
		public CanHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			
			if(_canSuperCount < Constants.BAR_WIDTH) {
				_canSuperCount++;
				//updating bars every second
				if(_whichChar == 0) {
					_superRoot.getChildren().remove(_superRect);
					_superRect = new Rectangle (Constants.BAR_WIDTH, Constants.NSUPER, 
							4 * _canSuperCount, Constants.BAR_WIDTH);
					_superRect.setFill(Color.DARKORANGE);
					_superRoot.getChildren().add(_superRect);
				}
				if (_whichChar == 1) {
					_superRoot.getChildren().remove(_superRect);
					_superRect = new Rectangle (Constants.BAR_WIDTH, Constants.SSUPER, 
							4 * _canSuperCount, Constants.BAR_WIDTH);
					_superRect.setFill(Color.CYAN);
					_superRoot.getChildren().add(_superRect);
				}
			}
			//stopping bar when it has reached max
			if(_canSuperCount == Constants.BAR_WIDTH) {
				_canSuperAttack = true;
				_canSuperTimeline.pause();
				Shinobi.this.setSuperLabel();
				Shinobi.this.showSuperTimeline();
			}
		}
	}
	
	public void setupCanAttackTimeline() {
		KeyFrame kf = new KeyFrame(Duration.seconds(3), new CanAttackHandler());
		_canAttackTimeline = new Timeline(kf);
		_canAttackTimeline.setCycleCount(Animation.INDEFINITE);
		_canAttackTimeline.play();
	}
	
	private class CanAttackHandler implements EventHandler<ActionEvent> {
		
		public CanAttackHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_canAttack = true;
			_canAttackTimeline.pause();
		}
	}
	
	//sets up label to notify character
	public void setSuperLabel() {
		_canSuper = new Label("SUPER!");
		if (_whichChar == 0) {
			_canSuper.setTextFill(Color.DARKORANGE);
			_canSuper.setLayoutY(Constants.NSUPER_L);
		} else if (_whichChar == 1) {
			_canSuper.setTextFill(Color.CYAN);
			_canSuper.setLayoutY(Constants.SSUPER_L);
		}
		_canSuper.setLayoutX(Constants.LABEL_Y);
		_canSuper.setFocusTraversable(false);
		_superRoot.getChildren().add(_canSuper);
	}
	
	public void showSuperTimeline() {
		
		KeyFrame kf = new KeyFrame(Duration.seconds(1), new ShowSuperHandler());
		_showSuperTimeline = new Timeline(kf);
		_showSuperTimeline.setCycleCount(1);
		_showSuperTimeline.play();	
	}
	
	private class ShowSuperHandler implements EventHandler<ActionEvent> {
		
		public ShowSuperHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_superRoot.getChildren().remove(_canSuper);
			//I don't want the label to last forever so I have a timeline
			//to remove it after 1 second
		}
	}
	
	public void setupSuperAttackTimeline(){
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.SUPER_LENGTH), new FinishSuperHandler());
		_superTimeline = new Timeline(kf);
		_superTimeline.setCycleCount(1);
		_superTimeline.play();		
	}
	
	private class FinishSuperHandler implements EventHandler<ActionEvent> {
		
		public FinishSuperHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_superAttack.attackOver();
			//Like the super Label timeline, this timeline is to remove the super
			//attack after it's duration
		}
	}
	
	//these move Timelines are here so that the attack will move with the character
	public void setupMoveSuperAttackTimeline() {
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.MOVE_SUPER), new ShiftHandler());
		_moveSuperTimeline = new Timeline(kf);
		_moveSuperTimeline.setCycleCount(Constants.MOVES);
		_moveSuperTimeline.play();		
	}
	
	private class ShiftHandler implements EventHandler<ActionEvent> {
		
		public ShiftHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_superAttack.moveWith(_loc);
		}
	}
	
	public void setupAttackTimeline(){
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.REG_LENGTH), new FinishHandler());
		_attackTimeline = new Timeline(kf);
		_attackTimeline.setCycleCount(1);
		_attackTimeline.play();		
	}
	
	private class FinishHandler implements EventHandler<ActionEvent> {
		
		public FinishHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_attack.attackOver();
			//Ends attack after duration
		}
	}
	
	public void setupMoveAttackTimeline() {
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.MOVE_REG), new MoveHandler());
		_moveAttackTimeline = new Timeline(kf);
		_moveAttackTimeline.setCycleCount(Constants.MOVES);
		_moveAttackTimeline.play();		
	}
	
	private class MoveHandler implements EventHandler<ActionEvent> {
		
		public MoveHandler() {	
		}
		
		@Override 
		public void handle(ActionEvent event) {
			_attack.moveWith(_loc);
			//allows the attack to move with the character
		}
	}
	
	//basic movement methods and if statements to keep the character in bounds
	public void moveRight() {
		if ((_loc[0] + Constants.STEP) >= 0 && (_loc[0] + Constants.STEP) <= Constants.ROOT_BOUNDX) {
			_prevLoc = _loc;
			_loc[0] += Constants.STEP;
			_viewChar.setX(_loc[0]);
		}
	}

	public void moveLeft() {
		if ((_loc[0] - Constants.STEP) >= 0 && (_loc[0] - Constants.STEP) <= Constants.ROOT_BOUNDX) {
			_prevLoc = _loc;
			_loc[0] -= Constants.STEP;
			_viewChar.setX(_loc[0]);
		}
	}

	public void moveUp() {
		if ((_loc[1] - Constants.STEP) >= Constants.START_W && (_loc[1] - Constants.STEP) <= Constants.ROOT_BOUNDY) {
			_prevLoc = _loc;
			_loc[1] -= Constants.STEP;
			_viewChar.setY(_loc[1]);	
		}
	}

	public void moveDown() {
		if ((_loc[1] + Constants.STEP) >= Constants.START_W && (_loc[1] + Constants.STEP) <= Constants.ROOT_BOUNDY) {
		_prevLoc = _loc;
		_loc[1] += Constants.STEP;
		_viewChar.setY(_loc[1]);	
		}
	}

	//attacking and setting up the timelines for movement and ending
	public void attack(int[] place) {
		_attack = new Attack(_whichChar, _loc[0], _loc[1], 
			place[0], place[1], _root);
		this.setupAttackTimeline();
		this.setupMoveAttackTimeline();
		_canAttack = false;
		_canAttackTimeline.playFromStart();
	}

	//same as attacking but then the canSuperCount & timeline is reset
	public void superAttack(int[] place) {
		_superAttack = new SuperAttack(_whichChar, _loc[0], _loc[1], 
				place[0], place[1], _root);	
		this.setupSuperAttackTimeline();
		this.setupMoveSuperAttackTimeline();
		_canSuperAttack = false;
		_superRoot.getChildren().remove(_superRect);
		_canSuperCount = 0;
		_canSuperTimeline.playFromStart();
	}

	//basic movement methods but these are only employed if the char is the computer
	public void topRight() {
		if ((_loc[1] - Constants.STEP) >= Constants.START_W
				&& (_loc[1] - Constants.STEP) <= Constants.ROOT_BOUNDY &&
				(_loc[0] + Constants.STEP) >= 0 
				&& (_loc[0] + Constants.STEP) <= Constants.ROOT_BOUNDX) {
		_prevLoc = _loc;
		_loc[1] -= Constants.STEP;
		_loc[0] += Constants.STEP;
		_viewChar.setY(_loc[1]);
		_viewChar.setX(_loc[0]);
		}
	}

	public void topLeft() {
		if ((_loc[1] - Constants.STEP) >= Constants.START_W
				&& (_loc[1] - Constants.STEP) <= Constants.ROOT_BOUNDY &&
				(_loc[0] - Constants.STEP) >= 0 
				&& (_loc[0] - Constants.STEP) <= Constants.ROOT_BOUNDX) {
		_prevLoc = _loc;
		_loc[1] -= Constants.STEP;
		_loc[0] -= Constants.STEP;
		_viewChar.setY(_loc[1]);
		_viewChar.setX(_loc[0]);
		}
	}

	public void bottomRight() {
		if ((_loc[1] + Constants.STEP) >= Constants.START_W
				&& (_loc[1] + Constants.STEP) <= Constants.ROOT_BOUNDY &&
				(_loc[0] + Constants.STEP) >= 0 
				&& (_loc[0] + Constants.STEP) <= Constants.ROOT_BOUNDX) {
		_prevLoc = _loc;
		_loc[0] += Constants.STEP;
		_loc[1] += Constants.STEP;
		_viewChar.setX(_loc[0]);
		_viewChar.setY(_loc[1]);
		}
	}

	public void bottomLeft() {
		if ((_loc[1] + Constants.STEP) >= Constants.START_W
				&& (_loc[1] + Constants.STEP) <= Constants.ROOT_BOUNDY &&
				(_loc[0] - Constants.STEP) >= 0 
				&& (_loc[0] - Constants.STEP) <= Constants.ROOT_BOUNDX) {
		_prevLoc = _loc;
		_loc[1] += Constants.STEP;
		_loc[0] -= Constants.STEP;
		_viewChar.setY(_loc[1]);
		_viewChar.setX(_loc[0]);
		}
	}
	
	//these are used by fight for searching 
	public int[] getLoc() {
		return _loc;
	}

	public int[] getPrevLoc() {
		return _prevLoc;
	}

	public boolean getCanSuper() {
		return _canSuperAttack;
	}
	
	public boolean getCanAttack() {
		return _canAttack;
	}
	
	public boolean getAttack() {
		if (_attackTimeline != null) {
			if (_attackTimeline.getStatus() == Status.RUNNING) {
				return true;
			}
		}
		return false;
	}
	
	public boolean getSuper() {
		if (_superTimeline != null) {
			if (_superTimeline.getStatus() == Status.RUNNING) {
				return true;
			}
		}
		return false;
	}
	
	//this is for the FadeTransition in Fight (occurs when Char loses points)
	public Node getNode() {
		return _viewChar;
	}
	
}
