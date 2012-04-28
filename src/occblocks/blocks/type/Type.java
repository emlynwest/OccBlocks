/*
 * Type.java
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
package occblocks.blocks.type;

import java.io.Serializable;


/**
 * Defines a variable type within the occam-pi language
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2012-02-12
 */
public class Type implements Serializable{
    
    public static final Type INT = new Type("INT");
    public static final Type INT16 = new Type("INT16");
    public static final Type INT32 = new Type("INT32");
    public static final Type INT64 = new Type("INT64");
    public static final Type REAL32 = new Type("REAL32");
    public static final Type REAL64 = new Type("REAL64");
    public static final Type BYTE = new Type("BYTE");
    public static final Type BOOL = new Type("BOOL");
    public static final Type GENERIC = new Type("");
    
    private String type;

    public Type(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public Type clone(){
        return new Type(type);
    }
    
    public boolean equals(Type compare){
        return type.equals(compare.getType());
    }
    
    @Override
    public String toString(){
        return type;
    }
}
