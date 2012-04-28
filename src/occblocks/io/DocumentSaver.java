/*
 * DocumentSaver.java
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

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import occblocks.blocks.Document;
import occblocks.blocks.Import;
import occblocks.codegen.ImportRegister;

/**
 * This is responsible for taking in a Document object and saving it to a given
 * location. At the moment this will simply dump everything out as binary objects
 * although there are plans to perhaps make this a little more flexible,
 * such as using XML or actual occam-pi source code.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-17
 */
public class DocumentSaver {
    
    public static boolean saveDocumentTo(String location, Document toSave){
        ObjectOutputStream oos = null;
        
        boolean result = true;
        
        //Flattern all the imports
        for(Import imp : toSave.getImports()){
            Import newImp = ImportRegister.getInstance().convertToEmpty(imp);
            if(newImp != null){
                toSave.removeImport(imp);
                toSave.addImport(newImp);
            }
        }
            
        
        try {
            FileOutputStream fos = new FileOutputStream(location);
            oos = new ObjectOutputStream(fos);
            
            oos.writeObject(toSave);
            oos.close();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            
            result = false;
            
        } finally {
            //Unflattern all the imports
            for(Import imp : toSave.getImports()){
                Import newImp = ImportRegister.getInstance().convertToFull(imp);
                if(newImp != null){
                    toSave.removeImport(imp);
                    toSave.addImport(newImp);
                }
            }
        }
        
        return result;
    }
}
