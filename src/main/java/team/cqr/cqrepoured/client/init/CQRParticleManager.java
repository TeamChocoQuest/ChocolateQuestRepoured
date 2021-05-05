package team.cqr.cqrepoured.client.init;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRParticleType;
import team.cqr.cqrepoured.particle.ParticleBeam;

@SideOnly(Side.CLIENT)
public class CQRParticleManager {

	private static final Int2ObjectMap<IParticleFactory> ID_PARTICLE_FACTORY_MAP = new Int2ObjectOpenHashMap<>();

	public static void init() {
		registerParticleFactory(CQRParticleType.BEAM, new ParticleBeam.Factory());
	}

	public static void registerParticleFactory(CQRParticleType particleType, IParticleFactory particleFactory) {
		ID_PARTICLE_FACTORY_MAP.put(particleType.getId(), particleFactory);
	}

	public static void spawnParticlesClient(CQRParticleType particleType, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
		if (optionalArguments.length < particleType.getArgumentCount()) {
			CQRMain.logger.warn("Not enough arguments to spawn particle! {}", particleType);
			return;
		}
		IParticleFactory factory = ID_PARTICLE_FACTORY_MAP.get(particleType.getId());
		if (factory == null) {
			CQRMain.logger.warn("No particle factory found! {}", particleType);
			return;
		}
		for (int i = 0; i < count; i++) {
			double x = xCoord;
			double y = yCoord;
			double z = zCoord;
			if (xOffset != 0.0D) {
				x += (Math.random() - Math.random()) * xOffset;
			}
			if (yOffset != 0.0D) {
				y += (Math.random() - Math.random()) * yOffset;
			}
			if (zOffset != 0.0D) {
				z += (Math.random() - Math.random()) * zOffset;
			}
			Particle particle = factory.createParticle(particleType.getId(), world, x, y, z, xSpeed, ySpeed, zSpeed, optionalArguments);
			// Can be replaced with a custom particle render manager too improve performance because particles with a custom texture are not batched.
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

}
