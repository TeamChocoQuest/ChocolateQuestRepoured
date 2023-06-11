package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRDwarfGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDwarf;

public class RenderCQRDwarf extends RenderCQRBipedBaseGeo<EntityCQRDwarf> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/dwarf_0.png");

	public RenderCQRDwarf(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelCQRDwarfGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/dwarf"), 0.9F, 0.65F);
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
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRDwarf currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQRDwarf currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQRDwarf currentEntity, IBone bone) {
		
	}

}
