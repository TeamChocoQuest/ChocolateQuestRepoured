package team.cqr.cqrepoured.item.gun;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//#TODO tests modifiers
public class ItemMusketKnife extends ItemMusket implements IFireArmTwoHanded {

	private final float attackDamage;
	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemMusketKnife(IItemTier material, Properties properties)
	{
		super(properties);
		this.attackDamage = 3.0F + material.getAttackDamageBonus();
		
		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlotType.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
		modifierBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4000000953674316D + (-0.8), AttributeModifier.Operation.ADDITION));
		this.attributeModifier = modifierBuilder.build();
	}

	/*@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		this.replaceModifier(modifiers, Attributes.ATTACK_SPEED, BASE_ATTACK_SPEED_UUID, -0.8F);
		return modifiers;
	}

	protected void replaceModifier(Multimap<Attribute, AttributeModifier> modifierMultimap, Attribute attribute, UUID id, double value) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute);
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getId().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getId(), modifier.getName(), modifier.getAmount() + value, modifier.getOperation()));
		}
	} */

	@Override
	public float getDestroySpeed(ItemStack pStack, BlockState pState) {
		if (pState.is(Blocks.COBWEB)) {
			return 15.0F;
		} else {
			Material material = pState.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !pState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		pStack.hurtAndBreak(1, pAttacker, (p_220045_0_) -> {
			p_220045_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack pStack, World pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
		if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
			pStack.hurtAndBreak(2, pEntityLiving, (p_220044_0_) -> {
				p_220044_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState pBlock) {
		return pBlock.is(Blocks.COBWEB);
	}

	/*@Override
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = this.material.getRepairItemStack();
		if (!mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false)) {
			return true;
		}
		return super.getIsRepairable(toRepair, repair);
	} */

	@SuppressWarnings("deprecation")
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack)
	{
		return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifier : super.getAttributeModifiers(equipmentSlot, stack);
	/*	Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(equipmentSlot);

		if(equipmentSlot == EquipmentSlotType.MAINHAND)
		{
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4000000953674316D, AttributeModifier.Operation.ADDITION));
		}

		return multimap; */
	}

	/*public Multimap<String, AttributeModifier> getItemAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
			multimap.put(Attributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}

		return multimap;
	} */

}
