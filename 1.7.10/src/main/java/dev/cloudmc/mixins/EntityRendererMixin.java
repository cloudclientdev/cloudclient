/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.FreelookMod;
import dev.cloudmc.feature.mod.impl.ZoomMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;setAngles(FF)V", ordinal = 1))
    private void lockPlayerLooking(EntityClientPlayerMP instance, float x, float y) {
        if (!FreelookMod.cameraToggled) {
            instance.setAngles(x, y);
        }
    }

    @Shadow
    private Minecraft mc;

    @Inject(
            method = "updateCameraAndRender",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;setAngles(FF)V", ordinal = 1)
    )
    private void updateCameraAndRender(float p_78480_1_, CallbackInfo ci) {
        float f1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f2 = f1 * f1 * f1 * 8.0F;
        float f3 = (float) this.mc.mouseHelper.deltaX * f2;
        float f4 = (float) this.mc.mouseHelper.deltaY * f2;
        if (FreelookMod.cameraToggled) {
            FreelookMod.cameraYaw += f3 / 8;
            FreelookMod.cameraPitch += f4 / 8;
            if (Math.abs(FreelookMod.cameraPitch) > 90.0F) {
                FreelookMod.cameraPitch = FreelookMod.cameraPitch > 0.0F ? 90.0F : -90.0F;
            }
        }
    }

    @Shadow private float prevCamRoll;
    @Shadow private float camRoll;
    @Shadow private float thirdPersonDistanceTemp;
    @Shadow private float thirdPersonDistance;
    @Shadow private float prevDebugCamYaw;
    @Shadow private float debugCamYaw;
    @Shadow private float prevDebugCamPitch;
    @Shadow private float debugCamPitch;
    @Shadow private boolean cloudFog;

    /**
     * @author DupliCAT
     * @reason Freelook
     */
    @Overwrite
    private void orientCamera(float p_78467_1_) {
        EntityLivingBase entitylivingbase = this.mc.renderViewEntity;
        float f1 = entitylivingbase.yOffset - 1.62F;
        double d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double) p_78467_1_;
        double d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double) p_78467_1_ - (double) f1;
        double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double) p_78467_1_;
        GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * p_78467_1_, 0.0F, 0.0F, 1.0F);

        if (entitylivingbase.isPlayerSleeping()) {
            f1 = (float) ((double) f1 + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);

            if (!this.mc.gameSettings.debugCamEnable) {
                ForgeHooksClient.orientBedCamera(mc, entitylivingbase);
                GL11.glRotatef(entitylivingbase.prevRotationYaw + (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(entitylivingbase.prevRotationPitch + (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView > 0) {
            double d7 = (double) (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * p_78467_1_);
            float f2;
            float f6;

            if (this.mc.gameSettings.debugCamEnable) {
                f6 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * p_78467_1_;
                f2 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * p_78467_1_;
                GL11.glTranslatef(0.0F, 0.0F, (float) (-d7));
                GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f6, 0.0F, 1.0F, 0.0F);
            } else {
                f6 = entitylivingbase.rotationYaw;
                f2 = entitylivingbase.rotationPitch;

                if(FreelookMod.cameraToggled){
                    f6 = FreelookMod.cameraYaw;
                    f2 = FreelookMod.cameraPitch;
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f2 += 180.0F;
                }

                double d3 = (double) (-MathHelper.sin(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
                double d4 = (double) (MathHelper.cos(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
                double d5 = (double) (-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d7;

                if(FreelookMod.cameraToggled){
                    d3 = (double) (MathHelper.sin(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
                    d4 = (double) (-MathHelper.cos(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
                }

                for (int k = 0; k < 8; ++k) {
                    float f3 = (float) ((k & 1) * 2 - 1);
                    float f4 = (float) ((k >> 1 & 1) * 2 - 1);
                    float f5 = (float) ((k >> 2 & 1) * 2 - 1);
                    f3 *= 0.1F;
                    f4 *= 0.1F;
                    f5 *= 0.1F;
                    MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(Vec3.createVectorHelper(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), Vec3.createVectorHelper(d0 - d3 + (double) f3 + (double) f5, d1 - d5 + (double) f4, d2 - d4 + (double) f5));

                    if (movingobjectposition != null) {
                        double d6 = movingobjectposition.hitVec.distanceTo(Vec3.createVectorHelper(d0, d1, d2));

                        if (d6 < d7) {
                            d7 = d6;
                        }
                    }
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if(FreelookMod.cameraToggled){
                    GL11.glRotatef(FreelookMod.cameraPitch - f2, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(FreelookMod.cameraYaw - f6, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, 0.0F, (float) (-d7));
                    GL11.glRotatef(f6 - FreelookMod.cameraYaw, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(f2 - FreelookMod.cameraPitch, 1.0F, 0.0F, 0.0F);
                }
                else {
                    GL11.glRotatef(entitylivingbase.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(entitylivingbase.rotationYaw - f6, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, 0.0F, (float) (-d7));
                    GL11.glRotatef(f6 - entitylivingbase.rotationYaw, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(f2 - entitylivingbase.rotationPitch, 1.0F, 0.0F, 0.0F);
                }
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.debugCamEnable) {
            if(FreelookMod.cameraToggled){
                GL11.glRotatef(FreelookMod.cameraPitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(FreelookMod.cameraYaw, 0.0F, 1.0F, 0.0F);
            }
            else {
                GL11.glRotatef(entitylivingbase.prevRotationPitch + (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(entitylivingbase.prevRotationYaw + (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        GL11.glTranslatef(0.0F, f1, 0.0F);
        d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double) p_78467_1_;
        d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double) p_78467_1_ - (double) f1;
        d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double) p_78467_1_;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, p_78467_1_);
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (Cloud.INSTANCE.modManager.getMod("NoHurtCam").isToggled())
            ci.cancel();
    }

    @Redirect(method = "getFOVModifier", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;fovSetting:F"))
    public float modifyFOV(GameSettings gameSettings) {
        if (Cloud.INSTANCE.modManager.getMod("Zoom").isToggled()) {
            return ZoomMod.getFOV();
        }
        return gameSettings.fovSetting;
    }
}