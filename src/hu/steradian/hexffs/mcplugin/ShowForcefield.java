package hu.steradian.hexffs.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ShowForcefield implements Listener {
    public static enum Shape
    {
        DescartesSphere,
        FibonacciSphere,
        Cube,
        Pyramid
    }

    Shape shape = Shape.Cube;

    @EventHandler
    public void Toggle(PlayerInteractEvent event) {
        Bukkit.getLogger().info("event");
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            switch (shape) {
                case DescartesSphere -> {
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
                                    targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 10, .125, .125, .125, 0, dustOptions, true);
                                }
                            }
                        }
                    }.runTask(Main.getPlugin());
                }
                case FibonacciSphere -> {
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
                                targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 10, .125, .125, .125, 100, dustOptions, true);
                            }
                        }
                    }.runTask(Main.getPlugin());
                }
                case Cube -> {
                    new BukkitRunnable() {
                        Location targetLocation;
                        int edge = 20;
                        double r = 10, x, y, z;
                        @Override
                        public void run() {

                            for (int i = 0; i < edge; i++) {

                                x = Formula.trapezoid(i, 0, edge/4, edge/2, 3*edge/4, edge, edge/4) * r;
                                y = Formula.trapezoid(i, 0, edge/4, edge/2, 3*edge/4, edge, 0) * r;
                                z = Formula.trapezoid(i, 0, edge/4, edge/2, 3*edge/4, edge, 0) * r;

                                targetLocation = event.getClickedBlock().getLocation().clone();
                                targetLocation.add(x, y, z);
                                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, (int)r);
                                targetLocation.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 30, .125, .125, .125, 100, dustOptions, true);
                            }
                        }
                    }.runTask(Main.getPlugin());
                }
                case Pyramid -> {

                }
            }
        }
    }
}
