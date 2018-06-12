package dgk.prototype.game;

public enum Rarity {

    NONE{
        double getChance() {
            return 0D;
        }
    },USELESS {
        double getChance() {
            return .9D;
        }
    },
    COMMON {
        double getChance() {
            return .85D;
        }
    },
    UNCOMMON {
        double getChance() {
            return .05D;
        }
    },
    RARE {
        double getChance() {
            return .01D;
        }
    },
    VERY_RARE {
        double getChance() {
            return .001D;
        }
    },
    EXTRAORDINARY {
        double getChance() {
            return .0001D;
        }
    };

    /**
     * The chance that matches the 'rarity' of each Enum.
     * @return The chance, perceived in percentage.
     */
    abstract double getChance();
}
