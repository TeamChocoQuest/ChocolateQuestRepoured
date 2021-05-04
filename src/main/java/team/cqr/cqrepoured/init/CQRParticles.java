package team.cqr.cqrepoured.init;

import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketSpawnParticles;
import team.cqr.cqrepoured.particle.ParticleBeam;
import team.cqr.cqrepoured.util.Reference;

public class CQRParticles {

	private static final Map<ResourceLocation, ParticleType> NAME_PARTICLE_MAP = new HashMap<>();
	private static final Int2ObjectMap<ParticleType> ID_PARTICLE_MAP = new Int2ObjectOpenHashMap<>();

	public static final ParticleType BEAM = registerParticleType("beam", new ParticleBeam.Factory());

	private static ParticleType registerParticleType(String name, IParticleFactory particleFactory) {
		ParticleType particleType = new ParticleType(new ResourceLocation(Reference.MODID, name), particleFactory);
		NAME_PARTICLE_MAP.put(particleType.registryName, particleType);
		ID_PARTICLE_MAP.put(particleType.particleTypeId, particleType);
		return particleType;
	}

	public static void spawnParticles(int particleId, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
		ParticleType particleType = ID_PARTICLE_MAP.get(particleId);
		if (particleType == null) {
			return;
		}
		spawnParticles(particleType, world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments);
	}

	public static void spawnParticles(ParticleType particleType, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
		if (world.isRemote) {
			spawnParticlesClient(particleType, world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments);
		} else {
			CQRMain.NETWORK.sendToDimension(new SPacketSpawnParticles(particleType.particleTypeId, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments), world.provider.getDimension());
		}
	}

	@SideOnly(Side.CLIENT)
	private static void spawnParticlesClient(ParticleType particleType, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
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
				y += (Math.random() - Math.random()) * zOffset;
			}
			Particle particle = particleType.particleFactory.createParticle(particleType.particleTypeId, world, x, y, z, xSpeed, ySpeed, zSpeed, optionalArguments);
			// Can be replaced with a custom particle render manager too improve performance because particles with a custom texture are not batched.
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	public static class ParticleType {

		private static int nextParticleTypeId = 0;
		private final int particleTypeId = nextParticleTypeId++;
		private final ResourceLocation registryName;
		private final IParticleFactory particleFactory;

		public ParticleType(ResourceLocation registryName, IParticleFactory particleFactory) {
			this.registryName = registryName;
			this.particleFactory = particleFactory;
		}

		public int getParticleTypeId() {
			return particleTypeId;
		}

		public ResourceLocation getRegistryName() {
			return registryName;
		}

		public IParticleFactory getParticleFactory() {
			return particleFactory;
		}

	}

}
