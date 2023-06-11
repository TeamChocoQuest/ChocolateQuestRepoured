package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRIllagerGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;

public class RenderCQRIllager extends RenderCQRBipedBaseGeo<EntityCQRIllager> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/illager_0.png");
	
	public RenderCQRIllager(EntityRendererProvider.Context rendermanager) {
		super(rendermanager, new ModelCQRIllagerGeo(CQRMain.prefix("geo/entity/biped_illager.geo.json"), TEXTURE, "mob/illager"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRIllager currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRIllager currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRIllager currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRIllager currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRIllager currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRIllager currentEntity, IBone bone) {
		
	}

}
