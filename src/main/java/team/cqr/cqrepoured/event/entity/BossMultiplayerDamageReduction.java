package team.cqr.cqrepoured.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

@EventBusSubscriber(modid = CQRMain.MODID)
public class BossMultiplayerDamageReduction {

	@SubscribeEvent
	public static void onLivingDamageEvent(LivingDamageEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof AbstractEntityCQRBoss)) {
			return;
		}

		int nearbyPlayerCount = 0;
		for (EntityPlayer player : entity.world.playerEntities) {
			if (!player.isCreative() && !player.isSpectator() && entity.getDistanceSq(player) < 100.0D * 100.0D) {
				nearbyPlayerCount++;
			}
		}
		if (nearbyPlayerCount > 1) {
			event.setAmount(event.getAmount() * (float) Math.pow(1.0D - CQRConfig.mobs.bossDamageReductionPerPlayer, nearbyPlayerCount - 1));
		}
	}

}
