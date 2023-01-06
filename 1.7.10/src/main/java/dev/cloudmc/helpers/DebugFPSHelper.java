/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

public class DebugFPSHelper {

    private static int debugFPS = 0;

    public static int getDebugFPS() {
        return debugFPS;
    }

    public static void setDebugFPS(int debugFPS) {
        DebugFPSHelper.debugFPS = debugFPS;
    }
}
