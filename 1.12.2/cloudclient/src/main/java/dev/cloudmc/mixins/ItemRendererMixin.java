package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Redirect(method = "renderFireInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"))
    public void renderFireInFirstPerson(float x, float y, float z) {
        GlStateManager.translate(x, y - Cloud.INSTANCE.optionManager.getOptionByName("Fire Height").getCurrentNumber() / 100f, z);
    }
}
