/*
 * STOP.java
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

import java.awt.Color;
import java.awt.Graphics;
import occblocks.codegen.CodeGen;

/**
 * Defines the STOP language construct. (that kills the poor program)
 * @author Steve "Uru" West <sw349@kent.ac.uk>
 * @version 2011-12-15
 */
public class STOP extends CodeBlock {

    @Override
    public CodeBlock clone() {
        return new STOP();
    }

    @Override
    public String toString() {
        return "STOP";
    }

    @Override
    public void draw(Graphics g) {
        String name = "STOP";

        //Calculate the width and height
        setWidth(g.getFontMetrics().stringWidth(name)+10);
        setHeight(g.getFontMetrics().getHeight()+3);

        g.setColor(Color.RED);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        if(isSelected())
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.PINK);

        g.drawRect(getX(), getY(), getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawString(name, getX()+5, getY()+g.getFontMetrics().getHeight());
    }

    @Override
    public void generateCode(int indentation, CodeGen gen) {
        gen.append(gen.getIndentation(indentation));
        gen.append("STOP\n");
    }
}
