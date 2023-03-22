/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import com.google.common.collect.Ordering;
import dev.cloudmc.Cloud;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class PotionHud extends HudMod {

    private final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    private final Gui gui = new Gui();

    private final PotionEffect effect = new PotionEffect(MobEffects.STRENGTH);
    private final PotionEffect effect2 = new PotionEffect(MobEffects.REGENERATION);
    private final PotionEffect effect3 = new PotionEffect(MobEffects.NIGHT_VISION);

    public PotionHud(String name, int x, int y) {
        super(name, x, y);
        setW(120);
        setH(30);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, Style.getColor(50).getRGB(), 0);
                }
                drawPotion(effect.getPotion(), effect, 0, true, isTime());
                drawPotion(effect2.getPotion(), effect, 30, true, isTime());
                drawPotion(effect3.getPotion(), effect, 60, true, isTime());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                drawPotion(effect.getPotion(), effect, 0, false, isTime());
                drawPotion(effect2.getPotion(), effect, 30, false, isTime());
                drawPotion(effect3.getPotion(), effect, 60, false, isTime());
            }
            super.renderMod(mouseX, mouseY);
            setH(90);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        Collection<PotionEffect> collection = Cloud.INSTANCE.mc.player.getActivePotionEffects();
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground() && !collection.isEmpty()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                int addY = 0;
                for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection)) {
                    drawPotion(potioneffect.getPotion(), potioneffect, addY, true, isTime());
                    addY += 30;
                    setH(addY);
                }
            } else {
                if (isBackground() && !collection.isEmpty()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                int addY = 0;
                for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection)) {
                    drawPotion(potioneffect.getPotion(), potioneffect, addY, false, isTime());
                    addY += 30;
                    setH(addY);
                }
            }
        }
        GLHelper.endScale();
    }

    private void drawPotion(Potion potion, PotionEffect effect, int y, boolean modern, boolean time) {
        if (modern) {
            Cloud.INSTANCE.fontHelper.size20.drawString(I18n.format(potion.getName()), getX() + 30, time ? getY() + 5 + y : getY() + 10 + y, getColor());
            if (time) {
                Cloud.INSTANCE.fontHelper.size20.drawString(Potion.getPotionDurationString(effect, 1.0f), getX() + 30, getY() + 17 + y, getColor());
            }
        } else {
            Cloud.INSTANCE.mc.fontRenderer.drawString(I18n.format(potion.getName()), getX() + 30, time ? getY() + 5 + y : getY() + 10 + y, getColor());
            if (time) {
                Cloud.INSTANCE.mc.fontRenderer.drawString(Potion.getPotionDurationString(effect, 1.0f), getX() + 30, getY() + 17 + y, getColor());
            }
        }
        Cloud.INSTANCE.mc.getTextureManager().bindTexture(inventoryBackground);
        gui.drawTexturedModalRect(getX() + 5, getY() + 5 + y, potion.getStatusIconIndex() % 8 * 18, 198 + potion.getStatusIconIndex() / 8 * 18, 18, 18);
    }

    public int getColor() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isTime() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Show Time").isCheckToggled();
    }
}
