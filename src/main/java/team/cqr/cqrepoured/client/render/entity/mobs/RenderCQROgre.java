package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQROgreGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQROgre;

public class RenderCQROgre extends RenderCQRBipedBaseGeo<EntityCQROgre> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/ogre.png");
	
	public RenderCQROgre(Context renderManager) {
		super(renderManager, new ModelCQROgreGeo(CQRMain.prefix("geo/entity/biped_ogre.geo.json"), TEXTURE, "mob/ogre"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQROgre currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQROgre currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQROgre currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQROgre currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQROgre currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQROgre currentEntity, IBone bone) {
		
	}


}
