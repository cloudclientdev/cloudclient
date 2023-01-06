/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl.keystrokes.keys;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyboardKey {

    private Animate animate = new Animate();

    public void renderKey(int x, int y, int width, int height, boolean modern, KeyBinding keyBinding, int color, int fontColor, boolean background) {
        boolean keyDown;
        if(Cloud.INSTANCE.mc.currentScreen == null) {
            keyDown = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
        else {
            keyDown = false;
        }

        animate
                .setEase(Easing.CUBIC_IN)
                .setMin(0)
                .setMax(12)
                .setSpeed(100)
                .setReversed(keyDown)
                .update();

        if (modern) {
            if (background) {
                Helper2D.drawRoundedRectangle(x, y, width, height, 2, color, 0);
            }

            if (animate.hasFinished() && !keyDown) {

            }
            else {
                Helper2D.drawRoundedRectangle(
                        x + animate.getValueI(),
                        y + animate.getValueI(),
                        width - animate.getValueI() * 2,
                        height - animate.getValueI() * 2,
                        2,
                        0x70ffffff,
                        0
                );
            }

            Cloud.INSTANCE.fontHelper.size20.drawString(
                    Keyboard.getKeyName(keyBinding.getKeyCode()),
                    x - Cloud.INSTANCE.fontHelper.size20.getStringWidth(Keyboard.getKeyName(keyBinding.getKeyCode())) / 2 + width / 2,
                    y + height / 2 - 4,
                    fontColor
            );
        }
        else {
            if (background) {
                Helper2D.drawRectangle(x, y, width, height, color);
            }

            if (animate.hasFinished() && !keyDown) {

            }
            else {
                Helper2D.drawRectangle(
                        x + animate.getValueI(),
                        y + animate.getValueI(),
                        width - animate.getValueI() * 2,
                        height - animate.getValueI() * 2,
                        0x70ffffff
                );
            }

            Cloud.INSTANCE.mc.fontRendererObj.drawString(
                    Keyboard.getKeyName(keyBinding.getKeyCode()),
                    x - Cloud.INSTANCE.fontHelper.size20.getStringWidth(Keyboard.getKeyName(keyBinding.getKeyCode())) / 2 + width / 2,
                    y + height / 2 - 4,
                    fontColor
            );
        }
    }
}