/*
 * IFCondition.java
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

import occblocks.codegen.CodeGen;

/**
 * Defines a single condition for an IF statement. This includes the BOOl
 * statement as well as the CodeBlock. This needs type checking added
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class IFCondition extends CodeBlock {

    private CodeBlock condition;
    private CodeBlock cb;

    /**
     * Constructs a new IFCondition.
     * @param condition The BOOL statement that will act as a trigger
     * @param cb The CodeBlock that will be run if the condition is true
     */
    public IFCondition(CodeBlock condition, CodeBlock cb) {
        this.condition = condition;
        this.cb = cb;
    }

    @Override
    public CodeBlock clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "IF Condtition";
    }

    /**
     * @return the condition
     */
    public CodeBlock getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(CodeBlock condition) {
        this.condition = condition;
    }

    /**
     * @return The CodeBlock that will be run
     */
    public CodeBlock getCodeBlock() {
        return cb;
    }

    /**
     * @param cb the CodeBlock to use if the condition is true
     */
    public void setCodeBlock(CodeBlock cb) {
        this.cb = cb;
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        condition.generateCode(indentation, gen);
        gen.append("\n");

        indentation++;
        cb.generateCode(indentation, gen);
        indentation--;
    }
}
