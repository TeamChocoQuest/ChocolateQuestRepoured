package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorHeavy;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorTransparent;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelArmorTurtle;
import com.teamcqr.chocolatequestrepoured.client.models.armor.ModelBackpack;

public class ModArmorModels {

	public static ModelBackpack backpack = new ModelBackpack(0.0F);

	public static ModelArmorTransparent slimeArmor = new ModelArmorTransparent(0.75F);
	public static ModelArmorTransparent slimeArmorLegs = new ModelArmorTransparent(0.3F);

	public static ModelArmorTurtle turtleArmor = new ModelArmorTurtle(0.5F);
	public static ModelArmorTurtle turtleArmorLegs = new ModelArmorTurtle(0.25F);

	//This is supposed! Reason: If future version have a separate model for the heavy diamond armor, adaption isnt really needed
	public static ModelArmorHeavy heavyIronArmor = new ModelArmorHeavy(1F);
	public static ModelArmorHeavy heavyIronArmorLegs = new ModelArmorHeavy(0.5F);
	
	public static ModelArmorHeavy heavyDiamondArmor = new ModelArmorHeavy(1F);
	public static ModelArmorHeavy heavyDiamondArmorLegs = new ModelArmorHeavy(0.5F);
	

}
