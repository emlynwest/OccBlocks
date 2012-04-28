/*
 * PAR.java
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
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import occblocks.codegen.CodeContainer;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;

/**
 * Defines a PAR block. This is almost identical to SEQ except that the children
 * cannot be rearranged (as there's no point, they all run at the same time
 * anyway)
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-20
 */
public class PAR extends CodeBlock {

    private ArrayList<CodeBlock> children;

    /**
     * Creates a new PAR code block
     */
    public PAR() {
        children = new ArrayList<CodeBlock>();
        setHeight(150);
        setWidth(150);
    }

    @Override
    public CodeBlock clone() {
        PAR newPAR = new PAR();

//        for (CodeBlock cb : children) {
//            newPAR.addChild(cb.clone());
//        }

        return newPAR;
    }

    @Override
    public String toString() {
        return "PAR";
    }

    @Override
    public void draw(Graphics g) {
        String name = "PAR";

        Polygon procOutline = new Polygon();
        procOutline.addPoint(getX(), getY());
        procOutline.addPoint(getX(), getHeight()+getY());
        procOutline.addPoint(getWidth()+getX(), getHeight()+getY());
        procOutline.addPoint(getWidth()+getX(), getY()+g.getFontMetrics().getHeight());
        procOutline.addPoint(getX()+g.getFontMetrics().stringWidth(name)+10, getY()+g.getFontMetrics().getHeight());
        procOutline.addPoint(getX()+g.getFontMetrics().stringWidth(name)+10, getY());

        g.setColor(new Color(179, 250, 255));
        g.fillPolygon(procOutline);

        if(isSelected())
            g.setColor(Color.BLACK);
        else
            g.setColor(new Color (201, 187, 0));

        g.drawPolygon(procOutline);
        
        g.setColor(Color.BLACK);
        g.drawString(name, getX()+5, getY()+g.getFontMetrics().getHeight());
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(gen.getIndentation(indentation));
        gen.append("PAR\n");

        indentation++;
        for (CodeContainer cc : children) {
            cc.generateCode(indentation, gen);
        }

        indentation--;
    }
    
    @Override
    public void pack(){
        int newH = 100, newW = 100;
        
        for (CodeBlock block : children) {
            //Work out the edges, plus a little extra for neatness
            int rightSide = block.getWidth() + block.getX() + 20;
            int bottomSide = block.getHeight() + block.getY() + 20;

            //Check if either the width or height need to be updated
            if (rightSide > newW)
                newW = rightSide - getX();

            if (bottomSide > newH)
                newH = bottomSide - getY();
        }
        
        this.setHeight(newH);
        this.setWidth(newW);
    }
    
    @Override
    public boolean addContent(CodeBlock toAdd, Canvas canvas){
        toAdd.setParent(this);
        children.add(toAdd);
        
        pack();
        return true;
    }
    
    @Override
    public boolean removeContent(CodeBlock content, Canvas canvas){
        content.setParent(null);
        children.remove(content);
        canvas.removeBlock(content);
        
        return true;
    }
    
    @Override
    public void addedToCanvas(Canvas canvas){
        for(CodeBlock child : children)
            canvas.addBlock(child);
    }
    
    @Override
    public void setX(int newX){
        super.setX(newX);
        for(CodeBlock child : children){
            child.setX(newX+child.getXDif());
        }
    }
    
    @Override
    public void setY(int newY){
        super.setY(newY);
        for(CodeBlock child : children){
            child.setY(newY+child.getYDif());
        }
    }
    
    @Override
    public void removedFromCanvas(Canvas canvas){
        for(CodeBlock child : children){
            canvas.removeBlock(child);
        }
    }
}
