package team.cqr.cqrepoured.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.particles.BlockHighlightParticleData;

public class ParticleMagicBell extends Particle {

	public ParticleMagicBell(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int lifetime, int color, BlockPos pos) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		//MagicBellRenderer.getInstance().add(color, lifetime, pos);
	}

	public ParticleMagicBell(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int lifetime, int color, int entityId) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		//MagicBellRenderer.getInstance().add(color, lifetime, entityId);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BlockHighlightParticleData> {
		
		public Factory(IAnimatedSprite sprite) {
			//Ignore
		}
		
		@Override
		public Particle createParticle(BlockHighlightParticleData pType, ClientWorld pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
			int lifetime = pType.getLifetime();
			int color = pType.getColor();
			Particle result = null;
			if (pType.getBlockPos().isPresent()) {
				BlockPos pos = pType.getBlockPos().get();
				result = new ParticleMagicBell(pLevel, pX, pY, pZ, lifetime, color, pos);
			} else if (pType.getEntityID().isPresent()) {
				int entityId = pType.getEntityID().get();
				result = new ParticleMagicBell(pLevel, pX, pY, pZ, lifetime, color, entityId);
			}
			if (result == null) {
				result = new ParticleMagicBell(pLevel, pX, pY, pZ, lifetime, color, -1);
			}
			
			return result;
		}
	}

	@Override
	public void render(IVertexBuilder var1, ActiveRenderInfo var2, float var3) {
		
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.NO_RENDER;
	}

}
