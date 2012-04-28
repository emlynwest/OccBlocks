/*
 * ToolBar.java
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

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.*;
import occblocks.Preferences;

/**
 * This will contain various buttons and tools used for editing.
 * The cursor tool is for selecting/moving things.
 * The channel tool is for creating/editing channels.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class ToolBar extends JPanel{
    
    private JButton cursor;
    private JButton channel;
    private JButton newDoc;
    private JButton compile;
    
    public ToolBar(){
        this.setLayout(new BorderLayout());
        
        //Grab the size of icons to use
        String iconSize = Preferences.instance().getPreferenceValue("icon_size");
        
        //Pannel to contain all the buttons on the left of the tool bar
        JPanel leftButtons = new JPanel();
        
        //The save button
        newDoc = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/page_add.png"));
        newDoc.setMargin(new Insets(0, 0, 0, 0));
        leftButtons.add(newDoc);
        
        //The save button
        JButton save = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/disk.png"));
        save.setMargin(new Insets(0, 0, 0, 0));
        leftButtons.add(save);
        
//        //The save as button
//        JButton saveAs = new JButton(
//                new ImageIcon("./resources/icons/"+iconSize+"/disk_multiple.png"));
//        saveAs.setMargin(new Insets(0, 0, 0, 0));
//        leftButtons.add(saveAs);
        
        //The add process button
        JButton addProc = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/brick_add.png"));
        addProc.setMargin(new Insets(0, 0, 0, 0));
        leftButtons.add(addProc);
        
        //Remove process button
        JButton remProc = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/brick_delete.png"));
        remProc.setMargin(new Insets(0, 0, 0, 0));
        leftButtons.add(remProc);
        
        //Cursor tool button
        cursor = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/cursor.png"));
        cursor.setMargin(new Insets(0, 0, 0, 0));
        cursor.setEnabled(false);
        leftButtons.add(cursor);
        
        //Channel tool button
        channel = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/brick_link.png"));
        channel.setMargin(new Insets(0, 0, 0, 0));
        leftButtons.add(channel);
        
        //Panel to contain all the buttons to be aligned to the right
        JPanel rightButtons = new JPanel();
        
        //The compile button
        compile = new JButton(
                new ImageIcon("./resources/icons/"+iconSize+"/cog_go.png"));
        compile.setMargin(new Insets(0, 0, 0, 0));
        rightButtons.add(compile);
        
        //Finally add the panels to the tool bar
        this.add(leftButtons, BorderLayout.WEST);
        this.add(rightButtons, BorderLayout.EAST);
    }
    
     /**
     * Enables the channel tool button and disables the cursor tool
     */
    public void enableChannelButton(){
        channel.setEnabled(false);
        cursor.setEnabled(true);
    }
    
    /**
     * Enables the cursor tool button and disables the channel tool
     */
    public void enableCursorButton(){
        channel.setEnabled(true);
        cursor.setEnabled(false);
    }

    public void addCursorAction(ActionListener actionListener) {
        cursor.addActionListener(actionListener);
    }
    
    public void addChannelAction(ActionListener actionListener) {
        channel.addActionListener(actionListener);
    }
    
    public void addNewAction(ActionListener actionListener){
        newDoc.addActionListener(actionListener);
    }
    
    public void addCompileAction(ActionListener actionListener){
        compile.addActionListener(actionListener);
    }
}
