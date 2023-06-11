package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQREndermanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerGlowingAreasGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;

@OnlyIn(Dist.CLIENT)
public class RenderCQREnderman extends RenderCQRBipedBaseGeo<EntityCQREnderman> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/enderman.png");

	public RenderCQREnderman(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQREndermanGeo<EntityCQREnderman>(CQRMain.prefix("geo/entity/biped_enderman.geo.json"), TEXTURE, "mob/enderman"));
		
		this.addLayer(new LayerGlowingAreasGeo<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQREnderman currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQREnderman currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQREnderman currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQREnderman currentEntity) {
		
	}

	@Override
	protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, EntityCQREnderman currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, EntityCQREnderman currentEntity) {
		
	}

}
