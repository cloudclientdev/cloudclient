package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {

    @Redirect(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 3))
    public void setRGB(float red, float green, float blue, float alpha) {
        if(Cloud.INSTANCE.modManager.getMod("Hit Color").isToggled()) {
            Color color = Cloud.INSTANCE.settingManager.getSettingByModAndName("Hit Color", "Damage Color").getColor();
            float a = Cloud.INSTANCE.settingManager.getSettingByModAndName("Hit Color", "Alpha").getCurrentNumber();
            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, a / 255f);
        } else {
            GL11.glColor4f(red, green, blue, alpha);
        }
    }
}
