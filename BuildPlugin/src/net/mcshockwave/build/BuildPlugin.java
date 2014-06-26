package net.mcshockwave.build;

import net.mcshockwave.build.commands.BPCommand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildPlugin extends JavaPlugin {
	
	public static BuildPlugin ins;
	
	@Override
	public void onEnable() {
		ins = this;
		
		Bukkit.getPluginManager().registerEvents(new DefaultListener(), this);
		
		getCommand("bp").setExecutor(new BPCommand());
	}

}
