package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRMinotaurGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMinotaur;

public class RenderCQRMinotaur extends RenderCQRBipedBaseGeo<EntityCQRMinotaur> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/minotaur.png");
	
	public RenderCQRMinotaur(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMinotaurGeo(CQRMain.prefix("geo/entity/biped_minotaur.geo.json"), TEXTURE, "mob/minotaur"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRMinotaur currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRMinotaur currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRMinotaur currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRMinotaur currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, EntityCQRMinotaur currentEntity) {
		
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRMinotaur currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRMinotaur currentEntity, IBone bone) {
		
	}

}
