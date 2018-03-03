package net.luckyfeed.pekits.utils;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableInt;
import net.luckyfeed.pekits.PEKits;

import java.util.Map;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class CombatTag {

    PEKits plugin;

    public CombatTag(PEKits plugin) {
        this.plugin =plugin;
    }

    private static Map<Player, CombatTag> tags;

    static {
        tags = Maps.newHashMap();
    }

    private Player player;
    private Entity damager;
    private TaskHandler task;

    private CombatTag(Player player, Entity damager, int ticks, final Runnable handler) {
        this.player = player;
        this.damager = damager;
        player.sendActionBar(TextFormat.colorize("&7Combat tagged by: &c" + damager.getName() + " &7for &e"+String.valueOf(ticks/20) + " seconds."), 10, 60, 10);

        final Player p = player;
        final Entity dmgr = damager;
        final int ts = ticks;
        final MutableInt curr = new MutableInt();

        task = plugin.getServer().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                curr.increment();

                if(curr.getValue() >= ts) {
                    task.cancel();
                    this.cancel();

                    handler.run();
                    p.sendMessage(TextFormat.colorize(PEKits.COMBAT_TAG +
                        "&cYou " +
                        "&7are no longer in combat"));
                    return;
                }

                p.sendActionBar(TextFormat.colorize("&7Combat tagged by: &c" + dmgr.getName() + " &7for &e"+String.valueOf(ts/20) + " seconds."), 10, 60, 10);
            }
        },20);
    }

    /**
     * Returns the player associated with this tag
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the entity who damaged
     *
     * @return Entity
     */
    public Entity getDamager() {
        return damager;
    }

    /**
     * Returns true when the player was hit by another player. This method is
     * only present as shortcut.
     *
     * @return Entity
     */
    public boolean isDamagedByPlayer() {
        return damager instanceof Player;
    }

    /**
     * Cancel the current combat tag
     */
    public void cancel() {
        // Forget
        if (tags.containsKey(player)) tags.remove(player);

        // Remove task
        if (!task.isCancelled()) this.task.cancel();
    }

    /**
     * Creates and stores a new tag set on the specified player. The amount of ticks define
     * how long this tag will last.
     *
     * @param player  Player to tag
     * @param damager Damager
     * @param ticks   Ticks to last
     * @return CombatTag
     */
    public static CombatTag tag(Player player, Entity damager, int ticks) {
        return tag(player, damager, ticks, null);
    }

    /**
     * Creates and stores a new tag set on the specified player. The amount of ticks define
     * how long this tag will last. The callback handler is called when the tag expires.
     *
     * @param player  Player to tag
     * @param damager Damager
     * @param ticks   Ticks to last
     * @param handler Callback handler
     * @return CombatTag
     */
    public static CombatTag tag(Player player, Entity damager, int ticks, Runnable handler) {
        // Cancel & remove the existing tag
        CombatTag existing = tags.get(player);

        if (existing != null) {
            existing.cancel();
        }

        // Create the tag
        CombatTag tag = new CombatTag(player, damager, ticks * 20, handler);

        // Store
        tags.put(player, tag);

        return tag;
    }

    /**
     * Returns true when the player is currently tagged
     *
     * @param player Player
     * @return Boolean
     */
    public static boolean isTagged(Player player) {
        return tags.containsKey(player);
    }

    /**
     * Returns the Combat Tag for the specified player
     *
     * @param player Player
     * @return Combat Tag. null if player is currently untagged
     */
    public static CombatTag getTag(Player player) {
        return tags.get(player);
    }

}