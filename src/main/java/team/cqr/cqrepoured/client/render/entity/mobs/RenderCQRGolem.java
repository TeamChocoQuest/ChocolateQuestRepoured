package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRGolemGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;

public class RenderCQRGolem extends RenderCQRBipedBaseGeo<EntityCQRGolem> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/golem.png");
	
	public RenderCQRGolem(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolemGeo(CQRMain.prefix("geo/entity/biped_golem.geo.json"), TEXTURE, "mob/golem"));
		
		this.addLayer(new LayerGlowingAreasGeo<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRGolem currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRGolem currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGolem currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRGolem currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRGolem currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRGolem currentEntity, IBone bone) {
		
	}

}
