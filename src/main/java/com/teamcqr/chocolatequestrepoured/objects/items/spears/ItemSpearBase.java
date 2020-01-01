package com.teamcqr.chocolatequestrepoured.objects.items.spears;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.ExtendedReachAttackPacket;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copyright (c) 20.12.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemSword {

	private final float SPECIAL_REACH_MULTIPLIER = 1.5F;
	private float reach;
	private float attackSpeed;

	public ItemSpearBase(ToolMaterial material, float reach, float attackSpeed) {
		super(material);
		this.reach = reach;
		this.attackSpeed = attackSpeed;
	}

	public float getReach() {
		return this.reach;
	}

	public float getReachExtended() {
		return this.reach * this.SPECIAL_REACH_MULTIPLIER;
	}

	@Override
	public ImmutableMap<String, ITimeValue> getAnimationParameters(ItemStack stack, World world, EntityLivingBase entity) {
		return super.getAnimationParameters(stack, world, entity);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		this.replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, this.attackSpeed);
		return modifiers;
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
		playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20 * 5); // 20 ticks per sec * 5 seconds
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		final float SPECIAL_CHARGE_TIME_TICKS = 20F * 1.5F;

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			int timeCharged = this.getMaxItemUseDuration(stack) - timeLeft;
			if (!player.isSneaking() && timeCharged > SPECIAL_CHARGE_TIME_TICKS) {
				RayTraceResult result = getMouseOverExtended(this.reach * this.SPECIAL_REACH_MULTIPLIER);
				if (result != null && result.entityHit != null) {
					if (result.entityHit != player && result.entityHit.hurtResistantTime == 0) {
						CQRMain.NETWORK.sendToServer(new ExtendedReachAttackPacket(result.entityHit.getEntityId(), true));
					}
				} else {
					player.swingArm(EnumHand.MAIN_HAND);
				}

				player.getCooldownTracker().setCooldown(stack.getItem(), 20 * 2); // 20 ticks per sec * 2 seconds
			}
		}
	}

	@Mod.EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {
		@SideOnly(Side.CLIENT)
		@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
		public static void onEvent(MouseEvent event) {
			if (event.getButton() == 0 && event.isButtonstate()) {
				Minecraft mc = Minecraft.getMinecraft();
				EntityPlayer clickingPlayer = mc.player;
				if (clickingPlayer != null) {
					ItemStack itemStack = clickingPlayer.getHeldItemMainhand();
					ItemSpearBase spear;

					if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemSpearBase) {
						spear = (ItemSpearBase) itemStack.getItem();
						float reach = spear.getReach();

						RayTraceResult result = getMouseOverExtended(reach);
						if (result != null && result.entityHit != null) {
							if (result.entityHit != clickingPlayer && result.entityHit.hurtResistantTime == 0) {
								CQRMain.NETWORK.sendToServer(new ExtendedReachAttackPacket(result.entityHit.getEntityId(), false));
							}
						}
					}
				}
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public static RayTraceResult getMouseOverExtended(float distance) {
		// Most of this is copied from EntityRenderer#getMouseOver(). Some variable names changed for readability

		Entity pointedEntity = null;
		Minecraft mc = Minecraft.getMinecraft();
		Entity renderViewEntity = mc.getRenderViewEntity();

		if (renderViewEntity != null && mc.world != null) {
			double d0 = distance;
			double d1 = d0;
			RayTraceResult rtResult = renderViewEntity.rayTrace(d0, 0);
			Vec3d eyeVec = renderViewEntity.getPositionEyes(0);

			if (rtResult != null) {
				d1 = rtResult.hitVec.distanceTo(eyeVec);
			}

			Vec3d vec3d1 = renderViewEntity.getLook(1.0F);
			Vec3d vec3d2 = eyeVec.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);

			Vec3d vec3d3 = null;

			List<Entity> list = mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D),
					Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
						@Override
						public boolean apply(@Nullable Entity p_apply_1_) {
							return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
						}
					}));

			double d2 = d1;

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = list.get(j);
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(eyeVec, vec3d2);

				if (axisalignedbb.contains(eyeVec)) {
					if (d2 >= 0.0D) {
						pointedEntity = entity1;
						vec3d3 = raytraceresult == null ? eyeVec : raytraceresult.hitVec;
						d2 = 0.0D;
					}
				} else if (raytraceresult != null) {
					double d3 = eyeVec.distanceTo(raytraceresult.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						if (entity1.getLowestRidingEntity() == renderViewEntity.getLowestRidingEntity() && !entity1.canRiderInteract()) {
							if (d2 == 0.0D) {
								pointedEntity = entity1;
								vec3d3 = raytraceresult.hitVec;
							}
						} else {
							pointedEntity = entity1;
							vec3d3 = raytraceresult.hitVec;
							d2 = d3;
						}
					}
				}
			}

			if (pointedEntity == null || eyeVec.distanceTo(vec3d3) > distance) {
				return new RayTraceResult(RayTraceResult.Type.MISS, eyeVec, (EnumFacing) null, rtResult.getBlockPos());
			} else {
				return new RayTraceResult(pointedEntity, vec3d3);
			}

		}

		return null;

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
		if (!worldIn.isRemote) {
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;

				if (player.getHeldItemMainhand() == stack) {
					if (!player.getHeldItemOffhand().isEmpty()) {
						if (!player.inventory.addItemStackToInventory(player.getHeldItemOffhand())) {
							player.entityDropItem(player.getHeldItemOffhand(), 0F);
						}

						if (!player.capabilities.isCreativeMode) {
							player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
						}
					}
				}
			}
		}
	}

}
