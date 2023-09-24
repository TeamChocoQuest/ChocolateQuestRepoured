package team.cqr.cqrepoured.client.event;

import de.dertoaster.multihitboxlib.api.event.client.PartRendererRegistrationEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.render.entity.boss.exterminator.RenderExterminatorBackpackPart;
import team.cqr.cqrepoured.client.render.entity.boss.netherdragon.RenderNetherDragonBodyPart;
import team.cqr.cqrepoured.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;
import team.cqr.cqrepoured.entity.boss.netherdragon.SubEntityNetherDragonSegment;

@EventBusSubscriber(modid = CQRConstants.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class MHLibEventListener {

	@SubscribeEvent
	public static void onPartRendererRegistration(final PartRendererRegistrationEvent event) {
		event.tryAdd(SubEntityNetherDragonSegment.class, RenderNetherDragonBodyPart::new);
		event.tryAdd(SubEntityExterminatorFieldEmitter.class, RenderExterminatorBackpackPart::new);
	}
	
}
