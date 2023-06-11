package team.cqr.cqrepoured.entity.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public class ParticleWalkerTornado extends EntityParticle {

	public ParticleWalkerTornado(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		this.xd = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
		this.yd = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
		this.zd = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
		float f = this.random.nextFloat() * 0.3F + 0.7F;
		this.rCol = f;
		this.gCol = f;
		this.bCol = f;
		this.scale(this.random.nextFloat() * this.random.nextFloat() * 6.0F + 1.0F);
		this.lifetime = (int) (16.0D / (this.random.nextFloat() * 0.8D + 0.2D)) + 2;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.setAlpha(7 - this.age * 8 / this.lifetime); //Correct replacement for setParticleIndex?
		this.yd += 0.004D;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.8999999761581421D;
		this.yd *= 0.8999999761581421D;
		this.zd *= 0.8999999761581421D;

		if (this.onGround) {
			this.xd *= 0.699999988079071D;
			this.zd *= 0.699999988079071D;
		}
	}

}
