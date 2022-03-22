package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.entity.ModelIceSpike;
import team.cqr.cqrepoured.entity.misc.EntityIceSpike;

public class RenderIceSpike extends EntityRenderer<EntityIceSpike> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/ice_packed.png");

	private final ModelIceSpike<EntityIceSpike> model = new ModelIceSpike<>();

	public RenderIceSpike(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
	}
	
	@Override
	public void render(EntityIceSpike pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		pMatrixStack.pushPose();
		
		IVertexBuilder ivertexbuilder = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
		this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, pPackedLight, pPackedLight, pEntityYaw, pPartialTicks, pPackedLight);
		
		pMatrixStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityIceSpike pEntity) {
		return TEXTURE;
	}
	
}
