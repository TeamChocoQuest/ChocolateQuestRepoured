package team.cqr.cqrepoured.item.sword;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemGreatSword extends ItemCQRWeapon {

	private final float specialAttackDamage;
	private final int specialAttackCooldown;

	//#TODO Materials
	public ItemGreatSword(IItemTier material, float attackDamage, int cooldown, Item.Properties props)
	{
		super(material, CQRConfig.materials.toolMaterials.greatSwordAttackDamageBonus, CQRConfig.materials.toolMaterials.greatSwordAttackSpeedBonus, props);
		this.specialAttackDamage = attackDamage;
		this.specialAttackCooldown = cooldown;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		ItemUtil.attackTarget(stack, player, entity, false, 0.0F, 1.0F, true, 2.0F, 0.0F, 1.25D, 0.25D, 0.5F);
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		ItemLore.addHoverTextLogic(tooltip, flagIn, "great_sword");
		/*if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.great_sword.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		} */
	}

	//#TODO Needs tests
	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
	{
		float range = 3F;
		double mx = entityLiving.position().x - range;
		double my = entityLiving.position().y - range;
		double mz = entityLiving.position().z - range;
		double max = entityLiving.position().x + range;
		double may = entityLiving.position().y + range;
		double maz = entityLiving.position().z + range;

		AxisAlignedBB bb = new AxisAlignedBB(mx, my, mz, max, may, maz);

		List<MobEntity> entitiesInAABB = worldIn.getEntitiesOfClass(MobEntity.class, bb);

		for (int i = 0; i < entitiesInAABB.size(); i++) {
			MobEntity entityInAABB = entitiesInAABB.get(i);

			if (this.getUseDuration(stack) - timeLeft <= 30) {
				entityInAABB.hurt(DamageSource.explosion(entityLiving), this.specialAttackDamage);
			}

			if (this.getUseDuration(stack) - timeLeft > 30 && this.getUseDuration(stack) - timeLeft <= 60) {
				entityInAABB.hurt(DamageSource.explosion(entityLiving), this.specialAttackDamage * 3);
			}

			if (this.getUseDuration(stack) - timeLeft > 60) {
				entityInAABB.hurt(DamageSource.explosion(entityLiving), this.specialAttackDamage * 4);
			}
		}

		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;

			float x = (float) -Math.sin(Math.toRadians(player.yRot));
			float z = (float) Math.cos(Math.toRadians(player.yRot));
			float y = (float) -Math.sin(Math.toRadians(player.xRot));
			x *= (1.0F - Math.abs(y));
			z *= (1.0F - Math.abs(y));

			if (player.isOnGround() && this.getUseDuration(stack) - timeLeft > 40)
			{
				player.position().add(0.0D, 0.1D, 0.0D);
				player.getDeltaMovement().add(0.0D, 0.35D, 0.0D);
				//player.posY += 0.1D;
				//player.motionY += 0.35D;
			}

			player.getCooldowns().addCooldown(stack.getItem(), this.specialAttackCooldown);
			player.swing(Hand.MAIN_HAND);
			worldIn.playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.GENERIC_EXPLODE, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			worldIn.addParticle(ParticleTypes.EXPLOSION, player.position().x + x, player.position().y + y + 1.5D, player.position().z + z, 0D, 0D, 0D);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
		}
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack)
	{
		return UseAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);
		return ActionResult.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean selected)
	{
		if (!(entity instanceof LivingEntity)) {
			return;
		}
		if (!selected) {
			return;
		}
		LivingEntity entityLiving = (LivingEntity) entity;
		ItemStack offhand = entityLiving.getMainHandItem();
		if (!offhand.isEmpty()) {
			entityLiving.addEffect(new EffectInstance(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
