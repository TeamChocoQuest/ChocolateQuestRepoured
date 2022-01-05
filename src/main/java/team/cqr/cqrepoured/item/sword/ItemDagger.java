package team.cqr.cqrepoured.item.sword;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemDagger extends ItemCQRWeapon {

	private static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("3915fbe7-e8ed-4032-9b18-749c96a37173");
	private final double movementSpeedBonus;
	private final int specialAttackCooldown;

	public ItemDagger(Properties props, IItemTier material, int cooldown) {
		super(material, CQRConfig.materials.toolMaterials.daggerAttackDamageBonus, CQRConfig.materials.toolMaterials.daggerAttackSpeedBonus, props);
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
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
		Multimap<Attribute, AttributeModifier> multimap = super.getDefaultAttributeModifiers(slot);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Weapon modifier", this.movementSpeedBonus, Operation.ADDITION));
		}

		return multimap;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn.isOnGround() && !playerIn.isSwingInProgress) {
			EntityUtil.move2D(playerIn, playerIn.moveStrafing, playerIn.moveForward, 1.0D, playerIn.rotationYaw);

			playerIn.motionY = 0.2D;
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), this.specialAttackCooldown);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent(TextFormatting.BLUE + "200% " + I18n.get("description.rear_damage.name")));

		ItemLore.addHoverTextLogic(tooltip, flagIn, this.getRegistryName().getPath());
	}

}
