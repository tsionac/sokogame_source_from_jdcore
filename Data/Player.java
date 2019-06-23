package Data;

public class Player {
	protected int x;
	protected int y;
	protected int counter;

	public Player(int x, int y) {
		this.setX(x);
		this.setY(y);

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
