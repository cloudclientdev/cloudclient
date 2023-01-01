package dev.cloudmc.mixins;

import dev.cloudmc.helpers.DebugFPS;
import dev.cloudmc.helpers.animation.DeltaTime;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {

    @Shadow private static int debugFPS;

    long lastFrame = Sys.getTime();

    /**
     Calculates the delta time which is used for Animations
     Also gets the current debug fps
     */
    @Inject(method = "runGameLoop", at = @At("RETURN"))
    public void runGameLoop(CallbackInfo ci){
        long currentTime = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;
        DeltaTime.setDeltaTime(deltaTime);
        DebugFPS.setDebugFPS(debugFPS);
    }
}
