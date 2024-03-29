package net.mcshockwave.build;

import net.mcshockwave.build.commands.BPCommand;
import net.mcshockwave.build.commands.SkullCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class BuildPlugin extends JavaPlugin {

	public static BuildPlugin	ins;

	@Override
	public void onEnable() {
		ins = this;

		Bukkit.getPluginManager().registerEvents(new DefaultListener(), this);

		getCommand("bp").setExecutor(new BPCommand());
		getCommand("skull").setExecutor(new SkullCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;
		if (args.length > 0) {
			String cmd = "";
			for (int i = 0; i < args.length; i++) {
				cmd += args[i] + " ";
			}
			cmd = cmd.substring(0, cmd.length() - 1);
			if (cmd.startsWith("/")) {
				cmd = cmd.replaceFirst("/", "");
			}

			if (!DefaultListener.powertools.containsKey(p.getName())) {
				DefaultListener.powertools.put(p.getName(), new HashMap<ItemStack, String>());
			}
			DefaultListener.powertools.get(p.getName()).put(
					new ItemStack(p.getItemInHand().getType(), 1, p.getItemInHand().getDurability()), cmd);

			p.sendMessage("§6Powertool for item set to \"" + cmd + "\"");
		}
		return false;
	}

	public static void pushMap(final String world) {
		new BukkitRunnable() {
			public void run() {
				createClientAndUpload(world);
				Bukkit.broadcastMessage("§eUploaded world file to remote folder!");
			}
		}.runTaskAsynchronously(ins);
	}

	public static void createClientAndUpload(String world) {
		// zipFile(new File(world), new File("/Maps/test.zip"));
		File directoryToZip = new File(world);

		List<File> fileList = new ArrayList<File>();
		Compressor.getAllFiles(directoryToZip, fileList);
		Compressor.writeZipFile(directoryToZip, fileList);

		FTPClient client = new FTPClient();
		try {
			client.connect("ftp.mcsw.us");

			client.setSoTimeout(10000);
			client.enterLocalPassiveMode();
			if (client.login("hostserver@mcsw.us", "MCShockwaveStaff")) {
				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
				uploadFile(client, new File(world + ".zip"));
			}
		} catch (Exception e) {
		}
	}

	public static final String	PATH_TO_MAPS_FOLDER	= "/Maps/";

	public static void uploadFile(FTPClient client, File f) {
		if (f.isDirectory()) {
			try {
				client.makeDirectory(PATH_TO_MAPS_FOLDER + f.getPath());
			} catch (IOException e) {
			}
			for (File file : f.listFiles()) {
				uploadFile(client, file);
			}
			return;
		}
		try {
			FileInputStream fs = new FileInputStream(f);
			String path = PATH_TO_MAPS_FOLDER + f.getPath();
			boolean success = client.storeFile(path, fs);
			Bukkit.broadcastMessage((success ? "§a" : "§c") + path);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
