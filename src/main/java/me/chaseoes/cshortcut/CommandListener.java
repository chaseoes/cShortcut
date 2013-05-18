package me.chaseoes.cshortcut;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.google.common.base.Joiner;

public class CommandListener implements Listener {

    CShortcut plugin;

    public CommandListener(CShortcut p) {
        plugin = p;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String args[] = event.getMessage().split(" ");

        // Loop through all defined commands in the configuration.
        for (String commandToCheck : plugin.getConfig().getConfigurationSection("commands").getKeys(false)) {
            // Check if the command we're currently looping through matches the
            // one the player typed.
            if (commandToCheck.equalsIgnoreCase(args[0])) {
                String permission = plugin.getConfig().getString("commands." + args[0] + ".permission");
                boolean canDo = false;
                if (permission != null) {
                    if (player.hasPermission(permission)) {
                        canDo = true;
                    } else {
                        canDo = false;
                    }
                } else {
                    canDo = true;
                }
                
                if (canDo) {
                    // Loop through all commands in the string list.
                    List<String> commands = plugin.getConfig().getStringList("commands." + args[0] + ".commands");
                    for (String com : commands) {
                        // Replace variables...
                        for (int i = 1; i < args.length; i++) {
                            com = com.replace("%" + i, args[i]);
                        }

                        com = com.replace("%0", Joiner.on(" ").join(args).replace(args[0] + " ", "")).replace("%name", player.getName()).replace("%displayname", player.getDisplayName());
                        String command = ChatColor.translateAlternateColorCodes('&', com);

                        // Execute commands.
                        doCommand(player, command);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission.");
                }
                event.setCancelled(true);
            }
        }
    }

    public void doCommand(final Player player, String com) {
        if (com.startsWith("%delay-")) {
            String del = com.split("\\ ")[0];
            int delay = Integer.parseInt(del.replace("%delay-", ""));
            final String command = com.replace("%delay-" + delay, "").substring(1);
            System.out.println(command);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (command.startsWith("/")) {
                        player.performCommand(command.substring(1));
                    } else if (command.contains("%console ")) {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%console ", ""));
                    } else if (command.contains("%message ")) {
                        player.sendMessage(command.replace("%message ", ""));
                    } else if (command.contains("%broadcast ")) {
                        plugin.getServer().broadcastMessage(command.replace("%broadcast ", ""));
                    }
                }
            }, delay * 20L);
        } else {
            String command = com;
            if (command.startsWith("/")) {
                player.performCommand(command.substring(1));
            } else if (command.contains("%console ")) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%console ", ""));
            } else if (command.contains("%message ")) {
                player.sendMessage(command.replace("%message ", ""));
            } else if (command.contains("%broadcast ")) {
                plugin.getServer().broadcastMessage(command.replace("%broadcast ", ""));
            }
        }
    }

}
