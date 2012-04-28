/*
 * WHILE.java
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

import java.awt.Color;
import java.awt.Graphics;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;

/**
 * Defines a WHILE block
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-17
 */
public class WHILE extends CodeBlock {

    private CodeBlock condition;
    private CodeBlock content;

    public WHILE(){
	this(new TRUE(), new SKIP());
    }
    
    /**
     * Creates a new WHILE with the given condition
     * @param ifc 
     */
    public WHILE(CodeBlock ifc, CodeBlock content) {
        this.condition = ifc;
	condition.setParent(this);
	this.content = content;
	content.setParent(this);
	this.setWidth(150);
	this.setHeight(150);
    }

    @Override
    public CodeBlock clone() {
        return new WHILE(condition.clone(), getContent().clone());
    }

    @Override
    public String toString() {
        return "WHILE";
    }

    @Override
    public void draw(Graphics g) {
	String name = "WHILE";
        
        //Calculate the width and height
	int initalWidth = g.getFontMetrics().stringWidth(name)+10+condition.getWidth();
	int contentWidth = content.getWidth()+10;
	if(initalWidth > contentWidth)
	    setWidth(initalWidth);
	else
	    setWidth(contentWidth);
	
        setHeight(g.getFontMetrics().getHeight()+10+content.getHeight());
	
        g.setColor(new Color(244, 201, 255));
	g.fillRect(getX(), getY(), getWidth(), getHeight());
	
	if(isSelected())
	    g.setColor(Color.BLACK);
	else
	    g.setColor(new Color(183, 151, 191));
	g.drawRect(getX(), getY(), getWidth(), getHeight());
	g.drawLine(getX(), g.getFontMetrics().getHeight()+3+getY(), getWidth()+getX(), g.getFontMetrics().getHeight()+3+getY());
	
	g.setColor(Color.BLACK);
        g.drawString(name, getX()+5, getY()+g.getFontMetrics().getHeight());
    }

    /**
     * @return the IFCondition for this WHILE
     */
    public CodeBlock getCondition() {
        return condition;
    }

    /**
     * @param ifc the new IFCondition for this WHILE
     */
    public void setCondition(CodeBlock ifc) {
        this.condition = ifc;
    }
    
    @Override
    public void addedToCanvas(Canvas addedTo){
	addedTo.addBlock(condition);
	addedTo.addBlock(content);
	
	setX(getX());
	setY(getY());
    }
    
    @Override
    public void removedFromCanvas(Canvas parent){
	parent.removeBlock(condition);
	parent.removeBlock(content);
    }
    
    @Override
    public void setX(int newX){
	super.setX(newX);
	condition.setX(newX+(getWidth()-condition.getWidth()));
	content.setX(newX+3);
    }
    
    @Override
    public void setY(int newY){
	super.setY(newY);
	condition.setY(newY);
	content.setY(newY+20);
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(gen.getIndentation(indentation));
        gen.append("WHILE ");
        condition.generateCode(indentation, gen);
        gen.append("\n");
        indentation++;
        content.generateCode(indentation, gen);
    }

    /**
     * @return the content
     */
    public CodeBlock getContent() {
	return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(CodeBlock content) {
	this.content = content;
    }
    
    @Override
    public boolean addContent(CodeBlock toAdd, Canvas canvas){
	canvas.removeBlock(content);
	setContent(toAdd);
        return true;
    }
    
    @Override
    public boolean setContent(CodeBlock newContent, Canvas canvas){
	canvas.removeBlock(content);
	setContent(newContent);
        return true;
    }
}
