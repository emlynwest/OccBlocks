/*
 * NOT.java
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
 * Defines a NOT statement
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class NOT extends CodeBlock {

    private CodeBlock bool;

    /**
     * Creates a new NOT statement with the given BOOL statement
     * @param bool 
     */
    public NOT(CodeBlock bool) {
        this.bool = bool;
    }

    @Override
    public CodeBlock clone() {
        return new NOT(getBool().clone());
    }

    @Override
    public String toString() {
        return "NOT " + getBool();
    }

    /**
     * @return the bool
     */
    public CodeBlock getBool() {
        return bool;
    }

    /**
     * @param bool the bool to set
     */
    public void setBool(CodeBlock bool) {
        this.bool = bool;
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append("NOT ");
        bool.generateCode(indentation, gen);
    }

    @Override
    public Type getType() {
        return Type.BOOL;
    }
}
