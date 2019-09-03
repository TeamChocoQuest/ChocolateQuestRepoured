package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;

import net.minecraft.world.World;

public class EntityCQRDwarf extends AbstractEntityCQR {

	public EntityCQRDwarf(World worldIn) {
		super(worldIn);
		this.setSize(0.55F, 1.4F);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		Item[] pickaxes = new Item[] { Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE };
		Item[] helmets = new Item[] { Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.CHAINMAIL_HELMET };

		this.setItemStackToSlot(this.rand.nextBoolean() ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, new ItemStack(pickaxes[this.rand.nextInt(pickaxes.length)]));
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(helmets[this.rand.nextInt(helmets.length)]));
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.DWARVES.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.DWARVES_AND_GOLEMS;
	}

}
