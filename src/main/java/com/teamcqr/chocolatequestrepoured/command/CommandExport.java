package com.teamcqr.chocolatequestrepoured.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandExport extends CommandBase {

	@Override
	public String getName() {
		return "cqr_export_all";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cqr_export_all [override_files]";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		List<TileEntityExporter> exporterList = new LinkedList<>();

		for (TileEntity tileEntity : sender.getEntityWorld().loadedTileEntityList) {
			if (tileEntity instanceof TileEntityExporter) {
				exporterList.add((TileEntityExporter) tileEntity);
			}
		}

		sender.sendMessage(new TextComponentString("Trying to export " + exporterList.size() + " structures..."));
		for (TileEntityExporter exporter : exporterList) {
			File file = new File(CQRMain.CQ_EXPORT_FILES_FOLDER, exporter.structureName + ".nbt");

			if (!file.exists() || (args.length >= 1 && args[0].equals("true"))) {
				exporter.saveStructure(sender.getEntityWorld(), exporter.getMinPos(), exporter.getMaxPos(), (EntityPlayer) sender);
			} else {
				String msg = "Couldn't export structure " + exporter.structureName + " because a file with that name already exists and file overriding is disabled.";
				sender.sendMessage(new TextComponentString(msg));
			}
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length == 1) {
			List<String> list = new ArrayList<>();
			list.add("false");
			list.add("true");
			return list;
		} else {
			return Collections.emptyList();
		}
	}

}
