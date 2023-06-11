package team.cqr.cqrepoured.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.ItemLore;

import java.util.List;

import javax.annotation.Nullable;

public class ItemBackpack extends ArmorItem {

	public ItemBackpack(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if(!worldIn.isClientSide)
		{
			if(handIn == InteractionHand.MAIN_HAND)
			{
				NetworkHooks.openGui((ServerPlayer) playerIn, new INamedContainerProvider() {
					@Override
					public TextComponent getDisplayName() {
						return new TranslationTextComponent("backpack.container"); //#TODO name
					}

					@Nullable
					@Override
					public Container createMenu(int windowId, Inventory inventory, Player player) {
						return CQRContainerTypes.BACKPACK.get().create(windowId, inventory);
					}
				});
				return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
			}
		}
		return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<TextComponent> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 36);
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	@Override
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return (A) CQRArmorModels.BACKPACK;
	}

}
