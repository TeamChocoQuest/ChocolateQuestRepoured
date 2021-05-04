package team.cqr.cqrepoured.init;

import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRParticleManager;
import team.cqr.cqrepoured.network.server.packet.SPacketSpawnParticles;

public enum CQRParticleType {

	BEAM("beam", 0);

	private static final Map<String, CQRParticleType> NAME_PARTICLE_MAP = new HashMap<>();
	private static final Int2ObjectMap<CQRParticleType> ID_PARTICLE_MAP = new Int2ObjectOpenHashMap<>();

	static {
		for (CQRParticleType particleType : CQRParticleType.values()) {
			NAME_PARTICLE_MAP.put(particleType.name, particleType);
			ID_PARTICLE_MAP.put(particleType.id, particleType);
		}
	}

	private final String name;
	private final int id;
	private final int argumentCount;

	private CQRParticleType(String name, int id) {
		this(name, id, 0);
	}

	private CQRParticleType(String name, int id, int argumentCount) {
		this.name = name;
		this.id = id;
		this.argumentCount = argumentCount;
	}

	public static CQRParticleType byName(String name) {
		return NAME_PARTICLE_MAP.get(name);
	}

	public static CQRParticleType byId(int id) {
		return ID_PARTICLE_MAP.get(id);
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getArgumentCount() {
		return argumentCount;
	}

	public static void spawnParticles(int particleId, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
		CQRParticleType particleType = ID_PARTICLE_MAP.get(particleId);
		if (particleType == null) {
			return;
		}
		spawnParticles(particleType, world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments);
	}

	public static void spawnParticles(CQRParticleType particleType, World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int count, double xOffset, double yOffset, double zOffset, int... optionalArguments) {
		if (world.isRemote) {
			CQRParticleManager.spawnParticlesClient(particleType, world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments);
		} else {
			if (count == 1) {
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
				CQRMain.NETWORK.sendToAllTracking(new SPacketSpawnParticles(particleType.id, x, y, z, xSpeed, ySpeed, zSpeed, optionalArguments), new TargetPoint(world.provider.getDimension(), x, y, z, 0.0D));
			} else {
				CQRMain.NETWORK.sendToAllTracking(new SPacketSpawnParticles(particleType.id, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments), new TargetPoint(world.provider.getDimension(), xCoord, yCoord, zCoord, 0.0D));
			}
		}
	}

}
