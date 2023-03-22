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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorHud extends HudMod {

    public ArmorHud(String name, int x, int y) {
        super(name, x, y);
        setW(25);
        setH(70);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isBackground()) {
                if (isModern()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, Style.getColor(50).getRGB(), 0);
                } else {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
            }

            renderItem(new ItemStack(Items.DIAMOND_HELMET), getX() + 4, getY() + 2);
            renderItem(new ItemStack(Items.DIAMOND_CHESTPLATE), getX() + 4, getY() + 16 + 2);
            renderItem(new ItemStack(Items.DIAMOND_LEGGINGS), getX() + 4, getY() + 34 + 2);
            renderItem(new ItemStack(Items.DIAMOND_BOOTS), getX() + 4, getY() + 51 + 2);
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            boolean isEmpty = true;
            for (ItemStack stack : Cloud.INSTANCE.mc.player.inventory.armorInventory) {
                if (!stack.isEmpty()) {
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty || Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "No Armor Background").isCheckToggled()) {
                if (isBackground()) {
                    if (isModern()) {
                        Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                    } else {
                        Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                    }
                }

                renderItem(Cloud.INSTANCE.mc.player.inventory.armorInventory.get(3), getX() + 4, getY() + 2);
                renderItem(Cloud.INSTANCE.mc.player.inventory.armorInventory.get(2), getX() + 4, getY() + 16 + 2);
                renderItem(Cloud.INSTANCE.mc.player.inventory.armorInventory.get(1), getX() + 4, getY() + 34 + 2);
                renderItem(Cloud.INSTANCE.mc.player.inventory.armorInventory.get(0), getX() + 4, getY() + 51 + 2);
            }
        }
        GLHelper.endScale();
    }

    private void renderItem(ItemStack stack, int x, int y) {
        Cloud.INSTANCE.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
    }

    private boolean isModern() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Background").isCheckToggled();
    }
}