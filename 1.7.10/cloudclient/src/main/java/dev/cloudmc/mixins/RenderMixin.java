package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Render.class)
public class RenderMixin {

    @Shadow
    protected RenderManager renderManager;

    @Shadow
    public FontRenderer getFontRendererFromRenderManager() {
        return null;
    }

    /**
     * @author duplicat
     * @reason NameTag tweaks
     */
    @Overwrite
    protected void renderLivingLabel(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_) {
        double d3 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);

        boolean nameTagToggled = Cloud.INSTANCE.modManager.getMod("NameTag").isToggled();
        int color = Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Font Color").getColor().getRGB();
        float alpha = nameTagToggled ? Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Opacity").getCurrentNumber() / 255f : 0.25f;
        float scale = nameTagToggled ? Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Size").getCurrentNumber() : 1;
        float yPos = nameTagToggled ? Cloud.INSTANCE.settingManager.getSettingByModAndName("NameTag", "Y Position").getCurrentNumber() - 2.5f : 0;

        if (d3 <= (double) (p_147906_9_ * p_147906_9_)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) p_147906_3_ + 0.0F, (float) p_147906_5_ + p_147906_1_.height + 0.5F + yPos, (float) p_147906_7_);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1 * scale, -f1 * scale, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.instance;
            byte b0 = 0;

            if (p_147906_2_.equals("deadmau5")) {
                b0 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int j = fontrenderer.getStringWidth(p_147906_2_) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, alpha);
            tessellator.addVertex(-j - 1, -1 + b0, 0.0D);
            tessellator.addVertex(-j - 1, 8 + b0, 0.0D);
            tessellator.addVertex(j + 1, 8 + b0, 0.0D);
            tessellator.addVertex(j + 1, -1 + b0, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, nameTagToggled ? color : 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, nameTagToggled ? color : -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
