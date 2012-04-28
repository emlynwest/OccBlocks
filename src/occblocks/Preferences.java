/*
 * Preferences.java
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
package occblocks;

import java.util.HashMap;
import java.util.Set;

/**
 * Contains various settings for the program.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-11-08
 */
public class Preferences {
    
    private static Preferences instance = new Preferences();
    
    /**
     * @return A usable instance of the Preferences class
     */
    public static Preferences instance(){
        return instance;
    }
    
    //Everything below here is actual code, not dealing with making this class a singleton
    private HashMap<String, String> settings;
    
    /**
     * Creates a new Preferences object with a set of default settings
     */
    private Preferences(){
        settings = new HashMap<String, String>();
        //Set up a whole load of default prefs
        //The location of the OccamDoc generated XML file
        settings.put("occdoc_xml_location", "./resources/docs.xml");
        //The location of the block list
        settings.put("codeblock_list_location", "resources/blocklist");
        //Default icon size
        settings.put("icon_size", "32");
    }
    
    /**
     * Gets the value of the given preference
     * @param name The name of the preference to get
     * @return null if the name is unknown
     */
    public String getPreferenceValue(String name){
        return settings.get(name);
    }
    
    /**
     * Returns a list of all known properties
     * @return 
     */
    public Set<String> getPreferences(){
        return settings.keySet();
    }
}
