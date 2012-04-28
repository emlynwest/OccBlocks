/*
 * CodeGen.java
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
package occblocks.codegen;

import occblocks.blocks.Document;

/**
 * This is responsible for converting the object tree into a flat text file
 * that can then be compiled externally.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-12
 */
public class CodeGen {

    private char indentChar;
    /**
     * This is one "step" of indentation. It could 1 for a single tab character
     * or more than 1 for something like spaces.
     */
    private int indentAmmount;
    
    private StringBuilder stringBuilder;

    /**
     * Creates a new CodeGen with the default occam-pi formatting. Space for the
     * indentation character and 2 steps with each indentation.
     */
    public CodeGen() {
        this(' ', 2);
    }

    /**
     * Creates a new CodeGen with the given indentation character and each
     * step will indent by the given amount.
     * @param indentChar
     * @param indentAmmount
     */
    public CodeGen(char indentChar, int indentAmmount) {
        this.indentChar = indentChar;
        this.indentAmmount = indentAmmount;
        resetString();
    }

    /**
     * Resets the generated code contained in this CodeGen
     */
    public void resetString(){
        stringBuilder = new StringBuilder();
    }
    
    /**
     * Appends the given string to the generated code
     * @param toAdd 
     */
    public void append(String toAdd){
        stringBuilder.append(toAdd);
    }
    
    /**
     * Gets the string stored within this CodeGen
     */
    public String getContent(){
        return stringBuilder.toString();
    }
    
    /**
     * @return the indentChar
     */
    public char getIndentChar() {
        return indentChar;
    }

    /**
     * @return the indentAmmount
     */
    public int getIndentAmmount() {
        return indentAmmount;
    }

    /**
     * Returns the appropreate indentation string for the given indentation
     * level. This starts at 0, no indentation.
     * @param level
     * @return
     */
    public String getIndentation(int level) {
        String out = "";

        for (int i = 0; i < level*indentAmmount; i++) {
            out += indentChar;
        }

        return out;
    }

    /**
     * Returns the code generated from the given Document
     * @param doc
     * @return
     */
    public void generateCode(Document doc){
        doc.generateCode(0, this);
    }
}
