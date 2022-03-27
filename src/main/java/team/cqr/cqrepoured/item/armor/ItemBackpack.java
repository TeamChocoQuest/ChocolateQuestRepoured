package team.cqr.cqrepoured.item.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.inventory.BackpackInventory;
import team.cqr.cqrepoured.item.ItemLore;

import java.util.List;

import javax.annotation.Nullable;

public class ItemBackpack extends ArmorItem {

	public ItemBackpack(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(!worldIn.isClientSide)
		{
			if(handIn == Hand.MAIN_HAND)
			{
				//BackpackInventory.openGUI((ServerPlayerEntity)playerIn, playerIn.getMainHandItem());
				//NetworkHooks.openGui((ServerPlayerEntity)playerIn, new INamedContainerProvider());
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, new INamedContainerProvider() {
					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent("backpack");
					}

					@Nullable
					@Override
					public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
						return CQRContainerTypes.BACKPACK.get().create(windowId, inventory);
					}
				});
				//playerIn.openGui(CQRMain.INSTANCE, GuiHandler.BACKPACK_GUI_ID, worldIn, handIn.ordinal(), 0, 0);
				return ActionResult.success(playerIn.getItemInHand(handIn));
			}
		}
		return ActionResult.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 36);
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return (A) CQRArmorModels.BACKPACK;
	}

}
