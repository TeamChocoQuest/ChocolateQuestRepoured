package team.cqr.cqrepoured.item.staff;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

import java.util.Random;

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
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);
		playerIn.swing(handIn);
		this.shootFromEntity(playerIn);
		this.changeTorch(worldIn, playerIn);
		stack.hurtAndBreak(1, playerIn, p -> p.broadcastBreakEvent(handIn));
		playerIn.getCooldowns().addCooldown(stack.getItem(), 20);
		return InteractionResultHolder.success(stack);
	}

	public void changeTorch(Level worldIn, Player player)
	{
		Vec3 start = player.getEyePosition(1.0F);
		Vec3 end = start.add(player.getLookAngle().scale(10.0D)); //#TODO CHECK
		HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.BlockMode.VISUAL, ClipContext.FluidMode.NONE, player));

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
		Level world = shooter.level;

		if (!world.isClientSide) {
			Random r = shooter.getRandom();
			for (int i = 0; i < 20; i++) {
				// TODO don't send 20 packets
				Vec3 v = shooter.getLookAngle();
				v = v.add((r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D, (r.nextFloat() - 0.5D) / 3.0D);
				((ServerLevel) world).sendParticles(ParticleTypes.FLAME, shooter.position().x, shooter.position().y + shooter.getEyeHeight(), shooter.position().z, 0, v.x, v.y, v.z, r.nextFloat() + 0.2D);
			}

			world.getEntitiesOfClass(LivingEntity.class, shooter.getBoundingBox().inflate(8.0D), entity -> {
				if (TargetUtil.isAllyCheckingLeaders(shooter, entity)) {
					return false;
				}

				double x = Mth.clamp(shooter.position().x, entity.position().x - entity.getBbWidth() * 0.5D, entity.position().x + entity.getBbWidth() * 0.5D) - shooter.position().x;
				double y = Mth.clamp(shooter.position().y + shooter.getEyeHeight(), entity.position().y, entity.position().y + entity.getBbHeight()) - (shooter.position().y + shooter.getEyeHeight());
				double z = Mth.clamp(shooter.position().z, entity.position().z - entity.getBbWidth() * 0.5D, entity.position().z + entity.getBbWidth() * 0.5D) - shooter.position().z;
				if (x * x + y * y + z * z > 8.0D * 8.0D) {
					return false;
				}

				Vec3 a = shooter.getLookAngle();
				Vec3 b = new Vec3(x, y, z);
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
	public void shoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
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
