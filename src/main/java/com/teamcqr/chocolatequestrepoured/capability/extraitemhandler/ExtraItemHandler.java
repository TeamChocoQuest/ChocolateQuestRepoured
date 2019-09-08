package com.teamcqr.chocolatequestrepoured.capability.extraitemhandler;

import net.minecraftforge.items.ItemStackHandler;

public class ExtraItemHandler extends ItemStackHandler implements IExtraItemHandler {

	public ExtraItemHandler() {
		this(1);
	}

	public ExtraItemHandler(int slots) {
		super(slots);
	}

}
