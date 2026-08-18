package com.example.chocolatequest.entity.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;

public abstract class AbstractCQRNPC extends AbstractVillager implements GeoEntity, com.example.chocolatequest.faction.IFactionRelated {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public AbstractCQRNPC(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
    }

    public com.example.chocolatequest.faction.EDefaultFaction getDefaultFaction() {
        return com.example.chocolatequest.faction.EDefaultFaction.NPC;
    }

    @Override
    public com.example.chocolatequest.faction.EDefaultFaction getFaction() {
        return this.getDefaultFaction();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractVillager.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    protected String customGreeting = null;

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.customGreeting != null) {
            tag.putString("CustomGreeting", this.customGreeting);
        }
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CustomGreeting", net.minecraft.nbt.Tag.TAG_STRING)) {
            this.customGreeting = tag.getString("CustomGreeting");
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack itemstack = player.getItemInHand(hand);
        
        // Creative mode custom trade adder
        if (player.isCreative() && player.isShiftKeyDown() && !itemstack.isEmpty()) {
            if (!this.level().isClientSide) {
                if (this.offers == null) this.offers = new net.minecraft.world.item.trading.MerchantOffers();
                // Adds a trade: 1 Emerald -> 1 of the held item
                this.offers.add(new net.minecraft.world.item.trading.MerchantOffer(new net.minecraft.world.item.trading.ItemCost(net.minecraft.world.item.Items.EMERALD, 1), itemstack.copy(), 9999, 2, 0.05F));
                player.displayClientMessage(net.minecraft.network.chat.Component.literal("Dodano nowy handel!"), true);
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer && !this.level().isClientSide() && !player.isCrouching()) {
            int rep = player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION).getReputation(this.getFaction());
            if (rep <= -50) {
                player.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.cqrepoured.merchant_refuses"), true);
                this.level().playSound(null, this.blockPosition(), net.minecraft.sounds.SoundEvents.VILLAGER_NO,
                        net.minecraft.sounds.SoundSource.NEUTRAL, 1.0F, 0.9F);
                return InteractionResult.SUCCESS;
            }

            if (this.customGreeting != null && !this.customGreeting.isEmpty()) {
                player.displayClientMessage(net.minecraft.network.chat.Component.literal("§6<" + this.getDisplayName().getString() + "> §f" + this.customGreeting), false);
            }

            this.prepareOffersFor(player, rep);
            if (!this.getOffers().isEmpty()) {
                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new com.example.chocolatequest.network.packet.SPacketUpdatePlayerReputation(player.getUUID(), this.getFaction().name(), rep));

                this.setTradingPlayer(player);
                
                serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
                    (id, inv, p) -> new com.example.chocolatequest.inventory.ContainerCQRMerchant(
                            id, inv, this, this.getFaction().name(), this.getTradeProfile()),
                    this.getDisplayName()
                ), buf -> {
                    buf.writeUtf(this.getFaction().name());
                    buf.writeUtf(this.getTradeProfile());
                });

                // Send the merchant offers packet to populate the UI on the client!
                serverPlayer.sendMerchantOffers(
                    serverPlayer.containerMenu.containerId, 
                    this.getOffers(), 
                    1, 
                    1, 
                    this.showProgressBar(), 
                    this.canRestock()
                );

                return net.minecraft.world.InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void updateTrades() {
        if (this.offers != null && !this.offers.isEmpty()) {
            return;
        }
        this.offers = new MerchantOffers();
        populateDefaultTrades(this.offers);
    }

    protected abstract void populateDefaultTrades(MerchantOffers offers);

    protected String getTradeProfile() {
        return "merchant";
    }

    protected void populateReputationTrades(MerchantOffers offers, int reputation) {
    }

    private void prepareOffersFor(Player player, int reputation) {
        if (this.offers != null && !this.offers.isEmpty()) {
            return;
        }
        this.offers = new MerchantOffers();
        this.populateDefaultTrades(this.offers);
        this.populateReputationTrades(this.offers, reputation);

        int priceAdjustment = 0;
        if (reputation >= 20) {
            priceAdjustment = -(reputation / 20);
        } else if (reputation <= -20) {
            priceAdjustment = (-reputation) / 20;
        }
        if (priceAdjustment != 0) {
            for (MerchantOffer offer : this.offers) {
                if (offer.getBaseCostA().is(Items.EMERALD)) {
                    offer.setSpecialPriceDiff(priceAdjustment);
                }
            }
        }
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        super.notifyTrade(offer);
        if (!this.level().isClientSide() && this.getTradingPlayer() instanceof net.minecraft.server.level.ServerPlayer player) {
            com.example.chocolatequest.attachment.PlayerReputationAttachment reputation =
                    player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION);
            reputation.addReputation(this.getFaction(), 2);
            int value = reputation.getReputation(this.getFaction());
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                    new com.example.chocolatequest.network.packet.SPacketUpdatePlayerReputation(
                            player.getUUID(), this.getFaction().name(), value));
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.cqrepoured.reputation_gained", this.getFaction().name(), value), true);
        }
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        // Simple XP reward logic or empty
    }

    // GeckoLib Implementation
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, this::predicate));
    }

    protected PlayState predicate(AnimationState<AbstractCQRNPC> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.biped.legs.walk"));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.biped.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    
    @Override
    public net.minecraft.world.entity.AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel p_146743_, net.minecraft.world.entity.AgeableMob p_146744_) {
        return null;
    }
}
