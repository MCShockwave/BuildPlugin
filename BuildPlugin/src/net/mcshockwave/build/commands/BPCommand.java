package net.mcshockwave.build.commands;

import net.mcshockwave.build.utils.WorldFileUtils;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			World w = p.getWorld();

			if (args.length == 0) {

			} else {
				String cmd = args[0];

				if (cmd.equalsIgnoreCase("regen")) {
					p.sendMessage("§cRegenerated file for " + w.getName());
					WorldFileUtils.set(w, new String[0]);
				}

				if (cmd.equalsIgnoreCase("append")) {
					String append = "";
					for (int i = 1; i < args.length; i++) {
						append += " " + args[i];
					}
					append = append.replaceFirst(" ", "");
					
					WorldFileUtils.append(w, append);
					p.sendMessage("§cAppended \"" + append + "\" to " + w.getName());
				}

				if (cmd.equalsIgnoreCase("read")) {
					p.sendMessage("§cLines of file for " + w.getName());
					String[] get = WorldFileUtils.get(w);
					for (String s : get) {
						p.sendMessage(s);
					}
				}
			}
		}
		return false;
	}

}
