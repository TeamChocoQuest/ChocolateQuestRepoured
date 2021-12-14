package team.cqr.cqrepoured.block;

import java.util.Random;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;

public class BlockTNTCQR extends BlockTNT {

	// DONE Idea: better visuals
	/*
	 * To distinguish it from normal tnt, it has a boolean field that gets set to false. This field indicates wether or not
	 * it will show a special tnt texture, it
	 * gets set to false upon dungeon generation
	 */
	public static final PropertyBool HIDDEN = PropertyBool.create("hidden");

	public BlockTNTCQR() {
		super();
		this.setDefaultState(super.getDefaultState().withProperty(HIDDEN, false));
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		// Copied from vanilla
		if (!worldIn.isRemote) {
			EntityTNTPrimedCQR entitytntprimed = new EntityTNTPrimedCQR(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, explosionIn.getExplosivePlacedBy());
			entitytntprimed.setFuse((short) (worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
			worldIn.spawnEntity(entitytntprimed);
		}
	}

	@Override
	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (worldIn.isRemote) {
			return;
		}
		if (!state.getValue(EXPLODE)) {
			return;
		}
		EntityTNTPrimedCQR entitytntprimed = new EntityTNTPrimedCQR(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
		worldIn.spawnEntity(entitytntprimed);
		worldIn.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.TNT);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, EXPLODE, HIDDEN);
	}

	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(EXPLODE, (meta + 1) % 2 == 0).withProperty(HIDDEN, meta >= 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		/*
		 * 0: Hidden + explode = false
		 * 1: Hidden = false, explode = true
		 * 2: Hidden = true, explode = false
		 * 3: Hidden + expldoe = true
		 */
		return (state.getValue(HIDDEN) ? 2 : 0) + super.getMetaFromState(state);
	}

}
