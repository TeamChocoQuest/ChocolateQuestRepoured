package team.cqr.cqrepoured.capability.armor.attachment;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityArmorAttachmentProvider extends SerializableCapabilityProvider<ICapabilityArmorAttachment> {

	public static final ResourceLocation IDENTIFIER = CQRMain.prefix("armor_attachment");
	
	public CapabilityArmorAttachmentProvider(Capability<ICapabilityArmorAttachment> capability, ICapabilityArmorAttachment defaultData) {
		super(capability, defaultData);
	}
	
	public static CapabilityArmorAttachmentProvider createProvider() {
		return new CapabilityArmorAttachmentProvider(CQRCapabilities.ARMOR_ATTACHMENT, new CapabilityArmorAttachment());
	}

}
