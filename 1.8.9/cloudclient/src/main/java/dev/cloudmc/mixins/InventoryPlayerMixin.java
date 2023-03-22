package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.ZoomMod;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPlayer.class)
public abstract class InventoryPlayerMixin {

    @Inject(method = "changeCurrentItem", at = @At("HEAD"), cancellable = true)
    public void changeCurrentItem(int direction, CallbackInfo ci) {
        if(ZoomMod.isZoom()) {
            ci.cancel();
        }
    }
}
