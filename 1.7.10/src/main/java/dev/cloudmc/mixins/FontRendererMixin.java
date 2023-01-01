package dev.cloudmc.mixins;

import dev.cloudmc.feature.mod.impl.NickHiderMod;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {

    /**
     Modifies every string to not have the Player Username
     */

    @ModifyArg(method = "renderString", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    public String changeString(String nick) {
        return NickHiderMod.replaceNickname(nick);
    }

    /**
     Modifies the string length to be accurate after changing the name
     */

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    public String modifyStringWidth(String nick) {
        return NickHiderMod.replaceNickname(nick);
    }
}
