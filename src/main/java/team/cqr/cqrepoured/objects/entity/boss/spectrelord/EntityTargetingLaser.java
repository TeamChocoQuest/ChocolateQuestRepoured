package team.cqr.cqrepoured.objects.entity.boss.spectrelord;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;

public class EntityTargetingLaser extends AbstractEntityLaser {

	private EntityLivingBase target;
	protected float maxRotationPerTick = 2.0F;

	public EntityTargetingLaser(World worldIn) {
		this(worldIn, null, 4.0F, null);
	}

	public EntityTargetingLaser(World worldIn, EntityLivingBase caster, float length, EntityLivingBase target) {
		super(worldIn, caster, length);
		this.target = target;
	}

	@Override
	public void updatePositionAndRotation() {
		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;
		Vec3d vec1 = new Vec3d(this.caster.posX, this.caster.posY + this.caster.height * 0.6D, this.caster.posZ);
		vec1 = vec1.add(this.getOffsetVector());
		Vec3d vec2 = new Vec3d(this.target.posX, this.target.posY + this.target.height * 0.6D, this.target.posZ);
		Vec3d vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
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
		Vec3d vec4 = Vec3d.fromPitchYaw(this.rotationPitchCQR, this.rotationYawCQR);
		this.setPosition(vec1.x + vec4.x * 0.25D, vec1.y + vec4.y * 0.25D, vec1.z + vec4.z * 0.25D);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeInt(this.target.getEntityId());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.target = (EntityLivingBase) this.world.getEntityByID(additionalData.readInt());
	}

}
