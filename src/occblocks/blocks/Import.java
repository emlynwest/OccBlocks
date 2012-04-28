/*
 * Import.java
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
import java.util.List;
import occblocks.codegen.CodeGen;

/**
 * Defines an import that is used within a document
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @vrsion 2011-12-12
 */
public class Import extends CodeBlock {

    private String name;
    private ArrayList<PROC> procs;

    /**
     * Creates a new Import object
     * @param name eg, "course.module"
     */
    public Import(String name) {
        this.name = name;
        procs = new ArrayList<PROC>();
    }

    @Override
    public CodeBlock clone() {
        Import imp = new Import(name);
        
        for(PROC proc : procs){
            imp.addPROC((PROC) proc.clone());
        }
        
        return imp;
    }
    
    public void addPROC(PROC proc){
        procs.add(proc);
    }
    
    public void removePROC(PROC proc){
        procs.remove(proc);
    }
    
    public List<PROC> getPROCS(){
        return procs;
    }

    @Override
    public String toString() {
        return "Import "+name;
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

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append("#INCLUDE \"");
        gen.append(name);
        gen.append("\"\n");
    }
}
