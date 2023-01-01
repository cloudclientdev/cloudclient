/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.titlescreen;

public class PanoramaTimer {

    private static int panoramaTimer = 0;

    public static int getPanoramaTimer() {
        return panoramaTimer;
    }

    public static void setPanoramaTimer(int panoramaTimer) {
        PanoramaTimer.panoramaTimer = panoramaTimer;
    }
}
