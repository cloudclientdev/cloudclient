/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.helpers;

import java.io.File;

public class DirHelper {

    private static final String currentOS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() { return (currentOS.contains("windows")); }
    public static boolean isMac() { return (currentOS.contains("mac")); }
    public static boolean isLinux() { return (currentOS.contains("linux")); }

    /**
     * Returns the Location of the directory the minecraft folder is in on every operating system
     *
     * @return Directory
     */

    public static String getDirectory() {
        if (isWindows()) {
            return System.getenv("APPDATA") + File.separator;
        }
        else if (isLinux()) {
            return System.getProperty("user.home") + File.separator;
        }
        else if (isMac()) {
            return System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator;
        }
        else {
            return null;
        }
    }
}
