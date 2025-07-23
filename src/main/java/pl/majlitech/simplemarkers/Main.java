package pl.majlitech.simplemarkers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public final class Main extends JavaPlugin implements CommandExecutor, org.bukkit.command.TabCompleter {
    private FileConfiguration messages;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);
        messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        getLogger().info("SimpleMarkers plugin has been enabled!");

        // Register commands, events, etc. here
        getCommand("sm").setExecutor(this);
        getCommand("sm").setTabCompleter(this);

    }

    // Helper method to check if a permission is enabled in config
    private boolean isPermissionEnabled(String perm) {
        return getConfig().getBoolean("permissions." + perm, true);
    }
    private String msg(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.getString(key, key));
    }

    private String msg(String key, Map<String, String> replacements) {
        String message = msg(key);
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "delete").stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(msg("usage")+"/sm <create|delete>");
            return true;
        }
        if (!sender.hasPermission("simplemarkers.use") || !isPermissionEnabled("use")) {
            sender.sendMessage(msg("no-permission"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(msg("only-player"));
            return true;
        }
        Player player = (Player) sender;
        Map<String, String> replacements = new HashMap<>();

        switch (args[0]) {
            case "create":
                if (!player.hasPermission("simplemarkers.create") || !isPermissionEnabled("create")) {
                    player.sendMessage(msg("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(msg("usage")+"/sm create <floating text, use \n for newline, & for color, check minecraft.tools for color codes>");
                    return true;
                }
                Location baseLocation = player.getLocation().add(0, 1.8, 0); // Spawn marker 1.5 blocks above the ground (player's eyes
                // Join all args from index 1 onwards with spaces
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) sb.append(" ");
                }

                String input = sb.toString().replace("\\n", "\n");

                String[] lines = input.split("\\n"); // split by literal "\n"

                double lineHeight = 0.3;
                for (int i = 0; i < lines.length; i++) {
                    Location lineLoc = baseLocation.clone().add(0, -i * lineHeight, 0);
                    ArmorStand lineStand = player.getWorld().spawn(lineLoc, ArmorStand.class);
                    lineStand.setMarker(true);
                    lineStand.setInvulnerable(true);
                    lineStand.setGravity(false);
                    lineStand.setVisible(false);
                    lineStand.setCustomName(lines[i].replace("&", "§"));
                    lineStand.setCustomNameVisible(true);
                    lineStand.addScoreboardTag("simplemarkers.marker");
                }
                replacements.put("count", String.valueOf(lines.length));
                player.sendMessage(msg("marker-created", replacements));
                player.sendMessage(msg("warning-kill"));
                break;
            case "delete":
                if (!player.hasPermission("simplemarkers.delete") || !isPermissionEnabled("delete")) {
                    player.sendMessage(msg("no-permission"));
                    return true;
                }
                int deleted = 0;
                for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
                    if (entity.getScoreboardTags().contains("simplemarkers.marker") && entity instanceof ArmorStand) {
                        entity.remove();
                        deleted++;
                    }
                }
                if (deleted==0) {
                    player.sendMessage(msg("no-marker-found"));
                } else {
                    replacements.put("count", String.valueOf(deleted));
                    player.sendMessage(msg("marker-deleted", replacements));

                }
                break;
            default:
                sender.sendMessage(msg("usage")+"/sm <create|delete>");




                break;
        }
        return true;
    }
}
