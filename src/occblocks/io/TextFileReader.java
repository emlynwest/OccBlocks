/*
 * TextFileReader.java
 * Copyright (C) 2011 Steven West, University of Kent <sw349@kent.ac.uk>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 */
package occblocks.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads the content of a text file. Please note that you will have to set a 
 * file location before you are able to read from it!
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-11-02
 */
public class TextFileReader {

    private String fileLocation;
    private ArrayList<String> fileContent;

    /**
     * @return the file location
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * @param fileLocation the file location to set
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Attempts to read the content of the file set with setFileLocation().
     * The content can then be loaded with getFileContent()
     * @throws FileNotFoundException Will be thrown if the file does not exist
     */
    public void readFileContent() throws FileNotFoundException {
        fileContent = new ArrayList<String>();
        
        Scanner scanner = new Scanner(new FileInputStream(new File(fileLocation)));
        try {
            while (scanner.hasNextLine()) {
                getFileContent().add(scanner.nextLine());
            }
        } catch( Exception e){
            System.out.println(e);
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /**
     * Gets the content of the file after it has been read
     * @return the fileContent
     */
    public ArrayList<String> getFileContent() {
        return fileContent;
    }
}
