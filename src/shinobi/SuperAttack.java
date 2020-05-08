package Indy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SuperAttack {

	private ImageView _viewSuperAttack;
	private Pane _root;
	private int _shiftx;
	private int _shifty;
	
	public SuperAttack(int a, int locx, int locy, int shiftx, int shifty, Pane root) {
		
		_root = root;
		_shiftx = shiftx;
		_shifty = shifty;
		Image img1 = new Image(this.getClass().getResourceAsStream("/images/razenshuriken(1).png"));
		Image img2 = new Image(this.getClass().getResourceAsStream("/images/chidori.png"));
		_viewSuperAttack = new ImageView();
		_viewSuperAttack.setFitWidth(170);
		_viewSuperAttack.setFitHeight(170);
		_viewSuperAttack.setPreserveRatio(true);
		_viewSuperAttack.setSmooth(true);
		
		
		if (a == 0) {
			_viewSuperAttack.setImage(img1);
		}
		if (a == 1) {
			_viewSuperAttack.setImage(img2);
		}
		
		_viewSuperAttack.setX(locx + shiftx);
		_viewSuperAttack.setY(locy + shifty);
		root.getChildren().add(_viewSuperAttack);
	}
	
	public void attackOver() {
		_root.getChildren().remove(_viewSuperAttack);
	}
	
	public void moveWith(int[] charLoc) {
		_viewSuperAttack.setX(charLoc[0] + _shiftx);
		_viewSuperAttack.setY(charLoc[1] + _shifty);
	}
}
