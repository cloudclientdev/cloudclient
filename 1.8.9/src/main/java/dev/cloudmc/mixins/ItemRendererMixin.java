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
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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

    /**
     * @author DupliCAT
     * @reason Freelook
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        if (this.mc.thePlayer.getHeldItem() != null &&
                Cloud.INSTANCE.modManager.getMod("Animation").isToggled() &&
                Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled()
        ) {
            this.attemptSwing();
        }

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
                EnumAction enumaction = this.itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(player, partialTicks);
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case BLOCK:
                        this.transformFirstPersonItem(f,
                                Cloud.INSTANCE.modManager.getMod("Animation").isToggled() &&
                                        Cloud.INSTANCE.settingManager.getSettingByModAndName("Animation", "Block Animation").isCheckToggled()
                                        ? f1 : 0.0F
                        );
                        this.doBlockTransformations();
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, 0.0F);
                        this.doBowTransformations(partialTicks, player);
                }
            }
            else {
                this.doItemUsedTransformations(f1);
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

    private void attemptSwing() {
        if (this.mc.thePlayer.getItemInUseCount() > 0) {
            final boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown() &&
                    this.mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                swingItem(this.mc.thePlayer);
            }
        }
    }

    private void swingItem(EntityPlayerSP player) {
        int swingEnd = player.isPotionActive(Potion.digSpeed) ?
                (6 - (1 + player.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (player.isPotionActive(Potion.digSlowdown) ?
                (6 + (1 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!player.isSwingInProgress || player.swingProgressInt >= swingEnd / 2 || player.swingProgressInt < 0) {
            player.swingProgressInt = -1;
            player.isSwingInProgress = true;
        }
    }
}
