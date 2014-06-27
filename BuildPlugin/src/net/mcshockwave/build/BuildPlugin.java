package net.mcshockwave.build;

import net.mcshockwave.build.commands.BPCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildPlugin extends JavaPlugin {

	public static BuildPlugin	ins;

	@Override
	public void onEnable() {
		ins = this;

		Bukkit.getPluginManager().registerEvents(new DefaultListener(), this);

		getCommand("bp").setExecutor(new BPCommand());
	}

	public static Location roundLocation(Location toRound) {
		Location l = toRound.clone();

		l.setX(round(l.getX(), 0.5));
		l.setY(round(l.getY(), 0.5));
		l.setZ(round(l.getZ(), 0.5));

		l.setPitch(0);
		l.setYaw((float) round(l.getYaw(), 45));

		return l;
	}

	public static double round(double toRound, double nearest) {
		double mult = 1 / nearest;

		toRound *= mult;
		toRound = (int) Math.round(toRound);
		toRound /= mult;

		return toRound;
	}

}
