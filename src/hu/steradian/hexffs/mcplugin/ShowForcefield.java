package hu.steradian.hexffs.mcplugin;

import org.bukkit.*;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ShowForcefield implements Listener {
    public enum Shape
    {
        DescartesSphere,
        FibonacciSphere,
        Cube,
        Pyramid
    }

    Shape shape = Shape.FibonacciSphere;
    int r = 20;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            if (Tag.WOOL.isTagged(block.getBlockData().getMaterial())) {
                Color color = DyeColor.getByWoolData(block.getData()).getColor();
                switch (shape) {
                    case DescartesSphere:
                        new BukkitRunnable() {
                            Location targetLocation;
                            double x;
                            double y;
                            double z;

                            @Override
                            public void run() {
                                for (double i = -Math.PI; i <= Math.PI; i += Math.PI / 16) {
                                    for (double j = -Math.PI; j < Math.PI; j += Math.PI / 32) {
                                        x = r * Math.sin(i) * Math.cos(j);
                                        y = r * Math.cos(i);
                                        z = r * Math.sin(i) * Math.sin(j);
                                        targetLocation = block.getLocation().clone();
                                        targetLocation.add(x, y, z);
                                        Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) r);
                                        targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 3, .125, .125, .125, 0, dustOptions, true);
                                    }
                                }
                            }
                        }.runTask(Main.getPlugin());
                        break;
                    case FibonacciSphere:
                        new BukkitRunnable() {
                            Location targetLocation;
                            double x;
                            double y;
                            double z;

                            @Override
                            public void run() {
                                double phi = Math.PI * (3.0 - Math.sqrt(5.0)), theta, radius;
                                int samples = 1000;
                                for (int i = 0; i < samples; i++) {
                                    y = (1 - (i / (float) (samples - 1)) * 2);
                                    radius = Math.sqrt(1 - y * y) * r;
                                    theta = phi * i;

                                    x = Math.cos(theta) * radius;
                                    z = Math.sin(theta) * radius;

                                    targetLocation = event.getClickedBlock().getLocation().clone();
                                    targetLocation.add(x, y * r, z);
                                    Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) r);
                                    targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 3, .125, .125, .125, 100, dustOptions, true);
                                }
                            }
                        }.runTask(Main.getPlugin());
                        break;
                    case Cube:
                        new BukkitRunnable() {
                            Location targetLocation;
                            final int nodeCount = 60;
                            double x;
                            double y;
                            double z;
                            final double step = nodeCount / 4;

                            @Override
                            public void run() {
                                //for (double layer = -r; layer <= r; layer += step) {
                                for (double layer = -Math.PI; layer <= Math.PI; layer += Math.PI / step) {
                                    y = r * Math.cos(layer);
                                    for (int i = 0; i < nodeCount; i++) {
                                        x = Formula.trapezoid(i, 0, nodeCount / 4, nodeCount / 2, 3 * nodeCount / 4, nodeCount, nodeCount / 4) * r;
                                        z = Formula.trapezoid(i, 0, nodeCount / 4, nodeCount / 2, 3 * nodeCount / 4, nodeCount, 0) * r;

                                        targetLocation = event.getClickedBlock().getLocation().clone();
                                        targetLocation.add(x, y, z);

                                        Particle.DustOptions dustOptions = new Particle.DustOptions(color, (int) r);
                                        targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 3, .125, .125, .125, 10, dustOptions, true);
                                    }
                                    Bukkit.getLogger().info("y: " + y + "; loc: " + targetLocation.toString());
                                }
                            }
                        }.runTask(Main.getPlugin());
                        break;
                    case Pyramid:
                        break;
                }
            }
            else if (Tag.SIGNS.isTagged(block.getBlockData().getMaterial()))
            {
                Bukkit.getLogger().info("SIGNS TAG");
                org.bukkit.block.Sign signBlock = (org.bukkit.block.Sign)block.getState();
                Bukkit.getLogger().info("SIGN: " + signBlock);
                Bukkit.getLogger().info("content: " + signBlock.getLine(1));

                if (signBlock.getLine(1).contains("radius")) {
                    BlockFace attachedFace = ((Directional) block.getBlockData()).getFacing().getOppositeFace();
                    Bukkit.getLogger().info("ATTACHED FACE: " + attachedFace);
                    Block attachedBlock = block.getRelative(attachedFace);
                    Block[] nextTo = {
                        attachedBlock.getRelative(BlockFace.SOUTH),
                        attachedBlock.getRelative(BlockFace.EAST),
                        attachedBlock.getRelative(BlockFace.NORTH),
                        attachedBlock.getRelative(BlockFace.WEST)};
                    for (Block b : nextTo) {
                        Bukkit.getLogger().info("CURRENT 'b': " + b.getBlockData().getMaterial().toString());
                        if (b.getBlockData().getMaterial().equals(Material.REPEATER)) {
                        //if (b.getType().getData().getName().contains("magenta_glazed")) {
                            int counter = 0;
                            BlockFace dir = ((Directional) b.getBlockData()).getFacing().getOppositeFace();
                            Bukkit.getLogger().info("DIR: " + dir);
                            Block curr = b;
                            while (!curr.getRelative(dir).getType().isAir())
                            {
                                Bukkit.getLogger().info("COUNTER: " + counter++);
                                curr = curr.getRelative(dir);
                            }

                            event.getPlayer().sendRawMessage("Radius sat to " + counter + " .");
                            r = counter;

                            break;
                        }
                    }
                }
                else {
                    try {
                        shape = Shape.valueOf(signBlock.getLine(1));
                    } catch (Exception ex) {
                        event.getPlayer().sendRawMessage(ex.getMessage());
                        shape = Shape.FibonacciSphere;
                    }
                }
            }
        }
    }
}
