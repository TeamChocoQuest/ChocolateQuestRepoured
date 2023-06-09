package team.cqr.cqrepoured.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.entity.PartEntity;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.SpawnerFactory;

public class ItemMobToSpawner extends Item {

	public ItemMobToSpawner(Properties properties) {
		super(properties.stacksTo(1));
	}

	/*
	 * @Override public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
	 * Block block =
	 * world.getBlockState(pos).getBlock(); return block != ModBlocks.SPAWNER && block != Blocks.MOB_SPAWNER; }
	 */

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (player.isCreative()) {
			if (!player.level.isClientSide && !(entity instanceof PartEntity)) {
				SpawnerFactory.placeSpawner(new Entity[] { entity }, false, null, player.level, entity.blockPosition());
				entity.remove();
				for (Entity passenger : entity.getPassengers()) {
					passenger.remove();
				}
				this.spawnAdditions(entity.level, entity.getX(), entity.getY() + entity.getBbHeight() * 0.5D, entity.getZ());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (state.getBlock() == CQRBlocks.SPAWNER.get() && !worldIn.isClientSide) {
			TileEntitySpawner spawner = (TileEntitySpawner) worldIn.getBlockEntity(pos);
			spawner.forceTurnBackIntoEntity();
		}
		return super.mineBlock(stack, worldIn, state, pos, entityLiving);
	}

	private void spawnAdditions(Level world, double x, double y, double z) {
		if (!world.isClientSide) {
			((ServerLevel) world).sendParticles(ParticleTypes.SMOKE, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			((ServerLevel) world).sendParticles(ParticleTypes.LAVA, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 0.8F, 0.6F + random.nextFloat() * 0.2F);
		}
	}

}
