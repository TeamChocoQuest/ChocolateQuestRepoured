package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

import com.mojang.blaze3d.vertex.PoseStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.ModelExterminatorGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.StandardBipedBones;
import team.cqr.cqrepoured.client.render.entity.layer.geo.CQRBlockAndItemGeoLayer;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;

public class RenderCQRExterminator extends RenderCQREntityGeo<EntityCQRExterminator> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/exterminator.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRMain.MODID, "geo/entity/boss/exterminator.geo.json");

	public RenderCQRExterminator(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelExterminatorGeo(MODEL_RESLOC, TEXTURE, "boss/exterminator"));

		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

	@Override
	protected Optional<ItemArmorGeoLayer<EntityCQRExterminator>> createArmorLayer(RenderCQREntityGeo<EntityCQRExterminator> renderCQREntityGeo) {
		// TODO: Create own armor layer
		return Optional.empty();
	}
	
	@Override
	protected Optional<BlockAndItemGeoLayer<EntityCQRExterminator>> createBlockAndItemLayer(RenderCQREntityGeo<EntityCQRExterminator> renderCQREntityGeo) {
		Map<String, Triple<Function<EntityCQRExterminator, ItemStack>, ItemDisplayContext, Optional<BiConsumer<EntityCQRExterminator, PoseStack>>>> map = new Object2ObjectArrayMap<>(1);
		
		map.put(HAND_IDENT_LEFT, Triple.of(
				(e) -> {
					return e.getItemInHand(InteractionHand.MAIN_HAND);
				}, 
				ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, 
				Optional.of((e, p) -> {
					p.scale(1.5F, 1.5F, 1.5F);
				})
			)
		);
		
		BlockAndItemGeoLayer<EntityCQRExterminator> layer = new CQRBlockAndItemGeoLayer<>(this, map);
		return Optional.of(layer);
	}
	
	public static final String HAND_IDENT_LEFT = "item_left_hand";

	@Override
	protected float getDeathMaxRotation(EntityCQRExterminator entityLivingBaseIn) {
		return 0.0F;
	}

	/*@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRExterminator currentEntity) {
		if (boneName.equalsIgnoreCase(HAND_IDENT_LEFT)) {
			//TODO: Figure out values
			//move left or right (from the entity's POV, positive: Right), move up or down the arm, move above (negative) or under the arm (positive)
			GlStateManager.translate(0.0, 0.0, -0.25);
			//Since the golem is massive we need to scale it up a bit
			
			//Standard code from LayerHeldItem
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			if(!(item.getItem() instanceof ItemFlamethrower)) {
				GlStateManager.rotate(100, 1, 0, 0);
				GlStateManager.translate(0, -0.3, -0.2);
				GlStateManager.scale(1.25, 1.25, 1.25);
			} else {
				//Different scale cause flamethrower is very small
				GlStateManager.scale(1.5, 1.5, 1.5);
			}
		}
	}*/
	
	@Override
	public RenderType getRenderType(EntityCQRExterminator animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityCutoutNoCull(texture);
	}
	
	@Override
	protected ResourceLocation getTextureOverrideForBone(GeoBone bone, EntityCQRExterminator animatable, float partialTick) {
		if(bone.getName().equalsIgnoreCase(StandardBipedBones.CAPE_BONE) && animatable.hasCape()) {
			return animatable.getResourceLocationOfCape();
		}
		return null;
	}

}
