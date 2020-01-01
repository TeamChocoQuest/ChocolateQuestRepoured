package com.teamcqr.chocolatequestrepoured.network;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ParticleMessageHandler implements IMessageHandler<ParticlesMessageToClient, IMessage>
{
	@Override
	public IMessage onMessage(ParticlesMessageToClient message, MessageContext ctx) 
	{
		if(!message.isMessageValid())
		{
			return null;
		}
		
	    if(ctx.side != Side.CLIENT) 
	    {
	      return null;
	    }
	    
	    Minecraft minecraft = Minecraft.getMinecraft();
	    final WorldClient worldClient = minecraft.world;
	    minecraft.addScheduledTask(new Runnable()
	    {
	      public void run() 
	      {
	        processMessage(worldClient, message);
	      }
	    });

	    return null;
	}
	
	private void processMessage(WorldClient worldClient, ParticlesMessageToClient message)
	{
	    Random random = new Random();
	    int NUMBER_OF_PARTICLES = message.getNumberOfParticles();
	    
	    for(int i = 0; i < NUMBER_OF_PARTICLES; ++i) 
	    {
	      Vec3d targetCoordinates = message.getTargetCoordinates();
	      double spawnXpos = targetCoordinates.x;
	      double spawnYpos = targetCoordinates.y + 1.0D + random.nextDouble();
	      double spawnZpos = targetCoordinates.z;
	      worldClient.spawnParticle(message.getParticleType(), spawnXpos, spawnYpos, spawnZpos, random.nextDouble() - 0.5D, -0.5D + random.nextDouble(), random.nextDouble() - 0.5D);
	    }
	    return;
	}
	
}