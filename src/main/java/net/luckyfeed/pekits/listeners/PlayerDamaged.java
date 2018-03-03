package net.luckyfeed.pekits.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.utils.TextFormat;
import net.luckyfeed.pekits.PEKits;
import net.luckyfeed.pekits.PlayerDamage;
import net.luckyfeed.pekits.utils.CombatTag;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class PlayerDamaged implements Listener {

    PEKits plugin;

    public PlayerDamaged(PEKits plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void on(EntityDamageEvent e){

        if(e.getEntity() instanceof Player){
            final Player player = (Player) e.getEntity();

            PlayerDamage dmg = new PlayerDamage(e);

            // Combat tag the player
            if (dmg.getDamager() != null) {
                if(dmg.getDamager() instanceof Player) {
                    if(!CombatTag.isTagged(player)) {
                        player.sendMessage(TextFormat.colorize(PEKits.COMBAT_TAG +
                            "&cYou " +
                            "&7have been combat tagged! " +
                            "&c&lDO NOT LOGOUT" +
                            ""));
                    }
                }
            }

            // When the player is going to die
            if(player.getHealth() - e.getFinalDamage() <= 0) {
                // Custom death messages
                plugin.getServer().broadcastMessage(dmg.getDeathMsg());

                if(CombatTag.isTagged(player))
                    CombatTag.getTag(player).cancel();
            }

            float victimHealth = player.getHealth();
            float victimFinalHealth = victimHealth-e.getFinalDamage();

            if(victimFinalHealth <= 0){
                e.setCancelled(true);
                player.setHealth(player.getMaxHealth());
                player.removeAllEffects();

                player.teleport(player.getLevel().getSpawnLocation());
            }
        }
    }
}
