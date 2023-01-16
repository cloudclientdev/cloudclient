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
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class BlockinfoHud extends HudMod {

    public BlockinfoHud(String name, int x, int y) {
        super(name, x, y);
        setW(130);
        setH(30);
    }

    private final RenderItem renderItem = new RenderItem();

    @Override
    public void renderMod(int mouseX, int mouseY) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, ClientStyle.getBackgroundColor(50).getRGB(), 0);
                }

                Cloud.INSTANCE.fontHelper.size20.drawString("Grass Block", getX() + 35, getY() + 10, getColor());
                renderItem(new ItemStack(Blocks.grass));
            }
            else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), ClientStyle.getBackgroundColor(50).getRGB());
                }

                Cloud.INSTANCE.mc.fontRendererObj.drawString("Grass Block", getX() + 35, getY() + 10, getColor());
                renderItem(new ItemStack(Blocks.grass));
            }
            super.renderMod(mouseX, mouseY);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {

        if (getLookingAtBlock() == null) {
            return;
        }

        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }

                Cloud.INSTANCE.fontHelper.size20.drawString(getBlockName(getLookingAtBlock()), getX() + 35, getY() + 10, getColor());
                renderItem(new ItemStack(getLookingAtBlock()));
            }
            else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }

                Cloud.INSTANCE.mc.fontRendererObj.drawString(getBlockName(getLookingAtBlock()), getX() + 35, getY() + 10, getColor());
                renderItem(new ItemStack(getLookingAtBlock()));
            }
        }
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

    private void renderItem(ItemStack stack) {
        GL11.glPushMatrix();

        GL11.glTranslatef(getX() + 3, getY() + 3, 1);
        GL11.glScalef(1.5f, 1.5f, 1);
        GL11.glTranslatef(-(getX() + 3), -(getY() + 3), -1);

        renderItem.renderItemAndEffectIntoGUI(
                Cloud.INSTANCE.mc.fontRendererObj,
                Cloud.INSTANCE.mc.getTextureManager(),
                stack,
                getX() + 3,
                getY() + 3
        );

        GL11.glPopMatrix();
    }

    private String getBlockName(Block block){
        String cleanName = block.getLocalizedName()
                .replace("tile.", "")
                .replace(".name", "");
        return cleanName.substring(0, 1).toUpperCase()
                + cleanName.substring(1);
    }

    private Block getLookingAtBlock() {
        MovingObjectPosition objectMouseOver = Cloud.INSTANCE.mc.objectMouseOver;
        if(objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
            return Cloud.INSTANCE.mc.theWorld.getBlock(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
        }
        return null;
    }
}