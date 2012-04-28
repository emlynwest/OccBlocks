/*
 * Document.java
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

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import occblocks.blocks.channel.CHAN;
import occblocks.codegen.CodeGen;

/**
 * Defines an whole occam-pi document. This will need to be updated to enable
 * "global" types/protocals and the like.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-03-09
 */
public class Document extends CodeBlock {

    private PROC rootProcess;
    private ArrayList<PROC> procs;
    private String name;
    private ArrayList<Import> imports;
    private File location;

    public Document(){
        this("unnamed", generateDefaultPROC("unnamed"));
    }
    
    /**
     * Generates a Document with a default process. This default process will
     * be named after the Document and will have
     * "CHAN BYTE screen!, err!, keyboard?" by default
     * @param name 
     */
    public Document(String name) {
        this(name, generateDefaultPROC(name));
    }

    /**
     * Constructs a document with the given name and initial root process
     * @param name
     * @param rootPROC  This can be null if a root process is not desired
     */
    public Document(String name, PROC rootPROC) {
        imports = new ArrayList<Import>();
        procs = new ArrayList<PROC>();
        this.name = name;
        rootProcess = rootPROC;
    }

    /**
     * Generates a new PROC with the given name with two CHAN BYTE ! and a
     * CHAN BYTE ? to be used as a program entry point
     * @param name
     * @return 
     */
    public static PROC generateDefaultPROC(String name) {
        //TODO: update this to include channels when PROC is finished
        PROC proc = new PROC(name);

        return proc;
    }

    /**
     * Adds a PROC to this Document
     * @param proc 
     */
    public void addPROC(PROC proc) {
        procs.add(proc);
    }

    /**
     * Removes a PROC from this Document
     * @param proc 
     */
    public void removePROC(PROC proc) {
        procs.remove(proc);
    }

    /**
     * Gets all the (non root) PROCs from this Document
     * @return 
     */
    public List<PROC> getPROCs() {
        return procs;
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
     * Adds an Import to this Document
     * @param imp 
     */
    public void addImport(Import imp) {
        imports.add(imp);
    }

    /**
     * Removes an Import from this Document
     * @param imp 
     */
    public void removeImport(Import imp) {
        imports.remove(imp);
    }

    /**
     * Gets the list of imports used
     * @return 
     */
    public List<Import> getImports() {
        return imports;
    }

    @Override
    public CodeBlock clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the rootProcess
     */
    public PROC getRootProcess() {
        return rootProcess;
    }

    /**
     * @param rootProcess the rootProcess to set
     */
    public void setRootProcess(PROC rootProcess) {
        this.rootProcess = rootProcess;
    }
    
    /**
     * Returns a file object that contains the path to where this Document is or
     * should be saved. There is no gaurentee that the file at the location will
     * exist.
     * @return A File object containing the file location
     */
    public File getLocation(){
        return location;
    }
    
    /**
     * Sets a new location for this Document
     * @param newLoc 
     */
    public void setLocation(File newLoc){
        location = newLoc;
    }
    
    /**
     * Sets a new location for this Document
     * @param newLoc 
     */
    public void setLocation(String newLoc){
        location = new File(newLoc);
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        //Load imports
        for(Import i : imports){
            i.generateCode(indentation, gen);
        }
        gen.append("\n");

        //TODO: Declare protocols (eventually, when they are implemented)
        //TODO: load up all child processes, checking for dependencies

        //Load up the main entry point for the program
        generateCodeForPROC(rootProcess, indentation, gen);
    }
    
    public void generateCodeForPROC(PROC proc, int indentation, CodeGen gen){
        gen.append("PROC ");
        gen.append(proc.getName());
        gen.append(" ( ");
        
        ArrayList<PROCParam> params = proc.getPROCParams();
        
        int count = 1;
        int total = params.size();
        for(PROCParam param : params){
            
            param.generateCode(indentation, gen);
            
            if(count != total)
                gen.append(", ");
            count++;
        }
        
        gen.append(" )\n");

        indentation++;
        //Generate channles
        for(CHAN chan : proc.getChannelList()){
            chan.generateCode(indentation, gen);
        }
        //Generate vars
        for(VAR var : proc.getVarList()){
            var.generateCode(indentation, gen);
        }
        
        //Add the child if it exists
        if(proc.getContent() != null)
            proc.getContent().generateCode(indentation, gen);

        //Finally the closing tag
        gen.append("\n:\n");
    }
}
