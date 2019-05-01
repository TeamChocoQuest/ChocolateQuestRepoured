package com.teamcqr.chocolatequestrepoured.proxy;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBullet;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileBulletCannon;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.client.render.projectile.RenderProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.client.render.tesr.TileEntityTableRenderer;
import com.teamcqr.chocolatequestrepoured.network.ParticleMessageHandler;
import com.teamcqr.chocolatequestrepoured.network.ParticlesMessageToClient;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBullet;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBulletCannon;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileSpiderBall;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.handlers.GuiHandler;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		registerGUIs();
		registerRenderers();
		CQRMain.NETWORK.registerMessage(ParticleMessageHandler.class, ParticlesMessageToClient.class, Reference.TARGET_EFFECT_MESSAGE_ID, Side.CLIENT);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	public void registerRenderers() 
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());
		RenderingRegistry.registerEntityRenderingHandler(ProjectileEarthQuake.class, new IRenderFactory<ProjectileEarthQuake>() 
		{
			@Override
			public Render<ProjectileEarthQuake> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectileEarthQuake(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(ProjectileBullet.class, new IRenderFactory<ProjectileBullet>() 
		{
			@Override
			public Render<ProjectileBullet> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectileBullet(manager);
			}
		});
	
		RenderingRegistry.registerEntityRenderingHandler(ProjectileSpiderBall.class, new IRenderFactory<ProjectileSpiderBall>() 
		{
			@Override
			public Render<ProjectileSpiderBall> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectileSpiderBall(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(ProjectileBulletCannon.class, new IRenderFactory<ProjectileBulletCannon>() 
		{
			@Override
			public Render<ProjectileBulletCannon> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectileBulletCannon(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(ProjectileVampiricSpell.class, new IRenderFactory<ProjectileVampiricSpell>() 
		{
			@Override
			public Render<ProjectileVampiricSpell> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectileVampiricSpell(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(ProjectilePoisonSpell.class, new IRenderFactory<ProjectilePoisonSpell>() 
		{
			@Override
			public Render<ProjectilePoisonSpell> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectilePoisonSpell(manager);
			}
		});
	}
	
	private void registerGUIs() 
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
	}
}