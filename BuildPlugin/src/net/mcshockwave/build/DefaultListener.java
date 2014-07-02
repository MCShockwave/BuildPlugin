package net.mcshockwave.build;

import net.mcshockwave.MCS.Menu.ItemMenu;
import net.mcshockwave.MCS.Menu.ItemMenu.Button;
import net.mcshockwave.MCS.Menu.ItemMenu.ButtonRunnable;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultListener implements Listener {

	int[]	ids	= { 50, 51, 52, 54, 55, 56, 57, 58, 59, 60, 61, 62, 65, 66, 90, 91, 92, 93, 94, 95, 96, 98, 100, 120 };

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();
		final Block b = event.getClickedBlock();

		if (a == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.MOB_SPAWNER
				&& event.getPlayer().getGameMode() == GameMode.CREATIVE && p.isOp()) {
			event.setCancelled(true);
			ItemMenu m = new ItemMenu("§9Select Mob", ids.length);
			int ind = -1;
			for (final int d : ids) {
				Button mob = new Button(true, Material.MONSTER_EGG, 1, d, "");
				mob.button = new ItemStack(Material.MONSTER_EGG, 1, (short) d);
				mob.setOnClick(new ButtonRunnable() {
					@SuppressWarnings("deprecation")
					public void run(Player p, InventoryClickEvent event) {
						CreatureSpawner s = (CreatureSpawner) b.getState();
						s.setSpawnedType(EntityType.fromId(d));
						s.update();
						p.sendMessage("§aSet spawner to spawn entity " + EntityType.fromId(d).name());
					}
				});
				m.addButton(mob, ++ind);
			}
			m.open(p);
		}
	}

}
