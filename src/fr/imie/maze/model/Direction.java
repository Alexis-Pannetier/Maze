package fr.imie.maze.model;

public class Direction {

	public Direction() {
	}

	public Point top(Point p) {
		return new Point(p.getY() + 1, p.getX());
	}

	public Point right(Point p) {
		return new Point(p.getY(), p.getX() + 1);
	}

	public Point bottom(Point p) {
		return new Point(p.getY() - 1, p.getX());
	}

	public Point left(Point p) {
		return new Point(p.getY(), p.getX() - 1);
	}

}
