package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRMandril extends AbstractEntityCQR {

	public EntityCQRMandril(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		Item[] swords = new Item[] { Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, ModItems.DAGGER_MONKING };
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(swords[getRNG().nextInt(swords.length)], 1));
		
		if(getRNG().nextBoolean()) {
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1));
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET, 1));
		}
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.MANDRILS.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.BEASTS;
	}
	
	@Override
	public int getTextureCount() {
		return 1;
	}
	
	@Override
	public boolean canRide() {
		return true;
	}

}
