package team.cqr.cqrepoured.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet.EBulletType;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.ItemLore;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMusket extends ItemRevolver implements IFireArmTwoHanded {

	public ItemMusket(Properties properties)
	{
		super(properties.durability(300));
		//this.setMaxDamage(300);
		//this.setMaxStackSize(1);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		//tooltip.add(new StringTextComponent("7.5 " + new TranslationTextComponent("description.bullet_damage.name")).withStyle(TextFormatting.BLUE));
		//tooltip.add(new StringTextComponent("-60 " + new TranslationTextComponent("description.fire_rate.name")).withStyle(TextFormatting.RED));
		//tooltip.add(new StringTextComponent("-10" + "% " + new TranslationTextComponent("description.accuracy.name")).withStyle(TextFormatting.RED));
		tooltip.add(new TranslationTextComponent("item.cqrepoured.tooltip.bullet_damage", 7.5).withStyle(ChatFormatting.BLUE));
		tooltip.add(new TranslationTextComponent("item.cqrepoured.tooltip.fire_rate", -60).withStyle(ChatFormatting.RED));
		tooltip.add(new TranslationTextComponent("item.cqrepoured.tooltip.accuracy", -10 + "%").withStyle(ChatFormatting.RED));

		ItemLore.addHoverTextLogic(tooltip, flagIn, "gun");
	}

	@Override
	protected float getRecoil() {
		return 0.5F * super.getRecoil();
	}

	@Override
	public void shoot(ItemStack stack, Level worldIn, Player player) {
		boolean flag = player.abilities.instabuild;
		ItemStack itemstack = this.findAmmo(player);

		if (!itemstack.isEmpty() || flag) {
			if (!worldIn.isClientSide) {
				if (flag && itemstack.isEmpty()) {
					ProjectileBullet bulletE = new ProjectileBullet(player, worldIn, EBulletType.IRON);
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

			worldIn.playLocalSound(player.position().x, player.position().y + player.getEyeHeight(), player.position().z, this.getShootSound(), SoundSource.MASTER, 1.0F, 0.9F + random.nextFloat() * 0.2F, false);
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
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		LivingEntity entityLiving = (LivingEntity) entityIn;
		ItemStack offhand = entityLiving.getMainHandItem();
		if (!offhand.isEmpty()) {
			entityLiving.addEffect(new MobEffectInstance(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
