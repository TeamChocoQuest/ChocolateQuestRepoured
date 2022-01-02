package team.cqr.cqrepoured.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.collect.Multimap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraftforge.entity.PartEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public class ItemUtil {

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

	public static void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, Attribute attribute, UUID id, Function<Double, Double> function) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getId().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getId(), modifier.getName(), function.apply(modifier.getAmount()), modifier.getOperation()));
		}
	}

	/**
	 * Copied from {@link PlayerEntity#attackTargetEntityWithCurrentItem(Entity)}
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
	public static void attackTarget(ItemStack stack, PlayerEntity player, Entity targetEntity, boolean fakeCrit, float damageBonus, float damageMultiplier, boolean sweepingEnabled, float sweepingDamage, float sweepingDamageMultiplicative, double sweepingRangeHorizontal, double sweepingRangeVertical,
                                    float sweepingKnockback) {
		// CQR: Replacement for ForgeHooks.onPlayerAttackTarget to prevent infinity loop
		if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, targetEntity))) {
			return;
		}
		if (targetEntity.isAttackable()) {
			if (!targetEntity.hitByEntity(player)) {
				float f = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
				// CQR: Add flat damage bonus
				f = f + damageBonus;
				float f1;

				if (targetEntity instanceof LivingEntity) {
					f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), ((LivingEntity) targetEntity).getMobType());
				} else {
					f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), CreatureAttribute.UNDEFINED);
				}

				float f2 = player.getCooledAttackStrength(0.5F);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				player.resetCooldown();

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackBonus(player);

					if (player.isSprinting() && flag) {
						player.level.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}

					boolean flag2 = flag && player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.isPotionActive(Effects.BLINDNESS) && !player.isRiding() && targetEntity instanceof LivingEntity;
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
					double d0 = player.distanceWalkedModified - player.prevDistanceWalkedModified;

					// CQR: Disable sweep attack when sweepingEnabled is false
					if (sweepingEnabled && flag && !flag2 && !flag1 && player.isOnGround() && d0 < player.getAIMoveSpeed()) {
						ItemStack itemstack = player.getItemInHand(Hand.MAIN_HAND);

						if (itemstack.getItem() instanceof SwordItem) {
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

					double d1 = targetEntity.motionX;
					double d2 = targetEntity.motionY;
					double d3 = targetEntity.motionZ;
					boolean flag5 = targetEntity.attackEntityFrom(DamageSource.playerAttack(player), f);

					if (flag5) {
						if (i > 0) {
							if (targetEntity instanceof LivingEntity) {
								((LivingEntity) targetEntity).knockback(player, i * 0.5F, MathHelper.sin(player.rotationYaw * 0.017453292F), (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
							} else {
								targetEntity.addVelocity(-MathHelper.sin(player.rotationYaw * 0.017453292F) * i * 0.5F, 0.1D, MathHelper.cos(player.rotationYaw * 0.017453292F) * i * 0.5F);
							}

							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if (flag3) {
							// CQR: Allow modification of sweeping damage
							float f3 = sweepingDamage + sweepingDamageMultiplicative * f;
							f3 = f3 + EnchantmentHelper.getSweepingDamageRatio(player) * f;

							double entityReachDistanceSqr = getEntityReachDistanceSqr(player);
							// CQR: Allow modification of sweeping hitbox
							AxisAlignedBB aabb = targetEntity.getBoundingBox().expandTowards(sweepingRangeHorizontal, sweepingRangeVertical, sweepingRangeHorizontal);
							for (LivingEntity entitylivingbase : player.level.getEntitiesOfClass(LivingEntity.class, aabb)) {
								// CQR: Increase sweeping range when players reach distance is higher
								if (entitylivingbase != player && entitylivingbase != targetEntity && !player.isAlliedTo(entitylivingbase) && player.distanceToSqr(entitylivingbase) < entityReachDistanceSqr) {
									// CQR: Allow modification of sweeping knockback strength
									entitylivingbase.knockback(player, sweepingKnockback, MathHelper.sin(player.rotationYaw * 0.017453292F), (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
									entitylivingbase.attackEntityFrom(DamageSource.playerAttack(player), f3);
								}
							}

							player.level.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
							player.sweepAttack();
						}

						if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged) {
							((ServerPlayerEntity) targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
							targetEntity.velocityChanged = false;
							targetEntity.motionX = d1;
							targetEntity.motionY = d2;
							targetEntity.motionZ = d3;
						}

						if (flag2) {
							player.level.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
							player.onCriticalHit(targetEntity);
						} else if (fakeCrit) { // CQR: Allow fake crits to happen
							player.level.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.2F);
							player.onCriticalHit(targetEntity);
						}

						if (!flag2 && !fakeCrit && !flag3) {
							if (flag) {
								player.level.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F);
							} else {
								player.level.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
							}
						}

						if (f1 > 0.0F) {
							player.onEnchantmentCritical(targetEntity);
						}

						player.setLastAttackedEntity(targetEntity);

						if (targetEntity instanceof LivingEntity) {
							EnchantmentHelper.applyThornEnchantments((LivingEntity) targetEntity, player);
						}

						EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
						ItemStack itemstack1 = player.getHeldItemMainhand();
						Entity entity = targetEntity;

						if (targetEntity instanceof PartEntity) {
							Entity ientitymultipart = ((PartEntity) targetEntity).getParent();

							if (ientitymultipart instanceof LivingEntity) {
								entity = (LivingEntity) ientitymultipart;
							}
						}

						if (!itemstack1.isEmpty() && entity instanceof LivingEntity) {
							ItemStack beforeHitCopy = itemstack1.copy();
							itemstack1.hitEntity((LivingEntity) entity, player);

							if (itemstack1.isEmpty()) {
								ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, Hand.MAIN_HAND);
								player.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
							}
						}

						if (targetEntity instanceof LivingEntity) {
							float f5 = f4 - ((LivingEntity) targetEntity).getHealth();
							player.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (j > 0) {
								targetEntity.setSecondsOnFire(j * 4);
							}

							if (player.level instanceof ServerWorld && f5 > 2.0F) {
								int k = (int) (f5 * 0.5D);
								((ServerWorld) player.level).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + targetEntity.height * 0.5F, targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
							}
						}

						player.addExhaustion(0.1F);
					} else {
						player.world.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);

						if (flag4) {
							targetEntity.extinguish();
						}
					}
				}
			}
		}
	}

	private static double getEntityReachDistanceSqr(PlayerEntity player) {
		double d = player.getEntityAttribute(PlayerEntity.REACH_DISTANCE).getAttributeValue();
		if (!player.isCreative()) {
			d -= 0.5D;
		}
		d -= 1.5D;
		return d * d;
	}

}
