package team.cqr.cqrepoured.entity.boss.spectrelord;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;

public class EntityTargetingLaser extends AbstractEntityLaser {

	protected LivingEntity target;
	protected float maxRotationPerTick = 2.0F;

	public EntityTargetingLaser(EntityType<? extends EntityTargetingLaser> type, World worldIn) {
		this(type, worldIn, null, 4.0F, null);
	}

	public EntityTargetingLaser(EntityType<? extends EntityTargetingLaser> type, World worldIn, LivingEntity caster, float length, LivingEntity target) {
		super(type, worldIn, caster, length);
		this.target = target;
	}

	@Override
	public void setupPositionAndRotation() {
		// TODO reduce unnecessary vec3d creation
		Vector3d vec1 = this.caster.position();
		vec1 = vec1.add(this.getOffsetVector());
		Vector3d vec2 = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
		Vector3d vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		this.rotationYawCQR = yaw;
		this.rotationPitchCQR = pitch;
		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void updatePositionAndRotation() {
		// TODO reduce unnecessary vec3d creation

		Vector3d vec1 = this.caster.position();

		vec1 = vec1.add(this.getOffsetVector());
		Vector3d vec2 = this.target.position().add(0, this.target.getBbHeight() * 0.6D, 0);
		Vector3d vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		// TODO make pitch rotatable to < -90 and > 90
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		float deltaYaw = MathHelper.wrapDegrees(yaw - this.rotationYawCQR);
		float deltaPitch = MathHelper.wrapDegrees(pitch - this.rotationPitchCQR);
		float delta = this.maxRotationPerTick / (float) Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
		if (delta > 1.0F) {
			delta = 1.0F;
		}
		deltaYaw *= delta;
		deltaPitch *= delta;
		this.rotationYawCQR += deltaYaw;
		this.rotationYawCQR = MathHelper.wrapDegrees(this.rotationYawCQR);
		this.rotationPitchCQR += deltaPitch;

		this.setPos(vec1.x, vec1.y, vec1.z);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		super.writeSpawnData(buffer);
		buffer.writeInt(this.target.getId());
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		super.readSpawnData(additionalData);
		this.target = (LivingEntity) this.level.getEntity(additionalData.readInt());
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {
		
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
