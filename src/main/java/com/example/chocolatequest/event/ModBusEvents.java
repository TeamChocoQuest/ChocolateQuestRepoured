package com.example.chocolatequest.event;

import com.example.chocolatequest.ChocolateQuestReDone;

import com.example.chocolatequest.registry.ModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void enqueueIMC(net.neoforged.fml.event.lifecycle.InterModEnqueueEvent event) {
        // Removed Curios API setup
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HEALING_SLIME.get(), com.example.chocolatequest.entity.HealingSlimeEntity.createAttributes().build());
        event.put(ModEntities.GIANT_SILVERFISH_NORMAL.get(), com.example.chocolatequest.entity.mount.EntityGiantSilverfishNormal.createAttributes().build());
        event.put(ModEntities.GIANT_SILVERFISH_RED.get(), com.example.chocolatequest.entity.mount.EntityGiantSilverfishRed.createAttributes().build());
        event.put(ModEntities.GIANT_SILVERFISH_GREEN.get(), com.example.chocolatequest.entity.mount.EntityGiantSilverfishGreen.createAttributes().build());
        event.put(ModEntities.CQ_ZOMBIE.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_SKELETON.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_SPECTER.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_ENDERMAN.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_PIRATE.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_GREMLIN.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.GREMLIN_SHAMAN.get(), com.example.chocolatequest.entity.boss.EntityCQRGremlinShaman.createAttributes().build());

        event.put(ModEntities.CQ_MONK.get(), com.example.chocolatequest.entity.npc.AbstractCQRNPC.createAttributes().build());
        event.put(ModEntities.CQ_MERCHANT.get(), com.example.chocolatequest.entity.npc.AbstractCQRNPC.createAttributes().build());
        event.put(ModEntities.CQ_PRISONER_KNIGHT.get(), com.example.chocolatequest.entity.npc.EntityCQRPrisonerKnight.createAttributes().build());

        event.put(ModEntities.CQ_BOARMAN.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_DUMMY.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_DWARF.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_GOBLIN.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_GOLEM.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_HUMAN.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_ILLAGER.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_MANDRIL.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_MINOTAUR.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_MUMMY.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_NPC.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_OGRE.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_ORC.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_TRITON.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.CQ_WALKER.get(), com.example.chocolatequest.entity.bases.AbstractEntityCQR.createCQRAttributes().build());
        event.put(ModEntities.SHELOB.get(), com.example.chocolatequest.entity.boss.ShelobEntity.createAttributes().build());
        event.put(ModEntities.SPIDER_MINION.get(), com.example.chocolatequest.entity.mob.SpiderMinionEntity.createAttributes().build());
        
        event.put(ModEntities.CQ_BULL.get(), com.example.chocolatequest.entity.boss.CQBullEntity.createAttributes().build());
        event.put(ModEntities.CQ_ICE_BULL.get(), com.example.chocolatequest.entity.boss.CQBullEntity.createAttributes().build());
        event.put(ModEntities.CQ_PIRATE_CAPTAIN.get(), com.example.chocolatequest.entity.boss.PirateCaptainEntity.createAttributes().build());
        event.put(ModEntities.CQ_PIRATE_PARROT.get(), com.example.chocolatequest.entity.boss.PirateParrotEntity.createAttributes().build());
        event.put(ModEntities.CQR_DRAGON.get(), com.example.chocolatequest.entity.boss.EntityCQRDragon.createAttributes().build());
        event.put(ModEntities.EXTERMINATOR.get(), com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator.createAttributes().build());
        event.put(ModEntities.LICH.get(), com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.createAttributes().build());
        event.put(ModEntities.NECROMANCER.get(), com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.createAttributes().build());
        event.put(ModEntities.BOARMAGE.get(), com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase.createAttributes().build());

        event.put(ModEntities.GIANT_TORTOISE.get(), com.example.chocolatequest.entity.boss.EntityCQRGiantTortoise.createAttributes().build());
        event.put(ModEntities.ENDERMENACE.get(), com.example.chocolatequest.entity.boss.EntityCQREndermenace.createAttributes().build());
        event.put(ModEntities.SHULKER_GOLEM.get(), com.example.chocolatequest.entity.boss.EntityCQRShulkerGolem.createAttributes().build());
        event.put(ModEntities.MONKING.get(), com.example.chocolatequest.entity.boss.EntityCQRMonking.createAttributes().build());
        event.put(ModEntities.WALKER_KING.get(), com.example.chocolatequest.entity.boss.EntityCQRWalkerKing.createAttributes().build());
        event.put(ModEntities.SPECTER_LORD.get(), com.example.chocolatequest.entity.boss.EntityCQRSpecterLord.createCQRAttributes().build());
    }

    @SubscribeEvent
    public static void registerRenderers(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HEALING_SLIME.get(), net.minecraft.client.renderer.entity.SlimeRenderer::new);
        event.registerEntityRenderer(ModEntities.SHELOB.get(), com.example.chocolatequest.client.render.RenderShelob::new);
        event.registerEntityRenderer(ModEntities.SPIDER_MINION.get(), net.minecraft.client.renderer.entity.CaveSpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.WEB_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDER_CALAMITY_ORB.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context, 1.8F, true));
        event.registerEntityRenderer(ModEntities.ENDER_BLOCK_PROJECTILE.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context, 2.0F, true));
        event.registerEntityRenderer(ModEntities.EXTERMINATOR.get(), com.example.chocolatequest.client.render.boss.CQExterminatorRenderer::new);
        event.registerEntityRenderer(ModEntities.EXTERMINATOR_HAND_LASER.get(), com.example.chocolatequest.client.render.entity.RenderLaser::new);
        event.registerEntityRenderer(ModEntities.ENDERMENACE_LASER.get(), com.example.chocolatequest.client.render.entity.RenderLaser::new);
        event.registerEntityRenderer(ModEntities.SPECTER_LORD.get(), com.example.chocolatequest.entity.client.CQRSpecterLordRenderer::new);
    }
}
