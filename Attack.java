package Indy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The Attack class. This class is mostly for
 * the graphical part of attacking.
 */
public class Attack {
	private ImageView _viewAttack;
	private Pane _root;
	private int _shiftx;
	private int _shifty;
	
	public Attack(int a, int locx, int locy, int shiftx, int shifty, Pane root) {
		_root = root;
		_shiftx = shiftx;  
		_shifty = shifty;
		//storing the shift so that we can use it to keep the position relative to
		//the character when the character moves
		Image img1 = new Image(this.getClass().getResourceAsStream("razengan(1).png"));
		Image img2 = new Image(this.getClass().getResourceAsStream("fireball(1).png"));
		_viewAttack = new ImageView();
		_viewAttack.setFitWidth(Constants.ATTACK);
		_viewAttack.setFitHeight(Constants.ATTACK);
		_viewAttack.setPreserveRatio(true);
		_viewAttack.setSmooth(true);
		
		//choosing img based on character chosen 
		//0 & 1 here correspond to Naruto & Sasuke as opposed to main & opponent
		if (a == 0) {
			_viewAttack.setImage(img1);
		} else if (a == 1) {
			_viewAttack.setImage(img2);
		}
		
		_viewAttack.setX(locx + shiftx);
		_viewAttack.setY(locy + shifty);
		root.getChildren().add(_viewAttack);
	}
	
	//removing attack
	public void attackOver() {	
		_root.getChildren().remove(_viewAttack);
	}
	
	//moving the attack with the character
	public void moveWith(int[] charLoc) {
		_viewAttack.setX(charLoc[0] + _shiftx);
		_viewAttack.setY(charLoc[1] + _shifty);
	}

}
