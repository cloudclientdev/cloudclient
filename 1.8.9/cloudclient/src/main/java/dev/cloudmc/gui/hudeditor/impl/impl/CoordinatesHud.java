/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CoordinatesHud extends HudMod {

    public CoordinatesHud(String name, int x, int y) {
        super(name, x, y);
        setW(120);
        setH(60);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, Style.getColor(50).getRGB(), 0);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString("X: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                Cloud.INSTANCE.fontHelper.size20.drawString("Y: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                Cloud.INSTANCE.fontHelper.size20.drawString("Z: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                Cloud.INSTANCE.fontHelper.size20.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                Cloud.INSTANCE.mc.fontRendererObj.drawString("X: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                Cloud.INSTANCE.mc.fontRendererObj.drawString("Y: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                Cloud.INSTANCE.mc.fontRendererObj.drawString("Z: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                Cloud.INSTANCE.mc.fontRendererObj.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString("X: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                Cloud.INSTANCE.fontHelper.size20.drawString("Y: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                Cloud.INSTANCE.fontHelper.size20.drawString("Z: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                Cloud.INSTANCE.fontHelper.size20.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                Cloud.INSTANCE.mc.fontRendererObj.drawString("X: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                Cloud.INSTANCE.mc.fontRendererObj.drawString("Y: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                Cloud.INSTANCE.mc.fontRendererObj.drawString("Z: " + MathHelper.round(Cloud.INSTANCE.mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                Cloud.INSTANCE.mc.fontRendererObj.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            }
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (isBiome()) {
            setW(120);
            setH(60);
        } else {
            setW(70);
            setH(45);
        }
    }

    private int getColor() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isBiome() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Biome").isCheckToggled();
    }

    private String getBiomeName() {
        return Cloud.INSTANCE.mc.theWorld.getBiomeGenForCoords(new BlockPos(Cloud.INSTANCE.mc.thePlayer.posX, Cloud.INSTANCE.mc.thePlayer.posY, Cloud.INSTANCE.mc.thePlayer.posZ)).biomeName;
    }
}
