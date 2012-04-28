/*
 * CodeBlock.java
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
package occblocks.blocks;

import java.awt.Graphics;
import java.io.Serializable;
import occblocks.blocks.type.TypeContainer;
import occblocks.blocks.type.Type;
import occblocks.codegen.CodeContainer;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;
import occblocks.gui.Drawable;

/**
 * Defines a common interface for blocks of code to allow them to be drawable
 * in the GUI and to be converted to text
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-20
 */
public abstract class CodeBlock implements Drawable, CodeContainer, TypeContainer, Serializable{

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isSelected=false;
    private CodeBlock parent;
    private int xDif = 0;
    private int yDif = 0;
    
    /**
     * This is defined in java.lang.Object but should be made a requirement 
     * for CodeBlocks
     * @return Returns an exact copy of this code block
     */
    @Override
    abstract public CodeBlock clone();
    
    /**
     * This is defined in java.lang.Object but should be made a requirement 
     * for CodeBlocks
     * @return Returns a string representation of this class
     */
    @Override
    abstract public String toString();

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
        if(parent != null){
            if(!parent.withinLimits(this))
                this.x = parent.getX()+xDif;
            
            xDif = getX() - getParent().getX();
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
        if(parent != null){
            if(!parent.withinLimits(this))
                this.y = parent.getY()+yDif;
            
            yDif = getY() - getParent().getY();
        }
    }
    
    /**
     * Gets the difference between the parent's X and this X
     * @return 
     */
    public int getXDif(){
        return xDif;
    }
    
    /**
     * Gets the difference between the parent's Y and this Y
     * @return 
     */
    public int getYDif(){
        return yDif;
    }

    @Override
    public int getWidth() {
        return width;
    }
    
    /**
     * Allows the object to set it's own width
     * @param width 
     */
    protected void setWidth(int width){
        this.width = width;
        if(parent != null)
            parent.pack();
    }

    @Override
    public int getHeight() {
        return height;
    }
    
    /**
     * Allows the object to set it's own height
     * @param width 
     */
    protected void setHeight(int height){
        this.height = height;
        if(parent != null)
            parent.pack();
    }

    /**
     * A default implementation of the getType() method that returns null (no type)
     * @return 
     */
    @Override
    public Type getType(){
        return null;
    }
    
    @Override
    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }
    
    @Override
    public boolean isSelected(){
        return isSelected;
    }

    /**
     * @return the parent
     */
    public CodeBlock getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(CodeBlock parent) {
        this.parent = parent;
        if(parent != null){
            xDif = getX() - getParent().getX();
            yDif = getY() - getParent().getY();
        }
    }
    
    /**
     * Removes the given CodeBlock from this CodeBlock. This should be implemented
     * by the subclass.
     * @param toRemove 
     */
    public boolean removeContent(CodeBlock toRemove, Canvas canvas){
        return true;
    }
    
    /**
     * Adds the given CodeBlock to this block. This should be implemented by the
     * subclass.
     * @param toAdd
     */
    public boolean addContent(CodeBlock toAdd, Canvas canvas){
        return true;
    }
    
    /**
     * Sets the content of this CodeBlock. This should be implemented by the
     * subclass.
     * @param newContent 
     */
    public boolean setContent(CodeBlock newContent, Canvas canvas){
        return true;
    }
    
    /**
     * Shrinks the size of the code block to fit its content
     */
    public void pack(){}
    
    @Override
    public void addedToCanvas(Canvas parent){}
    
    @Override
    public void removedFromCanvas(Canvas parent){}
    
    @Override
    public void generateCode(int indentation, CodeGen cg){}
    
    @Override
    public void draw(Graphics g){}
    
    public boolean withinLimits(CodeBlock child){
        if(child.getX() < this.getX() || child.getY() < this.getY())
            return false;
        
        return true;
    }
}
