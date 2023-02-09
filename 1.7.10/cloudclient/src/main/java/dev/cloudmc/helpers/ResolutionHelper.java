package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.ScaledResolution;

public class ResolutionHelper {

    private static ScaledResolution scaledResolution;

    public static int getHeight() {
        scaledResolution = new ScaledResolution(Cloud.INSTANCE.mc, Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);
        return scaledResolution.getScaledHeight();
    }

    public static int getWidth() {
        scaledResolution = new ScaledResolution(Cloud.INSTANCE.mc, Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);
        return scaledResolution.getScaledWidth();
    }

    public static int getFactor() {
        scaledResolution = new ScaledResolution(Cloud.INSTANCE.mc, Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);
        return scaledResolution.getScaleFactor();
    }
}