package dev.noah.perplayerkit.papi;

import dev.noah.perplayerkit.util.ToggleManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerPlayerKitExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "ross noah";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "perplayerkit";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        ToggleManager toggleManager = ToggleManager.get(player);

        return switch (params.toLowerCase()) {
            case "autoback" -> toggleManager.isAutoBack(player) ? "On" : "Off";
            case "hotkey" -> toggleManager.isHotKey(player) ? "On" : "Off";
            case "rekit_on_kill" -> toggleManager.isRekitOnKill(player) ? "On" : "Off";
            case "rekit_on_respawn" -> toggleManager.isRekitOnRespawn(player) ? "On" : "Off";
            default -> "Invalid";
        };
    }
}
