/*
 * ProgramMenu.java
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
package occblocks.gui;

import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-17
 */
public class ProgramMenu extends JMenuBar{
    
    private JMenuItem prefs;
    private JMenuItem exit;
    
    private JMenuItem new_;
    private JMenuItem open;
    private JMenuItem save;
//    private JMenuItem saveAs;
    private JMenuItem imports;
    private JMenuItem generateCode;
    
    private JMenuItem cursor;
    private JMenuItem channel;
    private JMenuItem addProc;
    private JMenuItem rmProc;
            
    private JMenuItem about;
    
    /**
     * Builds the menu bar that is displayed on the main window. This will include
     * things like the file menu for saving and the like as well as help and
     * preferences
     */
    public ProgramMenu(){
        this.add(buildAppMenu());
        this.add(buildFileMenu());
        this.add(buildToolMenu());
        this.add(Box.createHorizontalGlue());
        this.add(buildHelpMenu());
    }
    
    /**
     * Builds the tools menu
     * @return 
     */
    private JMenu buildToolMenu(){
        JMenu tools = new JMenu("Tools");
        tools.setMnemonic('T');
        
        cursor = new JMenuItem("Cursor");
        cursor.setEnabled(false);
        cursor.setMnemonic('C');
        tools.add(cursor);
        
        channel = new JMenuItem("Channel");
        channel.setMnemonic('h');
        tools.add(channel);
        
        tools.addSeparator();
        
        addProc = new JMenuItem("Add process");
        addProc.setMnemonic('A');
        tools.add(addProc);
        
        rmProc = new JMenuItem("Delete process");
        rmProc.setMnemonic('D');
        tools.add(rmProc);
        
        return tools;
    }
    
    /**
     * Builds the application menu
     * @return 
     */
    private JMenu buildAppMenu(){
        JMenu appMenu = new JMenu("OccBlocks");
        appMenu.setMnemonic('O');
        
        prefs = new JMenuItem("Preferences");
        prefs.setMnemonic('P');
        appMenu.add(prefs);
        
        appMenu.addSeparator();
        
        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        appMenu.add(exit);
        
        return appMenu;
    }
    
    /**
     * Builds the file menu
     * @return 
     */
    private JMenu buildFileMenu(){
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        new_ = new JMenuItem("New");
        new_.setMnemonic('N');
        fileMenu.add(new_);
        
        open = new JMenuItem("Open");
        open.setMnemonic('O');
        fileMenu.add(open);
        
        fileMenu.addSeparator();
        
        save = new JMenuItem("Save");
        save.setMnemonic('S');
        fileMenu.add(save);
        
//        saveAs = new JMenuItem("Save as");
//        saveAs.setMnemonic('a');
//        fileMenu.add(saveAs);
        
        fileMenu.addSeparator();
        
        imports = new JMenuItem("Imports");
        imports.setMnemonic('I');
        fileMenu.add(imports);
        
        generateCode = new JMenuItem("Generate Code");
        generateCode.setMnemonic('G');
        fileMenu.add(generateCode);
        
        return fileMenu;
    }
    
    /**
     * Builds the help menu
     * @return 
     */
    private JMenu buildHelpMenu(){
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        
        about = new JMenuItem("About");
        about.setMnemonic('A');
        
        helpMenu.add(about);
        
        return helpMenu;
    }
    
     /**
     * Enables the channel tool and disables the cursor tool
     */
    public void enableChannelMenu(){
        channel.setEnabled(false);
        cursor.setEnabled(true);
    }
    
    /**
     * Enables the cursor tool and disables the channel tool
     */
    public void enableCursorMenu(){
        channel.setEnabled(true);
        cursor.setEnabled(false);
    }
    
    /**
     * Adds an action to be performed when the prefs menu item is pressed
     * @param al 
     */
    public void addPrefsAction(ActionListener al){
        prefs.addActionListener(al);
    }
    
    /**
     * Adds an action to be performed when the exit menu item is pressed
     * @param al 
     */
    public void addExitAction(ActionListener al){
        exit.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu new item is pressed
     * @param al 
     */
    public void addNewAction(ActionListener al){
        new_.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu open item is pressed
     * @param al 
     */
    public void addOpenAction(ActionListener al){
        open.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu save item is pressed
     * @param al 
     */
    public void addSaveAction(ActionListener al){
        save.addActionListener(al);
    }
//    /**
//     * Adds an action to be performed when the menu save as item is pressed
//     * @param al 
//     */
//    public void addSaveAsAction(ActionListener al){
//        saveAs.addActionListener(al);
//    }
    /**
     * Adds an action to be performed when the menu cursor item is pressed
     * @param al 
     */
    public void addCursorAction(ActionListener al){
        cursor.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu channel item is pressed
     * @param al 
     */
    public void addChannelAction(ActionListener al){
        channel.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu add proc item is pressed
     * @param al 
     */
    public void addAddProcAction(ActionListener al){
        addProc.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu remove proc item is pressed
     * @param al 
     */
    public void addRmProcAction(ActionListener al){
        rmProc.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu about item is pressed
     * @param al 
     */   
    public void addAboutAction(ActionListener al){
        about.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu imports item is pressed
     * @param al 
     */   
    public void addImportsAction(ActionListener al){
        imports.addActionListener(al);
    }
    /**
     * Adds an action to be performed when the menu generate code item is pressed
     * @param al 
     */   
    public void addGenerateCodeAction(ActionListener al){
        generateCode.addActionListener(al);
    }
}
