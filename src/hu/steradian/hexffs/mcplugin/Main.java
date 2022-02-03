package hu.steradian.hexffs.mcplugin;

import hu.steradian.hexffs.mcplugin.fun.Coolant;
import hu.steradian.hexffs.mcplugin.fun.Refrigerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

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
        pm.registerEvents(new Coolant(), this);

        Refrigerator.registerRefrigeratorRecipe();
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
