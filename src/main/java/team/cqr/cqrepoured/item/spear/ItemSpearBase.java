package team.cqr.cqrepoured.item.spear;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.sword.ItemCQRWeapon;
import team.cqr.cqrepoured.util.ItemUtil;

/**
 * Copyright (c) 20.12.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemCQRWeapon {

	private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
	private final double reachDistanceBonus;

	public ItemSpearBase(Properties props, Tier material) {
		super(material, props);
		this.addAttributeModifiers(CQRConfig.SERVER_CONFIG.materials.itemTiers.spear);
		this.reachDistanceBonus = 1; // TODO PROBABLY NEEDS TO BE TWEAKED
	}

	public double getReach() {
		return this.reachDistanceBonus;
	}

	public double getReachExtended() {
		return this.reachDistanceBonus * SPECIAL_REACH_MULTIPLIER;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		ItemUtil.attackTarget(stack, player, entity, false, 0.0F, 1.0F, true, 1.0F, 0.0F, 0.25D, 0.25D, 0.2F);
		return true;
	}

	// Makes the right click a "charge attack" action
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player player = (Player) entityLiving;

			if (!worldIn.isClientSide) {
				Vec3 vec1 = player.getEyePosition(1.0F);
				Vec3 vec2 = player.getLookAngle();
				double reachDistance = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
				float charge = Math.min((float) player.getTicksUsingItem() / (float) 40, 1.0F);

				for (LivingEntity entity : this.getEntities(worldIn, LivingEntity.class, player, vec1, vec2, reachDistance, null)) {
					// TODO apply enchantments
					entity.hurt(worldIn.damageSources().playerAttack(player), (1.0F + this.getDamage()) * charge);
				}

				Vec3 vec3 = vec1.add(new Vec3(0.0D, -0.5D, 0.0D).xRot((float) Math.toRadians(-player.getXRot()))).add(new Vec3(-0.4D, 0.0D, 0.0D).yRot((float) Math.toRadians(-player.getYRot())));
				for (double d = reachDistance; d >= 0.0D; d--) {
					Vec3 vec4 = vec3.add(vec2.scale(d));
					((ServerLevel) worldIn).sendParticles(ParticleTypes.SMOKE, vec4.x, vec4.y, vec4.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
				}

				player.level().playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
				player.getCooldowns().addCooldown(stack.getItem(), 200);
			} else {
				player.swing(InteractionHand.MAIN_HAND);
			}
		}
	}

	// #TODO needs tests
	private <T extends Entity> List<T> getEntities(Level world, Class<T> entityClass, @Nullable T toIgnore, Vec3 vec1, Vec3 vec2, double range, @Nullable Predicate<T> predicate) {
		List<T> list = new ArrayList<>();
		Vec3 vec3 = vec1.add(vec2.normalize().scale(range));
		HitResult rayTraceResult1 = world.clip(new ClipContext(vec1, vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
		Vec3 vec4 = rayTraceResult1 != null ? rayTraceResult1.getLocation() : vec3;
		AABB aabb1 = new AABB(vec1.x, vec1.y, vec1.z, vec4.x, vec4.y, vec4.z);

		for (T entity : world.getEntitiesOfClass(entityClass, aabb1, predicate)) {
			if (entity == toIgnore) {
				continue;
			}

			AABB aabb2 = entity.getBoundingBox().inflate(entity.getPickRadius());
			Optional<Vec3> opt = aabb2.clip(vec1, vec4);

			if (opt.isPresent()) {
				list.add(entity);
			}
		}

		return list;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, "spear");
	}

	@Override
	public void onUseTick(Level worldIn, LivingEntity entityIn, ItemStack stack, int itemSlot) {
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (entityIn.getUseItem().getItem() != this) {
			return;
		}
		LivingEntity entityLiving = (LivingEntity) entityIn;
		ItemStack offhand = entityLiving.getMainHandItem();
		if (!offhand.isEmpty()) {
			entityLiving.addEffect(new MobEffectInstance(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
