package team.cqr.cqrepoured.item.armor;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.client.render.armor.RenderArmorBull;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemArmorBull extends CQRGeoArmorBase {

	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemArmorBull(ArmorMaterial materialIn, Type type, Properties prop) {
		super(materialIn, type, prop);

		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.ATTACK_DAMAGE,new AttributeModifier("BullArmorModifier", 1D, Operation.ADDITION));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == Mob.getEquipmentSlotForItem(stack)) {
			return this.attributeModifier;
		}
		return super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, "bull_armor");
	}
	
	@Override
	public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
		super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
		if (ItemUtil.hasFullSet(player, ItemArmorBull.class)) {
			if (player.isSprinting()) {
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 0, 1, false, false));
			}
		}
	}

	@Override
	public void registerControllers(ControllerRegistrar arg0) {
		
	}

	@Override
	protected GeoArmorRenderer<?> createRenderer() {
		return new RenderArmorBull();
	}

}
