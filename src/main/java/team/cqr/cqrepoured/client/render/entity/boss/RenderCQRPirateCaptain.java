package team.cqr.cqrepoured.client.render.entity.boss;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelPirateCaptainGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;

public class RenderCQRPirateCaptain extends RenderCQRBipedBaseGeo<EntityCQRPirateCaptain> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/boss/pirate_captain.png");

	public RenderCQRPirateCaptain(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelPirateCaptainGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "boss/pirate_captain"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRPirateCaptain currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRPirateCaptain currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRPirateCaptain currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRPirateCaptain currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRPirateCaptain currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRPirateCaptain currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRPirateCaptain currentEntity) {
		
	}

}
