package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpikedGlove extends Item {

	public ItemSpikedGlove() {
		super();
		setMaxStackSize(1);
		//With this durability you should be able to climb 1200m in total
		setMaxDamage(6000);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.spiked_glove.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if(entityIn instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			if(entity.getHeldItemMainhand().getItem() instanceof ItemSpikedGlove && entity.getHeldItemOffhand().getItem() instanceof ItemSpikedGlove) {
				//We actually have two bear hands
				if(entity instanceof EntityPlayer && ((EntityPlayer)entity).isSpectator()) {
					return;
				}
				if (entity.collidedHorizontally) {
					if (worldIn.isRemote) {
						if (entity.moveForward > 0) {
							double vY = 0.2D;
							
							int effLvlMain = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, entity.getHeldItemMainhand());
							int effLvlOff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, entity.getHeldItemOffhand());
							if(effLvlMain > 0 && effLvlOff > 0) {
								vY += 0.1D* (((0.5D * (double)effLvlMain) + (0.5D * (double)effLvlOff)) / (double)2);
							}
							
							entity.motionY = vY;
							entity.getHeldItemMainhand().damageItem(1, entity);
							entity.getHeldItemOffhand().damageItem(1, entity);
							this.createClimbingParticles(entity, worldIn);
						} else if (entity.isSneaking()) {
							entity.motionY = 0.0D;
						} else {
							entity.motionY = -0.2D;
						}
					}

					entity.onGround = true;
				}
				entity.fallDistance = 0F;
			}
		}
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return (enchantment == Enchantments.EFFICIENCY || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING);
	}
	
	private void createClimbingParticles(EntityLivingBase player, World world) {
		int i = (int) player.posX;
		int j = MathHelper.floor(player.getPosition().getY());
		int k = (int) player.posZ;

		int direction = MathHelper.floor((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (direction == 0) // south
		{
			if (k > 0) {
				k += 1;
			}

			if (i < 0) {
				i -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			IBlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
					world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, player.posX + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, player.getEntityBoundingBox().minY + 0.1D,
							(player.posZ + 0.3) + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D, Block.getStateId(iblockstate));
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
			IBlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
					world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, (player.posX - 0.3) + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, player.getEntityBoundingBox().minY + 0.1D,
							player.posZ + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D, Block.getStateId(iblockstate));
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
			IBlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
					world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, player.posX + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, player.getEntityBoundingBox().minY + 0.1D,
							(player.posZ - 0.3) + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D, Block.getStateId(iblockstate));
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
			IBlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
					world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, (player.posX + 0.3) + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, player.getEntityBoundingBox().minY + 0.1D,
							player.posZ + ((double) itemRand.nextFloat() - 0.5D) * (double) player.width, -player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D, Block.getStateId(iblockstate));
				}
			}
		}
	}
	
}
