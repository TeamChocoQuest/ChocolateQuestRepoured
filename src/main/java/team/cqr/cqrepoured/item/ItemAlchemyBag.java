package team.cqr.cqrepoured.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.util.GuiHandler;

public class ItemAlchemyBag extends ItemLore {

	public ItemAlchemyBag() {
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerIn, Hand handIn) {
		if (!world.isClientSide) {
			if (playerIn.isCrouching()) {
				playerIn.openGui(CQRMain.INSTANCE, GuiHandler.ALCHEMY_BAG_GUI_ID, world, handIn.ordinal(), 0, 0);
				return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
			}
			ItemStack stack = playerIn.getItemInHand(handIn);
			Item item = stack.getItem();
			if (item == this) {
				IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

				for (int i = 0; i < inventory.getSlots(); i++) {
					ItemStack potion = inventory.extractItem(i, 1, false);
					if (!potion.isEmpty()) {
						if (potion.getItem() instanceof SplashPotionItem || potion.getItem() instanceof LingeringPotionItem) {
							PotionEntity entitypotion = new PotionEntity(world, playerIn);
							entitypotion.setItem(potion);
							entitypotion.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
							world.addFreshEntity(entitypotion);

							world.playSound((PlayerEntity) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

							return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
						}
					}
				}
			}

		}
		return new ActionResult<>(ActionResultType.FAIL, playerIn.getItemInHand(handIn));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 5);
	}

}
