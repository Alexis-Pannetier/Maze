package fr.imie.maze.controller;

import java.util.ArrayList;

import fr.imie.maze.model.Direction;
import fr.imie.maze.model.Free;
import fr.imie.maze.model.Start;
import fr.imie.maze.model.Wall;
import fr.imie.maze.model.Goal;
import fr.imie.maze.model.Map;
import fr.imie.maze.model.Point;
import fr.imie.maze.view.View;

public class MazeGenerator {

	static boolean debug = false;
	static int debugSpeed = 100;
	Map map;
	View view;
	Point start;
	Point free;
	Point goal;
	ArrayList<Point> dirList;
	static FileManager file;

	public MazeGenerator(String argument, int number, int y, int x, String fileName) throws Exception {
		MazeGenerator.file = new FileManager(fileName);
		if (y > 2 && x > 2) {
			if (argument.compareTo("single") == 0) {
				mazeGeneratorSingle(y, x);
			} else if (argument.compareTo("multiple") == 0) {
				mazeGeneratorMultiple(y, x, number);
			} else if (argument.compareTo("debug") == 0) {
				MazeGenerator.debug = true;
				mazeGeneratorSingle(y, x);
			}
		} else {
			System.out.println("Heigt & Width, must be bigger than 2");
		}
	}

	private void mazeGeneratorMultiple(int y, int x, int number) throws Exception {
		FileManager.newFolder();
		for (int i = 0; i < number; i++) {
			map = new Map(y, x);
			view = new View(map);

			setMap();
			setGoal();
			setFree();
			setStart();
			System.out.println(view.showMaze());
			FileManager.saveFile(view.showMaze(), FileManager.getPath() + FileManager.getFileName() + "-" + (i + 1));
		}
		FileManager.zipFiles(number);
		FileManager.deleteFolder(FileManager.getFileName());
	}

	private void mazeGeneratorSingle(int y, int x) throws InterruptedException {
		map = new Map(y, x);
		view = new View(map);

		setMap();
		setGoal();
		setFree();
		setStart();
		System.out.println(view.showMaze());
		FileManager.saveFile(view.showMaze(), FileManager.getFileName());
	}

	private void setMap() {
		for (int j = 0; j < map.getSizeY(); j++) {
			for (int i = 0; i < map.getSizeX(); i++) {
				map.setPosition(j, i, new Wall(j, i));
			}
		}
	}

	private void setGoal() {
		int count = 0;
		int random = (int) (Math.random() * ((map.getSizeY() * 2) + (map.getSizeX() * 2) - 4));
		for (int j = 0; j < map.getSizeY(); j++) {
			for (int i = 0; i < map.getSizeX(); i++) {
				if (j < 1 || j == map.getSizeY() - 1 || i == 0 || i == map.getSizeX() - 1) {
					if (count == random) {
						goal = new Goal(j, i);
						map.setPosition(j, i, goal);
						return;
					} else {
						count++;
					}
				}
			}
		}
	}

	private void setFree() throws InterruptedException {
		dirList = new ArrayList<Point>();
		ArrayList<Point> track = new ArrayList<Point>();
		Point currentNeightbours;
		int random = 0;
		int iTrack = 0;
		track.add(goal);
		// Back-Track algorithm
		while (track.get(iTrack) != goal || track.size() == 1) {
			if (checkNeighboursOfNeighbours(track.get(iTrack)) > 0) {
				random = (int) (Math.random() * dirList.size()); // Random
				currentNeightbours = (Point) dirList.get(random);
				Point free = new Free(currentNeightbours.getY(), currentNeightbours.getX());
				map.setPosition(currentNeightbours.getY(), currentNeightbours.getX(), free);
				track.add(currentNeightbours);
				iTrack = track.size() - 1;
				if (debug) { // Debug
					System.out.println("\n" + view.showMaze());
					Thread.sleep(debugSpeed);
				}
			} else {
				iTrack--;
			}
		}
	}

	private void setStart() {
		Point valid;
		Point p = null;
		Point pos1 = new Point(0, 0); // min
		Point pos2 = new Point(0, 0); // max
		ArrayList<Point> perfectPos = new ArrayList<Point>();
		ArrayList<Point> correctPos = new ArrayList<Point>();
		int random = 0;
		// Opposite side of Start point
		if (goal.getY() < map.getSizeY() / 2 && goal.getX() < map.getSizeX() / 2) {// Goal top-left
			pos1.setPos(map.getSizeY() / 2, map.getSizeX() / 2);
			pos2.setPos(map.getSizeY() - 1, map.getSizeX() - 1);

		} else if (goal.getY() < map.getSizeY() / 2 && goal.getX() >= map.getSizeX() / 2) {// Goal top-right
			pos1.setPos(map.getSizeY() / 2, 0);
			pos2.setPos(map.getSizeY() - 1, (map.getSizeX() / 2) - 1);

		} else if (goal.getY() >= map.getSizeY() / 2 && goal.getX() >= map.getSizeX() / 2) {// Goal bot-right
			pos1.setPos(0, 0);
			pos2.setPos((map.getSizeY() / 2) - 1, (map.getSizeX() / 2) - 1);

		} else if (goal.getY() >= map.getSizeY() / 2 && goal.getX() < map.getSizeX() / 2) {// Goal bot-left
			pos1.setPos(0, map.getSizeX() / 2);
			pos2.setPos((map.getSizeY() / 2) - 1, map.getSizeX() - 1);
		}
		for (int j = pos1.getY(); j <= pos2.getY(); j++) {
			for (int i = pos1.getX(); i <= pos2.getX(); i++) {
				if ((j == 0 || j == map.getSizeY() - 1 || i == 0 || i == map.getSizeX() - 1)) {// Check if position is
																								// border map
					p = new Point(j, i);
					// Perfect position
					if (neighbours(p, free) == 1
							&& map.getPoint(p.getY(), p.getX()).getAsString() == free.getAsString()) {
						valid = new Point(j, i);
						perfectPos.add(valid);
						// Correct position
					} else if (neighbours(p, free) == 2
							&& map.getPoint(p.getY(), p.getX()).getAsString() == free.getAsString()) {
						valid = new Point(j, i);
						correctPos.add(valid);
					}
				}
			}
		}
		if (perfectPos.size() > 0) {
			random = (int) (Math.random() * perfectPos.size());
			start = new Start(perfectPos.get(random).getY(), perfectPos.get(random).getX());
		} else if (correctPos.size() > 0) {
			random = (int) (Math.random() * correctPos.size());
			start = new Start(correctPos.get(random).getY(), correctPos.get(random).getX());
		}
		map.setPosition(start.getY(), start.getX(), start);
		return;
	}

	private int checkNeighboursOfNeighbours(Point currentPoint) {
		Direction dir = new Direction();
		int validNeighbours = 0;
		dirList.clear();
		// Check if one neighbours is available
		for (int i = 0; i < 4; i++) {
			Point neighboursPoint = null;
			if (i == 0) {
				neighboursPoint = new Point(dir.top(currentPoint).getY(), dir.top(currentPoint).getX());
			} else if (i == 1) {
				neighboursPoint = new Point(dir.right(currentPoint).getY(), dir.right(currentPoint).getX());
			} else if (i == 2) {
				neighboursPoint = new Point(dir.bottom(currentPoint).getY(), dir.bottom(currentPoint).getX());
			} else if (i == 3) {
				neighboursPoint = new Point(dir.left(currentPoint).getY(), dir.left(currentPoint).getX());
			}
			// Neightbours inside map
			if (neighboursPoint.getY() >= 0 && neighboursPoint.getX() >= 0 && neighboursPoint.getY() < map.getSizeY()
					&& neighboursPoint.getX() < map.getSizeX()) {
				// Neighbours is not a goal Point & not a free area
				free = new Free(0, 0);
				if (map.getPoint(neighboursPoint.getY(), neighboursPoint.getX()).getAsString() != goal.getAsString()
						&& map.getPoint(neighboursPoint.getY(), neighboursPoint.getX()).getAsString() != free
								.getAsString()) {
					// Correct Neighbours if only one way
					if ((neighbours(neighboursPoint, free) < 2 && neighbours(neighboursPoint, goal) == 0)
							|| (neighbours(neighboursPoint, free) == 0 && neighbours(neighboursPoint, goal) == 1)) {
						dirList.add(neighboursPoint);
						validNeighbours++;
					}
				}
			}
		}
		return validNeighbours;
	}

	private int neighbours(Point p, Point pointType) {
		Direction direction = new Direction();
		int neighboursNbr = 0;
		for (int i = 0; i < 4; i++) {
			Point neighboursP = new Point(p.getY(), p.getX());
			if (i == 0) {
				neighboursP = new Point(direction.top(p).getY(), direction.top(p).getX());
			} else if (i == 1) {
				neighboursP = new Point(direction.right(p).getY(), direction.right(p).getX());
			} else if (i == 2) {
				neighboursP = new Point(direction.bottom(p).getY(), direction.bottom(p).getX());
			} else if (i == 3) {
				neighboursP = new Point(direction.left(p).getY(), direction.left(p).getX());
			}
			if (neighboursP.getY() < 0 || neighboursP.getY() >= map.getSizeY() || neighboursP.getX() < 0
					|| neighboursP.getX() >= map.getSizeX()) {
				continue;
			} else if (map.getPoint(neighboursP.getY(), neighboursP.getX()).getAsString() == pointType.getAsString()) {
				neighboursNbr++;
			}
		}
		return neighboursNbr;
	}

}
