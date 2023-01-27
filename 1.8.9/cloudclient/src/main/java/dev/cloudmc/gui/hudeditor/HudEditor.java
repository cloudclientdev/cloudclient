/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.gui.hudeditor.impl.impl.*;
import dev.cloudmc.gui.hudeditor.impl.impl.keystrokes.KeystrokesHud;
import dev.cloudmc.gui.modmenu.ModMenu;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class HudEditor extends GuiScreen {

    private final ArrayList<HudMod> hudModList;

    private Animate animateLogo = new Animate();
    private Animate animateSnapping = new Animate();
    private Animate animate = new Animate();

    private int counter;
    private int index;
    private final int offset;

    public HudEditor() {
        hudModList = new ArrayList<>();
        counter = 0;
        index = 10;
        offset = 10;
        init();
        animateLogo.setEase(Easing.CUBIC_OUT).setMin(0).setMax(70).setSpeed(100).setReversed(false);
        animateSnapping.setEase(Easing.CUBIC_OUT).setMin(0).setMax(50).setSpeed(100).setReversed(false);
        animate.setEase(Easing.LINEAR).setMin(0).setMax(25).setSpeed(200);
    }

    /**
     * Initialize every hud mod
     */

    public void init() {
        addHudMod(new SprintHud("ToggleSprint", index, offset));
        addHudMod(new SneakHud("ToggleSneak", index, offset));
        addHudMod(new FpsHud("FPS", index, offset));
        addHudMod(new KeystrokesHud("Keystrokes", index, offset));
        addHudMod(new ArmorHud("Armor Status", index, offset));
        addHudMod(new CoordinatesHud("Coordinates", index, offset));
        addHudMod(new ServerAddressHud("Server Address", index, offset));
        addHudMod(new PingHud("Ping", index, offset));
        addHudMod(new CpsHud("CPS", index, offset));
        addHudMod(new PotionHud("Potion Status", index, offset));
        addHudMod(new TimeHud("Time", index, offset));
        addHudMod(new SpeedIndicatorHud("Speed Indicator", index, offset));
        addHudMod(new BlockinfoHud("BlockInfo", index, offset));
        addHudMod(new ReachdisplayHud("ReachDisplay", index, offset));
        addHudMod(new DayCounterHud("Day Counter", index, offset));
    }

    /**
     * Draws the Screen with the button to show the modmenu and all the hudMods
     *
     * @param mouseX       The current X position of the mouse
     * @param mouseY       The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Cloud.INSTANCE.mc);
        Helper2D.drawRectangle(0, 0, width, height, 0x70000000);

        animateLogo.update();

        Helper2D.startScissor(0, height / 2 - 78, width, 73);
        Cloud.INSTANCE.fontHelper.size40.drawString(
                Cloud.modName,
                width / 2 - Cloud.INSTANCE.fontHelper.size40.getStringWidth(Cloud.modName) / 2,
                height / 2 + 36 - animateLogo.getValueI(),
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );
        Helper2D.drawPicture(
                width / 2 - 25,
                height / 2 - 8 - animateLogo.getValueI(),
                50, 50, ClientStyle.getBackgroundColor(70).getRGB(), "cloudlogo.png"
        );
        Helper2D.endScissor();

        animate.update();

        animate.update().setReversed(!MathHelper.withinBox(width / 2 - 50, height / 2 - 6, 100, 20, mouseX, mouseY));

        Helper2D.drawRoundedRectangle(
                width / 2 - 50,
                height / 2 - 6,
                100, 20, 2,
                ClientStyle.getBackgroundColor(animate.getValueI() + 30).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                "Open Mods",
                width / 2 - Cloud.INSTANCE.fontHelper.size20.getStringWidth("Open Mods") / 2,
                height / 2,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );

        for (HudMod hudMod : hudModList) {
            hudMod.renderMod(mouseX, mouseY);
            hudMod.updatePosition(mouseX, mouseY);
            if (hudMod.withinMod(mouseX, mouseY)) {
                int scroll = Mouse.getDWheel();
                if (scroll > 0 && hudMod.getSize() < 2) {
                    hudMod.setSize(hudMod.getSize() + 0.1f);
                } else if (scroll < 0 && hudMod.getSize() > 0.5f) {
                    hudMod.setSize(hudMod.getSize() - 0.1f);
                }
            }
            for (HudMod sHudMod : hudModList) {
                if (Cloud.INSTANCE.modManager.getMod(sHudMod.getName()).isToggled() && hudMod.isDragging() && !sHudMod.equals(hudMod)  && ClientStyle.isSnapping()) {
                    if (!sHudMod.equals(hudMod)) {
                        SnapPosition snap = new SnapPosition();
                        snap.setSnapping(true);
                        int snapRange = 5;
                        if (MathHelper.withinBoundsRange(hudMod.getX(), sHudMod.getX(), snapRange))
                            snap.setAll(sHudMod.getX(), sHudMod.getX(), false);
                        else if (MathHelper.withinBoundsRange(hudMod.getX() + hudMod.getW() * hudMod.getSize(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), snapRange))
                            snap.setAll(sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize() - hudMod.getW() * hudMod.getSize(), false);
                        else if (MathHelper.withinBoundsRange(hudMod.getX() + hudMod.getW() * hudMod.getSize(), sHudMod.getX(), snapRange))
                            snap.setAll(sHudMod.getX(), sHudMod.getX() - hudMod.getW() * hudMod.getSize(), false);
                        else if (MathHelper.withinBoundsRange(hudMod.getX(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), snapRange))
                            snap.setAll(sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), false);
                        else if (MathHelper.withinBoundsRange(hudMod.getY(), sHudMod.getY(), snapRange))
                            snap.setAll(sHudMod.getY(), sHudMod.getY(), true);
                        else if (MathHelper.withinBoundsRange(hudMod.getY() + hudMod.getH() * hudMod.getSize(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), snapRange))
                            snap.setAll(sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize() - hudMod.getH() * hudMod.getSize(), true);
                        else if (MathHelper.withinBoundsRange(hudMod.getY() + hudMod.getH() * hudMod.getSize(), sHudMod.getY(), snapRange))
                            snap.setAll(sHudMod.getY(), sHudMod.getY() - hudMod.getH() * hudMod.getSize(), true);
                        else if (MathHelper.withinBoundsRange(hudMod.getY(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), snapRange))
                            snap.setAll(sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), true);
                        else
                            snap.setSnapping(false);

                        if (snap.isSnapping()) {
                            if (!snap.isHorizontal()) {
                                Helper2D.drawRectangle((int) snap.getsPos(), 0, 1, sr.getScaledHeight(), 0x60ffffff);
                                hudMod.setX((int) snap.getPos());
                            } else {
                                Helper2D.drawRectangle(0, (int) snap.getsPos(), sr.getScaledWidth(), 1, 0x60ffffff);
                                hudMod.setY((int) snap.getPos());
                            }
                        }
                    }
                }
            }
        }

        Helper2D.drawRoundedRectangle(10, height - 50, 40, 40, 2,
                ClientStyle.getBackgroundColor(40).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1);
        Helper2D.drawPicture(15, height - 45, 30, 30, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB(),
                ClientStyle.isDarkMode() ? "icon/dark.png" : "icon/light.png");

        animateSnapping.update();

        Helper2D.drawRoundedRectangle(60, height - animateSnapping.getValueI(), 40, 40, 2,
                ClientStyle.getBackgroundColor(40).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1);
        Helper2D.drawPicture(65, height + 5 - animateSnapping.getValueI(), 30, 30, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB(),
                ClientStyle.isSnapping() ? "icon/grid.png" : "icon/nogrid.png");
    }

    /**
     * Sets the gui screen to the modmenu when the middle button is clicked
     * Toggles the Dark mode if the bottom left button is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (HudMod hudMod : hudModList) {
            if (hudMod.withinMod(mouseX, mouseY) && mouseButton == 0) {
                hudMod.setDragging(true);
                hudMod.setOffsetX(mouseX - hudMod.getX());
                hudMod.setOffsetY(mouseY - hudMod.getY());
            }
        }

        if (mouseButton == 0) {
            if (MathHelper.withinBox(width / 2 - 50, height / 2 - 6, 100, 20, mouseX, mouseY)) {
                mc.displayGuiScreen(new ModMenu());
            }

            if (MathHelper.withinBox(10, height - 50, 40, 40, mouseX, mouseY)) {
                ClientStyle.setDarkMode(!ClientStyle.isDarkMode());
            } else if (MathHelper.withinBox(60, height - 50, 40, 40, mouseX, mouseY)) {
                ClientStyle.setSnapping(!ClientStyle.isSnapping());
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (HudMod hudMod : hudModList) {
            hudMod.setDragging(false);
        }
    }

    /**
     * Loads a shader to blur the screen when the gui is opened
     */

    @Override
    public void initGui() {
        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        animateLogo.reset();
        animateSnapping.reset();
    }

    /**
     * Deletes all shaderGroups in order to remove the screen blur when the gui is closed
     */

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    /**
     * @return Returns an Arraylist of hud mods
     */

    public ArrayList<HudMod> getHudMods() {
        return hudModList;
    }

    /**
     * Adds a hudMod to the list
     *
     * @param hudMod The hudMod which should be added
     */

    public void addHudMod(HudMod hudMod) {
        if (counter % 5 == 0) {
            index = 10;
        }
        hudModList.add(hudMod);
        index += hudMod.getW() + offset;
        counter++;
    }

    /**
     * Returns a given hudMod using its name
     *
     * @param name The name of the hudMod
     * @return The returned hudMod
     */

    public HudMod getHudMod(String name) {
        for (HudMod hudMod : hudModList) {
            if (hudMod.getName().equalsIgnoreCase(name)) {
                return hudMod;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        if (Keyboard.isKeyDown(Cloud.INSTANCE.optionManager.getOptionByName("ModMenu Keybinding").getKey())) {
            Cloud.INSTANCE.mc.displayGuiScreen(Cloud.INSTANCE.hudEditor);
        }
    }
}
