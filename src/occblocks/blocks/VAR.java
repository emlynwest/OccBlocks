/*
 * VAR.java
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
import occblocks.gui.Canvas;

/**
 * Defines a VAR and also deals with initialisation (IS) if needed
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-13
 */
public class VAR extends PROCParam implements ValueContainer {

    private Type type;
    private String name;
    private Constant initialValue;
    private boolean isPROCParam;
    private boolean generateDef;

    /**
     * Constructs a new VAR with the given type, name and initial value
     * @param type
     * @param name
     * @param initalValue This can be null if an initial value is not wanted
     */
    public VAR(Type type, String name, Constant initialValue) {
        this.type = type;
        this.name = name;
        isPROCParam = false;
        setWidth(10);
        setHeight(10);
        setInitialValue(initialValue);
    }

    public VAR() {
        this(null, "VAR", null);
    }

    @Override
    public CodeBlock clone() {
        return new VAR(getType(), getName(), getInitialValue());
    }

    @Override
    public String toString() {
        String msg = "VAR";

        if (getType() != null) {
            msg += " "+getType();
        }
        
        if (getInitialValue() != null){
            msg += " "+getInitialValue().getValue();
        }

        return msg;
    }

    @Override
    public void draw(Graphics g) {

        if (isPROCParam()) {
            g.setColor(Color.YELLOW);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
            g.drawString(type.toString() + " " + name, getX(), getY() - 2);

        } else {

            String string = "VAR " + type.toString() + " " + name;

            setHeight(g.getFontMetrics().getHeight() + 3);
            setWidth(g.getFontMetrics().stringWidth(string) + 2);

            g.setColor(new Color(116, 237, 193));
            g.fillRect(getX(), getY(), getWidth(), getHeight());

            if (isSelected()) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(new Color(255, 187, 0));
            }

            g.drawRect(getX(), getY(), getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawString(string, getX() + 2, getY() + g.getFontMetrics().getHeight());
        }
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
     * @return the initialValue
     */
    public Constant getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue the initialValue to set
     */
    public void setInitialValue(Constant initialValue) {
//        if (initialValue != null) {
//            if (!initialValue.getType().equals(getType())) {
//                throw new IllegalArgumentException("Type mismatch. Expecting "
//                        + getType()
//                        + ", got "
//                        + initialValue.getType());
//            }
//        }

        this.initialValue = initialValue;
        if (this.initialValue != null) {
            this.initialValue.setX(getX() + getWidth() + 10);
            this.initialValue.setY(getY());
            this.initialValue.setParent(this);
        }
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        if(!isPROCParam){
            gen.append(gen.getIndentation(indentation));

            gen.append("VAL "+type.getType()+" ");
            
            gen.append(name);

            if (initialValue != null) {
                gen.append(" IS ");
                initialValue.generateCode(indentation, gen);
            }

            gen.append(":\n");
        } else {
            
            gen.append("VAL "+type.getType()+" "+name);
        }
    }

    @Override
    public void setX(int newX) {
        super.setX(newX);
        if (initialValue != null) {
            initialValue.setX(newX + getWidth() + 10);
        }
    }

    @Override
    public void setY(int newY) {
        super.setY(newY);
        if (initialValue != null) {
            initialValue.setY(newY);
        }
    }

    @Override
    public void setSelected(boolean status) {
        super.setSelected(status);
        if (initialValue != null) {
            initialValue.setSelected(status);
        }
    }

    /**
     * @return True if this VAR is being used as a parameter
     */
    public boolean isPROCParam() {
        return isPROCParam;
    }

    /**
     * @param isPROCParam Set to true if this VAR is being used as a parameter
     */
    public void setIsPROCParam(boolean isPROCParam) {
        this.isPROCParam = isPROCParam;
    }
    
    @Override
    public void removedFromCanvas(Canvas canvas){
        if(initialValue != null)
            canvas.removeBlock(initialValue);
    }
    
    @Override
    public void addedToCanvas(Canvas parent){
        if(initialValue != null)
            parent.addBlock(initialValue);
    }
    
    @Override
    public void addVAR(VAR toAdd, Canvas canvas){
        addConstant(new Constant(toAdd.getName()), canvas);
    }
    
    @Override
    public void addConstant(Constant toAdd, Canvas canvas){
        if(initialValue != null)
            canvas.removeBlock(initialValue);
        
        setInitialValue(toAdd);
        canvas.addBlock(initialValue);
    }
}
