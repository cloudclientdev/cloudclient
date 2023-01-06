/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.impl.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ArmorHud extends HudMod {

    public ArmorHud(String name, int x, int y) {
        super(name, x, y);
        setW(25);
        setH(70);
    }

    private final RenderItem renderItem = new RenderItem();

    @Override
    public void renderMod(int mouseX, int mouseY) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isBackground()) {
                if (isModern()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, ClientStyle.getBackgroundColor(50).getRGB(), 0);
                }
                else {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), ClientStyle.getBackgroundColor(50).getRGB());
                }
            }

            renderItem(new ItemStack(Items.diamond_helmet), getX() + 4, getY() + 2);
            renderItem(new ItemStack(Items.diamond_chestplate), getX() + 4, getY() + 16 + 2);
            renderItem(new ItemStack(Items.diamond_leggings), getX() + 4, getY() + 34 + 2);
            renderItem(new ItemStack(Items.diamond_boots), getX() + 4, getY() + 51 + 2);
            super.renderMod(mouseX, mouseY);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if (isBackground()) {
                if (isModern()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                else {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
            }

            renderItem(Cloud.INSTANCE.mc.thePlayer.inventory.armorInventory[3], getX() + 4, getY() + 2);
            renderItem(Cloud.INSTANCE.mc.thePlayer.inventory.armorInventory[2], getX() + 4, getY() + 16 + 2);
            renderItem(Cloud.INSTANCE.mc.thePlayer.inventory.armorInventory[1], getX() + 4, getY() + 34 + 2);
            renderItem(Cloud.INSTANCE.mc.thePlayer.inventory.armorInventory[0], getX() + 4, getY() + 51 + 2);
        }
    }

    private void renderItem(ItemStack stack, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(
                Cloud.INSTANCE.mc.fontRenderer,
                Cloud.INSTANCE.mc.getTextureManager(),
                stack,
                x,
                y
        );
        RenderHelper.disableStandardItemLighting();
    }

    private boolean isModern() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Background").isCheckToggled();
    }
}