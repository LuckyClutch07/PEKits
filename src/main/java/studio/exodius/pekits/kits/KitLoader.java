package studio.exodius.pekits.kits;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.io.File;
import java.util.List;

/**
 * <p>Copyright (c) LUCKYFEED, 2018.</p>
 *
 * @author LuckyClutch
 * @version 1.0.0
 * @since 3/1/2018
 */
public class KitLoader implements KitHandler {

    File kitsFile = new File("plugins/PEKits/kits.yml");
    Config kitConfig = new Config(kitsFile, Config.YAML);

    public List<Item> items() {
        ConfigSection

        return null;
    }
}
