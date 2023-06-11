package team.cqr.cqrepoured.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.inventory.ContainerBadge;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBadge extends ItemLore {

	public ItemBadge(Properties properties)
	{
		super(properties.stacksTo(1));
		//this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (player.isCreative()) {
			if (!player.level.isClientSide) {
				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, stack.copy());
					((ServerLevel) player.level).sendParticles((ServerPlayer) player, ParticleTypes.WITCH, false, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 8, 0.5D, 0.5D, 0.5D, 0.1D);
				} else {
					LazyOptional<IItemHandler> capability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

					if(capability.isPresent()) {
						IItemHandler handler = capability.resolve().get();
						ListTag itemList = new ListTag();
						for (int i = 0; i < handler.getSlots(); i++) {
							ItemStack stack1 = handler.getStackInSlot(i);
							CompoundTag slotTag = new CompoundTag();
							slotTag.putInt("Index", 0);
							stack1.save(slotTag);
							itemList.add(slotTag);
						}
						if (!itemList.isEmpty()) {
							entity.getPersistentData().put("Items", itemList);
							((ServerLevel) player.level).sendParticles((ServerPlayer) player, ParticleTypes.WITCH, false, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 8, 0.5D, 0.5D, 0.5D, 0.1D);
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (playerIn.isCreative()) {
			if (!worldIn.isClientSide)
			{
				NetworkHooks.openGui((ServerPlayer) playerIn, new INamedContainerProvider() {
					@Override
					public TextComponent getDisplayName() {
						return new TranslationTextComponent("badge.container"); //#TODO name
					}

					@Nullable
					@Override
					public Container createMenu(int windowId, Inventory inventory, Player player) {
						//return CQRContainerTypes.ALCHEMY_BAG.get().create(windowId, inventory);
						return new ContainerBadge(windowId, inventory, handIn);
					}
				}, b -> b.writeInt(handIn == InteractionHand.MAIN_HAND ? 0 : 1));
				//playerIn.openGui(CQRMain.INSTANCE, GuiHandler.BADGE_GUI_ID, worldIn, handIn.ordinal(), 0, 0);
			}
			return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
		}
		return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	/*	if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.badge.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		} */

		LazyOptional<IItemHandler> capability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		if(capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();
			for (int i = 0; i < handler.getSlots(); i++) {
				ItemStack stack1 = handler.getStackInSlot(i);
				if (!stack1.isEmpty()) {
					tooltip.add(new TranslationTextComponent(stack1.getDescriptionId(), stack1.getCount())); //#TODO okay?
					//tooltip.add(stack1.getDisplayName() + " x" + stack1.getCount());
				}
			}
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 9);
	}
}