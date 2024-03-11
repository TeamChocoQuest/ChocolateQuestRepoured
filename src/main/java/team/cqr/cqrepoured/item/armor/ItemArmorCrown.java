package team.cqr.cqrepoured.item.armor;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrown;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import team.cqr.cqrepoured.client.render.armor.RenderArmorKingCrown;
import team.cqr.cqrepoured.item.ItemLore;

public class ItemArmorCrown extends CQRGeoArmorBase implements GeoItem {

	public static final String NBT_KEY_CROWN = "CQR Crown";

	public ItemArmorCrown(ArmorMaterial materialIn, Properties props) {
		super(materialIn, Type.HELMET, props);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return CapabilityDynamicCrownProvider.createProvider();
	}

	@Nullable
	public Item getAttachedItem(ItemStack stack) {
		if (stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).isPresent()) {
			return stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).resolve().get().getAttachedItem();
		}
		return null;
	}

	public void attachItem(ItemStack crown, Item toAttach) {
		if (crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).isPresent()) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).resolve().get().attachItem(toAttach);
		}
	}

	public void attachItem(ItemStack crown, ItemStack toAttach) {
		if (crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).isPresent()) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).resolve().get().attachItem(toAttach);
		}
	}

	public void attachItem(ItemStack crown, ResourceLocation toAttach) {
		if (crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).isPresent()) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).resolve().get().attachItem(toAttach);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		LazyOptional<CapabilityDynamicCrown> lOpCap = stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null);
		if (lOpCap.isPresent()) {
			tooltip.add(Component.translatable("Attached helmet: " + new ItemStack(lOpCap.resolve().get().getAttachedItem()).getHoverName().getString()));
		}

		ItemLore.addHoverTextLogic(tooltip, flagIn, ForgeRegistries.ITEMS.getKey(this).getPath());
	}

	// TODO: Tooltip that shows the attachment
	// TODO: Stats are affected by attachment

	public static boolean hasCrown(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains(NBT_KEY_CROWN, Tag.TAG_COMPOUND);
	}

	@Override
	public void registerControllers(ControllerRegistrar arg0) {
		
	}

	@Override
	protected GeoArmorRenderer<?> createRenderer() {
		return new RenderArmorKingCrown();
	}

}
