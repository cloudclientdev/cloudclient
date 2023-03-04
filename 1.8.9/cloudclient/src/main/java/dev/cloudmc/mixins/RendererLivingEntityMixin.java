package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.FloatBuffer;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin<T extends EntityLivingBase> extends Render<T> {

    protected RendererLivingEntityMixin(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(
            method = "canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/entity/RenderManager;livingPlayer:Lnet/minecraft/entity/Entity;"
            )
    )
    public Entity canRenderName(RenderManager instance) {
        if (Cloud.INSTANCE.modManager.getMod("NameTag").isToggled()) {
            if (Cloud.INSTANCE.settingManager.getSettingByModAndName(
                    "NameTag", "NameTag in 3rd Person").isCheckToggled()) {
                return null;
            }
        }

        return instance.livingPlayer;
    }

    @Inject(method = "canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z", at = @At("HEAD"), cancellable = true)
    public void canRenderName(T entity, CallbackInfoReturnable<Boolean> cir) {
        if(Cloud.INSTANCE.modManager.getMod("NameTag").isToggled()) {
            if(Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Disable Player NameTags").isCheckToggled()) {
                cir.setReturnValue(false);
            }
        }
    }

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