/*
 * ChannelEnd.java
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
import occblocks.blocks.PROCParam;
import occblocks.blocks.type.Type;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;

/**
 * Defines the end of a channel, this is to be able to perform type checking
 * when assigning channels to processes.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-12
 */
public class ChannelEnd extends PROCParam {

    private Type type;
    private EndType direction;
    private String name;
    //This defines if this channel end relates to a CHAN created with the PROC's
    //headder.
    private boolean isHeaderParam = false;
    private ChannelEnd connectedTo;

    /**
     * Constructs a new channel end.
     * @param type
     * @param direction 
     */
    public ChannelEnd(Type type, EndType direction, CodeBlock parent) {
        setParent(parent);
        this.type = type;
        this.direction = direction;
        setHeight(10);
        setWidth(10);
    }

    /**
     * @return the type
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * @return the direction
     */
    public EndType getDirection() {
        return direction;
    }
    
    public boolean isHeaderParam(){
        return isHeaderParam;
    }
    
    public void setHeaderParam(boolean newValue){
        isHeaderParam = newValue;
    }

    @Override
    public CodeBlock clone() {
        ChannelEnd newCE = new ChannelEnd(type, direction, null);
        newCE.setName(name);
        return newCE;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name + " " + EndType.convertToChar(direction);
    }

    @Override
    public void draw(Graphics g) {
        if(connectedTo != null){
            g.setColor(Color.BLUE);
        } else if (direction == EndType.READ) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }

        g.fillOval(getX(), getY(), getHeight(), getWidth());
        g.setColor(Color.BLACK);
        g.drawOval(getX(), getY(), getHeight(), getWidth());
        g.drawString(type.toString() + " " + EndType.convertToChar(direction) + " " + name, getX(), getY()-2);
    }

    /**
     * Checks to see if the given ChannelEnd is equal to this one
     * @param ce
     * @return 
     */
    public boolean equal(ChannelEnd ce) {
        return (typeMatch(ce) && directionMatch(ce));
    }
    
    /**
     * Check to see if the given ChannelEnd has the same type
     * @param ce
     * @return 
     */
    public boolean typeMatch(ChannelEnd ce){
	if(getType().equals(Type.GENERIC) || ce.getType().equals(Type.GENERIC))
	    return true;
	
        return getType().equals(ce.getType());
    }
    
    /**
     * Check to see if the given ChannelEnd has the same direction
     * @param ce
     * @return 
     */
    public boolean directionMatch(ChannelEnd ce){
        if(isHeaderParam && ce.isHeaderParam())
            return false; //Can't join two header channels
        
        return getDirection().equals(ce.getDirection());
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
     * @return The channel end that this one is connected to, or null if not connected
     */
    public ChannelEnd getConnectedTo() {
        return connectedTo;
    }

    /**
     * Joins this channel end with another.
     * @param connectTo the other channel end to connect, or null to remove
     */
    public void setConnectedTo(ChannelEnd connectTo) {
        this.connectedTo = connectTo;
    }
    
    @Override
    public void generateCode(int indentation, CodeGen gen){
        gen.append("CHAN "+type.getType()+" "+name+EndType.convertToChar(direction));
    }
    
    public String getParentName(){
        if(connectedTo == null || connectedTo.getParent() == null)
            return name;
        
        String name = this.name;
        
        if( connectedTo.getParent() instanceof CHAN)
            name = ((CHAN) connectedTo.getParent()).getName();
        else
            name = connectedTo.getName();
        
        return name+EndType.convertToChar(direction);
    }
    
    @Override
    public void removedFromCanvas(Canvas canvas){
        if(connectedTo != null)
            connectedTo.setConnectedTo(null);
        
        connectedTo = null;
    }
}