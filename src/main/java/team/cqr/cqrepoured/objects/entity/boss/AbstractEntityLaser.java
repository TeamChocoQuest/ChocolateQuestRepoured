package team.cqr.cqrepoured.objects.entity.boss;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.util.math.BoundingBox;

public abstract class AbstractEntityLaser extends Entity implements IEntityAdditionalSpawnData {

	public EntityLivingBase caster;
	public float length;
	public float rotationYawCQR;
	public float rotationPitchCQR;
	public float prevRotationYawCQR;
	public float prevRotationPitchCQR;
	public float serverRotationYawCQR;
	public float serverRotationPitchCQR;
	private final Object2IntMap<EntityLivingBase> hitInfoMap = new Object2IntOpenHashMap<>();
	private final Object2IntMap<BlockPos> blockBreakMap = new Object2IntOpenHashMap<>();
	protected Vec3d offsetVector = Vec3d.ZERO;

	public AbstractEntityLaser(World worldIn) {
		this(worldIn, null, 4.0F);
	}

	public AbstractEntityLaser(World worldIn, EntityLivingBase caster, float length) {
		super(worldIn);
		this.caster = caster;
		this.length = length;

		/*
		 * Vec3d vec1 = new Vec3d(this.caster.posX, this.caster.posY + this.caster.height * 0.6D, this.caster.posZ); Vec3d vec2 = new Vec3d(this.target.posX,
		 * this.target.posY + this.target.height * 0.6D, this.target.posZ); Vec3d vec3 =
		 * vec2.subtract(vec1).normalize(); double d = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z); float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		 * float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, d)); this.posX = vec1.x;
		 * this.posY = vec1.y; this.posZ = vec1.z; this.prevPosX = vec1.x; this.prevPosY = vec1.y; this.prevPosZ = vec1.z; this.lastTickPosX = vec1.x; this.lastTickPosY
		 * = vec1.y; this.lastTickPosZ = vec1.z; //this.rotationYawCQR = yaw;
		 * //this.rotationPitchCQR = pitch; this.setRotationYawCQR(yaw); this.setRotationPitchCQR(pitch); this.prevRotationYawCQR = yaw; this.prevRotationPitchCQR =
		 * pitch;
		 */

		this.setSize(0.1F, 0.1F);
		this.ignoreFrustumCheck = true;
		this.noClip = true;
	}
	
	public Vec3d getOffsetVector() {
		return this.offsetVector;
	}
	
	@Override
	public BlockPos getPosition() {
		return new BlockPos(this.getPositionVector());
	}
	
	@Override
	public Vec3d getPositionVector() {
		return super.getPositionVector().add(this.getOffsetVector());
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 64.0D * 64.0D;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public boolean writeToNBTAtomically(NBTTagCompound compound) {
		return false;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound) {
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return compound;
	}

	public double laserEffectRadius() {
		return 0.25D;
	}

	@Override
	public void onEntityUpdate() {
		if (!this.world.isRemote && !this.caster.isEntityAlive()) {
			this.setDead();
		}

		super.onEntityUpdate();

		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;

		if (this.world.isRemote) {
			this.rotationYawCQR = this.serverRotationYawCQR;
			this.rotationPitchCQR = this.serverRotationPitchCQR;
		} else {
			this.updatePositionAndRotation();
			CQRMain.NETWORK.sendToAllTracking(new SPacketSyncLaserRotation(this), this);
		}

		if (!this.world.isRemote) {
			Vec3d start = this.getPositionVector();
			Vec3d end = start.add(Vec3d.fromPitchYaw(this.rotationPitchCQR, this.rotationYawCQR).scale(this.length));
			RayTraceResult result = this.world.rayTraceBlocks(start, end, false, true, false);
			double d = result != null ? (float) result.hitVec.subtract(this.getPositionVector()).length() : this.length;
			if(this.canBreakBlocks() && result != null && result.getBlockPos() != null) {
				IBlockState blockState = this.world.getBlockState(result.getBlockPos()); 
				if(blockState != null && blockState.getBlock() != null) {
					boolean hitBlock = blockState.getBlock().canCollideCheck(blockState, false);
					if (hitBlock) {
						if (result != null) {
							BlockPos p = result.getBlockPos();
							int value = this.blockBreakMap.getInt(p);
							int breakProgress = value & 0xFFFF;
							breakProgress += this.getBreakingSpeed();
							if (breakProgress >= 60) {
								IBlockState state = this.world.getBlockState(p);
								state.getBlock().dropBlockAsItem(this.world, p, state, 0);
								this.world.setBlockToAir(p);
								this.blockBreakMap.remove(p);
								this.world.sendBlockBreakProgress(this.getEntityId(), p, -1);
							} else {
								this.blockBreakMap.put(p, (this.ticksExisted << 16) | breakProgress);
								this.world.sendBlockBreakProgress(this.getEntityId(), p, (int) (breakProgress / 60.0F * 10.0F));
							}
						}
						for (Object2IntMap.Entry<BlockPos> entry : this.blockBreakMap.object2IntEntrySet()) {
							int value = entry.getIntValue();
							int lastTimeHit = value >> 16;
							if (this.ticksExisted - lastTimeHit >= 40) {
								entry.setValue(lastTimeHit | Math.max((value & 0xFFFF) - 1, 0));
							}
						}
					}
				}
			}

			Vec3d vec1 = new Vec3d(-laserEffectRadius(), -laserEffectRadius(), 0.0D);
			Vec3d vec2 = new Vec3d(laserEffectRadius(), laserEffectRadius(), d);
			BoundingBox bb = new BoundingBox(vec1, vec2, Math.toRadians(this.rotationYawCQR), Math.toRadians(this.rotationPitchCQR), start);
			CQRFaction faction = FactionRegistry.instance().getFactionOf(this.caster);
			for (EntityLivingBase entity : BoundingBox.getEntitiesInsideBB(this.world, this.caster, EntityLivingBase.class, bb)) {
				if ((faction == null || !faction.isAlly(entity)) && this.ticksExisted - this.hitInfoMap.getInt(entity) >= 10) {
					this.hitInfoMap.put(entity, this.ticksExisted);
					entity.attackEntityFrom(new DamageSource("ray").setDamageBypassesArmor(), this.getDamage());
				}
			}
		}
	}
	
	public boolean canBreakBlocks() {
		return false;
	}
	
	public int getBreakingSpeed() {
		return 1;
	}

	public float getDamage() {
		return 3.0F;
	}

	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
		this.world.sendBlockBreakProgress(this.getEntityId(), BlockPos.ORIGIN, -1);
	}

	public abstract void updatePositionAndRotation();

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.caster.getEntityId());
		buffer.writeFloat(this.length);
		buffer.writeFloat(this.rotationYawCQR);
		buffer.writeFloat(this.rotationPitchCQR);
		
		buffer.writeDouble(this.offsetVector.x);
		buffer.writeDouble(this.offsetVector.y);
		buffer.writeDouble(this.offsetVector.z);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.caster = (EntityLivingBase) this.world.getEntityByID(additionalData.readInt());
		this.length = additionalData.readFloat();
		this.rotationYawCQR = additionalData.readFloat();
		this.rotationPitchCQR = additionalData.readFloat();
		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;
		
		double vx = additionalData.readDouble();
		double vy = additionalData.readDouble();
		double vz = additionalData.readDouble();
		this.offsetVector = new Vec3d(vx,vy,vz);
	}

	public float getColorR() {
		return 0.1F;
	}

	public float getColorG() {
		return 0.7F;
	}

	public float getColorB() {
		return 0.9F;
	}

}
