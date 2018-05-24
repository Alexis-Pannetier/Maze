package fr.imie.maze.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import fr.imie.maze.model.Direction;
import fr.imie.maze.model.Free;
import fr.imie.maze.model.Start;
import fr.imie.maze.model.Goal;
import fr.imie.maze.model.Map;
import fr.imie.maze.model.Point;
import fr.imie.maze.model.Path;
import fr.imie.maze.model.Wall;
import fr.imie.maze.view.View;

public class MazeSolver {

	static boolean debug = false;
	static int debugSpeed = 100;
	static String lineMaze;
	static int sizeY;
	static int sizeX;
	Map map;
	View view;
	Point start;
	Point path;
	Point goal;
	ArrayList<Point> dirList;
	ArrayList<Point> visited;
	static FileManager file;

	// Maze Solver Manager
	public MazeSolver(String argument, int number, String fileName) throws InterruptedException {
		MazeGenerator.file = new FileManager(fileName);
		if (argument.compareTo("debug") == 0) {
			MazeSolver.debug = true;
			mazeSolverSingle(FileManager.getFileName());
		} else if (argument.compareTo("single") == 0) {
			mazeSolverSingle(FileManager.getFileName());
		} else if (argument.compareTo("multiple") == 0) {
			mazeSolverMultiple(FileManager.getFileName(), number);
		}
	}

	private void mazeSolverSingle(String fileName) throws InterruptedException {
		goal = new Start(0, 0);
		start = new Goal(0, 0);
		if (FileManager.openFile(fileName)) {
			sizeY = FileManager.getSizeY();
			sizeX = FileManager.getSizeX();
			lineMaze = FileManager.getLineMaze();

			map = new Map(sizeY, sizeX);
			view = new View(map);

			loadMap();
			solver();
			System.out.println(view.showMaze());
		} else {
			System.out.println("Can't solve the maze, the specific file doesn't exist");
		}
	}

	private void mazeSolverMultiple(String fileName, int number) throws InterruptedException {
		// Unzip all file into folder
		try {
			FileManager.unzipFolder();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 1; i <= number; i++) {

			start = new Start(0, 0);
			goal = new Goal(0, 0);
			if (FileManager.openFile(FileManager.getPath() + fileName + "-" + i)) {
				sizeY = FileManager.getSizeY();
				sizeX = FileManager.getSizeX();
				lineMaze = FileManager.getLineMaze();

				map = new Map(sizeY, sizeX);
				view = new View(map);

				loadMap();
				solver();
				System.out.println(view.showMaze());
				System.out.println(fileName + "-" + i + " solved!");
			} else {
				System.out.println("The specific file \"" + fileName + "\" doesn't exist");
			}
		}
		FileManager.deleteFolder(fileName);
	}

	private void loadMap() {
		int y = 0;
		int x = 0;
		for (int j = 0; j < sizeY + 2; j++) {
			for (int i = 0; i < ((sizeX + 2) * 2); i += 2) {
				if (j != 0 && j != sizeY + 1 && i != 0 && i != sizeX * 2 + 2) {
					writeToMap(j, i, y, x);
					x++;
					if (x >= sizeX) {
						x = 0;
						y++;
					}
				}
			}
		}
	}

	private void writeToMap(int j, int i, int y, int x) {
		char c = lineMaze.charAt(((j * (sizeX + 2)) * 2) + i);
		if (c == Free.getAsStringStatic()) {
			Point p = new Free(y, x);
			map.setPosition(y, x, p);
		} else if (c == Wall.getAsStringStatic()) {
			Point p = new Wall(y, x);
			map.setPosition(y, x, p);
		} else if (c == start.getAsString()) {
			start.setPos(y, x);
			map.setPosition(y, x, start);
		} else if (c == goal.getAsString()) {
			goal.setPos(y, x);
			map.setPosition(y, x, goal);
		}
	}

	private void solver() throws InterruptedException {
		dirList = new ArrayList<Point>();
		ArrayList<Point> track = new ArrayList<Point>();
		visited = new ArrayList<Point>();
		Point currentNeightbours;
		int random = 0;
		int iTrack = 0;
		track.add(goal);
		while (track.get(iTrack) != start || track.size() == 1) {
			if (checkNeighboursOfNeighbours(track.get(iTrack)) > 0) {
				if (dirList.size() > 0) {
					random = (int) (Math.random() * dirList.size()); // Random
					currentNeightbours = (Point) dirList.get(random);
					if (currentNeightbours.getY() == start.getY() && currentNeightbours.getX() == start.getX()) {
						return;
					}
					if (checkNeighboursOfNeighbours(track.get(iTrack)) > 1) { // add point already visited
						visited.add(currentNeightbours);
					}
					path = new Path(currentNeightbours.getY(), currentNeightbours.getX());
					map.setPosition(currentNeightbours.getY(), currentNeightbours.getX(), path);
					track.add(currentNeightbours);
					iTrack = track.size() - 1;
					if (debug) { // Debug
						System.out.println(view.showMaze());
						Thread.sleep(debugSpeed);
					}
				}
			} else {
				while (checkNeighboursOfNeighbours(track.get(iTrack)) < 1) {
					visited.add(track.get(iTrack));
					map.setPosition(track.get(iTrack).getY(), track.get(iTrack).getX(),
							new Free(track.get(iTrack).getY(), track.get(iTrack).getX()));
					track.remove(iTrack);
					iTrack--;
					if (debug) { // Debug
						System.out.println(view.showMaze());
						Thread.sleep(debugSpeed);
					}
				}
			}
		}
	}

	private int checkNeighboursOfNeighbours(Point p) {
		Direction direction = new Direction();
		int validNeighbours = 0;
		dirList.clear();
		// Check if one neighbours is available
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
			// Neightbours inside map
			Point free = new Free(0, 0);
			if (neighboursP.getY() >= 0 && neighboursP.getX() >= 0 && neighboursP.getY() < map.getSizeY()
					&& neighboursP.getX() < map.getSizeX() && neighboursP.getAsString() != free.getAsString()) {
				// Correct Neighbours
				if (map.getPoint(neighboursP.getY(), neighboursP.getX()).getAsString() == start.getAsString()) {
					dirList.clear();
					dirList.add(neighboursP);
					validNeighbours = 1;
					return validNeighbours;
				} else if (visited(neighboursP) == false
						&& map.getPoint(neighboursP.getY(), neighboursP.getX()).getAsString() == free.getAsString()) {
					dirList.add(neighboursP);
					validNeighbours++;
				}
			}
		}
		return validNeighbours;
	}

	private boolean visited(Point p) {
		for (int i = 0; i < visited.size(); i++) {
			if (p.getY() == visited.get(i).getY() && p.getX() == visited.get(i).getX()) {
				return true;
			}
		}
		return false;
	}

}
