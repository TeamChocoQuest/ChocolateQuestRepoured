package team.cqr.cqrepoured.item.armor;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemBootsCloud extends ArmorItem {

	private AttributeModifier movementSpeed;

	public ItemBootsCloud(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);

		this.movementSpeed = new AttributeModifier("CloudBootsSpeedModifier", 0.15D, Operation.MULTIPLY_TOTAL);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			multimap.put(Attributes.MOVEMENT_SPEED, this.movementSpeed);
		}

		return multimap;
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		
		player.addEffect(new EffectInstance(Effects.JUMP, 0, 4, false, false));

		player.flyingSpeed += 0.04F; //Correct replacement?
		if (player.fallDistance > 0.0F || player.isSprinting()) {
			world.addParticle(ParticleTypes.CLOUD, player.position().x, player.position().y, player.position().y, (random.nextFloat() - 0.5F) / 2.0F, -0.5D, (random.nextFloat() - 0.5F) / 2.0F);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

}
