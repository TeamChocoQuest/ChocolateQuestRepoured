package team.cqr.cqrepoured.item.gun;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBullet.EBulletType;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemRevolver extends ItemLore implements IRangedWeapon {

	public ItemRevolver(Properties properties)
	{
		super(properties.durability(300));
		//this.setMaxDamage(300);
		//this.setMaxStackSize(1);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn)
	{
		tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.bullet_damage", 5.0).withStyle(ChatFormatting.BLUE)));
		tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.fire_rate", -30)).withStyle(ChatFormatting.RED));
		tooltip.add((new TranslationTextComponent("item.cqrepoured.tooltip.accuracy", -50)).withStyle(ChatFormatting.RED));

		ItemLore.addHoverTextLogic(tooltip, flagIn, "gun");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		// System.out.println("Hand: " + handIn.toString());
		ItemStack stack = playerIn.getItemInHand(handIn);
		boolean flag = !this.findAmmo(playerIn).isEmpty();

		if (!playerIn.abilities.instabuild && !flag && this.getBulletStack(stack, playerIn) == ItemStack.EMPTY) {
			if (flag) {
				this.shoot(stack, worldIn, playerIn);
			}
			return flag ? InteractionResultHolder.pass(stack) : new InteractionResultHolder(InteractionResult.FAIL, stack);
		}

		else {
			this.shoot(stack, worldIn, playerIn);
			return InteractionResultHolder.success(stack);
		}
	}

	public void shoot(ItemStack stack, Level worldIn, Player player) {
		boolean flag = player.abilities.instabuild;
		ItemStack itemstack = this.findAmmo(player);

		if (!itemstack.isEmpty() || flag) {
			if (!worldIn.isClientSide) {
				if (flag && itemstack.isEmpty()) {
					ProjectileBullet bulletE = new ProjectileBullet(player, worldIn, EBulletType.IRON);
					//ProjectileBullet bulletE = CQREntityTypes.PROJECTILE_BULLET.get().create(worldIn);
					//bulletE.setBulletType(1);
					bulletE.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.5F, 5F);
					player.getCooldowns().addCooldown(stack.getItem(), 10);
					worldIn.addFreshEntity(bulletE);
				} else {
					//ProjectileBullet bulletE = CQREntityTypes.PROJECTILE_BULLET.get().create(worldIn); //, player, this.getBulletType(itemstack));
					//bulletE.setBulletType(this.getBulletType(itemstack));
					ProjectileBullet bulletE = new ProjectileBullet(player, worldIn, this.getBulletType(itemstack));
					bulletE.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 3.5F, 5F);
					player.getCooldowns().addCooldown(stack.getItem(), 10);
					worldIn.addFreshEntity(bulletE);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
				}
			}

			worldIn.playLocalSound(player.position().x, player.position().y + player.getEyeHeight(), player.position().z, this.getShootSound(), SoundSource.MASTER, 1.0F, 0.9F + random.nextFloat() * 0.2F, false);
			player.xRot -= worldIn.random.nextFloat() * this.getRecoil();

			if (!flag) {
				itemstack.shrink(1);

				if (itemstack.isEmpty()) {
					player.inventory.removeItem(itemstack);
				}
			}
		}
	}

	protected float getRecoil() {
		return 10F;
	}

	protected boolean isBullet(ItemStack stack) {
		return stack.getItem() instanceof ItemBullet;
	}

	protected ItemStack findAmmo(Player player) {
		if (this.isBullet(player.getItemInHand(InteractionHand.OFF_HAND))) {
			return player.getItemInHand(InteractionHand.OFF_HAND);
		} else if (this.isBullet(player.getItemInHand(InteractionHand.MAIN_HAND))) {
			return player.getItemInHand(InteractionHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
				ItemStack itemstack = player.inventory.getItem(i);

				if (this.isBullet(itemstack)) {
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	protected ItemStack getBulletStack(ItemStack stack, Player player) {
		if (stack.getItem() == CQRItems.BULLET_IRON.get()) {
			return new ItemStack(CQRItems.BULLET_IRON.get());
		}

		if (stack.getItem() == CQRItems.BULLET_GOLD.get()) {
			return new ItemStack(CQRItems.BULLET_GOLD.get());
		}

		if (stack.getItem() == CQRItems.BULLET_DIAMOND.get()) {
			return new ItemStack(CQRItems.BULLET_DIAMOND.get());
		}

		if (stack.getItem() == CQRItems.BULLET_FIRE.get()) {
			return new ItemStack(CQRItems.BULLET_FIRE.get());
		} else {
			// System.out.println("IT'S A BUG!!!! IF YOU SEE THIS REPORT IT TO MOD'S AUTHOR");
			// return ItemStack.EMPTY; // #SHOULD NEVER HAPPEN
			return new ItemStack(CQRItems.BULLET_IRON.get());
		}
	}

	protected EBulletType getBulletType(ItemStack stack) {
		if(stack.getItem() instanceof ItemBullet) {
			return ((ItemBullet)stack.getItem()).getType();
		}
		return EBulletType.IRON;
	}

	@Override
	public void shoot(Level worldIn, LivingEntity shooter, Entity target, InteractionHand handIn) {
		if (!worldIn.isClientSide) {
			ItemStack bulletStack = new ItemStack(CQRItems.BULLET_IRON.get(), 1);
			if (shooter instanceof AbstractEntityCQR) {
				AbstractEntityCQR cqrEnt = (AbstractEntityCQR) shooter;
				ItemStack bullet = cqrEnt.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.ARROW);
				if (bullet != null && !bullet.isEmpty() && (bullet.getItem() instanceof ItemBullet)) {
					bulletStack = bullet;
					bullet.shrink(1);
				}
			}
			ProjectileBullet bulletE = new ProjectileBullet(shooter, worldIn, this.getBulletType(bulletStack));
			Vec3 v = target.position().subtract(shooter.position());
			v = v.normalize();
			v = v.scale(3.5D);
			// bulletE.setVelocity(v.x, v.y, v.z);
			bulletE.setDeltaMovement(v);
			//bulletE.motionX = v.x;
			//bulletE.motionY = v.y;
			//bulletE.motionZ = v.z;
			//bulletE.velocityChanged = true;
			worldIn.addFreshEntity(bulletE);
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.REVOLVER_SHOOT;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 60;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}