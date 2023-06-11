package team.cqr.cqrepoured.entity.misc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.util.EntityUtil;

public class EntityElectricField extends Entity implements IDontRenderFire, IEntityOwnable {

	private static Set<BlockPos> EXISTING_FIELDS = new HashSet<>();
	private Queue<Direction> facesToSpreadTo = this.generateFacesQueue();

	private int charge;
	private int spreadTimer = 15;

	private UUID ownerID = null;

	public EntityElectricField(EntityType<? extends EntityElectricField> type, World worldIn) {
		this(type, worldIn, 100, null);
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
		this(CQREntityTypes.ELECTRIC_FIELD.get(), worldIn, charge, ownerId);
	}

	public EntityElectricField(EntityType<? extends EntityElectricField> type, World worldIn, int charge, UUID ownerId) {
		super(type, worldIn);
		this.noPhysics = true;
		this.charge = charge;
		this.ownerID = ownerId;
	}

	@Override
	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);

		BlockPos p = new BlockPos(x, y, z);
		EXISTING_FIELDS.add(p);
	}

	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	public boolean isPushedByFluid() {
		return false;
	}
	
	protected List<LivingEntity> getEntitiesAffectedByField() {
		return this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), this.selectionPredicate);
	}

	private Predicate<LivingEntity> selectionPredicate = input -> {
		if (!TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED.apply(input)) {
			return false;
		}
		if (this.ownerID == null || this.getOwner() == null /*|| this.getOwner().dead*/ || !this.getOwner().isAlive()) {
			return true;
		}

		if (input.getUUID().equals(this.ownerID)) {
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
	public boolean isOnFire() {
		return false;
	}

	public void destroyField() {
		EXISTING_FIELDS.remove(this.blockPosition());
		this.remove();
	}

	@Override
	public void baseTick() {
		super.baseTick();

		if (!this.level.isClientSide) {
			this.charge--;

			BlockPos pos = this.blockPosition();

			if (this.charge < 0) {
				this.destroyField();
				return;
			}

			this.getEntitiesAffectedByField().forEach(t -> {
				t.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).ifPresent(cap -> {
					cap.setRemainingTicks(200);
					cap.setRemainingSpreads(4);
				});
				
				//t.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).setRemainingTicks(200);
				//t.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).setRemainingSpreads(4);
			});

			if (!this.facesToSpreadTo.isEmpty()) {
				this.spreadTimer--;
				if (this.spreadTimer == 0) {
					this.spreadTimer = 5;

					while (!this.facesToSpreadTo.isEmpty()) {
						Direction face = this.facesToSpreadTo.poll();
						if (face != null) {
							BlockPos currentPos = pos.relative(face); //Correct?
							if (!EXISTING_FIELDS.contains(currentPos)) {
								BlockState blockState = this.level.getBlockState(currentPos);
								if (blockState.getMaterial().isLiquid() || blockState.getMaterial() == Material.METAL) {
									int charge = this.charge - 10;
									if (charge > 0) {
										EntityElectricField newField = new EntityElectricField(this.level, charge, this.ownerID);
										newField.setPos(currentPos.getX() + 0.5, currentPos.getY(), currentPos.getZ() + 0.5);

										this.level.addFreshEntity(newField);

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
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.charge = compound.getInt("charge");
		if (compound.contains("ownerId")) {
			this.ownerID = NBTUtil.loadUUID(compound.getCompound("ownerId"));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putInt("charge", this.charge);
		if (this.getOwner() != null) {
			compound.put("ownerId", NBTUtil.createUUID(this.getOwnerId()));
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
		return EntityUtil.getEntityByUUID(this.level, this.ownerID);
	}

	@Override
	protected void defineSynchedData() {
		//NOT NEEDED
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
