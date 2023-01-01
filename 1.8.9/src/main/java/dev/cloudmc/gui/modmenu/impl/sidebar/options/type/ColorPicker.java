/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.options.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ColorPicker extends Options {

    Animate animate = new Animate();

    private int rgb;
    private boolean drag;
    private boolean drag2;
    private int yPos;
    private int xPos2 = 127, yPos2;
    private int red, blue, green;
    private int finalRed, finalBlue, finalGreen;
    private boolean clientColor = false;

    public ColorPicker(Option option, Panel panel, int y) {
        super(option, panel, y);
        setOptionHeight(75);
        if(option.getName().equalsIgnoreCase("client color")){
            clientColor = true;
        }
    }

    /**
     * Renders the ColorPicker Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {

        animate
                .setEase(Easing.CUBIC_OUT)
                .setMin(0)
                .setMax(71)
                .setSpeed(200)
                .setReversed(false)
                .update();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                ClientStyle.getColor().getRGB()
        );

        if (isOpen()) {
            if (!animate.hasFinished()) {
                Helper2D.startScissor(panel.getX(), panel.getY() + panel.getH() + getY() + 26, panel.getW(), getH() - 23);
            }

            Helper2D.drawRoundedRectangle(
                    panel.getX() + panel.getW() - 37,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() - 45,
                    18, 71, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                    ClientStyle.isRoundedCorners() ? 0 : -1
            );
            Helper2D.drawRoundedRectangle(
                    panel.getX() + panel.getW() - 171,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() - 45,
                    131, 71, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                    ClientStyle.isRoundedCorners() ? 0 : -1
            );

            Helper2D.drawPicture(
                    panel.getX() + panel.getW() - 35,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() - 43,
                    14, 67, 0, "icon/hue.png"
            );
            Helper2D.drawRoundedRectangle(
                    panel.getX() + panel.getW() - 36,
                    panel.getY() + panel.getH() + getY() + yPos + animate.getValueI() - 44,
                    16, 4, 2, -1,
                    ClientStyle.isRoundedCorners() ? 0 : -1
            );

            chooseHue();
            chooseRGB();

            moveBar1(mouseY);
            moveBar2(mouseX, mouseY);

            Helper2D.drawHorizontalGradientRectangle(
                    panel.getX() + panel.getW() - 169,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() - 43,
                    panel.getX() + panel.getW() - 42,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() + 24,
                    -1, new Color(red, green, blue).getRGB()
            );
            Helper2D.drawGradientRectangle(
                    panel.getX() + panel.getW() - 169,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() - 43,
                    panel.getX() + panel.getW() - 42,
                    panel.getY() + panel.getH() + getY() + animate.getValueI() + 24,
                    0x00ffffff, 0xff000000
            );
            Helper2D.drawRoundedRectangle(
                    panel.getX() + panel.getW() - xPos2 - 44,
                    panel.getY() + panel.getH() + getY() + yPos2 + animate.getValueI() - 44,
                    4, 4, 2, -1,
                    ClientStyle.isRoundedCorners() ? 0 : -1
            );

            if (!animate.hasFinished()) {
                Helper2D.endScissor();
            }

            option.setColor(new Color(finalRed, finalGreen, finalBlue));
            if(clientColor){
                ClientStyle.setColor(option.getColor());
            }
        }

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 41,
                panel.getY() + panel.getH() + getY() + 1,
                22, 22, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                ClientStyle.isRoundedCorners() ? 0 : -1
        );
        Helper2D.drawRectangle(
                panel.getX() + panel.getW() - 39,
                panel.getY() + panel.getH() + getY() + 3,
                18, 18, option.getColor().getRGB()
        );

        Cloud.INSTANCE.fontHelper.size20.drawString(
                String.format("#%02x%02x%02x", option.getColor().getRed(), option.getColor().getGreen(), option.getColor().getBlue()),
                panel.getX() + panel.getW() - 60 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(
                                String.format("#%02x%02x%02x", option.getColor().getRed(), option.getColor().getGreen(), option.getColor().getBlue())
                        ),
                panel.getY() + panel.getH() + 3 + getY() + 6, ClientStyle.getColor().getRGB()
        );
    }

    /**
     * Renders the vertical bar for choosing the color hue
     *
     * @param mouseY The current Y position of the mouse
     */

    private void moveBar1(int mouseY) {
        if (drag) {
            yPos = mouseY - (panel.getY() + panel.getH() + getY() + 28);

            if (yPos < 0) {
                yPos = 0;
            }
            if (yPos > 65) {
                yPos = 65;
            }
        }
    }

    /**
     * Renders the horizontal bar for choosing the darkness and brightness of the color
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    private void moveBar2(int mouseX, int mouseY) {
        if (drag2) {
            xPos2 = (panel.getX() + panel.getW() - 42) - mouseX;
            yPos2 = mouseY - (panel.getY() + panel.getH() + getY() + 28);

            if (xPos2 < 0) {
                xPos2 = 0;
            }
            if (xPos2 > 127) {
                xPos2 = 127;
            }
            if (yPos2 < 0) {
                yPos2 = 0;
            }
            if (yPos2 > 66) {
                yPos2 = 66;
            }
        }
    }

    /**
     * Sets the rgb variable to a specific rgb value from the hue image
     */

    private void chooseHue() {
        BufferedImage image;
        try {
            image = ImageIO.read(ClassLoader.getSystemResource("assets/cloudmc/icon/hue.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rgb = image.getRGB(5, yPos * 2);
    }

    /**
     * Sets the final rgb values depending on the darkness and brightness of the image
     */

    private void chooseRGB() {
        int vertical = yPos2 * 4;
        int horizontal = xPos2 * 2;
        if (vertical > 254) {
            vertical = 254;
        }

        red = (rgb & 0x00ff0000) >> 16;
        green = (rgb & 0x0000ff00) >> 8;
        blue = rgb & 0x000000ff;

        int tempRed = (red + horizontal);
        if (tempRed > 255) {
            tempRed = 255;
        }

        int tempGreen = (green + horizontal);
        if (tempGreen > 255) {
            tempGreen = 255;
        }

        int tempBlue = (blue + horizontal);
        if (tempBlue > 255) {
            tempBlue = 255;
        }

        finalRed = (tempRed - vertical);
        if (finalRed < 0) {
            finalRed = 0;
        }

        finalGreen = (tempGreen - vertical);
        if (finalGreen < 0) {
            finalGreen = 0;
        }

        finalBlue = (tempBlue - vertical);
        if (finalBlue < 0) {
            finalBlue = 0;
        }
    }

    /**
     * Changes the drag variables if the mouse is over the bar and is clicked
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                panel.getX() + panel.getW() - 35,
                panel.getY() + panel.getH() + getY() + 28,
                14, 67, mouseX, mouseY)
        ) {
            drag = true;
        }

        if (MathHelper.withinBox(
                panel.getX() + panel.getW() - 169,
                panel.getY() + panel.getH() + getY() + 28,
                127, 67, mouseX, mouseY)
        ) {
            drag2 = true;
        }

        if (MathHelper.withinBox(
                panel.getX(),
                panel.getY() + panel.getH() + getY(),
                panel.getW(), 25, mouseX, mouseY)
        ) {
            if (isOpen()) {
                setH(25);
                setOpen(false);
            }
            else {
                setH(getOptionHeight() + 25);
                setOpen(true);
            }
            animate.reset();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
        drag2 = false;
    }
}
