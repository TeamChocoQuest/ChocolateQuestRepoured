package team.cqr.cqrepoured.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.model.entity.ModelIceSpike;
import team.cqr.cqrepoured.entity.misc.EntityIceSpike;

public class RenderIceSpike extends EntityRenderer<EntityIceSpike> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/ice_packed.png");

	private final ModelIceSpike<EntityIceSpike> model = new ModelIceSpike<>();

	public RenderIceSpike(EntityRendererProvider.Context p_i46179_1_) {
		super(p_i46179_1_);
	}
	
	@Override
	public void render(EntityIceSpike pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		pMatrixStack.pushPose();
		
		VertexConsumer ivertexbuilder = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
		this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, pPackedLight, pPackedLight, pEntityYaw, pPartialTicks, pPackedLight);
		
		pMatrixStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityIceSpike pEntity) {
		return TEXTURE;
	}
	
}
