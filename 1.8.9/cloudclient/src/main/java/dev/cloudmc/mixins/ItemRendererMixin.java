/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Final @Shadow private Minecraft mc;
    @Shadow private float prevEquippedProgress;
    @Shadow private float equippedProgress;
    @Shadow private ItemStack itemToRender;
    @Shadow private void rotateArroundXAndY(float angle, float angleY) {}
    @Shadow private void setLightMapFromPlayer(AbstractClientPlayer clientPlayer) {}
    @Shadow private void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks) {}
    @Shadow private void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress) {}
    @Shadow private void transformFirstPersonItem(float equipProgress, float swingProgress) {}
    @Shadow private void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks) {}
    @Shadow private void doBlockTransformations() {}
    @Shadow private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer) {}
    @Shadow private void doItemUsedTransformations(float swingProgress) {}
    @Shadow public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {}
    @Shadow private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress) {}

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItemModelForEntity(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"))
    public void renderItem(EntityLivingBase entity, ItemStack item, ItemCameraTransforms.TransformType transformType, CallbackInfo ci) {
        if (!(item.getItem() instanceof ItemSword)) return;
        if (!(entity instanceof EntityPlayer)) return;
        if (!(((EntityPlayer)entity).getItemInUseCount() > 0)) return;
        if (!(item.getItemUseAction() == EnumAction.BLOCK)) return;
        if (transformType != ItemCameraTransforms.TransformType.THIRD_PERSON) return;
        if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled()) {
            GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-0.04F, -0.04F, 0.0F);
        }
    }

    /**
     * Makes the sword position and rotation to be more accurate to 1.7.10
     */
    @Inject(method = "doBlockTransformations", at = @At("HEAD"), cancellable = true)
    public void swordBlockTransformations(CallbackInfo ci) {
        if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled()) {
            GlStateManager.translate(-0.24F, 0.17F, 0.0F);
            GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.18F, 0.00F);
            ci.cancel();
        }
    }

    /**
     * @author DupliCAT
     * @reason Freelook and Animation mods
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        boolean animationModToggled = Cloud.INSTANCE.modManager.getMod("Animation").isToggled();
//        if (animationModToggled && this.mc.thePlayer.getHeldItem() != null) {
//            if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled()) {
//                this.forceSwingArm();
//            } else if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Eat/Drink Animation").isCheckToggled()) {
//                this.forceSwingArm();
//            } else if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Bow Animation").isCheckToggled()) {
//                this.forceSwingArm();
//            }
//        }
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP player = this.mc.thePlayer;
        float f1 = player.getSwingProgress(partialTicks);
        float f2 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(player);
        this.rotateWithPlayerRotations(player, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null) {
            if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(player, f2, f, f1);
            }
            else if (player.getItemInUseCount() > 0) {
                EnumAction action = this.itemToRender.getItemUseAction();
                switch (action) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(player, partialTicks);
                        this.transformFirstPersonItem(f, animationModToggled &&
                                Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Eat/Drink Animation").isCheckToggled()
                                ? f1 : 0.0F);
                        break;
                    case BLOCK:
                        this.transformFirstPersonItem(f, animationModToggled &&
                                Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled()
                                ? f1 : 0.0F);
                        this.doBlockTransformations();
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, animationModToggled &&
                                Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Bow Animation").isCheckToggled()
                                ? f1 : 0.0F);
                        this.doBowTransformations(partialTicks, player);
                }
            }
            else {
                this.doItemUsedTransformations(f1);
                if (this.itemToRender.getItem() instanceof ItemFishingRod && animationModToggled &&
                        Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Fishing Rod").isCheckToggled()) {
                    GlStateManager.translate(0.0F, 0.0F, -0.35F);
                }
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(player, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!player.isInvisible()) {
            this.renderPlayerArm(player, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

//    /**
//     * Swings the player's arm if you're holding the attack and use item keys at the same time and looking at a block.
//     */
//    private void attemptSwing() {
//        if (this.mc.thePlayer.getItemInUseCount() > 0) {
//            final boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown() &&
//                    this.mc.gameSettings.keyBindUseItem.isKeyDown();
//            if (mouseDown && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
//                forceSwingArm();
//            }
//        }
//    }
//
//    /**
//     * Forces the player to swing their arm.
//     */
//    private void forceSwingArm() {
//        EntityPlayerSP player = this.mc.thePlayer;
//        int swingEnd = player.isPotionActive(Potion.digSpeed) ?
//                (6 - (1 + player.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (player.isPotionActive(Potion.digSlowdown) ?
//                (6 + (1 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
//        if (!player.isSwingInProgress || player.swingProgressInt >= swingEnd / 2 || player.swingProgressInt < 0) {
//            player.swingProgressInt = -1;
//            player.isSwingInProgress = true;
//        }
//    }
}
