package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class AbstractEntityCQRMageBase extends AbstractEntityCQRBoss {

	private static final DataParameter<Boolean> IDENTITY_HIDDEN = EntityDataManager.<Boolean>createKey(AbstractEntityCQRMageBase.class, DataSerializers.BOOLEAN);

	public AbstractEntityCQRMageBase(World worldIn, int size) {
		super(worldIn, size);
	}

	@Override
	public int getTextureCount() {
		return 1;
	}

	@Override
	public boolean canRide() {
		return false;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IDENTITY_HIDDEN, true);
	}

	public void revealIdentity() {
		this.dataManager.set(IDENTITY_HIDDEN, false);
	}

	@Override
	public float getSizeVariation() {
		return 0F;
	}

	public boolean isIdentityHidden() {
		return this.dataManager.get(IDENTITY_HIDDEN);
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);

		if ((this.getHealth() / this.getMaxHealth()) < 0.83) {
			this.revealIdentity();
		}
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.STAFF_VAMPIRIC, 1));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("identityHidden", this.isIdentityHidden());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (!compound.getBoolean("identityHidden")) {
			this.revealIdentity();
		}
	}

}
