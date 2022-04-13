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
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			if (!entity.level.isClientSide && entity.tickCount % 100 == 0) {
				entity.heal(1.0F);
				double x = entity.getX();
				double y = entity.getY() + entity.getEyeHeight();
				double z = entity.getZ();
				((ServerWorld) entity.level).addParticle(ParticleTypes.HEART, x, y, z, /*2,*/ 0.5D, 0.5D, 0.5D/*, 1.0D*/);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		LivingEntity entity = event.getEntityLiving();
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
		entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 0, false, true));
		entity.addEffect(new EffectInstance(Effects.REGENERATION, 100, 4, false, true));
		entity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 100, 1, false, true));

		double x = entity.getX();
		double y = entity.getY() + entity.getEyeHeight();
		double z = entity.getZ();
		((ServerWorld) entity.level).addParticle(ParticleTypes.HEART, x, y, z, /*4,*/ 0.5D, 0.5D, 0.5D/*, 1.0D*/);

		entity.level.playSound(null, x, y, z, SoundEvents.GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);

		CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_TURTLE.get(), 12000);
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();
		DamageSource source = event.getSource();

		if (entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == CQRItems.CHESTPLATE_TURTLE.get() && source.getSourcePosition() != null) {
			Vector3d hitVec = source.getSourcePosition();
			double x = entity.getX() - hitVec.x;
			double z = entity.getZ() - hitVec.z;
			double yaw = Math.toDegrees(Math.atan2(-x, z));

			if (ItemUtil.compareRotations(entity.yBodyRot/*correct replacement for Yaw offset?*/, yaw, 45.0D)) {
				double y = (entity.getY() + entity.getBbHeight() * 0.5D) - hitVec.y;
				double pitch = -Math.toDegrees(Math.asin(y));

				if (ItemUtil.compareRotations(0.0D, pitch, 60.0D)) {
					if (!entity.level.isClientSide) {
						entity.level.playSound(null, hitVec.x, hitVec.y, hitVec.z, SoundEvents.SHIELD_BLOCK, SoundCategory.NEUTRAL, 0.5F, 0.8F);
					}
					event.setAmount(event.getAmount() * 0.5F);
				}
			}
		}
	}

}
