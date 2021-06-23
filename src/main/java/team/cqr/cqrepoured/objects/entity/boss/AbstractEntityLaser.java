package team.cqr.cqrepoured.objects.entity.boss;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
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
	private final Map<BlockPos, BreakingInfo> blockBreakMap = new HashMap<>();
	protected Vec3d offsetVector = Vec3d.ZERO;

	private static class BreakingInfo {
		private static int counter = 0;
		public int lastTimeHit;
		public float progress;
		public int id = counter++ % 256;
	}

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
			RayTraceResult result = this.world.rayTraceBlocks(start, end, false, false, false);
			double d = result != null ? (float) result.hitVec.subtract(this.getPositionVector()).length() : this.length;

			if (result != null) {
				BlockPos pos = result.getBlockPos();
				IBlockState state = this.world.getBlockState(pos);
				if (this.canHitBlock(pos, state)) {
					float breakProgress = this.onHitBlock(pos, state);
					if (breakProgress > 0.0F) {
						if (breakProgress >= 1.0F) {
							// destroy block
							this.world.setBlockToAir(pos);
						} else {
							BreakingInfo breakingInfo = this.blockBreakMap.computeIfAbsent(pos, key -> new BreakingInfo());
							breakingInfo.lastTimeHit = this.ticksExisted;
							breakingInfo.progress += breakProgress;
							if (breakingInfo.progress >= 1.0F) {
								// destroy block
								this.world.setBlockToAir(pos);
								this.blockBreakMap.remove(pos);
								int i = 0x1000000 + this.getEntityId() * 256 + breakingInfo.id;
								this.world.sendBlockBreakProgress(i, pos, -1);
							}
						}
					}
				}
			}
			Iterator<Map.Entry<BlockPos, BreakingInfo>> iterator = this.blockBreakMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<BlockPos, BreakingInfo> entry = iterator.next();
				BreakingInfo breakingInfo = entry.getValue();
				if (this.ticksExisted - breakingInfo.lastTimeHit >= this.blockBreakThreshhold()) {
					breakingInfo.progress -= this.blockBreakRevert();
				}
				int i = 0x1000000 + this.getEntityId() * 256 + breakingInfo.id;
				if (breakingInfo.progress <= 0.0F) {
					iterator.remove();
					this.world.sendBlockBreakProgress(i, entry.getKey(), -1);
				} else {
					this.world.sendBlockBreakProgress(i, entry.getKey(), (int) (breakingInfo.progress * 10.0F));
				}
			}

			Vec3d vec1 = new Vec3d(-laserEffectRadius(), -laserEffectRadius(), 0.0D);
			Vec3d vec2 = new Vec3d(laserEffectRadius(), laserEffectRadius(), d);
			BoundingBox bb = new BoundingBox(vec1, vec2, Math.toRadians(this.rotationYawCQR), Math.toRadians(this.rotationPitchCQR), start);
			for (EntityLivingBase entity : BoundingBox.getEntitiesInsideBB(this.world, this.caster, EntityLivingBase.class, bb)) {
				if (this.canHitEntity(entity) && this.ticksExisted - this.hitInfoMap.getInt(entity) >= this.getEntityHitRate()) {
					this.onEntityHit(entity);
					this.hitInfoMap.put(entity, this.ticksExisted);
				}
			}
		}
	}

	public boolean canHitBlock(BlockPos pos, IBlockState state) {
		return true;
	}

	public float onHitBlock(BlockPos pos, IBlockState state) {
		float hardness = state.getBlockHardness(this.world, pos);
		if (hardness < 0.0F) {
			return 0.0F;
		}
		if (hardness == 0.0F) {
			return 1.0F;
		}
		int ticks;
		if (hardness <= 2.0F) {
			ticks = 40 + MathHelper.ceil(hardness * 20.0F);
		} else {
			ticks = MathHelper.ceil(20.0F * (8.0F * hardness) / (hardness + 2.0F));
		}
		return 1.0F / (float) ticks + 1.0E-7F;
	}

	public int blockBreakThreshhold() {
		return 60;
	}

	public float blockBreakRevert() {
		return 0.02F;
	}

	public int getEntityHitRate() {
		return 10;
	}

	public boolean canHitEntity(EntityLivingBase entity) {
		return !TargetUtil.isAllyCheckingLeaders(this.caster, entity);
	}

	public void onEntityHit(EntityLivingBase entity) {
		entity.attackEntityFrom(new DamageSource("ray").setDamageBypassesArmor(), this.getDamage());
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
		if (!this.world.isRemote) {
			Iterator<Map.Entry<BlockPos, BreakingInfo>> iterator = this.blockBreakMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<BlockPos, BreakingInfo> entry = iterator.next();
				int i = 0x1000000 + this.getEntityId() * 256 + entry.getValue().id;
				this.world.sendBlockBreakProgress(i, entry.getKey(), -1);
			}
		}
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
		this.offsetVector = new Vec3d(vx, vy, vz);
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
