package dev.noah.perplayerkit.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ToggleManager {

    private final Plugin plugin;

    private final NamespacedKey rekitOnRespawnKey;
    private final NamespacedKey hotKeyKey;
    private final NamespacedKey autoBackKey;
    private final NamespacedKey rekitOnKillKey;

    private static ToggleManager instance;

    private ToggleManager(Plugin plugin) {
        this.plugin = plugin;
        this.rekitOnRespawnKey = new NamespacedKey(plugin, "rekit_on_respawn");
        this.hotKeyKey = new NamespacedKey(plugin, "hotkey");
        this.autoBackKey = new NamespacedKey(plugin, "auto_back");
        this.rekitOnKillKey = new NamespacedKey(plugin, "rekit_on_kill");
    }

    public static ToggleManager getInstance(Plugin plugin) {
        if (instance == null) {
            instance = new ToggleManager(plugin);
        }
        return instance;
    }

    public static ToggleManager get(Player player) {
        return instance;
    }

    private boolean getToggle(Player player, NamespacedKey key) {
        Byte value = player.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        if (key.equals(autoBackKey)) {
            return value != null && value == 1;
        }

        return value == null || value == 1;
    }

    private void setToggle(Player player, NamespacedKey key, boolean value) {
        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
    }

    public boolean isRekitOnRespawn(Player player) {
        return getToggle(player, rekitOnRespawnKey);
    }

    public boolean isHotKey(Player player) {
        return getToggle(player, hotKeyKey);
    }

    public boolean isAutoBack(Player player) {
        return getToggle(player, autoBackKey);
    }

    public boolean isRekitOnKill(Player player) {
        return getToggle(player, rekitOnKillKey);
    }

    public void setRekitOnRespawn(Player player, boolean enabled) {
        setToggle(player, rekitOnRespawnKey, enabled);
    }

    public void setHotKey(Player player, boolean enabled) {
        setToggle(player, hotKeyKey, enabled);
    }

    public void setAutoBack(Player player, boolean enabled) {
        setToggle(player, autoBackKey, enabled);
    }

    public void setRekitOnKill(Player player, boolean enabled) {
        setToggle(player, rekitOnKillKey, enabled);
    }
}