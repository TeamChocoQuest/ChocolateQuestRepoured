package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.Capes;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateFleeSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateTeleportBehindEnemy;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.BossAIPirateTurnInvisible;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRPirateCaptain extends AbstractEntityCQRBoss {

	private static final DataParameter<Boolean> IS_DISINTEGRATING = EntityDataManager.<Boolean>createKey(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_REINTEGRATING = EntityDataManager.<Boolean>createKey(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> INVISIBILITY_TICKS = EntityDataManager.<Integer>createKey(EntityCQRPirateCaptain.class, DataSerializers.VARINT);

	public static int TURN_INVISIBLE_ANIMATION_TIME = 15;

	private boolean spawnedParrot = false;

	public int turnInvisibleTime = 1;

	public EntityCQRPirateCaptain(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_PIRATE_CAPTAIN;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.PIRATE_CAPTAIN.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		return;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		this.spawnShoulderEntities();
		return super.attackEntityAsMob(entityIn);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		this.spellHandler.addSpell(1, new BossAIPirateSummonParrot(this, 0, 20, 20));
		this.spellHandler.addSpell(0, new BossAIPirateFleeSpell(this, 60, 30, 30));

		this.tasks.addTask(2, new BossAIPirateTurnInvisible(this));
		this.tasks.addTask(1, new BossAIPirateTeleportBehindEnemy(this));
	}

	@Override
	public void setSitting(boolean sitting) {
		super.setSitting(false);
	}

	@Override
	public boolean hasCape() {
		return true;
	}

	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_PIRATE;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(IS_DISINTEGRATING, false);
		this.dataManager.register(IS_REINTEGRATING, false);
		this.dataManager.register(INVISIBILITY_TICKS, 1);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER, 1));

		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, new ItemStack(CQRItems.BULLET_FIRE, 64));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING, 2));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("spawnedParrot", this.spawnedParrot);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.spawnedParrot = compound.getBoolean("spawnedParrot");
	}

	public boolean hasSpawnedParrot() {
		return this.spawnedParrot;
	}

	public void setSpawnedParrot(boolean b) {
		this.spawnedParrot = b;
	}

	public void setIsDisintegrating(boolean value) {
		this.dataManager.set(IS_DISINTEGRATING, value);
	}

	public void setIsReintegrating(boolean value) {
		this.dataManager.set(IS_REINTEGRATING, value);
	}

	public boolean isDisintegrating() {
		return this.dataManager.get(IS_DISINTEGRATING);
	}

	public boolean isReintegrating() {
		return this.dataManager.get(IS_REINTEGRATING);
	}

	public int getInvisibleTicks() {
		return this.dataManager.get(INVISIBILITY_TICKS);
	}

	public void setInvisibleTicks(int value) {
		this.dataManager.set(INVISIBILITY_TICKS, value);
	}
}
