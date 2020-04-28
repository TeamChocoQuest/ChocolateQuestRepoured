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
				EntitySlimePart slime = new EntitySlimePart(entity.world, entity);
				double x = entity.posX - 5.0D + 2.5D * slime.getRNG().nextDouble();
				double y = entity.posY;
				double z = entity.posZ - 5.0D + 2.5D * slime.getRNG().nextDouble();
				slime.setPosition(x, y, z);
				entity.world.spawnEntity(slime);
			}
			this.setCooldown(120);
		}
	}

}
