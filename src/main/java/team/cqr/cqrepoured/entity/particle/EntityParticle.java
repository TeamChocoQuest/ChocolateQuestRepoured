package team.cqr.cqrepoured.entity.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;

public class EntityParticle extends Particle {

	public EntityParticle(final ClientWorld worldIn, final double posXIn, final double posYIn, final double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	public EntityParticle(final ClientWorld worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
	}

	public void setMotionX(final double motionX) {
		this.xd = motionX;
	}

	public void setMotionY(final double motionY) {
		this.yd = motionY;
	}

	public void setMotionZ(final double motionZ) {
		this.zd = motionZ;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public double getMotionX() {
		return this.xd;
	}

	public double getMotionY() {
		return this.yd;
	}

	public double getMotionZ() {
		return this.zd;
	}

	@Override
	public void render(IVertexBuilder pBuffer, ActiveRenderInfo pRenderInfo, float pPartialTicks) {
		
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
