package team.cqr.cqrepoured.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.Capes;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateFleeSpell;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateTeleportBehindEnemy;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.BossAIPirateTurnInvisible;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQRPirateCaptain extends AbstractEntityCQRBoss {

	private static final DataParameter<Boolean> IS_DISINTEGRATING = EntityDataManager.<Boolean>createKey(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_REINTEGRATING = EntityDataManager.<Boolean>createKey(EntityCQRPirateCaptain.class, DataSerializers.BOOLEAN);

	public static int TURN_INVISIBLE_ANIMATION_TIME = 15;

	private boolean spawnedParrot = false;

	public EntityCQRPirateCaptain(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_PIRATE_CAPTAIN;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.PirateCaptain;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}

	@Override
	public boolean isPotionApplicable(EffectInstance potioneffectIn) {
		if (!super.isPotionApplicable(potioneffectIn)) {
			return false;
		}
		if (potioneffectIn.getPotion().beneficial) {
			return true;
		}
		return potioneffectIn.getPotion() == Effects.GLOWING;
	}

	@Override
	public boolean canAttack(Entity entityIn) {
		this.spawnShoulderEntities();
		return super.canAttack(entityIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

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
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.dataManager.register(IS_DISINTEGRATING, false);
		this.dataManager.register(IS_REINTEGRATING, false);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER, 1));

		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.ARROW, new ItemStack(CQRItems.BULLET_FIRE, 64));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(CQRItems.POTION_HEALING, 2));
	}

	@Override
	public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, ILivingEntityData livingdata) {
		this.setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void save(CompoundNBT compound) {
		super.save(compound);
		compound.setBoolean("spawnedParrot", this.spawnedParrot);
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
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

	@Override
	public float getInvisibility() {
		return this.dataManager.get(INVISIBILITY);
	}

	@Override
	protected int getInvisibilityTurningTime() {
		return EntityCQRPirateCaptain.TURN_INVISIBLE_ANIMATION_TIME;
	}
}
