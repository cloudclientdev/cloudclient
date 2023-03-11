/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BossbarHud extends HudMod {

    private final Gui gui = new Gui();
    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");

    public BossbarHud(String name, int x, int y) {
        super(name, x, y);
        setW(182);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            renderPlaceholder();
            setH(17);
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onBossBarRender(RenderGameOverlayEvent.Pre.BossInfo e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (!e.isCanceled() && e.isCancelable() && Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            e.setCanceled(true);

            if(!(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
                render(e.getY(), e.getBossInfo());
                setH(e.getY() + 5);
            }
        }
        GLHelper.endScale();
    }

    private void render(int yOffset, BossInfo info) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Cloud.INSTANCE.mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);

        int x = getX();
        int y = getY() + yOffset;
        int w = getW();

        gui.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);
        if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
            gui.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }

        int i = (int) (info.getPercent() * 183.0F);
        if (i > 0) {
            gui.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);

            if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
                gui.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
            }
        }

        String text = info.getName().getFormattedText();
        Cloud.INSTANCE.mc.fontRenderer.drawStringWithShadow(text, x + w / 2f - Cloud.INSTANCE.mc.fontRenderer.getStringWidth(text) / 2f, y - 9, -1);
    }

    private void renderPlaceholder() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Cloud.INSTANCE.mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);

        int x = getX();
        int y = getY() + 12;
        int w = getW();

        gui.drawTexturedModalRect(x, y, 0, 0, 182, 5);
        gui.drawTexturedModalRect(x, y, 0, 5, 100, 5);

        String text = "BossBar";
        Cloud.INSTANCE.mc.fontRenderer.drawStringWithShadow(text, x + w / 2f - Cloud.INSTANCE.mc.fontRenderer.getStringWidth(text) / 2f, y - 9, -1);
    }
}
