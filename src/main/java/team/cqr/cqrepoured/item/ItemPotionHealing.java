package team.cqr.cqrepoured.item;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.item.UseAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

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
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (playerIn.getHealth() < playerIn.getMaxHealth()) {
			playerIn.startUsingItem(handIn);
			return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
		}
		return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		stack.shrink(1);

		if (!worldIn.isClientSide) {
			if (entityLiving instanceof Player) {
				Player player = (Player) entityLiving;
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
	/*	if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.healing_potion.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		} */
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

}
