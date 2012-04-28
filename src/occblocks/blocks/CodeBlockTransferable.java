/*
 * CodeBlockTransferable.java
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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Creates a transferable container for CodeBlocks to enable java's DnD support
 * @see java.awt.datatransfer.Transferable
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-09-18
 */
public class CodeBlockTransferable implements Transferable{
    
    /**
     * Contains our custom data flavor
     */
    public static final DataFlavor dataFlavor =
            new DataFlavor(CodeBlock.class, "Code Block");
    
    /**
     * Contains the info to be transfered
     */
    private CodeBlock codeBlock;

    /**
     * Creates a new CodeBlockTransferable.
     * @param codeBlock 
     */
    public CodeBlockTransferable(CodeBlock codeBlock) {
        this.codeBlock = codeBlock;
    }
    
        
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {dataFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(dataFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        //Check to see if a valid data flavor has been asked for
        if(!isDataFlavorSupported(flavor)){
            throw new UnsupportedFlavorException(flavor);
        }
        
        return codeBlock;
    }
    
}
