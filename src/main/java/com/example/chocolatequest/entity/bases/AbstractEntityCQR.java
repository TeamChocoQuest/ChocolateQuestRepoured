package com.example.chocolatequest.entity.bases;

import com.example.chocolatequest.faction.EDefaultFaction;
import com.example.chocolatequest.faction.IFactionRelated;
import com.example.chocolatequest.entity.mob.enums.MobTier;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import com.example.chocolatequest.entity.ai.goal.HealAlliesGoalCQR;
import com.example.chocolatequest.entity.ai.goal.MeleeAttackGoalCQR;
import com.example.chocolatequest.entity.ai.goal.RangedAttackGoalCQR;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractEntityCQR extends PathfinderMob implements GeoEntity, IFactionRelated, RangedAttackMob, Merchant {

    // Synched data
    protected static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> CHAT_BUBBLE_INDEX = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.INT);
    private int chatBubbleTimer = 0;
    protected static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> TEXTURE_INDEX = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<String> MOB_TIER = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<Boolean> HAS_USED_POTION = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_DRINKING_POTION = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> HEALTH_SCALE = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> SIZE_SCALE = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> TAVERN_NPC = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);

    // Leader tracking
    private LivingEntity leader;
    private java.util.UUID leaderUUID;
    
    // Weapon swapping
    private ItemStack previousWeapon = ItemStack.EMPTY;

    // Extra Inventory (Potion, Badge, Arrow)
    public final net.minecraft.world.SimpleContainer extraInventory = new net.minecraft.world.SimpleContainer(3);

    private static final String CUSTOM_TRADES_TAG = "TavernCustomTrades";
    private final java.util.List<TavernCustomTrade> tavernCustomTrades = new java.util.ArrayList<>();
    private MerchantOffers tavernOffers = new MerchantOffers();
    @Nullable
    private Player tradingPlayer;
    private int tavernMerchantXp;

    // GeckoLib
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected int lastTickWithAttackTarget = Integer.MIN_VALUE;
    protected int lastTimeSeenAttackTarget = Integer.MIN_VALUE;
    protected Vec3 lastPosAttackTarget = Vec3.ZERO;
    protected int lastTickShieldDisabled = Integer.MIN_VALUE;
    protected float damageBlockedWithShield;

    protected BlockPos homePosition;
    private final java.util.List<BlockPos> patrolPath = new java.util.ArrayList<>();
    private int patrolPathIndex;

    // Forced configuration from spawn egg
    public String forcedWeapon = "";

    // Animations
    public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.biped.idle");
    public static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.biped.legs.walk");
    public static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("animation.biped.sit");
    public static final RawAnimation SNEAK_ANIM = RawAnimation.begin().thenLoop("animation.biped.body.sneak");
    
    public static final RawAnimation FIREARM_RIGHT = RawAnimation.begin().thenLoop("animation.biped.arms.right.firearm");
    public static final RawAnimation FIREARM_LEFT = RawAnimation.begin().thenLoop("animation.biped.arms.left.firearm");
    public static final RawAnimation FIREARM_SMALL_RIGHT = RawAnimation.begin().thenLoop("animation.biped.arms.right.firearm-small");
    public static final RawAnimation FIREARM_SMALL_LEFT = RawAnimation.begin().thenLoop("animation.biped.arms.left.firearm-small");

    public static final RawAnimation GREATSWORD_POSE = RawAnimation.begin().thenLoop("animation.biped.arms.greatsword");
    public static final RawAnimation GREATSWORD_ATTACK = RawAnimation.begin().thenPlay("animation.biped.arms.attack-greatsword");
    
    public static final RawAnimation SPEAR_POSE_RIGHT = RawAnimation.begin().thenLoop("animation.biped.arms.right.spear");
    public static final RawAnimation SPEAR_POSE_LEFT = RawAnimation.begin().thenLoop("animation.biped.arms.left.spear");
    public static final RawAnimation SPEAR_ATTACK = RawAnimation.begin().thenPlay("animation.biped.arms.attack-spear");

    protected AbstractEntityCQR(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.xpReward = 5;
    }

    public static AttributeSupplier.Builder createCQRAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_SPEED, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    
    public abstract double getBaseHealth();

    
    private EDefaultFaction customFaction = null;

    public void setFaction(EDefaultFaction faction) {
        this.customFaction = faction;
    }

    public EDefaultFaction getFaction() {
        if (this.customFaction != null) {
            return this.customFaction;
        }
        return this.getDefaultFaction();
    }

    @Override
    public abstract EDefaultFaction getDefaultFaction();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_SITTING, false);
        builder.define(CHAT_BUBBLE_INDEX, -1);
        builder.define(HAS_TARGET, false);
        builder.define(TEXTURE_INDEX, this.getTextureVariant());
        builder.define(MOB_TIER, MobTier.DEFAULT.getName());
        builder.define(HAS_USED_POTION, false);
        builder.define(IS_DRINKING_POTION, false);
        builder.define(HEALTH_SCALE, 1.0f);
        builder.define(SIZE_SCALE, 1.0f);
        builder.define(TAVERN_NPC, false);
    }

    
    public int getTextureCount() {
        return 1;
    }

    /**
     * Select a random texture variant index on spawn.
     */
    protected int getTextureVariant() {
        return this.getTextureCount() > 1 ? this.random.nextInt(this.getTextureCount()) : 0;
    }

    public int getTextureIndex() {
        return this.entityData.get(TEXTURE_INDEX);
    }

    public int getChatBubbleIndex() {
        return this.entityData.get(CHAT_BUBBLE_INDEX);
    }
    public boolean isSitting() {
        return this.entityData.get(IS_SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(IS_SITTING, sitting);
    }

    public MobTier getMobTier() {
        return MobTier.fromName(this.entityData.get(MOB_TIER));
    }

    public void setMobTier(MobTier tier) {
        this.entityData.set(MOB_TIER, tier.getName());
    }

    public boolean hasUsedPotion() {
        return this.entityData.get(HAS_USED_POTION);
    }

    public void setHasUsedPotion(boolean used) {
        this.entityData.set(HAS_USED_POTION, used);
    }

    public boolean isDrinkingPotion() {
        return this.entityData.get(IS_DRINKING_POTION);
    }

    public void setDrinkingPotion(boolean drinking) {
        this.entityData.set(IS_DRINKING_POTION, drinking);
    }

    public float getHealthScale() {
        return this.entityData.get(HEALTH_SCALE);
    }

    public void setHealthScale(float scale) {
        this.entityData.set(HEALTH_SCALE, scale);
    }

    public float getSizeVariation() {
        return this.entityData.get(SIZE_SCALE);
    }

    public void setSizeVariation(float scale) {
        float clamped = net.minecraft.util.Mth.clamp(scale, 0.25F, 3.0F);
        this.entityData.set(SIZE_SCALE, clamped);
        this.refreshDimensions();
    }

    @Override
    public float getScale() {
        // LivingEntity 1.21.1 uses getScale() for both its dimensions and the
        // by commands or another mod while applying the editor value once.
        return super.getScale() * this.getSizeVariation();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIZE_SCALE.equals(key)) {
            this.refreshDimensions();
        }
    }

    public float getEditorDropChance(EquipmentSlot slot) {
        return this.getEquipmentDropChance(slot);
    }

    public boolean isTavernNpc() {
        if (this.entityData.get(TAVERN_NPC)) return true;
        if (!this.hasCustomName()) return false;
        String name = this.getCustomName().getString().trim().toLowerCase(java.util.Locale.ROOT);
        return name.equals("adventurer") || name.equals("mandril vendor")
                || name.equals("the gunsmith") || name.equals("bartender")
                || name.equals("inquisition soldier") || name.equals("lard")
                || name.equals("guard");
    }

    public void setTavernNpc(boolean tavernNpc) {
        this.entityData.set(TAVERN_NPC, tavernNpc);
    }

    public EDefaultFaction getTavernReputationFaction() {
        return EDefaultFaction.NPC;
    }

    /** Tavern residents form a temporary civilian faction regardless of race. */
    public boolean isTavernAlly(Entity entity) {
        return this.isTavernNpc()
                && entity instanceof AbstractEntityCQR other
                && other.isTavernNpc();
    }

    public ItemStack getPreviousWeapon() {
        return this.previousWeapon;
    }

    @Override
    protected net.minecraft.world.InteractionResult mobInteract(Player player, net.minecraft.world.InteractionHand hand) {
        if (player.getItemInHand(hand).getItem() instanceof net.minecraft.world.item.NameTagItem) {
            return super.mobInteract(player, hand);
        }

        if (this.isTavernNpc()) {
            if (player.isCreative() && player.isCrouching()
                    && hand == net.minecraft.world.InteractionHand.MAIN_HAND) {
                if (!this.level().isClientSide()) {
                    ItemStack result = player.getMainHandItem();
                    if (result.isEmpty()) {
                        this.removeLastTavernTrade(player);
                    } else {
                        this.addTavernTrade(player, player.getOffhandItem(), result);
                    }
                }
                return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            if (!player.isCreative() && !player.isCrouching()) {
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer
                        && !this.level().isClientSide()) {
                    this.openTavernTradeScreen(serverPlayer);
                }
                return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }

        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer && !this.level().isClientSide()) {
            if (!player.isCrouching()) {
                if (player.isCreative() || this.getLeader() == player) {
                    serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
                        (id, inv, p) -> new com.example.chocolatequest.inventory.ContainerCQREntity(id, inv, this),
                        this.getDisplayName()
                    ), buf -> buf.writeInt(this.getId()));
                    return net.minecraft.world.InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    public void addTavernTradeFromHands(Player player) {
        ItemStack result = player.getMainHandItem();
        if (result.isEmpty()) {
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.cqrepoured.custom_trade_missing_result"), true);
            return;
        }
        this.addTavernTrade(player, player.getOffhandItem(), result);
    }

    private void addTavernTrade(Player player, ItemStack requestedCost, ItemStack offeredResult) {
        ItemStack cost = requestedCost.isEmpty() ? new ItemStack(Items.EMERALD) : requestedCost.copy();
        ItemStack result = offeredResult.copy();
        this.tavernCustomTrades.add(new TavernCustomTrade(cost, result));
        player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                "message.cqrepoured.custom_trade_added",
                cost.getCount(), cost.getHoverName(), result.getCount(), result.getHoverName()), true);
    }

    public void removeLastTavernTrade(Player player) {
        if (this.tavernCustomTrades.isEmpty()) {
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.cqrepoured.custom_trade_none"), true);
            return;
        }
        TavernCustomTrade removed = this.tavernCustomTrades.remove(this.tavernCustomTrades.size() - 1);
        player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                "message.cqrepoured.custom_trade_removed", removed.result().getHoverName()), true);
    }

    public void openTavernTradeScreen(net.minecraft.server.level.ServerPlayer player) {
        if (!this.isTavernNpc()) return;
        EDefaultFaction reputationFaction = this.getTavernReputationFaction();
        int reputation = player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION)
                .getReputation(reputationFaction);
        if (!player.isCreative() && reputation <= -50) {
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.cqrepoured.merchant_refuses"), true);
            this.level().playSound(null, this.blockPosition(), SoundEvents.VILLAGER_NO,
                    SoundSource.NEUTRAL, 1.0F, 0.9F);
            return;
        }

        this.prepareTavernOffers(reputation);
        this.setTradingPlayer(player);
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new com.example.chocolatequest.network.packet.SPacketUpdatePlayerReputation(
                        player.getUUID(), reputationFaction.name(), reputation));
        player.openMenu(new net.minecraft.world.SimpleMenuProvider(
                (id, inventory, menuPlayer) -> new com.example.chocolatequest.inventory.ContainerCQRMerchant(
                        id, inventory, this, reputationFaction.name(), "tavern"),
                this.getDisplayName()), buffer -> {
            buffer.writeUtf(reputationFaction.name());
            buffer.writeUtf("tavern");
        });
        player.sendMerchantOffers(player.containerMenu.containerId, this.tavernOffers,
                1, this.tavernMerchantXp, false, false);
    }

    private void prepareTavernOffers(int reputation) {
        MerchantOffers prepared = new MerchantOffers();

        String role = this.hasCustomName()
                ? this.getCustomName().getString().trim().toLowerCase(java.util.Locale.ROOT)
                : "";

        if (role.contains("gunsmith")) {
            addCraftTrade(prepared, Items.IRON_INGOT, 2, Items.GUNPOWDER, 2, ModItems.BULLET_IRON.get(), 16, 32);
            addCraftTrade(prepared, Items.GOLD_INGOT, 2, Items.GUNPOWDER, 2, ModItems.BULLET_GOLD.get(), 16, 24);
            addCraftTrade(prepared, Items.DIAMOND, 1, Items.GUNPOWDER, 2, ModItems.BULLET_DIAMOND.get(), 8, 16);
            addCraftTrade(prepared, Items.FIRE_CHARGE, 1, Items.GUNPOWDER, 2, ModItems.BULLET_FIRE.get(), 8, 16);
            addCraftTrade(prepared, Items.EMERALD, 6, Items.IRON_INGOT, 3, ModItems.MUSKET.get(), 1, 6);
            if (reputation >= 10) addCraftTrade(prepared, Items.EMERALD, 8, Items.IRON_INGOT, 4, ModItems.REVOLVER.get(), 1, 6);
            if (reputation >= 25) addCraftTrade(prepared, Items.EMERALD, 12, ModItems.IRON_DAGGER.get(), 1, ModItems.MUSKET_DAGGER_IRON.get(), 1, 4);
            if (reputation >= 40) addCraftTrade(prepared, Items.EMERALD, 16, ModItems.CANNON_BALL.get(), 4, ModItems.GUN_STAFF.get(), 1, 2);
            addBuy(prepared, Items.GUNPOWDER, 8, 1, 24);
            addBuy(prepared, Items.IRON_INGOT, 4, 1, 24);
        } else if (role.contains("bartender") || role.contains("lard")) {
            addBuy(prepared, Items.WHEAT, 16, 1, 24);
            addBuy(prepared, Items.SWEET_BERRIES, 16, 1, 24);
            addBuy(prepared, Items.BEEF, 8, 1, 24);
            addBuy(prepared, Items.PORKCHOP, 8, 1, 24);
            addSell(prepared, Items.BREAD, 8, 1, 32);
            addSell(prepared, Items.COOKED_BEEF, 5, 1, 24);
            addSell(prepared, Items.COOKED_PORKCHOP, 5, 1, 24);
            addSell(prepared, Items.GOLDEN_CARROT, 4, 2, 20);
            if (reputation >= 15) addSell(prepared, ModItems.MINI_HEALING_POTION.get(), 2, 3, 12);
            if (reputation >= 30) addSell(prepared, ModItems.HEAL_POTION.get(), 1, 6, 8);
            if (reputation >= 45) addSell(prepared, Items.GOLDEN_APPLE, 1, 8, 6);
        } else if (role.contains("mandril")) {
            addCraftTrade(prepared, Items.EMERALD, 6, Items.GOLD_INGOT, 2, ModItems.MONKING_DAGGER.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 8, Items.GOLD_INGOT, 4, ModItems.SHIELD_MONKING.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 14, Items.GOLD_INGOT, 6, ModItems.MONKING_GREAT_SWORD.get(), 1, 4);
            if (reputation >= 25) addCraftTrade(prepared, Items.EMERALD, 16, ModItems.MUSKET.get(), 1, ModItems.MUSKET_DAGGER_MONKING.get(), 1, 3);
            addBuy(prepared, Items.GOLD_INGOT, 4, 1, 16);
            addBuy(prepared, Items.APPLE, 12, 1, 16);
        } else if (role.contains("inquisition")) {
            addCraftTrade(prepared, Items.EMERALD, 6, Items.GOLD_INGOT, 5, ModItems.INQUISITION_HELMET.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 8, Items.GOLD_INGOT, 8, ModItems.INQUISITION_CHESTPLATE.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 7, Items.GOLD_INGOT, 7, ModItems.INQUISITION_LEGGINGS.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 5, Items.GOLD_INGOT, 4, ModItems.INQUISITION_BOOTS.get(), 1, 6);
            if (reputation >= 25) addCraftTrade(prepared, Items.EMERALD, 12, Items.SHIELD, 1, ModItems.SHIELD_SUN.get(), 1, 3);
            addBuy(prepared, Items.ROTTEN_FLESH, 24, 1, 16);
            addBuy(prepared, Items.GOLD_INGOT, 4, 1, 16);
        } else if (role.contains("adventurer")) {
            addCraftTrade(prepared, Items.EMERALD, 4, Items.STRING, 8, ModItems.HOOKSHOT.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 4, Items.LEATHER, 6, ModItems.BACKPACK.get(), 1, 8);
            addCraftTrade(prepared, Items.EMERALD, 3, Items.FEATHER, 4, ModItems.GOLDEN_FEATHER.get(), 1, 10);
            addCraftTrade(prepared, Items.EMERALD, 8, Items.FEATHER, 4, ModItems.CLOUD_BOOTS.get(), 1, 4);
            addCraftTrade(prepared, Items.EMERALD, 8, Items.ENDER_PEARL, 2, ModItems.TELEPORT_STONE.get(), 1, 4);
            if (reputation >= 20) addCraftTrade(prepared, Items.EMERALD, 6, Items.GOLD_INGOT, 1, ModItems.MAGIC_BELL.get(), 1, 4);
            addBuy(prepared, Items.ROTTEN_FLESH, 20, 1, 20);
            addBuy(prepared, Items.BONE, 16, 1, 20);
            addBuy(prepared, Items.STRING, 12, 1, 20);
            addBuy(prepared, Items.SPIDER_EYE, 8, 1, 20);
            addBuy(prepared, Items.ENDER_PEARL, 2, 1, 12);
        } else {
            // Guard & Blacksmith role: Armor upgrades & martial provisions
            addCraftTrade(prepared, Items.IRON_HELMET, 1, Items.SPIDER_EYE, 3, ModItems.SPIDER_HELMET.get(), 1, 8);
            addCraftTrade(prepared, Items.IRON_CHESTPLATE, 1, ModItems.BULL_HORN.get(), 2, ModItems.BULL_CHESTPLATE.get(), 1, 8);
            addCraftTrade(prepared, Items.DIAMOND_HELMET, 1, ModItems.SCALE_TURTLE.get(), 2, ModItems.TURTLE_HELMET.get(), 1, 6);
            addCraftTrade(prepared, Items.LEATHER_BOOTS, 1, ModItems.SLIME_BALL_CQR.get(), 4, ModItems.SLIME_BOOTS.get(), 1, 8);
            addCraftTrade(prepared, Items.IRON_CHESTPLATE, 1, ModItems.SPIDER_LEATHER.get(), 4, ModItems.SPIDER_CHESTPLATE.get(), 1, 6);
            addCraftTrade(prepared, Items.IRON_HELMET, 1, ModItems.BULL_HORN.get(), 2, ModItems.BULL_HELMET.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 4, Items.IRON_INGOT, 5, ModItems.HEAVY_IRON_HELMET.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 6, Items.IRON_INGOT, 8, ModItems.HEAVY_IRON_CHESTPLATE.get(), 1, 6);
            addCraftTrade(prepared, Items.EMERALD, 4, Items.IRON_INGOT, 3, ModItems.IRON_GREAT_SWORD.get(), 1, 6);
            if (reputation >= 20) addCraftTrade(prepared, Items.EMERALD, 8, Items.SHIELD, 1, ModItems.SHIELD_DRAGONSLAYER.get(), 1, 3);
            addBuy(prepared, Items.RAW_IRON, 5, 1, 24);
            addBuy(prepared, Items.COAL, 16, 1, 24);
            addBuy(prepared, ModItems.BULL_HORN.get(), 2, 2, 16);
            addBuy(prepared, ModItems.SPIDER_LEATHER.get(), 4, 2, 16);
            addBuy(prepared, ModItems.SCALE_TURTLE.get(), 2, 3, 16);
        }

        for (TavernCustomTrade customTrade : this.tavernCustomTrades) {
            prepared.add(new MerchantOffer(
                    new ItemCost(customTrade.cost().getItem(), Math.max(1, customTrade.cost().getCount())),
                    customTrade.result().copy(), 9999, 2, 0.0F));
        }

        int adjustment = reputation >= 20 ? -(reputation / 20)
                : reputation <= -20 ? (-reputation) / 20 : 0;
        if (adjustment != 0) {
            for (MerchantOffer offer : prepared) {
                if (offer.getBaseCostA().is(Items.EMERALD)) {
                    offer.setSpecialPriceDiff(adjustment);
                }
            }
        }
        this.tavernOffers = prepared;
    }

    private static void addCraftTrade(MerchantOffers offers, net.minecraft.world.level.ItemLike in1, int c1,
                                     net.minecraft.world.level.ItemLike in2, int c2,
                                     net.minecraft.world.level.ItemLike out, int outCount, int maxUses) {
        offers.add(new MerchantOffer(
                new ItemCost(in1.asItem(), c1),
                java.util.Optional.of(new ItemCost(in2.asItem(), c2)),
                new ItemStack(out, outCount),
                maxUses, 2, 0.05F));
    }

    private static void addSell(MerchantOffers offers, net.minecraft.world.level.ItemLike result,
                                int resultCount, int emeraldCost, int maxUses) {
        offers.add(new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost),
                new ItemStack(result, resultCount), maxUses, 2, 0.05F));
    }

    private static void addBuy(MerchantOffers offers, net.minecraft.world.level.ItemLike cost,
                               int costCount, int emeraldResult, int maxUses) {
        offers.add(new MerchantOffer(new ItemCost(cost.asItem(), costCount),
                new ItemStack(Items.EMERALD, emeraldResult), maxUses, 2, 0.05F));
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    @Nullable
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        return this.tavernOffers;
    }

    @Override
    public void overrideOffers(MerchantOffers offers) {
        this.tavernOffers = offers;
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        if (!this.level().isClientSide() && this.tradingPlayer instanceof net.minecraft.server.level.ServerPlayer player) {
            EDefaultFaction reputationFaction = this.getTavernReputationFaction();
            var reputation = player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION);
            reputation.addReputation(reputationFaction, 2);
            int value = reputation.getReputation(reputationFaction);
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                    new com.example.chocolatequest.network.packet.SPacketUpdatePlayerReputation(
                            player.getUUID(), reputationFaction.name(), value));
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.cqrepoured.reputation_gained", reputationFaction.name(), value), true);
        }
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {
    }

    @Override
    public int getVillagerXp() {
        return this.tavernMerchantXp;
    }

    @Override
    public void overrideXp(int xp) {
        this.tavernMerchantXp = xp;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    @Override
    public boolean canRestock() {
        return false;
    }

    @Override
    public boolean isClientSide() {
        return this.level().isClientSide();
    }

    public void setPreviousWeapon(ItemStack stack) {
        this.previousWeapon = stack;
    }
    
    public boolean canUsePotion() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new com.example.chocolatequest.entity.ai.goal.CQDrinkPotionGoal(this));
        this.goalSelector.addGoal(1, new HealAlliesGoalCQR(this));
        this.goalSelector.addGoal(2, new RangedAttackGoalCQR(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoalCQR(this));
        this.goalSelector.addGoal(4, new com.example.chocolatequest.entity.ai.EntityAIMoveToLeader(this));
        this.goalSelector.addGoal(5, new com.example.chocolatequest.entity.ai.goal.CQRPatrolPathGoal(this, 0.9D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new com.example.chocolatequest.entity.ai.attack.special.EntityAIAttackSpecial(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new com.example.chocolatequest.entity.ai.EntityAIIdleSit(this));

        this.targetSelector.addGoal(0, new CQRHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new com.example.chocolatequest.entity.ai.target.EntityAICQRNearestAttackTarget(this));
    }

    private boolean spinToWin = false;
    private int lastTimeHitByAxeWhileBlocking = 0;

    public boolean canUseSpinToWinAttack() {
        return true;
    }

    public void setSpinToWin(boolean spin) {
        this.spinToWin = spin;
    }

    public boolean isSpinningToWin() {
        return this.spinToWin;
    }

    public void tick() {

        if (!this.level().isClientSide() && this.isAlive()) {
            if (this.chatBubbleTimer > 0) {
                this.chatBubbleTimer--;
                if (this.chatBubbleTimer <= 0) {
                    this.entityData.set(CHAT_BUBBLE_INDEX, -1);
                }
            } else {
                if (!this.getClass().getName().contains(".boss.")) {
                    if (this.getTarget() == null && !this.isPassenger()) {
                        // random tick (avg every 20 seconds)
                        if (this.random.nextInt(400) == 0) {
                            this.entityData.set(CHAT_BUBBLE_INDEX, this.random.nextInt(29));
                            this.chatBubbleTimer = 60 + this.random.nextInt(60);
                        }
                    } else {
                        if (this.entityData.get(CHAT_BUBBLE_INDEX) != -1) {
                            this.entityData.set(CHAT_BUBBLE_INDEX, -1);
                            this.chatBubbleTimer = 0;
                        }
                    }
                }
            }
        }
        LivingEntity attackTarget = this.getTarget();
        if (attackTarget != null) {
            this.lastTickWithAttackTarget = this.tickCount;
            if (this.isInSightRange(attackTarget) && this.getSensing().hasLineOfSight(attackTarget)) {
                this.lastTimeSeenAttackTarget = this.tickCount;
            }
            if (this.lastTimeSeenAttackTarget + 100 >= this.tickCount) {
                this.lastPosAttackTarget = attackTarget.position();
            }
        }

        // Decay shield damage counter when not in combat
        if (this.lastTickWithAttackTarget + 60 < this.tickCount && this.damageBlockedWithShield > 0.0F) {
            this.damageBlockedWithShield = Math.max(this.damageBlockedWithShield - 0.02F, 0.0F);
        }

        super.tick();
    }

    @Override
    public void aiStep() {
        this.updateSwingTime(); // PathfinderMob doesn't call this like Monster does
        super.aiStep();
    }

    /**
     * Check if target is within follow range (sight range).
     */
    public boolean isInSightRange(Entity target) {
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        return this.distanceToSqr(target) <= followRange * followRange;
    }

    /**
     * Check if target is within melee attack reach.
     */
    public boolean isInAttackReach(Entity target) {
        double reach = this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + target.getBbWidth();
        return this.distanceToSqr(target) <= reach;
    }

    /**
     * Check if another entity is a faction ally.
     */
    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) return true;
        if (this.isTavernAlly(entity)) return true;
        if (entity instanceof IFactionRelated other) {
            return this.getFaction().isAlly(other.getFaction());
        }
        return false;
    }

    
    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isTavernNpc() && target instanceof Player || this.isTavernAlly(target)) {
            return false;
        }
        if (target instanceof IFactionRelated other) {
            if (this.getFaction().isAlly(other.getFaction())) {
                return false;
            }
        }
        return super.canAttack(target);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target != null && (this.isTavernNpc() && target instanceof Player
                || this.isTavernAlly(target)) ? null : target);
    }

    /**
     * Check if an entity is a faction enemy.
     */
    public boolean isFactionEnemy(Entity entity) {
        if (this.isTavernNpc() && entity instanceof Player || this.isTavernAlly(entity)) {
            return false;
        }
        if (entity instanceof IFactionRelated other) {
            return this.getFaction().isEnemy(other.getFaction());
        }
        // Non-faction entities: Players are enemies if faction says so
        if (entity instanceof Player) {
            return this.getFaction().isEnemy(EDefaultFaction.PLAYERS);
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.isTavernAlly(target)) return false;
        return super.doHurtTarget(target);
    }

    public boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.is(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR) && this.isBlocking()) {
            Vec3 vec3d = damageSourceIn.getSourcePosition();
            if (vec3d != null) {
                Vec3 lookAngle = this.getViewVector(1.0F);
                Vec3 toAttacker = vec3d.subtract(this.position()).normalize();
                toAttacker = new Vec3(toAttacker.x, 0.0D, toAttacker.z);
                if (toAttacker.dot(lookAngle) < 0.0D) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof AbstractEntityCQR attacker
                && this.isTavernAlly(attacker)) {
            this.setLastHurtByMob(null);
            return false;
        }
        // Shield blocking
        if (!this.level().isClientSide() && amount > 0.0F && this.canBlockDamageSource(source)) {
            this.playSound(net.minecraft.sounds.SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().random.nextFloat() * 0.4F);
            this.damageBlockedWithShield += amount;
            if (this.damageBlockedWithShield >= 10.0F) { // Shield break threshold
                this.damageBlockedWithShield = 0.0F;
                this.lastTickShieldDisabled = this.tickCount;
            }
        }
        return super.hurt(source, amount);
    }

    public int getLastTimeHitByAxeWhileBlocking() {
        return this.lastTickShieldDisabled;
    }

    public Vec3 getLastPosAttackTarget() {
        return this.lastPosAttackTarget;
    }

    public int getLastTimeSeenAttackTarget() {
        return this.lastTimeSeenAttackTarget;
    }

    public boolean hasHomePositionCQR() {
        return this.homePosition != null;
    }

    public BlockPos getHomePositionCQR() {
        return this.homePosition;
    }

    public void setHomePositionCQR(BlockPos pos) {
        this.homePosition = pos;
    }

    public boolean hasPatrolPath() {
        return !this.patrolPath.isEmpty();
    }

    public java.util.List<BlockPos> getPatrolPathWorld() {
        BlockPos anchor = this.homePosition != null ? this.homePosition : this.blockPosition();
        return this.patrolPath.stream().map(anchor::offset).toList();
    }

    public void setPatrolPathFromWorld(java.util.List<BlockPos> worldNodes) {
        if (this.homePosition == null) this.homePosition = this.blockPosition();
        this.patrolPath.clear();
        for (BlockPos node : worldNodes) this.patrolPath.add(node.subtract(this.homePosition).immutable());
        this.patrolPathIndex = 0;
        this.getNavigation().stop();
    }

    public BlockPos getCurrentPatrolNode() {
        if (this.patrolPath.isEmpty()) return null;
        if (this.patrolPathIndex < 0 || this.patrolPathIndex >= this.patrolPath.size()) this.patrolPathIndex = 0;
        BlockPos anchor = this.homePosition != null ? this.homePosition : this.blockPosition();
        return anchor.offset(this.patrolPath.get(this.patrolPathIndex));
    }

    public void advancePatrolNode() {
        if (!this.patrolPath.isEmpty()) this.patrolPathIndex = (this.patrolPathIndex + 1) % this.patrolPath.size();
    }

    
    public boolean hasLeader() {
        return this.leader != null && this.leader.isAlive();
    }
    
    public LivingEntity getLeader() {
        return this.leader;
    }
    
    public void setLeader(LivingEntity leader) {
        this.leader = leader;
        if (leader != null) {
            this.leaderUUID = leader.getUUID();
        } else {
            this.leaderUUID = null;
        }
    }

    @Override
    public SoundSource getSoundSource() {
        return this.isTavernNpc() ? SoundSource.NEUTRAL : SoundSource.HOSTILE;
    }

    @Override
    protected final SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.canBlockDamageSource(damageSourceIn) ? SoundEvents.SHIELD_BLOCK : this.getDefaultHurtSound(damageSourceIn);
    }

    protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.HOSTILE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel serverLevel, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, source, recentlyHit);
        ItemStack badge = this.extraInventory.getItem(1);
        if (badge.getItem() instanceof com.example.chocolatequest.item.BadgeItem) {
            net.minecraft.world.item.component.ItemContainerContents contents = badge.getOrDefault(
                    net.minecraft.core.component.DataComponents.CONTAINER,
                    net.minecraft.world.item.component.ItemContainerContents.EMPTY);
            for (ItemStack content : contents.nonEmptyItems()) {
                this.spawnAtLocation(content.copy());
            }
        }
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("ExtraInventory", this.extraInventory.createTag(this.registryAccess()));
        compound.putInt("textureIndex", this.entityData.get(TEXTURE_INDEX));
        compound.putString("mobTier", this.entityData.get(MOB_TIER));
        compound.putBoolean("hasUsedPotion", this.entityData.get(HAS_USED_POTION));
        compound.putFloat("healthScale", this.getHealthScale());
        compound.putFloat("sizeScale", this.getSizeVariation());
        compound.putBoolean("TavernNPC", this.isTavernNpc());
        ListTag savedTrades = new ListTag();
        for (TavernCustomTrade trade : this.tavernCustomTrades) {
            CompoundTag savedTrade = new CompoundTag();
            savedTrade.put("Cost", trade.cost().save(this.registryAccess()));
            savedTrade.put("Result", trade.result().save(this.registryAccess()));
            savedTrades.add(savedTrade);
        }
        compound.put(CUSTOM_TRADES_TAG, savedTrades);
        compound.putInt("TavernMerchantXp", this.tavernMerchantXp);
        if (this.homePosition != null) {
            compound.putInt("homeX", this.homePosition.getX());
            compound.putInt("homeY", this.homePosition.getY());
            compound.putInt("homeZ", this.homePosition.getZ());
        }
        compound.putLongArray("CQRPatrolPath", this.patrolPath.stream().mapToLong(BlockPos::asLong).toArray());
        compound.putInt("CQRPatrolPathIndex", this.patrolPathIndex);
        if (!this.forcedWeapon.isEmpty()) {
            compound.putString("cqrWeapon", this.forcedWeapon);
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTavernNpc(compound.getBoolean("TavernNPC") || this.isTavernNpc());
        this.tavernCustomTrades.clear();
        ListTag savedTrades = compound.getList(CUSTOM_TRADES_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < savedTrades.size(); i++) {
            CompoundTag savedTrade = savedTrades.getCompound(i);
            ItemStack cost = ItemStack.parseOptional(this.registryAccess(), savedTrade.getCompound("Cost"));
            ItemStack result = ItemStack.parseOptional(this.registryAccess(), savedTrade.getCompound("Result"));
            if (!cost.isEmpty() && !result.isEmpty()) {
                this.tavernCustomTrades.add(new TavernCustomTrade(cost, result));
            }
        }
        this.tavernMerchantXp = compound.getInt("TavernMerchantXp");
        if (compound.contains("ExtraInventory", 9)) {
            this.extraInventory.fromTag(compound.getList("ExtraInventory", 10), this.registryAccess());
        }
        if (compound.contains("textureIndex")) {
            this.entityData.set(TEXTURE_INDEX, compound.getInt("textureIndex"));
        }
        if (compound.contains("mobTier")) {
            this.entityData.set(MOB_TIER, compound.getString("mobTier"));
        }
        if (compound.contains("hasUsedPotion")) {
            this.entityData.set(HAS_USED_POTION, compound.getBoolean("hasUsedPotion"));
        }
        if (compound.contains("healthScale")) {
            this.setHealthScale(compound.getFloat("healthScale"));
        }
        if (compound.contains("sizeScale")) {
            this.setSizeVariation(compound.getFloat("sizeScale"));
        }
        if (compound.contains("homeX")) {
            this.homePosition = new BlockPos(
                    compound.getInt("homeX"),
                    compound.getInt("homeY"),
                    compound.getInt("homeZ")
            );
        }
        this.patrolPath.clear();
        for (long node : compound.getLongArray("CQRPatrolPath")) this.patrolPath.add(BlockPos.of(node));
        this.patrolPathIndex = this.patrolPath.isEmpty() ? 0 : Math.floorMod(compound.getInt("CQRPatrolPathIndex"), this.patrolPath.size());
        if (compound.contains("cqrWeapon")) {
            this.forcedWeapon = compound.getString("cqrWeapon");
        }
        
        // Equip immediately when loading from NBT (e.g. from Spawner or Spawn Egg with NBT)
        if (this.tickCount == 0 && (!this.forcedWeapon.isEmpty() || compound.contains("mobTier"))) {
            this.equipBasedOnTier(this.getMobTier());
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);

        // Apply base health from subclass
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
        this.setHealth(this.getMaxHealth());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.setDropChance(slot, 0.04F);
        }

        // Assign random tier if default
        if (this.getMobTier() == MobTier.DEFAULT) {
            MobTier[] tiers = MobTier.values();
            // simple random for now (except DEFAULT and BOSS)
            MobTier randomTier = tiers[1 + this.random.nextInt(tiers.length - 2)]; 
            this.setMobTier(randomTier);
        }

        equipBasedOnTier(this.getMobTier());

        return data;
    }

    protected void equipBasedOnTier(MobTier tier) {
        switch (tier) {
            case LEATHER:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.LEATHER_HELMET));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(net.minecraft.world.item.Items.LEATHER_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(net.minecraft.world.item.Items.LEATHER_LEGGINGS));
                this.setItemSlot(EquipmentSlot.FEET, new ItemStack(net.minecraft.world.item.Items.LEATHER_BOOTS));
                this.equipRandomWeapon(tier);
                break;
            case CHAINMAIL:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.CHAINMAIL_HELMET));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(net.minecraft.world.item.Items.CHAINMAIL_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(net.minecraft.world.item.Items.CHAINMAIL_LEGGINGS));
                this.setItemSlot(EquipmentSlot.FEET, new ItemStack(net.minecraft.world.item.Items.CHAINMAIL_BOOTS));
                this.equipRandomWeapon(tier);
                break;
            case IRON:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.IRON_HELMET));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(net.minecraft.world.item.Items.IRON_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(net.minecraft.world.item.Items.IRON_LEGGINGS));
                this.setItemSlot(EquipmentSlot.FEET, new ItemStack(net.minecraft.world.item.Items.IRON_BOOTS));
                this.equipRandomWeapon(tier);
                break;
            case DIAMOND:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.DIAMOND_HELMET));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(net.minecraft.world.item.Items.DIAMOND_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(net.minecraft.world.item.Items.DIAMOND_LEGGINGS));
                this.setItemSlot(EquipmentSlot.FEET, new ItemStack(net.minecraft.world.item.Items.DIAMOND_BOOTS));
                this.equipRandomWeapon(tier);
                break;
            case GOLD:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.GOLDEN_HELMET));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(net.minecraft.world.item.Items.GOLDEN_CHESTPLATE));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(net.minecraft.world.item.Items.GOLDEN_LEGGINGS));
                this.setItemSlot(EquipmentSlot.FEET, new ItemStack(net.minecraft.world.item.Items.GOLDEN_BOOTS));
                this.equipRandomWeapon(tier);
                break;
            case HEALER:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(net.minecraft.world.item.Items.WHITE_WOOL));
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.HEAL_STAFF.get()));
                break;
            case BOSS:
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.BULL_HELMET.get()));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.BULL_CHESTPLATE.get()));
                this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.BULL_LEGGINGS.get()));
                this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.BULL_BOOTS.get()));
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(net.minecraft.world.item.Items.IRON_SWORD));
                break;
            case DEFAULT:
            default:
                this.equipRandomWeapon(tier);
                break;
        }
    }

    protected void equipRandomWeapon(MobTier tier) {
        if (!this.forcedWeapon.isEmpty()) {
            if (this.forcedWeapon.equals("Sword")) {
                net.minecraft.world.item.Item sword = net.minecraft.world.item.Items.IRON_SWORD;
                switch (tier) {
                    case LEATHER: sword = net.minecraft.world.item.Items.WOODEN_SWORD; break;
                    case CHAINMAIL: sword = net.minecraft.world.item.Items.STONE_SWORD; break;
                    case IRON: sword = net.minecraft.world.item.Items.IRON_SWORD; break;
                    case DIAMOND: sword = net.minecraft.world.item.Items.DIAMOND_SWORD; break;
                    case GOLD: sword = net.minecraft.world.item.Items.GOLDEN_SWORD; break;
                    default: break;
                }
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(sword));
                if (this.random.nextFloat() < 0.4F) {
                    this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(com.example.chocolatequest.entity.mob.CQEntityHelper.getShieldForMob(this)));
                } else {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
            } else if (this.forcedWeapon.equals("Bow")) {
                float gunChance = this.random.nextFloat();
                if (gunChance < 0.15f) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MUSKET.get()));
                } else if (gunChance < 0.25f) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.REVOLVER.get()));
                } else {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(net.minecraft.world.item.Items.BOW));
                }
                this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            } else if (this.forcedWeapon.equals("Staff")) {
                net.minecraft.world.item.Item[] staffs = {
                    ModItems.FIRE_STAFF.get(),
                    ModItems.POISON_STAFF.get(),
                    ModItems.WIND_STAFF.get(),
                    ModItems.ICE_STAFF.get(),
                    ModItems.WATER_STAFF.get(),
                    ModItems.HEAL_STAFF.get()
                };
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(staffs[this.random.nextInt(staffs.length)]));
                this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            return;
        }
        float r = this.random.nextFloat();
        if (r < 0.2F) { // 20% chance for an archer (bow/gun)
            float gunChance = this.random.nextFloat();
            if (gunChance < 0.15f) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MUSKET.get()));
            } else if (gunChance < 0.25f) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.REVOLVER.get()));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(net.minecraft.world.item.Items.BOW));
            }
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else if (r < 0.4F) { // 20% chance for a staff
            net.minecraft.world.item.Item[] staffs = {
                ModItems.FIRE_STAFF.get(),
                ModItems.POISON_STAFF.get(),
                ModItems.WIND_STAFF.get(),
                ModItems.ICE_STAFF.get(),
                ModItems.WATER_STAFF.get(),
                ModItems.HEAL_STAFF.get()
            };
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(staffs[this.random.nextInt(staffs.length)]));
            this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else { // 60% chance for sword (knight)
            net.minecraft.world.item.Item sword = net.minecraft.world.item.Items.IRON_SWORD;
            switch (tier) {
                case LEATHER: sword = net.minecraft.world.item.Items.STONE_SWORD; break;
                case CHAINMAIL: sword = net.minecraft.world.item.Items.IRON_SWORD; break;
                case IRON: sword = net.minecraft.world.item.Items.IRON_SWORD; break;
                case DIAMOND: sword = net.minecraft.world.item.Items.DIAMOND_SWORD; break;
                case GOLD: sword = net.minecraft.world.item.Items.GOLDEN_SWORD; break;
                default: break;
            }
            
            // 5% chance to spawn as a "Commander" with feather and higher shield chance
            boolean isCommander = this.random.nextFloat() < 0.05F;
            if (isCommander) {
                // Put feather in extraInventory slot 1 (Badge) so it renders above helmet
                this.extraInventory.setItem(1, new ItemStack(net.minecraft.world.item.Items.FEATHER));
            }
            
            if ((tier == MobTier.IRON || tier == MobTier.DIAMOND) && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(sword));
                this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(sword));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(sword));
                if (this.random.nextFloat() < (isCommander ? 0.6F : 0.4F)) {
                    this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(com.example.chocolatequest.entity.mob.CQEntityHelper.getShieldForMob(this)));
                } else {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "base_controller", 5, this::predicateBase));
        controllers.add(new AnimationController<>(this, "two_handed_controller", 3, this::predicateTwoHanded));
        controllers.add(new AnimationController<>(this, "right_arm_controller", 2, this::predicateRightArm));
        controllers.add(new AnimationController<>(this, "left_arm_controller", 2, this::predicateLeftArm));
    }

    protected PlayState predicateBase(AnimationState<AbstractEntityCQR> event) {
        if (this.isPassenger() || this.isSitting()) {
            event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(SIT_ANIM);
            return PlayState.CONTINUE;
        } else if (this.isCrouching()) {
            event.getController().setAnimationSpeed(1.0D);
            event.getController().setAnimation(SNEAK_ANIM);
            return PlayState.CONTINUE;
        }
        
        if (event.isMoving()) {
            event.getController().setAnimationSpeed(1.5D);
            event.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }
        
        event.getController().setAnimationSpeed(1.0D);
        event.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    protected PlayState predicateRightArm(AnimationState<AbstractEntityCQR> event) {
        if (isHoldingTwoHandedWeapon()) return PlayState.STOP;
        
        ItemStack rightHandItem = this.isLeftHanded() ? this.getOffhandItem() : this.getMainHandItem();
        String itemName = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(rightHandItem.getItem()).getPath();
        
        if (itemName.contains("revolver") || itemName.contains("pistol")) {
            event.getController().setAnimation(FIREARM_SMALL_RIGHT);
            return PlayState.CONTINUE;
        } else if (itemName.contains("musket") || itemName.contains("rifle") || rightHandItem.getItem() instanceof net.minecraft.world.item.BowItem || rightHandItem.getItem() instanceof net.minecraft.world.item.CrossbowItem) {
            event.getController().setAnimation(FIREARM_RIGHT);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    protected PlayState predicateLeftArm(AnimationState<AbstractEntityCQR> event) {
        if (isHoldingTwoHandedWeapon()) return PlayState.STOP;
        
        ItemStack leftHandItem = this.isLeftHanded() ? this.getMainHandItem() : this.getOffhandItem();
        String itemName = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(leftHandItem.getItem()).getPath();
        
        if (itemName.contains("revolver") || itemName.contains("pistol")) {
            event.getController().setAnimation(FIREARM_SMALL_LEFT);
            return PlayState.CONTINUE;
        } else if (itemName.contains("musket") || itemName.contains("rifle") || leftHandItem.getItem() instanceof net.minecraft.world.item.BowItem || leftHandItem.getItem() instanceof net.minecraft.world.item.CrossbowItem) {
            event.getController().setAnimation(FIREARM_LEFT);
            return PlayState.CONTINUE;
        }
        
        return PlayState.STOP;
    }

    private boolean isHoldingTwoHandedWeapon() {
        return this.getMainHandItem().getItem() instanceof com.example.chocolatequest.item.BigswordItem ||
               this.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.BigswordItem ||
               this.getMainHandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem ||
               this.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem;
    }

    protected PlayState predicateTwoHanded(AnimationState<AbstractEntityCQR> event) {
        if (isSpinningToWin()) {
            event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop("animation.biped.arms.spin-to-win"));
            return PlayState.CONTINUE;
        }
        if (this.getMainHandItem().getItem() instanceof com.example.chocolatequest.item.BigswordItem || this.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.BigswordItem) {
            if (this.swinging) {
                if (this.swingTime <= 1) event.getController().forceAnimationReset();
                event.getController().setAnimation(GREATSWORD_ATTACK);
            } else {
                event.getController().setAnimation(GREATSWORD_POSE);
            }
            return PlayState.CONTINUE;
        } else if (this.getMainHandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem || this.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem) {
            if (this.swinging) {
                if (this.swingTime <= 1) event.getController().forceAnimationReset();
                event.getController().setAnimation(SPEAR_ATTACK);
            } else {
                boolean left = this.isLeftHanded();
                if (this.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.SpearItem) {
                    left = !left;
                }
                event.getController().setAnimation(left ? SPEAR_POSE_LEFT : SPEAR_POSE_RIGHT);
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private record TavernCustomTrade(ItemStack cost, ItemStack result) {
    }

    
    public static class CQRHurtByTargetGoal extends HurtByTargetGoal {
        public CQRHurtByTargetGoal(AbstractEntityCQR mob) {
            super(mob);
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) return false;
            LivingEntity attacker = this.mob.getLastHurtByMob();
            if (attacker == null) return false;
            AbstractEntityCQR cqrMob = (AbstractEntityCQR) this.mob;
            if (cqrMob.isTavernAlly(attacker)) return false;
            // Don't retaliate against allies
            if (attacker instanceof IFactionRelated other) {
                if (cqrMob.getFaction().isAlly(other.getFaction())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void alertOthers() {
            double alertRadius = 32.0D;
            var list = this.mob.level().getEntitiesOfClass(
                    AbstractEntityCQR.class,
                    this.mob.getBoundingBox().inflate(alertRadius, 10.0D, alertRadius)
            );
            for (AbstractEntityCQR ally : list) {
                if (ally == this.mob) continue;
                if (ally.getTarget() != null) continue;
                AbstractEntityCQR cqrMob = (AbstractEntityCQR) this.mob;
                if (!cqrMob.getFaction().isAlly(ally.getFaction())) continue;
                LivingEntity attacker = this.mob.getLastHurtByMob();
                if (attacker != null && ally.canAttack(attacker)) {
                    ally.setTarget(attacker);
                }
            }
        }
    }

    
    public static class CQRNearestAttackableTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
        private final AbstractEntityCQR cqrMob;

        public CQRNearestAttackableTargetGoal(AbstractEntityCQR mob) {
            super(mob, LivingEntity.class, 10, true, false, null);
            this.cqrMob = mob;
        }

        @Override
        protected boolean canAttack(@Nullable LivingEntity target, net.minecraft.world.entity.ai.targeting.TargetingConditions conditions) {
            if (target == null) return false;
            // Check faction
            if (target instanceof IFactionRelated other) {
                if (cqrMob.getFaction().isAlly(other.getFaction())) return false;
                if (!cqrMob.getFaction().isEnemy(other.getFaction())) return false;
            }
            if (target instanceof Player) {
                if (!cqrMob.getFaction().isEnemy(EDefaultFaction.PLAYERS)) return false;
            } else {
                // Non-faction, non-player entities: don't target by default
                return false;
            }
            return super.canAttack(target, conditions);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        net.minecraft.world.item.ItemStack mainHandItem = this.getMainHandItem();
        String itemName = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(mainHandItem.getItem()).getPath();
        
        if (itemName.contains("revolver") || itemName.contains("pistol") || itemName.contains("musket") || itemName.contains("rifle")) {
            com.example.chocolatequest.item.EBulletType bulletType = com.example.chocolatequest.item.EBulletType.IRON;
            net.minecraft.world.item.ItemStack offhand = this.getOffhandItem();
            if (offhand.getItem() == ModItems.BULLET_FIRE.get()) bulletType = com.example.chocolatequest.item.EBulletType.FIRE;
            else if (offhand.getItem() == ModItems.BULLET_GOLD.get()) bulletType = com.example.chocolatequest.item.EBulletType.GOLD;
            else if (offhand.getItem() == ModItems.BULLET_DIAMOND.get()) bulletType = com.example.chocolatequest.item.EBulletType.DIAMOND;
            
            com.example.chocolatequest.entity.projectile.ProjectileBulletEntity bullet = new com.example.chocolatequest.entity.projectile.ProjectileBulletEntity(this.level(), this, bulletType);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - bullet.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            bullet.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
            this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(bullet);
            return;
        }

        net.minecraft.world.item.ItemStack itemstack = this.getProjectile(this.getItemInHand(net.minecraft.world.entity.projectile.ProjectileUtil.getWeaponHoldingHand(this, net.minecraft.world.item.Items.BOW)));
        net.minecraft.world.entity.projectile.AbstractArrow abstractarrow = net.minecraft.world.entity.projectile.ProjectileUtil.getMobArrow(this, itemstack, velocity, this.getMainHandItem());
        if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.CrossbowItem) {
             abstractarrow = net.minecraft.world.entity.projectile.ProjectileUtil.getMobArrow(this, itemstack, velocity, this.getMainHandItem());
        }
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(net.minecraft.sounds.SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

}
