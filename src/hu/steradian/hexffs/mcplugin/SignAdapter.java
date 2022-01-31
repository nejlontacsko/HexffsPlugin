package hu.steradian.hexffs.mcplugin;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;

public class SignAdapter {

    public enum SignCommands {
        Shape,
        ShowAll,
        Radius
    }

    public static Boolean isHexFFsRelated(Sign sign) {
        String s = sign.getLine(0);
        return s.startsWith("[") ? s.contains("HexFFS") : false;
    }

    public static boolean validateSign(Sign sign) {
        SignCommands command;
        try {
            command = SignCommands.valueOf(sign.getLine(1));
        } catch (Exception ex) {
            return false;
        }

        String argument = sign.getLine(2);
        switch (command)
        {
            case Shape:
                try {
                    ShowForcefield.Shape.valueOf(argument);
                } catch (Exception ex) {
                    return false;
                }
                return true;
            case ShowAll:
                if (argument.length() > 0) {
                    try {
                        Aspect.valueOf(argument);
                    } catch (Exception ex) {
                        return false;
                    }
                }
                return true;
            default:
                return true;
        }
    }

    public static void updateText(Sign sign, Boolean valid) {
        sign.setLine(0, "[§" + (valid ? "9" : "c") + "HexFFS§r]");
        sign.setGlowingText(valid);
        sign.update();
    }

    public static SignCommands getCommand(Sign sign) {
        return SignCommands.valueOf(sign.getLine(1));
    }

    public static String getArgument(Sign sign) {
        return sign.getLine(2);
    }

    private static BlockFace getAttachedFace(Block sign) {
        return (sign.getBlockData() instanceof Directional) ?
                ((Directional) sign.getBlockData()).getFacing().getOppositeFace() : BlockFace.DOWN;
    }

    public static Block getAttachedBlock(Block sign) {
        return sign.getRelative(getAttachedFace(sign));
    }

    public static String getFullContent(Sign sign) {
        return "Content: " + String.join(" ", sign.getLines());
    }
}
