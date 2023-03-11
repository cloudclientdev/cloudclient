/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.titlescreen;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.titlescreen.buttons.IconButton;
import dev.cloudmc.gui.titlescreen.buttons.TextButton;
import dev.cloudmc.helpers.font.GlyphPageFontRenderer;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiWorldSelection;

import java.io.IOException;
import java.util.ArrayList;

public class TitleScreen extends Panorama {

    private final ArrayList<TextButton> textButtons = new ArrayList<>();
    private final ArrayList<IconButton> iconButtons = new ArrayList<>();

    public TitleScreen() {
        textButtons.add(new TextButton("Singleplayer", width / 2 - 75, height / 2));
        textButtons.add(new TextButton("Multiplayer", width / 2 - 75, height / 2 + 25));
        textButtons.add(new TextButton("Settings", width / 2 - 75, height / 2 + 50));
        iconButtons.add(new IconButton("cross.png", width - 25, 5));
    }

    /**
     * Renders button text and logos on the screen
     *
     * @param mouseX       The current X position of the mouse
     * @param mouseY       The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int y = 0;
        for (TextButton textButton : textButtons) {
            textButton.renderButton(width / 2 - 75, height / 2 + y * 25, mouseX, mouseY);
            y++;
        }

        for (IconButton iconButton : iconButtons) {
            if (iconButton.getIcon().equals("cross.png")) {
                iconButton.renderButton(width - 25, 5, mouseX, mouseY);
            }
        }

        drawLogo();
        drawCopyright();
    }

    /**
     * Is called when any mouse button is pressed. Adds functionality to every button on screen
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (TextButton textButton : textButtons) {
            if (textButton.isHovered(mouseX, mouseY)) {
                switch (textButton.getText()) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiWorldSelection(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                }
            }
        }

        for (IconButton iconButton : iconButtons) {
            if (iconButton.isHovered(mouseX, mouseY)) {
                if (iconButton.getIcon().equals("cross.png")) {
                    mc.shutdown();
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the main "Cloud" Text and the Logo in the middle
     */

    private void drawLogo() {
        GlyphPageFontRenderer fontRenderer = Cloud.INSTANCE.fontHelper.size40;
        fontRenderer.drawString(Cloud.modName, width / 2f - fontRenderer.getStringWidth(Cloud.modName) / 2f, height / 2f - 27.5f, -1);
        Helper2D.drawPicture(width / 2 - 30, height / 2 - 78, 60, 60, 0x40ffffff, "cloudlogo.png");
    }

    /**
     * Draws the "Cloud Client" Text and Mojang Copyright Notice on the bottom
     */

    private void drawCopyright() {
        GlyphPageFontRenderer fontRenderer = Cloud.INSTANCE.fontHelper.size20;
        String copyright = "Copyright Mojang Studios. Do not distribute!";
        String text = Cloud.modName + " Client " + Cloud.modVersion;
        fontRenderer.drawString(copyright, width - fontRenderer.getStringWidth(copyright) - 2, height - fontRenderer.getFontHeight(), 0x50ffffff);
        fontRenderer.drawString(text, 4, height - fontRenderer.getFontHeight(), 0x50ffffff);
    }
}