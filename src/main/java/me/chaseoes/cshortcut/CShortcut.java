package me.chaseoes.cshortcut;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CShortcut extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().header("cShortcut version " + getDescription().getVersion() + " by chaseoes. Configuration help: http://dev.bukkit.org/server-mods/cshortcut/ #");
        if (!new File(getDataFolder() + "/config.yml").exists()) {
            getConfig().options().copyDefaults(true);
        }
        getConfig().options().copyHeader(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("cshortcut")) {
            if (strings.length == 0) {
                cs.sendMessage(ChatColor.GOLD + "[cShortcut] " + ChatColor.LIGHT_PURPLE + "Version " + ChatColor.GREEN + getDescription().getVersion() + ChatColor.LIGHT_PURPLE + " by chaseoes.");
                return true;
            }

            if (strings[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                saveConfig();
                cs.sendMessage(ChatColor.GOLD + "[cShortcut] " + ChatColor.LIGHT_PURPLE + "Successfully reloaded the configuration.");
                return true;
            } else if (strings[0].equalsIgnoreCase("help")) {
                cs.sendMessage(ChatColor.GOLD + "[cShortcut] " + ChatColor.LIGHT_PURPLE + "You may use the following commands:");
                cs.sendMessage(ChatColor.RED + "/cShortcut" + ChatColor.GRAY + ": General plugin information.");
                cs.sendMessage(ChatColor.RED + "/cShortcut reload" + ChatColor.GRAY + ": Reload the configuration.");
                return true;
            }
            cs.sendMessage(ChatColor.GOLD + "[cShortcut] " + ChatColor.LIGHT_PURPLE + "Unknown command. Type " + ChatColor.GREEN + "/cShortcut help " + ChatColor.LIGHT_PURPLE + "for help.");
        }
        return true;
    }

}
