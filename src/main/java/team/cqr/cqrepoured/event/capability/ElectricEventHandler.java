package team.cqr.cqrepoured.event.capability;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;

@EventBusSubscriber(modid = CQRMain.MODID)
public class ElectricEventHandler {

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity entity = event.getTarget();
		if (!(entity instanceof LivingEntity) || !checkForCapabilityAndServerSide((LivingEntity) entity) || !(entity instanceof ServerPlayerEntity)) {
			return;
		}
		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability((LivingEntity) entity), (ServerPlayerEntity) event.getEntityPlayer());
	}

	@SubscribeEvent
	public static void onLogIn(PlayerLoggedInEvent event) {
		LivingEntity entity = event.player;
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof ServerPlayerEntity)) {
			return;
		}
		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (ServerPlayerEntity) entity);
	}

	@SubscribeEvent
	public static void onRespawn(PlayerRespawnEvent event) {
		LivingEntity entity = event.player;
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof ServerPlayerEntity)) {
			return;
		}
		// First, reduce the ticks
		CapabilityElectricShock icapability = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);

		icapability.setRemainingTicks(-1);
		// We don't need to send the update ourselves, the capability handles it itself in the setter
		// CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (EntityPlayerMP) entity);
	}

	@SubscribeEvent
	public static void onChangeDimension(PlayerChangedDimensionEvent event) {
		LivingEntity entity = event.player;
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof ServerPlayerEntity)) {
			return;
		}
		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (ServerPlayerEntity) entity);
	}

	private static boolean checkForCapabilityAndServerSide(LivingEntity entity) {
		if (!entity.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			return false;
		}

		if (entity.world.isRemote) {
			// If we are on the remote end, we don't do anything
			return false;
		}
		return true;
	}

	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (!checkForCapabilityAndServerSide(entity)) {
			return;
		}

		// First, reduce the ticks
		CapabilityElectricShock currentCap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		currentCap.reduceRemainingTicks();

		// Mechanicals can get electrocuted but don't take damage
		if (entity instanceof IMechanical || entity.getCreatureAttribute() == CQRCreatureAttributes.MECHANICAL) {
			// But, if we are wet, we get damage from beign electrocuted
			if (((IMechanical) entity).canReceiveElectricDamageCurrently()) {
				currentCap.setRemainingTicks(100);
				entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2);
			}
		} else if (currentCap.isElectrocutionActive()) {
			entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1);
		}
		if (entity.isDead || entity.getHealth() < 1) {
			currentCap.setTarget(null);
		}
		// Maybe you could spread to other entities?
		if (currentCap.canSpread() && currentCap.getTarget() == null && currentCap.getRemainignSpreads() > 0) {
			spreadElectrocute(entity, currentCap);
		} else if (currentCap.getTarget() != null) {
			if (!entity.canEntityBeSeen(currentCap.getTarget()) || entity.getDistance(currentCap.getTarget()) > 16) {
				currentCap.setTarget(null);
			} else {
				CapabilityElectricShock targetCap = currentCap.getTarget().getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
				if (targetCap != null) {
					targetCap.setRemainingTicks(100);
				}
			}
		}
	}

	private static void spreadElectrocute(LivingEntity spreader, CapabilityElectricShock sourceCap) {
		// First, get all applicable entities in range
		List<LivingEntity> entities = spreader.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, spreader.getEntityBoundingBox().grow(12), Predicates.and(TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED, entityLiving -> {
			if (entityLiving.getPersistentID().equals(sourceCap.getCasterID())) {
				return false;
			}
			if (!spreader.canEntityBeSeen(entityLiving)) {
				return false;
			}
			if (spreader.getDistance(entityLiving) > CQRConfig.general.electricFieldEffectSpreadRange) {
				return false;
			}
			return !TargetUtil.isAllyCheckingLeaders(spreader, entityLiving);
		}));
		if (entities.isEmpty()) {
			return;
		}
		LivingEntity chosen = entities.get(spreader.world.rand.nextInt(entities.size()));
		sourceCap.setTarget(chosen);
		sourceCap.reduceSpreads();

		CapabilityElectricShock targetCap = chosen.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		targetCap.setRemainingTicks(100);
		targetCap.setCasterID(sourceCap.getCasterID());
		if (targetCap.getRemainignSpreads() < 0 || targetCap.getRemainignSpreads() >= sourceCap.getRemainignSpreads()) {
			targetCap.setRemainingSpreads(sourceCap.getRemainignSpreads() - 1);
		}
	}

}
