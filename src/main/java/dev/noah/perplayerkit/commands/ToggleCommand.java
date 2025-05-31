package dev.noah.perplayerkit.commands;

import dev.noah.perplayerkit.util.ToggleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand implements CommandExecutor {

    private final ToggleManager toggleManager;

    public ToggleCommand(Plugin plugin) {
        this.toggleManager = ToggleManager.getInstance(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("kit.use")) {
            return true;
        }

        if (args.length != 1) {
            return true;
        }

        String option = args[0].toLowerCase();

        switch (option) {
            case "autoback":
                toggleManager.setAutoBack(player, !toggleManager.isAutoBack(player));
                break;
            case "hotkey":
                toggleManager.setHotKey(player, !toggleManager.isHotKey(player));
                break;
            case "rekit_on_kill":
                toggleManager.setRekitOnKill(player, !toggleManager.isRekitOnKill(player));
                break;
            case "rekit_on_respawn":
                toggleManager.setRekitOnRespawn(player, !toggleManager.isRekitOnRespawn(player));
                break;
            default:
                return true;
        }
        return true;
    }
}