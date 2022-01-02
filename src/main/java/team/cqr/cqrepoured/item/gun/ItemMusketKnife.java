package team.cqr.cqrepoured.item.gun;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMusketKnife extends ItemMusket {

	private final float attackDamage;
	private final Item.ToolMaterial material;

	public ItemMusketKnife(Item.ToolMaterial material) {
		this.material = material;
		this.attackDamage = 3.0F + material.getAttackDamage();
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		this.replaceModifier(modifiers, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, -0.8F);
		return modifiers;
	}

	protected void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, Attribute attribute, UUID id, double value) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() + value, modifier.getOperation()));
		}
	}

	/** Copied from {@link SwordItem#getDestroySpeed(ItemStack, BlockState)} */
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Block block = state.getBlock();

		if (block == Blocks.WEB) {
			return 15.0F;
		} else {
			Material material1 = state.getMaterial();
			return material1 != Material.PLANTS && material1 != Material.VINE && material1 != Material.CORAL && material1 != Material.LEAVES && material1 != Material.GOURD ? 1.0F : 1.5F;
		}
	}

	/** Copied from {@link SwordItem#hitEntity(ItemStack, LivingEntity, LivingEntity)} */
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damageItem(1, attacker);
		return true;
	}

	/** Copied from {@link SwordItem#onBlockDestroyed(ItemStack, World, BlockState, BlockPos, LivingEntity)} */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (state.getBlockHardness(worldIn, pos) != 0.0D) {
			stack.damageItem(2, entityLiving);
		}

		return true;
	}

	/** Copied from {@link SwordItem#canHarvestBlock(BlockState)} */
	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		return blockIn.getBlock() == Blocks.WEB;
	}

	/** Copied from {@link SwordItem#getItemEnchantability()} */
	@Override
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}

	/** Copied from {@link SwordItem#getIsRepairable(ItemStack, ItemStack)} */
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = this.material.getRepairItemStack();
		if (!mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false)) {
			return true;
		}
		return super.getIsRepairable(toRepair, repair);
	}

	/** Copied from {@link SwordItem#getItemAttributeModifiers(EquipmentSlotType)} */
	@SuppressWarnings("deprecation")
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
			multimap.put(Attributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}

		return multimap;
	}

}
