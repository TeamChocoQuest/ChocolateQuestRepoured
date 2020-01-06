package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttackRanged;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise.BossAIHealingTurtle;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCQRGiantTortoise extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob {

	private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.<Boolean>createKey(EntityCQRGiantTortoise.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> ANIM_STATE = EntityDataManager.<Integer>createKey(EntityCQRGiantTortoise.class, DataSerializers.VARINT);

	protected EntityCQRGiantTortoisePart[] parts = new EntityCQRGiantTortoisePart[5];
	protected boolean isInShell = false;
	protected ETortoiseAnimState currentAnimation = ETortoiseAnimState.NONE;

	static int EAnimStateGlobalID = 0;

	@SideOnly(Side.CLIENT)
	private ETortoiseAnimState lastTickAnim = ETortoiseAnimState.NONE;
	@SideOnly(Side.CLIENT)
	private boolean animationChanged = false;

	public enum ETortoiseAnimState {
		SPIN_UP, SPIN_DOWN, SPIN, MOVE_PARTS_IN, MOVE_PARTS_OUT, WALKING, HEALING, NONE;

		private int id;

		private ETortoiseAnimState() {
			this.id = EAnimStateGlobalID;
			EAnimStateGlobalID++;
		}

		public static ETortoiseAnimState valueOf(int i) {
			if (i >= values().length) {
				return NONE;
			}
			return values()[i];
		}

		public int getID() {
			return this.id;
		}
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new BossAIHealingTurtle(this));
		this.tasks.addTask(8, new EntityAIAttackRanged(this));
		this.tasks.addTask(10, new EntityAIAttack(this));
		this.tasks.addTask(15, new EntityAIMoveToLeader(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	final float baseWidth = 2.0F;
	final float baseHeight = 1.7F;

	@SideOnly(Side.CLIENT)
	private int animationProgress = 0;

	public EntityCQRGiantTortoise(World worldIn) {
		// super(worldIn, size);
		super(worldIn, 1);

		this.bossInfoServer.setColor(Color.GREEN);
		this.bossInfoServer.setCreateFog(true);
		this.bossInfoServer.setOverlay(Overlay.PROGRESS);

		this.setSize(this.baseWidth, this.baseHeight);

		for (int i = 0; i < this.parts.length - 1; i++) {
			this.parts[i] = new EntityCQRGiantTortoisePart(this, "tortoise_leg" + i, 0.7F, 1.1F, false);
		}
		this.parts[this.parts.length - 1] = new EntityCQRGiantTortoisePart(this, "tortoise_head", 0.7F, 0.7F, true);

		this.noClip = false;
		this.setNoGravity(false);
		this.experienceValue = 150;

		this.ignoreFrustumCheck = true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(MOUTH_OPEN, false);
		this.dataManager.register(ANIM_STATE, ETortoiseAnimState.NONE.getID());
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.99D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.125D);
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		return this.attackEntityFrom(source, damage, true);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		if (sentFromPart && !this.isInShell) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}
		// TODO: Play "armor hit" sound
		return true;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public int getAir() {
		return 100;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_TURTLE.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.GIANT_TORTOISE.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSwingingArms(boolean swingingArms) {

	}

	public void setMouthOpen(boolean open) {
		this.dataManager.set(MOUTH_OPEN, open);
	}

	public boolean isMouthOpen() {
		return this.dataManager.get(MOUTH_OPEN);
	}

	public ETortoiseAnimState getCurrentAnimation() {
		if (!this.world.isRemote) {
			return this.currentAnimation;
		}
		return ETortoiseAnimState.valueOf(this.dataManager.get(ANIM_STATE));
		// return ETortoiseAnimState.MOVE_PARTS_OUT;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeBoolean(this.dataManager.get(MOUTH_OPEN));
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.dataManager.set(MOUTH_OPEN, additionalData.readBoolean());
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.setAir(999);

		if (this.getCurrentAnimation().equals(ETortoiseAnimState.MOVE_PARTS_OUT) || this.getCurrentAnimation().equals(ETortoiseAnimState.MOVE_PARTS_IN) || this.getCurrentAnimation().equals(ETortoiseAnimState.WALKING)) {
			this.setInShell(false);
		} else {
			this.setInShell(true);
		}

		for (EntityCQRGiantTortoisePart part : this.parts) {
			this.world.updateEntityWithOptionalForce(part, true);
		}

		this.alignParts();

		if (this.getWorld().isRemote) {
			if (!this.lastTickAnim.equals(this.getCurrentAnimation()) && this.getAnimationProgress() > 2) {
				this.setAnimationProgress(0);
				this.animationChanged = true;
			}

			this.lastTickAnim = this.getCurrentAnimation();
		}
	}

	private void alignParts() {
		// Legs
		Vec3d v = new Vec3d(0, 0, this.baseWidth / 2 + this.baseWidth * 0.1);
		v = VectorUtil.rotateVectorAroundY(v, this.rotationYawHead);

		this.parts[this.parts.length - 1].setPosition(this.posX + v.x, this.posY + 0.5, this.posZ + v.z);
		this.parts[this.parts.length - 1].setRotation(this.rotationYawHead, this.rotationPitch);

		v = VectorUtil.rotateVectorAroundY(v, 45D);

		for (int i = 0; i < this.parts.length - 1; i++) {
			if (i > 0) {
				v = VectorUtil.rotateVectorAroundY(v, 90D);
			}

			double x = this.posX + v.x;
			double y = this.posY;
			double z = this.posZ + v.z;

			this.parts[i].setPosition(x, y, z);
			this.parts[i].setRotation(this.rotationYaw + i * 45F, this.rotationPitch);
		}
	}

	@Override
	public Entity[] getParts() {
		return this.parts;
	}

	@Override
	public void setDead() {
		super.setDead();
		for (EntityCQRGiantTortoisePart part : this.parts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			this.world.removeEntityDangerously(part);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SLIME_HURT;
	}

	@SideOnly(Side.CLIENT)
	public int getAnimationProgress() {
		return this.animationProgress;
	}

	@SideOnly(Side.CLIENT)
	public void setAnimationProgress(int newProg) {
		this.animationProgress = newProg;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldModelReset() {
		return this.animationChanged;
	}

	@SideOnly(Side.CLIENT)
	public void setAnimationChanged(boolean newVal) {
		this.animationChanged = newVal;
	}

	public void setCurrentAnimation(ETortoiseAnimState newState) {
		this.currentAnimation = newState;
		this.dataManager.set(ANIM_STATE, newState.getID());
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	public void setInShell(boolean val) {
		this.isInShell = val;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setBoolean("inShell", this.isInShell);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.isInShell = compound.getBoolean("inShell");
	}

}
