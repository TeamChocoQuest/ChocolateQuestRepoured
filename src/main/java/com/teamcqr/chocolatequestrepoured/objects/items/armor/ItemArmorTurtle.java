package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorTurtle extends ItemArmor {

	private AttributeModifier health;

	public ItemArmorTurtle(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.health = new AttributeModifier("TurtleHealthModifier", 2.0D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.health);
		}

		return multimap;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null) {
			int cooldown = CapabilityCooldownHandlerHelper.getCooldown(player, ModItems.CHESTPLATE_TURTLE);
			if (cooldown > 0) {
				tooltip.add(TextFormatting.RED + I18n.format("description.turtle_armor_charging.name") + this.convertCooldown(cooldown));
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.turtle_armor.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	private String convertCooldown(int cd) {
		int i = cd / 20;
		int minutes = i / 60;
		int seconds = i % 60;

		if (seconds < 10) {
			return minutes + ":" + "0" + seconds;
		}

		return minutes + ":" + seconds;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nullable
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return armorSlot == EntityEquipmentSlot.LEGS ? ModArmorModels.turtleArmorLegs : ModArmorModels.turtleArmor;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onLivingUpdateEvent(LivingUpdateEvent event) {
			EntityLivingBase entity = event.getEntityLiving();

			if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
				if (!entity.world.isRemote && entity.ticksExisted % 200 == 0) {
					entity.heal(1.0F);
					double x = entity.posX;
					double y = entity.posY + entity.getEyeHeight();
					double z = entity.posZ;
					((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 2, 0.5D, 0.5D, 0.5D, 1.0D);
				}

				if (!CapabilityCooldownHandlerHelper.onCooldown(entity, ModItems.CHESTPLATE_TURTLE) && entity.getHealth() < Math.max(5.0F, entity.getMaxHealth() * 0.2F)) {
					if (!entity.world.isRemote) {
						entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 2, false, true));
						double x = entity.posX;
						double y = entity.posY + entity.getEyeHeight();
						double z = entity.posZ;
						((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);
						entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);
					}
					CapabilityCooldownHandlerHelper.setCooldown(entity, ModItems.CHESTPLATE_TURTLE, 12000);
				}
			}
		}

		@SubscribeEvent
		public static void onLivingHurtEvent(LivingHurtEvent event) {
			EntityLivingBase entity = event.getEntityLiving();
			DamageSource source = event.getSource();

			if (entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.CHESTPLATE_TURTLE && source.getDamageLocation() != null) {
				Vec3d hitVec = source.getDamageLocation();
				double x = entity.posX - hitVec.x;
				double z = entity.posZ - hitVec.z;
				double yaw = Math.toDegrees(Math.atan2(-x, z));

				if (ItemUtil.compareRotations((double) entity.renderYawOffset, yaw, 45.0D)) {
					double y = (entity.posY + (double) entity.height * 0.5D) - hitVec.y;
					double pitch = -Math.toDegrees(Math.asin(y));

					if (ItemUtil.compareRotations(0.0D, pitch, 60.0D)) {
						if (event.getSource().getImmediateSource() instanceof EntityArrow) {
							event.getSource().getImmediateSource().setDead();
						}
						if (!entity.world.isRemote) {
							entity.world.playSound(null, hitVec.x, hitVec.y, hitVec.z, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 0.5F, 0.8F);
						}
						event.setCanceled(true);
					}
				}
			}
		}

	}

}
