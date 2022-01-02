package team.cqr.cqrepoured.item.spear;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.item.sword.ItemCQRWeapon;
import team.cqr.cqrepoured.util.ItemUtil;

/**
 * Copyright (c) 20.12.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemCQRWeapon {

	private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("95dd73a8-c715-42f9-8f6d-abf5e40fa3cd");
	private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
	private final double reachDistanceBonus;

	public ItemSpearBase(ToolMaterial material) {
		super(material, CQRConfig.materials.toolMaterials.spearAttackDamageBonus, CQRConfig.materials.toolMaterials.spearAttackSpeedBonus);
		this.reachDistanceBonus = CQRConfig.materials.toolMaterials.spearReachDistanceBonus;
	}

	public double getReach() {
		return this.reachDistanceBonus;
	}

	public double getReachExtended() {
		return this.reachDistanceBonus * SPECIAL_REACH_MULTIPLIER;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		ItemUtil.attackTarget(stack, player, entity, false, 0.0F, 1.0F, true, 1.0F, 0.0F, 0.25D, 0.25D, 0.2F);
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(REACH_DISTANCE_MODIFIER, "Weapon modifier", this.reachDistanceBonus, 0));
		}

		return multimap;
	}

	// Makes the right click a "charge attack" action
	@Override
	public UseAction getItemUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;

			if (!worldIn.isRemote) {
				Vector3d vec1 = player.getPositionEyes(1.0F);
				Vector3d vec2 = player.getLookVec();
				double reachDistance = player.getEntityAttribute(PlayerEntity.REACH_DISTANCE).getAttributeValue();
				float charge = Math.min((float) player.getItemInUseMaxCount() / (float) 40, 1.0F);

				for (LivingEntity entity : this.getEntities(worldIn, LivingEntity.class, player, vec1, vec2, reachDistance, null)) {
					// TODO apply enchantments
					entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (1.0F + this.getAttackDamage()) * charge);
				}

				Vector3d vec3 = vec1.add(new Vector3d(0.0D, -0.5D, 0.0D).rotatePitch((float) Math.toRadians(-player.rotationPitch))).add(new Vector3d(-0.4D, 0.0D, 0.0D).rotateYaw((float) Math.toRadians(-player.rotationYaw)));
				for (double d = reachDistance; d >= 0.0D; d--) {
					Vector3d vec4 = vec3.add(vec2.scale(d));
					((ServerWorld) worldIn).spawnParticle(ParticleTypes.SMOKE_NORMAL, vec4.x, vec4.y, vec4.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
				}

				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
				player.getCooldownTracker().setCooldown(stack.getItem(), 200);
			} else {
				player.swingArm(Hand.MAIN_HAND);
			}
		}
	}

	private <T extends Entity> List<T> getEntities(World world, Class<T> entityClass, @Nullable T toIgnore, Vector3d vec1, Vector3d vec2, double range, @Nullable Predicate<T> predicate) {
		List<T> list = new ArrayList<>();
		Vector3d vec3 = vec1.add(vec2.normalize().scale(range));
		RayTraceResult rayTraceResult1 = world.rayTraceBlocks(vec1, vec3, false, true, false);
		Vector3d vec4 = rayTraceResult1 != null ? rayTraceResult1.hitVec : vec3;
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
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.spear_diamond.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (!isSelected) {
			return;
		}
		LivingEntity entityLiving = (LivingEntity) entityIn;
		ItemStack offhand = entityLiving.getHeldItemOffhand();
		if (!offhand.isEmpty()) {
			entityLiving.addPotionEffect(new EffectInstance(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
