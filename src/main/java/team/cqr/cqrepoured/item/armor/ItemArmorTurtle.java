package team.cqr.cqrepoured.item.armor;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemArmorTurtle extends ArmorItem {

	private AttributeModifier health;

	public ItemArmorTurtle(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		this.health = new AttributeModifier("TurtleHealthModifier", 2.0D, Operation.ADDITION);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			multimap.put(Attributes.MAX_HEALTH, this.health);
		}

		return multimap;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		PlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			int cooldown = CapabilityCooldownHandlerHelper.getCooldown(player, CQRItems.CHESTPLATE_TURTLE);
			if (cooldown > 0) {
				tooltip.add(new TranslationTextComponent(TextFormatting.RED + "description.turtle_armor_charging.name", this.convertCooldown(cooldown)));
			}
		}
		
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

	private String convertCooldown(int cd) {
		int i = cd / 20;
		int minutes = i / 60;
		int seconds = i % 60;

		if (seconds < 10) {
			return minutes + ":" + "0" + seconds;
		}

		return minutes + ":" + seconds;
	}
	
	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return armorSlot == EquipmentSlotType.LEGS ? (A) CQRArmorModels.TURTLE_ARMOR_LEGS : (A) CQRArmorModels.TURTLE_ARMOR;
	}

}
