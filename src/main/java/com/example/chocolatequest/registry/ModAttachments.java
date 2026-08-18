package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.attachment.PlayerReputationAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ChocolateQuestReDone.MODID);

    public static final Supplier<AttachmentType<PlayerReputationAttachment>> PLAYER_REPUTATION = ATTACHMENTS.register(
            "player_reputation",
            () -> AttachmentType.serializable(PlayerReputationAttachment::new).build()
    );
}
