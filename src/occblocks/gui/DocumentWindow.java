/*
 * DocumentWindow.java
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
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import occblocks.Preferences;
import occblocks.blocks.*;
import occblocks.codegen.CodeGen;
import occblocks.codegen.ImportRegister;
import occblocks.io.DocumentSaver;
import occblocks.io.OccdocXMLReader;
import occblocks.io.TextFileWriter;

/**
 * This class handles the display of the main program window and its sub-components
 * 
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-16
 */
public class DocumentWindow extends JFrame{
    
    private Document document;
    private JTree tree;
    private ToolBar toolBar;
    private ProgramMenu programMenu;
    private Canvas rootPROCCanvas;
    private ImportList importList;

    public DocumentWindow(Document document){
        super("OccBlocks ~ "+document.getLocation());
        
        this.document = document;
        
        //Set up the window
        //Load the defualt look and feel to make the place look shiny
        JFrameUtils.loadDefaultLookAndFeel();

        //Build a list of icons
        ArrayList<Image> iconList = new ArrayList<Image>();
        //It's good to have lots of sizes as we are not sure how the image
        //will be used. Gnome might only use the 16x16 version for a window
        //title, while Windows7 might use the larger 48x48 version for it's 
        //whiny task bar
        iconList.add(new ImageIcon("./resources/icons/logo128.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo64.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo48.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo32.png").getImage());
        iconList.add(new ImageIcon("./resources/icons/logo16.png").getImage());

        this.setIconImages(iconList);

        //Make sure the ends the program when we are done. This will have to be
        //changed to enable file saving
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Set an inital size and layout
        //TODO: Make the window remember it's last location and status
        this.setSize(750, 750);
        
        this.setLayout(new BorderLayout());
        
        //Create a new Canvas to throw our blocks onto
        rootPROCCanvas = new Canvas(document.getRootProcess(), this);

        //Add it to a nice scroll pane so we can make it big without worrying
        JScrollPane scroll = new JScrollPane(rootPROCCanvas,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        //Build all the resizeable panes
        JSplitPane paneOne = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        paneOne.setTopComponent(itemsPanel());
        paneOne.setBottomComponent(scroll);
        paneOne.setResizeWeight(0);
        paneOne.setOneTouchExpandable(true);
        paneOne.setDividerSize(10);
        
        JSplitPane paneTwo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        paneTwo.setTopComponent(paneOne);
        paneTwo.setBottomComponent(importPanel());
        paneTwo.setResizeWeight(1);
        paneTwo.setOneTouchExpandable(true);
        paneTwo.setDividerSize(10);
        
        this.add(paneTwo, BorderLayout.CENTER);
        
        //Add the menu
        programMenu = new ProgramMenu();
        //<editor-fold defaultstate="collapsed" desc="Program menu action listeners">
        //Set up the program menu action listeners
        programMenu.addAboutAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                showAboutWindow();
            }
        });
        
        programMenu.addExitAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                closeWindow();
            }
        });
        
        programMenu.addNewAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                createNewDocument();
            }
        });
        
        programMenu.addCursorAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                enableChannelTool();
            }
        });
        
        programMenu.addChannelAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                enableCursorTool();
            }
        });
        
        programMenu.addSaveAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveDocument();
            }
        });
        
        programMenu.addOpenAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                openExistingDocument();
            }
        });
        
        programMenu.addGenerateCodeAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                generateCode();
            }
        });
        
        programMenu.addImportsAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                showImportSelector();
            }
        });
        //</editor-fold>
        
        this.setJMenuBar(programMenu);
        
        //Add the shiny shiny buttons
        toolBar = new ToolBar();
        
        //<editor-fold defaultstate="collapsed" desc="Tool bar action listeners">
        toolBar.addNewAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                createNewDocument();
            }
        });
        
        toolBar.addCursorAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                enableChannelTool();
            }
        });
        
        toolBar.addChannelAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                enableCursorTool();
            }
        });
        
        toolBar.addCompileAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                generateCode();
            }
        });
        //</editor-fold>
        
        this.add(toolBar, BorderLayout.NORTH);
        
        //Let there be light!
        this.setVisible(true);
        
        //Fill the window
        JFrameUtils.maxamiseWindow(this);
    }
    
    private void saveDocument(){
        if(!DocumentSaver.saveDocumentTo(document.getLocation().getAbsolutePath(), document))
            JOptionPane.showMessageDialog(this, "Unable to save", "Oops", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(this,
                    "Saved to:\n"+document.getLocation().getAbsolutePath(),
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JComponent importPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        importList = new ImportList();
        
        //Load up and add the imports
        for(Import imp : document.getImports()){
            importList.addImport(imp);
        }
        
        JScrollPane scroll = new JScrollPane(importList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scroll.setPreferredSize(new Dimension(150, 25));
        panel.add(scroll, BorderLayout.CENTER);
        
        panel.add(new ProcInfo(document.getRootProcess()), BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the JTree and adds some dummy test data to it. This will be
     * updated once the various generator classes have been created
     * @return 
     */
    private JComponent itemsPanel() {
        //Create a branch for language blocks (PAR, SEQ and the like)
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Language");

        //Grab all the avalable blocks and add them to the list
        CodeBlockLibrary cbl = CodeBlockLibrary.getInstance();
        for (CodeBlock cb : cbl.getBlocks()) {
            root.add(new DefaultMutableTreeNode(cb));
        }

        //Create and set up the tree
        tree = new JTree(root);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        //set up dnd
        tree.setDragEnabled(true);
        //Create the TransferHandler that allows us to drag and drop processes
        //onto the Canvas (or wherever)
        tree.setTransferHandler(new TransferHandler() {

            @Override
            protected Transferable createTransferable(JComponent c) {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
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

        JScrollPane panel = new JScrollPane(tree);

        panel.setPreferredSize(new Dimension(150, 25));

        return panel;
    }
    
    /**
     * Enables The channel tool and disables the buttons for it
     */
    public void enableChannelTool(){
        toolBar.enableCursorButton();
        programMenu.enableCursorMenu();
        rootPROCCanvas.setSelectionModeEnabled(true);
    }
    
    /**
     * Enables The cursor tool and disables the buttons for it
     */
    public void enableCursorTool(){
        toolBar.enableChannelButton();
        programMenu.enableChannelMenu();
        rootPROCCanvas.setSelectionModeEnabled(false);
    }
    
    public void showAboutWindow(){
        AboutInformation ai = new AboutInformation(this);
        JFrameUtils.centerWindow(this, ai);
        ai.setVisible(true);
    }
    
    private void createNewDocument() {
        occblocks.OccBlocks.createNewDoc(this);
    }
    
    private void openExistingDocument(){
        occblocks.OccBlocks.openExistingDoc(this);
    }
    
    public void closeWindow(){
        this.dispose();
    }
    
    public void showImportSelector(){
        ImportSelector is = new ImportSelector(this, document);
        
        for(Import imp : document.getImports())
            importList.removeImport(imp);
        
        is.setVisible(true);
        
        for(Import imp : document.getImports())
            importList.addImport(imp);
    }
    
    public void generateCode(){
        CodeGen cg = new CodeGen();
        cg.generateCode(document);
        
        String writeLocation = document.getLocation().getAbsolutePath();
        String name = document.getLocation().getName();
        writeLocation = writeLocation.substring(0,
                writeLocation.length()-name.length());
        
        name = name.substring(0, name.lastIndexOf("."))+".occ";
        
        System.out.println("Saving file to: "+writeLocation+name);
        try {
            TextFileWriter.writeFile(writeLocation+name, cg.getContent());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Unable to save file", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "File saved to: \n"+writeLocation+name, "", JOptionPane.INFORMATION_MESSAGE);
        
        System.out.println("File Content:");
        System.out.println(cg.getContent());
    }
}
