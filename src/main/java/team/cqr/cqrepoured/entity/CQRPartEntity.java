package team.cqr.cqrepoured.entity;

import org.joml.Vector3d;

import net.minecraft.entity.EntitySize;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public abstract class CQRPartEntity<T extends Entity> extends PartEntity<T> {
	
	protected EntitySize size;

	protected int newPosRotationIncrements;
	protected double interpTargetX;
	protected double interpTargetY;
	protected double interpTargetZ;
	protected double interpTargetYaw;
	protected double interpTargetPitch;
	public float renderYawOffset;
	public float prevRenderYawOffset;

	public int deathTime;
	public int hurtTime;

	public CQRPartEntity(T parent) {
		super(parent);
	}

	@OnlyIn(Dist.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		interpTargetX = x;
		interpTargetY = y;
		interpTargetZ = z;
		interpTargetYaw = yaw;
		interpTargetPitch = pitch;
		newPosRotationIncrements = posRotationIncrements;
	}
	
	@Override
	public void tick() {
		updateLastPos();
		super.tick();
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getX() + (this.interpTargetX - this.getX()) / (double)this.newPosRotationIncrements;
			double d2 = this.getY() + (this.interpTargetY - this.getY()) / (double)this.newPosRotationIncrements;
			double d4 = this.getZ() + (this.interpTargetZ - this.getZ()) / (double)this.newPosRotationIncrements;
			double d6 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.yRot);
			this.yRot = (float)((double)this.yRot + d6 / (double)this.newPosRotationIncrements);
			this.xRot = (float)((double)this.xRot + (this.interpTargetPitch - (double)this.xRot) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPos(d0, d2, d4);
			this.setRot(this.yRot, this.xRot);
		}

		while (yRot - yRotO < -180F) yRotO -= 360F;
		while (yRot - yRotO >= 180F) yRotO += 360F;

		while (renderYawOffset - prevRenderYawOffset < -180F) prevRenderYawOffset -= 360F;
		while (renderYawOffset - prevRenderYawOffset >= 180F) prevRenderYawOffset += 360F;

		while (xRot - xRotO < -180F) xRotO -= 360F;
		while (xRot - xRotO >= 180F) xRotO += 360F;
	}


	public void writeData(PacketBuffer buffer) {
		buffer.writeDouble(getX());
		buffer.writeDouble(getY());
		buffer.writeDouble(getZ());
		buffer.writeFloat(this.yRot);
		buffer.writeFloat(this.xRot);
		
		buffer.writeFloat(size.width);
		buffer.writeFloat(size.height);
		buffer.writeBoolean(size.fixed);

	}

	public void readData(PacketBuffer buffer) {
		Vector3d vec = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		setPositionAndRotationDirect(vec.x, vec.y, vec.z, buffer.readFloat(), buffer.readFloat(), 3);
		final float w = buffer.readFloat();
		final float h = buffer.readFloat();
		this.setSize(buffer.readBoolean() ? EntitySize.fixed(w, h) : EntitySize.scalable(w, h));
		this.refreshDimensions();
	}
	
	private void setSize(EntitySize size) {
		this.size = size;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	public final void updateLastPos() {
		this.setPosAndOldPos(getX(), getY(), getZ());
		yRotO = yRot;
		xRotO = xRot;
		tickCount++;
	}
	
	@Override
	public void setPosAndOldPos(double pX, double pY, double pZ) {
		super.setPosAndOldPos(pX, pY, pZ);
		
		this.setBoundingBox(this.size.makeBoundingBox(pX, pY, pZ));
	}
	
	/*@Override
	public void setPos(double pX, double pY, double pZ) {
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		
		this.xOld = this.xo;
		this.yOld = this.yo;
		this.zOld = this.zo;
		
		super.setPos(pX, pY, pZ);
	}*/
	
	public EntitySize getSize() {
		return this.size;
	}
	
	@Override
	public EntitySize getDimensions(Pose pPose) {
		return this.size;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if(this.level.isClientSide) {
			//return false;
		}
		if(this.isInvulnerableTo(pSource)) {
			return false;
		}
		if(this.getParent() instanceof IEntityMultiPart) {
			return ((IEntityMultiPart)this.getParent()).hurt(this, pSource, pAmount);
		}
		return super.hurt(pSource, pAmount);
	}
	
	@Override
	public boolean isInvisible() {
		//Return true to not render the hitbox two times
		return true;
	}
	
	public boolean hasCustomRenderer() {
		return false;
	}
	
}
