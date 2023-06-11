package team.cqr.cqrepoured.item;

import net.minecraft.block.BlockRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public class ItemSpikedGlove extends ItemLore
{
	public ItemSpikedGlove(Properties properties)
	{
		super(properties.durability(6000));
		//this.setMaxStackSize(1);
		// With this durability you should be able to climb 1200m in total
		//this.setMaxDamage(6000);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		if (entityIn instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) entityIn;
			if (entity.getMainHandItem().getItem() instanceof ItemSpikedGlove && entity.getMainHandItem().getItem() instanceof ItemSpikedGlove) {
				// We actually have two bear hands
				if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isSpectator()) {
					return;
				}
				if (entity.horizontalCollision) {
					if (worldIn.isClientSide) {
						if (entity.zza > 0) {
							double vY = 0.2D;

							int effLvlMain = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, entity.getMainHandItem());
							int effLvlOff = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, entity.getOffhandItem());
							if (effLvlMain > 0 && effLvlOff > 0) {
								vY += 0.1D * (((0.5D * effLvlMain) + (0.5D * effLvlOff)) / 2);
							}

							entity.setDeltaMovement(entity.getDeltaMovement().x, vY, entity.getDeltaMovement().z);
							//entity.motionY = vY;

							this.createClimbingParticles(entity, worldIn);
						} else if (entity.isCrouching())
						{
							entity.setDeltaMovement(entity.getDeltaMovement().x, 0.0D, entity.getDeltaMovement().z);
							//entity.motionY = 0.0D;
						} else {
							entity.setDeltaMovement(entity.getDeltaMovement().x, -0.2D, entity.getDeltaMovement().z);
							//entity.motionY = -0.2D;
						}
					} else {
						entity.getMainHandItem().hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(Hand.MAIN_HAND));
						entity.getOffhandItem().hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(Hand.MAIN_HAND));
					}

					entity.setOnGround(true);
				}
				entity.fallDistance = 0F;
			}
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return (enchantment == Enchantments.BLOCK_EFFICIENCY || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING);
	}

	private void createClimbingParticles(LivingEntity player, World world) {
		int i = (int) player.position().x;
		int j = MathHelper.floor(player.blockPosition().getY());
		int k = (int) player.position().z;

		int direction = MathHelper.floor((player.yRot * 4.0F / 360.0F) + 0.5D) & 3;

		if (direction == 0) // south
		{
			if (k > 0) {
				k += 1;
			}

			if (i < 0) {
				i -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), player.position().x + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, (player.position().z + 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							//-player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D);
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
				}
			}
		}

		if (direction == 1) // west
		{
			if (i > 0) {
				i -= 1;
			}

			if (k < 0) {
				k -= 1;
			}

			if (i < 0) {
				i -= 2;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), (player.position().x - 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, player.position().z + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
				}
			}
		}

		if (direction == 2) // north
		{
			if (i < 0) {
				i -= 1;
			}

			if (k > 0) {
				k -= 1;
			}

			if ((i > 0 && k < 0) || (i < 0 && k < 0)) {
				k -= 2;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), player.position().x + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, (player.position().z - 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
				}
			}
		}

		if (direction == 3) // east
		{
			if (i > 0) {
				i += 1;
			}

			if (k < 0) {
				k -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), (player.position().x + 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(), player.getBoundingBox().minY + 0.1D, player.position().z + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
				}
			}
		}
	}

}
