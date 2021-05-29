package team.cqr.cqrepoured.integration.plustic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;

@EventBusSubscriber
public class AntiPortlyGentleman {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void blockPortlyRelocatorOnBosses(PlayerInteractEvent.EntityInteract event) {
		ItemStack tool = event.getItemStack();
		if(tool == null || tool.getItem() == null || tool.isEmpty() || !tool.hasTagCompound()) {
			return;
		}
		
		if(event.getEntityLiving() != null && event.getEntityLiving() instanceof AbstractEntityCQRBoss) {
			NBTTagCompound itemNBT = tool.getTagCompound();
			if(itemNBT.hasKey("Traits", Constants.NBT.TAG_STRING)) {
				NBTTagList tagList = itemNBT.getTagList("Traits", Constants.NBT.TAG_STRING);
				for(int i = 0; i < tagList.tagCount(); i++) {
					if(tagList.getStringTagAt(i).equals("portly")) {
						event.setCanceled(true);
						
						event.getEntityPlayer().sendMessage(new TextComponentString("Hmm... this doesn't seem to work for bosses..."));
						
						return;
					}
				}
			}
		}
		
	}
}
