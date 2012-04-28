/*
 * DocumentLoader.java
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

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import occblocks.blocks.Document;
import occblocks.blocks.Import;
import occblocks.codegen.ImportRegister;

/**
 * This is responsible for loading a Document from a given file
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-17
 */
public class DocumentLoader {
    
    public static Document openDocument(String location) throws java.io.InvalidClassException{
        ObjectInputStream oos = null;
        
        Document openedDoc = null;
        
        try {
            FileInputStream fos = new FileInputStream(location);
            oos = new ObjectInputStream(fos);
            
            openedDoc = (Document) oos.readObject();
            oos.close();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            
        }
        
        //Unflattern all the imports
            for(Import imp : openedDoc.getImports()){
                Import newImp = ImportRegister.getInstance().convertToFull(imp);
                if(newImp != null){
                    openedDoc.removeImport(imp);
                    openedDoc.addImport(newImp);
                }
            }
        
        return openedDoc;
    }
}
