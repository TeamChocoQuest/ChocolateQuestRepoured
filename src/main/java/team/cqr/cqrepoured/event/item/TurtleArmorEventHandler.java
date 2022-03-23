package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorTurtle;
import team.cqr.cqrepoured.util.ItemUtil;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class TurtleArmorEventHandler {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			if (!entity.world.isRemote && entity.ticksExisted % 100 == 0) {
				entity.heal(1.0F);
				double x = entity.posX;
				double y = entity.posY + entity.getEyeHeight();
				double z = entity.posZ;
				((ServerWorld) entity.world).spawnParticle(ParticleTypes.HEART, x, y, z, 2, 0.5D, 0.5D, 0.5D, 1.0D);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (!ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			return;
		}
		if (CapabilityCooldownHandlerHelper.onCooldown(entity, CQRItems.CHESTPLATE_TURTLE)) {
			return;
		}
		if (entity.getHealth() - event.getAmount() > entity.getMaxHealth() * 0.2F) {
			return;
		}
		event.setAmount(Math.min(event.getAmount(), entity.getHealth() - 1.0F));
		entity.heal(entity.getMaxHealth() * 0.2F);
		entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 0, false, true));
		entity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 4, false, true));
		entity.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 100, 1, false, true));

		double x = entity.posX;
		double y = entity.posY + entity.getEyeHeight();
		double z = entity.posZ;
		((ServerWorld) entity.world).spawnParticle(ParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);

		entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);

		CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_TURTLE, 12000);
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();
		DamageSource source = event.getSource();

		if (entity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == CQRItems.CHESTPLATE_TURTLE && source.getDamageLocation() != null) {
			Vector3d hitVec = source.getDamageLocation();
			double x = entity.posX - hitVec.x;
			double z = entity.posZ - hitVec.z;
			double yaw = Math.toDegrees(Math.atan2(-x, z));

			if (ItemUtil.compareRotations(entity.renderYawOffset, yaw, 45.0D)) {
				double y = (entity.posY + entity.height * 0.5D) - hitVec.y;
				double pitch = -Math.toDegrees(Math.asin(y));

				if (ItemUtil.compareRotations(0.0D, pitch, 60.0D)) {
					if (!entity.world.isRemote) {
						entity.world.playSound(null, hitVec.x, hitVec.y, hitVec.z, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 0.5F, 0.8F);
					}
					event.setAmount(event.getAmount() * 0.5F);
				}
			}
		}
	}

}
