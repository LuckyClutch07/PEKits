package net.luckyfeed.pekits.kits;

import cn.nukkit.item.Item;
import net.luckyfeed.pekits.Requirement;

import java.util.List;

/**
 * <p>Copyright (c) LUCKYFEED, 2018.</p>
 *
 * @author LuckyClutch
 * @version 1.0.0
 * @since 3/1/2018
 */
public interface KitHandler {
    List<Item> items();
    List<Requirement> requirements();
}
