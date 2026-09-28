package team.cqr.cqrepoured.client.world.structure.preview;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

class StructureBlockAccess implements IBlockAccess {

	private final CQStructure structure;
	private final List<PreparablePosInfo> blocks;
	private final BlockPos size;

	public StructureBlockAccess(CQStructure structure) {
		this.structure = structure;
		this.blocks = this.structure.getBlockInfoList();
		this.size = this.structure.getSize();
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return null;
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		return 15 << 20;
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		if (pos.getX() < 0 || pos.getX() >= this.size.getX()) return Blocks.AIR.getDefaultState();
		if (pos.getY() < 0 || pos.getY() >= this.size.getY()) return Blocks.AIR.getDefaultState();
		if (pos.getZ() < 0 || pos.getZ() >= this.size.getZ()) return Blocks.AIR.getDefaultState();

		return this.blocks.get((pos.getX() * this.size.getY() + pos.getY()) * this.size.getZ() + pos.getZ()).getRenderState();
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return this.getBlockState(pos).getBlock() == Blocks.AIR;
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		return Biomes.PLAINS;
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return 0;
	}

	@Override
	public WorldType getWorldType() {
		return WorldType.DEFAULT;
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return this.getBlockState(pos).isSideSolid(this, pos, side);
	}

}
