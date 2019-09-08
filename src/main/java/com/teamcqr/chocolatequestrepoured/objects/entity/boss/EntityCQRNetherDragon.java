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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
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
	
	enum EDragonMovementState {
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
	
	private EDragonMovementState movementState = EDragonMovementState.FLYING;

	private EntityCQRNetherDragonSegment[] dragonBodyParts = new EntityCQRNetherDragonSegment[16];
	
	private final BossInfoServer bossInfoServer = new BossInfoServer(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);

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
		this.setSize(2.0F, 2.0F);
		this.noClip = true;
		this.setNoGravity(true);
		this.experienceValue = 100;
		
		this.ignoreFrustumCheck = true;
		
		//Init the body parts
		for(int i = 0; i < dragonBodyParts.length; i++) {
			dragonBodyParts[i] = new EntityCQRNetherDragonSegment(this, i);
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
		if (source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage()) {
			//return this.attackEntityFromPart(this.headPart, source, amount);
		}

		return false;
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

		return damage > 0.01F;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		//TODO: Shoot fireball OR spit fire if close enough
		double distance = getDistance(target);
		
		if(distance > 25) {
			//Shoot fireball
		} else {
			//Spit fire
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		//Unused?
	}
	
	protected void moveParts() {
		for(int i = 0; i < this.dragonBodyParts.length; i++) {
			Entity foregoingPart = i == 0 ? this : this.dragonBodyParts[i -1];
			
			double headerX = foregoingPart.posX;
			double headerY = foregoingPart.posY;
			double headerZ = foregoingPart.posZ;
			
			float angle = (float) (((foregoingPart.rotationYaw +180.0F) * Math.PI) /180.0F);
			double idealX = -MathHelper.sin(angle);
			double idealZ = MathHelper.cos(angle);
			
			Vec3d movementDifference = new Vec3d(dragonBodyParts[i].posX - headerX, dragonBodyParts[i].posY, dragonBodyParts[i].posZ).normalize();
			movementDifference = movementDifference.addVector(idealX, 0, idealZ).normalize();
			
			
			double destinationX = headerX +1.0D * movementDifference.x;
			double destinationY = headerY +1.0D * movementDifference.y;
			double destinationZ = headerZ +1.0D * movementDifference.z;
			
			//Now update the position
			dragonBodyParts[i].setPosition(destinationX, destinationY, destinationZ);
			
			double distance = (double) MathHelper.sqrt(movementDifference.x * movementDifference.x + movementDifference.z * movementDifference.z + movementDifference.y * movementDifference.y);

			dragonBodyParts[i].setRotation((float) (Math.atan2(movementDifference.z, movementDifference.x) * 180.0D / Math.PI) + 90.0F,
					-(float) (Math.atan2(movementDifference.y, distance) * 180.0D / Math.PI));
		}
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		//DONE: Destroy the blocks
		destroyBlocksInAABB(getEntityBoundingBox());
		
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
	

}
