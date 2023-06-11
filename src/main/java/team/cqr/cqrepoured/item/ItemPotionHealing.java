package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemPotionHealing extends ItemLore {

	public ItemPotionHealing(Properties properties) {
		super(properties.stacksTo(16));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 24;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.getHealth() < playerIn.getMaxHealth()) {
			playerIn.startUsingItem(handIn);
			return ActionResult.success(playerIn.getItemInHand(handIn));
		}
		return ActionResult.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		stack.shrink(1);

		if (!worldIn.isClientSide) {
			if (entityLiving instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entityLiving;
				player.heal(4.0F);

				if (!player.isCreative()) {
					ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);

					if (stack.isEmpty()) {
						return bottle;
					} else if (!player.addItem(bottle)) {
						worldIn.addFreshEntity(new ItemEntity(worldIn, player.getX(), player.getY(), player.getZ(), bottle));
					}
				}
			} else {
				entityLiving.heal(entityLiving.getMaxHealth() * 0.5F);
			}
		}

		return stack;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
	/*	if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.healing_potion.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		} */
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

}
