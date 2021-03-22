package team.cqr.cqrepoured.objects.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.util.Reference;

public class EnchantmentSpectral extends Enchantment {

	public EnchantmentSpectral() {
		this(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
	}

	private EnchantmentSpectral(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setName("spectral");
		this.setRegistryName(Reference.MODID, "spectral");
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
		if (creatureType == CQRCreatureAttributes.CREATURE_TYPE_ABYSS_WALKER) {
			return (float) level * 2.5F;
		}
		return 0;
	}

	public boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentDamage || ench instanceof EnchantmentSpectral);
	}

	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		if(target instanceof EntityLivingBase) {
			EntityLivingBase livingTarget = (EntityLivingBase)target;
			if(livingTarget.getCreatureAttribute() == CQRCreatureAttributes.CREATURE_TYPE_ABYSS_WALKER) {
				int i = 20 + user.getRNG().nextInt(10 * level);
				livingTarget.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, i, 2));
			}
		}
	}

}
