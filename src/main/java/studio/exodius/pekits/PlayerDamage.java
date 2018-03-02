package studio.exodius.pekits;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.utils.TextFormat;
import studio.exodius.pekits.utils.CombatTag;

/**
 * <p>Copyright (c) ExodiusMC, 2018. Property of Exodius Studios.</p>
 *
 * @author aleja
 * @version 1.0.0
 * @since 2/14/2018
 */
public class PlayerDamage {
    private Player player;
    private Entity killer;
    private String deathMsg;

    public PlayerDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) throw new IllegalStateException("Damaged entity must be player");

        this.player = (Player) e.getEntity();
        this.killer = null;

        this.deathMsg = PEKits.COMBAT_TAG + player.getName() + " ";

        switch(e.getCause()) {
            case ENTITY_ATTACK:
                killer = ((EntityDamageByEntityEvent) e).getDamager();

                deathMsg = deathMsg + TextFormat.GRAY + "was slain by " + killer.getName();

                if(killer instanceof Player) {
                    if(player.getInventory().getItemInHand().getId() != 0) {
                        deathMsg = deathMsg + " with " + (player.getInventory().getItemInHand().getCustomName());
                    }
                }
                break;
            case PROJECTILE:
                EntityDamageByEntityEvent _e = ((EntityDamageByEntityEvent) e);

                if(!(_e.getDamager() instanceof EntityProjectile)) return;
                Entity shooter = ((EntityProjectile) _e.getDamager()).shootingEntity;

                killer = shooter;

                deathMsg = deathMsg + TextFormat.GRAY + "was shot by " + killer.getName();
                break;
            case FALL:
                if(CombatTag.isTagged(player)) {
                    CombatTag tag = CombatTag.getTag(player);

                    if(tag.isDamagedByPlayer()) {
                        killer = tag.getDamager();
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed to their death by " + killer.getName();
                    } else {
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed to their death by " + tag.getDamager().getName();
                    }
                } else {
                    deathMsg = deathMsg + TextFormat.GRAY + "fell to their death";
                }
                break;
            case FIRE:
            case FIRE_TICK:
                if(CombatTag.isTagged(player)) {
                    CombatTag tag = CombatTag.getTag(player);

                    if(tag.isDamagedByPlayer()) {
                        killer = tag.getDamager();
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed into fire by " + killer.getName();
                    } else {
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed into fire by " + tag.getDamager().getName();
                    }
                } else {
                    deathMsg = deathMsg + TextFormat.GRAY + "turned to ash";
                }
                break;
            case DROWNING:
                deathMsg = deathMsg + TextFormat.GRAY + "drowned";
                break;
            case ENTITY_EXPLOSION:
            case BLOCK_EXPLOSION:
                if(e instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent __e = ((EntityDamageByEntityEvent) e);

                    if(__e.getDamager() instanceof EntityPrimedTNT) {
                        Entity igniter = ((EntityPrimedTNT) __e.getDamager()).getSource();

                        if(!(igniter instanceof Player)) return;
                        killer = igniter;
                    } else {
                        killer = __e.getDamager();
                    }

                    deathMsg = deathMsg + TextFormat.GRAY + "was exploded by " + killer.getName();
                    break;
                }

                deathMsg = deathMsg + TextFormat.GRAY + "exploded into pieces";
                break;
            case VOID:
                if(CombatTag.isTagged(player)) {
                    CombatTag tag = CombatTag.getTag(player);

                    if(tag.isDamagedByPlayer()) {
                        killer = tag.getDamager();
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed into oblivion by " + killer.getName();
                    } else {
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed into oblivion by " + tag.getDamager().getName();
                    }
                } else {
                    deathMsg = deathMsg + TextFormat.GRAY + "fell into oblivion";
                }

                break;
            case MAGIC:
                deathMsg = deathMsg + TextFormat.GRAY + "was killed by magic";
                break;
            case LAVA:
                if(CombatTag.isTagged(player)) {
                    CombatTag tag = CombatTag.getTag(player);

                    if(tag.isDamagedByPlayer()) {
                        killer = tag.getDamager();
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed onto magma by " + killer.getName();
                    } else {
                        deathMsg = deathMsg + TextFormat.GRAY + "was pushed onto magma by " + tag.getDamager().getName();
                    }
                } else {
                    deathMsg = deathMsg + TextFormat.GRAY + "burnt their feet on magma";
                }
                break;
            default:
                deathMsg = deathMsg + TextFormat.GRAY + "died";
        }
    }

    /**
     * Returns the player who died
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the entity who killed the victim
     *
     * @return Killer
     */
    public Entity getDamager() {
        return killer;
    }

    /**
     * Returns the death message
     *
     * @return TextBuilder
     */
    public String getDeathMsg() {
        return deathMsg;
    }
}
