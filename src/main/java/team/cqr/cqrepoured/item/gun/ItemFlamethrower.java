package team.cqr.cqrepoured.item.gun;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.item.ItemFuelUsing;

public class ItemFlamethrower extends ItemFuelUsing {

	public ItemFlamethrower() {
		super(new Predicate<ItemStack>() {

			@Override
			public boolean test(ItemStack t) {
				return t != null && t.getItem() == Items.SLIME_BALL;
			}
		});
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return this.getFuelInItem(stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	public void shootFlames(EntityLivingBase entity) {
		World world = entity.world;
		float rotationYaw = MathHelper.wrapDegrees(entity.rotationYawHead);
		double armDist = 1.0D;
		double offY = entity.height * 0.75D;
		double posX = entity.posX - Math.sin(Math.toRadians(rotationYaw)) * armDist;
		double posY = entity.posY + offY;
		double posZ = entity.posZ + Math.cos(Math.toRadians(rotationYaw)) * armDist;

		float x = (float) -Math.sin(Math.toRadians(rotationYaw));
		float z = (float) Math.cos(Math.toRadians(rotationYaw));
		double y = -Math.sin(Math.toRadians(entity.rotationPitch));
		x = (float) (x * (1.0D - Math.abs(y)));
		z = (float) (z * (1.0D - Math.abs(y)));

		if (world.isRemote) {
			for (int i = 0; i < 8; i++) {
				world.spawnParticle(EnumParticleTypes.FLAME, posX, posY, posZ, (x + itemRand.nextFloat() - 0.5D) / 3.0D, (y + itemRand.nextFloat() - 0.5D) / 8.0D, (z + itemRand.nextFloat() - 0.5D) / 3.0D);
			}
		} else {
			int dist = 10;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().grow(entity.getLookVec().x * dist, entity.getLookVec().y * dist, entity.getLookVec().z * dist).expand(1.0D, 1.0D, 1.0D));

			for (Entity e : list) {
				if (e instanceof EntityLivingBase && !e.isWet() && !e.isBeingRidden()) {
					double d = posX - e.posX;
					double d2 = posZ - e.posZ;
					double rotDiff = Math.atan2(d, d2);
					rotDiff = rotDiff * 180.0D / 3.141592D;
					rotDiff = -MathHelper.wrapDegrees(rotDiff - 180.0D);
					rotDiff -= rotationYaw;

					if (Math.abs(rotDiff) < 30.0D) {
						e.setFire(2);
						e.attackEntityFrom(DamageSource.IN_FIRE, 2.5F);
					}
				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (this.getFuelInItem(playerIn.getHeldItem(handIn)) <= 0) {
			return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		}
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;
			if (player.getActiveItemStack() == stack) {
				if (player.isHandActive()) {
					this.shootFlames((EntityLivingBase) entityIn);
					this.removeFuel(stack, 1);
				}
			} else if((((EntityLivingBase) entityIn).getHeldItemMainhand() == stack || ((EntityLivingBase) entityIn).getHeldItemOffhand() == stack) && entityIn.ticksExisted % 5 == 0 && this.getFuelInItem(stack) < this.getMaxFuel()) {
				this.refuelFromInventory(player.inventory, stack, !player.isCreative());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.flamethrower.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
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

	@Override
	public int getMaxFuel() {
		return 2000;
	}

	@Override
	protected int getFuelForSingleItem(ItemStack fuelItem) {
		return 20;
	}

	@Override
	protected int getMaxProcessedItemsPerRefuelCycle() {
		return 1;
	}

}
