package team.cqr.cqrepoured.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.entity.boss.EntityCQRLich;

public class BlockPhylactery extends Block {

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

	public BlockPhylactery(Material materialIn) {
		super(materialIn);
		this.setHardness(0.5F);
		this.setLightOpacity(8);
		this.setLightLevel(1.0F);
		this.setSoundType(SoundType.GLASS);
		this.setTickRandomly(true);
	}

	public BlockPhylactery(Material blockMaterialIn, MaterialColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		this.setHardness(0.5F);
		this.setLightOpacity(8);
		this.setLightLevel(1.5F);
		this.setSoundType(SoundType.GLASS);
		this.setTickRandomly(true);
	}

	@Override
	public void onPlayerDestroy(World worldIn, BlockPos pos, BlockState state) {
		super.onPlayerDestroy(worldIn, pos, state);
		worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true);
	}

	@Deprecated
	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Deprecated
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
		super.randomTick(worldIn, pos, state, rand);
		AxisAlignedBB aabb = new AxisAlignedBB(pos.add(3, 2, 3), pos.add(-3, -2, -3));
		List<EntityCQRLich> lichesInRange = worldIn.getEntitiesWithinAABB(EntityCQRLich.class, aabb);
		if (!lichesInRange.isEmpty()) {
			int i = 0;
			while (i < lichesInRange.size()) {
				EntityCQRLich lich = lichesInRange.get(i);
				if (lich != null && !lich.isDead) {
					if (!lich.hasPhylactery()) {
						lich.setCurrentPhylacteryBlock(pos);
						i = lichesInRange.size();
					} else {
						i++;
					}
				}
			}
		}
	}

}
