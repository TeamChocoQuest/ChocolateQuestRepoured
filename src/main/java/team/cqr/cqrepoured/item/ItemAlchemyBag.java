package team.cqr.cqrepoured.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.inventory.ContainerAlchemyBag;

import javax.annotation.Nullable;

public class ItemAlchemyBag extends ItemLore {

	public ItemAlchemyBag(Properties properties)
	{
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand handIn)
	{
		if(!world.isClientSide)
		{
			if(playerIn.isCrouching())
			{
				NetworkHooks.openGui((ServerPlayer) playerIn, new INamedContainerProvider() {
					@Override
					public TextComponent getDisplayName() {
						return new TranslationTextComponent("alchemy_bag.container"); //#TODO name
					}

					@Nullable
					@Override
					public Container createMenu(int windowId, Inventory inventory, Player player) {
						//return CQRContainerTypes.ALCHEMY_BAG.get().create(windowId, inventory);
						return new ContainerAlchemyBag(windowId, inventory, handIn);
					}
				}, b -> b.writeInt(handIn == InteractionHand.MAIN_HAND ? 0 : 1));
				return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
			}

			ItemStack stack = playerIn.getItemInHand(handIn);
			Item item = stack.getItem();

			if(item == this)
			{
				LazyOptional<IItemHandler> cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

				if(cap.isPresent())
				{
					IItemHandler inventory = cap.resolve().get();

					for(int i = 0; i < inventory.getSlots(); i++)
					{
						ItemStack potion = inventory.extractItem(i, 1, false);
						if(!potion.isEmpty())
						{
							if(potion.getItem() instanceof SplashPotionItem || potion.getItem() instanceof LingeringPotionItem)
							{
								PotionEntity entitypotion = new PotionEntity(world, playerIn);
								entitypotion.setItem(potion);
								entitypotion.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, -20.0F, 0.5F, 1.0F);
								world.addFreshEntity(entitypotion);

								world.playSound(null, playerIn.position().x, playerIn.position().y, playerIn.position().z, SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

								return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
							}
						}
					}
				}
			}

		}
		return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 5);
	}
}
