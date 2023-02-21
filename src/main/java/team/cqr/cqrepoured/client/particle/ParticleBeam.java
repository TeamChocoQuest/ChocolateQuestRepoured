package team.cqr.cqrepoured.client.particle;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;

public class ParticleBeam extends Particle {

	public ParticleBeam(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.xd = xSpeedIn;
		this.yd = ySpeedIn;
		this.zd = zSpeedIn;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		
		public Factory(IAnimatedSprite sprite) {
			//Ignore
		}

		@Override
		public Particle createParticle(BasicParticleType particleType, ClientWorld world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleBeam(world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public void render(IVertexBuilder buffer, ActiveRenderInfo pRenderInfo, float partialTicks) {
		float f = 0.0F;
		float f1 = 1.0F;
		float f2 = 0.0F;
		float f3 = 1.0F;
		float f4 = 0.1F * this.particleScale;

		float f5 = (float) (this.xo + (this.x - this.xo) * partialTicks - interpPosX);
		float f6 = (float) (this.yo + (this.y - this.yo) * partialTicks - interpPosY);
		float f7 = (float) (this.zo + (this.z - this.zo) * partialTicks - interpPosZ);
		int i = this.getLightColor(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		
		Vector3f cameraViewDir = pRenderInfo.getLookVector();
		double rotationX;
		double rotationXY;
		double rotationZ;
		double rotationYZ;
		double rotationXZ;
		
		Vector3d[] avec3d = new Vector3d[] {
				new Vector3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationXZ * f4),
				new Vector3d(-rotationX * f4 + rotationXY * f4, rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4),
				new Vector3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4),
				new Vector3d(rotationX * f4 - rotationXY * f4, -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4) };

		if (this.roll != 0.0F) {
			float f8 = this.roll + (this.roll - this.oRoll) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * cameraViewDir.x();
			float f11 = MathHelper.sin(f8 * 0.5F) * cameraViewDir.y();
			float f12 = MathHelper.sin(f8 * 0.5F) * cameraViewDir.z();
			Vector3d vec3d = new Vector3d(f10, f11, f12);

			for (int l = 0; l < 4; ++l) {
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dot(vec3d)).add(avec3d[l].scale(f9 * f9 - vec3d.dot(vec3d))).add(vec3d.cross(avec3d[l]).scale(2.0F * f9));
			}
		}

		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bind(new ResourceLocation(CQRMain.MODID, "textures/particles/beam.png"));
		//Necessary?
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE);
		buffer.vertex(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).uv(f1, f3).color(this.rCol, this.gCol, this.bCol, this.alpha).lightmap(j, k).endVertex();
		buffer.vertex(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).uv(f1, f2).color(this.rCol, this.gCol, this.bCol, this.alpha).lightmap(j, k).endVertex();
		buffer.vertex(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).uv(f, f2).color(this.rCol, this.gCol, this.bCol, this.alpha).lightmap(j, k).endVertex();
		buffer.vertex(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).uv(f, f3).color(this.rCol, this.gCol, this.bCol, this.alpha).lightmap(j, k).endVertex();
		Tessellator.getInstance().end();
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

}
