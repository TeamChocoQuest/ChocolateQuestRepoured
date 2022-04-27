package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRBoarmanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRBoarman;

public class RenderCQRBoarman extends RenderCQRBipedBaseGeo<EntityCQRBoarman> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/boarman_0.png");

	public RenderCQRBoarman(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarmanGeo(CQRMain.prefix("geo/entity/biped_boarman.geo.json"), TEXTURE, "mob/boarman"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRBoarman currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRBoarman currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRBoarman currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRBoarman currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRBoarman currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRBoarman currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRBoarman currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRBoarman currentEntity, IBone bone) {
		
	}

}
