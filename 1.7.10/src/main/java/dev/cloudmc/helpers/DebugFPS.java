/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.helpers;

public class DebugFPS {

    private static int debugFPS = 0;

    public static int getDebugFPS() {
        return debugFPS;
    }

    public static void setDebugFPS(int debugFPS) {
        DebugFPS.debugFPS = debugFPS;
    }
}
