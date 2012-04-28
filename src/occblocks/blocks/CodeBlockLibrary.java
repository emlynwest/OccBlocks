/*
 * CodeBlockLibrary.java
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
package occblocks.blocks;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import occblocks.Preferences;
import occblocks.io.TextFileReader;

/**
 * This class is simply to provide a list of available CodeBlock subclasses at
 * runtime. This list will exclude PROC, the root document block and imports.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-11-02
 */
public class CodeBlockLibrary {

    //This makes sure that the class is initised on JVM startup
    private static CodeBlockLibrary instance = new CodeBlockLibrary();
    private HashMap<String, CodeBlock> list;

    /**
     * This is private to stop the class being used normally. For a usable
     * instance use getInstance()
     * @see http://www.javaworld.com/javaworld/javatips/jw-javatip113.html?page=2
     */
    private CodeBlockLibrary() {
        list = new HashMap<String, CodeBlock>();
        refreshBlockList();
    }

    private void refreshBlockList() {
        //The code for this has been stolen from:
        // http://www.javaworld.com/javaworld/javatips/jw-javatip113.html?page=2
        //But modified not to create instances of the objects (incase they have
        //constructors with parameters)

        TextFileReader tfr = new TextFileReader();
        String fileLocation = Preferences.instance().getPreferenceValue("codeblock_list_location");
        tfr.setFileLocation(fileLocation);
        try{
            tfr.readFileContent();
        } catch (FileNotFoundException e){
            System.out.println(e);
        }

        //Loop through each file and poke it
        for (String classname : tfr.getFileContent()) {
            try {
                //Check to see if it's a subclass of CodeBlock and not
                //CodeBlock it self or one of the "non-listed" classes
                Class current = Class.forName(classname);
                if (CodeBlock.class.isAssignableFrom(current)){

                    CodeBlock cb = (CodeBlock) current.newInstance();

                    list.put(classname, cb);
                }
            } catch (ClassNotFoundException ex) {
                //Oops something went wrong, this class does not exist
                System.out.println(ex);

            } catch (IllegalAccessException iae) {
                System.out.println(iae);

            } catch (InstantiationException ie) {
                System.out.println(ie);
            }
        }
    }

    /**
     * Will not return PROC, CodeBlock, CodeBlockLibary, etc.
     * @return The known CodeBlocks
     */
    public Collection<CodeBlock> getBlocks() {
        return list.values();
    }

    /**
     * 
     * @return The pre-setup instance for this class
     */
    public static CodeBlockLibrary getInstance() {
        return instance;
    }
}
