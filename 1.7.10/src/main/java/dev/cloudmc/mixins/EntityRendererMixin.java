package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.FreelookMod;
import dev.cloudmc.feature.mod.impl.ZoomMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Callable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

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

                if (FreelookMod.cameraToggled) {
                    f6 = FreelookMod.cameraYaw;
                    f2 = FreelookMod.cameraPitch;
                }

                if (this.mc.gameSettings.thirdPersonView == 2) {
                    f2 += 180.0F;
                }

                double d3 = (double) (-MathHelper.sin(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
                double d4 = (double) (MathHelper.cos(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
                double d5 = (double) (-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d7;

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
                GL11.glRotatef(FreelookMod.cameraYaw + 180, 0.0F, 1.0F, 0.0F);
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

    @Shadow private boolean lightmapUpdateNeeded;
    @Shadow private void updateLightmap(float p_78472_1_) {}
    @Shadow private long prevFrameTime;
    @Shadow private float smoothCamYaw;
    @Shadow private float smoothCamPitch;
    @Shadow private float smoothCamPartialTicks;
    @Shadow private float smoothCamFilterX;
    @Shadow private float smoothCamFilterY;
    @Shadow public static boolean anaglyphEnable;
    @Shadow public void renderWorld(float p_78471_1_, long p_78471_2_) {}
    @Shadow private long renderEndNanoTime;
    @Shadow public ShaderGroup theShaderGroup;
    @Shadow public void setupOverlayRendering() {}


    /**
     * @author DupliCAT
     * @reason Freelook
     */
    @Overwrite
    public void updateCameraAndRender(float p_78480_1_) {
        this.mc.mcProfiler.startSection("lightTex");

        if (this.lightmapUpdateNeeded) {
            this.updateLightmap(p_78480_1_);
        }

        this.mc.mcProfiler.endSection();
        boolean flag = Display.isActive();

        if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = Minecraft.getSystemTime();
        }

        this.mc.mcProfiler.startSection("mouse");

        if (this.mc.inGameHasFocus && flag) {

            if(FreelookMod.cameraToggled && mc.gameSettings.thirdPersonView != 1){
                FreelookMod.cameraToggled = false;
            }

            this.mc.mouseHelper.mouseXYChange();
            float f1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f1 * f1 * f1 * 8.0F;
            float f3 = (float) this.mc.mouseHelper.deltaX * f2;
            float f4 = (float) this.mc.mouseHelper.deltaY * f2;
            byte b0 = 1;

            if (this.mc.gameSettings.invertMouse) {
                b0 = -1;
            }

            if (this.mc.gameSettings.smoothCamera) {
                this.smoothCamYaw += f3;
                this.smoothCamPitch += f4;
                float f5 = p_78480_1_ - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = p_78480_1_;
                f3 = this.smoothCamFilterX * f5;
                f4 = this.smoothCamFilterY * f5;
            }
            if(FreelookMod.cameraToggled){
                FreelookMod.cameraYaw += f3 / 8;
                FreelookMod.cameraPitch += f4 / 8;
                if (Math.abs(FreelookMod.cameraPitch) > 90.0F) {
                    FreelookMod.cameraPitch = FreelookMod.cameraPitch > 0.0F ? 90.0F : -90.0F;
                }
            }
            else {
                this.mc.thePlayer.setAngles(f3, f4 * (float) b0);
            }
        }

        this.mc.mcProfiler.endSection();

        if (!this.mc.skipRenderWorld) {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            final int k = Mouse.getX() * i / this.mc.displayWidth;
            final int l = j - Mouse.getY() * j / this.mc.displayHeight - 1;
            int i1 = this.mc.gameSettings.limitFramerate;

            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("level");

                if (this.mc.isFramerateLimitBelowMax()) {
                    this.renderWorld(p_78480_1_, this.renderEndNanoTime + (long) (1000000000 / i1));
                } else {
                    this.renderWorld(p_78480_1_, 0L);
                }

                if (OpenGlHelper.shadersSupported) {
                    if (this.theShaderGroup != null) {
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glPushMatrix();
                        GL11.glLoadIdentity();
                        this.theShaderGroup.loadShaderGroup(p_78480_1_);
                        GL11.glPopMatrix();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    this.mc.ingameGUI.renderGameOverlay(p_78480_1_, this.mc.currentScreen != null, k, l);
                }

                this.mc.mcProfiler.endSection();
            } else {
                GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
            }

            if (this.mc.currentScreen != null) {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

                try {
                    if (!MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.DrawScreenEvent.Pre(this.mc.currentScreen, k, l, p_78480_1_)))
                        this.mc.currentScreen.drawScreen(k, l, p_78480_1_);
                    MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.DrawScreenEvent.Post(this.mc.currentScreen, k, l, p_78480_1_));
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addCrashSectionCallable("Screen name", () -> mc.currentScreen.getClass().getCanonicalName());
                    crashreportcategory.addCrashSectionCallable("Mouse location", () -> String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k, l, Mouse.getX(), Mouse.getY()));
                    crashreportcategory.addCrashSectionCallable("Screen size", () -> String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), mc.displayWidth, mc.displayHeight, scaledresolution.getScaleFactor()));
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

    @Shadow private Minecraft mc;
    @Shadow public int debugViewDirection;
    @Shadow private float fovModifierHandPrev;
    @Shadow private float fovModifierHand;
    @Shadow private float prevDebugCamFOV;
    @Shadow private float debugCamFOV;

    /**
     * @author DupliCAT
     * @reason Zoom
     */
    @Overwrite
    private float getFOVModifier(float p_78481_1_, boolean p_78481_2_) {
        if (this.debugViewDirection > 0) {
            return 90.0F;
        } else {
            EntityLivingBase entityplayer = this.mc.renderViewEntity;
            float f1 = 70.0F;

            if (p_78481_2_) {
                if(Cloud.INSTANCE.modManager.getMod("Zoom").isToggled()){
                    f1 = ZoomMod.getFOV();
                }
                else {
                    f1 = mc.gameSettings.fovSetting;
                }
                f1 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * p_78481_1_;
            }

            if (entityplayer.getHealth() <= 0.0F) {
                float f2 = (float) entityplayer.deathTime + p_78481_1_;
                f1 /= (1.0F - 500.0F / (f2 + 500.0F)) * 2.0F + 1.0F;
            }

            Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entityplayer, p_78481_1_);

            if (block.getMaterial() == Material.water) {
                f1 = f1 * 60.0F / 70.0F;
            }

            return f1 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * p_78481_1_;
        }
    }
}