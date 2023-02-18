package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.FloatBuffer;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 0))
    public FloatBuffer setRed(FloatBuffer instance, float v) {
        if (Cloud.INSTANCE.modManager.getMod("Hit Color").isToggled()) {
            instance.put(Cloud.INSTANCE.settingManager.getSettingByModAndName(
                    "Hit Color", "Damage Color").getColor().getRed() / 255f);
        } else {
            instance.put(1f);
        }
        return instance;
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 1))
    public FloatBuffer setGreen(FloatBuffer instance, float v) {
        if (Cloud.INSTANCE.modManager.getMod("Hit Color").isToggled()) {
            instance.put(Cloud.INSTANCE.settingManager.getSettingByModAndName(
                    "Hit Color", "Damage Color").getColor().getGreen() / 255f);
        } else {
            instance.put(0f);
        }
        return instance;
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 2))
    public FloatBuffer setBlue(FloatBuffer instance, float v) {
        if (Cloud.INSTANCE.modManager.getMod("Hit Color").isToggled()) {
            instance.put(Cloud.INSTANCE.settingManager.getSettingByModAndName(
                    "Hit Color", "Damage Color").getColor().getBlue() / 255f);
        } else {
            instance.put(0f);
        }
        return instance;
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 3))
    public FloatBuffer setAlpha(FloatBuffer instance, float v) {
        if (Cloud.INSTANCE.modManager.getMod("Hit Color").isToggled()) {
            instance.put(Cloud.INSTANCE.settingManager.getSettingByModAndName(
                    "Hit Color", "Alpha").getCurrentNumber() / 255f);
        } else {
            instance.put(0.3f);
        }
        return instance;
    }
}