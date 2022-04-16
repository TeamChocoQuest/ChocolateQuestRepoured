package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRDwarfGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDwarf;

public class RenderCQRDwarf extends RenderCQRBipedBaseGeo<EntityCQRDwarf> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/dwarf_0.png");

	public RenderCQRDwarf(EntityRendererManager renderManager, AnimatedGeoModel<EntityCQRDwarf> modelProvider) {
		super(renderManager, new ModelCQRDwarfGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "dwarf"), 0.9F, 0.65F);
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRDwarf currentEntity) {
		this.standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRDwarf currentEntity) {
		this.standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRDwarf currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRDwarf currentEntity) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRDwarf currentEntity) {
		
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRDwarf currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRDwarf currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRDwarf currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRDwarf currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRDwarf currentEntity, IBone bone) {
		
	}

}
