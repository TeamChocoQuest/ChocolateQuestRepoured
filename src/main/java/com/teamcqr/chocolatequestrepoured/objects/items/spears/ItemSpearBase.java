package com.teamcqr.chocolatequestrepoured.objects.items.spears;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copyright (c) 20.12.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemSword {

	public static final UUID ATTACK_RANGE_MODIFIER = new UUID(0x0, 0x1);
	public static final float BASE_ATTACK_RANGE = 4.0F;
	private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
	private double reach;
	private double attackSpeed;

	public ItemSpearBase(ToolMaterial material, double reach, double attackSpeed) {
		super(material);
		this.reach = Math.max(reach, -3.5F);
		this.attackSpeed = attackSpeed;
	}

	public double getReach() {
		return this.reach;
	}

	public double getReachExtended() {
		return this.reach * SPECIAL_REACH_MULTIPLIER;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		// Copied from EntityPlayer#attackTargetEntityWithCurrentItem(Entity targetEntity)
		// Removed sweeping
		if (entity.canBeAttackedWithItem()) {
			if (!entity.hitByEntity(player)) {
				float f = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				float f1;

				if (entity instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
				} else {
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
				}

				float f2 = player.getCooledAttackStrength(0.5F);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				player.resetCooldown();

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackModifier(player);

					if (player.isSprinting() && flag) {
						player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
						++i;
					}

					boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding() && entity instanceof EntityLivingBase;
					flag2 = flag2 && !player.isSprinting();

					net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, entity, flag2, flag2 ? 1.5F : 1.0F);
					flag2 = hitResult != null;
					if (flag2) {
						f *= hitResult.getDamageModifier();
					}

					f = f + f1;

					float f4 = 0.0F;
					boolean flag4 = false;
					int j = EnchantmentHelper.getFireAspectModifier(player);

					if (entity instanceof EntityLivingBase) {
						f4 = ((EntityLivingBase) entity).getHealth();

						if (j > 0 && !entity.isBurning()) {
							flag4 = true;
							entity.setFire(1);
						}
					}

					double d1 = entity.motionX;
					double d2 = entity.motionY;
					double d3 = entity.motionZ;
					boolean flag5 = entity.attackEntityFrom(DamageSource.causePlayerDamage(player), f);

					if (flag5) {
						if (i > 0) {
							if (entity instanceof EntityLivingBase) {
								((EntityLivingBase) entity).knockBack(player, i * 0.5F, MathHelper.sin(player.rotationYaw * 0.017453292F), (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
							} else {
								entity.addVelocity(-MathHelper.sin(player.rotationYaw * 0.017453292F) * i * 0.5F, 0.1D, MathHelper.cos(player.rotationYaw * 0.017453292F) * i * 0.5F);
							}

							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if (entity instanceof EntityPlayerMP && entity.velocityChanged) {
							((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
							entity.velocityChanged = false;
							entity.motionX = d1;
							entity.motionY = d2;
							entity.motionZ = d3;
						}

						if (flag2) {
							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
							player.onCriticalHit(entity);
						} else if (flag) {
							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
						} else {
							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
						}

						if (f1 > 0.0F) {
							player.onEnchantmentCritical(entity);
						}

						player.setLastAttackedEntity(entity);

						if (entity instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, player);
						}

						EnchantmentHelper.applyArthropodEnchantments(player, entity);
						ItemStack itemstack1 = player.getHeldItemMainhand();
						Entity entity1 = entity;

						if (entity instanceof MultiPartEntityPart) {
							IEntityMultiPart ientitymultipart = ((MultiPartEntityPart) entity).parent;

							if (ientitymultipart instanceof EntityLivingBase) {
								entity1 = (EntityLivingBase) ientitymultipart;
							}
						}

						if (!itemstack1.isEmpty() && entity1 instanceof EntityLivingBase) {
							ItemStack beforeHitCopy = itemstack1.copy();
							itemstack1.hitEntity((EntityLivingBase) entity1, player);

							if (itemstack1.isEmpty()) {
								net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.MAIN_HAND);
								player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
							}
						}

						if (entity instanceof EntityLivingBase) {
							float f5 = f4 - ((EntityLivingBase) entity).getHealth();
							player.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (j > 0) {
								entity.setFire(j * 4);
							}

							if (player.world instanceof WorldServer && f5 > 2.0F) {
								int k = (int) (f5 * 0.5D);
								((WorldServer) player.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entity.posX, entity.posY + entity.height * 0.5F, entity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
							}
						}

						player.addExhaustion(0.1F);
					} else {
						player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);

						if (flag4) {
							entity.extinguish();
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			this.replaceModifier(multimap, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, this.attackSpeed);
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(ATTACK_RANGE_MODIFIER, "Weapon modifier", 1, 0));
		}

		return multimap;
	}

	protected void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, IAttribute attribute, UUID id, double value) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() + value, modifier.getOperation()));
		}
	}

	// Makes the right click a "charge attack" action
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;

			if (!worldIn.isRemote) {
				Vec3d vec1 = player.getPositionEyes(1.0F);
				Vec3d vec2 = player.getLookVec();
				double reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
				float charge = Math.min((float) player.getItemInUseMaxCount() / (float) 40, 1.0F);

				for (EntityLivingBase entity : this.getEntities(worldIn, EntityLivingBase.class, player, vec1, vec2, reachDistance, null)) {
					// TODO apply enchantments
					entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (1.0F + this.getAttackDamage()) * charge);
				}

				Vec3d vec3 = vec1.add(new Vec3d(0.0D, -0.5D, 0.0D).rotatePitch((float) Math.toRadians(-player.rotationPitch))).add(new Vec3d(-0.4D, 0.0D, 0.0D).rotateYaw((float) Math.toRadians(-player.rotationYaw)));
				for (int i = (int) reachDistance; i > -1; i--) {
					Vec3d vec4 = vec3.add(vec2.scale(i));
					((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec4.x, vec4.y, vec4.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
				}

				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
				player.getCooldownTracker().setCooldown(stack.getItem(), 200);
			} else {
				player.swingArm(EnumHand.MAIN_HAND);
			}
		}
	}

	private <T extends Entity> List<T> getEntities(World world, Class<T> entityClass, @Nullable T toIgnore, Vec3d vec1, Vec3d vec2, double range, @Nullable Predicate<T> predicate) {
		List<T> list = new ArrayList<>();
		Vec3d vec3 = vec1.add(vec2.normalize().scale(range));
		RayTraceResult rayTraceResult1 = world.rayTraceBlocks(vec1, vec3, false, true, false);
		Vec3d vec4 = rayTraceResult1 != null ? rayTraceResult1.hitVec : vec3;
		AxisAlignedBB aabb1 = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec4.x, vec4.y, vec4.z);

		for (T entity : world.getEntitiesWithinAABB(entityClass, aabb1, predicate)) {
			if (entity == toIgnore) {
				continue;
			}

			AxisAlignedBB aabb2 = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
			RayTraceResult rayTraceResult2 = aabb2.calculateIntercept(vec1, vec4);

			if (rayTraceResult2 != null) {
				list.add(entity);
			}
		}

		return list;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.spear_diamond.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	// Unequip off hand weapons
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;

			if (player.getHeldItemMainhand() == stack && !player.getHeldItemOffhand().isEmpty()) {
				ItemStack stack1 = player.getHeldItemOffhand();
				player.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);

				if (!player.inventory.addItemStackToInventory(stack1)) {
					player.entityDropItem(stack1, 0.0F);
				}
			}
		}
	}

}
