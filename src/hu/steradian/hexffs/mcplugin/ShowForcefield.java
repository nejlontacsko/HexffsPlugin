package hu.steradian.hexffs.mcplugin;

import org.bukkit.*;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static hu.steradian.hexffs.mcplugin.SignAdapter.*;

public class ShowForcefield implements Listener {
    public enum Shape
    {
        DescartesSphere,
        FibonacciSphere,
        Cube,
        Pyramid
    }

    Shape shape = Shape.DescartesSphere;
    int r = 20;

    private static void placeParticles(List<Location> locs, Color color, float r) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int counter = 0;
                for (Location l : locs) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) (r / 2.5));
                    l.getWorld().spawnParticle(Particle.REDSTONE, l, 4, .250, .250, .250, 1, dustOptions, true);
                    counter++;
                }
                Bukkit.getLogger().info("Forcefield revealed by " + counter + " pcs. " + color.toString() + " colored particles.");
            }
        }.runTask(Main.getPlugin());
    }

    public static void revealForcefield(Block block, Shape shape, Color color, float r) {
        new BukkitRunnable() {
            //Color color = DyeColor.getByWoolData(block.getData()).getColor();
            double x, y, z;
            List<Location> locs = new ArrayList<Location>();

            @Override
            public void run() {
                switch (shape) {
                    case DescartesSphere:
                        for (double i = -Math.PI; i <= Math.PI; i += Math.PI / 16) {
                            for (double j = -Math.PI; j < Math.PI; j += Math.PI / 32) {
                                x = r * Math.sin(i) * Math.cos(j);
                                y = r * Math.cos(i);
                                z = r * Math.sin(i) * Math.sin(j);
                                locs.add(block.getLocation().clone().add(x, y, z));
                            }
                        }
                        break;
                    case FibonacciSphere:
                        double phi = Math.PI * (3.0 - Math.sqrt(5.0)), theta, radius, samples = 499;
                        for (int i = 0; i <= samples; i++) {
                            y = 1 - (i / samples) * 2;
                            radius = Math.sqrt(1 - y * y) * r;
                            theta = phi * i;
                            x = Math.cos(theta) * radius;
                            z = Math.sin(theta) * radius;
                            locs.add(block.getLocation().clone().add(x, y * r, z));
                        }
                        break;
                    case Cube:
                        final int nodeAmount = 60;
                        final double a = 0, b = nodeAmount / 4, c = nodeAmount / 2, d = 3 * nodeAmount / 4;
                        for (double layer = -Math.PI; layer <= Math.PI; layer += Math.PI / b) {
                            y = r * Math.cos(layer);
                            for (int i = 0; i < nodeAmount; i++) {
                                x = Formula.trapezoid(i, a, b, c, d, nodeAmount, nodeAmount / 4) * r;
                                z = Formula.trapezoid(i, a, b, c, d, nodeAmount, a) * r;
                                locs.add(block.getLocation().clone().add(x, y, z));
                            }
                        }
                        break;
                    default:
                        Bukkit.getLogger().info("Selected shape is unimplemented in this plugin version.");
                        break;
                }
                placeParticles(locs, color, r);
            }
        }.runTask(Main.getPlugin());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            if (Tag.WOOL.isTagged(block.getBlockData().getMaterial())) {
                revealForcefield(block, shape, DyeColor.getByWoolData(block.getData()).getColor(), r);
            }
            else if (Tag.SIGNS.isTagged(block.getBlockData().getMaterial()))
            {
                Sign sign = (Sign)block.getState();
                Bukkit.getLogger().info("Sign rightclicked. " + getFullContent(sign));

                if (isHexFFsRelated(sign)) {
                    boolean valid = validateSign(sign);
                    updateText(sign, valid);
                    if (valid) {
                        switch (getCommand(sign)) {
                            case Radius:
                                Block attachedBlock = getAttachedBlock(block);
                                Block[] nextTo = {
                                        attachedBlock.getRelative(BlockFace.SOUTH),
                                        attachedBlock.getRelative(BlockFace.EAST),
                                        attachedBlock.getRelative(BlockFace.NORTH),
                                        attachedBlock.getRelative(BlockFace.WEST)};

                                boolean found = false;
                                int counter = 0;

                                for (Block b : nextTo) {
                                    if (b.getBlockData().getMaterial().equals(Material.REPEATER)) {
                                        found = true;

                                        BlockFace dir = ((Directional) b.getBlockData()).getFacing().getOppositeFace();
                                        Block curr = b;

                                        while (!curr.getRelative(dir).getType().isAir() && counter < 100) {
                                            curr = curr.getRelative(dir);
                                            counter++;
                                        }
                                        break;
                                    }
                                }

                                if (found) {
                                    String msg = "Radius set to §6" + counter + "§r.";
                                    if (counter == 100)
                                        msg += " (§eMaximum distance reached.§r)";
                                    event.getPlayer().sendRawMessage(msg);
                                    Bukkit.getLogger().info(msg);
                                    r = counter;
                                } else
                                    event.getPlayer().sendRawMessage("An attached §6Redstone Repeater§r required.");
                                break;
                            case Shape:
                                shape = Shape.valueOf(getArgument(sign));
                                event.getPlayer().sendRawMessage("Shape set to §6" + shape.toString() + "§r.");
                                break;
                            case ShowAll:
                                String argument = sign.getLine(2);
                                for (Node node : Main.getNodes()) {
                                    if (argument.length() > 0)
                                    {
                                        if (node.aspect == Aspect.valueOf(argument))
                                            node.revealForcefield();
                                    }
                                    else
                                        node.revealForcefield();
                                }
                                break;
                        }
                    }
                }
            }
            else if (block.getBlockData().getMaterial() == Material.BLUE_ICE) {
                Main.registerNode(new Node(Aspect.Water, block.getLocation(), 6));
            }
            else if (block.getBlockData().getMaterial() == Material.MOSS_BLOCK) {
                Main.registerNode(new Node(Aspect.Earth, block.getLocation(), 6));
            }
            else if (block.getBlockData().getMaterial() == Material.MAGMA_BLOCK) {
                Main.registerNode(new Node(Aspect.Fire, block.getLocation(), 6));
            }
            else if (block.getBlockData().getMaterial() == Material.GLASS) {
                Main.registerNode(new Node(Aspect.Air, block.getLocation(), 6));
            }
            else if (block.getBlockData().getMaterial() == Material.QUARTZ_BLOCK) {
                Main.registerNode(new Node(Aspect.Order, block.getLocation(), 6));
            }
            else if (block.getBlockData().getMaterial() == Material.DEEPSLATE) {
                Main.registerNode(new Node(Aspect.Chaos, block.getLocation(), 6));
            }
        }
    }
}
