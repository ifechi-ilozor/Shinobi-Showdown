package Indy;

/**
 * The DirectionTest Class helps the game logic
 * interact with the Direction class.
 */
public class DirectionTest {
	private Direction _dir;
	
	public DirectionTest(Direction invalid) {
		_dir = invalid;
	}
	
	public boolean test(Direction direct) {
		return _dir == direct;
	}
}
