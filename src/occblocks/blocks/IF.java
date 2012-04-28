/*
 * IF.java
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

import java.util.ArrayList;
import occblocks.codegen.CodeGen;

/**
 * Defines an IF statement.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-11-09
 */
public class IF extends CodeBlock {

    private ArrayList<IFCondition> statements;

    /**
     * Constructs a new IF statement
     */
    public IF() {
        statements = new ArrayList<IFCondition>();
    }

    /**
     * Adds an if condition at the end of the list
     * @param ifc 
     */
    public void addStatement(IFCondition ifc) {
        statements.add(ifc);
    }

    /**
     * Removes the given IFCondition
     * @param ifc 
     */
    public void removeStatement(IFCondition ifc) {
        statements.remove(ifc);
    }

    /**
     * Inserts a condition at the given index
     * @param ifc
     * @param index 
     */
    public void insertStatement(IFCondition ifc, int index) {
        statements.add(index, ifc);
    }

    /**
     * @return The full list of IFConditions
     */
    public ArrayList<IFCondition> getConditions() {
        return statements;
    }

    @Override
    public CodeBlock clone() {
        IF newIF = new IF();

        for (IFCondition ifc : statements) {
            newIF.addStatement(ifc);
        }

        return newIF;
    }

    @Override
    public String toString() {
        return "IF";
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(gen.getIndentation(indentation));
        gen.append("IF\n");
        
        indentation++;
        for (IFCondition ifc : statements) {
            ifc.generateCode(indentation, gen);
        }
        indentation--;
        gen.append("\n");
    }
}
