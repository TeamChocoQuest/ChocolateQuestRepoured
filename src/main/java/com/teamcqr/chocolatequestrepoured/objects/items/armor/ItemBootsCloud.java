package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBootsCloud extends ItemArmor {

	private AttributeModifier movementSpeed;

	public ItemBootsCloud(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.movementSpeed = new AttributeModifier("CloudBootsSpeedModifier", 0.15D, 2);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}

		return multimap;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 0, 4, false, false));

		if (!player.onGround) {
			player.jumpMovementFactor += 0.04F;

			if (player.fallDistance >= 1.0F) {
				for (int i = 0; i < 3; i++) {
					world.spawnParticle(EnumParticleTypes.CLOUD, player.posX, player.posY, player.posZ,
							(itemRand.nextFloat() - 0.25F) / 2.0F, -0.5D, (itemRand.nextFloat() - 0.25F) / 2.0F);
				}

				player.fallDistance = 0F;
			}
		}

		if (player.isSprinting()) {
			world.spawnParticle(EnumParticleTypes.CLOUD, player.posX, player.posY, player.posZ,
					(itemRand.nextFloat() - 0.5F), -0.5D, (itemRand.nextFloat() - 0.5F));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.cloud_boots.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}