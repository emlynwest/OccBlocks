/*
 * ImportList.java
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

import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import occblocks.blocks.CodeBlock;
import occblocks.blocks.CodeBlockTransferable;
import occblocks.blocks.Import;
import occblocks.blocks.PROC;

/**
 * Defines a list that can contain CodeBlocks. This class implements Java's DnD
 * framework so objects can be dragged out of the list and onto the editor.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-17
 */
public class ImportList extends JTree{

    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;
    
    
    /**
     * Sets up the new list of code blocks
     */
    public ImportList() {
        //Set up the needed objects
        rootNode = new DefaultMutableTreeNode("Imports");
        treeModel = new DefaultTreeModel(rootNode);
        this.setModel(treeModel);
        
        //And all the default settings
        this.setRootVisible(false);
        this.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        //set up dnd
        this.setDragEnabled(true);
        //Create the TransferHandler that allows us to drag and drop processes
        //onto the Canvas (or wherever)
        this.setTransferHandler(new TransferHandler() {

            @Override
            protected Transferable createTransferable(JComponent c) {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
                CodeBlock cb = (CodeBlock) node.getUserObject();
                //This clone is super important as we don't want to be editing
                //the same object over and over!
                return new CodeBlockTransferable(cb.clone());
            }

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }
        });
    }
    
    /**
     * Adds a list of PROCs contained within an import to this list
     * @param imp 
     */
    public void addImport(Import imp){
        //Create the root name with the name of the import
        DefaultMutableTreeNode importNode =
                new DefaultMutableTreeNode(imp.getName());
        
        //Load up all the PROCs
        for(PROC proc : imp.getPROCS()){
            DefaultMutableTreeNode procNode = new DefaultMutableTreeNode(proc);
            importNode.add(procNode);
        }
        treeModel.insertNodeInto(importNode, rootNode, rootNode.getChildCount());
        
        //Expand the root node so its children can be seen
        this.expandPath(new TreePath(rootNode.getPath()));
    }
    
    public void removeImport(Import imp){
        TreePath path = this.getNextMatch(imp.getName(), 0, Position.Bias.Forward);
        MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();
        treeModel.removeNodeFromParent(node);
    }
}
