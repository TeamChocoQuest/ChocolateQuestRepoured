package team.cqr.cqrepoured.structuregen.structurefile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;

public class BlockInfoEmpty extends AbstractBlockInfo {

	public BlockInfoEmpty(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoEmpty(BlockPos pos) {
		super(pos);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {

	}

	@Override
	public byte getId() {
		return EMPTY_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

}
