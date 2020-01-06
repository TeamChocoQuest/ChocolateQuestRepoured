package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
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

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(pickaxes[this.rand.nextInt(pickaxes.length)]));
		if (this.world.rand.nextBoolean()) {
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
		}
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(helmets[this.rand.nextInt(helmets.length)]));
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.DWARF.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.DWARVES_AND_GOLEMS;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return ModSounds.CLASSIC_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_DWARF.getLootTable();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isFireDamage()) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public int getTextureCount() {
		return 3;
	}

	@Override
	public boolean canRide() {
		return true;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}
}
