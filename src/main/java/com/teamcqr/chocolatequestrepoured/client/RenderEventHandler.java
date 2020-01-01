package com.teamcqr.chocolatequestrepoured.client;

import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemMusket;
import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemRevolver;

import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT)
public class RenderEventHandler {

	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		Item itemMain = event.getEntityPlayer().getHeldItemMainhand().getItem();
		Item itemOff = event.getEntityPlayer().getHeldItemOffhand().getItem();
		//Musket
		if(itemMain instanceof ItemMusket) {
			if(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		}
		if(itemOff instanceof ItemMusket) {
			if(!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().leftArmPose = ArmPose.BOW_AND_ARROW;
			} else {
				event.getRenderer().getMainModel().rightArmPose = ArmPose.BOW_AND_ARROW;
			}
		}
		//Revolver
		if(itemMain instanceof ItemRevolver) {
			if(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90)); 
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
			}
		}
		if(itemOff instanceof ItemRevolver) {
			if(!(event.getEntityPlayer().getPrimaryHand() == EnumHandSide.LEFT)) {
				event.getRenderer().getMainModel().bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90)); 
			} else {
				event.getRenderer().getMainModel().bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
			}
		}
		//Great Sword
	}
	@SubscribeEvent
	public static void onRenderEntity(RenderLivingEvent.Post<EntityLivingBase> event) {
		
	}

}
