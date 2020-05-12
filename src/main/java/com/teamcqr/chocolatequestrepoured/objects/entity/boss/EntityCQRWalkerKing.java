package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.Capes;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerLightningCircles;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerLightningSpiral;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking.BossAIWalkerTornadoAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAIWalkerIllusions;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig.Mobs;
import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityCQRWalkerKing extends AbstractEntityCQRBoss {
	
	private int lightningTick = 0;
	private int borderLightning = 20;
	private boolean active = false;
	private int activationCooldown = 80;
	private int dragonAttackCooldown = 0;
	
	public EntityCQRWalkerKing(World worldIn) {
		super(worldIn);
		
		this.bossInfoServer.setColor(Color.PURPLE);
		this.bossInfoServer.setCreateFog(true);
		this.bossInfoServer.setDarkenSky(true);
		this.bossInfoServer.setPlayEndBossMusic(true);
		
		this.experienceValue = 200;
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.spellHandler.addSpell(0, new EntityAIWalkerIllusions(this, 600, 40));
		this.tasks.addTask(15, new BossAIWalkerTornadoAttack(this));
		this.tasks.addTask(16, new BossAIWalkerLightningCircles(this));
		this.tasks.addTask(17, new BossAIWalkerLightningSpiral(this));
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		setEquipmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public void onLivingUpdate() {
		if(dragonAttackCooldown > 0) {
			dragonAttackCooldown--;
		}
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
		this.heal(1F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return super.attackEntityFrom(source, amount);
	}
	
	private void backStabAttacker(DamageSource source) {
		if(source.getTrueSource() != null) {
			if(teleportBehindEntity(source.getTrueSource())) {
				attackEntityAsMob(source.getTrueSource());
			}
		}
	}
	
	private boolean teleportBehindEntity(Entity entity) {
		Vec3d p = entity.getPositionVector().subtract(entity.getLookVec().scale(4 + (entity.width * 0.5)));
		if(getNavigator().canEntityStandOnPos(new BlockPos(p.x,p.y,p.z))) {
			for(int ix = -1; ix <= 1; ix++) {
				for(int iz = -1; iz <= 1; iz++) {
					((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + ix, posY +2, posZ +iz, 10, 0, 0, 0, 0.25, 0, 0, 0);
				}
			}
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1, 1, true);
			return attemptTeleport(p.x, p.y, p.z);
		}
		return false;
	}

	private void handleAttackedByDragon(Entity dragon) {
		if (CQRConfig.advanced.enableSpecialFeatures && dragon.getControllingPassenger() != null /*&& (getRNG().nextInt(100) +1) > 95*/) {
			if(dragon instanceof EntityLiving && dragon.getControllingPassenger() instanceof EntityLivingBase) {
				dragon.getControllingPassenger().dismountRidingEntity();
				//((EntityLiving)dragon).setAttackTarget((EntityLivingBase) dragon.getControllingPassenger());
				/*if(dragon instanceof EntityTameable) {
					try {
						((EntityTameable)dragon).setOwnerId(null);
					} catch(NullPointerException ex) {
						
					}
					try {
						((EntityTameable)dragon).setTamedBy(null);
					} catch(NullPointerException ex) {
						
					}
					((EntityTameable)dragon).setTamed(false);
				}*/
			}
		}
		
		//KILL IT!!!
		int lightningCount = 6 + getRNG().nextInt(3);
		double angle = 360 / lightningCount;
		double dragonSize = dragon.width > dragon.height ? dragon.width : dragon.height;
		Vec3d v = new Vec3d(3 + (3 * dragonSize),0,0);
		for(int i = 0; i < lightningCount; i++) {
			Vec3d p = VectorUtil.rotateVectorAroundY(v, i * angle);
			int dY = -3 + getRNG().nextInt(7);
			EntityColoredLightningBolt clb = new EntityColoredLightningBolt(world, dragon.posX + p.x, dragon.posY + dY, dragon.posZ + p.z, false, false, 1F, 0.00F, 0.0F, 0.4F);
			world.spawnEntity(clb);
		}
		dragon.attackEntityFrom(DamageSource.MAGIC, 10F);
	}

	private void handleActivation() {
		if(!world.isRemote && !world.getWorldInfo().isThundering()) {
			active = true;
			activationCooldown = 80;
			world.getWorldInfo().setCleanWeatherTime(0);
			world.getWorldInfo().setRainTime(400);
			world.getWorldInfo().setThunderTime(200);
			world.getWorldInfo().setRaining(true);
			world.getWorldInfo().setThundering(true);
		}
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
		
		if(!(source.getImmediateSource() != null && source.getImmediateSource() instanceof EntitySpectralArrow)) {
			amount *= 0.5F;
		}
		
		if(source.getImmediateSource() != null ) {
			if(source.getImmediateSource() instanceof EntitySpectralArrow) {
				amount *= 2;
				super.attackEntityFrom(source, amount, sentFromPart);
				return true;
			}
			if(source.getImmediateSource() instanceof EntityThrowable && !world.isRemote) {
				//STAB HIM IN THE BACK!!
				backStabAttacker(source);
				return false;
			}
		}
		
		handleActivation();

		if(source.getTrueSource() != null && !world.isRemote) {
			ResourceLocation resLoc = EntityList.getKey(source.getTrueSource());
			if(resLoc != null) {
				// Start IceAndFire compatibility
				boolean flag = resLoc.getResourceDomain().equalsIgnoreCase("iceandfire") && CQRConfig.advanced.enableSpecialFeatures;
				if (flag) {
					amount /= 2;
				}
				// End IceAndFire compatibility
				
				//If we are attacked by a dragon: KILL IT
				if(dragonAttackCooldown <= 0 && (resLoc.getResourcePath().contains("dragon") || resLoc.getResourcePath().contains("wyrm") || resLoc.getResourcePath().contains("wyvern") || flag)) {
					dragonAttackCooldown = 80;
					handleAttackedByDragon(source.getTrueSource());
				}
			}
		}
		
		if(Mobs.bosses.harderWalkerKing && !world.isRemote) {
			if(getRNG().nextDouble() < 0.2 && source.getTrueSource() != null) {
				//Revenge Attack
				if(getRNG().nextDouble() < 0.7) {
					attackEntityAsMob(source.getTrueSource());
					teleportBehindEntity(source.getTrueSource());
				} 
			}
		}
		return super.attackEntityFrom(source, amount, sentFromPart);
	}
	
	@Override
	public boolean canBlockDamageSource(DamageSource damageSourceIn) {
		if(!Mobs.bosses.harderWalkerKing) {
			return super.canBlockDamageSource(damageSourceIn);
		}
		if (super.canBlockDamageSource(damageSourceIn)) {
			if(getRNG().nextDouble() < 0.3) {
				return true;
			}
			if(getRNG().nextDouble() < 0.1) {
				//Attack back
				counterAttack();
			}
		}
		return false;
	}

	private void counterAttack() {
		counterAttack(this.getAttackTarget());
	}
	
	private void counterAttack(Entity entitylivingbase) {
		double d0 = Math.min(entitylivingbase.posY, this.posY);
		double d1 = Math.max(entitylivingbase.posY, this.posY) + 1.0D;
		float f = (float) MathHelper.atan2(entitylivingbase.posZ - this.posZ, entitylivingbase.posX - this.posX);
		for (int i = 0; i < 5; ++i) {
			float f1 = f + (float) i * (float) Math.PI * 0.4F;
			this.spawnFangs(this.posX + (double) MathHelper.cos(f1) * 1.5D, this.posZ + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
		}

		for (int k = 0; k < 8; ++k) {
			float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
			this.spawnFangs(this.posX + (double) MathHelper.cos(f2) * 2.5D, this.posZ + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
		}
	}
	
	private void spawnFangs(double x, double z, double minY, double maxY, float rotationYawRadians, int warmupDelayTicks) {
		BlockPos blockpos = new BlockPos(x, maxY, z);
		boolean flag = false;
		double d0 = 0.0D;

		while (true) {
			if (!this.world.isBlockNormalCube(blockpos, true) && this.world.isBlockNormalCube(blockpos.down(), true)) {
				if (!this.world.isAirBlock(blockpos)) {
					IBlockState iblockstate = this.world.getBlockState(blockpos);
					AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

					if (axisalignedbb != null) {
						d0 = axisalignedbb.maxY;
					}
				}

				flag = true;
				break;
			}

			blockpos = blockpos.down();

			if (blockpos.getY() < MathHelper.floor(minY) - 1) {
				break;
			}
		}

		if (flag) {
			EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(this.world, x, (double) blockpos.getY() + d0, z, rotationYawRadians, warmupDelayTicks, this);
			this.world.spawnEntity(entityevokerfangs);
		}
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
		return CQRLootTableList.ENTITIES_WALKER_KING;
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
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot"))
        {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0)
            {
                this.dropExperience(MathHelper.floor((float)50F));
            }
        }
	}
	
	@Override
	protected void onFinalDeath() {
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot"))
        {
            this.dropExperience(MathHelper.floor((float)1200));
        }
	}
	
	@Override
	protected boolean usesEnderDragonDeath() {
		return true;
	}

	@Override
	protected boolean doesExplodeOnDeath() {
		return false;
	}
	
	@Override
	protected EnumParticleTypes getDeathAnimParticles() {
		return EnumParticleTypes.EXPLOSION_HUGE;
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return super.getExperiencePoints(player);
	}
	
	private void dropExperience(int p_184668_1_)
    {
        while (p_184668_1_ > 0)
        {
            int i = EntityXPOrb.getXPSplit(p_184668_1_);
            p_184668_1_ -= i;
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
        }
    }
	
}
