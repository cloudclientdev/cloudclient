/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin extends Gui {

    private static int scroll = 0;

    @Shadow
    protected static RenderItem itemRender;

    @Shadow
    public int width;

    @Shadow
    public int height;

    /**
     * @author DupliCAT
     * @reason ScrollableTooltips
     */
    @Overwrite(remap = false)
    protected void drawHoveringText(List lines, int mouseX, int mouseY, FontRenderer font) {
        if (!lines.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = lines.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int tooltipX = mouseX + 12;
            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (lines.size() > 1) {
                tooltipHeight += 2 + (lines.size() - 1) * 10;
            }

            if (tooltipX + k > width) {
                tooltipX -= 28 + k;
            }

            if (tooltipY + tooltipHeight + 6 > this.height) {
                tooltipY = this.height - tooltipHeight - 6;
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(0, getTooltipPosition(tooltipY, tooltipHeight, height), 0);

            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = -267386864;
            this.drawGradientRect(tooltipX - 3, tooltipY - 4, tooltipX + k + 3, tooltipY - 3, j1, j1);
            this.drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + k + 3, tooltipY + tooltipHeight + 4, j1, j1);
            this.drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + k + 3, tooltipY + tooltipHeight + 3, j1, j1);
            this.drawGradientRect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, j1, j1);
            this.drawGradientRect(tooltipX + k + 3, tooltipY - 3, tooltipX + k + 4, tooltipY + tooltipHeight + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, k1, l1);
            this.drawGradientRect(tooltipX + k + 2, tooltipY - 3 + 1, tooltipX + k + 3, tooltipY + tooltipHeight + 3 - 1, k1, l1);
            this.drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + k + 3, tooltipY - 3 + 1, k1, k1);
            this.drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + k + 3, tooltipY + tooltipHeight + 3, l1, l1);

            for (int i2 = 0; i2 < lines.size(); ++i2) {
                String s1 = (String) lines.get(i2);
                font.drawStringWithShadow(s1, tooltipX, tooltipY, -1);

                if (i2 == 0) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GL11.glPopMatrix();

            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    private static int getTooltipPosition(int tooltipY, int tooltipHeight, int screenHeight) {
        boolean shouldScroll = tooltipY < 0;

        if (shouldScroll && Cloud.INSTANCE.modManager.getMod("ScrollTooltips").isToggled()) {
            int mouseScroll = Mouse.getDWheel();

            if (mouseScroll > 0) {
                if(scroll + tooltipY < 10) {
                    scroll += 10;
                }
            } else if (mouseScroll < 0) {
                if(scroll + tooltipY + tooltipHeight + 10 > screenHeight) {
                    scroll -= 10;
                }
            }

        } else {
            scroll = 0;
        }

        return scroll;
    }
}