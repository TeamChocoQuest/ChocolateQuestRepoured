package team.cqr.cqrepoured.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleBlockHighlight extends Particle {

	public ParticleBlockHighlight(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int lifetime) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.particleMaxAge = lifetime;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY,
			float rotationXZ) {
	}

	public BlockPos getPos() {
		return new BlockPos(this.posX, this.posY, this.posZ);
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn,
				double zSpeedIn, int... optionalArguments) {
			return new ParticleBlockHighlight(worldIn, xCoordIn, yCoordIn, zCoordIn, optionalArguments.length > 0 ? optionalArguments[0] : 20);
		}
	}

}
