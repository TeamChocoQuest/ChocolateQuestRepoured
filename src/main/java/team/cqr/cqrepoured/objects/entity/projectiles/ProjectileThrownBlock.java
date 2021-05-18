package team.cqr.cqrepoured.objects.entity.projectiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.config.CQRConfig;

public class ProjectileThrownBlock extends ProjectileBase implements IEntityAdditionalSpawnData {

	private ResourceLocation block = Blocks.END_STONE.getRegistryName();
	private IBlockState state = null;
	private boolean placeOnImpact = false;

	public ProjectileThrownBlock(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
	}

	private ProjectileThrownBlock(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.setSize(1, 1);
	}

	public ProjectileThrownBlock(World worldIn, EntityLivingBase shooter, IBlockState block, boolean placeOnImpact) {
		super(worldIn, shooter);
		this.block = block.getBlock().getRegistryName();
		this.placeOnImpact = placeOnImpact;
		this.state = block;
		this.setSize(1, 1);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, this.block.toString());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.block = new ResourceLocation(ByteBufUtils.readUTF8String(additionalData));
		this.state = Block.REGISTRY.getObject(this.block).getDefaultState();
	}

	public IBlockState getBlock() {
		return this.state != null ? this.state : Blocks.BEDROCK.getDefaultState();
	}

	@Override
	public boolean hasNoGravity() {
		return false;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (this.world.isRemote) {
			return;
		}

		if (result.typeOfHit == Type.ENTITY) {
			if (result.entityHit == this.thrower) {
				return;
			}

			if (result.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart) result.entityHit).parent == this.thrower) {
				return;
			}

			result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, thrower), 10);
			this.setDead();
			return;
		} 
		if (CQRConfig.bosses.thrownBlocksGetPlaced && this.placeOnImpact) {
			this.world.setBlockState(new BlockPos(result.hitVec.x, result.hitVec.y, result.hitVec.z), this.state);
			//this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 1.5F, false);
			if (this.world instanceof WorldServer) {
				WorldServer ws = (WorldServer) this.world;
				Vec3d pos = result.hitVec;
				double particleSpeed = 0.2D;
				for (int i = 0; i < 50; i++) {
					double dx = -0.5 + this.rand.nextDouble();
					dx *= particleSpeed;
					double dy = -0.5 + this.rand.nextDouble();
					dy *= particleSpeed;
					double dz = -0.5 + this.rand.nextDouble();
					dz *= particleSpeed;
					ws.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.x, pos.y, pos.z, dx, dy, dz, Block.getStateId(this.state));
					this.playSound(this.state.getBlock().getSoundType(this.state, this.world, this.getPosition(), this).getPlaceSound(), 1.5F, 1.25F);
				}
			}
		}

		this.setDead();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		NBTTagCompound blockstate = new NBTTagCompound();
		NBTUtil.writeBlockState(blockstate, this.state);
		compound.setTag("blockdata", blockstate);
		super.writeEntityToNBT(compound);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		try {
			NBTTagCompound blockstate = compound.getCompoundTag("blockdata");
			this.state = NBTUtil.readBlockState(blockstate);
		} catch(Exception ex) {
			//Ignore
			this.state = Blocks.END_STONE.getDefaultState();
		}
		super.readEntityFromNBT(compound);
	}

}
