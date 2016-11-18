package com.oprun.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HelperFile {

	public static void writeIntoFile(String file, String line) {

		try {

			Files.write(Paths.get("C:\\temp\\" + file), line.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("line " + line + " : print file KO.");
		}

	}

}
