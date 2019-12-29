package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffThunder extends Item implements IRangedWeapon{

	public ItemStaffThunder() {
		setMaxDamage(2048);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (isNotAirBlock(worldIn)) {
			playerIn.swingArm(handIn);
			spawnLightningBolt(playerIn, worldIn);
			stack.damageItem(1, playerIn);
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}

	public void spawnLightningBolt(EntityPlayer player, World worldIn) {
		RayTraceResult result = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(20D, 1.0F);

		if (result != null) {
			EntityLightningBolt entity = new EntityLightningBolt(worldIn, result.getBlockPos().getX(),
					result.getBlockPos().getY(), result.getBlockPos().getZ(), false);
			worldIn.spawnEntity(entity);
		}
	}

	public boolean isNotAirBlock(World worldIn) {
		RayTraceResult result = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(20D, 1.0F);

		if (result != null) {
			BlockPos pos = new BlockPos(result.getBlockPos().getX(), result.getBlockPos().getY(),
					result.getBlockPos().getZ());

			if (!worldIn.isAirBlock(pos)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_thunder.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void shoot(World worldIn, EntityLivingBase shooter, Entity target, EnumHand handIn) {
		Vec3d v = target.getPositionVector().subtract(shooter.getPositionVector());
		Vec3d pos = target.getPositionVector();
		if(v.lengthVector() > 20) {
			v = v.normalize();
			v = v.scale(20D);
			pos = shooter.getPositionVector().add(v);
		}
		EntityLightningBolt entity = new EntityLightningBolt(worldIn, pos.x,
				pos.y, pos.z, false);
		worldIn.spawnEntity(entity);
	}
	@Override
	public SoundEvent getShootSound() {
		return ModSounds.MAGIC;
	}

}