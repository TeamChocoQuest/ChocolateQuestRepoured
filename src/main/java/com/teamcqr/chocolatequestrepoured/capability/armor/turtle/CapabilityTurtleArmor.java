package com.teamcqr.chocolatequestrepoured.capability.armor.turtle;

import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmor;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorTurtle;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CapabilityTurtleArmor extends CapabilitySpecialArmor {

	public CapabilityTurtleArmor() {
		super(ItemArmorTurtle.class);
	}

	@Override
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();

		if (entity.ticksExisted % 200 == 0 && !entity.world.isRemote) {
			entity.heal(1.0F);
			double x = entity.posX;
			double y = entity.posY + entity.getEyeHeight();
			double z = entity.posZ;
			((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 2, 0.5D, 0.5D, 0.5D, 1.0D);
		}

		if (!this.onCooldown() && entity.getHealth() < Math.max(5.0F, entity.getMaxHealth() * 0.2F)) {
			if (!entity.world.isRemote) {
				entity.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 100, 2, false, true));
				double x = entity.posX;
				double y = entity.posY + entity.getEyeHeight();
				double z = entity.posZ;
				((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);
				entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);
			}
			this.setCooldown(424);
		}
	}

	@Override
	public void onLivingDamageEvent(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();

		if (!this.onCooldown()) {
			float health = entity.getHealth() - event.getAmount();
			if (health < Math.max(5.0F, entity.getMaxHealth() * 0.2F)) {
				if (!entity.world.isRemote) {
					entity.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 100, 2, false, true));
					double x = entity.posX;
					double y = entity.posY + entity.getEyeHeight();
					double z = entity.posZ;
					((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);
					entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);
				}
				this.setCooldown(12000);
			}
		}
	}

}
