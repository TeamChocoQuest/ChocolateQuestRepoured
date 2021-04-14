package team.cqr.cqrepoured.objects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.util.Reference;

public class ItemAlchemyBag extends ItemLore {

	public ItemAlchemyBag() {
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand handIn) {
		if (!world.isRemote) {
			if(playerIn.isSneaking()) {
				playerIn.openGui(CQRMain.INSTANCE, Reference.ALCHEMY_BAG_GUI_ID, world, handIn.ordinal(), 0, 0);
				return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}
			ItemStack stack = playerIn.getHeldItem(handIn);
			Item item = stack.getItem();
			if(item == this) {
				IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				
				for(int i = 0; i < inventory.getSlots(); i++) {
					ItemStack potion = inventory.extractItem(i, 1, false);
					if(potion != null && !potion.isEmpty()) {
						if (potion.getItem() instanceof ItemSplashPotion || potion.getItem() instanceof ItemLingeringPotion) {
							EntityPotion entitypotion = new EntityPotion(world, playerIn, potion);
				            entitypotion.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
				            world.spawnEntity(entitypotion);
							
							world.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
							
							return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn)); 
						}
					}
				}
			}
			
		}
		return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 5);
	}

}
