package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRIllagerGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;

public class RenderCQRIllager extends RenderCQRBipedBaseGeo<EntityCQRIllager> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/illager_0.png");
	
	public RenderCQRIllager(EntityRendererManager rendermanager) {
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
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRIllager currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRIllager currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRIllager currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRIllager currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRIllager currentEntity, IBone bone) {
		
	}

}
