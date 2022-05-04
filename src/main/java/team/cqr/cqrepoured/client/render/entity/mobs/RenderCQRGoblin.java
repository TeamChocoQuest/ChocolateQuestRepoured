package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRGoblinGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class RenderCQRGoblin extends RenderCQRBipedBaseGeo<EntityCQRGoblin> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/goblin.png");

	public RenderCQRGoblin(EntityRendererManager rendermanagerIn) {
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
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRGoblin currentEntity) {
		return null;
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGoblin currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGoblin currentEntity, IBone bone) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRGoblin currentEntity) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGoblin currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRGoblin currentEntity) {
		
	}

}
