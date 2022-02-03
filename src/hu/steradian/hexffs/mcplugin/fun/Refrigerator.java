package hu.steradian.hexffs.mcplugin.fun;

import hu.steradian.hexffs.mcplugin.Main;
import net.minecraft.world.level.block.entity.TileEntityFurnaceFurnace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftFurnaceFurnace;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Refrigerator extends CraftFurnaceFurnace {

    public Refrigerator(World world, TileEntityFurnaceFurnace tileEntity) {
        super(world, tileEntity);
    }

    static public void registerRefrigeratorRecipe() {

        //Concept: usage of an own recipe
        /*List<RefrigeratorRecipe> recipes = new ArrayList<>();
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.COAL_ORE), Material.COAL));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.COPPER_ORE), Material.COPPER_INGOT));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.LAPIS_ORE), Material.LAPIS_LAZULI));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.IRON_ORE), Material.IRON_INGOT));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.REDSTONE_ORE), Material.REDSTONE_BLOCK));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.DIAMOND_ORE), Material.DIAMOND));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.GOLD_ORE), Material.GOLD_INGOT));
        recipes.add(new RefrigeratorRecipe(new ItemStack(Material.EMERALD_ORE), Material.EMERALD));

        for (RefrigeratorRecipe rr : recipes) {
            boolean res = Bukkit.addRecipe(rr);
            Bukkit.getLogger().info("Recipe registration: " + rr.getKey().getKey() + " " + (res ? "successful" : "failed"));
        }*/

        //Just the old, but working solution
        //(reduced only to gold)
        List<FurnaceRecipe> recipes = new ArrayList<>();
        recipes.add(new FurnaceRecipe(new NamespacedKey(Main.getPlugin(), "fun"), new ItemStack(Material.GOLD_ORE), Material.GOLD_INGOT, 0.07F, 200));

        for (FurnaceRecipe rr : recipes) {
            boolean res = Bukkit.addRecipe(rr);
            Bukkit.getLogger().info("Recipe registration: " + rr.getKey().getKey() + " " + (res ? "successful" : "failed"));
        }

        //TEST
        //(reduced only to gold)
        /*List<RefrigeratorRecipe> recipes = new ArrayList<>();
        recipes.add(new RefrigeratorRecipe(new NamespacedKey(Main.getPlugin(), "fun"), new ItemStack(Material.GOLD_ORE), Material.GOLD_INGOT, 0.07F, 200));

        for (RefrigeratorRecipe rr : recipes) {
            boolean res = Bukkit.addRecipe(rr);
            Bukkit.getLogger().info("Recipe registration: " + rr.getKey().getKey() + " " + (res ? "successful" : "failed"));
        }*/
    }

}
