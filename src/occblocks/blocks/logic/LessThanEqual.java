/*
 * LessThanEquals.java
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
package occblocks.blocks.logic;

import occblocks.blocks.CodeBlock;
import occblocks.blocks.type.Type;
import occblocks.codegen.CodeGen;

/**
 * Defines the <= (less than or equals) operator.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class LessThanEqual extends CodeBlock {

    private CodeBlock leftSide;
    private CodeBlock rightSide;

    /**
     * Creates a new less than or equal operator
     * @param leftSide
     * @param rightSide 
     */
    public LessThanEqual(CodeBlock leftSide, CodeBlock rightSide) {
        //TODO: add type checking
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public CodeBlock clone() {
        return new LessThanEqual(getLeftSide().clone(), getRightSide().clone());
    }

    @Override
    public String toString() {
        return leftSide + " <= " + rightSide;
    }

    /**
     * @return the leftSide
     */
    public CodeBlock getLeftSide() {
        return leftSide;
    }

    /**
     * @param leftSide the leftSide to set
     */
    public void setLeftSide(CodeBlock leftSide) {
        this.leftSide = leftSide;
    }

    /**
     * @return the rightSide
     */
    public CodeBlock getRightSide() {
        return rightSide;
    }

    /**
     * @param rightSide the rightSide to set
     */
    public void setRightSide(CodeBlock rightSide) {
        this.rightSide = rightSide;
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append("(");
        leftSide.generateCode(indentation, gen);
        gen.append(" <= ");
        rightSide.generateCode(indentation, gen);
        gen.append(")");
    }
    
    @Override
    public Type getType(){
        return Type.BOOL;
    }
}
