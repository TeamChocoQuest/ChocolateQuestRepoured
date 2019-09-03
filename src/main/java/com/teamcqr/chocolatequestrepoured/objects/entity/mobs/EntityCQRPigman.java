package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;

import net.minecraft.world.World;

public class EntityCQRPigman extends AbstractEntityCQR {

	public EntityCQRPigman(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 2.3F);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.GREAT_SWORD_IRON));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.PIGMEN.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.UNDEAD;
	}

}
