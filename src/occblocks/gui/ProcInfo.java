/*
 * ProcInfo.java
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
import javax.swing.*;
import occblocks.blocks.PROC;
import occblocks.blocks.PROCParam;

/**
 *
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-11-13
 */
public class ProcInfo extends JPanel{
    
    private PROC proc;
    
    private JLabel name;
    private DefaultListModel listModel;
    
    /**
     * Creates a new ProcInfo that displays information about the given PROC.
     * If this is set to null then the controls become disabled
     * @param proc 
     */
    public ProcInfo(PROC proc){
        buildComponents();
        setProc(proc);
    }
    
    /**
     * Creates a new ProcInfo with a null as the PROC.
     */
    public ProcInfo(){
        this(null);
    }
    
    /**
     * Sets the proc to display
     * @param newProc null to disable the controls
     */
    public final void setProc(PROC newProc){
        this.proc = newProc;
        if(proc == null)
            disableControls();
        else
            enableControls();
        updateComponents();
    }
    
    /**
     * Gets the PROC that is assigned to this ProcInfo
     * @return 
     */
    public PROC getProc(){
        return proc;
    }
    
    private void buildComponents(){
        this.setLayout(new BorderLayout());
        
        JPanel labelPanel = new JPanel();
        name = new JLabel("");
        labelPanel.add(name);
        this.add(labelPanel, BorderLayout.NORTH);
        
        listModel = new DefaultListModel();
        JList paramList = new JList(listModel);
        
        JScrollPane scroll = new JScrollPane(paramList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        this.add(scroll, BorderLayout.CENTER);
    }
    
    /**
     * Updates the information being displayed
     */
    private void updateComponents(){
        name.setText(proc.getName());
        
        listModel.clear();
        for(PROCParam param : proc.getPROCParams()){
            String proc = param.toString();
            listModel.addElement(proc);
        }
    }
    
    private void disableControls(){
        
    }
    
    private void enableControls(){
    }
}
