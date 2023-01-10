package dev.badbird.playercommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class PlayerCommands extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        List<String> commands = getConfig().getStringList("commands.join");
        int delay = getConfig().getInt("command.delay");
        if (delay > 0) {
            getServer().getScheduler().runTaskLater(this, () -> {
                execute(commands, event.getPlayer());
            }, delay);
        } else {
            execute(commands, event.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        List<String> commands = getConfig().getStringList("commands.leave");
        int delay = getConfig().getInt("command.delay");
        if (delay > 0) {
            getServer().getScheduler().runTaskLater(this, () -> {
                execute(commands, event.getPlayer());
            }, delay);
        } else {
            execute(commands, event.getPlayer());
        }
    }

    public void execute(List<String> commands, Player player) {
        for (String command : commands) {
            command = command.replace("%player%", player.getName());
            boolean console = !command.startsWith("player:");
            if (!console) {
                command = command.substring(7);
            }
            if (command.startsWith("console:")) {
                console = true;
                command = command.substring(8);
            }
            if (console) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } else {
                player.performCommand(command);
            }
        }
    }
}
