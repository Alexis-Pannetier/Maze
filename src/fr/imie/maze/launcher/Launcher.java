package fr.imie.maze.launcher;

import fr.imie.maze.controller.MazeGenerator;
import fr.imie.maze.controller.MazeSolver;

public class Launcher {

	public static void main(String[] args) throws Exception {
		boolean validArguments = false;
		String argument = null;
		String fileName = null;
		int y = 0; // Min:2
		int x = 0; // Min:2
		int number = 1;

		// Arguments check
		try {
			argument = args[0];
		} catch (Exception e) {
			System.out.println("Wrong first argument");
		}
		if (argument.compareTo("single") == 0 || argument.compareTo("debug") == 0) {
			try {
				y = Integer.parseInt(args[1]);
				x = Integer.parseInt(args[2]);
				validArguments = true;
			} catch (Exception e) {
				validArguments = false;
				System.out.println("The Heigt & Width must be a number");
			}
			try {
				fileName = args[3];
			} catch (Exception e) {
				System.out.println("Wrong file name");
			}
		} else if (argument.compareTo("multiple") == 0) {
			try {
				number = Integer.parseInt(args[1]);
				y = Integer.parseInt(args[2]);
				x = Integer.parseInt(args[3]);
				validArguments = true;
			} catch (Exception e) {
				validArguments = false;
				System.out.println("Number, Heigt & Width must be a valid number");
			}
			try {
				fileName = args[4];
			} catch (Exception e) {
				System.out.println("Wrong file name");
			}
		}

		if (validArguments) {
			new MazeGenerator(argument, number, y, x, fileName);
			new MazeSolver(argument, number, fileName);
		} else {
			System.out.println("The first argument must be \"single\", \"multiple\" or \"debug\".");
		}
	}
}
