/*
 * OccBlocks.java
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
package occblocks;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import occblocks.blocks.Document;
import occblocks.blocks.Import;
import occblocks.blocks.PROC;
import occblocks.blocks.channel.ChannelEnd;
import occblocks.blocks.channel.EndType;
import occblocks.blocks.type.Type;
import occblocks.codegen.ImportRegister;
import occblocks.io.DocumentLoader;
import occblocks.gui.DocumentWindow;
import occblocks.gui.UserOptionSelector;

/**
 * This is the main entry point for the program.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-17
 */
public class OccBlocks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        occblocks.gui.JFrameUtils.loadDefaultLookAndFeel();

        UserOptionSelector uos = new UserOptionSelector();

        uos.setVisible(true);

        if (uos.getUserOption() == UserOptionSelector.OPTION_EXIT) {
            System.exit(0);
        } else {
            if (uos.getUserOption() == UserOptionSelector.OPTION_NEW) {
                createNewDoc(null);
            } else if (uos.getUserOption() == UserOptionSelector.OPTION_OPEN){
                openExistingDoc(null);
            }
        }
    }

    public static DocumentWindow openExistingDoc(Component parent){
        //Create a new document, first ask the user
        JFileChooser fileChooser = new JFileChooser(
                new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new OccFileFilter());

        fileChooser.showSaveDialog(parent);

        File selectedFile = fileChooser.getSelectedFile();

        DocumentWindow docWindow = null;
        
        if(selectedFile != null){
            Document doc = null;
            
            try{
                doc = DocumentLoader.openDocument(selectedFile.getAbsolutePath());
            } catch (java.io.InvalidClassException e){
                JOptionPane.showMessageDialog(parent,
                        "Incompatable version",
                        "Unable to open",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            if(doc == null)
                return null;
            
            docWindow = new DocumentWindow(doc);
        }
        
        return docWindow;
    }
    
    public static DocumentWindow createNewDoc(Component parent) {
        //Create a new document, first ask the user
        JFileChooser fileChooser = new JFileChooser(
                new File(System.getProperty("user.home")));
        fileChooser.setSelectedFile(new File("unnamed.obd"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new OccFileFilter());

        fileChooser.showSaveDialog(parent);

        File selectedFile = fileChooser.getSelectedFile();

        DocumentWindow docWindow = null;
        if (selectedFile != null) {
            //Get the name of the document
            String name = selectedFile.getName();
            String path = selectedFile.getPath().substring(0,
                    selectedFile.getPath().length() - name.length() - 1);
            //Make sure the selected file ends with a .occ
            if (!selectedFile.getName().endsWith(".obd"))
                name += ".obd";

            //Build a root proc
            String rootProcName = name.substring(0, name.length()-4);
            PROC proc = new PROC(rootProcName);
            ChannelEnd keyboard = new ChannelEnd(Type.BYTE, EndType.READ, null);
            keyboard.setName("keyboard");
            keyboard.setHeaderParam(true);
            
            ChannelEnd screen = new ChannelEnd(Type.BYTE, EndType.WRITE, null);
            screen.setName("screen");
            screen.setHeaderParam(true);
            
            ChannelEnd error = new ChannelEnd(Type.BYTE, EndType.WRITE, null);
            error.setName("error");
            error.setHeaderParam(true);

            proc.addPROCParam(keyboard);
            proc.addPROCParam(screen);
            proc.addPROCParam(error);

            Document newDoc = new Document(name, proc);
            newDoc.setLocation(path + File.separator + name);
            //Add the courde.module import by default, perhaps change this later
            for(Import imp : ImportRegister.getInstance().getFullImportList()){
                if(imp.getName().equals("course.module")){
                    newDoc.addImport(imp);
                    break;
                }
            }

            docWindow = new DocumentWindow(newDoc);
        }
        return docWindow;
    }
}

/**
 * A custom file filter so we can single out .occ files
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 */
class OccFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if (f.getName().endsWith(".obd")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "OccBlocks Document file";
    }
}
