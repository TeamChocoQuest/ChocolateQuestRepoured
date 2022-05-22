package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.misc.EntityElectricField;

public class RenderElectricFieldEntity extends EntityRenderer<EntityElectricField> {

	public RenderElectricFieldEntity(EntityRendererManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void render(EntityElectricField entity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		long seed = (entity.getId() * 255L) ^ (entity.tickCount >> 1 << 1);
		ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(pMatrixStack, pBuffer, entity, 0, 0, 0, 5, seed);
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityElectricField pEntity) {
		return null;
	}

}
