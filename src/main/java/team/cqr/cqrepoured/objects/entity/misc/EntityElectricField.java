package team.cqr.cqrepoured.objects.entity.misc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;

public class EntityElectricField extends Entity {
	
	private static HashSet<BlockPos> EXISTING_FIELDS = new HashSet<>();
	private Queue<EnumFacing> facesToSpreadTo = generateFacesQueue();

	private int charge;
	private int spreadTimer = 15;
	
	public EntityElectricField(World worldIn) {
		this(worldIn, 100);
	}
	
	private Queue<EnumFacing> generateFacesQueue() {
		Queue<EnumFacing> q = new LinkedList<>();
		
		q.add(EnumFacing.UP);
		q.add(EnumFacing.DOWN);
		q.add(EnumFacing.NORTH);
		q.add(EnumFacing.EAST);
		q.add(EnumFacing.SOUTH);
		q.add(EnumFacing.WEST);
		
		return q;
	}

	public EntityElectricField(World worldIn, int charge) {
		super(worldIn);
		this.charge = charge;
		this.setSize(1, 1);
	}

	@Override
	protected void entityInit() {
		//Unused
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		
		BlockPos p = new BlockPos(x,y,z);
		EXISTING_FIELDS.add(p);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
		if(entityIn instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) entityIn;
			if(living.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
				CapabilityElectricShock cap = living.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
				
				if(TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED.apply(living)) {
					cap.setRemainingTicks(200);
				}
			}
		}
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		
		if (!this.world.isRemote) {
			this.charge--;
			
			BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
			
			if(this.charge < 0) {
				this.setDead();
				EXISTING_FIELDS.remove(pos);
				return;
			}
			
			if(!this.facesToSpreadTo.isEmpty()) {
				this.spreadTimer--;
				if(this.spreadTimer == 0) {
					this.spreadTimer = 5;
					
					EnumFacing face = this.facesToSpreadTo.poll();
					if(face != null) {
						pos = pos.offset(face);
						if(!EXISTING_FIELDS.contains(pos)) {
							IBlockState blockState = this.world.getBlockState(pos);
							if(blockState.getMaterial().isLiquid() || blockState.getMaterial() == Material.IRON) {
								int charge = this.charge - 10;
								if(charge > 0) {
									EntityElectricField newField = new EntityElectricField(this.world, charge);
									newField.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
									
									this.world.spawnEntity(newField);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.charge = compound.getInteger("charge");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("charge", this.charge);
	}

}
