package hu.steradian.hexffs.mcplugin.fun;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Coolant implements Listener {
    Material[] coolants = {
        Material.ICE,
        Material.PACKED_ICE,
        Material.BLUE_ICE,
        Material.SNOW,
        Material.SNOWBALL,
        Material.SNOW_BLOCK,
        Material.POWDER_SNOW,
        Material.POWDER_SNOW_BUCKET,
        Material.POWDER_SNOW_CAULDRON
    };

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getBlockData().getMaterial() == Material.FURNACE) {
                Furnace furnace = (Furnace) event.getClickedBlock().getState();
                ItemStack[] handItems = {
                        event.getPlayer().getInventory().getItemInMainHand(),
                        event.getPlayer().getInventory().getItemInOffHand()
                };
                for (ItemStack stack : handItems) {
                    if (Arrays.asList(coolants).contains(stack.getType())) {
                        FurnaceInventory fInv = furnace.getInventory();
                        Bukkit.getLogger().info("Furnace inventory: " + fInv.getFuel());
                        if (fInv.getFuel() == null) {
                            fInv.setFuel(stack);
                            stack.setAmount(0);
                        }
                        else if (fInv.getFuel().getType() == stack.getType()) {
                            int currAmount = fInv.getFuel().getAmount();
                            int stackAmount = stack.getAmount();
                            int addAmount = (currAmount + stackAmount > 64) ? (64 - currAmount) : stackAmount;
                            addAmount = (addAmount <= stackAmount) ? addAmount : stackAmount;
                            fInv.getFuel().setAmount(currAmount + addAmount);
                            stack.setAmount(stackAmount - addAmount);
                        }

                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRunOutFuel(FurnaceBurnEvent event)
    {
        event.setBurnTime(400);
    }

    @EventHandler
    public void onStartSmelt(FurnaceStartSmeltEvent event)
    {
        String key = event.getRecipe().getKey().toString();
        Bukkit.getLogger().info(key);
        Bukkit.getLogger().info(event.getBlock().getState().toString());
        if (key.contains("hexffs")) {
            Furnace furnace = (Furnace)event.getBlock().getState();
            if (!Arrays.asList(coolants).contains(furnace.getInventory().getFuel())) {
                furnace.setBurnTime((short) 0);
                furnace.update();
            }
        }

    }
}
