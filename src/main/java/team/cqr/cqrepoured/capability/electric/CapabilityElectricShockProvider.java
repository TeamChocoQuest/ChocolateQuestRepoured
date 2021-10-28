package team.cqr.cqrepoured.capability.electric;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;
import team.cqr.cqrepoured.objects.entity.IMechanical;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.util.Reference;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilityElectricShockProvider extends SerializableCapabilityProvider<CapabilityElectricShock> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "entity_electrocute_handler");

	@CapabilityInject(CapabilityElectricShock.class)
	public static final Capability<CapabilityElectricShock> ELECTROCUTE_HANDLER_CQR = null;

	public CapabilityElectricShockProvider(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityElectricShock.class, new CapabilityElectricShockStorage(), () -> new CapabilityElectricShock(null));
	}

	public static CapabilityElectricShockProvider createProvider(EntityLivingBase entity) {
		return new CapabilityElectricShockProvider(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, new CapabilityElectricShock(entity));
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity entity = event.getTarget();
		if (!(entity instanceof EntityLivingBase) || !checkForCapabilityAndServerSide((EntityLivingBase) entity) || !(entity instanceof EntityPlayerMP)) {
			return;
		}
		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability((EntityLivingBase) entity), (EntityPlayerMP) event.getEntityPlayer());
	}

	@SubscribeEvent
	public static void onLogIn(PlayerLoggedInEvent event) {
		EntityLivingBase entity = event.player;
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof EntityPlayerMP)) {
			return;
		}
		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (EntityPlayerMP) entity);
	}

	@SubscribeEvent
	public static void onRespawn(PlayerRespawnEvent event) {
		EntityLivingBase entity = event.player;
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof EntityPlayerMP)) {
			return;
		}
		// First, reduce the ticks
		CapabilityElectricShock icapability = entity.getCapability(ELECTROCUTE_HANDLER_CQR, null);

		icapability.setRemainingTicks(-1);

		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (EntityPlayerMP) entity);
	}

	@SubscribeEvent
	public static void onChangeDimension(PlayerChangedDimensionEvent event) {
		EntityLivingBase entity = event.player;
		if (!checkForCapabilityAndServerSide(entity) || !(entity instanceof EntityPlayerMP)) {
			return;
		}
		CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (EntityPlayerMP) entity);
	}

	private static boolean checkForCapabilityAndServerSide(EntityLivingBase entity) {
		if (!entity.hasCapability(ELECTROCUTE_HANDLER_CQR, null)) {
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
		EntityLivingBase entity = event.getEntityLiving();
		if (!checkForCapabilityAndServerSide(entity)) {
			return;
		}

		// First, reduce the ticks
		CapabilityElectricShock icapability = entity.getCapability(ELECTROCUTE_HANDLER_CQR, null);
		final int beforeUpdateTicks = icapability.getRemainingTicks();
		final boolean beforeUpdateHasTarget = icapability.getTarget() != null;
		icapability.reduceRemainingTicks();

		// Mechanicals can get electrocuted but don't take damage
		if (entity instanceof IMechanical || entity.getCreatureAttribute() == CQRCreatureAttributes.CREATURE_TYPE_MECHANICAL) {
			// But, if we are wet, we get damage from beign electrocuted
			if (entity.isInWater()) {
				icapability.setRemainingTicks(100);
				entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2);
			}
		} else if (icapability.getRemainingTicks() >= 0) {
			entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1);
		}
		if (entity.isDead || entity.getHealth() < 1) {
			icapability.setTarget(null);
		}
		// Maybe you could spread to other entities?
		if (icapability.getRemainingTicks() > 50 && icapability.getTarget() == null && icapability.getRemainignSpreads() > 0) {
			spreadElectrocute(entity, icapability);
		} else if (icapability.getTarget() != null) {
			if (!entity.canEntityBeSeen(icapability.getTarget()) || entity.getDistance(icapability.getTarget()) > 16) {
				icapability.setTarget(null);
			} else {
				CapabilityElectricShock targetCap = icapability.getTarget().getCapability(ELECTROCUTE_HANDLER_CQR, null);
				if (targetCap != null) {
					targetCap.setRemainingTicks(100);
				}
			}
		}
		
		final int afterUpdateTicks = icapability.getRemainingTicks();
		final boolean afterUpdateHasTarget = icapability.getTarget() != null;
		if(afterUpdateHasTarget != beforeUpdateHasTarget || (afterUpdateTicks <= 0 && beforeUpdateTicks > 0) || (afterUpdateTicks > 0 && beforeUpdateTicks <= 0)) {
			CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateElectrocuteCapability(entity), entity);
			if(entity instanceof EntityPlayerMP) {
				CQRMain.NETWORK.sendTo(new SPacketUpdateElectrocuteCapability(entity), (EntityPlayerMP) entity);
			}
		}
	}

	private static void spreadElectrocute(EntityLivingBase spreader, CapabilityElectricShock icapability) {
		// First, get all applicable entities in range
		List<EntityLivingBase> entities = spreader.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, spreader.getEntityBoundingBox().grow(12), TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED);
		entities.removeIf((EntityLivingBase input) -> {
			if (!spreader.canEntityBeSeen(input) || spreader.getDistance(input) > CQRConfig.general.electricFieldEffectSpreadRange) {
				return true;
			}
			return !TargetUtil.isAllyCheckingLeaders(spreader, input);
		});
		if (entities.isEmpty()) {
			return;
		}
		Collections.shuffle(entities);
		EntityLivingBase chosen = entities.get(0);
		icapability.setTarget(chosen);
		icapability.reduceSpreads();

		CapabilityElectricShock targetCap = chosen.getCapability(ELECTROCUTE_HANDLER_CQR, null);
		targetCap.setRemainingTicks(100);
		if(targetCap.getRemainignSpreads() < 0) {
			targetCap.setRemainingSpreads(icapability.getRemainignSpreads());
		}
	}

}
