/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl.keystrokes;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.gui.hudeditor.impl.impl.keystrokes.keys.KeyboardKey;
import dev.cloudmc.gui.hudeditor.impl.impl.keystrokes.keys.MouseKey;
import dev.cloudmc.helpers.GLHelper;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KeystrokesHud extends HudMod {

    KeyboardKey keyUp = new KeyboardKey();
    KeyboardKey keyDown = new KeyboardKey();
    KeyboardKey keyLeft = new KeyboardKey();
    KeyboardKey keyRight = new KeyboardKey();
    KeyboardKey keyJump = new KeyboardKey();
    MouseKey mouseKey0 = new MouseKey();
    MouseKey mouseKey1 = new MouseKey();

    public KeystrokesHud(String name, int x, int y) {
        super(name, x, y);
        setW(80);
        setH(80);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            keyUp.renderKey(getX() + 28, getY() + 2, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindForward, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground());
            keyDown.renderKey(getX() + 28, getY() + 28, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindBack, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground());
            keyLeft.renderKey(getX() + 2, getY() + 28, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindLeft, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground());
            keyRight.renderKey(getX() + getW() - 26, getY() + 28, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindRight, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground());
            keyJump.renderKey(getX() + 2, isClicks() ? getY() + 54 + 26 : getY() + 54, 76, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindJump, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground());
            if (isClicks()) {
                mouseKey0.renderKey(getX() + 2, getY() + 54, 38, 24, isModern(), 0, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground(), isCPS());
                mouseKey1.renderKey(getX() + 2 + 40, getY() + 54, 36, 24, isModern(), 1, ClientStyle.getBackgroundColor(50).getRGB(), getColor(), isBackground(), isCPS());
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            keyUp.renderKey(getX() + 28, getY() + 2, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindForward, 0x50000000, getColor(), isBackground());
            keyDown.renderKey(getX() + 28, getY() + 28, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindBack, 0x50000000, getColor(), isBackground());
            keyLeft.renderKey(getX() + 2, getY() + 28, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindLeft, 0x50000000, getColor(), isBackground());
            keyRight.renderKey(getX() + getW() - 26, getY() + 28, 24, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindRight, 0x50000000, getColor(), isBackground());
            keyJump.renderKey(getX() + 2, isClicks() ? getY() + 54 + 26 : getY() + 54, 76, 24, isModern(), Cloud.INSTANCE.mc.gameSettings.keyBindJump, 0x50000000, getColor(), isBackground());
            if (isClicks()) {
                mouseKey0.renderKey(getX() + 2, getY() + 54, 38, 24, isModern(), 0, 0x50000000, getColor(), isBackground(), isCPS());
                mouseKey1.renderKey(getX() + 2 + 40, getY() + 54, 36, 24, isModern(), 1, 0x50000000, getColor(), isBackground(), isCPS());
            }
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (isClicks()) {
            setH(106);
        }
        else {
            setH(80);
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

    private boolean isClicks() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Clicks").isCheckToggled();
    }

    private boolean isCPS() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "CPS").isCheckToggled();
    }
}