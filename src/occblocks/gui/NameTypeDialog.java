/*
 * NameTypeDialog.java
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

import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Shows a dialog that allows the user to enter or edit information about a CHAN
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-01-31
 */
public class NameTypeDialog extends JDialog{
    
    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_OK = 1;
    
    private Window parent;
    private JTextField nameEntry;
    private JTextField typeEntry;
    private int lastResult;
    private JLabel nameLabel;
    
    /**
     * Constructs the dialog.
     * @param parent 
     */
    public NameTypeDialog(Window parent, String title){
        super(parent, title);
        this.parent = parent;
        this.setModal(true);
        
        this.setLayout(new GridLayout(3,2));
        
        //Load up the window with some content
        this.add(nameLabel = new JLabel("Name:"));
        nameEntry = new JTextField();
        this.add(nameEntry);
        
        this.add(new JLabel("Type:"));
        typeEntry = new JTextField();
        this.add(typeEntry);
        
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                closeWindowWithFailure();
            }
        });
        this.add(cancel);
        
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                closeWindowWithWin();
            }
        });
        this.add(ok);
        
        //This allows us to respond to key presses so the user can hit enter 
        //instead of clicking the buttons
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        closeWindowWithWin();
                    } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        closeWindowWithFailure();
                    }
                }

                return false;
            }
        });
        
        pack();
        this.setSize(300, getHeight());
    }

    /**
     * @return the type the user entered or null if there was not one
     */
    public occblocks.blocks.type.Type getEnteredType() {
        String type = typeEntry.getText();
        
        if(type.trim().equals(""))
            return null;
        
        return new occblocks.blocks.type.Type(type);
    }

    /**
     * Sets the type to show when the dialog opens
     * @param type the type to set
     */
    public void setType(occblocks.blocks.type.Type type) {
        typeEntry.setText(type.getType());
    }

    /**
     * @return the chanName the user entered or null if there was not one
     */
    public String getEnteredName() {
        String name = nameEntry.getText();
        
        if(name.trim().equals(""))
            return null;
        
        return name;
    }

    /**
     * Sets the channel name to be shown when the dialog opens
     * @param chanName the chanName to set
     */
    public void setChanName(String chanName) {
        nameEntry.setText(chanName);
    }
    
    /**
     * Closes the window after setting the result to RESULT_CANCEL
     */
    private void closeWindowWithFailure(){
        lastResult = RESULT_CANCEL;
        this.setVisible(false);
    }
    
    /**
     * Closes the window after setting the result to RESULT_OK
     */
    private void closeWindowWithWin(){
        lastResult = RESULT_OK;
        this.setVisible(false);
    }
    
    /**
     * Shows the dialog to the user, centered over the parent window.
     * When the dialog is closed this method will return RESULT_OK or RESULT_CANCEL
     * @return RESULT_OK if the user clicked ok or pressed the enter key, RESULT_CANCEL if they pressed the cancel button or pressed escape
     */
    public int showDialog(){
        JFrameUtils.centerWindow(parent, this);
        this.setVisible(true);
        
        return lastResult;
    }
    
    /**
     * Sets the text for the name entry label
     * @param label 
     */
    public void setNameLabel(String label){
        nameLabel.setText(label);
    }
}
