package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin extends Gui {

    @Redirect(
            method = "drawWorldBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawGradientRect(IIIIII)V"
            )
    )
    public void drawWorldBackground(GuiScreen instance, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Gui Tweaks", "Darken Background").isCheckToggled() ||
                !Cloud.INSTANCE.modManager.getMod("Gui Tweaks").isToggled()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }
}
