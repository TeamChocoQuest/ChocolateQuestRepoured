package team.cqr.cqrepoured.item.staff;

import java.util.Random;

import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.block.TorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemStaffFire extends ItemLore implements IRangedWeapon {

	public ItemStaffFire(Properties properties)
	{
		super(properties.stacksTo(1).durability(2048));
		//this.setMaxStackSize(1);
		//.setMaxDamage(2048);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		boolean flag = super.hurtEnemy(stack, target, attacker);

		if (flag && random.nextInt(5) == 0) {
			if (target.getVehicle() != null) {
				target.stopRiding();
			}
		}
		return flag;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.swing(handIn);
		this.shootFromEntity(playerIn);
		this.changeTorch(worldIn, playerIn);
		stack.hurtAndBreak(1, playerIn, p -> p.broadcastBreakEvent(handIn));
		playerIn.getCooldowns().addCooldown(stack.getItem(), 20);
		return ActionResult.success(stack);
	}

	public void changeTorch(World worldIn, PlayerEntity player)
	{
		Vector3d start = player.getEyePosition(1.0F);
		Vector3d end = start.add(player.getLookAngle().scale(10.0D)); //#TODO CHECK
		RayTraceResult result = worldIn.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, player));

		if (result != null && !worldIn.isClientSide) {
			BlockPos pos = new BlockPos(result.getLocation().x, result.getLocation().y, result.getLocation().z);
			BlockState blockStateLookingAt = worldIn.getBlockState(pos);

			if(blockStateLookingAt.getBlock() == CQRBlocks.UNLIT_TORCH.get())
			{
				worldIn.setBlockAndUpdate(pos, Blocks.TORCH.defaultBlockState());//.setValue(TorchBlock.FACING, blockStateLookingAt.getValue(TorchBlock.FACING)));
			}
			else if(blockStateLookingAt.getBlock() == CQRBlocks.UNLIT_TORCH_WALL.get())
			{
				worldIn.setBlockAndUpdate(pos, Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, blockStateLookingAt.getValue(WallTorchBlock.FACING)));
			}
		}
	}

	public void shootFromEntity(LivingEntity shooter) {
		World world = shooter.level;

		if (!world.isClientSide) {
			Random r = shooter.getRandom();
			for (int i = 0; i < 20; i++) {
				// TODO don't send 20 packets
				Vector3d v = shooter.getLookAngle();
				v = v.add((r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D);
				((ServerWorld) world).sendParticles(ParticleTypes.FLAME, shooter.position().x, shooter.position().y + shooter.getEyeHeight(), shooter.position().z, 0, v.x, v.y, v.z, r.nextFloat() + 0.2D);
			}

			world.getEntitiesOfClass(LivingEntity.class, shooter.getBoundingBox().inflate(8.0D), entity -> {
				if (TargetUtil.isAllyCheckingLeaders(shooter, entity)) {
					return false;
				}

				double x = MathHelper.clamp(shooter.position().x, entity.position().x - entity.getBbWidth() * 0.5D, entity.position().x + entity.getBbWidth() * 0.5D) - shooter.position().x;
				double y = MathHelper.clamp(shooter.position().y + shooter.getEyeHeight(), entity.position().y, entity.position().y + entity.getBbHeight()) - (shooter.position().y + shooter.getEyeHeight());
				double z = MathHelper.clamp(shooter.position().z, entity.position().z - entity.getBbWidth() * 0.5D, entity.position().z + entity.getBbWidth() * 0.5D) - shooter.position().z;
				if (x * x + y * y + z * z > 8.0D * 8.0D) {
					return false;
				}

				Vector3d a = shooter.getLookAngle();
				Vector3d b = new Vector3d(x, y, z);
				if (Math.toDegrees(Math.acos(a.dot(b) / (a.length() * b.length()))) > 40.0D) {
					return false;
				}

				return shooter.canSee(entity);
			}).forEach(target -> {
				if (target.hurt(DamageSource.mobAttack(shooter).setIsFire(), 3.0F)) {
					target.setSecondsOnFire(5);
				}
			});
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		this.shootFromEntity(shooter);
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.GHAST_SHOOT;
	}

	@Override
	public double getRange() {
		return 7.5D;
	}

	@Override
	public int getCooldown() {
		return 60;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
