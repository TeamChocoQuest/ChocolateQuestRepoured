package team.cqr.cqrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.objects.items.ISupportWeapon;
import team.cqr.cqrepoured.objects.items.swords.ItemFakeSwordHealingStaff;

public class ItemStaffHealing extends Item implements ISupportWeapon<ItemFakeSwordHealingStaff> {

	public static final float HEAL_AMOUNT_ENTITIES = 4.0F;

	public ItemStaffHealing() {
		this.setMaxDamage(128);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!player.world.isRemote && entity instanceof EntityLivingBase && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
			((EntityLivingBase) entity).heal(HEAL_AMOUNT_ENTITIES);
			entity.setFire(0);
			((WorldServer) player.world).spawnParticle(EnumParticleTypes.HEART, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			player.world.playSound(null, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ, CQRSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + itemRand.nextFloat() * 0.2F);
			stack.damageItem(1, player);
			player.getCooldownTracker().setCooldown(stack.getItem(), 30);
		}
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.0D, 0));
		}

		return multimap;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_healing.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public ItemFakeSwordHealingStaff getFakeWeapon() {
		return CQRItems.DIAMOND_SWORD_FAKE_HEALING_STAFF;
	}

}
