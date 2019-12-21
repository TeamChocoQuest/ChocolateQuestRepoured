package com.teamcqr.chocolatequestrepoured.objects.items.spears;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.ExtendedReachAttackPacket;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 20.12.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class ItemSpearBase extends ItemSword {

	private float reach;

	public ItemSpearBase(ToolMaterial material, float reach) {
		super(material);
		this.reach = reach;
	}

	public float getReach(){
		return reach;
	}

	@Mod.EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SideOnly(Side.CLIENT)
		@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
		public static void onEvent(MouseEvent event) {
			if (event.getButton() == 0 && event.isButtonstate())
			{
				Minecraft mc = Minecraft.getMinecraft();
				EntityPlayer clickingPlayer = mc.player;
				if (clickingPlayer != null)
				{
					ItemStack itemStack = clickingPlayer.getHeldItemMainhand();
					ItemSpearBase spear;

					if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemSpearBase)
					{
						spear = (ItemSpearBase)itemStack.getItem();
						float reach = spear.getReach();

						RayTraceResult result = getMouseOverExtended(reach);
						if (result != null && result.entityHit != null)
						{
							if (result.entityHit != clickingPlayer && result.entityHit.hurtResistantTime == 0)
							{
								CQRMain.NETWORK.sendToServer(new ExtendedReachAttackPacket(result.entityHit.getEntityId()));
							}
						}
					}
				}
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public static RayTraceResult getMouseOverExtended(float distance)
	{
		//Most of this is copied from EntityRenderer#getMouseOver()

		Entity pointedEntity = null;
		Minecraft mc = Minecraft.getMinecraft();
		Entity renderViewEntity = mc.getRenderViewEntity();

		if (renderViewEntity != null && mc.world != null)
		{
			double d0 = distance;
			double d1 = d0;
			RayTraceResult rtResult = renderViewEntity.rayTrace(d0, 0);
			Vec3d eyeVec = renderViewEntity.getPositionEyes(0);

			if (rtResult != null)
			{
				d1 = rtResult.hitVec.distanceTo(eyeVec);
			}

			Vec3d vec3d1 = renderViewEntity.getLook(1.0F);
			Vec3d vec3d2 = eyeVec.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);

			Vec3d vec3d3 = null;

			List<Entity> list = mc.world.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
			{
				public boolean apply(@Nullable Entity p_apply_1_)
				{
					return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
				}
			}));

			double d2 = d1;

			for (int j = 0; j < list.size(); ++j)
			{
				Entity entity1 = list.get(j);
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double)entity1.getCollisionBorderSize());
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(eyeVec, vec3d2);

				if (axisalignedbb.contains(eyeVec))
				{
					if (d2 >= 0.0D)
					{
						pointedEntity = entity1;
						vec3d3 = raytraceresult == null ? eyeVec : raytraceresult.hitVec;
						d2 = 0.0D;
					}
				}
				else if (raytraceresult != null)
				{
					double d3 = eyeVec.distanceTo(raytraceresult.hitVec);

					if (d3 < d2 || d2 == 0.0D)
					{
						if (entity1.getLowestRidingEntity() == renderViewEntity.getLowestRidingEntity() && !entity1.canRiderInteract())
						{
							if (d2 == 0.0D)
							{
								pointedEntity = entity1;
								vec3d3 = raytraceresult.hitVec;
							}
						}
						else
						{
							pointedEntity = entity1;
							vec3d3 = raytraceresult.hitVec;
							d2 = d3;
						}
					}
				}
			}

			if (pointedEntity == null || eyeVec.distanceTo(vec3d3) > distance)
			{
				return new RayTraceResult(RayTraceResult.Type.MISS, eyeVec, (EnumFacing)null, rtResult.getBlockPos());
			}
			else
			{
				return new RayTraceResult(pointedEntity, vec3d3);
			}

		}

		return null;

	}
	
}
