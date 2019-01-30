package com.tiviacz.chocolatequestrepoured;

import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.proxy.CommonProxy;
import com.tiviacz.chocolatequestrepoured.util.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class CQRMain
{
	public static CreativeTabs CQRTab = new CreativeTabs("ChocolateQuestRepouredTab")
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ModItems.BOOTS_CLOUD);
		}
	};
	
	@Instance
	public static CQRMain INSTANCE;
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("cqrepoured");
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}