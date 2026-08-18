package com.example.chocolatequest.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

/** Lightweight, thin Tesla arcs. Each segment is an independent line, so separate
 * bolts cannot accidentally become connected by the render buffer. */
public final class SimpleElectricRenderUtil {
    private SimpleElectricRenderUtil() {}

    public static void renderElectricLineBetween(PoseStack poseStack, MultiBufferSource buffer,
                                                  Vec3 start, Vec3 end, int seed) {
        Vec3 direction = end.subtract(start);
        double distance = direction.length();
        if (distance < 0.01D) return;

        RandomSource random = RandomSource.create(seed);
        int segments = Math.max(6, Math.min(18, (int)(distance * 1.65D)));
        double jitter = Math.min(0.32D, 0.07D + distance * 0.025D);
        Vec3 forward = direction.normalize();
        Vec3 reference = Math.abs(forward.y) > 0.92D ? new Vec3(1.0D, 0.0D, 0.0D) : new Vec3(0.0D, 1.0D, 0.0D);
        Vec3 side = forward.cross(reference).normalize();
        Vec3 up = side.cross(forward).normalize();

        Vec3[] points = new Vec3[segments + 1];
        points[0] = start;
        points[segments] = end;
        for (int i = 1; i < segments; i++) {
            double progress = i / (double)segments;
            // Ends stay attached to the coil/target while the middle flickers most.
            double envelope = Math.sin(Math.PI * progress);
            points[i] = start.add(direction.scale(progress))
                    .add(side.scale((random.nextDouble() - 0.5D) * 2.0D * jitter * envelope))
                    .add(up.scale((random.nextDouble() - 0.5D) * 2.0D * jitter * envelope));
        }

        VertexConsumer vertices = buffer.getBuffer(RenderType.lines());
        for (int i = 0; i < segments; i++) {
            Vec3 a = points[i];
            Vec3 b = points[i + 1];
            Vec3 normal = b.subtract(a).normalize();
            add(vertices, poseStack, a, normal, 82, 174, 255, 235);
            add(vertices, poseStack, b, normal, 180, 235, 255, 255);
        }
    }

    /** Short crawling arcs around an electrocuted body, similar to a Tesla coil discharge. */
    public static void renderElectricField(PoseStack poseStack, MultiBufferSource buffer,
                                           Vec3 center, double width, double height, int seed) {
        RandomSource random = RandomSource.create(seed);
        double radius = Math.max(0.32D, width * 0.68D);
        double halfHeight = Math.max(0.45D, height * 0.42D);
        for (int i = 0; i < 7; i++) {
            double angleA = random.nextDouble() * Math.PI * 2.0D;
            double angleB = angleA + (random.nextDouble() - 0.5D) * 1.7D;
            Vec3 a = center.add(Math.cos(angleA) * radius,
                    (random.nextDouble() - 0.5D) * halfHeight * 2.0D,
                    Math.sin(angleA) * radius);
            Vec3 b = center.add(Math.cos(angleB) * radius,
                    (random.nextDouble() - 0.5D) * halfHeight * 2.0D,
                    Math.sin(angleB) * radius);
            renderElectricLineBetween(poseStack, buffer, a, b, seed + i * 197);
        }
    }

    private static void add(VertexConsumer vertices, PoseStack poseStack, Vec3 point, Vec3 normal,
                            int red, int green, int blue, int alpha) {
        vertices.addVertex(poseStack.last().pose(), (float)point.x, (float)point.y, (float)point.z)
                .setColor(red, green, blue, alpha)
                .setNormal(poseStack.last(), (float)normal.x, (float)normal.y, (float)normal.z);
    }
}
