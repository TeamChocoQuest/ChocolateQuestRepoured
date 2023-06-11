package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRGremlinGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGremlin;

public class RenderCQRGremlin extends RenderCQRBipedBaseGeo<EntityCQRGremlin> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/gremlin.png");

	public RenderCQRGremlin(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGremlinGeo(CQRMain.prefix("geo/entity/biped_gremlin.geo.json"), TEXTURE, "mob/gremlin"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRGremlin currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRGremlin currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGremlin currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRGremlin currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGremlin currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRGremlin currentEntity) {
		
	}

}
