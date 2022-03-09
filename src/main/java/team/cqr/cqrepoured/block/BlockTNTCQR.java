package team.cqr.cqrepoured.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.TNTBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;

public class BlockTNTCQR extends TNTBlock {

	// DONE Idea: better visuals
	/*
	 * To distinguish it from normal tnt, it has a boolean field that gets set to false. This field indicates wether or not
	 * it will show a special tnt texture, it
	 * gets set to false upon dungeon generation
	 */
	public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");

	public BlockTNTCQR() {
		//Copied from Blocks.class
		super(AbstractBlock.Properties.of(Material.EXPLOSIVE).instabreak().sound(SoundType.GRASS));
		this.registerDefaultState(super.defaultBlockState().setValue(HIDDEN, false));
	}

	@Override
	public void wasExploded(World pLevel, BlockPos pPos, Explosion pExplosion) {
		if (!pLevel.isClientSide) {
			EntityTNTPrimedCQR tntentity = new EntityTNTPrimedCQR(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, pExplosion.getSourceMob());
	         tntentity.setFuse((short)(pLevel.random.nextInt(tntentity.getLife() / 4) + tntentity.getLife() / 8));
	         pLevel.addFreshEntity(tntentity);
	      }
	}
	
	@Override
	public void catchFire(BlockState state, World worldIn, BlockPos pos, Direction face, LivingEntity igniter) {
		if (worldIn.isClientSide) {
			return;
		}
		if (!state.getValue(UNSTABLE)) {
			return;
		}
		EntityTNTPrimedCQR entitytntprimed = new EntityTNTPrimedCQR(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
		worldIn.addFreshEntity(entitytntprimed);
		worldIn.playSound(null, entitytntprimed.getX(), entitytntprimed.getY(), entitytntprimed.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	public Item asItem() {
		return Item.BY_BLOCK.get(Blocks.TNT);
	}
	

	/*@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, EXPLODE, HIDDEN);
	}
	
	@Deprecated
	@Override
	public BlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(EXPLODE, (meta + 1) % 2 == 0).withProperty(HIDDEN, meta >= 2);
	}

	@Override
	public int getMetaFromState(BlockState state) {
		
		// 0: Hidden + explode = false
		// 1: Hidden = false, explode = true
		// 2: Hidden = true, explode = false
		// 3: Hidden + expldoe = true
		 
		return (state.getValue(HIDDEN) ? 2 : 0) + super.getMetaFromState(state);
	}*/
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(HIDDEN);
	}

}
