/*
 * Constant.java
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
import occblocks.blocks.type.Type;
import occblocks.codegen.CodeGen;

/**
 * Defines a constant value, such as a BYTE or INT
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class Constant extends CodeBlock {

    private Type type;
    private String value;

    /**
     * Constructs a new constant
     * @param type
     * @param name
     * @param value 
     */
    public Constant(String value) {
        this.value = value;
    }
    
    public Constant(){
        value = "Constant";
    }

    @Override
    public CodeBlock clone() {
        return new Constant(getValue());
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void draw(Graphics g) {
        setHeight(g.getFontMetrics().getHeight()+4);
        setWidth(g.getFontMetrics().stringWidth(value)+4);
        
        g.setColor(new Color(116, 237, 193));
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        
        g.setColor(Color.BLACK);
        g.drawLine(getX(),
                getY()+(getHeight()/2),
                getParent().getX()+getParent().getWidth(),
                getY()+(getHeight()/2));
        
        if(isSelected())
            g.setColor(Color.BLACK);
        else
            g.setColor(new Color(255, 187, 0));
        
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        
        g.setColor(Color.BLACK);
        g.drawString(value, getX()+2, getY()+g.getFontMetrics().getHeight());
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(value);
    }
}
