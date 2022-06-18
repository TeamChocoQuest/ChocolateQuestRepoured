package team.cqr.cqrepoured.config;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import team.cqr.cqrepoured.CQRMain;

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
		this.name = CQRMain.MODID + ":" + name;
		this.config = config;
		this.equipSound = equipSound;
		this.repairIngredient = new LazyValue<>(repairIngredient);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slot) {
		return HEALTH_PER_SLOT[slot.getIndex()] * config.durability.get();
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slot) {
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
