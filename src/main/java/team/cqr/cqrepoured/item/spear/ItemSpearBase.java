package team.cqr.cqrepoured.item.spear;

import com.google.common.base.Predicate;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.item.IExtendedItemTier;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.sword.ItemCQRWeapon;
import team.cqr.cqrepoured.util.ItemUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) 20.12.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemCQRWeapon {

	private static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("95dd73a8-c715-42f9-8f6d-abf5e40fa3cd");
	private static final float SPECIAL_REACH_MULTIPLIER = 1.5F;
	private final double reachDistanceBonus;

	public ItemSpearBase(Properties props, IExtendedItemTier material) {
		super(material, material.getFixedAttackDamageBonus(), material.getAttackSpeedBonus(), props);
		this.reachDistanceBonus = material.getMovementSpeedBonus(); //#TODO PROBABLY NEEDS TO BE TWEAKED
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
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> ret = LinkedHashMultimap.create(super.getAttributeModifiers(slot, stack));
		 Attribute reachDistance = ForgeMod.REACH_DISTANCE.get(); //ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "reach_distance"));
         if(reachDistance != null && slot == EquipmentSlotType.MAINHAND)
             ret.put(reachDistance, new AttributeModifier(REACH_DISTANCE_MODIFIER, "Weapon modifier", this.reachDistanceBonus, Operation.ADDITION));
   
        return ret;
	}

	// Makes the right click a "charge attack" action
	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
	
	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;

			if (!worldIn.isClientSide) {
				Vector3d vec1 = player.getEyePosition(1.0F);
				Vector3d vec2 = player.getLookAngle();
				double reachDistance = player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
				float charge = Math.min((float) player.getTicksUsingItem() / (float) 40, 1.0F);

				for (LivingEntity entity : this.getEntities(worldIn, LivingEntity.class, player, vec1, vec2, reachDistance, null)) {
					// TODO apply enchantments
					entity.hurt(DamageSource.playerAttack(player), (1.0F + this.getDamage()) * charge);
				}

				Vector3d vec3 = vec1.add(new Vector3d(0.0D, -0.5D, 0.0D).xRot((float) Math.toRadians(-player.xRot))).add(new Vector3d(-0.4D, 0.0D, 0.0D).yRot((float) Math.toRadians(-player.yRot)));
				for (double d = reachDistance; d >= 0.0D; d--) {
					Vector3d vec4 = vec3.add(vec2.scale(d));
					((ServerWorld) worldIn).sendParticles(ParticleTypes.SMOKE, vec4.x, vec4.y, vec4.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
				}

				player.level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
				player.getCooldowns().addCooldown(stack.getItem(), 200);
			} else {
				player.swing(Hand.MAIN_HAND);
			}
		}
	}
	//#TODO needs tests
	private <T extends Entity> List<T> getEntities(World world, Class<T> entityClass, @Nullable T toIgnore, Vector3d vec1, Vector3d vec2, double range, @Nullable Predicate<T> predicate) {
		List<T> list = new ArrayList<>();
		Vector3d vec3 = vec1.add(vec2.normalize().scale(range));
		RayTraceResult rayTraceResult1 = world.clip(new RayTraceContext(vec1, vec3, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, null));
		Vector3d vec4 = rayTraceResult1 != null ? rayTraceResult1.getLocation() : vec3;
		AxisAlignedBB aabb1 = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec4.x, vec4.y, vec4.z);

		for (T entity : world.getEntitiesOfClass(entityClass, aabb1, predicate)) {
			if (entity == toIgnore) {
				continue;
			}

			AxisAlignedBB aabb2 = entity.getBoundingBox().inflate(entity.getPickRadius());
			Optional opt = aabb2.clip(vec1, vec4);

			if (opt.isPresent())
			{
				list.add(entity);
			}
		}

		return list;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

	@Override
	public void onUseTick(World worldIn, LivingEntity entityIn, ItemStack stack, int itemSlot) {
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (entityIn.getUseItem().getItem() != this) {
			return;
		}
		LivingEntity entityLiving = (LivingEntity) entityIn;
		ItemStack offhand = entityLiving.getMainHandItem();
		if (!offhand.isEmpty()) {
			entityLiving.addEffect(new EffectInstance(CQRPotions.TWOHANDED, 30, 1));
		}
	}

}
