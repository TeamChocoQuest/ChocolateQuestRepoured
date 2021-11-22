package team.cqr.cqrepoured.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.render.MagicBellRenderer;

public class ParticleMagicBell extends Particle {

	public ParticleMagicBell(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int lifetime, int color, BlockPos pos) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		MagicBellRenderer.getInstance().add(color, lifetime, pos);
	}

	public ParticleMagicBell(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int lifetime, int color, int entityId) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		MagicBellRenderer.getInstance().add(color, lifetime, entityId);
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY,
			float rotationXZ) {
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		@Override
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn,
				double zSpeedIn, int... optionalArguments) {
			int lifetime = optionalArguments.length >= 1 ? optionalArguments[0] : 20;
			int color = optionalArguments.length >= 2 ? optionalArguments[1] : 0xFFFFFF;
			if (optionalArguments.length >= 5) {
				BlockPos pos = new BlockPos(optionalArguments[2], optionalArguments[3], optionalArguments[4]);
				return new ParticleMagicBell(worldIn, xCoordIn, yCoordIn, zCoordIn, lifetime, color, pos);
			} else {
				int entityId = optionalArguments.length >= 3 ? optionalArguments[2] : -1;
				return new ParticleMagicBell(worldIn, xCoordIn, yCoordIn, zCoordIn, lifetime, color, entityId);
			}
		}
	}

}
