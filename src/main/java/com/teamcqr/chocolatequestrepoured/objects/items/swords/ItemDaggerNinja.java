package com.teamcqr.chocolatequestrepoured.objects.items.swords;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDaggerNinja extends ItemDagger {

	public ItemDaggerNinja(ToolMaterial material, int cooldown) {
		super(material, cooldown);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.isSneaking()) {
			worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), 30);

			for (int i = 0; i < 6; i++) {
				worldIn.spawnParticle(EnumParticleTypes.PORTAL, playerIn.posX + itemRand.nextFloat() - 0.5D, playerIn.posY + itemRand.nextFloat() - 0.5D, playerIn.posZ + itemRand.nextFloat() - 0.5D, itemRand.nextFloat() - 0.5F,
						itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F);
			}

			double x = -Math.sin(Math.toRadians(playerIn.rotationYaw));
			double z = Math.cos(Math.toRadians(playerIn.rotationYaw));
			double y = -Math.sin(Math.toRadians(playerIn.rotationPitch));
			x *= (1.0D - Math.abs(y));
			z *= (1.0D - Math.abs(y));
			int dist = 4;

			BlockPos pos = new BlockPos(playerIn.posX + x * dist, playerIn.posY + y * dist + 1, playerIn.posZ + z * dist);

			if (worldIn.getBlockState(pos).getBlock().isPassable(worldIn, pos) && pos.getY() > 0) {
				playerIn.setPosition(playerIn.posX + x * dist, playerIn.posY + y * dist + 1, playerIn.posZ + z * dist);
				playerIn.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 40, 5, false, false));
			} else {
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}

			stack.damageItem(1, playerIn);

			for (int i = 0; i < 6; i++) {
				worldIn.spawnParticle(EnumParticleTypes.PORTAL, playerIn.posX + itemRand.nextFloat() - 0.5D, playerIn.posY + itemRand.nextFloat() - 0.5D, playerIn.posZ + itemRand.nextFloat() - 0.5D, itemRand.nextFloat() - 0.5F,
						itemRand.nextFloat() - 0.5F, itemRand.nextFloat() - 0.5F);
			}
		} else {
			super.onItemRightClick(worldIn, playerIn, handIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + "200% " + I18n.format("description.rear_damage.name"));

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.ninja_dagger.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
