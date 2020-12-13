package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.models.entities.boss.ModelGiantTortoise;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRGiantTortoise extends RenderLiving<EntityCQRGiantTortoise> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise.png");

	public RenderCQRGiantTortoise(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGiantTortoise(), 1.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoise entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return TEXTURE;
	}

}
