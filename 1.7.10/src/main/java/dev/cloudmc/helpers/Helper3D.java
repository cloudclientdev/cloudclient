/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.helpers;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;

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
}