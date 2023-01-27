/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.titlescreen;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TitleScreen extends Panorama {

    ArrayList<Button> buttons = new ArrayList<>();

    private final Button singlePlayerButton;
    private final Button multiPlayerButton;
    private final Button settingsButton;

    private Animate animate = new Animate();

    public TitleScreen() {
        singlePlayerButton = new Button("Singleplayer", width / 2 - 75, height / 2);
        addButton(singlePlayerButton);
        multiPlayerButton = new Button("Multiplayer", width / 2 - 75, height / 2 + 25);
        addButton(multiPlayerButton);
        settingsButton = new Button("Settings", width / 2 - 75, height / 2 + 50);
        addButton(settingsButton);
        animate.setEase(Easing.LINEAR).setMin(0).setMax(25).setSpeed(200);
    }

    /**
     * Renders button text and logos on the screen
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.renderButton(
                    mouseX,
                    mouseY,
                    width / 2 - 75,
                    height / 2 + i * 25
            );
        }

        drawLogo();
        drawExit(mouseX, mouseY);
        drawText();
    }

    /**
     * Is called when any mouse button is pressed. Adds functionality to every button on screen
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(singlePlayerButton.isPressed(mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        else if (multiPlayerButton.isPressed(mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (settingsButton.isPressed(mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        else if (MathHelper.withinBox(width - 25, 5, 20, 20, mouseX, mouseY)) {
            mc.shutdown();
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the main "Cloud" Text and the Logo in the middle
     */

    private void drawLogo() {
        Cloud.INSTANCE.fontHelper.size40.drawString(
                Cloud.modName,
                width / 2f - Cloud.INSTANCE.fontHelper.size40.getStringWidth(Cloud.modName) / 2f,
                height / 2f - 27.5f,
                -1
        );
        Helper2D.drawPicture(width / 2 - 30, height / 2 - 78, 60, 60, 0x40ffffff, "cloudlogo.png");
    }

    /**
     * Draws the "Cloud Client" Text and Mojang Copyright Notice on the bottom
     */

    private void drawText() {
        Cloud.INSTANCE.fontHelper.size20.drawString(
                "Copyright Mojang Studios. Do not distribute!",
                width - Cloud.INSTANCE.fontHelper.size20.getStringWidth("Copyright Mojang Studios. Do not distribute!") - 2,
                height - Cloud.INSTANCE.fontHelper.size20.getFontHeight(),
                0x50ffffff
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                Cloud.modName + " Client " + Cloud.modVersion,
                4,
                height - Cloud.INSTANCE.fontHelper.size20.getFontHeight(),
                0x50ffffff
        );
    }

    /**
     * Draws the exit button on the top right, and changes the opacity if hovered using mouseX and mouseY
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    private void drawExit(int mouseX, int mouseY) {
        animate.update().setReversed(!MathHelper.withinBox(width - 25, 5, 20, 20, mouseX, mouseY));
        Helper2D.drawRoundedRectangle(
                width - 25,
                5,
                20,
                20,
                2,
                new Color(255, 255, 255, animate.getValueI() + 30).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );
        Helper2D.drawPicture(width - 25, 5, 20, 20, 0xffffffff, "icon/cross.png");
    }

    private void addButton(Button button){
        buttons.add(button);
    }
}