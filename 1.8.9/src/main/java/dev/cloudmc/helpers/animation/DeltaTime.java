/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.helpers.animation;

public class DeltaTime {

    private static int deltaTime = 0;

    public static int getDeltaTime() {
        return deltaTime;
    }

    public static void setDeltaTime(int deltaTime) {
        DeltaTime.deltaTime = deltaTime;
    }
}
