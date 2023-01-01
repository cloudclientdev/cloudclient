/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.modmenu;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.helpers.TimeHelper;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ModMenu extends GuiScreen {

    Panel panel;

    public boolean animationFinish;

    Animate animateModMenu = new Animate();
    Animate animateClock = new Animate();

    public ModMenu() {
        panel = new Panel();
    }

    /**
     * Draws the panel of the modmenu used to toggle mods and change settings
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Helper2D.drawRectangle(0, 0, width, height, 0x70000000);
        ScaledResolution sr = new ScaledResolution(mc);

        animationFinish = animateModMenu.hasFinished();
        animateModMenu
                .setEase(Easing.CUBIC_OUT)
                .setMin(0).setMax(sr.getScaledHeight() / 1.2f)
                .setSpeed(1000)
                .setReversed(false)
                .update();

        /*
        Draws and updates the panel
         */

        if(!animationFinish)
            panel.setY(height - animateModMenu.getValueI());
        panel.renderPanel(mouseX, mouseY);
        panel.updatePosition(mouseX, mouseY);

        animateClock
                .setEase(Easing.CUBIC_OUT)
                .setMin(0)
                .setMax(50)
                .setSpeed(100)
                .setReversed(false)
                .update();

        /*
        Draws the time at the top right
         */

        Helper2D.drawRoundedRectangle(width - 130, -10 - 50 + animateClock.getValueI(), 140, 60, 10, ClientStyle.getBackgroundColor(50).getRGB(), ClientStyle.isRoundedCorners() ? 0 : -1);
        Helper2D.drawPicture(width - 50, 5 - 50 + animateClock.getValueI(), 40, 40, ClientStyle.getColor().getRGB(), "icon/clock.png");
        Cloud.INSTANCE.fontHelper.size40.drawString(TimeHelper.getFormattedTimeMinute(), width - 120, 10 - 50 + animateClock.getValueI(), ClientStyle.getColor().getRGB());
        Cloud.INSTANCE.fontHelper.size20.drawString(TimeHelper.getFormattedDate(), width - 120, 30 - 50 + animateClock.getValueI(), ClientStyle.getColor().getRGB());

        /*
        Draws the dark and light mode button on the bottom left
         */

        Helper2D.drawRoundedRectangle(10, height - 50, 40, 40, 2, ClientStyle.getBackgroundColor(40).getRGB(), ClientStyle.isRoundedCorners() ? 0 : -1);
        Helper2D.drawPicture(15, height - 45, 30, 30, ClientStyle.getColor().getRGB(), ClientStyle.isDarkMode() ? "icon/dark.png" : "icon/light.png");
    }

    /**
     * Sets different values of the panel when any mouse button is clicked
     * Changes the darkMode boolean if the button in the bottom left is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        panel.mouseClicked(mouseX, mouseY, mouseButton);
        if (MathHelper.withinBox(
                panel.getX(),
                panel.getY(),
                panel.getW(),
                panel.getH(),
                mouseX,
                mouseY
        ) && mouseButton == 0) {
            panel.setDragging(true);
            panel.setOffsetX(mouseX - panel.getX());
            panel.setOffsetY(mouseY - panel.getY());
        }

        if (MathHelper.withinBox(10, height - 50, 40, 40, mouseX, mouseY)) {
            ClientStyle.setDarkMode(!ClientStyle.isDarkMode());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        panel.setDragging(false);
        panel.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Loads a shader to blur the screen when the gui is opened
     */

    @Override
    public void initGui() {
        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }

    /**
     * Deleted all shaderGroups in order to remove the screen blur when the gui is closed
     */

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
}
