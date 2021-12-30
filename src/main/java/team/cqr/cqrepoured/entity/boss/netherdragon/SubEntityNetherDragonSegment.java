package team.cqr.cqrepoured.entity.boss.netherdragon;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;

import net.minecraft.block.Block;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.entity.IDontRenderFire;

public class SubEntityNetherDragonSegment extends MultiPartEntityPart implements IBlacklistedFromStatues, IDontRenderFire {

	private EntityCQRNetherDragon dragon;
	private int partIndex = 0;
	private int realID = 0;
	private boolean isSkeletal = false;

	public SubEntityNetherDragonSegment(EntityCQRNetherDragon dragon, int partID, boolean skeletal) {
		super(dragon, "dragonPart" + partID, 0.5F, 0.5F);

		this.setSize(1.25F, 1.25F);
		this.dragon = dragon;
		this.partIndex = dragon.INITIAL_SEGMENT_COUNT - partID;
		this.realID = partID;

		// String partName, float width, float height
		this.setInvisible(false);
	}

	public void onRemovedFromBody() {
	}

	public boolean isSkeletal() {
		return this.isSkeletal || this.dragon == null || this.dragon.getSkeleProgress() >= this.realID;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isExplosion() || source.isFireDamage() || this.dragon == null) {
			return false;
		}

		return this.dragon.attackEntityFromPart(this, source, amount);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		++this.ticksExisted;

		if (this.dragon.getSegmentCount() < this.partIndex) {
			// this.world.removeEntityDangerously(this);
			this.setDead();
		}
		if (this.world.isRemote && (this.dragon == null || this.dragon.isDead)) {
			this.setDead();
		}

	}

	// As this is a part it does not make any noises
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
	}

	@Override
	public void setRotation(float yaw, float pitch) {
		super.setRotation(yaw, pitch);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (this.dragon == null || this.dragon.isDead) {
			return false;
		}
		return this.dragon.processInitialInteract(player, hand);
	}

	@Nullable
	public EntityCQRNetherDragon getParent() {
		return this.dragon;
	}

	public void explode() {
		if (!this.world.isRemote) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1, false);
		}
	}

	public void switchToSkeletalState() {
		if (!this.world.isRemote) {
			this.isSkeletal = true;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0, false);
		}
	}

	public void die() {
		this.explode();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("skeletal", this.isSkeletal());
		compound.setInteger("realID", this.realID);
		compound.setInteger("partIndex", this.partIndex);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.realID = compound.getInteger("realID");
		this.partIndex = compound.getInteger("partIndex");
	}

	@Override
	public boolean canBeTurnedToStone() {
		return false;
	}

	public int getPartIndex() {
		return this.partIndex;
	}

}
