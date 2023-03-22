package dev.cloudmc.gui.titlescreen;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.glu.Project;

@SideOnly(Side.CLIENT)
public class Panorama extends GuiScreen {
    private float panoramaTimer;
    private ResourceLocation backgroundTexture;
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[]{
            new ResourceLocation(Cloud.modID, "panorama/panorama_0.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_1.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_2.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_3.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_4.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_5.png")
    };

    public void updateScreen() {
        panoramaTimer++;
    }

    public void initGui() {
        DynamicTexture viewportTexture = new DynamicTexture(256, 256);
        backgroundTexture = mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
    }

    private void drawPanorama() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        for (int j = 0; j < 64; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % 8) / 8.0F - 0.5F) / 64.0F;
            float f1 = ((float) (j / 8) / 8.0F - 0.5F) / 64.0F;
            GlStateManager.translate(f, f1, 0.0F);
            GlStateManager.rotate(MathHelper.sin(panoramaTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-panoramaTimer * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();

                switch (k) {
                    case 1:
                        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case 2:
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case 3:
                        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case 4:
                        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                        break;
                    case 5:
                        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                        break;
                }

                mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[k]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                bufferbuilder.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox() {
        mc.getTextureManager().bindTexture(backgroundTexture);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();

        for (int j = 0; j < 3; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = width;
            int l = height;
            float f1 = (float) (j - 1) / 256.0F;
            bufferbuilder.pos(k, l, zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(k, 0.0D, zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(0.0D, l, zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox() {
        mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        drawPanorama();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        float f = 120.0F / (float) (Math.max(width, height));
        float f1 = (float) height * f / 256.0F;
        float f2 = (float) width * f / 256.0F;
        int i = width;
        int j = height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, j, zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(i, j, zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(i, 0.0D, zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        panoramaTimer += partialTicks;
        GlStateManager.disableAlpha();
        renderSkybox();
        GlStateManager.enableAlpha();
        Helper2D.drawRectangle(0, 0, width, height, 0x70000000);
    }
}
