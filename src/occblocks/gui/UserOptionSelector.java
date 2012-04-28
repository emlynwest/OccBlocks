/*
 * UserOptionSelector.java
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 * This dialog will be used to ask the user if they want to start a new file,
 * open one or exit when the program is first opened.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-07
 */
public class UserOptionSelector extends JDialog{
    
    public static final int OPTION_NEW = 0;
    public static final int OPTION_OPEN = 1;
    public static final int OPTION_EXIT = 2;
    
    private int userOption = OPTION_EXIT;
    
    /**
     * Sets up the dialog ready for use
     */
    public UserOptionSelector(){
        super((JDialog)null, "OccBlocks");
        JFrameUtils.loadDefaultLookAndFeel();
        
        //Build a list of icons
        ArrayList<Image> iconList = new ArrayList<Image>();
        iconList.add(new ImageIcon("./resources/icons/logo128.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo64.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo48.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo32.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo16.png").getImage());

        this.setIconImages(iconList);
        
        this.setModal(true);
        this.setResizable(false);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        JLabel programIcon = new JLabel("");
        programIcon.setIcon(new ImageIcon("./resources/icons/logo64.png"));
        addLine(programIcon);
        
        JButton newButton = new JButton("Start new document");
        newButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                setUserOption(OPTION_NEW);
            }
        });
        addLine(newButton);
        
        JButton openButton = new JButton("Open existing document");
        openButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                setUserOption(OPTION_OPEN);
            }
        });
        addLine(openButton);
        
        JButton closeButton = new JButton("Exit");
        closeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                setUserOption(OPTION_EXIT);
            }
        });
        addLine(closeButton);
        
        this.pack();
    }
    
    /**
     * Sets the selected user option then closes the dialog
     * @param option 
     */
    private void setUserOption(int option){
        userOption = option;
        this.dispose();
    }
    
    /**
     * Adds a line to the window
     * @param content 
     */
    private void addLine(JComponent content){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(content);
        this.add(panel);
    }
    
    @Override
    public void setVisible(boolean visible){
        JFrameUtils.centerWindow(this);
        super.setVisible(visible);
    }
    
    /**
     * Gets whatever the user selected.
     * @return  One of OPTION_NEW, OPTION_OPEN, OPTION_EXIT
     */
    public int getUserOption(){
        return userOption;
    }
}
