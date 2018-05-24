package fr.imie.maze.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileManager {

	static String name;
	static String fileName;
	static String fileExtension;
	static String path;
	static String lineMaze;
	static int sizeY;
	static int sizeX;
	static int bufferSize;
	static ZipOutputStream zos;

	public FileManager(String name) {
		FileManager.bufferSize = 1024;
		FileManager.name = name;
		FileManager.fileName = name;
		FileManager.fileExtension = ".txt";
		if (fileName.length() > 4) {
			if (fileName.substring(fileName.length() - 4, fileName.length() - 3).compareTo(".") == 0) {
				fileName = fileName.substring(0, fileName.length() - 4);
			}
		}
		FileManager.path = fileName + "/";
	}

	// ----- GENERATOR -----
	public static void saveFile(String content, String name) {

		try (PrintWriter out = new PrintWriter(name + fileExtension)) {
			out.println(content);
			System.out.println("Saved in: " + name + fileExtension);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean newFolder() {
		boolean folderCreated = (new File(path)).mkdirs();
		if (!folderCreated) {
			System.out.println("Can't create a new folder.");
		}
		return folderCreated;
	}

	public static void zipFiles(int number) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName + ".zip");
			zos = new ZipOutputStream(fos);

			for (int i = 1; i <= number; i++) {
				addToZipFile(fileName + "-" + i + ".txt");
			}
			zos.close();
			fos.close();
			System.out.println("All files compressed in \"" + fileName + ".zip\"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToZipFile(String name) throws FileNotFoundException, IOException {

		File file = new File(path + name);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(path + name);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[bufferSize];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}
		zos.closeEntry();
		fis.close();

	}

	// ----- SOLVER -----
	public static boolean openFile(String name) {
		sizeY = 0;
		sizeX = 0;
		lineMaze = "";
		try {
			File f = new File(name + fileExtension);
			if (f.exists()) {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				try {
					String line = br.readLine();
					while (line != null) {
						Scanner scanner = new Scanner(line);
						String myLine = scanner.nextLine();
						for (int i = 0; i < myLine.length(); i++) {
							if (sizeY == 0) {
								sizeX++;
							}
							lineMaze += myLine.charAt(i);
						}
						sizeY++;
						line = br.readLine();
						scanner.close();
					}
					sizeX = (sizeX / 2) - 2;
					sizeY -= 2;
					br.close();
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean deleteFolder(String Name) {
		File index = new File(Name);
		String[] entries = index.list();

		if (!index.exists()) {
			System.out.println("Can't find the specific folder.");
		} else if (index.exists()) {
			for (String s : entries) {
				File currentFile = new File(index.getPath(), s);
				currentFile.delete();
			}
			index.delete();
		}
		return true;
	}

	public static void unzipFolder() throws FileNotFoundException, IOException {
		try {
			ZipFile zipFile = new ZipFile(fileName + ".zip");
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = zipEntry.getName();
				File file = new File(name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}
				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[bufferSize];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getSizeY() {
		return sizeY;
	}

	public static int getSizeX() {
		return sizeX;
	}

	public static String getLineMaze() {
		return lineMaze;
	}

	public static String getFileName() {
		return fileName;
	}

	public static String getPath() {
		return path;
	}

}
