package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.FreelookMod;
import dev.cloudmc.feature.mod.impl.ZoomMod;
import dev.cloudmc.helpers.render.Helper3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;turn(FF)V"))
    private void lockPlayerLooking(EntityPlayerSP instance, float x, float y) {
        if (!FreelookMod.cameraToggled) {
            instance.turn(x, y);
        }
    }

    @Inject(
            method = "updateCameraAndRender",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;turn(FF)V", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void updateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo ci, boolean flag, float f, float f1, float f2, float f3, int i) {
        if (FreelookMod.cameraToggled) {
            FreelookMod.cameraYaw += f2 / 8;
            FreelookMod.cameraPitch += f3 / 8;
            if (Math.abs(FreelookMod.cameraPitch) > 90.0F) {
                FreelookMod.cameraPitch = FreelookMod.cameraPitch > 0.0F ? 90.0F : -90.0F;
            }
        }
    }

    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private float thirdPersonDistancePrev;

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 2))
    public void orientCamera(float x, float y, float z, float partialTicks) {
        double double0 = mc.player.prevPosX + (mc.player.posX - mc.player.prevPosX) * (double) partialTicks;
        double double1 = mc.player.prevPosY + (mc.player.posY - mc.player.prevPosY) * (double) partialTicks + (double) mc.player.getEyeHeight();
        double double2 = mc.player.prevPosZ + (mc.player.posZ - mc.player.prevPosZ) * (double) partialTicks;
        double double3 = this.thirdPersonDistancePrev + (4.0F - this.thirdPersonDistancePrev) * partialTicks;
        if (FreelookMod.cameraToggled) {
            GlStateManager.translate(0.0F, 0.0F, (float) (-Helper3D.calculateCameraDistance(double0, double1, double2, double3)));
        } else {
            GlStateManager.translate(x, y, z);
        }
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

    @Redirect(method = "setupCameraTransform", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;viewBobbing:Z", ordinal = 0))
    public boolean setupCameraTransform(GameSettings instance) {
        return !Cloud.INSTANCE.optionManager.getOptionByName("Minimal View Bobbing").isCheckToggled() && Cloud.INSTANCE.mc.gameSettings.viewBobbing;
    }

    /**
     * @author duplicat
     * @reason NameTag tweaks
     */
    @Overwrite
    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking) {
        boolean nameTagToggled = Cloud.INSTANCE.modManager.getMod("NameTag").isToggled();
        int color = Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Font Color").getColor().getRGB();
        float alpha = nameTagToggled ? Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Opacity").getCurrentNumber() / 255f : 0.25f;
        float scale = nameTagToggled ? Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Size").getCurrentNumber() : 1;
        float yPos = nameTagToggled ? Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Y Position").getCurrentNumber() - 2.5f : 0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, isSneaking ? y : y + yPos, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(isSneaking ? -0.025F : -0.025F * scale, isSneaking ? -0.025F : -0.025F * scale, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!isSneaking) {
            GlStateManager.disableDepth();
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, isSneaking ? 0.25f : alpha).endVertex();
        bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, isSneaking ? 0.25f : alpha).endVertex();
        bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, isSneaking ? 0.25f : alpha).endVertex();
        bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, isSneaking ? 0.25f : alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, nameTagToggled ? color : 553648127);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : nameTagToggled ? color : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
