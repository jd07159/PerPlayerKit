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

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import dev.noah.perplayerkit.KitManager;
import dev.noah.perplayerkit.util.DisabledCommand;
import dev.noah.perplayerkit.util.ToggleManager;
import dev.noah.perplayerkit.util.CooldownManager;
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
    public void onPostRespawn(PlayerPostRespawnEvent e) {
        Player player = e.getPlayer();
        ToggleManager toggleManager = ToggleManager.get(player);

        if (DisabledCommand.isBlockedInWorld(player)) {
            return;
        }

        if (!toggleManager.isRekitOnRespawn(player)) {
            return;
        }

        if (!e.getPlayer().hasPermission("kit.use")) {
            return;
        }

        KitManager.get().loadLastKit(e.getPlayer());

    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        ToggleManager toggleManager = ToggleManager.get(player);

        if (DisabledCommand.isBlockedInWorld(player)) {
            return;
        }

        if (!toggleManager.isHotKey(player)) {
            return;
        }
        if (!player.hasPermission("kit.use")) {
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        if (this.cooldown.isOnCooldown(player)) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        KitManager.get().loadLastKit(player);
        this.cooldown.setCooldown(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        ToggleManager toggleManager = ToggleManager.get(player);

        if (DisabledCommand.isBlockedInWorld(player)) {
            return;
        }

        if (!toggleManager.isAutoBack(player)) {
            return;
        }

        if (!e.getPlayer().hasPermission("kit.use")) {
            return;
        }

        if (player.getLastDeathLocation() != null) {
            e.setRespawnLocation(player.getLastDeathLocation());
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = player.getKiller();

        if (killer == player || killer == null) {
            return;
        }

        if (DisabledCommand.isBlockedInWorld(killer)) {
            return;
        }

        if (!killer.hasPermission("kit.use")) {
            return;
        }

        ToggleManager toggleManager = ToggleManager.get(killer);

        if (!toggleManager.isRekitOnKill(killer)) {
            return;
        }

        KitManager.get().loadLastKit(killer);
    }
}