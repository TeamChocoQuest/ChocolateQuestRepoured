package team.cqr.cqrepoured.objects.items.guns;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
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
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		this.replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, -0.8F);
		return modifiers;
	}

	protected void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, IAttribute attribute, UUID id, double value) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() + value, modifier.getOperation()));
		}
	}

	/** Copied from {@link ItemSword#getDestroySpeed(ItemStack, IBlockState)} */
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Block block = state.getBlock();

		if (block == Blocks.WEB) {
			return 15.0F;
		} else {
			Material material1 = state.getMaterial();
			return material1 != Material.PLANTS && material1 != Material.VINE && material1 != Material.CORAL && material1 != Material.LEAVES && material1 != Material.GOURD ? 1.0F : 1.5F;
		}
	}

	/** Copied from {@link ItemSword#hitEntity(ItemStack, EntityLivingBase, EntityLivingBase)} */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}

	/** Copied from {@link ItemSword#onBlockDestroyed(ItemStack, World, IBlockState, BlockPos, EntityLivingBase)} */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if ((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
			stack.damageItem(2, entityLiving);
		}

		return true;
	}

	/** Copied from {@link ItemSword#canHarvestBlock(IBlockState)} */
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return blockIn.getBlock() == Blocks.WEB;
	}

	/** Copied from {@link ItemSword#getItemEnchantability()} */
	@Override
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}

	/** Copied from {@link ItemSword#getIsRepairable(ItemStack, ItemStack)} */
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = this.material.getRepairItemStack();
		if (!mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false)) {
			return true;
		}
		return super.getIsRepairable(toRepair, repair);
	}

	/** Copied from {@link ItemSword#getItemAttributeModifiers(EntityEquipmentSlot)} */
	@SuppressWarnings("deprecation")
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}

		return multimap;
	}

}
