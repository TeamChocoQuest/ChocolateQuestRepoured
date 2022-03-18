package team.cqr.cqrepoured.item.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRArmorModels;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.util.ItemUtil;

//#TODO Needs tests
public class ItemArmorSpider extends ArmorItem {

	private AttributeModifier movementSpeed;

	public ItemArmorSpider(IArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties properties) {
		super(materialIn, slot, properties);

		this.movementSpeed = new AttributeModifier("SpiderArmorModifier", 0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if(slot == MobEntity.getEquipmentSlotForItem(stack))
		{
			multimap.put(Attributes.MOVEMENT_SPEED, this.movementSpeed);
		}
		return multimap;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		ItemLore.addHoverTextLogic(tooltip, flagIn, "spider_armor");
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
	{
		if(ItemUtil.hasFullSet(player, ItemArmorSpider.class))
		{
			if(player.isSpectator())
			{
				return;
			}
			if(player.horizontalCollision)
			{
				if(world.isClientSide)
				{
					if(player.zza > 0)
					{
						player.setDeltaMovement(player.getDeltaMovement().x, 0.2D, player.getDeltaMovement().z); //.motionY = 0.2D;
						this.createClimbingParticles(player, world);
					}
					else if(player.isCrouching())
					{
						player.setDeltaMovement(player.getDeltaMovement().x, 0.0D, player.getDeltaMovement().z);
						//player.motionY = 0.0D;
					}
					else
					{
						player.setDeltaMovement(player.getDeltaMovement().x, -0.2D, player.getDeltaMovement().z);

						//player.motionY = -0.2D;
					}
				}

				player.setOnGround(true); //should do
				//player.onGround = true;
			}
			player.fallDistance = 0F;
			player.flyingSpeed += 0.005;
			player.addEffect(new EffectInstance(Effects.JUMP, 0, 1, false, false));
		}
	}

	private void createClimbingParticles(PlayerEntity player, World world)
	{
		int i = (int) player.position().x;
		int j = MathHelper.floor(player.blockPosition().getY());
		int k = (int) player.position().z;

		int direction = MathHelper.floor((player.yRot * 4.0F / 360.0F) + 0.5D) & 3;

		if (direction == 0) // south
		{
			if (k > 0) {
				k += 1;
			}

			if (i < 0) {
				i -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), player.position().x + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getBoundingBox().minY + 0.1D,
							(player.position().z + 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							//-player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D,
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
				}
			}
		}

		if (direction == 1) // west
		{
			if (i > 0) {
				i -= 1;
			}

			if (k < 0) {
				k -= 1;
			}

			if (i < 0) {
				i -= 2;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate),
							(player.position().x - 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getBoundingBox().minY + 0.1D, player.position().z + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);
							//-player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D);
				}
			}
		}

		if (direction == 2) // north
		{
			if (i < 0) {
				i -= 1;
			}

			if (k > 0) {
				k -= 1;
			}

			if ((i > 0 && k < 0) || (i < 0 && k < 0)) {
				k -= 2;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), player.position().x + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getBoundingBox().minY + 0.1D,
							(player.position().z - 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);

									//-player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D);
				}
			}
		}

		if (direction == 3) // east
		{
			if (i > 0) {
				i += 1;
			}

			if (k < 0) {
				k -= 1;
			}

			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState iblockstate = world.getBlockState(blockpos);

			if (!iblockstate.getBlock().addRunningEffects(iblockstate, world, blockpos, player)) {
				if (iblockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
					world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), (player.position().x + 0.3) + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getBoundingBox().minY + 0.1D,
							player.position().z + (random.nextFloat() - 0.5D) * player.getBbWidth(),
							player.getDeltaMovement().multiply(-4.0D, 1.0D, 1.0D).x, 1.5D, player.getDeltaMovement().multiply(1.0D, 1.0D, -4.0D).z);

					//-player.motionX * 4.0D, 1.5D, -player.motionZ * 4.0D, );
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@Nullable
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel _default)
	{
		return armorSlot == EquipmentSlotType.LEGS ? CQRArmorModels.SPIDER_ARMOR_LEGS : CQRArmorModels.SPIDER_ARMOR;
	}

}
