/*
 * OR.java
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
 * Defines an OR statement
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class OR extends CodeBlock {

    private CodeBlock leftBranch;
    private CodeBlock rightBranch;

    public OR(CodeBlock leftBranch, CodeBlock rightBranch) {
        this.leftBranch = leftBranch;
        this.rightBranch = rightBranch;
    }

    @Override
    public CodeBlock clone() {
        return new OR(leftBranch.clone(), rightBranch.clone());
    }

    @Override
    public String toString() {
        return "(" + leftBranch.toString() + " OR " + rightBranch.toString() + ")";
    }

    /**
     * @return the leftBranch
     */
    public CodeBlock getLeftBranch() {
        return leftBranch;
    }

    /**
     * @param leftBranch the leftBranch to set
     */
    public void setLeftBranch(CodeBlock leftBranch) {
        this.leftBranch = leftBranch;
    }

    /**
     * @return the rightBranch
     */
    public CodeBlock getRightBranch() {
        return rightBranch;
    }

    /**
     * @param rightBranch the rightBranch to set
     */
    public void setRightBranch(CodeBlock rightBranch) {
        this.rightBranch = rightBranch;
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append("(");
        leftBranch.generateCode(indentation, gen);
        gen.append(" OR ");
        rightBranch.generateCode(indentation, gen);
        gen.append(")");
    }

    @Override
    public Type getType() {
        return Type.BOOL;
    }
}
