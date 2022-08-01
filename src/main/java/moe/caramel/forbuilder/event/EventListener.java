package moe.caramel.forbuilder.event;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import static moe.caramel.forbuilder.Main.DEBUG;

public record EventListener(@NotNull Plugin plugin) implements Listener {

    private static final NamespacedKey OVER_WORLD = NamespacedKey.minecraft("overworld");

    @EventHandler
    void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getConfig().getBoolean(DEBUG, false)) return;
        if (!event.getBlock().getWorld().getKey().equals(OVER_WORLD)) return;
        event.setCancelled(true);
    }

    @EventHandler
    void onBlockBreak(BlockPlaceEvent event) {
        if (plugin.getConfig().getBoolean(DEBUG, false)) return;
        if (!event.getBlock().getWorld().getKey().equals(OVER_WORLD)) return;
        event.setCancelled(true);
    }

    @EventHandler
    void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    void onBlockRedstone(BlockRedstoneEvent event) {
        event.setNewCurrent(0); // No
    }

    @EventHandler
    void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;
        if (event.getClickedBlock().getType() != Material.SOUL_SAND) return;
        event.setCancelled(true);
    }

    @EventHandler
    void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event) {
        if (event.getPlayer().isSneaking()) return; // Use "Toggle Sneaking"
        event.setCancelled(true);
    }
}
