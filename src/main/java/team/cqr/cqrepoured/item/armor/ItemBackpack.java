package team.cqr.cqrepoured.item.armor;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;
import team.cqr.cqrepoured.client.render.armor.RenderArmorBackpack;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemBackpack extends CQRGeoArmorBase implements GeoItem {

	public ItemBackpack(ArmorMaterial materialIn, Type equipmentSlotIn, Properties prop) {
		super(materialIn, equipmentSlotIn, prop);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if(!worldIn.isClientSide)
		{
			if(handIn == InteractionHand.MAIN_HAND)
			{
				NetworkHooks.openScreen((ServerPlayer) playerIn, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.translatable("backpack.container"); //#TODO name
					}

					@Nullable
					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
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
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLore.addHoverTextLogic(tooltip, flagIn, ForgeRegistries.ITEMS.getKey(this).getPath());
	}
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, 36);
	}
	
	@Override
	public void registerControllers(ControllerRegistrar arg0) {
		
	}

	@Override
	protected GeoArmorRenderer<?> createRenderer() {
		return new RenderArmorBackpack();
	}

}
