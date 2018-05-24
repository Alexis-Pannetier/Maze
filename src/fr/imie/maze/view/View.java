package fr.imie.maze.view;

import fr.imie.maze.model.Map;

public class View {

	Map map;
	char border;

	public View(Map map) {
		this.map = map;
		this.border = 'X';
	}

	public String showMap() {
		String showMap = "";
		for (int j = 0; j < map.getSizeY(); j++) {
			for (int i = 0; i < map.getSizeX(); i++) {
				showMap += map.getPoint(j, i).getAsString() + " ";
			}
			showMap += "\n";
		}
		return showMap;
	}

	public String showMaze() {
		String showMap = "";
		String space = " ";
		int y = 0;
		int x = 0;
		// Border top
		for (int i = 0; i < map.getSizeX() + 2; i++) {
			showMap += border + space;
		}
		showMap += "\n";
		for (int j = 0; j < map.getSizeY(); j++) {
			for (int i = 0; i < map.getSizeX() + 2; i++) {
				// Border left
				if (i == 0) {
					showMap += border + space;
				} // Border right
				else if (i == map.getSizeX() + 1) {
					showMap += border + space;
					// Body
				} else {
					try {
						showMap += map.getPoint(y, x).getAsString() + space;
					} catch (Exception e) {
						System.out.println("y:" + y + " x:" + x + " -> Out of array.");
					}
					if (x + 1 == map.getSizeX()) {
						x = 0;
						y++;
					} else {
						x++;
					}
				}
			}
			showMap += "\n";
		}
		// Border Bottom
		for (int i = 0; i < map.getSizeX() + 2; i++) {
			showMap += border + space;
		}
		return showMap;
	}
}
