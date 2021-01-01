package team.cqr.cqrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrown;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import team.cqr.cqrepoured.client.init.CQRArmorModels;

public class ItemCrown extends ItemArmor {

	public static final String NBT_KEY_CROWN = "CQR Crown";

	public ItemCrown(ArmorMaterial materialIn, int renderIndexIn) {
		super(materialIn, renderIndexIn, EntityEquipmentSlot.HEAD);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return CQRArmorModels.crown;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityDynamicCrownProvider.createProvider();
	}

	@Nullable
	public Item getAttachedItem(ItemStack stack) {
		if (stack.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			return stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).getAttachedItem();
		}
		return null;
	}

	public void attachItem(ItemStack crown, Item toAttach) {
		if (crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}

	public void attachItem(ItemStack crown, ItemStack toAttach) {
		if (crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}

	public void attachItem(ItemStack crown, ResourceLocation toAttach) {
		if (crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		CapabilityDynamicCrown capability = stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null);
		if (capability.getAttachedItem() != null) {
			tooltip.add("Attached helmet: " + new ItemStack(capability.getAttachedItem(), 1).getDisplayName());
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description." + this.getRegistryName().getPath() + ".name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	// TODO: Tooltip that shows the attachment
	// TODO: Stats are affected by attachment

	public static boolean hasCrown(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT_KEY_CROWN, Constants.NBT.TAG_COMPOUND);
	}

}
