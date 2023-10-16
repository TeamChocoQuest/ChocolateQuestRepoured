package team.cqr.cqrepoured.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public class ItemUtil {

	public static boolean hasFullSet(Entity entity, Class<? extends Item> itemClass) {
		if(entity instanceof LivingEntity) {
			return hasFullSet((LivingEntity)entity, itemClass);
		}
		return false;
	}
	
	public static boolean hasFullSet(LivingEntity entity, Class<? extends Item> itemClass) {
		Iterator<ItemStack> iterable = entity.getArmorSlots().iterator();
		Class<? extends Item> helm, chest, legs, feet;
		try {
			helm = iterable.next().getItem().getClass();
			chest = iterable.next().getItem().getClass();
			legs = iterable.next().getItem().getClass();
			feet = iterable.next().getItem().getClass();
		} catch (NoSuchElementException ex) {
			return false;
		}

		return helm == itemClass && chest == itemClass && legs == itemClass && feet == itemClass;
	}

	public static boolean compareRotations(double yaw1, double yaw2, double maxDiff) {
		maxDiff = Math.abs(maxDiff);
		double d = Math.abs(yaw1 - yaw2) % 360;
		double diff = d > 180.0D ? 360.0D - d : d;

		return diff < maxDiff;
	}

	public static boolean isCheaterItem(ItemStack item) {
		if (!item.isEnchanted()) {
			return false;
		}
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			if (entry.getValue() > entry.getKey().getMaxLevel() * 2) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see ItemUtil#join(Collection)
	 */
	@SafeVarargs
	public static Multimap<Attribute, AttributeModifier> join(Multimap<Attribute, AttributeModifier>... multimaps) {
		return join(Arrays.asList(multimaps));
	}

	/**
	 * Constructs a new {@link Multimap} with the contents of all passed multimaps. Every attribute modifier is merged with
	 * all attribute modifiers of the other multimaps which target the same attribute and use the same operation.
	 */
	public static Multimap<Attribute, AttributeModifier> join(Collection<Multimap<Attribute, AttributeModifier>> multimaps) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		SetMultimap<Attribute, Operation> used = HashMultimap.create();

		multimaps.forEach(multimap -> multimap.forEach((attribute, modifier) -> {
			if (!used.put(attribute, modifier.getOperation()))
				return;
			Operation operation = modifier.getOperation();
			double amount = multimaps.stream()
					.flatMap(multimap1 -> multimap1.get(attribute).stream())
					.filter(modifier1 -> modifier1.getOperation() == operation)
					.mapToDouble(modifier1 -> modifier1.getAmount())
					.reduce(0.0D, (x, y) -> operation == Operation.MULTIPLY_TOTAL ? (1.0D + x) * (1.0D + y) - 1.0D : x + y);
			builder.put(attribute, new AttributeModifier(modifier.getId(), modifier.getName(), amount, operation));
		}));

		return builder.build();
	}

	/**
	 * Copied from {@link Player#attackTargetEntityWithCurrentItem(Entity)}
	 * 
	 * @param stack
	 * @param player
	 * @param targetEntity
	 * @param fakeCrit                     If set to true and no real crit occurred it will spawn spell crit particles
	 *                                     (vanilla: false)
	 * @param damageBonus                  A flat damage bonus which affects the main attack and enchantments like sweeping
	 *                                     edge (vanilla: 0.0F)
	 * @param damageMultiplier             A damage multiplier which affects the main attack and enchantments like sweeping
	 *                                     edge (vanilla: 1.0F)
	 * @param sweepingEnabled              If set to false the player won't be able to make a sweeping attack with this item
	 *                                     (vanilla: true)
	 * @param sweepingDamage               The base amount of damage which the sweeping attack deals (vanilla: 1.0F)
	 * @param sweepingDamageMultiplicative A damage bonus for sweeping attacks based on the main attack damage (vanilla:
	 *                                     0.0F)
	 * @param sweepingRangeHorizontal      (vanilla: 1.0D)
	 * @param sweepingRangeVertical        (vanilla: 0.25D)
	 * @param sweepingKnockback            (vanilla: 0.4F)
	 */
	public static void attackTarget(ItemStack stack, Player player, Entity targetEntity, boolean fakeCrit, float damageBonus, float damageMultiplier, boolean sweepingEnabled, float sweepingDamage, float sweepingDamageMultiplicative, double sweepingRangeHorizontal, double sweepingRangeVertical,
                                    float sweepingKnockback) {
		// CQR: Replacement for ForgeHooks.onPlayerAttackTarget to prevent infinity loop
		if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, targetEntity))) {
			return;
		}
		if (targetEntity.isAttackable()) {
			if (!targetEntity.skipAttackInteraction(player)) {
				float f = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
				// CQR: Add flat damage bonus
				f = f + damageBonus;
				float f1;

				if (targetEntity instanceof LivingEntity) {
					f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), ((LivingEntity) targetEntity).getMobType());
				} else {
					f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), MobType.UNDEFINED);
				}

				float f2 = player.getAttackStrengthScale(0.5F);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				player.resetAttackStrengthTicker();

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackBonus(player);

					if (player.isSprinting() && flag) {
						player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}

					boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && targetEntity instanceof LivingEntity;
					flag2 = flag2 && !player.isSprinting();

					CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(player, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
					flag2 = hitResult != null;
					if (flag2) {
						f *= hitResult.getDamageModifier();
					}

					f = f + f1;
					// CQR: Add damage bonus multiplier
					f = f * damageMultiplier;
					boolean flag3 = false;
					double d0 = player.walkDist - player.walkDistO;

					// CQR: Disable sweep attack when sweepingEnabled is false
					if (sweepingEnabled && flag && !flag2 && !flag1 && player.onGround() && d0 < player.getSpeed()) {
						ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);

						if (itemstack.getItem() instanceof SwordItem || itemstack.is(ItemTags.SWORDS)) {
							flag3 = true;
						}
					}

					float f4 = 0.0F;
					boolean flag4 = false;
					int j = EnchantmentHelper.getFireAspect(player);

					if (targetEntity instanceof LivingEntity) {
						f4 = ((LivingEntity) targetEntity).getHealth();

						if (j > 0 && !targetEntity.isOnFire()) {
							flag4 = true;
							targetEntity.setSecondsOnFire(1);
						}
					}

					Vec3 motion = player.getDeltaMovement();
					
					boolean flag5 = targetEntity.hurt(player.damageSources().playerAttack(player), f);

					if (flag5) {
						if (i > 0) {
							if (targetEntity instanceof LivingEntity) {
								((LivingEntity) targetEntity).knockback(i * 0.5F, Mth.sin(player.getYRot() * 0.017453292F), (-Mth.cos(player.getYRot() * 0.017453292F)));
							} else {
								targetEntity.push(-Mth.sin(player.getYRot() * 0.017453292F) * i * 0.5F, 0.1D, Mth.cos(player.getYRot() * 0.017453292F) * i * 0.5F);
							}

							player.setDeltaMovement(player.getDeltaMovement().multiply(0.6, 1.0, 0.6));
							player.setSprinting(false);
						}

						if (flag3) {
							// CQR: Allow modification of sweeping damage
							float f3 = sweepingDamage + sweepingDamageMultiplicative * f;
							f3 = f3 + EnchantmentHelper.getSweepingDamageRatio(player) * f;

							double entityReachDistanceSqr = getEntityReachDistanceSqr(player);
							// CQR: Allow modification of sweeping hitbox
							AABB aabb = targetEntity.getBoundingBox().expandTowards(sweepingRangeHorizontal, sweepingRangeVertical, sweepingRangeHorizontal);
							for (LivingEntity entitylivingbase : player.level().getEntitiesOfClass(LivingEntity.class, aabb)) {
								// CQR: Increase sweeping range when players reach distance is higher
								if (entitylivingbase != player && entitylivingbase != targetEntity && !player.isAlliedTo(entitylivingbase) && player.distanceToSqr(entitylivingbase) < entityReachDistanceSqr) {
									// CQR: Allow modification of sweeping knockback strength
									entitylivingbase.knockback(sweepingKnockback, Mth.sin(player.getYRot() * 0.017453292F), (-Mth.cos(player.getYRot() * 0.017453292F)));
									entitylivingbase.hurt(player.damageSources().playerAttack(player), f3);
								}
							}

							player.level().playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
							player.sweepAttack();
						}

						if (targetEntity instanceof ServerPlayer && targetEntity.hurtMarked) {
							((ServerPlayer) targetEntity).connection.send(new ServerboundMoveVehiclePacket(targetEntity));
							targetEntity.hurtMarked = false;
							targetEntity.setDeltaMovement(motion);
						}

						if (flag2) {
							player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
							player.crit(targetEntity);
						} else if (fakeCrit) { // CQR: Allow fake crits to happen
							player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.2F);
							player.crit(targetEntity);
						}

						if (!flag2 && !fakeCrit && !flag3) {
							if (flag) {
								player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F);
							} else {
								player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
							}
						}

						if (f1 > 0.0F) {
							player.magicCrit(targetEntity);
						}

						player.setLastHurtMob(targetEntity);

						if (targetEntity instanceof LivingEntity) {
							EnchantmentHelper.doPostHurtEffects((LivingEntity) targetEntity, player);
						}

						EnchantmentHelper.doPostDamageEffects(player, targetEntity);
						ItemStack itemstack1 = player.getMainHandItem();
						Entity entity = targetEntity;

						if (targetEntity instanceof PartEntity) {
							Entity ientitymultipart = ((PartEntity<?>) targetEntity).getParent();

							if (ientitymultipart instanceof LivingEntity) {
								entity = (LivingEntity) ientitymultipart;
							}
						}

						if (!itemstack1.isEmpty() && entity instanceof LivingEntity) {
							ItemStack beforeHitCopy = itemstack1.copy();
							itemstack1.hurtEnemy((LivingEntity) entity, player);

							if (itemstack1.isEmpty()) {
								ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, InteractionHand.MAIN_HAND);
								player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
							}
						}

						if (targetEntity instanceof LivingEntity) {
							float f5 = f4 - ((LivingEntity) targetEntity).getHealth();
							player.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (j > 0) {
								targetEntity.setSecondsOnFire(j * 4);
							}

							if (player.level() instanceof ServerLevel && f5 > 2.0F) {
								int k = (int) (f5 * 0.5D);
								((ServerLevel)targetEntity.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5D), targetEntity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
							}
						}

						player.causeFoodExhaustion(0.1F);
					} else {
						player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);

						if (flag4) {
							targetEntity.clearFire();
						}
					}
				}
			}
		}
	}

	private static double getEntityReachDistanceSqr(Player player) {
		double d = player.getAttribute(ForgeMod.ENTITY_REACH.get()).getValue();
		if (!player.isCreative()) {
			d -= 0.5D;
		}
		d -= 1.5D;
		return d * d;
	}

}
