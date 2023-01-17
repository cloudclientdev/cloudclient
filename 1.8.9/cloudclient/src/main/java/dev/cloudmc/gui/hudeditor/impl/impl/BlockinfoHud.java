/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BlockinfoHud extends HudMod {

    public BlockinfoHud(String name, int x, int y) {
        super(name, x, y);
        setW(130);
        setH(30);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, ClientStyle.getBackgroundColor(50).getRGB(), 0);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString("Grass Block", getX() + 35, getY() + 10, getColor());

                ItemStack itemStack = new ItemStack(Blocks.grass);
                renderItem(itemStack);
            }
            else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), ClientStyle.getBackgroundColor(50).getRGB());
                }
                Cloud.INSTANCE.mc.fontRendererObj.drawString("Grass Block", getX() + 35, getY() + 10, getColor());

                ItemStack itemStack = new ItemStack(Blocks.grass);
                renderItem(itemStack);
            }
            super.renderMod(mouseX, mouseY);
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        IBlockState blockState = getLookingAtBlockState();
        if (blockState == null) {
            return;
        }

        if (Cloud.INSTANCE.modManager.getMod(getName()).isToggled() && !(Cloud.INSTANCE.mc.currentScreen instanceof HudEditor)) {

            World world = Cloud.INSTANCE.mc.theWorld;
            BlockPos blockPos = getLookingAtBlockPos();
            int meta = blockState.getBlock().getDamageValue(world, blockPos);
            int id = Item.itemRegistry.getIDForObject(blockState.getBlock().getItem(world, blockPos));
            ItemStack finalItem = new ItemStack(Item.getItemById(id), 1, meta);

            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Cloud.INSTANCE.fontHelper.size20.drawString(finalItem.getDisplayName(), getX() + 35, getY() + 10, getColor());
                renderItem(finalItem);
            }
            else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                Cloud.INSTANCE.mc.fontRendererObj.drawString(finalItem.getDisplayName(), getX() + 35, getY() + 10, getColor());
                renderItem(finalItem);
            }
        }
    }

    private BlockPos getLookingAtBlockPos() {
        MovingObjectPosition objectMouseOver = Cloud.INSTANCE.mc.objectMouseOver;
        if (objectMouseOver == null) {
            return null;
        }
        return objectMouseOver.getBlockPos();
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
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glTranslatef(getX() + 3, getY() + 3, 1);
        GL11.glScalef(1.5f, 1.5f, 1);
        GL11.glTranslatef(-(getX() + 3), -(getY() + 3), -1);
        Cloud.INSTANCE.mc.getRenderItem().renderItemAndEffectIntoGUI(
                stack, getX() + 3, getY() + 3
        );

        GL11.glPopMatrix();
    }

    private IBlockState getLookingAtBlockState() {
        if (Cloud.INSTANCE.mc.objectMouseOver != null && Cloud.INSTANCE.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && Cloud.INSTANCE.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos = Cloud.INSTANCE.mc.objectMouseOver.getBlockPos();
            return Cloud.INSTANCE.mc.theWorld.getBlockState(blockpos);
        }
        return null;
    }
}