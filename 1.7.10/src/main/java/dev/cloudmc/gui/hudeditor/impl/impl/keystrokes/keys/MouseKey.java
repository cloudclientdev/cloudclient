/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl.keystrokes.keys;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.CpsHelper;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MouseKey {

    public void renderKey(int x, int y, int width, int height, boolean modern, int mouseButton, int color, int fontColor, boolean background, boolean cps) {
        boolean mouseDown;
        if(Cloud.INSTANCE.mc.currentScreen == null) {
            mouseDown = Mouse.isButtonDown(mouseButton);
        }
        else {
            mouseDown = false;
        }

        if (modern) {
            if (background) {
                Helper2D.drawRoundedRectangle(x, y, width, height, 2, color, 0);
            }

            if (mouseDown) {
                Helper2D.drawRoundedRectangle(
                        x,
                        y,
                        width,
                        height,
                        2,
                        0x70ffffff,
                        0
                );
            }

            Cloud.INSTANCE.fontHelper.size20.drawString(
                    getCPS(mouseButton) != 0 && cps ? getCPS(mouseButton) + " CPS" : mouseButton == 0 ? "LMB" : "RMB",
                    x - Cloud.INSTANCE.fontHelper.size20.getStringWidth(getCPS(mouseButton) != 0 && cps ? getCPS(mouseButton) + " CPS" : mouseButton == 0 ? "LMB" : "RMB") / 2 + width / 2,
                    y + height / 2 - 4,
                    fontColor
            );
        }
        else {
            if (background) {
                Helper2D.drawRectangle(x, y, width, height, color);
            }

            if (mouseDown) {
                Helper2D.drawRectangle(
                        x,
                        y,
                        width,
                        height,
                        0x70ffffff
                );
            }

            Cloud.INSTANCE.mc.fontRenderer.drawString(
                    getCPS(mouseButton) != 0 && cps ? getCPS(mouseButton) + " CPS" : mouseButton == 0 ? "LMB" : "RMB",
                    x - Minecraft.getMinecraft().fontRenderer.getStringWidth(getCPS(mouseButton) != 0 && cps ? getCPS(mouseButton) + " CPS" : mouseButton == 0 ? "LMB" : "RMB") / 2 + width / 2,
                    y + height / 2 - 4,
                    fontColor
            );
        }
    }

    private int getCPS(int mouseButton) {
        return Cloud.INSTANCE.cpsHelper.getCPS(mouseButton);
    }
}
