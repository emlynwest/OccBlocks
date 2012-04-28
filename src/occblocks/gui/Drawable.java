/*
 * Drawable.java
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

import java.awt.Graphics;

/**
 * Defines common methods to allow objects to be drawn
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-10
 */
public interface Drawable {

    /**
     * Defines how the object is drawn on the Canvas
     * @param g 
     */
    public void draw(Graphics g);

    /**
     * 
     * @return the X coordinate of the top left corner
     */
    public int getX();

    /**
     * 
     * @return the Y coordinate of the top left corner
     */
    public int getY();

    /**
     * @param x The new X value of the top left corner
     */
    public void setX(int x);

    /**
     * 
     * @param y The new Y value of the top left corner
     */
    public void setY(int y);

    /**
     * This method returns the height of the object as it is rendered on screen
     * (in pixels). It is safe to assume that the value will not be correct
     * until the first time the object is rendered. (To allow for things like
     * font size calculation)
     * @return The total visual height of the object
     */
    public int getHeight();

    /**
     * This method returns the width of the object as it is rendered on screen
     * (in pixels). It is safe to assume that the value will not be correct
     * until the first time the object is rendered. (To allow for things like
     * font size calculation)
     * @return The total visual width of the object
     */
    public int getWidth();
    
    /**
     * This method gets called when the Drawable has been added to a canvas for
     * drawing.
     */
    public void addedToCanvas(Canvas parent);
    
    /**
     * This method gets called when the Drawable has been removed from a canvas
     */
    public void removedFromCanvas(Canvas parent);
    
    /**
     * Sets the selected status of this Drawable
     */
    public void setSelected(boolean isSelected);
    
    /**
     * Returns true if this Drawable is selected
     * @return 
     */
    public boolean isSelected();
    
//    /**
//     * Sets the canvas that this drawable object belongs to
//     * @param newCanvas 
//     */
//    public void setCanvas(Canvas newCanvas);
//    
//    /**
//     * Gets the canvas that this drawable is assigned to
//     * @return 
//     */
//    public Canvas getCanvas();
}
