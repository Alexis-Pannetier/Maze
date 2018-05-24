package fr.imie.maze.model;

public class Path extends Point {

	public Path(int y, int x) {
		super(y, x);
	}

	public void setPosition(Point p) {
		this.y = p.getY();
		this.x = p.getX();
	}

	public int getY() {
		return super.getY();
	}

	public void setY(int y) {
		super.setY(y);
	}

	public int getX() {
		return super.getX();
	}

	public void setX(int x) {
		super.setX(x);
	}

	public void setPosition(int y, int x) {
		this.y = y;
		this.x = x;
	}

	public char getAsString() {
		return '+';
	}

	static public char getAsStringStatic() {
		return '+';
	}
	
	@Override
	public String toString() {
		return "Path [y=" + y + ", x=" + x + "]";
	}
	
}
