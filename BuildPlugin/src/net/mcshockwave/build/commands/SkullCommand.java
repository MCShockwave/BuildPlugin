package net.mcshockwave.build.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (p.getGameMode() == GameMode.CREATIVE || p.isOp()) {
				ItemStack sk = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				SkullMeta m = (SkullMeta) sk.getItemMeta();
				m.setOwner(args[0]);
				sk.setItemMeta(m);
				p.getInventory().addItem(sk);
			}
		}
		return false;
	}

}
