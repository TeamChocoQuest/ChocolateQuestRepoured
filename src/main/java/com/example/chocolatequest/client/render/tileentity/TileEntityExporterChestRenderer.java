package com.example.chocolatequest.client.render.tileentity;

import java.util.Arrays;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;
import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.block.BlockExporterChest;
import com.example.chocolatequest.block.entity.TileEntityExporterChest;

public class TileEntityExporterChestRenderer extends ChestRenderer<TileEntityExporterChest> {

    private static final float SCALE = 0.75F;
    private static final Vector3f[][] FACES = {
            quad(0.5F, 0.0F, 0.5F, Direction.EAST, Direction.SOUTH, SCALE),
            quad(0.5F, 0.875F, 0.5F, Direction.EAST, Direction.NORTH, SCALE),
            quad(0.5F, 0.4375F, 0.0625F, Direction.WEST, Direction.UP, SCALE),
            quad(0.5F, 0.4375F, 0.9375F, Direction.EAST, Direction.UP, SCALE),
            quad(0.0625F, 0.4375F, 0.5F, Direction.SOUTH, Direction.UP, SCALE),
            quad(0.9375F, 0.4375F, 0.5F, Direction.NORTH, Direction.UP, SCALE)
    };

    public TileEntityExporterChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TileEntityExporterChest pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        super.render(pBlockEntity, pPartialTicks, pMatrixStack, pBuffer, pCombinedLight, pCombinedOverlay);

        BlockState state = pBlockEntity.getBlockState();
        if (state.getBlock() instanceof BlockExporterChest) {
            ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/block/" + BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath() + ".png");
            VertexConsumer ivertexbuilder = pBuffer.getBuffer(RenderType.entityCutout(tex));

            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5D, 0.5D, 0.5D);
            pMatrixStack.mulPose(Axis.YN.rotationDegrees(state.getValue(BlockExporterChest.FACING).toYRot()));
            pMatrixStack.scale(1.0F + 1.0F / 128.0F, 1.0F + 1.0F / 128.0F, 1.0F + 1.0F / 128.0F);
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
            Arrays.stream(Direction.values()).forEach(face -> drawFace(pMatrixStack, ivertexbuilder, face, pCombinedLight, pCombinedOverlay));
            pMatrixStack.popPose();
        }
    }

    private void drawFace(PoseStack matrixStack, VertexConsumer vertexBuilder, Direction face, int light, int overlay) {
        Vector3f n = new Vector3f(face.getStepX(), face.getStepY(), face.getStepZ());
        n.mul(matrixStack.last().normal());

        Vector4f[] vecs = Arrays.stream(FACES[face.ordinal()]).map(v -> new Vector4f(v.x(), v.y(), v.z(), 1.0F)).toArray(Vector4f[]::new);
        Arrays.stream(vecs).forEach(v -> v.mul(matrixStack.last().pose()));

        vertexBuilder.addVertex(vecs[0].x(), vecs[0].y(), vecs[0].z()).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setOverlay(overlay).setLight(light).setNormal(matrixStack.last(), n.x(), n.y(), n.z());
        vertexBuilder.addVertex(vecs[1].x(), vecs[1].y(), vecs[1].z()).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setOverlay(overlay).setLight(light).setNormal(matrixStack.last(), n.x(), n.y(), n.z());
        vertexBuilder.addVertex(vecs[2].x(), vecs[2].y(), vecs[2].z()).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 0.0F).setOverlay(overlay).setLight(light).setNormal(matrixStack.last(), n.x(), n.y(), n.z());
        vertexBuilder.addVertex(vecs[3].x(), vecs[3].y(), vecs[3].z()).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0.0F, 0.0F).setOverlay(overlay).setLight(light).setNormal(matrixStack.last(), n.x(), n.y(), n.z());
    }

    private static Vector3f[] quad(float x, float y, float z, Direction axis1, Direction axis2, float scale) {
        Vector3f[] vecs = new Vector3f[] {
                new Vector3f(x, y, z),
                new Vector3f(x, y, z),
                new Vector3f(x, y, z),
                new Vector3f(x, y, z)
        };
        scale *= 0.5F;
        vecs[0].add(-axis1.getStepX() * scale, -axis1.getStepY() * scale, -axis1.getStepZ() * scale);
        vecs[0].add(-axis2.getStepX() * scale, -axis2.getStepY() * scale, -axis2.getStepZ() * scale);
        vecs[1].add(axis1.getStepX() * scale, axis1.getStepY() * scale, axis1.getStepZ() * scale);
        vecs[1].add(-axis2.getStepX() * scale, -axis2.getStepY() * scale, -axis2.getStepZ() * scale);
        vecs[2].add(axis1.getStepX() * scale, axis1.getStepY() * scale, axis1.getStepZ() * scale);
        vecs[2].add(axis2.getStepX() * scale, axis2.getStepY() * scale, axis2.getStepZ() * scale);
        vecs[3].add(-axis1.getStepX() * scale, -axis1.getStepY() * scale, -axis1.getStepZ() * scale);
        vecs[3].add(axis2.getStepX() * scale, axis2.getStepY() * scale, axis2.getStepZ() * scale);
        return vecs;
    }
}
