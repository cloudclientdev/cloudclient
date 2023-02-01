package dev.cloudmc.mixins;

import cpw.mods.fml.client.FMLClientHandler;
import dev.cloudmc.Cloud;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiIngameMenu.class)
public abstract class GuiIngameMenuMixin extends GuiScreen {

    @Shadow
    private int field_146445_a;

    /**
     * @author duplicat
     * @reason add Cloud Button to Game menu
     */
    @Overwrite
    public void initGui() {
        this.field_146445_a = 0;
        this.buttonList.clear();
        byte b0 = -16;
        boolean flag = true;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b0, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton) this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b0, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b0, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(12, this.width / 2 + 2, this.height / 4 + 96 + b0, 98, 20, "Mod Options..."));
        GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + b0, 98, 20, I18n.format("menu.shareToLan")));
        this.buttonList.add(new GuiButton(20, this.width / 2 + 2, this.height / 4 + 72 + b0, 98, 20, "Cloud Menu"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20, I18n.format("gui.achievements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20, I18n.format("gui.stats")));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    /**
     * @author duplicat
     * @reason make cloud button load menu
     */
    @Overwrite
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new GuiMainMenu());
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                if (this.mc.thePlayer != null)
                    this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;
            case 6:
                if (this.mc.thePlayer != null)
                    this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 12:
                FMLClientHandler.instance().showInGameModOptions((GuiIngameMenu) (Object) this);
                break;
            case 20:
                this.mc.displayGuiScreen(Cloud.INSTANCE.hudEditor);
                break;
        }
    }
}
