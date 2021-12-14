package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorTurtle;
import team.cqr.cqrepoured.util.ItemUtil;

@EventBusSubscriber(modid = CQRMain.MODID)
public class TurtleArmorEventHandler {

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();

		if (ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			if (!entity.world.isRemote && entity.ticksExisted % 100 == 0) {
				entity.heal(1.0F);
				double x = entity.posX;
				double y = entity.posY + entity.getEyeHeight();
				double z = entity.posZ;
				((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 2, 0.5D, 0.5D, 0.5D, 1.0D);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!ItemUtil.hasFullSet(entity, ItemArmorTurtle.class)) {
			return;
		}
		if (entity.getHealth() - event.getAmount() > entity.getMaxHealth() * 0.2F) {
			return;
		}
		event.setAmount(Math.min(event.getAmount(), entity.getHealth() - 1.0F));
		entity.heal(entity.getMaxHealth() * 0.2F);
		entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 0, false, true));
		entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 4, false, true));
		entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 1, false, true));

		double x = entity.posX;
		double y = entity.posY + entity.getEyeHeight();
		double z = entity.posZ;
		((WorldServer) entity.world).spawnParticle(EnumParticleTypes.HEART, x, y, z, 4, 0.5D, 0.5D, 0.5D, 1.0D);

		entity.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 0.6F, 1.2F);

		CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_TURTLE, 12000);
	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();

		if (entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == CQRItems.CHESTPLATE_TURTLE && source.getDamageLocation() != null) {
			Vec3d hitVec = source.getDamageLocation();
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
