package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;

public class BlockTNTCQR extends TntBlock {

	/**
	 * To distinguish it from normal TNT, it has a boolean field that gets set to false. This field indicates whether or not
	 * it will show a special TNT texture, it gets set to false upon dungeon generation
	 */
	public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");

	public BlockTNTCQR() {
		super(Properties.copy(Blocks.TNT));
		this.registerDefaultState(super.defaultBlockState().setValue(HIDDEN, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(HIDDEN);
	}

	@Override
	public void wasExploded(Level pLevel, BlockPos pPos, Explosion pExplosion) {
		if (!pLevel.isClientSide) {
			EntityTNTPrimedCQR entityTNT = new EntityTNTPrimedCQR(pLevel, pPos.getX() + 0.5D, pPos.getY(), pPos.getZ() + 0.5D, pExplosion.getIndirectSourceEntity());
			entityTNT.setFuse((short) (pLevel.random.nextInt(entityTNT.getFuse() / 4) + entityTNT.getFuse() / 8));
			pLevel.addFreshEntity(entityTNT);
		}
	}

	@Override
	public void onCaughtFire(BlockState state, Level worldIn, BlockPos pos, Direction face, LivingEntity igniter) {
		if (!worldIn.isClientSide) {
			EntityTNTPrimedCQR entityTNT = new EntityTNTPrimedCQR(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
			worldIn.addFreshEntity(entityTNT);
			worldIn.playSound(null, entityTNT.getX(), entityTNT.getY(), entityTNT.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

}
