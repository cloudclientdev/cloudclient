/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.GLHelper;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.font.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DirectionHud extends HudMod {

    public DirectionHud(String name, int x, int y) {
        super(name, x, y);
        setW(200);
        setH(30);
    }

    private final String[] directionsFull = {"N", "W", "S", "E"};
    private final String[] directionsHalf = {"NW", "SW", "SE", "NE"};

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, ClientStyle.getBackgroundColor(50).getRGB(), 0);
                }
                FontHelper fontHelper = Cloud.INSTANCE.fontHelper;

                int value = (int) normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw);
                fontHelper.size20.drawString(String.valueOf(value), getX() + getW() / 2f + 1 - fontHelper.size20.getStringWidth(String.valueOf(value)) / 2f, getY() - 12, -1);
                Helper2D.drawPicture((int) (getX() + getW() / 2f - 4), getY() - 2, 8, 8, -1, "icon/triangle.png");

                GLHelper.startScissor(getX(), getY(), (int) (getW() * getSize()), (int) (getH() * getSize()));
                for(int i = 0; i < 4; i++) {
                    int xFull = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 86);
                    int xHalf = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 43);

                    fontHelper.size30.drawString("§l" + directionsFull[i], xFull, getY() + getH() / 2f - 5, getColor());
                    fontHelper.size20.drawString(directionsHalf[i], xHalf, getY() + getH() / 2f - 1, getColor());

                    for(int j = 0; j < 6; j++) {
                        Helper2D.drawRoundedRectangle((int) (xFull - 2 + j * 15 + fontHelper.size30.getStringWidth(directionsFull[i]) / 2f), getY() + 2, 2, j == 3 ? 6 : 4, 1, 0x80ffffff, 0);
                    }
                }
                GLHelper.endScissor();
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), ClientStyle.getBackgroundColor(50).getRGB());
                }
                FontRenderer fontHelper = Cloud.INSTANCE.mc.fontRendererObj;

                int value = (int) normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw);
                fontHelper.drawString(String.valueOf(value), (int) (getX() + getW() / 2f + 1 - fontHelper.getStringWidth(String.valueOf(value)) / 2f), getY() - 12, -1);
                Helper2D.drawPicture((int) (getX() + getW() / 2f - 4), getY() - 2, 8, 8, -1, "icon/triangle.png");

                GLHelper.startScissor(getX(), getY(), (int) (getW() * getSize()), (int) (getH() * getSize()));
                for(int i = 0; i < 4; i++) {
                    int xFull = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 83);
                    int xHalf = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 40);

                    GLHelper.startScale(xFull, getY() + 11, 1.5f);
                    fontHelper.drawString("§l" + directionsFull[i], xFull, (int) (getY() + getH() / 2f - 4), getColor());
                    GLHelper.endScale();

                    fontHelper.drawString(directionsHalf[i], xHalf, (int) (getY() + getH() / 2f - 1), getColor());

                    for(int j = 0; j < 6; j++) {
                        Helper2D.drawRectangle((int) (xFull - 2 + j * 15 + (fontHelper.getStringWidth(directionsFull[i]) / 2f) * 1.5f), getY() + 2, 2, j == 3 ? 6 : 4, 0x80ffffff);
                    }
                }
                GLHelper.endScissor();
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
                FontHelper fontHelper = Cloud.INSTANCE.fontHelper;

                int value = (int) normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw);
                fontHelper.size20.drawString(String.valueOf(value), getX() + getW() / 2f + 1 - fontHelper.size20.getStringWidth(String.valueOf(value)) / 2f, getY() - 12, -1);
                Helper2D.drawPicture((int) (getX() + getW() / 2f - 4), getY() - 2, 8, 8, -1, "icon/triangle.png");

                GLHelper.startScissor(getX(), getY(), (int) (getW() * getSize()), (int) (getH() * getSize()));
                for(int i = 0; i < 4; i++) {
                    int xFull = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 86);
                    int xHalf = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 43);

                    fontHelper.size30.drawString("§l" + directionsFull[i], xFull, getY() + getH() / 2f - 5, getColor());
                    fontHelper.size20.drawString(directionsHalf[i], xHalf, getY() + getH() / 2f - 1, getColor());

                    for(int j = 0; j < 6; j++) {
                        Helper2D.drawRoundedRectangle((int) (xFull - 2 + j * 15 + fontHelper.size30.getStringWidth(directionsFull[i]) / 2f), getY() + 2, 2, j == 3 ? 6 : 4, 1, 0x80ffffff, 0);
                    }
                }
                GLHelper.endScissor();
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                FontRenderer fontHelper = Cloud.INSTANCE.mc.fontRendererObj;

                int value = (int) normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw);
                fontHelper.drawString(String.valueOf(value), (int) (getX() + getW() / 2f + 1 - fontHelper.getStringWidth(String.valueOf(value)) / 2f), getY() - 12, -1);
                Helper2D.drawPicture((int) (getX() + getW() / 2f - 4), getY() - 2, 8, 8, -1, "icon/triangle.png");

                GLHelper.startScissor(getX(), getY(), (int) (getW() * getSize()), (int) (getH() * getSize()));
                for(int i = 0; i < 4; i++) {
                    int xFull = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 83);
                    int xHalf = (int) ((normalized(Cloud.INSTANCE.mc.thePlayer.rotationYaw) + i * 90) % 360 + getX() - 40);

                    GLHelper.startScale(xFull, getY() + 11, 1.5f);
                    fontHelper.drawString("§l" + directionsFull[i], xFull, (int) (getY() + getH() / 2f - 4), getColor());
                    GLHelper.endScale();

                    fontHelper.drawString(directionsHalf[i], xHalf, (int) (getY() + getH() / 2f - 1), getColor());

                    for(int j = 0; j < 6; j++) {
                        Helper2D.drawRectangle((int) (xFull - 2 + j * 15 + (fontHelper.getStringWidth(directionsFull[i]) / 2f) * 1.5f), getY() + 2, 2, j == 3 ? 6 : 4, 0x80ffffff);
                    }
                }
                GLHelper.endScissor();
            }
        }
        GLHelper.endScale();
    }

    public float normalized(float value) {
        float normalized = value % 360;
        if(normalized < 0) {
            normalized += 360;
        }
        return normalized;
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
}
