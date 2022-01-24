package hu.steradian.hexffs.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

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

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Bukkit.getLogger().info("event");
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            switch (shape) {
                case DescartesSphere:
                    new BukkitRunnable() {
                        Location targetLocation;
                        double r = 20, x, y, z;

                        @Override
                        public void run() {
                            for (double i = -Math.PI; i <= Math.PI; i += Math.PI / 16) {
                                for (double j = -Math.PI; j < Math.PI; j += Math.PI / 32) {
                                    x = r * Math.sin(i) * Math.cos(j);
                                    y = r * Math.cos(i);
                                    z = r * Math.sin(i) * Math.sin(j);
                                    targetLocation = event.getClickedBlock().getLocation().clone();
                                    targetLocation.add(x, y, z);
                                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, (float) r);
                                    targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 3, .125, .125, .125, 0, dustOptions, true);
                                }
                            }
                        }
                    }.runTask(Main.getPlugin());
                    break;
                case FibonacciSphere:
                    new BukkitRunnable() {
                        Location targetLocation;
                        double r = 20, x, y, z;

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
                                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, (float) r);
                                targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 3, .125, .125, .125, 100, dustOptions, true);
                            }
                        }
                    }.runTask(Main.getPlugin());
                    break;
                case Cube:
                    new BukkitRunnable() {
                        Location targetLocation;
                        int nodeCount = 60;
                        double r = 20, x, y, z, step = nodeCount/4;
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

                                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, (int) r);
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
    }
}
