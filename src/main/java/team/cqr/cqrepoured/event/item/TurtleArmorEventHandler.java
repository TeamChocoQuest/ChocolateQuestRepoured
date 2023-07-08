package team.cqr.cqrepoured.event.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorTurtle;
import team.cqr.cqrepoured.util.ItemUtil;

@EventBusSubscriber(modid = CQRMain.MODID)
public class TurtleArmorEventHandler {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();

		if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			if (!entity.level().isClientSide() && entity.tickCount % 100 == 0) {
				entity.heal(1.0F);
				double x = entity.getX();
				double y = entity.getY() + entity.getEyeHeight();
				double z = entity.getZ();
				((ServerLevel) entity.level()).sendParticles(ParticleTypes.HEART, x, y, z, 2, 0.5D, 0.5D, 0.5D, 1.0D);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		if (!ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			return;
		}
		if (CapabilityCooldownHandlerHelper.onCooldown(entity, CQRItems.CHESTPLATE_TURTLE.get())) {
			return;
		}
		if (entity.getHealth() - event.getAmount() > entity.getMaxHealth() * 0.2F) {
			return;
		}
		event.setAmount(Math.min(event.getAmount(), entity.getHealth() - 1.0F));
		entity.heal(entity.getMaxHealth() * 0.2F);
		entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, true));
		entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 4, false, true));
		entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, false, true));

		double x = entity.getX();
		double y = entity.getY() + entity.getEyeHeight();
		double z = entity.getZ();
		((ServerLevel) entity.level()).sendParticles(ParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);

		entity.level().playSound(null, x, y, z, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.6F, 1.2F);

		CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_TURTLE.get(), 12000);
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		DamageSource source = event.getSource();

		if (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == CQRItems.CHESTPLATE_TURTLE.get() && source.getSourcePosition() != null) {
			Vec3 hitVec = source.getSourcePosition();
			double x = entity.getX() - hitVec.x;
			double z = entity.getZ() - hitVec.z;
			double yaw = Math.toDegrees(Math.atan2(-x, z));

			if (ItemUtil.compareRotations(entity.yBodyRot/*correct replacement for Yaw offset?*/, yaw, 45.0D)) {
				double y = (entity.getY() + entity.getBbHeight() * 0.5D) - hitVec.y;
				double pitch = -Math.toDegrees(Math.asin(y));

				if (ItemUtil.compareRotations(0.0D, pitch, 60.0D)) {
					if (!entity.level().isClientSide()) {
						entity.level().playSound(null, hitVec.x, hitVec.y, hitVec.z, SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 0.5F, 0.8F);
					}
					event.setAmount(event.getAmount() * 0.5F);
				}
			}
		}
	}

}
