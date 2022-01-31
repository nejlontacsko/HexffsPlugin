package hu.steradian.hexffs.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    private static Main plugin;
    private static List<Node> nodes = new ArrayList<Node>();

    @Override
    public void onLoad() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Loading  " + this.getName());
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());

        plugin = this;

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ShowForcefield(), this);
    }

    public static void registerNode(Node node) {
        nodes.add(node);
    }

    public static List<Node> getNodes() {
        return nodes;
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
    }

    public static Main getPlugin() {
        return plugin;
    }
}
