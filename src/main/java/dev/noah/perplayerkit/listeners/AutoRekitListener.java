/*
 * Copyright 2022-2025 Noah Ross
 *
 * This file is part of PerPlayerKit.
 *
 * PerPlayerKit is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * PerPlayerKit is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with PerPlayerKit. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.noah.perplayerkit.listeners;

import dev.noah.perplayerkit.KitManager;
import dev.noah.perplayerkit.PerPlayerKit;
import dev.noah.perplayerkit.util.CooldownManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;

public class AutoRekitListener implements Listener {

    private final Plugin plugin;
    private final CooldownManager cooldown = new CooldownManager(1);

    public AutoRekitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        String rekitOnRespawn = "rekit_on_respawn";
        boolean isRekitOnRespawn = PerPlayerKit.storageManager.getToggleState(uuid, rekitOnRespawn);

        String autoBack = "autoback";
        boolean isAutoBack = PerPlayerKit.storageManager.getToggleState(uuid, autoBack);

        if (isRekitOnRespawn) {
            if (e.getPlayer().hasPermission("kit.use"))
                KitManager.get().loadLastKit(player);
        }

        if (isAutoBack) {
        Location deathLocation = player.getLastDeathLocation();
            if (deathLocation != null) {
                e.setRespawnLocation(deathLocation);
            }
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = player.getKiller();

        if (killer == null || !killer.hasPermission("kit.use")) {
            return;
        }

        String uuid = killer.getUniqueId().toString();
        String rekitOnKill = "rekit_on_kill";

        boolean isRekitOnKill = PerPlayerKit.storageManager.getToggleState(uuid, rekitOnKill);
        if (!isRekitOnKill) {
            return;
        }

        KitManager.get().loadLastKit(killer);
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        String hotKey = "hotkey";
        boolean isHotKey = PerPlayerKit.storageManager.getToggleState(uuid, hotKey);

        if (!isHotKey) {
            return;
        }

        if (!player.isSneaking()) return;

        if (cooldown.isOnCooldown(player)) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);

        if (player.hasPermission("kit.use")) {
            KitManager.get().loadLastKit(player);
            cooldown.setCooldown(player);
        }
    }
}
