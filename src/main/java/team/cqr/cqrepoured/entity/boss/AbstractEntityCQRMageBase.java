package team.cqr.cqrepoured.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.init.CQRItems;

public abstract class AbstractEntityCQRMageBase extends AbstractEntityCQRBoss {

	private static final DataParameter<Boolean> IDENTITY_HIDDEN = EntityDataManager.<Boolean>defineId(AbstractEntityCQRMageBase.class, DataSerializers.BOOLEAN);

	protected AbstractEntityCQRMageBase(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(IDENTITY_HIDDEN, true);
	}

	public void revealIdentity() {
		this.entityData.set(IDENTITY_HIDDEN, false);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	public boolean isIdentityHidden() {
		return this.entityData.get(IDENTITY_HIDDEN);
	}
	
	@Override
	protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
		super.actuallyHurt(damageSrc, damageAmount);

		if (!this.level.isClientSide && (this.getHealth() / this.getMaxHealth()) < 0.83F) {
			this.revealIdentity();
		}
	}

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, SpawnReason p_213386_3_, ILivingEntityData setDamageValue, CompoundNBT p_213386_5_) {
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(CQRItems.STAFF_VAMPIRIC.get(), 1));
		return super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, setDamageValue, p_213386_5_);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("identityHidden", this.isIdentityHidden());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("identityHidden") && !compound.getBoolean("identityHidden")) {
			this.revealIdentity();
		}
	}

	protected static final ITextComponent HIDDEN_NAME = new StringTextComponent("???");
	
	@Override
	public ITextComponent getDisplayName() {
		if (this.isIdentityHidden()) {
			return HIDDEN_NAME;
		}
		return super.getDisplayName();
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}

}
