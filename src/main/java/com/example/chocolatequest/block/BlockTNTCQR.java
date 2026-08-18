package com.example.chocolatequest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;

public class BlockTNTCQR extends TntBlock {

	public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");

	public BlockTNTCQR(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(HIDDEN, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(HIDDEN);
	}

	@Override
	public void wasExploded(Level pLevel, BlockPos pPos, Explosion pExplosion) {
		if (!pLevel.isClientSide) {
			PrimedTnt entityTNT = new PrimedTnt(pLevel, pPos.getX() + 0.5D, pPos.getY(), pPos.getZ() + 0.5D, pExplosion.getIndirectSourceEntity());
			int fuse = entityTNT.getFuse();
			entityTNT.setFuse((short) (pLevel.random.nextInt(fuse / 4) + fuse / 8));
			pLevel.addFreshEntity(entityTNT);
		}
	}

	@Override
	public void onCaughtFire(BlockState state, Level worldIn, BlockPos pos, @org.jetbrains.annotations.Nullable Direction face, @org.jetbrains.annotations.Nullable LivingEntity igniter) {
		if (!worldIn.isClientSide) {
			PrimedTnt entityTNT = new PrimedTnt(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
			worldIn.addFreshEntity(entityTNT);
			worldIn.playSound(null, entityTNT.getX(), entityTNT.getY(), entityTNT.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
			worldIn.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
		}
	}

}
