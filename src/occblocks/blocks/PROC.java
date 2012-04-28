/*
 * PROC.java
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
import java.util.ArrayList;
import occblocks.blocks.channel.CHAN;
import occblocks.blocks.channel.ChannelEnd;
import occblocks.codegen.CodeGen;
import occblocks.gui.Canvas;

/**
 * Defines a PROC structure. Type checking needs to be implemented here
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-20
 */
public class PROC extends CodeBlock {

    private String name;
    private int width;
    private int height;
    /**
     * Contains a list of channel ends or VARs that this PROC needs
     */
    private ArrayList<PROCParam> params;
    /**
     * Contains a list of assigned channels
     */
    private ArrayList<CHAN> chans;
    private ArrayList<VAR> vars;
    private CodeBlock content;

    public PROC(){
        this("unnamed");
    }
    
    public PROC(String name) {
        params = new ArrayList<PROCParam>();
        chans = new ArrayList<CHAN>();
        vars = new ArrayList<VAR>();
        this.name = name;
        setContent(new SKIP(), null);
    }

    /**
     * Adds a channel to this process, this does not assign it just defines
     * that one is needed. (eg PROC foo (CHAN INT someChan?) )
     * @param name
     * @param ce 
     */
    public void addPROCParam(PROCParam ce) {
        ce.setParent(this);
        //Set an inital location for this
        ce.setX(this.getX());
        ce.setY(this.getY());

        if(ce instanceof VAR){
            ((VAR) ce).setIsPROCParam(true);
        }

        params.add(ce);
    }

    public ArrayList<PROCParam> getPROCParams() {
        return params;
    }

    /**
     * Adds a channel to this PROC that is used somewhere inside
     * @param channel 
     */
    public void addChannel(CHAN channel){
        channel.setParent(this);
        chans.add(channel);
    }
    
    /**
     * Removes a channel that has been assigned to this PROC
     * @param channel 
     */
    public void removeChannel(CHAN channel){
        chans.remove(channel);
    }
    
    /**
     * Gets all the CHANs that have been assigned to this PROC
     * @return 
     */
    public ArrayList<CHAN> getChannelList(){
        return chans;
    }
    
    /**
     * Adds a VAR to this PROC that is used somewhere inside
     * @param channel 
     */
    public void addVAR(VAR var){
        var.setParent(this);
        vars.add(var);
    }
    
    /**
     * Removes a VAR that has been assigned to this PROC
     * @param var 
     */
    public void removeVAR(VAR var){
        vars.remove(var);
    }
    
    /**
     * Gets all the VARs that have been assigned to this PROC
     * @return 
     */
    public ArrayList<VAR> getVarList(){
        return vars;
    }
    
    @Override
    public void draw(Graphics g) {
        width = g.getFontMetrics().stringWidth(name) + 10;
        height = g.getFontMetrics().getHeight() + 2;
        
        g.setColor(Color.GREEN);
        for(PROCParam param : params){
            int paramX = param.getX() + (param.getWidth()/2);
            int paramY = param.getY() + (param.getHeight()/2);
            
            g.drawLine(getX(), getY(), paramX, paramY);
        }
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawString(name, getX() + 5, getY() + 12);
        
        g.setColor(Color.BLACK);
        g.drawRect(getX(), getY(), width, height);
    }
    
    @Override
    public void setX(int newX){
        boolean setChildren = true;
        if(getParent() != null && newX < getParent().getX())
            setChildren = false;
        
        super.setX(newX);
        
        if(setChildren){
            for(PROCParam param : params){
                param.setX(newX+param.getXDif());
            }
        }
    }
    
    @Override
    public void setY(int newY){
        boolean setChildren = true;
        if(getParent() != null && newY < getParent().getY())
            setChildren = false;
        
        super.setY(newY);
        
        if(setChildren){
            for(PROCParam param : params){
                param.setY(newY+param.getYDif());
            }
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public CodeBlock clone() {
        PROC newPROC = new PROC(getName());
        newPROC.setX(getX());
        newPROC.setY(getY());
//TODO: This needs updating
        for (PROCParam param : params) {
            PROCParam newParam = (PROCParam) param.clone();
            newParam.setParent(newPROC);
            newPROC.addPROCParam(newParam);
        }

        return newPROC;
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(gen.getIndentation(indentation));
        gen.append(name+" (");
        
        int count = 1;
        int total = params.size();
        for(PROCParam param : params){
            
            if(param instanceof ChannelEnd){
                ChannelEnd end = (ChannelEnd) param;
                gen.append(end.getParentName());
                
            } else if(param instanceof VAR){
                VAR var = (VAR) param;
                if(var.getInitialValue() != null)
                    gen.append(var.getInitialValue().getValue());
                else
                    gen.append(var.getName()); //This needs to be updated
            }
            
            if(count != total)
                gen.append(", ");
            count++;
        }
        
        gen.append(" )\n");
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
    @Override
    public boolean setContent(CodeBlock content, Canvas canvas) {
        if(content == this)
            throw new IllegalArgumentException("A CodeBlock cannot contain itself");
        
        //Remove the old content if there is one
        if(this.content != null)
            removeContent(this.content, canvas);
        
        content.setParent(this);
        this.content = content;
        
        return true;
    }
    
    @Override
    public boolean addContent(CodeBlock content, Canvas canvas) {
        return setContent(content, canvas);
    }
    
    @Override
    public void addedToCanvas(Canvas parent){
        //Add all the children
        for(PROCParam param : params){
            parent.addBlock(param);
        }
    }
    
    @Override
    public void removedFromCanvas(Canvas parent){
        for(PROCParam param : params){
            parent.removeBlock(param);
        }
    }
    
    @Override
    public boolean removeContent(CodeBlock toRemove, Canvas canvas){
        if(toRemove instanceof CHAN){
            chans.remove((CHAN)toRemove);
            return true;
        }
        
        content.setParent(null);
        canvas.removeBlock(toRemove);
        content = null;
        return true;
    }
    
    @Override
    public boolean withinLimits(CodeBlock child){
        if(child instanceof PROCParam)
            return true;
        else
            return super.withinLimits(child);
    }
}
