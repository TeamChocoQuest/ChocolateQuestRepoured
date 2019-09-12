package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRNetherDragon extends AbstractEntityCQR implements IEntityMultiPart, IRangedAttackMob {
	
	public enum EDragonMovementState {
		CHARGING,
		FLYING,
		//When it is flying up or down, it will spiral up or down
		FLYING_UPWARDS,
		FLYING_DOWNWARDS
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
	
	public static final int SEGMENT_COUNT = 32;
	
	private EDragonMovementState movementState = EDragonMovementState.FLYING;

	private EntityCQRNetherDragonSegment[] dragonBodyParts = new EntityCQRNetherDragonSegment[SEGMENT_COUNT];
	
	private final BossInfoServer bossInfoServer = new BossInfoServer(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);

	private boolean mouthOpen = false;

	/*
	 * Notes: This dragon is meant to "swim" through the skies, it moves like a snake, so the model needs animation, also the parts are meant to move like the parts from Twilight Forests Naga
	 * 
	 * Also the nether dragon destroys all blocks in its hitbox, if these are not lava, also if the block it moved through are leaves or burnable, it will set them on fire
	 * It will also break obsidian blocks, but not command blocks or structure blocks or bedrock
	 */
	
	public EntityCQRNetherDragon(World worldIn) {
		super(worldIn);
		/*this.dragonBodyParts = new MultiPartEntityPart[] { this.headPart, this.body1, this.body2, this.body3,
				this.body4, this.body5, this.body6, this.body7, this.body8, this.body9, this.body10, this.body11,
				this.body12, this.body13, this.body14, this.body15, this.body16 };*/
		this.setSize(1.5F, 1.5F);
		this.noClip = true;
		this.setNoGravity(true);
		this.experienceValue = 100;
		
		this.ignoreFrustumCheck = true;
		
		//Init the body parts
		for(int i = 0; i < dragonBodyParts.length; i++) {
			dragonBodyParts[i] = new EntityCQRNetherDragonSegment(this, i +1);
		}
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfoServer.setName(getDisplayName());
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		/*if (source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage()) {
			//return this.attackEntityFromPart(this.headPart, source, amount);
			return true;
		}*/

		return super.attackEntityFrom(source, amount);
	}
	

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.NETHER_DRAGON.getValue();
	}

	@Override
	public EFaction getFaction() {
		return null;
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
		//if (dragonPart != this.headPart) {
			damage = damage / 4.0F + Math.min(damage, 1.0F);
		//}

		return attackEntityFrom(source, damage);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		//TODO: Shoot fireball OR spit fire if close enough
		double distance = getDistance(target);
		
		if(distance > 25) {
			//Shoot fireball
		} else {
			//Spit fire
			setMouthOpen(true);
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		//Unused?
	}
	
	//This code is not entirely made by me, it is oriented from this:
	//https://github.com/TeamTwilight/twilightforest/blob/1.12.x/src/main/java/twilightforest/entity/boss/EntityTFNaga.java
	protected void moveParts() {
		for (int i = 0; i < this.dragonBodyParts.length; i++) {
			Entity leader = i == 0 ? this : this.dragonBodyParts[i - 1];
			
			double headerX = leader.posX;
			double headerY = leader.posY;
			double headerZ = leader.posZ;

			float angle = (((leader.rotationYaw + 180) * new Float(Math.PI)) / 180F);

			double straightDegree = 0.05D + (1.0 / (float) (i + 1)) * 0.5D;

			double calculatedRotatedX = -MathHelper.sin(angle) * straightDegree;
			double calculatedRotatedZ = MathHelper.cos(angle) * straightDegree;

			double x = dragonBodyParts[i].posX;
			double y = dragonBodyParts[i].posY;
			double z = dragonBodyParts[i].posZ;
			
			Vec3d deltaPos = new Vec3d(x - headerX, y - headerY, z - headerZ);
			deltaPos = deltaPos.normalize();

			deltaPos = deltaPos.add(new Vec3d(calculatedRotatedX, 0, calculatedRotatedZ).normalize());

			//Dont change these values, they are important for the correct allignment of the segments!!!
			double f = i != 0 ? 0.378D : 0.338D;

			double targetX = headerX + f * deltaPos.x;
			double targetY = headerY + f * deltaPos.y;
			double targetZ = headerZ + f * deltaPos.z;

			//Set rotated position
			dragonBodyParts[i].setPosition(targetX, targetY, targetZ);

			double distance = (double) MathHelper.sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z);
			//Finally apply the new rotation -> Rotate the block
			dragonBodyParts[i].setRotation((float) (Math.atan2(deltaPos.z, deltaPos.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(deltaPos.y, distance) * 180.0D / Math.PI));
		}
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
		//DONE: Destroy the blocks
		destroyBlocksInAABB(getEntityBoundingBox());
		/*for(EntityCQRNetherDragonSegment part : this.dragonBodyParts) {
			destroyBlocksInAABB(part.getEntityBoundingBox());
		}*/
		
	}
	
	//Copied from ender dragon
	private boolean destroyBlocksInAABB(AxisAlignedBB aabb)
    {
		if(getWorld().getGameRules().hasRule("mobGriefing") && !getWorld().getGameRules().getBoolean("mobGriefing")) {
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

        for (int k1 = x1; k1 <= x2; ++k1)
        {
            for (int l1 = y1; l1 <= y2; ++l1)
            {
                for (int i2 = z1; i2 <= z2; ++i2)
                {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (!block.isAir(iblockstate, this.world, blockpos) && iblockstate.getMaterial() != Material.FIRE)
                    {
                        if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this))
                        {
                            cancelled = true;
                        }
                        //Check if the entity can destroy the blocks -> Event that can be cancelled by e.g. anti griefing mods or the protection system
                        else if (net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate))
                        {
                            if (block != Blocks.BEDROCK && block != Blocks.STRUCTURE_BLOCK && block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY)
                            {
                                blockDestroyed = this.world.setBlockToAir(blockpos) || blockDestroyed;
                            }
                            else
                            {
                                cancelled = true;
                            }
                        }
                        else
                        {
                            cancelled = true;
                        }
                    }
                }
            }
        }

        if (blockDestroyed)
        {
            double x = aabb.minX + (aabb.maxX - aabb.minX) * (double)this.rand.nextFloat();
            double y = aabb.minY + (aabb.maxY - aabb.minY) * (double)this.rand.nextFloat();
            double z = aabb.minZ + (aabb.maxZ - aabb.minZ) * (double)this.rand.nextFloat();
            
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0.0D, 0.0D, 0.0D);
        }

        return cancelled;
    }
	
	@Override
	public void onUpdate() {
		super.onUpdate();

		// update bodySegments parts
		for (EntityCQRNetherDragonSegment segment : dragonBodyParts) {
			this.world.updateEntityWithOptionalForce(segment, true);
		}

		moveParts();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return super.getHurtSound(damageSourceIn);
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return super.getDeathSound();
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return super.getAmbientSound();
	}
	
	@Override
	public void setDead() {
		super.setDead();
		for (EntityCQRNetherDragonSegment dragonPart : dragonBodyParts) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			this.world.removeEntityDangerously(dragonPart);
		}
	}
	
	@Override
	public Entity[] getParts() {
		return dragonBodyParts;
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
		return movementState;
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
		mouthOpen = open;
	}
	
	public boolean isMouthOpen() {
		return mouthOpen ;
	}
	

}
