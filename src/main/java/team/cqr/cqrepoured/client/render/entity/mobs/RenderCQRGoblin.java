package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRGoblinGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class RenderCQRGoblin extends RenderCQRBipedBaseGeo<EntityCQRGoblin> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/goblin.png");

	public RenderCQRGoblin(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGoblinGeo(CQRMain.prefix("geo/entity/biped_goblin.geo.json"), TEXTURE, "mob/goblin"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRGoblin currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRGoblin currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGoblin currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRGoblin currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRGoblin currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRGoblin currentEntity, IBone bone) {
		
	}

}
