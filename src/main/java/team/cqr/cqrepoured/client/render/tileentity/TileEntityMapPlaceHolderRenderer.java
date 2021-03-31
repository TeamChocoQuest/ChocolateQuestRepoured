package team.cqr.cqrepoured.client.render.tileentity;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.util.Reference;

@SideOnly(Side.CLIENT)
public class TileEntityMapPlaceHolderRenderer extends FastTESR<TileEntityMap> {

	@Override
	public void renderTileEntityFast(TileEntityMap te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
		if (!te.hasWorld()) {
			return;
		}

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		if (!state.getProperties().containsKey(BlockHorizontal.FACING)) {
			return;
		}

		EnumFacing facing = state.getValue(BlockHorizontal.FACING);
		int lightmapCoords = state.getPackedLightmapCoords(te.getWorld(), te.getPos());
		int skyLight = lightmapCoords >> 16;
		int blockLight = lightmapCoords & 0xFFFF;
		EnumFacing orientation = te.getOrientation();

		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/blocks/map.png"));
		buffer.setTranslation(x, y, z);
		float f = 4.0F / 128.0F;
		float f1 = 124.0F / 128.0F;
		float f2 = 9.0F / 128.0F;
		float f3 = 119.0F / 128.0F;
		double[] texCoords = { 0.0D, 1.0D, 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D };
		this.rotateTexCoords(texCoords, orientation);
		switch (facing) {
		case NORTH:
			buffer.pos(f1, f, f3).color(255, 255, 255, 255).tex(texCoords[0], texCoords[1]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f, f, f3).color(255, 255, 255, 255).tex(texCoords[2], texCoords[3]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f, f1, f3).color(255, 255, 255, 255).tex(texCoords[4], texCoords[5]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f1, f1, f3).color(255, 255, 255, 255).tex(texCoords[6], texCoords[7]).lightmap(skyLight, blockLight).endVertex();
			break;
		case SOUTH:
			buffer.pos(f, f, f2).color(255, 255, 255, 255).tex(texCoords[0], texCoords[1]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f1, f, f2).color(255, 255, 255, 255).tex(texCoords[2], texCoords[3]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f1, f1, f2).color(255, 255, 255, 255).tex(texCoords[4], texCoords[5]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f, f1, f2).color(255, 255, 255, 255).tex(texCoords[6], texCoords[7]).lightmap(skyLight, blockLight).endVertex();
			break;
		case EAST:
			buffer.pos(f2, f, f1).color(255, 255, 255, 255).tex(texCoords[0], texCoords[1]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f2, f, f).color(255, 255, 255, 255).tex(texCoords[2], texCoords[3]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f2, f1, f).color(255, 255, 255, 255).tex(texCoords[4], texCoords[5]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f2, f1, f1).color(255, 255, 255, 255).tex(texCoords[6], texCoords[7]).lightmap(skyLight, blockLight).endVertex();
			break;
		case WEST:
			buffer.pos(f3, f, f).color(255, 255, 255, 255).tex(texCoords[0], texCoords[1]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f3, f, f1).color(255, 255, 255, 255).tex(texCoords[2], texCoords[3]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f3, f1, f1).color(255, 255, 255, 255).tex(texCoords[4], texCoords[5]).lightmap(skyLight, blockLight).endVertex();
			buffer.pos(f3, f1, f).color(255, 255, 255, 255).tex(texCoords[6], texCoords[7]).lightmap(skyLight, blockLight).endVertex();
			break;
		default:
			break;
		}
		buffer.setTranslation(0, 0, 0);
	}

	private void rotateTexCoords(double[] texCoords, EnumFacing facing) {
		int rotationCount = (facing.getHorizontalIndex() + 2) % 4;
		for (int i = rotationCount; i > 0; i--) {
			double d = texCoords[texCoords.length - 2];
			double d1 = texCoords[texCoords.length - 1];
			System.arraycopy(texCoords, 0, texCoords, 2, texCoords.length - 2);
			texCoords[0] = d;
			texCoords[1] = d1;
		}
	}

}
