package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.inventory.ContainerBadge;

public class ItemBadge extends ItemLore {

	public ItemBadge(Properties properties)
	{
		super(properties.stacksTo(1));
		//this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (player.isCreative()) {
			if (!player.level.isClientSide) {
				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, stack.copy());
					((ServerWorld) player.level).sendParticles((ServerPlayerEntity) player, ParticleTypes.WITCH, false, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 8, 0.5D, 0.5D, 0.5D, 0.1D);
				} else {
					LazyOptional<IItemHandler> capability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

					if(capability.isPresent()) {
						IItemHandler handler = capability.resolve().get();
						ListNBT itemList = new ListNBT();
						for (int i = 0; i < handler.getSlots(); i++) {
							ItemStack stack1 = handler.getStackInSlot(i);
							CompoundNBT slotTag = new CompoundNBT();
							slotTag.putInt("Index", 0);
							stack1.save(slotTag);
							itemList.add(slotTag);
						}
						if (!itemList.isEmpty()) {
							entity.getPersistentData().put("Items", itemList);
							((ServerWorld) player.level).sendParticles((ServerPlayerEntity) player, ParticleTypes.WITCH, false, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 8, 0.5D, 0.5D, 0.5D, 0.1D);
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (playerIn.isCreative()) {
			if (!worldIn.isClientSide)
			{
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, new INamedContainerProvider() {
					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent("badge.container"); //#TODO name
					}

					@Nullable
					@Override
					public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
						//return CQRContainerTypes.ALCHEMY_BAG.get().create(windowId, inventory);
						return new ContainerBadge(windowId, inventory, handIn);
					}
				}, b -> b.writeInt(handIn == Hand.MAIN_HAND ? 0 : 1));
				//playerIn.openGui(CQRMain.INSTANCE, GuiHandler.BADGE_GUI_ID, worldIn, handIn.ordinal(), 0, 0);
			}
			return ActionResult.success(playerIn.getItemInHand(handIn));
		}
		return ActionResult.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
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
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 9);
	}
}