package team.cqr.cqrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;

public class BlockInfoBanner extends BlockInfo {

	public BlockInfoBanner(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoBanner(BlockPos pos) {
		super(pos);
	}

	public BlockInfoBanner(int x, int y, int z, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		super(x, y, z, blockstate, tileentityData);
	}

	public BlockInfoBanner(BlockPos pos, IBlockState blockstate, @Nullable NBTTagCompound tileentityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), blockstate, tileentityData);
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		super.generateAt(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, pos);

		if (dungeonMob.getBanner() != null) {
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityBanner) {
				((TileEntityBanner) tileentity).setItemValues(dungeonMob.getBanner().getBanner(), true);
			} else {
				CQRMain.logger.warn("Failed to place banner at {}", pos);
			}
		}
	}

	@Override
	public byte getId() {
		return BANNER_INFO_ID;
	}

	@Deprecated
	public BlockInfoBanner(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
	}

}
