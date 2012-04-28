/*
 * ImportSelector.java
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

import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import occblocks.blocks.Document;
import occblocks.blocks.Import;
import occblocks.codegen.ImportRegister;

/**
 * This class is responsible for adding and removing Imports from a Document
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-04
 */
public class ImportSelector extends JDialog{
    
    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_OK = 1;
    
    private int lastResult = RESULT_CANCEL;
    private Document doc;
    private HashMap<Import, JCheckBox> importList;
    private Window parent;
    
    public ImportSelector(Window parent, Document doc){
        super(parent, "Imports");
        this.doc = doc;
        this.parent = parent;
        importList = new HashMap<Import, JCheckBox>();
        this.setLayout(new BorderLayout());
        this.setModal(true);
        
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
        
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(0,1));
        
        for(Import imp : ImportRegister.getInstance().getFullImportList()){
            JCheckBox cb = new JCheckBox(imp.getName());
            importList.put(imp, cb);
            
            for(Import docImp : doc.getImports())
                cb.setSelected(docImp.getName().equals(imp.getName()));
            
            cb.setText(imp.getName());
            itemsPanel.add(cb);
        }
        
        JScrollPane scroll = new JScrollPane(itemsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        this.add(scroll, BorderLayout.CENTER);
	
	JButton close = new JButton("Close");
	close.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent e){
		closeWindowWithWin();
	    }
	});
	this.add(close, BorderLayout.SOUTH);
        
        this.setSize(200, 350);
    }
    
    @Override
    public void setVisible(boolean visible){
        JFrameUtils.centerWindow(parent, this);
        //Update the doc with its new Imports
        doc.getImports().clear();
        
        for(Import imp : importList.keySet()){
            if(importList.get(imp).isSelected()){
                doc.addImport(imp);
            }
        }
        
        super.setVisible(visible);
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
     * @return the lastResult
     */
    public int getLastResult() {
        return lastResult;
    }
}
