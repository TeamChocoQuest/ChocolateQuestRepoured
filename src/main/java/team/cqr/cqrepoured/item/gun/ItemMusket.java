package team.cqr.cqrepoured.item.gun;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.ItemLore;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMusket extends ItemRevolver {

	public ItemMusket(Properties properties)
	{
		super(properties.durability(300));
		//this.setMaxDamage(300);
		//this.setMaxStackSize(1);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("7.5 " + new TranslationTextComponent("description.bullet_damage.name")).withStyle(TextFormatting.BLUE));
		tooltip.add(new StringTextComponent("-60 " + new TranslationTextComponent("description.fire_rate.name")).withStyle(TextFormatting.RED));
		tooltip.add(new StringTextComponent("-10" + "% " + new TranslationTextComponent("description.accuracy.name")).withStyle(TextFormatting.RED));

		ItemLore.addHoverTextLogic(tooltip, flagIn, "gun");
	}

	@Override
	protected float getRecoil() {
		return 0.5F * super.getRecoil();
	}

	@Override
	public void shoot(ItemStack stack, World worldIn, PlayerEntity player) {
		boolean flag = player.abilities.instabuild;
		ItemStack itemstack = this.findAmmo(player);

		if (!itemstack.isEmpty() || flag) {
			if (!worldIn.isClientSide) {
				if (flag && itemstack.isEmpty()) {
					ProjectileBullet bulletE = new ProjectileBullet(player, worldIn, 1);
					bulletE.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.5F, 2F);
					player.getCooldowns().addCooldown(stack.getItem(), 30);
					worldIn.addFreshEntity(bulletE);
				} else {
					ProjectileBullet bulletE = new ProjectileBullet(player, worldIn, this.getBulletType(itemstack));
					bulletE.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.5F, 2F);
					player.getCooldowns().addCooldown(stack.getItem(), 30);
					worldIn.addFreshEntity(bulletE);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
				}
			}

			worldIn.playLocalSound(player.position().x, player.position().y + player.getEyeHeight(), player.position().z, this.getShootSound(), SoundCategory.MASTER, 1.0F, 0.9F + random.nextFloat() * 0.2F, false);
			player.xRot -= worldIn.random.nextFloat() * 10;

			if (!flag) {
				itemstack.shrink(1);

				if (itemstack.isEmpty()) {
					player.inventory.removeItem(itemstack);
				}
			}
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.MUSKET_SHOOT;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		LivingEntity entityLiving = (LivingEntity) entityIn;
		ItemStack offhand = entityLiving.getMainHandItem();
		if (!offhand.isEmpty()) {
			entityLiving.addEffect(new EffectInstance(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
