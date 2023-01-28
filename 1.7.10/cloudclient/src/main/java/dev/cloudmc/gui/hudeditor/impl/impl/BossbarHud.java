/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.boss.BossStatus;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class BossbarHud extends HudMod {

    public BossbarHud(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        Helper2D.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if(BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                renderBossHealth();
            } else {
                Helper2D.endScale();
                return;
            }
            super.renderMod(mouseX, mouseY);
        }
        Helper2D.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        Helper2D.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if(BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                renderBossHealth();
            }
        }
        Helper2D.endScale();
    }

    private void renderBossHealth() {
        BossStatus.statusBarTime--;
        FontRenderer fontrenderer = Cloud.INSTANCE.mc.fontRendererObj;
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
        fontrenderer.drawStringWithShadow(s, x + width / 2 - fontrenderer.getStringWidth(s) / 2, y, 16777215);

        setW(width);
        setH(15);
    }
}
