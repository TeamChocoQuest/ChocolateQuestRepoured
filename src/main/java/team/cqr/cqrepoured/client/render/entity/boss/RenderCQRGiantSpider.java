package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelGiantSpider;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.entity.boss.EntityCQRGiantSpider;

public class RenderCQRGiantSpider extends MobRenderer<EntityCQRGiantSpider> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/giant_spider.png");

	public RenderCQRGiantSpider(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGiantSpider(), 2.0F);
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantSpider entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return TEXTURE;
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRGiantSpider entityLivingBaseIn) {
		return 180F;
	}

}
