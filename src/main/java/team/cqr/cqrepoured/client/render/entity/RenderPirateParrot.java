package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRPirateParrotHeldItem;

public class RenderPirateParrot extends ParrotRenderer {

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/pirate_parrot.png");

	public RenderPirateParrot(EntityRendererManager renderManager) {
		super(renderManager);

		this.addLayer(new LayerCQRPirateParrotHeldItem(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(ParrotEntity entity) {
		return TEXTURE;
	}

}
