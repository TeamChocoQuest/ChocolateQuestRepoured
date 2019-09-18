package com.teamcqr.chocolatequestrepoured.capability.armor.slime;

import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmor;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorSlime;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CapabilitySlimeArmor extends CapabilitySpecialArmor {

	public CapabilitySlimeArmor() {
		super(ItemArmorSlime.class);
	}

	@Override
	public void onLivingUpdateEvent(LivingUpdateEvent event) {

	}

	@Override
	public void onLivingDamageEvent(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();

		if (!this.onCooldown()) {
			if (!entity.world.isRemote) {
				EntitySlimePart slime = new EntitySlimePart(entity.world, entity, 1.0F);
				slime.posX = entity.posX + 4.0D * entity.world.rand.nextDouble() - 2.0D;
				slime.posY = entity.posY + 0.5D * entity.world.rand.nextDouble();
				slime.posZ = entity.posZ + 4.0D * entity.world.rand.nextDouble() - 2.0D;
				slime.motionX = entity.world.rand.nextDouble() - 0.5D;
				slime.motionY = 0.5D * entity.world.rand.nextDouble();
				slime.motionZ = entity.world.rand.nextDouble() - 0.5D;
				entity.world.spawnEntity(slime);
			}
			this.setCooldown(100);
		}
	}

}
