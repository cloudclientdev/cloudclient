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
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Collection;

public class PotionHud extends HudMod {

    private final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    private final Gui gui = new Gui();
    Potion potion = Potion.potionTypes[5];
    Potion potion2 = Potion.potionTypes[10];
    Potion potion3 = Potion.potionTypes[16];

    public PotionHud(String name, int x, int y) {
        super(name, x, y);
        setW(120);
        setH(30);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, ClientStyle.getBackgroundColor(50).getRGB(), 0);
                }
                drawPotion(potion, new PotionEffect(5, 1, 1), 0, true, isTime());
                drawPotion(potion2, new PotionEffect(10, 1, 1), 30, true, isTime());
                drawPotion(potion3, new PotionEffect(16, 1, 1), 60, true, isTime());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), ClientStyle.getBackgroundColor(50).getRGB());
                }
                drawPotion(potion, new PotionEffect(5, 1, 1), 0, false, isTime());
                drawPotion(potion2, new PotionEffect(10, 1, 1), 30, false, isTime());
                drawPotion(potion3, new PotionEffect(16, 1, 1), 60, false, isTime());
            }
            super.renderMod(mouseX, mouseY);
            setH(90);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text e) {
        Collection collection = Cloud.INSTANCE.mc.thePlayer.getActivePotionEffects();
        if(collection.isEmpty()){
            return;
        }
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                int addY = 0;
                for (Object obj : Cloud.INSTANCE.mc.thePlayer.getActivePotionEffects()) {
                    PotionEffect potioneffect = (PotionEffect) obj;
                    Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                    drawPotion(potion, potioneffect, addY, true, isTime());
                    addY += 30;
                    setH(addY);
                }
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                int addY = 0;
                for (Object obj : Cloud.INSTANCE.mc.thePlayer.getActivePotionEffects()) {
                    PotionEffect potioneffect = (PotionEffect) obj;
                    Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                    drawPotion(potion, potioneffect, addY, false, isTime());
                    addY += 30;
                    setH(addY);
                }
            }
        }
    }

    private void drawPotion(Potion potion, PotionEffect effect, int y, boolean modern, boolean time) {
        if (modern) {
            Cloud.INSTANCE.fontHelper.size20.drawString(I18n.format(potion.getName()), getX() + 30, time ? getY() + 5 + y : getY() + 10 + y, getColor());
            if (time) {
                Cloud.INSTANCE.fontHelper.size20.drawString(Potion.getDurationString(effect), getX() + 30, getY() + 17 + y, getColor());
            }
        } else {
            Cloud.INSTANCE.mc.fontRenderer.drawString(I18n.format(potion.getName()), getX() + 30, time ? getY() + 5 + y : getY() + 10 + y, getColor());
            if (time) {
                Cloud.INSTANCE.mc.fontRenderer.drawString(Potion.getDurationString(effect), getX() + 30, getY() + 17 + y, getColor());
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
