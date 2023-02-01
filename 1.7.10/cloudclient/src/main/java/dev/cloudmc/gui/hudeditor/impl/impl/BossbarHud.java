/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.GLHelper;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.font.GlyphPageFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.boss.BossStatus;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class BossbarHud extends HudMod {

    public BossbarHud(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            renderBossHealthPlaceHolder(isModern());
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if(BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                renderBossHealth(isModern());
            }
        }
        GLHelper.endScale();
    }

    private void renderBossHealth(boolean modern) {
        BossStatus.statusBarTime--;
        int x = getX();
        int y = getY();
        int width = 182;
        int health = (int) (BossStatus.healthScale * (float) (width + 1));

        Cloud.INSTANCE.mc.getTextureManager().bindTexture(Gui.icons);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);

        if (health > 0) {
            Helper2D.drawTexturedModalRect(x, y + 10, 0, 79, health, 5);
        }

        String s = BossStatus.bossName;
        if(modern) {
            Cloud.INSTANCE.fontHelper.size20.drawString(s, (float) (x + width / 2 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(s) / 2), y, 16777215);
        } else {
            Cloud.INSTANCE.mc.fontRendererObj.drawString(s, x + width / 2 - Cloud.INSTANCE.mc.fontRendererObj.getStringWidth(s) / 2, y, 16777215);
        }

        setW(width);
        setH(15);
    }

    private void renderBossHealthPlaceHolder(boolean modern) {
        int x = getX();
        int y = getY();
        int width = 182;
        int health = 100;

        Cloud.INSTANCE.mc.getTextureManager().bindTexture(Gui.icons);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 79, health, 5);

        String s = "BossBar";
        if(modern) {
            Cloud.INSTANCE.fontHelper.size20.drawString(s, (float) (x + width / 2 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(s) / 2), y, 16777215);
        } else {
            Cloud.INSTANCE.mc.fontRendererObj.drawString(s, x + width / 2 - Cloud.INSTANCE.mc.fontRendererObj.getStringWidth(s) / 2, y, 16777215);
        }

        setW(width);
        setH(15);
    }

    private boolean isModern() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }
}
