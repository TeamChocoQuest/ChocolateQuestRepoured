package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityTargetingLaser;

public class EntityExterminatorHandLaser extends EntityTargetingLaser {

	public EntityExterminatorHandLaser(World worldIn) {
		super(worldIn);
	}

	public EntityExterminatorHandLaser(EntityLivingBase caster, EntityLivingBase target, Vec3d offset) {
		this(caster.world, caster, 48, target, offset);
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1 || pass == 0;
	}

	public EntityExterminatorHandLaser(World worldIn, EntityLivingBase caster, float length, EntityLivingBase target, Vec3d offset) {
		super(worldIn, caster, length, target);

		this.offsetVector = offset;

		// TODO reduce unnecessary vec3d creation
		Vec3d vec1 = new Vec3d(this.caster.posX, this.caster.posY + this.caster.height * 0.6D, this.caster.posZ);
		vec1 = vec1.add(this.getOffsetVector());
		Vec3d vec2 = new Vec3d(target.posX, target.posY + target.height * 0.6D, target.posZ);
		Vec3d vec3 = vec2.subtract(vec1);
		double dist = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
		float yaw = (float) Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
		float pitch = (float) Math.toDegrees(Math.atan2(-vec3.y, dist));
		this.rotationYawCQR = yaw;
		this.rotationPitchCQR = pitch;
		Vec3d vec4 = Vec3d.fromPitchYaw(this.rotationPitchCQR, this.rotationYawCQR);
		this.setPosition(vec1.x + vec4.x * 0.25D, vec1.y + vec4.y * 0.25D, vec1.z + vec4.z * 0.25D);

		this.maxRotationPerTick = 1.25F;
	}
	
	@Override
	public Vec3d getOffsetVector() {
		if(this.caster instanceof EntityCQRExterminator) {
			return ((EntityCQRExterminator) this.caster).getCannonFiringPointOffset(true).subtract(0, 1.75 * ((EntityCQRExterminator) this.caster).getSizeVariation(), 0);
		}
		return super.getOffsetVector();
	}

	@Override
	public float getColorR() {
		return 1.0F;
	}

	@Override
	public float getColorG() {
		return 0.0F;
	}

	@Override
	public float getColorB() {
		return 0.0F;
	}

	@Override
	public float getDamage() {
		return 1.25F;
	}

	@Override
	public boolean canBreakBlocks() {
		return true;
	}

	@Override
	public int getBreakingSpeed() {
		return 12;
	}

}
