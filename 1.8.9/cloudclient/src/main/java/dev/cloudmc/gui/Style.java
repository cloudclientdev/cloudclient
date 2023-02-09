/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui;

import java.awt.*;

public class Style {

    private static boolean darkMode = false;
    private static boolean snapping = true;

    /**
     * Returns a color with the given transparency depending on if dark mode is active
     *
     * @param transparency The transparency the returned color should have
     * @return The black or white color which is returned
     */

    public static Color getColor(int transparency) {
        return isDarkMode() ?
                new Color(0, 0, 0, transparency) :
                new Color(255, 255, 255, transparency);
    }

    /**
     * Returns a color with the given transparency depending on if dark mode is active but reversed
     *
     * @param transparency The transparency the returned color should have
     * @return The black or white color which is returned
     */

    public static Color getReverseColor(int transparency) {
        return Style.isDarkMode() ?
                new Color(255, 255, 255, transparency) :
                new Color(0, 0, 0, transparency);
    }

    /**
     * Returns a boolean which says if the client should be in dark or light mode
     *
     * @return Boolean stating if client should be dark or light
     */
    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        Style.darkMode = darkMode;
    }

    public static boolean isSnapping() {
        return snapping;
    }

    public static void setSnapping(boolean snapping) {
        Style.snapping = snapping;
    }
}
