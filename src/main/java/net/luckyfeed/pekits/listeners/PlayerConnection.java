package net.luckyfeed.pekits.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import net.luckyfeed.pekits.PEKits;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class PlayerConnection implements Listener {

    PEKits plugin;

    public PlayerConnection(PEKits plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();


    }
}
