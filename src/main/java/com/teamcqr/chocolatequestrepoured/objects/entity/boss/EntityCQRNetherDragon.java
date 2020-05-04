package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAIChargeAtTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon.BossAISpiralUpOrDown;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRNetherDragonSegment;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCQRNetherDragon extends AbstractEntityCQRBoss implements IEntityMultiPart, IRangedAttackMob {

	public enum EDragonMovementState {
		CHARGING,
		FLYING,
		// When it is flying up or down, it will spiral up or down
		FLYING_UPWARDS,
		FLYING_DOWNWARDS
	}

	public enum ENetherDragonAttacks {
		SPIT_FIRE, FIREBALL, LIGHTNINGS, BITE
	}

	/**
	 * AI:
	 * Circle around about 30 blocks above your home location in a radius of ~30 blocks
	 * 
	 * If you see a player: Charge at it, bite it, fly in a 22.5° angle upwards until you flew 5 blocks up
	 * Then begin spiraling up to your "strafing y", there you fly 0.5 - 3 rounds on your circle and attack again
	 * While you are circling, you may change to a higher, thinner circler, about 10 blocks above the normal.
	 * You fly up to it by spiraling up or down, whilst charging at the player you may spit fire or shoot fireballs
	 */

	public int segmentCount = -1;
	/*
	 * 0: Normal mode
	 * 1: Transition to phase 2
	 * 2: skeletal phase
	 */
	private int phase = 0;
	private int phaseChangeTimer = 0;

	private EDragonMovementState movementState = EDragonMovementState.FLYING;

	private EntityCQRNetherDragonSegment[] dragonBodyParts; 

	// private boolean mouthOpen = false;
	private static final DataParameter<Boolean> MOUTH_OPEN = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SKELE_COUNT = EntityDataManager.<Integer>createKey(EntityCQRNetherDragon.class, DataSerializers.VARINT);

	private boolean isReadyToAttack = true;

	private ENetherDragonAttacks currentAttack = null;

	private int attackTimer = 0;

	/*
	 * Notes: This dragon is meant to "swim" through the skies, it moves like a snake, so the model needs animation, also the parts are meant to move like the parts from Twilight Forests Naga
	 * 
	 * Also the nether dragon destroys all blocks in its hitbox, if these are not lava, also if the block it moved through are leaves or burnable, it will set them on fire
	 * It will also break obsidian blocks, but not command blocks or structure blocks or bedrock
	 */

	public EntityCQRNetherDragon(World worldIn) {
		super(worldIn);
		/*
		 * this.dragonBodyParts = new MultiPartEntityPart[] { this.headPart, this.body1, this.body2, this.body3,
		 * this.body4, this.body5, this.body6, this.body7, this.body8, this.body9, this.body10, this.body11,
		 * this.body12, this.body13, this.body14, this.body15, this.body16 };
		 */
		this.noClip = true;
		this.setNoGravity(true);
		this.experienceValue = 100;

		this.ignoreFrustumCheck = true;

		// Init the body parts
		if(this.segmentCount < 0) {
			this.segmentCount = 18;
		}
		this.dragonBodyParts = new EntityCQRNetherDragonSegment[this.segmentCount];
		//if(!world.isRemote) {
			for (int i = 0; i < this.dragonBodyParts.length; i++) {
				this.dragonBodyParts[i] = new EntityCQRNetherDragonSegment(this, i + 1);
				worldIn.spawnEntity(this.dragonBodyParts[i]);
			}
			moveParts();
		//}
	}
	
	@Override
	public float getDefaultWidth() {
		return 2.0F;
	}

	@Override
	public float getDefaultHeight() {
		return 2.0F;
	}
	
	@Override
	protected boolean usesEnderDragonDeath() {
		return false;
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(MOUTH_OPEN, false);
		this.dataManager.register(SKELE_COUNT, -1);
	}
	
	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		// if (dragonPart != this.headPart) {
		damage = damage / 4.0F + Math.min(damage, 1.0F);
		// }
		if(this.phase == 1) {
			return false;
		}
		
		if(this.phase == 2) {
			damage = this.getMaxHealth() / this.getSegmentCount();
			this.setHealth(getHealth() - damage);
			((EntityCQRNetherDragonSegment) dragonPart).explode();
			removePart(dragonPart);
			if(damage >= this.getHealth()) {
				super.attackEntityFrom(source, damage +1);
			}
			return true;
		}
		if(this.phase == 0) {
			return attackEntityFrom(source, damage);
		}

		return super.attackEntityFrom(source, damage);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		/*
		 * if (source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage()) {
		 * //return this.attackEntityFromPart(this.headPart, source, amount);
		 * return true;
		 * }
		 */
		if (source.isFireDamage() || source.isExplosion()) {
			return false;
		}
		
		//Phase change
		if(this.phase == 0 && amount >= this.getHealth()) {
			this.phase++;
			//DONE: Init phase 2!!
			this.setHealth(this.getMaxHealth() -1);
			
			return false;
		} else if (phase != 0) {
			//Play blaze sound
			playSound(SoundEvents.ENTITY_BLAZE_HURT, 2F, 1.5F);
			return false;
		}

		return super.attackEntityFrom(source, amount);
	}
	
	public void removePart(MultiPartEntityPart part) {
		int index = 0;
		for(index = 0; index < this.dragonBodyParts.length; index++) {
			if(part == this.dragonBodyParts[index]) {
				break;
			}
		}
		for(int i = index; i < this.dragonBodyParts.length -1; i++) {
			this.dragonBodyParts[i] = this.dragonBodyParts[i +1];
			if(i+1 < this.dragonBodyParts.length) {
				this.dragonBodyParts[i +1] = null;
			}
		}
		/*for(int i = this.dragonBodyParts.length -1; i >= 0; i++) {
			if(this.dragonBodyParts[i].isDead || this.dragonBodyParts[i] == null) {
				this.dragonBodyParts[i] = null;
			}
		}*/
		/*if(part != null) {
			this.world.removeEntityDangerously(part);
		}*/
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(5, new net.minecraft.entity.ai.EntityAIAttackRanged(this, 1.1, 30, 60, 40));
		this.tasks.addTask(6, new BossAIChargeAtTarget(this));
		//this.tasks.addTask(7, new BossAIFlyToLocation(this));
		this.tasks.addTask(8, new BossAISpiralUpOrDown(this));
		this.tasks.addTask(10, new EntityAIAttack(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.NETHER_DRAGON.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// TODO: Shoot fireball OR spit fire if close enough
		double distance = this.getDistance(target);

		if (distance > 25) {
			// Shoot fireball
		} else {
			// Spit fire
			this.setMouthOpen(true);
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// Unused?
	}

	// This code is not entirely made by me, it is oriented from this:
	// https://github.com/TeamTwilight/twilightforest/blob/1.12.x/src/main/java/twilightforest/entity/boss/EntityTFNaga.java
	protected void moveParts() {
		for (int i = 0; i < this.dragonBodyParts.length; i++) {
			if(dragonBodyParts[i] == null) {
				continue;
			}
			Entity leader = i == 0 ? this : this.dragonBodyParts[i - 1];
			if(leader == null) {
				continue;
			}

			double headerX = leader.posX;
			double headerY = leader.posY;
			double headerZ = leader.posZ;

			float angle = (((leader.rotationYaw + 180) * new Float(Math.PI)) / 180F);

			double straightDegree = 0.05D + (1.0 / (float) (i + 1)) * 0.5D;

			double calculatedRotatedX = -MathHelper.sin(angle) * straightDegree;
			double calculatedRotatedZ = MathHelper.cos(angle) * straightDegree;

			double x = this.dragonBodyParts[i].posX;
			double y = this.dragonBodyParts[i].posY;
			double z = this.dragonBodyParts[i].posZ;

			Vec3d deltaPos = new Vec3d(x - headerX, y - headerY, z - headerZ);
			deltaPos = deltaPos.normalize();

			deltaPos = deltaPos.add(new Vec3d(calculatedRotatedX, 0, calculatedRotatedZ).normalize());

			// Dont change these values, they are important for the correct allignment of the segments!!!
			double f = i != 0 ? 0.378D : 0.338D;

			double targetX = headerX + f * deltaPos.x;
			double targetY = headerY + f * deltaPos.y;
			double targetZ = headerZ + f * deltaPos.z;

			// Set rotated position
			this.dragonBodyParts[i].setPosition(targetX, targetY, targetZ);

			double distance = (double) MathHelper.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z);
			// Finally apply the new rotation -> Rotate the block
			this.dragonBodyParts[i].setRotation((float) (Math.atan2(deltaPos.z, deltaPos.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(deltaPos.y, distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		// DONE: Destroy the blocks
		this.destroyBlocksInAABB(this.getEntityBoundingBox());
		/*
		 * for(EntityCQRNetherDragonSegment part : this.dragonBodyParts) {
		 * destroyBlocksInAABB(part.getEntityBoundingBox());
		 * }
		 */

		// TODO: Attack stuff -> in updateAI

	}

	// Copied from ender dragon
	private boolean destroyBlocksInAABB(AxisAlignedBB aabb) {
		if ((this.getWorld().getGameRules().hasRule("mobGriefing") && !this.getWorld().getGameRules().getBoolean("mobGriefing")) || this.world.isRemote) {
			return false;
		}

		int x1 = MathHelper.floor(aabb.minX);
		int y1 = MathHelper.floor(aabb.minY);
		int z1 = MathHelper.floor(aabb.minZ);
		int x2 = MathHelper.floor(aabb.maxX);
		int y2 = MathHelper.floor(aabb.maxY);
		int z2 = MathHelper.floor(aabb.maxZ);

		boolean cancelled = false;
		boolean blockDestroyed = false;

		for (int k1 = x1; k1 <= x2; ++k1) {
			for (int l1 = y1; l1 <= y2; ++l1) {
				for (int i2 = z1; i2 <= z2; ++i2) {
					BlockPos blockpos = new BlockPos(k1, l1, i2);
					IBlockState iblockstate = this.world.getBlockState(blockpos);
					Block block = iblockstate.getBlock();

					if (!block.isAir(iblockstate, this.world, blockpos) && iblockstate.getMaterial() != Material.FIRE) {
						if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
							cancelled = true;
						}
						// Check if the entity can destroy the blocks -> Event that can be cancelled by e.g. anti griefing mods or the protection system
						else if (net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate)) {
							if (block != Blocks.BEDROCK && block != Blocks.STRUCTURE_BLOCK && block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS
									&& block != Blocks.END_GATEWAY) {
								blockDestroyed = this.world.setBlockToAir(blockpos) || blockDestroyed;
							} else {
								cancelled = true;
							}
						} else {
							cancelled = true;
						}
					}
				}
			}
		}

		if (blockDestroyed) {
			double x = aabb.minX + (aabb.maxX - aabb.minX) * (double) this.rand.nextFloat();
			double y = aabb.minY + (aabb.maxY - aabb.minY) * (double) this.rand.nextFloat();
			double z = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double) this.rand.nextFloat();

			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0.0D, 0.0D, 0.0D);
		}

		return cancelled;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(this.phase == 1 && !world.isRemote) {
			this.phaseChangeTimer--;
			if(this.phaseChangeTimer <= 0) {
				this.phaseChangeTimer = 2;
				for(int i = 0; i < this.dragonBodyParts.length; i++) {
					if(!this.dragonBodyParts[i].isSkeletal()) {
						this.dragonBodyParts[i].switchToSkeletalState();
						if(!world.isRemote) {
							if(i == 0) {
								this.dataManager.set(SKELE_COUNT, 1);
							} else {
								this.dataManager.set(SKELE_COUNT, i);
							}
						}
						break;
					}
				}
			}
			
			if(this.dragonBodyParts[this.dragonBodyParts.length -1].isSkeletal()) {
				this.dataManager.set(SKELE_COUNT, this.dragonBodyParts.length +1);
				this.phase++;
			}
		}

		// update bodySegments parts
		for (EntityCQRNetherDragonSegment segment : this.dragonBodyParts) {
			if(segment != null) {
				this.world.updateEntityWithOptionalForce(segment, true);
				if(this.phase == 2 && !segment.isSkeletal() && !world.isRemote) {
					segment.switchToSkeletalState();
				}
			}
		}

		//if(!this.isDead) {
			//if(!world.isRemote) {
				this.moveParts();
			//}
		//}
			
	}
	
	public int getSkeleProgress() {
		return this.dataManager.get(SKELE_COUNT);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return ModSounds.NETHER_DRAGON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSounds.NETHER_DRAGON_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	public void setDead() {
		super.setDead();
		for (EntityCQRNetherDragonSegment dragonPart : this.dragonBodyParts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			if(dragonPart != null) {
				this.world.removeEntityDangerously(dragonPart);
			}
		}
	}

	@Override
	public Entity[] getParts() {
		return this.dragonBodyParts;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfoServer.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfoServer.removePlayer(player);
	}

	public EDragonMovementState getCurrentMovementState() {
		return this.movementState;
	}

	public void updateMovementState(EDragonMovementState charging) {
		this.movementState = charging;
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateFlying(this, worldIn) {
			@Override
			public float getPathSearchRange() {
				return 128.0F;
			}
		};
	}

	@Override
	public int getHealingPotions() {
		return 0;
	}

	public void setMouthOpen(boolean open) {
		this.dataManager.set(MOUTH_OPEN, open);
	}

	public boolean isMouthOpen() {
		return this.dataManager.get(MOUTH_OPEN);
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

	public void startAttack(ENetherDragonAttacks attackType) {
		if (this.isReadyToAttack) {
			this.currentAttack = attackType;
			this.setMouthOpen(true);
			this.attackTimer = 0;
		}
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesBoss.BOSS_DRAGON_NETHER.getLootTable();
	}
	
	@Override
	public boolean canOpenDoors() {
		return false;
	}

	@Override
	public boolean canPutOutFire() {
		return false;
	}

	@Override
	public boolean canIgniteTorch() {
		return false;
	}
	
	@Override
	protected void onDeathUpdate() {
		if (this.isSitting()) {
			this.setSitting(false);
		}
		++this.deathTicks;
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.setNoGravity(true);
		if(this.deathTicks % 2 == 0) {
			EntityCQRNetherDragonSegment segment = null;
			for(int i = this.dragonBodyParts.length -1; i >= 0; i--) {
				if(this.dragonBodyParts[i] != null && !this.dragonBodyParts[i].isDead) {
					segment = this.dragonBodyParts[i];
					break;
				}
			}
			if(segment != null) {
				segment.explode();
				if(!world.isRemote) {
					dropExperience(25, segment.posX,segment.posY, segment.posZ);
				}
			} else {
				//All segments are dead -> head is still there
				if(!world.isRemote) {
					this.world.createExplosion(this, posX, posY, posZ, 6, true);
					dropExperience(100, posX, posY, posZ);
				}
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				this.setDead();
				onFinalDeath();
			}
		}
	}
	
	private void dropExperience(int p_184668_1_, double x, double y, double z)
    {
        while (p_184668_1_ > 0)
        {
            int i = EntityXPOrb.getXPSplit(p_184668_1_);
            p_184668_1_ -= i;
            EntityXPOrb xp = new EntityXPOrb(this.world, x, y, z, i);
            xp.setEntityInvulnerable(true);
            this.world.spawnEntity(xp);
        }
    }

	public int getSegmentCount() {
		return this.segmentCount;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("segmentCount", this.segmentCount);
		compound.setInteger("phase", this.phase);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.segmentCount = compound.getInteger("segmentCount");
		this.phase = compound.getInteger("phase");
	}

}
