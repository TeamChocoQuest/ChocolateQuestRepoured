package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.Capes;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttackRanged;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIBackstab;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerLightningCircles;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerLightningSpiral;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerTornadoAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIWalkerIllusions;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityCQRWalkerKing extends AbstractEntityCQRBoss {
	
	private int lightningTick = 0;
	private int borderLightning = 20;
	private boolean active = false;
	private int activationCooldown = 80;

	public EntityCQRWalkerKing(World world) {
		this(world, 1);
	}
	
	public EntityCQRWalkerKing(World worldIn, int size) {
		super(worldIn, size);
		
		this.bossInfoServer.setColor(Color.PURPLE);
		this.bossInfoServer.setCreateFog(true);
		this.bossInfoServer.setDarkenSky(true);
		this.bossInfoServer.setOverlay(Overlay.PROGRESS);
		this.bossInfoServer.setPlayEndBossMusic(true);
		
		this.experienceValue = 200;
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));
		//Spells
		this.tasks.addTask(6, new EntityAIWalkerIllusions(this));
		
		this.tasks.addTask(7, new BossAIWalkerTornadoAttack(this));
		this.tasks.addTask(8, new BossAIWalkerLightningCircles(this));
		this.tasks.addTask(9, new BossAIWalkerLightningSpiral(this));
		//normal combat
		this.tasks.addTask(10, new EntityAIAttackRanged(this));
		this.tasks.addTask(11, new EntityAIBackstab(this));
		this.tasks.addTask(12, new EntityAIAttack(this));
		
		//Low priority stuff
		this.tasks.addTask(15, new EntityAIMoveToLeader(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	
	@Override
	public void onLivingUpdate() {
		if(fallDistance > 12) {
			BlockPos teleportPos = null;
			boolean teleport = getAttackTarget() != null || getHomePositionCQR() != null;
			if(getAttackTarget() != null && !world.isRemote) {
				Vec3d v = getAttackTarget().getLookVec();
				v = v.normalize();
				v = v.subtract(0, v.y, 0);
				v = v.scale(3);
				teleportPos = new BlockPos(getAttackTarget().getPositionVector().subtract(v));
				if(world.isBlockFullCube(teleportPos) || world.isBlockFullCube(teleportPos.offset(EnumFacing.UP)) || world.isAirBlock(teleportPos.offset(EnumFacing.DOWN))) {
					teleportPos = getAttackTarget().getPosition();
				}
			} else if(getHomePositionCQR() != null && !world.isRemote) {
				teleportPos = getHomePositionCQR();
			}
			if(teleport) {
				//spawn cloud
				for(int ix = -1; ix <= 1; ix++) {
					for(int iz = -1; iz <= 1; iz++) {
						((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + ix, posY +2, posZ +iz, 10, 0, 0, 0, 0.25, 0, 0, 0);
					}
				}
				world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1, 1, true);
				attemptTeleport(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
			}
		}
		if(active && !world.isRemote) {
			if(getAttackTarget() == null && !world.isRemote) {
				activationCooldown--;
				if(activationCooldown < 0) {
					active = false;
					world.getWorldInfo().setThundering(false);
					activationCooldown = 80;
				}
			} else if(!world.isRemote) {
				world.getWorldInfo().setCleanWeatherTime(0);
				world.getWorldInfo().setRainTime(400);
				world.getWorldInfo().setThunderTime(200);
				world.getWorldInfo().setRaining(true);
				world.getWorldInfo().setThundering(true);
			}
			lightningTick++;
			if(lightningTick > borderLightning) {
				// strike lightning
				lightningTick = 0;
				borderLightning = 50;
				int x = -20 + getRNG().nextInt(41);
				int z = -15 + getRNG().nextInt(41);
				int y = -10 + getRNG().nextInt(21);
				
				EntityColoredLightningBolt entitybolt = new EntityColoredLightningBolt(world, posX +x, posY +y, posZ +z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
				world.spawnEntity(entitybolt);
			}
		} else if(world.isRemote) {
			active = false;
		}
		super.onLivingUpdate();
	}
	
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		this.heal(0.2F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.getImmediateSource() != null && source.getImmediateSource() instanceof EntitySpectralArrow) {
			amount *= 2;
		}
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		if(source == DamageSource.WITHER) {
			this.heal(amount /2);
			return true;
		}
		if(source == DamageSource.FALL) {
			return true;
		}
		
		float dmg = amount;
		if(!(source.getImmediateSource() != null && source.getImmediateSource() instanceof EntitySpectralArrow)) {
			 dmg *= 0.75F;
		}
		
		if(!world.isRemote && !world.getWorldInfo().isThundering()) {
			active = true;
			activationCooldown = 80;
			world.getWorldInfo().setCleanWeatherTime(0);
			world.getWorldInfo().setRainTime(400);
			world.getWorldInfo().setThunderTime(200);
			world.getWorldInfo().setRaining(true);
			world.getWorldInfo().setThundering(true);
		}
		return super.attackEntityFrom(source, dmg, sentFromPart);
	}
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}
	
	@Override
	public boolean hasCape() {
		return this.deathTicks <= 0;
	}
	
	@Override
	public ResourceLocation getResourceLocationOfCape() {
		return Capes.CAPE_WALKER;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_WALKER_KING.getLootTable();
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.WALKER_KING.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.WALKERS;
	}

	@Override
	public int getTextureCount() {
		return 1;
	}

	@Override
	public boolean canRide() {
		return true;
	}

	@Override
	public boolean canOpenDoors() {
		return true;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITHER_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WITHER_HURT;
	}
	
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITHER_DEATH;
	};
	
	@Override
	protected SoundEvent getFinalDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, getSword());
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.SHIELD_WALKER_KING, 1));
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, new ItemStack(ModItems.POTION_HEALING, 3));
	}
	
	
	private ItemStack getSword() {
		ItemStack sword = new ItemStack(ModItems.SWORD_WALKER, 1);
		
		for(int i = 0; i < 1 + getRNG().nextInt(3 * (world.getDifficulty().ordinal() +1)); i++) {
			sword = EnchantmentHelper.addRandomEnchantment(getRNG(), sword, 20 + getRNG().nextInt(41), true);
		}
		if(!EnchantmentHelper.hasVanishingCurse(sword)) {
			sword.addEnchantment(Enchantments.VANISHING_CURSE, 1);
		}
		
		return sword;
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		world.getWorldInfo().setThundering(false);
		super.onDeath(cause);
	}
	
	@Override
	protected boolean usesEnderDragonDeath() {
		return true;
	}

	@Override
	protected boolean doesExplodeOnDeath() {
		return true;
	}
	
	@Override
	protected EnumParticleTypes getDeathAnimParticles() {
		return EnumParticleTypes.EXPLOSION_HUGE;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return super.getExperiencePoints(player);
	}
	
}
