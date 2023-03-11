/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.helpers.render.Helper3D;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox;

public class BlockOverlayMod extends Mod {

    public BlockOverlayMod() {
        super(
                "BlockOverlay",
                "Adds an customizable overlay to blocks.",
                Type.Visual
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Outline", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Filling", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Thickness", this, 20, 3));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Outline Color", this, new Color(0, 0, 0), new Color(255, 0, 0), 0, new float[]{0, 65}));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Filling Color", this, new Color(0, 0, 0), new Color(255, 0, 0), 0, new float[]{0, 65}));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Alpha", this, 255, 100));
    }

    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
        e.setCanceled(true);
        drawSelectionBox(e.getPlayer(), e.getTarget(), e.getSubID(), e.getPartialTicks());
    }

    public void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
            GlStateManager.glLineWidth(getThickness());
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            IBlockState iblockstate = Cloud.INSTANCE.mc.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() != Material.AIR && Cloud.INSTANCE.mc.world.getWorldBorder().contains(blockpos)) {
                double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
                double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
                double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

                AxisAlignedBB box = iblockstate.getSelectedBoundingBox(Cloud.INSTANCE.mc.world, blockpos)
                                .grow(0.0020000000949949026D).offset(-d3, -d4, -d5);

                if(isFilling()) {
                    RenderGlobal.renderFilledBox(
                            box,
                            getFillColor().getRed() / 255f,
                            getFillColor().getGreen() / 255f,
                            getFillColor().getBlue() / 255f,
                            getAlpha() / 255f
                    );
                }

                if(isOutline()) {
                    RenderGlobal.drawSelectionBoundingBox(
                            box,
                            getOutlineColor().getRed() / 255f,
                            getOutlineColor().getGreen() / 255f,
                            getOutlineColor().getBlue() / 255f,
                            getAlpha() / 255f
                    );
                }
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
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
