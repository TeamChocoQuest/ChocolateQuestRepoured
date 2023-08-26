package team.cqr.cqrepoured.config;

import java.util.function.Supplier;

import javax.swing.UIDefaults.LazyValue;

import net.minecraft.item.IArmorMaterial;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.crafting.Ingredient;
import team.cqr.cqrepoured.CQRConstants;

public class CQRArmorMaterial implements IArmorMaterial {

	private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final ArmorMaterialConfig config;
	private final SoundEvent equipSound;
	private final LazyValue<Ingredient> repairIngredient;

	public CQRArmorMaterial(String name, ArmorMaterialConfig config) {
		this(name, config, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.EMPTY);
	}

	public CQRArmorMaterial(String name, ArmorMaterialConfig config, SoundEvent equipSound) {
		this(name, config, equipSound, () -> Ingredient.EMPTY);
	}

	public CQRArmorMaterial(String name, ArmorMaterialConfig config, Supplier<Ingredient> repairIngredient) {
		this(name, config, SoundEvents.ARMOR_EQUIP_GENERIC, repairIngredient);
	}

	public CQRArmorMaterial(String name, ArmorMaterialConfig config, SoundEvent equipSound, Supplier<Ingredient> repairIngredient) {
		this.name = CQRConstants.MODID + ":" + name;
		this.config = config;
		this.equipSound = equipSound;
		this.repairIngredient = new LazyValue<>(repairIngredient);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlot slot) {
		return HEALTH_PER_SLOT[slot.getIndex()] * config.durability.get();
	}

	@Override
	public int getDefenseForSlot(EquipmentSlot slot) {
		return config.defense.get().get(slot.getIndex());
	}

	@Override
	public int getEnchantmentValue() {
		return config.enchantmentValue.get();
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}

	@Override
	public float getToughness() {
		return (float) (double) config.toughness.get();
	}

	@Override
	public float getKnockbackResistance() {
		return (float) (double) config.knockbackResistance.get();
	}

}
