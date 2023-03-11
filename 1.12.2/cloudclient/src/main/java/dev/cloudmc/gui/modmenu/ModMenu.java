/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.ResolutionHelper;
import dev.cloudmc.helpers.TimeHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ModMenu extends GuiScreen {

    private final Panel panel = new Panel();

    private final Animate animateModMenu = new Animate();
    private final Animate animateClock = new Animate();
    private final Animate animateSnapping = new Animate();

    public ModMenu() {
        animateModMenu.setEase(Easing.CUBIC_OUT).setMin(0).setSpeed(1000).setReversed(false);
        animateClock.setEase(Easing.CUBIC_OUT).setMin(0).setMax(50).setSpeed(100).setReversed(false);
        animateSnapping.setEase(Easing.CUBIC_IN).setMin(0).setMax(50).setSpeed(100).setReversed(false);
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
        boolean roundedCorners = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();
        int color = Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB();

        float max = ResolutionHelper.getHeight() / 2f + 150;
        animateModMenu.setMax(max).update();
        if(!animateModMenu.hasFinished()) {
            panel.setY(height - animateModMenu.getValueI());
        }
        panel.renderPanel(mouseX, mouseY);
        panel.updatePosition(mouseX, mouseY);

        /*
        Draws the time at the top right
         */

        animateClock.update();

        Helper2D.drawRoundedRectangle(width - 130, animateClock.getValueI() - 60, 140, 60, 10, Style.getColor(50).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(width - 50, 5 - 50 + animateClock.getValueI(), 40, 40, color, "icon/clock.png");

        Cloud.INSTANCE.fontHelper.size40.drawString(TimeHelper.getFormattedTimeMinute(), width - 120, 10 - 50 + animateClock.getValueI(), color);
        Cloud.INSTANCE.fontHelper.size20.drawString(TimeHelper.getFormattedDate(), width - 120, 30 - 50 + animateClock.getValueI(), color);

        /*
        Draws the dark and light mode button on the bottom left
         */

        animateSnapping.update();
        Helper2D.drawRoundedRectangle(10, height - 50, 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(15, height - 45, 30, 30, color, Style.isDarkMode() ? "icon/dark.png" : "icon/light.png");
        Helper2D.drawRoundedRectangle(60, height - 50 + animateSnapping.getValueI(), 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(65, height - 45 + animateSnapping.getValueI(), 30, 30, color, Style.isSnapping() ? "icon/grid.png" : "icon/nogrid.png");
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
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        panel.mouseClicked(mouseX, mouseY, mouseButton);
        if(mouseButton == 0) {
            if (MathHelper.withinBox(
                    panel.getX(), panel.getY(),
                    panel.getW(), panel.getH(),
                    mouseX, mouseY
            )) {
                panel.setDragging(true);
                panel.setOffsetX(mouseX - panel.getX());
                panel.setOffsetY(mouseY - panel.getY());
            }

            if (MathHelper.withinBox(10, height - 50, 40, 40, mouseX, mouseY)) {
                Style.setDarkMode(!Style.isDarkMode());
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        panel.setDragging(false);
        panel.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        panel.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Loads a shader to blur the screen when the gui is opened
     */

    @Override
    public void initGui() {
        panel.initGui();
        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        super.initGui();
    }

    /**
     * Deleted all shaderGroups in order to remove the screen blur when the gui is closed
     */

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        super.onGuiClosed();
    }
}
