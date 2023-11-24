package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.ItemArmorGeoLayer;
import team.cqr.cqrepoured.client.render.entity.StandardBipedBones;

public class CQRItemArmorGeoLayer<T extends LivingEntity & GeoAnimatable> extends ItemArmorGeoLayer<T> {
	
	protected static final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_HEAD = (armorModel) -> armorModel.head;
	protected static final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_BODY = (armorModel) -> armorModel.body;
	protected static final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_ARM_LEFT = (armorModel) -> armorModel.leftArm;
	protected static final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_ARM_RIGHT = (armorModel) -> armorModel.rightArm;
	protected static final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_LEG_LEFT = (armorModel) -> armorModel.leftLeg;
	protected static final Function<HumanoidModel<?>, ModelPart> GET_ARMOR_LEG_RIGHT = (armorModel) -> armorModel.rightLeg;
	
	public static <E extends LivingEntity & GeoAnimatable> Triple<Function<E, ItemStack>, Function<HumanoidModel<?>, ModelPart>, EquipmentSlot> constructTriple(EquipmentSlot slot, Function<HumanoidModel<?>, ModelPart> partFunc) {
		final Function<E, ItemStack> itemFunc = (e) -> {
			return e.getItemBySlot(slot);
		};
		return Triple.of(itemFunc,  partFunc, slot);
	}
	
	public static <E extends LivingEntity & GeoAnimatable> Map<String, Triple<Function<E, ItemStack>, Function<HumanoidModel<?>, ModelPart>, EquipmentSlot>> createStandardBipedCalcMap() {
		Map<String, Triple<Function<E, ItemStack>, Function<HumanoidModel<?>, ModelPart>, EquipmentSlot>> result = new Object2ObjectArrayMap<>(8);
		
		result.put(StandardBipedBones.ARMOR_BODY, constructTriple(EquipmentSlot.CHEST, GET_ARMOR_BODY));
		result.put(StandardBipedBones.ARMOR_HEAD, constructTriple(EquipmentSlot.HEAD, GET_ARMOR_HEAD));
		result.put(StandardBipedBones.ARMOR_LEFT_ARM, constructTriple(EquipmentSlot.CHEST, GET_ARMOR_ARM_LEFT));
		result.put(StandardBipedBones.ARMOR_RIGHT_ARM, constructTriple(EquipmentSlot.CHEST, GET_ARMOR_ARM_RIGHT));
		result.put(StandardBipedBones.ARMOR_LEFT_LEG, constructTriple(EquipmentSlot.LEGS, GET_ARMOR_LEG_LEFT));
		result.put(StandardBipedBones.ARMOR_RIGHT_LEG, constructTriple(EquipmentSlot.LEGS, GET_ARMOR_LEG_RIGHT));
		result.put(StandardBipedBones.ARMOR_LEFT_FOOT, constructTriple(EquipmentSlot.FEET, GET_ARMOR_LEG_LEFT));
		result.put(StandardBipedBones.ARMOR_RIGHT_FOOT, constructTriple(EquipmentSlot.FEET, GET_ARMOR_LEG_RIGHT));
		
		return result;
	}
	
	protected final Map<String, Triple<Function<T, ItemStack>, Function<HumanoidModel<?>, ModelPart>, EquipmentSlot>> CALCULATION_MAP;
	
	protected ItemStack currentArmorAtBone = null;
	protected EquipmentSlot currentSlotAtBone = null;
	protected Function<HumanoidModel<?>, ModelPart> currentArmorPartAtBone = null;
	
	public CQRItemArmorGeoLayer(GeoRenderer<T> geoRenderer, final Map<String, Triple<Function<T, ItemStack>, Function<HumanoidModel<?>, ModelPart>, EquipmentSlot>> calcMap) {
		super(geoRenderer);
		this.CALCULATION_MAP = calcMap;
	}

	@Override
	protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
		Triple<Function<T, ItemStack>, Function<HumanoidModel<?>, ModelPart>, EquipmentSlot> functionHolder = this.CALCULATION_MAP.getOrDefault(bone.getName(), null);
		if (functionHolder != null) {
			this.currentArmorAtBone = functionHolder.getLeft().apply(animatable);
			this.currentArmorPartAtBone = functionHolder.getMiddle();
			this.currentSlotAtBone = functionHolder.getRight();
			
			return this.currentArmorAtBone;
		}
		return super.getArmorItemForBone(bone, animatable);
	}
	
	@Override
	protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
		if (this.currentSlotAtBone != null) {
			return this.currentSlotAtBone;
		}
		return super.getEquipmentSlotForBone(bone, stack, animatable);
	}
	
	@Override
	protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
		if (this.currentArmorPartAtBone != null) {
			return this.currentArmorPartAtBone.apply(baseModel);
		}
		return super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
	}
	
}
