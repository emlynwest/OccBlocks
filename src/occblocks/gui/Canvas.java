/*
 * Canvas.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Polygon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.DropLocation;
import javax.swing.TransferHandler.TransferSupport;
import occblocks.blocks.*;
import occblocks.blocks.channel.CHAN;
import occblocks.blocks.channel.ChannelEnd;

/**
 * Defines an area that Drawables can be drawn onto and moved around.
 * I wonder if I should make this implement Drawable.
 * The idea is that this will be for displaying and editing one PROC
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-16
 */
public class Canvas extends JPanel {

    /**
     * Contains all the visible items
     */
    private ArrayList<CodeBlock> blocks;
    /**
     * Contains mouse coordinates. These are only updated while dragging
     */
    private int mouseX;
    private int mouseY;
    /**
     * Contains the object that is currently being dragged (or null if there
     * is not one)
     */
    private CodeBlock dragging;
    /**
     * Contains the XY offset of the dragging object's XY compared to the mouse.
     * This makes it possible to stop object from "jumping" when the mouse is
     * pressed down.
     */
    private int dragXOff;
    private int dragYOff;
    /**
     * Height and width of the canvas
     */
    private int canvasHeight;
    private int canvasWidth;
    private PROC proc;
    private PROCParam readEnd = null;
    private PROCParam writeEnd = null;
    private boolean selectionMode = true;
    private boolean drawn = false;
    private JFrame parent;

    /**
     * Constructs a new Canvas object for your use.
     */
    public Canvas(PROC proc, JFrame parent) {
	this.parent = parent;
	this.proc = proc;
	//Set an inital height and width. This is sure to chage
	canvasHeight = 500;
	canvasWidth = 500;

	//Construct the list of objects
	blocks = new ArrayList<CodeBlock>();

	//<editor-fold defaultstate="collapsed" desc="Mouse click events">
	//Set up a new mouse listener so we can find out what the user is doing
	//Using a MouseAdaptor because it means we can pick and choose which
	//methods to implement
	this.addMouseListener(new MouseAdapter() {

	    /**
	     * Mouse gets pressed down so find if an object was clicked,
	     * work out the offset in relation to the top left corner and then
	     * start dragging
	     */
	    @Override
	    public void mousePressed(MouseEvent e) {
		processClickEvent(e);
	    }

	    //Mouse is released so stop dragging
	    @Override
	    public void mouseReleased(MouseEvent e) {
		dragging = null;
	    }
	});
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Mouse motion code">
	//Make the canvas able to respond to moving mouse events, again using
	//an adaptor
	this.addMouseMotionListener(new MouseMotionAdapter() {

	    @Override
	    public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (!selectionMode && readEnd != null) {
		    repaint();
		}
	    }

	    //While the mouse button is down and moving
	    @Override
	    public void mouseDragged(MouseEvent e) {
		//Update the mouse location
		mouseX = e.getX();
		mouseY = e.getY();

		//If an item is being dragged
		if (dragging != null && selectionMode) {
		    //Work out the new location using the offset calculated
		    //when dragging started. This stops the object from jumping
		    //to the mouse's location and instead creates a more nautral
		    //feel. Try removing the "- dragXOff" and "- dragYOff" to
		    //see what I mean
		    int newx = mouseX - dragXOff;
		    int newy = mouseY - dragYOff;

		    //Stop the dragged item going off the top or left.
		    //Means I don't have to expand the canvas below 0.
		    if (newx < 0) {
			newx = 0;
		    }
		    if (newy < 0) {
			newy = 0;
		    }
		    //I know, it's lazy.

		    //Update the objects new location
		    dragging.setX(newx);
		    dragging.setY(newy);

		    //Work out if we need to enlarge the canvas
		    //Get the lower edge
		    if (dragging.getY() + dragging.getHeight() >= getCanvasHeight() - 10) {
			setCanvasSize(canvasHeight + dragging.getHeight(), canvasWidth);
		    }
		    //Do the same for the side
		    if (dragging.getX() + dragging.getWidth() >= getCanvasWidth() - 10) {
			setCanvasSize(canvasHeight + dragging.getHeight(), canvasWidth + dragging.getWidth());
		    }

		    //Finally redraw to show the new location
		    if (dragging.getParent() != null) {
			dragging.getParent().pack();
		    }
		    pack();
		    repaint();
		}
	    }
	});
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Transfer handler">
	//Set up a TransferHandler to allow CodeBlocks to be dropped onto
	//the canvas. This will have to be updated when we start editing the
	//object tree
	this.setTransferHandler(new TransferHandler() {

	    //inform the world what data flavor we are expecting
	    @Override
	    public boolean canImport(TransferSupport support) {
		for (DataFlavor df : support.getDataFlavors()) {
		    if (df.equals(CodeBlockTransferable.dataFlavor)) {
			return true;
		    }
		}
		return false;
	    }

	    //Now for the fun part. Importing the new CodeBlock
	    @Override
	    public boolean importData(TransferSupport support) {
		//Make a check to see if we are dealing with the right flavor
		if (!canImport(support)) {
		    return false;
		}
		Transferable t = support.getTransferable();
		return handleTransfer(t, support);
	    }
	});
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Key listener code">
	KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

	    @Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
		    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			deleteSelected();
		    }
		}

		return false;
	    }
	});
	//</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="Handle mouse click events">
    private void processClickEvent(MouseEvent e) {
	dragging = getObjectAt(e.getX(), e.getY());

	//Make sure the currently selected thing is no longer selected
	deselectAll();

	//If we are selecting then set up the location of the mouse in relation
	//to the dragged item
	if (dragging != null && selectionMode) {
	    if (e.getClickCount() >= 2 && dragging instanceof ValueContainer) {
		//Open a dialog
		VarSelectDialog vd = new VarSelectDialog(parent, proc);
		if (vd.showDialog() == VarSelectDialog.RESULT_OK) {
		    ValueContainer vc = (ValueContainer) dragging;
		    Object result = vd.getEnteredValue();

		    if (result instanceof String) {
			vc.addConstant(new Constant((String) result), this);
		    } else if (result instanceof VAR) {
			vc.addVAR((VAR) result, this);
		    }
		}

	    } else {

		//Work out the offset
		dragXOff = e.getX() - dragging.getX();
		dragYOff = e.getY() - dragging.getY();
		dragging.setSelected(true);
	    }
	}
	if (!selectionMode && dragging instanceof ChannelEnd) {
	    //First assign the clicked item to the read end
	    if (readEnd == null) {
		readEnd = (ChannelEnd) dragging;
	    } else {
		//We already have a read end so assign a write end and add the new channel
		writeEnd = (ChannelEnd) dragging;
		dragging = null;

		//TODO: Add type checking here
		ChannelEnd rEnd = (ChannelEnd) readEnd;
		ChannelEnd wEnd = (ChannelEnd) writeEnd;

		//Make sure the directions match
		if (rEnd.directionMatch(wEnd) && rEnd.typeMatch(wEnd)) {
		    //Make sure that only one is a PROC
		    if (chansCanJoin(rEnd, wEnd)) {

			rEnd.setConnectedTo(wEnd);
			wEnd.setConnectedTo(rEnd);

		    }
		}

		readEnd = null;
		writeEnd = null;
	    }
	}
	this.repaint();
    }
    //</editor-fold>

    /**
     * Contains the logic for if two channel ends are allowed to be joined
     * @param c1
     * @param c2
     * @return True if they can be joined
     */
    private boolean chansCanJoin(ChannelEnd c1, ChannelEnd c2) {
	//This is starting to get messy
	if((c1.getParent() instanceof ChannelRead || c1.getParent() instanceof ChannelWrite) &&
		(!(c2.getParent() instanceof ChannelRead) && !(c2.getParent() instanceof ChannelWrite))
	)
	    return true;
	
	if((c2.getParent() instanceof ChannelRead || c2.getParent() instanceof ChannelWrite) &&
		(!(c1.getParent() instanceof ChannelRead) && !(c1.getParent() instanceof ChannelWrite))
	)
	    return true;
	
	//If the read end is a PROC but not the super proc, and the write is is not a proc
	if ((c1.getParent() instanceof PROC && c1.getParent() != proc) && !(c2.getParent() instanceof PROC))
		return true;

	if ((c2.getParent() instanceof PROC && c2.getParent() != proc) && !(c1.getParent() instanceof PROC))
	    return true;
	
	if (c1.getParent() instanceof PROC && c2.getParent() instanceof PROC && (c1.getParent() == proc || c2.getParent() == proc))
	    return true;
	
	return false;
    }

    //<editor-fold defaultstate="collapsed" desc="Transfer handler code">
    private boolean handleTransfer(Transferable t, TransferSupport support) {
	try {
	    //Grab the object and perform another test just to make sure
	    Object item = t.getTransferData(
		    CodeBlockTransferable.dataFlavor);

	    if (!(item instanceof CodeBlock)) {
		return false;
	    }

	    //Cast and clone
	    CodeBlock cb = (CodeBlock) item;
	    //Clone is used to stop the same CodeBlock being added
	    //over and over, that would just be silly
	    cb = cb.clone();

	    //get the XY and set the block's location
	    DropLocation loc = support.getDropLocation();
	    int x = loc.getDropPoint().x;
	    int y = loc.getDropPoint().y;
	    cb.setX(x);
	    cb.setY(y);

	    //Find out what we are dropping onto and set the content
	    CodeBlock newParent = getObjectAt(x, y);
	    if (cb instanceof CHAN) {

		NameTypeDialog ntd = new NameTypeDialog(parent, "Enter channel info");
		int result = ntd.showDialog();

		if (result == NameTypeDialog.RESULT_CANCEL
			|| ntd.getEnteredName() == null
			|| ntd.getEnteredType() == null) {
		    return true;
		}

		cb = new CHAN(ntd.getEnteredName(), ntd.getEnteredType());
		cb.setX(x);
		cb.setY(y);

		proc.addChannel((CHAN) cb);

	    } else if (cb instanceof VAR) {
		//It's a new var so ask for some info about it

		VAR var = (VAR) cb;

		NameTypeDialog ntd = new NameTypeDialog(parent, "Enter VAR info");
		int result = ntd.showDialog();

		if (result == NameTypeDialog.RESULT_CANCEL
			|| ntd.getEnteredName() == null
			|| ntd.getEnteredType() == null) {
		    return true;
		}

		var.setName(ntd.getEnteredName());
		var.setType(ntd.getEnteredType());

		proc.addVAR(var);

	    } else {
		//Nothing special so drop the content into the right place
		if (newParent == null) {
		    proc.setContent(cb, this);
		} else {
		    newParent.addContent(cb, this);
		}

	    }
	    //Finally add the block
	    addBlock(cb);

	} catch (Exception ex) {
	    //This might be updated to generate user messages about
	    //invalid drop locations
	    System.out.println(ex + ex.getMessage());
	    ex.printStackTrace();
	}

	return true;
    }
    //</editor-fold>

    /**
     * Deselectes any selected blocks
     */
    public void deselectAll() {
	for (Drawable draw : blocks) {
	    draw.setSelected(false);
	}
    }

    /**
     * Deletes any selected objects
     */
    public void deleteSelected() {
	for (CodeBlock block : blocks) {
	    if (block.isSelected()) {
		CodeBlock blockParent = block.getParent();

		if (parent != null) {
		    blockParent.removeContent(block, this);
		    this.removeBlock(block);
		}

		break;
	    }
	}

	repaint();
    }

    /**
     * Returns the first object that can be found under the given x,y.
     * This might be made public later on
     * @param x 
     * @param y
     * @return null if there is no object at the given location
     */
    private CodeBlock getObjectAt(int x, int y) {
	//Loop through the list backwards so that things on top are selected
	//first
	for (int i = blocks.size() - 1; i >= 0; i--) {
	    CodeBlock d = blocks.get(i);
	    if (x >= d.getX() && y >= d.getY()
		    && x <= d.getX() + d.getWidth()
		    && y <= d.getY() + d.getHeight()) {
		return d;
	    }
	}

	return null;
    }

    /**
     * Pack! Calculates the minimum size needed to contain all the currently
     * added CodeBlocks then sets the Canvas to that size
     */
    public void pack() {
	//100, 100 just incase there's nothing there. Impossible to add to a 
	//0x0 canvas
	int newHeight = 100;
	int newWidth = 100;

	//Ensure everything is drawn so we can get accurate sizes
	//paint(this.getGraphics());

	for (Drawable d : blocks) {
	    //Work out the edges, plus a little extra for neatness
	    int rightSide = d.getWidth() + d.getX() + 20;
	    int bottomSide = d.getHeight() + d.getY() + 20;

	    //Check if either the width or height need to be updated
	    if (rightSide > newWidth) {
		newWidth += rightSide - newWidth;
	    }

	    if (bottomSide > newHeight) {
		newHeight += bottomSide - newHeight;
	    }
	}

	//Set the new height
	setCanvasSize(newHeight, newWidth);
    }

    /**
     * Adds a CodeBlock (well, any Drawable) to the canvas
     * @param toAdd 
     */
    public void addBlock(CodeBlock toAdd) {
	blocks.add(toAdd);
//        toAdd.setCanvas(this);
	toAdd.addedToCanvas(this);
	
	//This is a sneaky hack to get things to be the right size and lay out
	//correctly before they are actually drawn
	BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	toAdd.draw(image.getGraphics());
	
	pack();
	repaint();
    }

    /**
     * Removes the given drawable from the canvas
     * @param toRemove 
     */
    public void removeBlock(CodeBlock toRemove) {
	blocks.remove(toRemove);
	toRemove.removedFromCanvas(this);
	if (toRemove instanceof VAR) {
	    proc.removeVAR((VAR) toRemove);
	} else if (toRemove instanceof CHAN) {
	    proc.removeChannel((CHAN) toRemove);
	}
	pack();
	repaint();
    }

    /**
     * Set the new size of the Canvas
     * @param newHeight
     * @param newWidth 
     */
    protected void setCanvasSize(int newHeight, int newWidth) {
	canvasHeight = newHeight;
	canvasWidth = newWidth;

	this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
	this.setSize(new Dimension(canvasWidth, canvasHeight));
    }

    /**
     * 
     * @return The height of the Canvas in pixels
     */
    public int getCanvasHeight() {
	return canvasHeight;
    }

    /**
     * 
     * @return The width of the Canvas in pixels.
     */
    public int getCanvasWidth() {
	return canvasWidth;
    }

    @Override
    public void paint(Graphics g) {
	g.setFont(new Font("SansSerif", Font.PLAIN, 12));
	g.clearRect(0, 0, 3000, 3000);
	if (!drawn) {
	    //Finally add the process's channel ends so they can actually be used
	    int count = 0;
	    int xOffset = g.getFontMetrics().stringWidth(proc.getName()) + 25;
	    for (PROCParam param : proc.getPROCParams()) {
		param.setX((100 * count) + xOffset);
		param.setY(20);
		blocks.add(param);
		count++;
	    }

	    //Make the content of the PROC dragable
	    CodeBlock content = proc.getContent();
	    content.setX(20);
	    content.setY(20 + g.getFontMetrics().getHeight());
	    this.addBlock(content);

	    drawn = true;
	}

	//Create an inital background
	g.setColor(Color.WHITE);
	g.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());

	//Draw the main process.
	String name = proc.getName();

	Polygon procOutline = new Polygon();
	procOutline.addPoint(10, 10);
	procOutline.addPoint(10, getCanvasHeight() - 10);
	procOutline.addPoint(getCanvasWidth() - 10, getCanvasHeight() - 10);
	procOutline.addPoint(getCanvasWidth() - 10, 10 + g.getFontMetrics().getHeight());
	procOutline.addPoint(10 + g.getFontMetrics().stringWidth(name) + 10, 10 + g.getFontMetrics().getHeight());
	procOutline.addPoint(10 + g.getFontMetrics().stringWidth(name) + 10, 10);

	g.setColor(new Color(255, 174, 92));
	g.fillPolygon(procOutline);
	g.setColor(new Color(164, 82, 0));
	g.drawPolygon(procOutline);

	g.setColor(Color.BLACK);
	g.drawString(name, 15, g.getFontMetrics().getHeight() + 7);

	g.setColor(Color.BLACK);

	ArrayList<CodeBlock> drawLater = new ArrayList<CodeBlock>();
	//Loop through all the known drawables and ask them to draw themselves
	for (CodeBlock d : blocks) {
	    if (d instanceof PROCParam) {
		drawLater.add(d);
	    } else {

		try {
		    d.draw(g);

		} catch (UnsupportedOperationException uoe) {
		    System.out.println(d + " failed to draw. It shall be removed.");
		    blocks.remove(d);
		}
	    }
	}

	//Loop through all the draw later things and draw the connecting lines
	for (CodeBlock param : drawLater) {
	    if (param instanceof ChannelEnd) {
		ChannelEnd ce = (ChannelEnd) param;

		if (ce.getConnectedTo() != null) {
		    g.setColor(Color.BLACK);
		    g.drawLine(ce.getX() + (ce.getWidth() / 2),
			    ce.getY() + (ce.getHeight() / 2),
			    ce.getConnectedTo().getX() + (ce.getConnectedTo().getWidth() / 2),
			    ce.getConnectedTo().getY() + (ce.getConnectedTo().getHeight() / 2));
		}
	    }
	}

	//Now loop though the things that need to be above the channel lines
	for (CodeBlock param : drawLater) {
	    try {
		param.draw(g);
	    } catch (Exception e) {
		System.out.println(e);
		blocks.remove(param);
	    }
	}

	if (!selectionMode && readEnd != null) {
	    int x1 = readEnd.getX() + (readEnd.getWidth() / 2);
	    int y1 = readEnd.getY() + (readEnd.getHeight() / 2);

	    g.drawLine(x1, y1, mouseX, mouseY);
	}
    }

    public void setSelectionModeEnabled(boolean selectionMode) {
	this.selectionMode = selectionMode;
	//Make sure nothing is selected
	readEnd = null;
	repaint();
    }
}
