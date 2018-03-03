package net.luckyfeed.pekits.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import net.luckyfeed.pekits.PEKits;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class PlayerJoin implements Listener {

    PEKits plugin;

    public PlayerJoin(PEKits plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerJoinEvent event){
        Player player = event.getPlayer();

        Item item = new Item(1, 0, 64);
        item.setCustomName(TextFormat.AQUA+"Shit");

        player.getInventory().addItem(item);
        player.sendMessage(TextFormat.colorize("&6&oWelcome to &a&lLuckyFEED &b&lSpace &6&ostation! Choose a world to travel to!"));
    }
}
