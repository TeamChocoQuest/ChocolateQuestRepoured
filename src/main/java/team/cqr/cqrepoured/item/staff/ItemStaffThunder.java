package team.cqr.cqrepoured.item.staff;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffThunder extends ItemLore implements IRangedWeapon {

	public ItemStaffThunder() {
		this.setMaxDamage(2048);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (this.isNotAirBlock(worldIn, playerIn)) {
			if (!worldIn.isRemote) {
				playerIn.swingArm(handIn);
				this.spawnLightningBolt(playerIn, worldIn);
				stack.damageItem(1, playerIn);
				playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
			}
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.FAIL, stack);
	}

	public void spawnLightningBolt(PlayerEntity player, World worldIn) {
		if (!worldIn.isRemote) {
			Vector3d start = player.getPositionEyes(1.0F);
			Vector3d end = start.add(player.getLookVec().scale(20.0D));
			RayTraceResult result = worldIn.rayTraceBlocks(start, end);

			if (result != null) {
				EntityColoredLightningBolt entity = new EntityColoredLightningBolt(worldIn, result.hitVec.x, result.hitVec.y, result.hitVec.z, true, false);
				worldIn.spawnEntity(entity);
			}
		}
	}

	public boolean isNotAirBlock(World worldIn, PlayerEntity player) {
		Vector3d start = player.getPositionEyes(1.0F);
		Vector3d end = start.add(player.getLookVec().scale(20.0D));
		RayTraceResult result = worldIn.rayTraceBlocks(start, end);

		return result != null && !worldIn.isAirBlock(result.getBlockPos());
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		Vector3d v = target.position().subtract(shooter.position());
		Vector3d pos = target.position();
		if (v.length() > 20) {
			v = v.normalize();
			v = v.scale(20D);
			pos = shooter.position().add(v);
		}
		EntityColoredLightningBolt entity = new EntityColoredLightningBolt(worldIn, pos.x, pos.y, pos.z, true, false);
		worldIn.spawnEntity(entity);
	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.MAGIC;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 80;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
