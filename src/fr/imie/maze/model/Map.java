package fr.imie.maze.model;

public class Map {

	int sizeY;
	int sizeX;
	Point[][] map;

	public Map(int y, int x) {
		this.sizeY = y;
		this.sizeX = x;
		map = new Point[sizeY][sizeX];
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public Point getPoint(int y, int x) {
		return map[y][x];
	}

	public void setPosition(int y, int x, Point p) {
		this.map[y][x] = p;
	}

}
