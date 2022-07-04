package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRZombieGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRZombie;

public class RenderCQRZombie extends RenderCQRBipedBaseGeo<EntityCQRZombie> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/zombie_0.png");
	
	public RenderCQRZombie(EntityRendererManager renderManager) {
		super(renderManager, new ModelCQRZombieGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "zombie"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRZombie currentEntity) {
		this.standardArmorCalculationForBone(boneName, currentEntity);		
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRZombie currentEntity) {
		this.standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRZombie currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRZombie currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRZombie currentEntity) {
		
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRZombie currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRZombie currentEntity, IBone bone) {
		
	}

}
