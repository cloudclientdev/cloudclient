/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.helpers.Helper3D;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BlockOverlayMod extends Mod {

    public BlockOverlayMod() {
        super(
                "BlockOverlay",
                "Adds an customizable overlay to blocks."
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Outline", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Filling", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Thickness", this, 20, 3));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Outline Color", this, new Color(0, 0, 0)));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Filling Color", this, new Color(0, 0, 0)));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Alpha", this, 255, 100));
    }

    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
        e.setCanceled(true);
        drawSelectionBox(e.player, e.target, e.subID, e.partialTicks);
    }

    public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPosition, int execute, float partialTicks) {
        if (execute == 0 && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glLineWidth(getThickness());
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            float f1 = 0.002F;
            Block block = Cloud.INSTANCE.mc.theWorld.getBlock(
                    movingObjectPosition.blockX,
                    movingObjectPosition.blockY,
                    movingObjectPosition.blockZ
            );

            if (block.getMaterial() != Material.air) {
                block.setBlockBoundsBasedOnState(
                        Cloud.INSTANCE.mc.theWorld,
                        movingObjectPosition.blockX,
                        movingObjectPosition.blockY,
                        movingObjectPosition.blockZ
                );

                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

                AxisAlignedBB box =
                        block.getSelectedBoundingBoxFromPool(
                                Cloud.INSTANCE.mc.theWorld,
                                movingObjectPosition.blockX,
                                movingObjectPosition.blockY,
                                movingObjectPosition.blockZ
                        ).expand(f1, f1, f1).getOffsetBoundingBox(-d0, -d1, -d2);

                if(isFilling()){
                    GL11.glColor4f(
                            getFillColor().getRed() / 255f,
                            getFillColor().getGreen() / 255f,
                            getFillColor().getBlue() / 255f,
                            getAlpha() / 255f
                    );
                    Helper3D.drawFilledBoundingBox(box);
                }
                if(isOutline()) {
                    GL11.glColor4f(
                            getOutlineColor().getRed() / 255f,
                            getOutlineColor().getGreen() / 255f,
                            getOutlineColor().getBlue() / 255f,
                            getAlpha() / 255f
                    );
                    RenderGlobal.drawOutlinedBoundingBox(box, -1);
                }
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private float getThickness() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Thickness").getCurrentNumber();
    }

    private boolean isOutline() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Outline").isCheckToggled();
    }

    private boolean isFilling() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Filling").isCheckToggled();
    }

    private Color getFillColor() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Filling Color").getColor();
    }

    private Color getOutlineColor() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Outline Color").getColor();
    }

    private int getAlpha() {
        return (int) Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Alpha").getCurrentNumber();
    }
}
