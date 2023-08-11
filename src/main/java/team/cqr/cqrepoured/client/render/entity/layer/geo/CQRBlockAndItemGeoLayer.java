package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

import com.mojang.blaze3d.vertex.PoseStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import team.cqr.cqrepoured.client.render.entity.StandardBipedBones;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class CQRBlockAndItemGeoLayer<T extends LivingEntity & GeoAnimatable> extends BlockAndItemGeoLayer<T> {
	
	public static <E extends LivingEntity & GeoAnimatable> Map<String, Triple<Function<E, ItemStack>, ItemDisplayContext, Optional<BiConsumer<E, PoseStack>>>> createStandardBipedCalcMap() {
		Map<String, Triple<Function<E, ItemStack>, ItemDisplayContext, Optional<BiConsumer<E, PoseStack>>>> result = new Object2ObjectArrayMap<>(3);
		
		result.put(StandardBipedBones.LEFT_HAND, Triple.of(
				(e) -> {
					if (e instanceof Mob m) {
						return m.isLeftHanded() ? m.getItemBySlot(EquipmentSlot.MAINHAND) : m.getItemBySlot(EquipmentSlot.OFFHAND);
					}
					return e.getItemBySlot(EquipmentSlot.OFFHAND);
				}, 
				ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, 
				Optional.empty()
			)
		);

		result.put(StandardBipedBones.RIGHT_HAND, Triple.of(
				(e) -> {
					if (e instanceof Mob m) {
						return !m.isLeftHanded() ? m.getItemBySlot(EquipmentSlot.MAINHAND) : m.getItemBySlot(EquipmentSlot.OFFHAND);
					}
					return e.getItemBySlot(EquipmentSlot.MAINHAND);
				}, 
				ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, 
				Optional.empty()
			)
		);
		
		result.put(StandardBipedBones.POTION_BONE, Triple.of(
				(e) -> {
					if (e instanceof AbstractEntityCQR aecqr && !aecqr.isHoldingPotion()) {
						return aecqr.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
					} else {
						return ItemStack.EMPTY;
					}
				}, 
				ItemDisplayContext.NONE, 
				Optional.of((e, p) -> {
					p.scale(0.5F, 0.5F, 0.5F);
				})
			)
		);
		
		return result;
	}
	
	protected final Map<String, Triple<Function<T, ItemStack>, ItemDisplayContext, Optional<BiConsumer<T, PoseStack>>>> CALCULATION_MAP;
	
	protected ItemStack currentItemAtBone = null;
	protected ItemDisplayContext currentTransformTypeAtBone = null;
	protected Optional<BiConsumer<T, PoseStack>> currentPoseTransformations = Optional.empty();

	public CQRBlockAndItemGeoLayer(GeoRenderer<T> renderer, final Map<String, Triple<Function<T, ItemStack>, ItemDisplayContext, Optional<BiConsumer<T, PoseStack>>>> calcMap) {
		super(renderer);
		this.CALCULATION_MAP = calcMap;
	}
	
	@Override
	protected ItemStack getStackForBone(GeoBone bone, T animatable) {
		Triple<Function<T, ItemStack>, ItemDisplayContext, Optional<BiConsumer<T, PoseStack>>> functionHolder = this.CALCULATION_MAP.getOrDefault(bone.getName(), null);
		if (functionHolder != null) {
			this.currentItemAtBone = functionHolder.getLeft().apply(animatable);
			this.currentTransformTypeAtBone = functionHolder.getMiddle();
			this.currentPoseTransformations = functionHolder.getRight();
			
			return this.currentItemAtBone;
		}
		return super.getStackForBone(bone, animatable);
	}
	
	@Override
	protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
		if (this.currentTransformTypeAtBone != null) {
			return this.currentTransformTypeAtBone;
		}
		return super.getTransformTypeForStack(bone, stack, animatable);
	}
	
	@Override
	protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
		poseStack.pushPose();
		
		if(this.currentPoseTransformations.isPresent()) {
			this.currentPoseTransformations.get().accept(animatable, poseStack);
		}
		
		super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
		
		poseStack.popPose();
	}
	
	

}
