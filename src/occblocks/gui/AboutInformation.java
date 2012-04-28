/*
 * AboutInformation.java
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
import javax.swing.*;

/**
 * Shows information about the program and its author (that's me!)
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-11-11
 */
public class AboutInformation extends JDialog{
    
    /**
     * Constructs the about information window with the given parent.
     * By default this is not modal.
     * @param owner 
     */
    public AboutInformation(Frame owner) {
        super(owner);
        
        this.setTitle("About OccBlocks");
        this.setResizable(false);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        this.addLine(getLogoPanel());
        this.addLine(new JLabel("Created by: Steve \"Uru\" West at the University of Kent"));
        this.addLine(new JLabel("<sw349@kent.ac.uk>"));
        this.addLine(new JLabel("Version: Alpha"));
        this.addLine(new JLabel(""));
        this.addLine(new JLabel("No animals, humans, dust puppies or rubber ducks"));
        this.addLine(new JLabel("where harmed in the creation of this program."));
        
        JButton closeButton = new JButton("Ok, thanks!");
        closeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                closeWindow();
            }
        });
        this.addLine(closeButton);
        
        this.pack();
    }
    
    /**
     * Creates a panel to hold the logos
     * @return 
     */
    private JPanel getLogoPanel(){
        JPanel panel = new JPanel();
        
        JLabel programIcon = new JLabel("");
        programIcon.setIcon(new ImageIcon("./resources/icons/logo64.png"));
        panel.add(programIcon);
        
        JLabel ukcLogo = new JLabel("");
        ukcLogo.setIcon(new ImageIcon("./resources/icons/ukc_logo.png"));
        panel.add(ukcLogo);
        
        return panel;
    }
    
    /**
     * Closes the window
     */
    private void closeWindow(){
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
}
