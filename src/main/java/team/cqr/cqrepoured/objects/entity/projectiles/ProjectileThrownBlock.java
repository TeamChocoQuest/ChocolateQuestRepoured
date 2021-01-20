package team.cqr.cqrepoured.objects.entity.projectiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.util.CQRConfig;

public class ProjectileThrownBlock extends ProjectileBase implements IEntityAdditionalSpawnData {
	
	private ResourceLocation block;
	private IBlockState state = null;
	private boolean explosive = false;
	private boolean placeOnImpact = false;

	public ProjectileThrownBlock(World worldIn) {
		super(worldIn);
		this.setSize(1,1);
	}

	private ProjectileThrownBlock(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.setSize(1,1);
	}

	public ProjectileThrownBlock(World worldIn, EntityLivingBase shooter, IBlockState block, boolean explodeOnImpact, boolean placeOnImpact) {
		super(worldIn, shooter);
		this.block = block.getBlock().getRegistryName();
		this.explosive = explodeOnImpact;
		this.placeOnImpact = placeOnImpact;
		this.state = block;
		this.setSize(1,1);
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
		
		if(this.explosive) {
			this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 3.0F, CQRConfig.bosses.thrownBlocksDestroyTerrain);
		} else if(CQRConfig.bosses.thrownBlocksGetPlaced && this.placeOnImpact) {
			this.world.setBlockState(this.getPosition(), this.state);
		}
		
		if (result.typeOfHit == Type.ENTITY) {
			if (result.entityHit == this.thrower) {
				return;
			}

			if (result.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart) result.entityHit).parent == this.thrower) {
				return;
			}
			
			result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, thrower), 10);
			
		}
		
		this.setDead();
	}

}
