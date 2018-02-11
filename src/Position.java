
public class Position {
	private int x;
	private int y;
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return "" + x + "," + y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	@Override
	public int hashCode(){
		return (x*17) + (y*31);
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Position){
			Position pos = (Position) o;
			if(getX() == pos.getX() && getY() == pos.getY()){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
