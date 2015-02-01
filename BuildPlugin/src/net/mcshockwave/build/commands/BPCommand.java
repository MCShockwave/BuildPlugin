package net.mcshockwave.build.commands;

import net.mcshockwave.MCS.Utils.PacketUtils;
import net.mcshockwave.MCS.Utils.PacketUtils.ParticleEffect;
import net.mcshockwave.build.BuildPlugin;
import net.mcshockwave.build.FileElements;
import net.mcshockwave.build.utils.LaserTagMapGenerator;
import net.mcshockwave.build.utils.WorldFileUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class BPCommand implements CommandExecutor {

	public static HashMap<String, BukkitTask>	showTasks	= new HashMap<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			World w = p.getWorld();

			if (args.length == 0) {

			} else {
				String cmd = args[0];

				if (cmd.equalsIgnoreCase("setspawn")) {
					String team = args[1];
					Location loc = p.getLocation();
					loc = BuildPlugin.roundLocation(loc);

					FileElements.set(team + "-spawnpoint", loc, w);

					p.sendMessage("§cSet spawn for " + team + " at current location in " + w.getName());
					p.sendMessage("§eLocation: "
							+ String.format("x%s y%s z%s yaw%s pit%s", loc.getX(), loc.getY(), loc.getZ(),
									loc.getYaw(), loc.getPitch()));
				}

				if (cmd.equalsIgnoreCase("gotospawn")) {
					String team = args[1];
					Location tp = FileElements.getLoc(team + "-spawnpoint", w);
					p.teleport(tp);
					p.sendMessage("§cTeleported to spawn for " + team + " at " + w.getName());
				}

				if (cmd.equalsIgnoreCase("setlobby")) {
					Location loc = p.getLocation();
					loc = BuildPlugin.roundLocation(loc);

					FileElements.set("lobby", loc, w);

					p.sendMessage("§cSet lobby at current location in " + w.getName());
					p.sendMessage("§eLocation: "
							+ String.format("x%s y%s z%s yaw%s pit%s", loc.getX(), loc.getY(), loc.getZ(),
									loc.getYaw(), loc.getPitch()));
				}

				if (cmd.equalsIgnoreCase("gotolobby")) {
					Location tp = FileElements.getLoc("lobby", w);
					p.teleport(tp);
					p.sendMessage("§cTeleported to lobby at " + w.getName());
				}

				if (cmd.equalsIgnoreCase("setblock")) {
					String el = args[1];
					@SuppressWarnings("deprecation")
					Location bloc = p.getTargetBlock(null, 100).getLocation();
					FileElements.set(el, bloc, w);
					p.sendMessage("§cSet target block to element " + el);
				}

				if (cmd.equalsIgnoreCase("getblock")) {
					String el = args[1];
					Location bl = FileElements.getLoc(el, w).add(0.5, 0, 0.5);
					p.teleport(bl);
				}

				if (cmd.equalsIgnoreCase("setloc")) {
					String el = args[1];
					Location loc = p.getLocation();
					loc = BuildPlugin.roundLocation(loc);

					FileElements.set(el, loc, w);

					p.sendMessage("§cSet " + el + " to current location in " + w.getName());
					p.sendMessage("§eLocation: "
							+ String.format("x%s y%s z%s yaw%s pit%s", loc.getX(), loc.getY(), loc.getZ(),
									loc.getYaw(), loc.getPitch()));
				}

				if (cmd.equalsIgnoreCase("gotoloc")) {
					String el = args[1];
					Location tp = FileElements.getLoc(el, w);
					p.teleport(tp);
					p.sendMessage("§cTeleported to " + el + " at " + w.getName());
				}

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

				if (cmd.equalsIgnoreCase("addloc")) {
					int id = 0;
					while (FileElements.has(args[1] + "-" + ++id, w.getName())) {
					}
					String el = args[1] + "-" + id;
					Location loc = p.getLocation();
					loc = BuildPlugin.roundLocation(loc);

					FileElements.set(el, loc, w);

					p.sendMessage("§cSet " + el + " to current location in " + w.getName());
					p.sendMessage("§eLocation: "
							+ String.format("x%s y%s z%s yaw%s pit%s", loc.getX(), loc.getY(), loc.getZ(),
									loc.getYaw(), loc.getPitch()));
				}
				if (cmd.equalsIgnoreCase("addblock")) {
					int id = 0;
					while (FileElements.has(args[1] + "-" + ++id, w.getName())) {
					}
					String el = args[1] + "-" + id;
					@SuppressWarnings("deprecation")
					Location bloc = p.getTargetBlock(null, 100).getLocation();
					FileElements.set(el, bloc, w);
					p.sendMessage("§cSet target block to element " + el);
				}

				if (cmd.equalsIgnoreCase("set")) {
					String el = args[1];
					String setto = "";
					for (int i = 2; i < args.length; i++) {
						setto += " " + args[i];
					}
					setto = setto.replaceFirst(" ", "");

					FileElements.set(el, setto, w.getName());

					p.sendMessage("§cSet element '" + el + "' to \"" + setto + "\" in world " + w.getName());
				}

				if (cmd.equalsIgnoreCase("get")) {
					String el = args[1];

					p.sendMessage("§cElement '" + el + "' is \"" + FileElements.get(el, w.getName()) + "\" in "
							+ w.getName());
				}

				if (cmd.equalsIgnoreCase("push")) {
					BuildPlugin.pushMap(args[1]);
				}

				if (cmd.equalsIgnoreCase("ltmap")) {
					Location cent = p.getLocation().clone().add(0, -1, 0);
					int rad = Integer.parseInt(args[1]);
					LaserTagMapGenerator.generate(cent, rad);
				}

				if (cmd.equalsIgnoreCase("showall")) {
					if (showTasks.containsKey(p.getName())) {
						showTasks.get(p.getName()).cancel();
						showTasks.remove(p.getName());
						p.sendMessage("§cStopped showing");
					} else {
						final String show = args[1];
						showTasks.put(p.getName(), new BukkitRunnable() {
							public void run() {
								if (!p.isOnline()) {
									showTasks.remove(p.getName());
									cancel();
								}
								for (Location l : FileElements.getAll(show, p.getWorld())) {
									PacketUtils.playParticleEffect(ParticleEffect.WITCH_MAGIC,
											l.clone().add(0.5, 0.5, 0.5), 0, 0.05f, 2);
								}
							}
						}.runTaskTimer(BuildPlugin.ins, 2, 2));
						p.sendMessage("§aShowing all blocks named " + show);
					}
				}
			}
		}
		return false;
	}
}
