/*
 * ImportRegister.java
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

import java.util.ArrayList;
import java.util.List;
import occblocks.Preferences;
import occblocks.blocks.Import;
import occblocks.io.OccdocXMLReader;

/**
 * This contains a list of all known imports and can convert to and from a format
 * that is more suitable for saving. This format is simply an "empty" import
 * with the name of the import it represents.
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-16
 */
public class ImportRegister {

    private ArrayList<Import> imports;

    private ImportRegister() {
        imports = new ArrayList<Import>();
        //Load up and add the imports
        OccdocXMLReader reader = new OccdocXMLReader();
        for (Import imp : reader.getDocumentContent(Preferences.instance().getPreferenceValue("occdoc_xml_location"))) {
            imports.add(imp);
        }
    }

    /**
     * Converts the given Import into an empty Import. That is, an Import that
     * only contains a name.
     * @param imp
     * @return Null if the given import is not contained in the list
     */
    public Import convertToEmpty(Import imp) {
        if (!imports.contains(imp)) {
            return null;
        }

        return new Import(imp.getName());
    }

    public Import convertToFull(Import convert) {
        for (Import imp : imports) {
            if (imp.getName().equals(convert.getName())) {
                return imp;
            }
        }

        return null;
    }

    public List<Import> getFullImportList() {
        return imports;
    }

    public static ImportRegister getInstance() {
        return ImportRegisterHolder.INSTANCE;
    }

    private static class ImportRegisterHolder {

        private static final ImportRegister INSTANCE = new ImportRegister();
    }
}
