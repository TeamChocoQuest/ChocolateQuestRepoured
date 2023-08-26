package team.cqr.cqrepoured.client.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.CQRConstants;

@Mod.EventBusSubscriber(modid = CQRConstants.MODID, bus = Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class CQRParticles {
	
	@SubscribeEvent
	public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		
		ParticleManager pm = mc.particleEngine;
		
		//pm.register(CQRParticleTypes.BEAM.get(), ParticleBeam.Factory::new);
		//pm.register(CQRParticleTypes.BLOCK_HIGHLIGHT.get(), ParticleMagicBell.Factory::new);
	}

}
