/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.FreelookMod;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Helper3D {

    /**
     * Draws a filled box over a given Axis Aligned Bounding Box in the world
     *
     * @param boundingBox The Axis Aligned Bounding Box in the world
     */

    public static void drawFilledBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        tessellator.draw();
    }

    public static double calculateCameraDistance(double d0, double d1, double d2, double d7) {
        float f6 = FreelookMod.cameraYaw;
        float f2 = FreelookMod.cameraPitch;

        if (Cloud.INSTANCE.mc.gameSettings.thirdPersonView == 2) {
            f2 += 180.0F;
        }

        double d3 = (double) (MathHelper.sin(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
        double d4 = (double) (-MathHelper.cos(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f2 / 180.0F * (float) Math.PI)) * d7;
        double d5 = (double) (-MathHelper.sin(f2 / 180.0F * (float) Math.PI)) * d7;

        for (int k = 0; k < 8; ++k) {
            float f3 = (float) ((k & 1) * 2 - 1);
            float f4 = (float) ((k >> 1 & 1) * 2 - 1);
            float f5 = (float) ((k >> 2 & 1) * 2 - 1);
            f3 *= 0.1F;
            f4 *= 0.1F;
            f5 *= 0.1F;
            MovingObjectPosition movingobjectposition = Cloud.INSTANCE.mc.theWorld.rayTraceBlocks(Vec3.createVectorHelper(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5), Vec3.createVectorHelper(d0 - d3 + (double) f3 + (double) f5, d1 - d5 + (double) f4, d2 - d4 + (double) f5));

            if (movingobjectposition != null) {
                double d6 = movingobjectposition.hitVec.distanceTo(Vec3.createVectorHelper(d0, d1, d2));

                if (d6 < d7) {
                    d7 = d6;
                }
            }
        }
        return d7;
    }
}