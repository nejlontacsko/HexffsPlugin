package hu.steradian.hexffs.mcplugin;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class Node extends BukkitRunnable {
    Aspect aspect;
    Location loc;
    Color color;
    int size;
    int activity; // particle~mana spawn rate

    public Node(Aspect aspect, Location location, int size)
    {
        this.aspect = aspect;
        this.loc = location;
        loc.add(0.5, 0.5, 0.5);
        this.size = size;
        runTaskTimer(Main.getPlugin(), 4, 10-size);
    }

    @Override
    public void run() {
        switch (aspect)
        {
            case Fire:
                loc.getWorld().spawnParticle(Particle.SMALL_FLAME, loc, 8, .250, .250, .250, .05);
                loc.getWorld().spawnParticle(Particle.DRIP_LAVA, loc, 8, .250, .250, .250);
                loc.getWorld().spawnParticle(Particle.FALLING_LAVA, loc, 8, .250, .250, .250);
                color = Color.RED;
                break;
            case Earth:
                loc.getWorld().spawnParticle(Particle.COMPOSTER, loc, 12, .250, .250, .250);
                loc.getWorld().spawnParticle(Particle.FALLING_SPORE_BLOSSOM, loc, 12, .250, .250, .250);
                loc.getWorld().spawnParticle(Particle.SLIME, loc, 3, .250, .250, .250);
                color = Color.GREEN;
                break;
            case Water:
                loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc, 5, .250, .250, .250, .05);
                loc.getWorld().spawnParticle(Particle.DRIP_WATER, loc, 5, .250, .250, .250);
                loc.getWorld().spawnParticle(Particle.FALLING_WATER, loc, 5, .250, .250, .250);
                color = Color.AQUA;
                break;
            case Air:
                loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1, .250, .250, .250, 0);
                loc.getWorld().spawnParticle(Particle.CLOUD, loc, 2, .250, .250, .250, 0);
                loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 5, .250, .250, .250, .01);
                color = Color.WHITE;
                break;
            case Order:
                loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 12, .250, .250, .250, .75);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 2, .250, .250, .250, .05);
                loc.getWorld().spawnParticle(Particle.FALLING_NECTAR, loc, 10, .250, .250, .250, 0);
                color = Color.YELLOW;
                break;
            case Chaos:
                loc.getWorld().spawnParticle(Particle.CRIMSON_SPORE, loc, 6, .250, .250, .250, 0);
                loc.getWorld().spawnParticle(Particle.SOUL, loc, 5, .250, .250, .250, .06);
                loc.getWorld().spawnParticle(Particle.ASH, loc, 100, .250, .250, .250);
                color = Color.MAROON;
                break;
            default:
                break;
        }
    }

    public void revealForcefield()
    {
        new BukkitRunnable() {
            @Override
            public void run() {

                ShowForcefield.revealForcefield(loc.getBlock(), ShowForcefield.Shape.FibonacciSphere, color, size * 3);
            }
        }.runTask(Main.getPlugin());
    }
}
