package team.cqr.cqrepoured.block;

import net.minecraft.block.TNTBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import team.cqr.cqrepoured.entity.misc.EntityTNTPrimedCQR;

public class BlockTNTCQR extends TNTBlock {

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
	public void wasExploded(World pLevel, BlockPos pPos, Explosion pExplosion) {
		if (!pLevel.isClientSide) {
			EntityTNTPrimedCQR entityTNT = new EntityTNTPrimedCQR(pLevel, pPos.getX() + 0.5D, pPos.getY(), pPos.getZ() + 0.5D, pExplosion.getSourceMob());
			entityTNT.setFuse((short) (pLevel.random.nextInt(entityTNT.getLife() / 4) + entityTNT.getLife() / 8));
			pLevel.addFreshEntity(entityTNT);
		}
	}

	@Override
	public void catchFire(BlockState state, World worldIn, BlockPos pos, Direction face, LivingEntity igniter) {
		if (!worldIn.isClientSide) {
			EntityTNTPrimedCQR entityTNT = new EntityTNTPrimedCQR(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
			worldIn.addFreshEntity(entityTNT);
			worldIn.playSound(null, entityTNT.getX(), entityTNT.getY(), entityTNT.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

}
