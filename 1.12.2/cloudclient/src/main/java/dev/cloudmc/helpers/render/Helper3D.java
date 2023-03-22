/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers.render;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.FreelookMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Helper3D {

    public static double calculateCameraDistance(double d0, double d1, double d2, double d3) {
        float f1 = FreelookMod.cameraYaw;
        float f2 = FreelookMod.cameraPitch;

        if (Cloud.INSTANCE.mc.gameSettings.thirdPersonView == 2) {
            f2 += 180.0F;
        }

        double d4 = (double) (MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
        double d5 = (double) (-MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
        double d6 = (double) (-MathHelper.sin(f2 * 0.017453292F)) * d3;

        for (int i = 0; i < 8; ++i) {
            float f3 = (float) ((i & 1) * 2 - 1);
            float f4 = (float) ((i >> 1 & 1) * 2 - 1);
            float f5 = (float) ((i >> 2 & 1) * 2 - 1);
            f3 = f3 * 0.1F;
            f4 = f4 * 0.1F;
            f5 = f5 * 0.1F;
            RayTraceResult raytraceresult = Cloud.INSTANCE.mc.world.rayTraceBlocks(
                    new Vec3d(d0 + (double) f3, d1 + (double) f4, d2 + (double) f5),
                    new Vec3d(d0 - d4 + (double) f3 + (double) f5, d1 - d6 + (double) f4, d2 - d5 + (double) f5)
            );

            if (raytraceresult != null) {
                double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));

                if (d7 < d3) {
                    d3 = d7;
                }
            }
        }
        return d3;
    }
}
