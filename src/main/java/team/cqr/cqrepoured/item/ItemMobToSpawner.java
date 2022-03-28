package team.cqr.cqrepoured.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
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
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (state.getBlock() == CQRBlocks.SPAWNER.get() && !worldIn.isClientSide) {
			TileEntitySpawner spawner = (TileEntitySpawner) worldIn.getBlockEntity(pos);
			spawner.forceTurnBackIntoEntity();
		}
		return super.mineBlock(stack, worldIn, state, pos, entityLiving);
	}

	private void spawnAdditions(World world, double x, double y, double z) {
		if (!world.isClientSide) {
			((ServerWorld) world).sendParticles(ParticleTypes.SMOKE, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			((ServerWorld) world).sendParticles(ParticleTypes.LAVA, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.8F, 0.6F + random.nextFloat() * 0.2F);
		}
	}

}
