package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelGiantSpider;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantSpider;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRGiantSpider extends RenderLiving<EntityCQRGiantSpider> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_spider.png");

	public RenderCQRGiantSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGiantSpider(), 2.0F);
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

}
