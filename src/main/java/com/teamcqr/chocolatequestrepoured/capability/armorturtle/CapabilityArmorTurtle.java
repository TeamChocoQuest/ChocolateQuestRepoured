package com.teamcqr.chocolatequestrepoured.capability.armorturtle;

import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorTurtle;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityArmorTurtle implements ICapabilityArmorTurtle {

	protected int cooldown;

	@Override
	public void applyContinousEffect(EntityLivingBase entity) {
		if (!entity.world.isRemote) {
			entity.heal(1.0F);
			double x = entity.posX;
			double y = entity.posY + entity.getEyeHeight();
			double z = entity.posZ;
			((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 2, 0.5D, 0.5D, 0.5D, 1.0D);
		}
	}

	@Override
	public void applyEffect(EntityLivingBase entity) {
		if (!entity.world.isRemote) {
			entity.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 100, 2, false, true));
			double x = entity.posX;
			double y = entity.posY + entity.getEyeHeight();
			double z = entity.posZ;
			((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);
			entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);
		}
		entity.getCapability(CapabilityArmorTurtleProvider.ARMOR_TURTLE_CAPABILITY, null).setCooldown(240);
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void register(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof EntityLivingBase) {
				event.addCapability(new ResourceLocation(Reference.MODID, "armor_turtle"), new CapabilityArmorTurtleProvider());
			}
		}

		@SubscribeEvent
		public static void onLivingUpdateEvent(LivingUpdateEvent event) {
			EntityLivingBase entity = event.getEntityLiving();
			ICapabilityArmorTurtle icapability = entity.getCapability(CapabilityArmorTurtleProvider.ARMOR_TURTLE_CAPABILITY, null);

			if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
				if (entity.ticksExisted % 200 == 0) {
					icapability.applyContinousEffect(entity);
				}

				if (icapability.getCooldown() > 0) {
					icapability.setCooldown(icapability.getCooldown() - 1);
				} else if (entity.getHealth() < 5.0F || entity.getHealth() / entity.getMaxHealth() < 0.2F) {
					icapability.applyEffect(entity);
				}
			}
		}

		@SubscribeEvent
		public static void onLivingDamageEvent(LivingDamageEvent event) {
			EntityLivingBase entity = event.getEntityLiving();
			ICapabilityArmorTurtle icapability = entity.getCapability(CapabilityArmorTurtleProvider.ARMOR_TURTLE_CAPABILITY, null);

			if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class) && icapability.getCooldown() <= 0) {
				float newHP = entity.getHealth() - event.getAmount();
				if (newHP < 5.0F || newHP / entity.getMaxHealth() < 0.2F) {
					icapability.applyEffect(entity);
				}
			}
		}

	}

}
