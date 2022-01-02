package team.cqr.cqrepoured.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.entity.PartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.SpawnerFactory;

public class ItemMobToSpawner extends Item {

	public ItemMobToSpawner() {
		this.setMaxStackSize(1);
	}

	/*
	 * @Override public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
	 * Block block =
	 * world.getBlockState(pos).getBlock(); return block != ModBlocks.SPAWNER && block != Blocks.MOB_SPAWNER; }
	 */

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (player.isCreative()) {
			if (!player.world.isRemote && !(entity instanceof PartEntity)) {
				SpawnerFactory.placeSpawner(new Entity[] { entity }, false, null, player.world, new BlockPos(entity));
				entity.setDead();
				for (Entity passenger : entity.getPassengers()) {
					passenger.setDead();
				}
				this.spawnAdditions(entity.world, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (state.getBlock() == CQRBlocks.SPAWNER && !worldIn.isRemote) {
			TileEntitySpawner spawner = (TileEntitySpawner) worldIn.getTileEntity(pos);
			spawner.forceTurnBackIntoEntity();
		}
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	private void spawnAdditions(World world, double x, double y, double z) {
		if (!world.isRemote) {
			((ServerWorld) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			((ServerWorld) world).spawnParticle(EnumParticleTypes.LAVA, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.8F, 0.6F + itemRand.nextFloat() * 0.2F);
		}
	}

}
