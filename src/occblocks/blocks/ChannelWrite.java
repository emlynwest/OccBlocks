/*
 * ChannelWrite.java
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
import occblocks.blocks.channel.ChannelEnd;
import occblocks.blocks.channel.EndType;
import occblocks.blocks.type.Type;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;

/**
 * Defines a ChannelWrite to channel statement (!). Needs type checking added
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-03-03
 */
public class ChannelWrite extends CodeBlock{

    private VAR var;
    private ChannelEnd chan;

    public ChannelWrite(){
        var = new VAR(Type.GENERIC, "VAR", null);
        var.setParent(this);
        var.setIsPROCParam(true);
        
        chan = new ChannelEnd(Type.GENERIC, EndType.WRITE, null);
        chan.setParent(this);
        chan.setName("");
    }

    @Override
    public CodeBlock clone() {
        return new ChannelWrite();
    }

    @Override
    public String toString() {
        return "Write";
    }

    @Override
    public void draw(Graphics g){
        String toDraw = "!";
        this.setHeight(g.getFontMetrics().getHeight()+5);
        this.setWidth(g.getFontMetrics().stringWidth(toDraw)+5);
        
        g.setColor(Color.GREEN);
        int x1 = getX(), y1 = getY();
        g.drawLine(x1, y1, var.getX()+(var.getWidth()/2), var.getY()+(var.getHeight()/2));
        g.drawLine(x1, y1, chan.getX()+(chan.getWidth()/2), chan.getY()+(chan.getHeight()/2));
        
        g.setColor(new Color(252, 255, 168));
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        
        if(isSelected())
            g.setColor(Color.BLACK);
        else
            g.setColor(new Color(64, 255, 236));
        
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        
        g.setColor(Color.BLACK);
        g.drawString(toDraw, getX()+2, getY()+g.getFontMetrics().getHeight());
    }
    
    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(gen.getIndentation(indentation));
        gen.append(chan.getParentName());
        gen.append(" ! ");
        gen.append(var.getInitialValue().getValue());
        gen.append("\n");
    }
    
    @Override
    public void setX(int newX){
        super.setX(newX);
        
        var.setX(newX+var.getXDif());
        chan.setX(newX+chan.getXDif());
    }
    
    @Override
    public void setY(int newY){
        super.setY(newY);
        
        var.setY(newY+var.getYDif());
        chan.setY(newY+chan.getYDif());
    }
    
    @Override
    public void addedToCanvas(Canvas canvas){
        var.setX(getX());
        var.setY(getY());
        
        chan.setX(getX());
        chan.setY(getY());
        
        canvas.addBlock(var);
        canvas.addBlock(chan);
    }
    
    @Override
    public void removedFromCanvas(Canvas canvas){
        canvas.removeBlock(var);
        canvas.removeBlock(chan);
    }
    
    @Override
    public boolean withinLimits(CodeBlock child){
        if(child instanceof PROCParam)
            return true;
        else
            return super.withinLimits(child);
    }
}
