package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiIngameMenu.class)
public abstract class GuiIngameMenuMixin extends GuiScreen {

    @Shadow private int saveStep;

    /**
     * @author duplicat
     * @reason add Cloud Button to Game menu
     */
    @Overwrite
    public void initGui() {
        this.saveStep = 0;
        this.buttonList.clear();
        int i = -16;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144 - 16, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(30, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, "Servers"));
        this.buttonList.add(new GuiButton(31, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, "Cloud Client"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + i, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(12, this.width / 2 + 2, this.height / 4 + 120 + i, 98, 20, I18n.format("fml.menu.modoptions")));
        GuiButton guibutton = this.addButton(new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + i, 200, 20, I18n.format("menu.shareToLan")));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.advancements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats")));
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
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);

                if (flag) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                } else if (flag1) {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                } else {
                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                }

            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                if (this.mc.player != null)
                    this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager()));
                break;
            case 6:
                if (this.mc.player != null)
                    this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 12:
                net.minecraftforge.fml.client.FMLClientHandler.instance().showInGameModOptions((GuiIngameMenu) (Object) this);
                break;
            case 30:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 31:
                this.mc.displayGuiScreen(Cloud.INSTANCE.hudEditor);
                break;
        }
    }
}
