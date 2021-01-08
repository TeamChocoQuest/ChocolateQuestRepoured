package team.cqr.cqrepoured.client.render.entity.boss;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.client.models.entities.boss.ModelEnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQREnderCalamity extends GeoEntityRenderer<EntityCQREnderCalamity> {

	private String entityName;
	private ResourceLocation texture;
	
	public double widthScale;
	public double heightScale;
	
	public RenderCQREnderCalamity(RenderManager renderManager) {
		this(renderManager, new ModelEnderCalamity(new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity.png")), "boss/ender_calamity");
	}
	
	protected RenderCQREnderCalamity(RenderManager renderManager, AnimatedGeoModel<EntityCQREnderCalamity> modelProvider, String entityName) {
		this(renderManager, modelProvider, entityName, 1D, 1D);
	}
	
	protected RenderCQREnderCalamity(RenderManager renderManager, AnimatedGeoModel<EntityCQREnderCalamity> modelProvider, String entityName, double widthScale, double heightScale) {
		super(renderManager, modelProvider);
		this.shadowSize = 0;
		this.entityName = entityName;
		this.widthScale = widthScale;
		this.heightScale = heightScale;
		
		this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + ".png");
	}

	@Override
	public void renderEarly(EntityCQREnderCalamity animatable, float ticks, float red, float green, float blue, float partialTicks) {
		//super.renderEarly(animatable, ticks, red, green, blue, partialTicks);
		double width = this.widthScale * animatable.getSizeVariation();
		double height = this.heightScale * animatable.getSizeVariation();
		GL11.glScaled(width, height, width);
	}
	
	
	
	@Override
	public ResourceLocation getTextureLocation(EntityCQREnderCalamity entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + "_" + entity.getTextureIndex() + ".png") : this.texture;
	}

}
