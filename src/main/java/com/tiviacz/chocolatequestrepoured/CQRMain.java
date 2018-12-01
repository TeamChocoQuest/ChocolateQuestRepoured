package com.tiviacz.chocolatequestrepoured;

import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.proxy.CommonProxy;
import com.tiviacz.chocolatequestrepoured.util.Reference;
import com.tiviacz.chocolatequestrepoured.util.handlers.RegistryHandler;

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
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class CQRMain
{
	public static CreativeTabs CQRTab = (new CreativeTabs("ChocolateQuestRepouredTab")
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ModItems.CLOUD_BOOTS);
		}
	});
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.PreInitRegistries(event);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		RegistryHandler.initRegistries(event);
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event)
	{
		RegistryHandler.PostInitRegistries(event);
	}
}