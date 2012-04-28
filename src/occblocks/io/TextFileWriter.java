/*
 * TextFileWriter.java
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

import java.io.*;

/**
 *
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class TextFileWriter {
    
    /**
     * Tries to write the given content out to the given location.
     * @param location
     * @param content
     * @throws IOException 
     */
    public static void writeFile(String location, String content)
            throws IOException{
        
        FileWriter fileStream = new FileWriter(location);
        BufferedWriter out = new BufferedWriter(fileStream);
        
        out.write(content);
        
        out.close();
    }
}
