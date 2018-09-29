package com.tiviacz.chocolatequestrepoured;

import com.tiviacz.chocolatequestrepoured.network.PacketRequestUpdateTable;
import com.tiviacz.chocolatequestrepoured.network.PacketUpdateTable;
import com.tiviacz.chocolatequestrepoured.proxy.CommonProxy;
import com.tiviacz.chocolatequestrepoured.tab.ChocolateQuestRepouredTab;
import com.tiviacz.chocolatequestrepoured.util.Reference;
import com.tiviacz.chocolatequestrepoured.util.handlers.RegistryHandler;

import net.minecraft.creativetab.CreativeTabs;
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
public class ChocolateQuestRepouredMain 
{

	public static CreativeTabs SpecialTab = new ChocolateQuestRepouredTab();
	
	public static SimpleNetworkWrapper network;
	
	@Instance
	public static ChocolateQuestRepouredMain instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.PreInitRegistries(event);
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
		network.registerMessage(new PacketUpdateTable.Handler(), PacketUpdateTable.class, 0, Side.CLIENT);
		network.registerMessage(new PacketRequestUpdateTable.Handler(), PacketRequestUpdateTable.class, 1, Side.SERVER);
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
