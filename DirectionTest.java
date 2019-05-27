package Indy;

public class DirectionTest {

	private Direction _dir;
	
	public DirectionTest(Direction invalid) {
		_dir = invalid;
	}
	
	public boolean test(Direction direct) {
		
		if(_dir == direct) {
			return true;
		}
		return false;
	}
}
