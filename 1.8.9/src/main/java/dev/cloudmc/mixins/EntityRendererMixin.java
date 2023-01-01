package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.FreelookMod;
import dev.cloudmc.feature.mod.impl.ZoomMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Shadow private Minecraft mc;
    @Shadow private float thirdPersonDistanceTemp;
    @Shadow private float thirdPersonDistance;
    @Shadow private boolean cloudFog;
    @Shadow private long prevFrameTime;
    @Shadow private float smoothCamYaw;
    @Shadow private float smoothCamPitch;
    @Shadow private float smoothCamPartialTicks;
    @Shadow private float smoothCamFilterX;
    @Shadow private float smoothCamFilterY;
    @Shadow public static boolean anaglyphEnable;
    @Shadow private ShaderGroup theShaderGroup;
    @Shadow private boolean useShader;
    @Shadow private long renderEndNanoTime;
    @Shadow public void renderWorld(float partialTicks, long finishTimeNano) {}
    @Shadow public void setupOverlayRendering() {}

    /**
     * @author DupliCAT
     * @reason Freelook
     */
    @Overwrite
    private void orientCamera(float partialTicks) {
        Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPlayerSleeping()) {
            f = (float) ((double) f + 1.0D);
            GlStateManager.translate(0.0F, 0.3F, 0.0F);

            if (!this.mc.gameSettings.debugCamEnable) {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                net.minecraftforge.client.ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);

                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
            }
        }
        else if (this.mc.gameSettings.thirdPersonView > 0) {
            double d3 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks;

            if (this.mc.gameSettings.debugCamEnable) {
                GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
            }
            else {
                float f1 = entity.rotationYaw;
                float f2 = entity.rotationPitch;

                if (FreelookMod.cameraToggled) {
                    f1 = FreelookMod.cameraYaw;
                    f2 = FreelookMod.cameraPitch;
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f2 += 180.0F;
                }

                double d4 = (double) (-MathHelper.sin(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
                double d5 = (double) (MathHelper.cos(f1 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d3;
                double d6 = (double) (-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d3;

                for (int i = 0; i < 8; ++i) {
                    float f3 = (float) ((i & 1) * 2 - 1);
                    float f4 = (float) ((i >> 1 & 1) * 2 - 1);
                    float f5 = (float) ((i >> 2 & 1) * 2 - 1);
                    f3 = f3 * 0.1F;
                    f4 = f4 * 0.1F;
                    f5 = f5 * 0.1F;
                    MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), new Vec3(d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5));

                    if (movingobjectposition != null) {
                        double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

                        if (d7 < d3) {
                            d3 = d7;
                        }
                    }
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (FreelookMod.cameraToggled) {
                    GlStateManager.rotate(FreelookMod.cameraPitch - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(FreelookMod.cameraYaw - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - FreelookMod.cameraYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - FreelookMod.cameraPitch, 1.0F, 0.0F, 0.0F);
                }
                else {
                    GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
                    GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
                }
            }
        }
        else {
            GlStateManager.translate(0.0F, 0.0F, -0.1F);
        }

        if (!this.mc.gameSettings.debugCamEnable) {
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            float roll = 0.0F;
            if (entity instanceof EntityAnimal) {
                EntityAnimal entityanimal = (EntityAnimal) entity;
                yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
            }
            Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
            net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup event = new net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup((EntityRenderer) (Object) this, entity, block, partialTicks, yaw, pitch, roll);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

            if (FreelookMod.cameraToggled) {
                GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(FreelookMod.cameraPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(FreelookMod.cameraYaw + 180.0F, 0.0F, 1.0F, 0.0F);
            }
            else {
                GlStateManager.rotate(event.roll, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(event.pitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(event.yaw, 0.0F, 1.0F, 0.0F);
            }
        }

        GlStateManager.translate(0.0F, -f, 0.0F);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + (double) f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
        this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
    }

    /**
     * @author DupliCAT
     * @reason Freelook
     */
    @Overwrite
    public void updateCameraAndRender(float partialTicks, long nanoTime) {
        boolean flag = Display.isActive();

        if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        }
        else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }

        this.mc.mcProfiler.startSection("mouse");

        if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }

        if (this.mc.inGameHasFocus && flag) {

            if (FreelookMod.cameraToggled && mc.gameSettings.thirdPersonView != 1) {
                FreelookMod.cameraToggled = false;
            }

            this.mc.mouseHelper.mouseXYChange();
            float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float) this.mc.mouseHelper.deltaX * f1;
            float f3 = (float) this.mc.mouseHelper.deltaY * f1;
            int i = 1;

            if (this.mc.gameSettings.invertMouse) {
                i = -1;
            }

            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += f2;
                this.smoothCamPitch += f3;
                float f4 = partialTicks - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = partialTicks;
                f2 = this.smoothCamFilterX * f4;
                f3 = this.smoothCamFilterY * f4;

            }
            else {
                this.smoothCamYaw = 0.0F;
                this.smoothCamPitch = 0.0F;
            }
            if (FreelookMod.cameraToggled) {
                FreelookMod.cameraYaw += f2 / 8;
                FreelookMod.cameraPitch += f3 / 8;
                if (Math.abs(FreelookMod.cameraPitch) > 90.0F) {
                    FreelookMod.cameraPitch = FreelookMod.cameraPitch > 0.0F ? 90.0F : -90.0F;
                }
            }
            else {
                this.mc.thePlayer.setAngles(f2, f3 * (float) i);
            }
        }

        this.mc.mcProfiler.endSection();

        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.getScaledHeight();
            final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            int i2 = this.mc.gameSettings.limitFramerate;

            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("level");
                int j = Math.min(Minecraft.getDebugFPS(), i2);
                j = Math.max(j, 60);
                long k = System.nanoTime() - nanoTime;
                long l = Math.max((long) (1000000000 / j / 4) - k, 0L);
                this.renderWorld(partialTicks, System.nanoTime() + l);

                if (OpenGlHelper.shadersSupported) {
                    this.mc.renderGlobal.renderEntityOutlineFramebuffer();

                    if (this.theShaderGroup != null && this.useShader) {
                        GlStateManager.matrixMode(5890);
                        GlStateManager.pushMatrix();
                        GlStateManager.loadIdentity();
                        this.theShaderGroup.loadShaderGroup(partialTicks);
                        GlStateManager.popMatrix();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GlStateManager.alphaFunc(516, 0.1F);
                    this.mc.ingameGUI.renderGameOverlay(partialTicks);
                }

                this.mc.mcProfiler.endSection();
            }
            else {
                GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
            }

            if (this.mc.currentScreen != null) {
                GlStateManager.clear(256);

                try {
                    net.minecraftforge.client.ForgeHooksClient.drawScreen(this.mc.currentScreen, k1, l1, partialTicks);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addCrashSectionCallable("Screen name", () -> EntityRendererMixin.this.mc.currentScreen.getClass().getCanonicalName());
                    crashreportcategory.addCrashSectionCallable("Mouse location", () -> String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY()));
                    crashreportcategory.addCrashSectionCallable("Screen size", () -> String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), EntityRendererMixin.this.mc.displayWidth, EntityRendererMixin.this.mc.displayHeight, scaledresolution.getScaleFactor()));
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    /**
     * @author DupliCAT
     * @reason NoHurtCam
     */
    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (Cloud.INSTANCE.modManager.getMod("NoHurtCam").isToggled())
            ci.cancel();
    }

    @Shadow private boolean debugView;
    @Shadow private float fovModifierHandPrev;
    @Shadow private float fovModifierHand;

    /**
     * @author DupliCAT
     * @reason Zoom
     */
    @Overwrite
    private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
        if (this.debugView) {
            return 90.0F;
        }
        else {
            Entity entity = this.mc.getRenderViewEntity();
            float f = 70.0F;

            if (useFOVSetting) {
                if(Cloud.INSTANCE.modManager.getMod("Zoom").isToggled()){
                    f = ZoomMod.getFOV();
                }
                else {
                    f = mc.gameSettings.fovSetting;
                }
                f = f * (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
            }

            if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0F) {
                float f1 = (float) ((EntityLivingBase) entity).deathTime + partialTicks;
                f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
            }

            Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);

            if (block.getMaterial() == Material.water) {
                f = f * 60.0F / 70.0F;
            }

            return net.minecraftforge.client.ForgeHooksClient.getFOVModifier((EntityRenderer) (Object) this, entity, block, partialTicks, f);
        }
    }


}