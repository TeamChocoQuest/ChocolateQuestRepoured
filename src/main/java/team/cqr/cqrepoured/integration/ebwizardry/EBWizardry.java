package team.cqr.cqrepoured.integration.ebwizardry;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EBWizardry {

	public static final DataParameter<String> CONTINUOUS_SPELL = EntityDataManager.createKey(AbstractEntityCQR.class, DataSerializers.STRING);
	public static final DataParameter<Integer> SPELL_COUNTER = EntityDataManager.createKey(AbstractEntityCQR.class, DataSerializers.VARINT);

}
