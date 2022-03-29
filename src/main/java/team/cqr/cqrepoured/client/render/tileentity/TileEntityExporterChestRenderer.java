package team.cqr.cqrepoured.client.render.tileentity;

import java.util.Arrays;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.client.CQRepouredClient;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public class TileEntityExporterChestRenderer extends ChestTileEntityRenderer<TileEntityExporterChest> {

	private static final float SCALE = 0.75F;
	private static final Vector3f[][] FACES = {
			quad(0.5F, 0.0F, 0.5F, Direction.EAST, Direction.SOUTH, SCALE),
			quad(0.5F, 0.875F, 0.5F, Direction.EAST, Direction.NORTH, SCALE),
			quad(0.5F, 0.4375F, 0.0625F, Direction.WEST, Direction.UP, SCALE),
			quad(0.5F, 0.4375F, 0.9375F, Direction.EAST, Direction.UP, SCALE),
			quad(0.0625F, 0.4375F, 0.5F, Direction.SOUTH, Direction.UP, SCALE),
			quad(0.9375F, 0.4375F, 0.5F, Direction.NORTH, Direction.UP, SCALE)
	};

	public TileEntityExporterChestRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
	}

	@Override
	public void render(TileEntityExporterChest pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
		super.render(pBlockEntity, pPartialTicks, pMatrixStack, pBuffer, pCombinedLight, pCombinedOverlay);

		BlockState state = CQRepouredClient.getBlockEntityBlockState(pBlockEntity);
		if (state.getBlock() instanceof BlockExporterChest) {
			ResourceLocation tex = new ResourceLocation(CQRMain.MODID, "textures/block/" + state.getBlock().getRegistryName().getPath() + ".png");
			IVertexBuilder ivertexbuilder = pBuffer.getBuffer(RenderType.entityCutout(tex));

			pMatrixStack.pushPose();
			pMatrixStack.translate(0.5D, 0.5D, 0.5D);
			pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(state.getValue(BlockExporterChest.FACING).toYRot()));
			pMatrixStack.scale(1.0F + 1.0F / 128.0F, 1.0F + 1.0F / 128.0F, 1.0F + 1.0F / 128.0F);
			pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
			Arrays.stream(Direction.values()).forEach(face -> drawFace(pMatrixStack, ivertexbuilder, face, pCombinedLight, pCombinedOverlay));
			pMatrixStack.popPose();
		}
	}

	private void drawFace(MatrixStack matrixStack, IVertexBuilder vertexBuilder, Direction face, int light, int overlay) {
		Vector3f n = new Vector3f(face.getStepX(), face.getStepY(), face.getStepZ());
		n.transform(matrixStack.last().normal());

		Vector4f[] vecs = Arrays.stream(FACES[face.ordinal()]).map(Vector4f::new).toArray(Vector4f[]::new);
		Arrays.stream(vecs).forEach(v -> v.transform(matrixStack.last().pose()));

		vertexBuilder.vertex(vecs[0].x(), vecs[0].y(), vecs[0].z(), 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, overlay, light, n.x(), n.y(), n.z());
		vertexBuilder.vertex(vecs[1].x(), vecs[1].y(), vecs[1].z(), 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, overlay, light, n.x(), n.y(), n.z());
		vertexBuilder.vertex(vecs[2].x(), vecs[2].y(), vecs[2].z(), 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, overlay, light, n.x(), n.y(), n.z());
		vertexBuilder.vertex(vecs[3].x(), vecs[3].y(), vecs[3].z(), 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, overlay, light, n.x(), n.y(), n.z());
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
