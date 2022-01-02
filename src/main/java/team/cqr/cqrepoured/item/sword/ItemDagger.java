package team.cqr.cqrepoured.item.sword;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemDagger extends ItemCQRWeapon {

	private static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("3915fbe7-e8ed-4032-9b18-749c96a37173");
	private final double movementSpeedBonus;
	private final int specialAttackCooldown;

	public ItemDagger(ToolMaterial material, int cooldown) {
		super(material, CQRConfig.materials.toolMaterials.daggerAttackDamageBonus, CQRConfig.materials.toolMaterials.daggerAttackSpeedBonus);
		this.movementSpeedBonus = CQRConfig.materials.toolMaterials.daggerMovementSpeedBonus;
		this.specialAttackCooldown = cooldown;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		boolean flag = ItemUtil.compareRotations(player.rotationYaw, entity.rotationYaw, 50.0D);
		ItemUtil.attackTarget(stack, player, entity, flag, 0.0F, flag ? 2.0F : 1.0F, true, 1.0F, 0.0F, 0.25D, 0.25D, 0.3F);
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Weapon modifier", this.movementSpeedBonus, 2));
		}

		return multimap;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn.onGround && !playerIn.isSwingInProgress) {
			EntityUtil.move2D(playerIn, playerIn.moveStrafing, playerIn.moveForward, 1.0D, playerIn.rotationYaw);

			playerIn.motionY = 0.2D;
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), this.specialAttackCooldown);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return super.hitEntity(stack, target, attacker);
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.BLUE + "200% " + I18n.format("description.rear_damage.name"));

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.dagger.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
