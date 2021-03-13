package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.util.ByteBufUtil;

public class EntityEndLaser extends AbstractEntityLaser {

	private BlockPos target;

	public EntityEndLaser(World worldIn) {
		this(worldIn, null, 4, new BlockPos(0, 0, 0));
	}

	public EntityEndLaser(EntityLivingBase caster, BlockPos target) {
		this(caster.world, caster, 32, target);
	}

	public EntityEndLaser(World worldIn, EntityLivingBase caster, float length, BlockPos target) {
		super(worldIn, caster, length);
		this.target = target;
	}

	@Override
	public void updatePositionAndRotation() {
		this.prevRotationYawCQR = this.rotationYawCQR;
		this.prevRotationPitchCQR = this.rotationPitchCQR;
		Vec3d vec1 = new Vec3d(this.caster.posX, this.caster.posY + this.caster.height * 0.6D, this.caster.posZ);
		Vec3d vec2 = new Vec3d(this.target);
		Vec3d vec3 = vec2.subtract(vec1).normalize();
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		this.rotationYawCQR += MathHelper.clamp(MathHelper.wrapDegrees(yaw - this.rotationYawCQR), -2.0F, 2.0F);
		this.rotationYawCQR = MathHelper.wrapDegrees(this.rotationYawCQR);
		this.rotationPitchCQR += MathHelper.clamp(MathHelper.wrapDegrees(pitch - this.rotationPitchCQR), -1.0F, 1.0F);
		Vec3d vec4 = Vec3d.fromPitchYaw(this.rotationPitchCQR, this.rotationYawCQR);
		this.setPosition(vec1.x + vec4.x * 0.25D, vec1.y + vec4.y * 0.25D, vec1.z + vec4.z * 0.25D);
	}

	@Override
	protected double laserEffectRadius() {
		return 1D;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		ByteBufUtil.writeBlockPos(buffer, this.target);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.target = ByteBufUtil.readBlockPos(additionalData);
	}

	@Override
	public float getColorR() {
		return 0.8F;
	}

	@Override
	public float getColorG() {
		return 0.01F;
	}

	@Override
	public float getColorB() {
		return 0.98F;
	}

}
