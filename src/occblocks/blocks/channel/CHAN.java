/*
 * CHAN.java
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
package occblocks.blocks.channel;

import java.awt.Color;
import java.awt.Graphics;
import occblocks.blocks.CodeBlock;
import occblocks.blocks.type.Type;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;

/**
 * Defines a CHAN (and their use)
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-18
 */
public class CHAN extends CodeBlock {

    private String name;
    private ChannelEnd read;
    private ChannelEnd write;

    /**
     * Creates a new Channel with the given name and the given type
     * @param name
     * @param type 
     */
    public CHAN(String name, Type type) {
        this(name,
                new ChannelEnd(type, EndType.READ, null),
                new ChannelEnd(type, EndType.WRITE, null));
        
        read.setName("");
        write.setName("");
    }

    /**
     * Creates a Channel with the given name and the specified channel ends.
     * This is so the user can specify shared channel ends. An 
     * IllegalArgumentException will be thrown if the the types of read and 
     * write do not match.
     * @param name
     * @param read
     * @param write 
     */
    public CHAN(String name, ChannelEnd read, ChannelEnd write) {
        this.name = name;
        this.read = read;
        this.write = write;
        
        read.setParent(this);
        write.setParent(this);
        
        setHeight(20);
        setWidth(50);

        if(!read.typeMatch(write))
            throw new IllegalArgumentException("Types do not match");
        if (read.directionMatch(write))
            throw new IllegalArgumentException("Incompatible channel ends");
    }
    
    public CHAN(){
        this("", new Type("null"));
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the read
     */
    public ChannelEnd getRead() {
        return read;
    }

    /**
     * @return the write
     */
    public ChannelEnd getWrite() {
        return write;
    }

    @Override
    public CodeBlock clone() {
        return new CHAN(name,
                (ChannelEnd) read.clone(), (ChannelEnd) write.clone());
    }

    @Override
    public String toString() {
        return "CHAN " + name;
    }

    @Override
    public void draw(Graphics g) {
        
        String toDraw = read.getType().toString()+" "+name;
        this.setHeight(g.getFontMetrics().getHeight()+5);
        this.setWidth(g.getFontMetrics().stringWidth(toDraw)+5);
        
        g.setColor(Color.GREEN);
        int x1, x2, y1, y2;
        x1 = getRead().getX() + (getRead().getWidth() / 2);
        y1 = getRead().getY() + (getRead().getHeight() / 2);

        x2 = getWrite().getX() + (getWrite().getWidth() / 2);
        y2 = getWrite().getY() + (getWrite().getHeight() / 2);

        g.drawLine(x1, y1, getX(), getY());
        g.drawLine(x2, y2, getX(), getY());
        
        g.setColor(new Color(171, 255, 247));
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
        gen.append("CHAN ");
        gen.append(read.getType().getType()+" ");
        gen.append(name);
        gen.append(":\n");
    }
    
    @Override
    public void addedToCanvas(Canvas parent){
        parent.addBlock(read);
        parent.addBlock(write);
    }
    
    @Override
    public void removedFromCanvas(Canvas parent){
        parent.removeBlock(read);
        parent.removeBlock(write);
    }
    
    @Override
    public void setX(int newX){
        super.setX(newX);
        read.setX(newX+read.getXDif());
        write.setX(newX+write.getXDif());
    }
    
    @Override
    public void setY(int newY){
        super.setY(newY);
        read.setY(newY+read.getYDif());
        write.setY(newY+write.getYDif());
    }
    
    @Override
    public boolean withinLimits(CodeBlock block){
	return true;
    }
}
