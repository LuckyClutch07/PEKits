package net.luckyfeed.pekits.listeners;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import net.luckyfeed.pekits.events.ArmorEquipEvent;
import net.luckyfeed.pekits.utils.ArmorType;

import java.util.List;

public class ArmorListener implements Listener {

    private final List<Integer> blockedMaterials;

    public ArmorListener(List<Integer> blockedMaterials){
        this.blockedMaterials = blockedMaterials;
    }

    @EventHandler
    public final void onInventoryClick(final InventoryClickEvent e){
        boolean shift = false, numberkey = false;
        if(e.isCancelled()) return;
        if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
            shift = true;
        }
        if(e.getClick().equals(ClickType.NUMBER_KEY)){
            numberkey = true;
        }
        if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
        if(e.getInventory() != null && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
        if(e.getSourceItem() == null) return;
        ArmorType newArmorType = ArmorType.matchType(shift ? e.getSourceItem() : e.getHeldItem());
        if(!shift && newArmorType != null && e.getSlot() != newArmorType.getSlot()){
            // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots place.
            return;
        }
        if(shift){
            newArmorType = ArmorType.matchType(e.getSourceItem());
            if(newArmorType != null){
                boolean equipping = true;
                if(e.getSlot() == newArmorType.getSlot()){
                    equipping = false;
                }
                if(newArmorType.equals(ArmorType.HELMET) && (equipping ? e.getPlayer().getInventory().getHelmet() == null : e.getPlayer().getInventory().getHelmet() != null) || newArmorType.equals(ArmorType.CHESTPLATE) && (equipping ? e.getPlayer().getInventory().getChestplate() == null : e.getPlayer().getInventory().getChestplate() != null) || newArmorType.equals(ArmorType.LEGGINGS) && (equipping ? e.getPlayer().getInventory().getLeggings() == null : e.getPlayer().getInventory().getLeggings() != null) || newArmorType.equals(ArmorType.BOOTS) && (equipping ? e.getPlayer().getInventory().getBoots() == null : e.getPlayer().getInventory().getBoots() != null)){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getPlayer(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getSourceItem(), equipping ? e.getSourceItem() : null);
                    Nukkit.main().getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()){
                        e.setCancelled(true);
                    }
                }
            }
        }else{
            Item newArmorPiece = e.getHeldItem();
            Item oldArmorPiece = e.getSourceItem();
            if(numberkey){
                if(e.getInventory().getType().equals(InventoryType.PLAYER)){// Prevents shit in the 2by2 crafting
                    // e.getClickedInventory() == The players inventory
                    // e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
                    // e.getRawSlot() == The slot the item is going to.
                    // e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
                    Item hotbarItem = e.getInventory().getItem(e.getHotbarButton());
                    if(hotbarItem != null){// Equipping
                        newArmorType = ArmorType.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = e.getInventory().getItem(e.getSlot());
                    }else{// Unequipping
                        newArmorType = ArmorType.matchType(e.getHeldItem() != null && e.getHeldItem().getId() != 0 ? e.getHeldItem() : e.getHeldItem());
                    }
                }
            }else{
                // e.getCurrentItem() == Unequip
                // e.getCursor() == Equip
                newArmorType = ArmorType.matchType(e.getHeldItem() != null && e.getHeldItem().getId() != 0 ? e.getHeldItem() : e.getCursor());
            }
            if(newArmorType != null && e.getRawSlot() == newArmorType.getSlot()){
                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
                if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getPlayer(), method, newArmorType, oldArmorPiece, newArmorPiece);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.getAction() == PlayerInteractEvent.Action.PHYSICAL) return;
        if(e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK){
            final Player player = e.getPlayer();
            if(e.getBlock() != null && e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK){// Having both of these checks is useless, might as well do it though.
                // Some blocks have actions when you right click them which stops the client from equipping the armor in hand.
                int mat = e.getBlock().getId();
                for(int s : blockedMaterials){
                    if(mat == s) return;
                }
            }
            ArmorType newArmorType = ArmorType.matchType(e.getItem());
            if(newArmorType != null){
                if(newArmorType.equals(ArmorType.HELMET) && e.getPlayer().getInventory().getHelmet() == null || newArmorType.equals(ArmorType.CHESTPLATE) && e.getPlayer().getInventory().getChestplate() == null || newArmorType.equals(ArmorType.LEGGINGS) && e.getPlayer().getInventory().getLeggings() == null || newArmorType.equals(ArmorType.BOOTS) && e.getPlayer().getInventory().getBoots() == null){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()){
                        e.setCancelled(true);
                        player.updateInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void inventoryDrag(InventoryDragEvent event){
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorType type = ArmorType.matchType(event.getOldCursor());
        if(event.getRawSlots().isEmpty()) return;// Idk if this will ever happen
        if(type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)){
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                event.setResult(Result.DENY);
                event.setCancelled(true);
            }
        }
        // Debug shit
		/*System.out.println("Slots: " + event.getInventorySlots().toString());
		System.out.println("Raw Slots: " + event.getRawSlots().toString());
		if(event.getCursor() != null){
			System.out.println("Cursor: " + event.getCursor().getType().name());
		}
		if(event.getOldCursor() != null){
			System.out.println("OldCursor: " + event.getOldCursor().getType().name());
		}
		System.out.println("Type: " + event.getType().name());*/
    }

	/*@EventHandler
	public void dispenserFireEvent(BlockDispenseEvent e){
		ArmorType type = ArmorType.matchType(e.getItem());
		if(type != null){
			org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) e.getBlock().getState();
			org.bukkit.material.Dispenser dispenserData = (org.bukkit.material.Dispenser) dispenser.getData();
			Location loc = shift(e.getBlock().getLocation(), dispenserData.getFacing(), 1);
			List<EntityLiving> list = ((CraftWorld) loc.getWorld()).getHandle().a(EntityLiving.class, new AxisAlignedBB(new BlockPosition(loc.getX(), loc.getY(), loc.getZ())), Predicates.and(IEntitySelector.e, new IEntitySelector.EntitySelectorEquipable(CraftItemStack.asNMSCopy(e.getItem()))));
			if(list.isEmpty()) return;
			EntityLiving ent = list.get(0);
			if(ent instanceof EntityPlayer){
				Player p = (Player) ent.getBukkitEntity();
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, EquipMethod.DISPENSER, type, null, e.getItem());
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
				if(armorEquipEvent.isCancelled()){
					e.setCancelled(true);
				}
			}
		}
	}*/

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent e){
        ArmorType type = ArmorType.matchType(e.getBrokenItem());
        if(type != null){
            Player p = e.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                ItemStack i = e.getBrokenItem().clone();
                i.setAmount(1);
                i.setDurability((short) (i.getDurability() - 1));
                if(type.equals(ArmorType.HELMET)){
                    p.getInventory().setHelmet(i);
                }else if(type.equals(ArmorType.CHESTPLATE)){
                    p.getInventory().setChestplate(i);
                }else if(type.equals(ArmorType.LEGGINGS)){
                    p.getInventory().setLeggings(i);
                }else if(type.equals(ArmorType.BOOTS)){
                    p.getInventory().setBoots(i);
                }
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(i != null && !i.getType().equals(Material.AIR)){
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, EquipMethod.DEATH, ArmorType.matchType(i), i, null));
                // No way to cancel a death event.
            }
        }
    }

    private Location shift(Location start, BlockFace direction, int multiplier){
        if(multiplier == 0) return start;
        return new Location(start.getWorld(), start.getX() + direction.getModX() * multiplier, start.getY() + direction.getModY() * multiplier, start.getZ() + direction.getModZ() * multiplier);
    }
}