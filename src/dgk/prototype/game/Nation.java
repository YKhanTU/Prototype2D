package dgk.prototype.game;

import java.io.Serializable;
import java.util.Random;

/**
 * This represents a Nation. If you start out as a 'Village', you are entitling yourself to a new Nation.
 * The player starts out as a 'Village' with minimal supplies and resources.
 */
public class Nation implements Serializable {

    private String name;

    private int id;

    /**
     * The population count. This will be extrapolated from the # of citizens this Nation holds.
     */
    private int populationCount;

    /**
     * The death count. The amount of people that have died in this nation.
     */
    private int deathCount;

    /**
     * The amount of currency that this city has.
     */
    private int gold;

    private int woodResources;
    private int stoneResources;
    private int steelResources;

    public Nation(String name) {
        this.name = name;
        this.id = new Random().nextInt();
        this.populationCount = 1;
        this.deathCount = 1;
        this.gold = 100;
        this.woodResources = 100;
        this.stoneResources = 100;
        this.steelResources = 100;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
