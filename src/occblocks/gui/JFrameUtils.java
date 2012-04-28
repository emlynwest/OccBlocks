/*
 * JFrameUtils.java
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
package occblocks.gui;

import java.awt.*;
import javax.swing.*;

/**
 * Provides some commonly used utilities for use with Windows and JFrames
 * @author Steven West <sw349@kent.ac.uk>
 * @version 2009-10-27
 */
public class JFrameUtils {

    /**
     * Moves a JFrame into the center of the screen
     * @param window The JFrame to center
     */
    public static void centerWindow(Window window) {
        /**
         * Get the current screen resolution and window size.
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();
        /**
         * Create two new vars to store the window position.
         */
        int x = 0;
        int y = 0;
        /**
         * Workout the center of the window
         */
        double hWwindow = windowSize.getWidth() / 2;
        double hHwindow = windowSize.getHeight() / 2;
        /**
         * Workout the center of the user's screen.
         */
        double hWscreen = screenSize.getWidth() / 2;
        double hHscreen = screenSize.getHeight() / 2;
        /**
         * Take the width of the window from the screen width and
         * the height of the window from the screen height to work out
         * where the top left corner of the window should be.
         */
        x = (int) (hWscreen - hWwindow);
        y = (int) (hHscreen - hHwindow);
        /**
         * Set the window location that was decided above.
         */
        window.setLocation(x, y);
    }

    /**
     * Centers window2 over window1
     * @param window1
     * @param window2
     */
    public static void centerWindow(Window window1, Window window2) {
        /**
         * Get the current screen resolution and window size.
         */
        Dimension window1Size = window1.getSize();
        Dimension window2Size = window2.getSize();
        /**
         * Create two new vars to store the window position.
         */
        int x = 0;
        int y = 0;
        /**
         * Workout the center of the window2
         */
        double hWwindow = window2Size.getWidth() / 2;
        double hHwindow = window2Size.getHeight() / 2;
        /**
         * Workout the center of the window1
         */
        double hWscreen = window1Size.getWidth() / 2;
        double hHscreen = window1Size.getHeight() / 2;
        x = (int) (hWscreen - hWwindow);
        y = (int) (hHscreen - hHwindow);
        /**
         * then add the position so they overlap
         */
        x += window1.getX();
        y += window1.getY();
        /**
         * Set the window location that was decided above.
         */
        window2.setLocation(x, y);
    }

    /**
     * Attempts to maxamise the given JFrame
     * @param window
     */
    public static void maxamiseWindow(JFrame window) {
        //Check to see if the window can be maximised.
        if (java.awt.Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH)) {
            //Make the window maximized
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            System.out.println("Unable to maximise the window");
        }
    }

    /**
     * Attempts to load the system's look and feel
     */
    public static void loadDefaultLookAndFeel() {
        //Try and load the host OS's look
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            System.out.println("Unable to load native look," +
                    " using default swing");
            System.out.println(e.getMessage());
        }
    }
}
