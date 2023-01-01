/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.impl.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.CpsHelper;
import dev.cloudmc.helpers.Helper2D;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class CpsHud extends HudMod {

    CpsHelper cpsRight = new CpsHelper();
    CpsHelper cpsLeft = new CpsHelper();

    public CpsHud(String name, int x, int y) {
        super(name, x, y);
        setW(60);
        setH(20);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, ClientStyle.getBackgroundColor(50).getRGB(), 0);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2,
                        getY() + 6,
                        getColor()
                );
            }
            else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), ClientStyle.getBackgroundColor(50).getRGB());
                }
                Cloud.INSTANCE.mc.fontRenderer.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2 - Cloud.INSTANCE.mc.fontRenderer.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2,
                        getY() + 6,
                        getColor()
                );
            }
            super.renderMod(mouseX, mouseY);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text e) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2,
                        getY() + 6,
                        getColor()
                );
            }
            else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                Cloud.INSTANCE.mc.fontRenderer.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2 - Cloud.INSTANCE.mc.fontRenderer.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2,
                        getY() + 6,
                        getColor()
                );
            }
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

    private boolean isRightClick() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Right click").isCheckToggled();
    }

    private int getLeftCPS() {
        return cpsLeft.getCPS(0);
    }

    private int getRightCPS() {
        return cpsRight.getCPS(1);
    }
}
