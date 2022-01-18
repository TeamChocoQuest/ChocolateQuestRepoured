package team.cqr.cqrepoured.entity.misc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntityElectricField extends Entity implements IDontRenderFire, IEntityOwnable {

	private static Set<BlockPos> EXISTING_FIELDS = new HashSet<>();
	private Queue<Direction> facesToSpreadTo = this.generateFacesQueue();

	private int charge;
	private int spreadTimer = 15;

	private UUID ownerID = null;

	public EntityElectricField(World worldIn) {
		this(worldIn, 100, null);
	}

	private Queue<Direction> generateFacesQueue() {
		Queue<Direction> q = new LinkedList<>();

		q.add(Direction.UP);
		q.add(Direction.DOWN);
		q.add(Direction.NORTH);
		q.add(Direction.EAST);
		q.add(Direction.SOUTH);
		q.add(Direction.WEST);

		return q;
	}

	public EntityElectricField(World worldIn, int charge, UUID ownerId) {
		super(worldIn);
		this.noClip = true;
		this.charge = charge;
		this.ownerID = ownerId;
		this.setSize(1.125F, 1.125F);
	}

	@Override
	protected void entityInit() {
		// Unused
	}

	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);

		BlockPos p = new BlockPos(x, y, z);
		EXISTING_FIELDS.add(p);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	protected List<LivingEntity> getEntitiesAffectedByField() {
		return this.world.getEntitiesWithinAABB(LivingEntity.class, this.getEntityBoundingBox(), this.selectionPredicate);
	}

	private Predicate<LivingEntity> selectionPredicate = input -> {
		if (!TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED.apply(input)) {
			return false;
		}
		if (this.ownerID == null || this.getOwner() == null || this.getOwner().isDead || !this.getOwner().isEntityAlive()) {
			return true;
		}

		if (input.getPersistentID().equals(this.ownerID)) {
			return false;
		}
		if (input instanceof IEntityOwnable) {
			return !((IEntityOwnable) input).getOwnerId().equals(this.ownerID);
		}
		Faction ownerFaction = FactionRegistry.instance(this).getFactionOf(this.getOwner());
		if (ownerFaction != null) {
			return TargetUtil.createPredicateNonAlly(ownerFaction).apply(input);
		}
		if (this.getOwner() instanceof LivingEntity) {
			return TargetUtil.isAllyCheckingLeaders((LivingEntity) this.getOwner(), input);
		}
		return true;
	};

	@Override
	public boolean isBurning() {
		return false;
	}

	public void destroyField() {
		this.setDead();
		EXISTING_FIELDS.remove(new BlockPos(this.posX, this.posY, this.posZ));
	}

	@Override
	public void baseTick() {
		super.baseTick();

		if (!this.world.isRemote) {
			this.charge--;

			BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);

			if (this.charge < 0) {
				this.destroyField();
				return;
			}

			this.getEntitiesAffectedByField().forEach(t -> {
				t.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).setRemainingTicks(200);
				t.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).setRemainingSpreads(4);
			});

			if (!this.facesToSpreadTo.isEmpty()) {
				this.spreadTimer--;
				if (this.spreadTimer == 0) {
					this.spreadTimer = 5;

					while (!this.facesToSpreadTo.isEmpty()) {
						Direction face = this.facesToSpreadTo.poll();
						if (face != null) {
							BlockPos currentPos = pos.offset(face);
							if (!EXISTING_FIELDS.contains(currentPos)) {
								BlockState blockState = this.world.getBlockState(currentPos);
								if (blockState.getMaterial().isLiquid() || blockState.getMaterial() == Material.IRON) {
									int charge = this.charge - 10;
									if (charge > 0) {
										EntityElectricField newField = new EntityElectricField(this.world, charge, this.ownerID);
										newField.setPosition(currentPos.getX() + 0.5, currentPos.getY(), currentPos.getZ() + 0.5);

										this.world.spawnEntity(newField);

										break;
									}
								}
							}
						} else {
							break;
						}
					}
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(CompoundNBT compound) {
		this.charge = compound.getInteger("charge");
		if (compound.hasKey("ownerId")) {
			this.ownerID = NBTUtil.getUUIDFromTag(compound.getCompoundTag("ownerId"));
		}
	}

	@Override
	protected void save(CompoundNBT compound) {
		compound.setInteger("charge", this.charge);
		if (this.getOwner() != null) {
			compound.setTag("ownerId", NBTUtil.createUUIDTag(this.getOwnerId()));
		}
	}

	@Nullable
	@Override
	public UUID getOwnerId() {
		return this.ownerID;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return EntityUtil.getEntityByUUID(this.world, this.ownerID);
	}

}
