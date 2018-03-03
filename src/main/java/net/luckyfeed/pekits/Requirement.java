package net.luckyfeed.pekits;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Copyright (c) LUCKYFEED, 2018.</p>
 *
 * @author LuckyClutch
 * @version 1.0.0
 * @since 3/1/2018
 */
public class Requirement {

    @Getter @Setter private RequirementType type;
    @Getter @Setter private int amount;

    public Requirement(RequirementType type, int amount){
        this.type = type;
        this.amount = amount;
    }

    public enum RequirementType {
        XP,
        MONEY,
        LEVEL
    }
}
