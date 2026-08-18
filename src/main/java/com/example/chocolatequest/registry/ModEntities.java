package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ChocolateQuestReDone.MODID);
    public static final net.neoforged.neoforge.registries.DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRSpecterLord>> SPECTER_LORD =
            ENTITIES.register("specter_lord", () -> EntityType.Builder.of(com.example.chocolatequest.entity.boss.EntityCQRSpecterLord::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("specter_lord"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.CQRPotionEntity>> CQR_POTION = ENTITIES.register("cqr_potion",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.CQRPotionEntity>of(com.example.chocolatequest.entity.projectile.CQRPotionEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("cqr_potion"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.ProjectileBulletEntity>> PROJECTILE_BULLET = ENTITIES.register("projectile_bullet",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.ProjectileBulletEntity>of(com.example.chocolatequest.entity.projectile.ProjectileBulletEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("projectile_bullet"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.DarkProjectileEntity>> DARK_PROJECTILE = ENTITIES.register("dark_projectile",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.DarkProjectileEntity>of(com.example.chocolatequest.entity.projectile.DarkProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("dark_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.EnderCalamityOrbEntity>> ENDER_CALAMITY_ORB = ENTITIES.register("ender_calamity_orb",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.EnderCalamityOrbEntity>of(com.example.chocolatequest.entity.projectile.EnderCalamityOrbEntity::new, MobCategory.MISC)
                    .sized(0.65f, 0.65f)
                    .clientTrackingRange(12)
                    .updateInterval(1)
                    .build("ender_calamity_orb"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.EnderBlockProjectileEntity>> ENDER_BLOCK_PROJECTILE = ENTITIES.register("ender_block_projectile",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.EnderBlockProjectileEntity>of(com.example.chocolatequest.entity.projectile.EnderBlockProjectileEntity::new, MobCategory.MISC)
                    .sized(0.9f, 0.9f)
                    .clientTrackingRange(12)
                    .updateInterval(1)
                    .build("ender_block_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.PoisonProjectileEntity>> POISON_PROJECTILE = ENTITIES.register("poison_projectile",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.PoisonProjectileEntity>of(com.example.chocolatequest.entity.projectile.PoisonProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("poison_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.WindProjectileEntity>> WIND_PROJECTILE = ENTITIES.register("wind_projectile",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.WindProjectileEntity>of(com.example.chocolatequest.entity.projectile.WindProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("wind_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.FlyingHeartEntity>> FLYING_HEART = ENTITIES.register("flying_heart",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.FlyingHeartEntity>of(com.example.chocolatequest.entity.projectile.FlyingHeartEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("flying_heart"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.BubbleProjectileEntity>> BUBBLE_PROJECTILE = ENTITIES.register("bubble_projectile",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.BubbleProjectileEntity>of(com.example.chocolatequest.entity.projectile.BubbleProjectileEntity::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("bubble_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.BubbleTrapEntity>> BUBBLE_TRAP = ENTITIES.register("bubble_trap",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.BubbleTrapEntity>of(com.example.chocolatequest.entity.projectile.BubbleTrapEntity::new, MobCategory.MISC)
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("bubble_trap"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.HealingSlimeEntity>> HEALING_SLIME = ENTITIES.register("healing_slime",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.HealingSlimeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f) // Size 1 slime
                    .build("healing_slime"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQZombieEntity>> CQ_ZOMBIE = ENTITIES.register("cq_zombie",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQZombieEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_zombie"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQSkeletonEntity>> CQ_SKELETON = ENTITIES.register("cq_skeleton",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQSkeletonEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.99f)
                    .build("cq_skeleton"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQSpecterEntity>> CQ_SPECTER = ENTITIES.register("cq_specter",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQSpecterEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_specter"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQEndermanEntity>> CQ_ENDERMAN = ENTITIES.register("cq_enderman",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQEndermanEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.9f)
                    .build("cq_enderman"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQPirateEntity>> CQ_PIRATE = ENTITIES.register("cq_pirate",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQPirateEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_pirate"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQGremlinEntity>> CQ_GREMLIN = ENTITIES.register("cq_gremlin",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQGremlinEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.5f)
                    .build("cq_gremlin"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRGremlinShaman>> GREMLIN_SHAMAN = ENTITIES.register("cq_gremlin_shaman",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.boss.EntityCQRGremlinShaman::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.5f)
                    .build("cq_gremlin_shaman"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.ProjectileHookShotHook>> PROJECTILE_HOOKSHOT = ENTITIES.register("projectile_hookshot",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.ProjectileHookShotHook>of((type, level) -> new com.example.chocolatequest.entity.projectile.ProjectileHookShotHook(type, level), MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("projectile_hookshot"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.ProjectileSpiderHook>> PROJECTILE_SPIDER_HOOK = ENTITIES.register("projectile_spider_hook",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.ProjectileSpiderHook>of((type, level) -> new com.example.chocolatequest.entity.projectile.ProjectileSpiderHook(type, level), MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("projectile_spider_hook"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQBoarmanEntity>> CQ_BOARMAN = ENTITIES.register("cq_boarman",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQBoarmanEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_boarman"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQDummyEntity>> CQ_DUMMY = ENTITIES.register("cq_dummy",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQDummyEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_dummy"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQDwarfEntity>> CQ_DWARF = ENTITIES.register("cq_dwarf",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQDwarfEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_dwarf"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQGoblinEntity>> CQ_GOBLIN = ENTITIES.register("cq_goblin",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQGoblinEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_goblin"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQGolemEntity>> CQ_GOLEM = ENTITIES.register("cq_golem",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQGolemEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_golem"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQHumanEntity>> CQ_HUMAN = ENTITIES.register("cq_human",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQHumanEntity::new, net.minecraft.world.entity.MobCategory.CREATURE)
                    .sized(0.6f, 1.95f)
                    .build("cq_human"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQIllagerEntity>> CQ_ILLAGER = ENTITIES.register("cq_illager",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQIllagerEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_illager"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQMandrilEntity>> CQ_MANDRIL = ENTITIES.register("cq_mandril",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQMandrilEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_mandril"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQMinotaurEntity>> CQ_MINOTAUR = ENTITIES.register("cq_minotaur",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQMinotaurEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_minotaur"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQMummyEntity>> CQ_MUMMY = ENTITIES.register("cq_mummy",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQMummyEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_mummy"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQNPCEntity>> CQ_NPC = ENTITIES.register("cq_npc",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQNPCEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_npc"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQOgreEntity>> CQ_OGRE = ENTITIES.register("cq_ogre",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQOgreEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_ogre"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQOrcEntity>> CQ_ORC = ENTITIES.register("cq_orc",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQOrcEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_orc"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQTritonEntity>> CQ_TRITON = ENTITIES.register("cq_triton",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQTritonEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_triton"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.CQWalkerEntity>> CQ_WALKER = ENTITIES.register("cq_walker",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mob.CQWalkerEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .build("cq_walker"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.WebProjectileEntity>> WEB_PROJECTILE = ENTITIES.register("web_projectile",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.WebProjectileEntity>of(com.example.chocolatequest.entity.projectile.WebProjectileEntity::new, net.minecraft.world.entity.MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("web_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mob.SpiderMinionEntity>> SPIDER_MINION = ENTITIES.register("spider_minion",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.mob.SpiderMinionEntity>of(com.example.chocolatequest.entity.mob.SpiderMinionEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.7F, 0.5F)
                    .build("spider_minion"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.ShelobEntity>> SHELOB = ENTITIES.register("shelob",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.ShelobEntity>of(com.example.chocolatequest.entity.boss.ShelobEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(2.4F, 1.4F)
                    .build("shelob"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.CQBullEntity>> CQ_BULL = ENTITIES.register("cq_bull",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.CQBullEntity>of(com.example.chocolatequest.entity.boss.CQBullEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.6F, 1.7F)
                    .build("cq_bull"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.CQIceBullEntity>> CQ_ICE_BULL = ENTITIES.register("cq_ice_bull",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.CQIceBullEntity>of(com.example.chocolatequest.entity.boss.CQIceBullEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.6F, 1.7F)
                    .build("cq_ice_bull"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.PirateCaptainEntity>> CQ_PIRATE_CAPTAIN = ENTITIES.register("cq_pirate_captain",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.PirateCaptainEntity>of(com.example.chocolatequest.entity.boss.PirateCaptainEntity::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build("cq_pirate_captain"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.PirateParrotEntity>> CQ_PIRATE_PARROT = ENTITIES.register("cq_pirate_parrot",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.PirateParrotEntity>of(com.example.chocolatequest.entity.boss.PirateParrotEntity::new, net.minecraft.world.entity.MobCategory.CREATURE)
                    .sized(0.5F, 0.9F)
                    .build("cq_pirate_parrot"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator>> EXTERMINATOR = ENTITIES.register("exterminator",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator>of(com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.4F, 2.9F)
                    .clientTrackingRange(10)
                    .build("exterminator"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.exterminator.EntityExterminatorHandLaser>> EXTERMINATOR_HAND_LASER = ENTITIES.register("exterminator_hand_laser",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.exterminator.EntityExterminatorHandLaser>of(com.example.chocolatequest.entity.boss.exterminator.EntityExterminatorHandLaser::new, net.minecraft.world.entity.MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(10)
                    .build("exterminator_hand_laser"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EndermenaceLaserEntity>> ENDERMENACE_LASER = ENTITIES.register("endermenace_laser",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EndermenaceLaserEntity>of(com.example.chocolatequest.entity.boss.EndermenaceLaserEntity::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .noSave()
                    .build("endermenace_laser"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.projectile.ProjectileCannonBall>> PROJECTILE_CANNON_BALL = ENTITIES.register("projectile_cannon_ball",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.projectile.ProjectileCannonBall>of(com.example.chocolatequest.entity.projectile.ProjectileCannonBall::new, net.minecraft.world.entity.MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("projectile_cannon_ball"));

    
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRLich>> LICH = ENTITIES.register("lich",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRLich>of(com.example.chocolatequest.entity.boss.EntityCQRLich::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .build("lich"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRNecromancer>> NECROMANCER = ENTITIES.register("necromancer",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRNecromancer>of(com.example.chocolatequest.entity.boss.EntityCQRNecromancer::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build("necromancer"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRBoarmage>> BOARMAGE = ENTITIES.register("boarmage",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRBoarmage>of(com.example.chocolatequest.entity.boss.EntityCQRBoarmage::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build("boarmage"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRMonking>> MONKING = ENTITIES.register("monking",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRMonking>of(com.example.chocolatequest.entity.boss.EntityCQRMonking::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.5F, 4.875F)
                    .clientTrackingRange(10)
                    .build("monking"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRWalkerKing>> WALKER_KING = ENTITIES.register("walker_king",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRWalkerKing>of(com.example.chocolatequest.entity.boss.EntityCQRWalkerKing::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .build("walker_king"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRGiantTortoise>> GIANT_TORTOISE = ENTITIES.register("giant_tortoise",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRGiantTortoise>of(com.example.chocolatequest.entity.boss.EntityCQRGiantTortoise::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(3.0F, 2.0F)
                    .clientTrackingRange(10)
                    .build("giant_tortoise"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQREndermenace>> ENDERMENACE = ENTITIES.register("endermenace",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQREndermenace>of(com.example.chocolatequest.entity.boss.EntityCQREndermenace::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.65F, 5.5F)
                    .clientTrackingRange(10)
                    .build("endermenace"));
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRShulkerGolem>> SHULKER_GOLEM = ENTITIES.register("shulker_golem",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.boss.EntityCQRShulkerGolem>of(com.example.chocolatequest.entity.boss.EntityCQRShulkerGolem::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(2.2F, 4.5F)
                    .clientTrackingRange(12)
                    .build("shulker_golem"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.boss.EntityCQRDragon>> CQR_DRAGON = ENTITIES.register("cqr_dragon",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.boss.EntityCQRDragon::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(3.0F, 3.0F)
                    .clientTrackingRange(16)
                    .build("cqr_dragon"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mount.EntityGiantSilverfishNormal>> GIANT_SILVERFISH_NORMAL = ENTITIES.register("giant_silverfish_normal",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mount.EntityGiantSilverfishNormal::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.4f, 0.8f)
                    .build("giant_silverfish_normal"));
                    
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mount.EntityGiantSilverfishRed>> GIANT_SILVERFISH_RED = ENTITIES.register("giant_silverfish_red",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mount.EntityGiantSilverfishRed::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.4f, 0.8f)
                    .build("giant_silverfish_red"));
                    
    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.mount.EntityGiantSilverfishGreen>> GIANT_SILVERFISH_GREEN = ENTITIES.register("giant_silverfish_green",
            () -> EntityType.Builder.of(com.example.chocolatequest.entity.mount.EntityGiantSilverfishGreen::new, net.minecraft.world.entity.MobCategory.MONSTER)
                    .sized(1.4f, 0.8f)
                    .build("giant_silverfish_green"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.npc.EntityCQRMonk>> CQ_MONK = ENTITIES.register("cq_monk",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.npc.EntityCQRMonk>of(com.example.chocolatequest.entity.npc.EntityCQRMonk::new, net.minecraft.world.entity.MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .build("cq_monk"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.npc.EntityCQRMerchant>> CQ_MERCHANT = ENTITIES.register("cq_merchant",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.npc.EntityCQRMerchant>of(com.example.chocolatequest.entity.npc.EntityCQRMerchant::new, net.minecraft.world.entity.MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .build("cq_merchant"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.example.chocolatequest.entity.npc.EntityCQRPrisonerKnight>> CQ_PRISONER_KNIGHT = ENTITIES.register("cq_prisoner_knight",
            () -> EntityType.Builder.<com.example.chocolatequest.entity.npc.EntityCQRPrisonerKnight>of(com.example.chocolatequest.entity.npc.EntityCQRPrisonerKnight::new, net.minecraft.world.entity.MobCategory.CREATURE)
                    .sized(0.6F, 1.95F)
                    .build("cq_prisoner_knight"));

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
