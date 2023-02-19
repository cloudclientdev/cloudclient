/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.renderer.ItemRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Redirect(method = "renderFireInFirstPerson", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V"))
    public void renderFireInFirstPerson(float x, float y, float z) {
        GL11.glTranslatef(x, y - Cloud.INSTANCE.optionManager.getOptionByName("Fire Height").getCurrentNumber() / 100f, z);
    }
}
