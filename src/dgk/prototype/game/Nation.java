package dgk.prototype.game;

import dgk.prototype.gui.GUI;
import dgk.prototype.gui.GUIInGameMenu;
import dgk.prototype.gui.GUIMenu;

import java.io.Serializable;
import java.util.Random;

/**
 * This represents a Nation. If you start out as a 'Village', you are entitling yourself to a new Nation.
 * The player starts out as a 'Village' with minimal supplies and resources.
 */
public class Nation implements Serializable {

    /*

    "Native Metals. Gold, Silver and Copper are all examples of Native metals that naturally occur in a relatively pure state.
        Ancient man first found and began using Native Metals approximately 5000 years BC."


     */

    public static final long CURRENCY_GENERATION_TIMER = 1000L;

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

    // TEMPORARY
    private long startTime = -1L;

    public Nation(String name) {
        this.name = name;
        this.id = new Random().nextInt();
        this.populationCount = 1;
        this.deathCount = 1;
        this.gold = 100;
        this.woodResources = 100;
        this.stoneResources = 100;
        this.steelResources = 100;

        startTime = System.currentTimeMillis();
    }

    public void onUpdate() {
        long elapsed = System.currentTimeMillis() - startTime;

        if(elapsed >= CURRENCY_GENERATION_TIMER) {
            gold += 1;
            startTime = System.currentTimeMillis();
        }

        GUI gui = GameWindow.getInstance().gui;
        GUIMenu menu = gui.getMenu("Nation Menu");

        menu.getGUILabel("CurrencyLabel").setText("" + getGold());
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

    public int getPopulationCount() {
        return populationCount;
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getWoodResources() {
        return woodResources;
    }

    public void setWoodResources(int woodResources) {
        this.woodResources = woodResources;
    }

    public int getStoneResources() {
        return stoneResources;
    }

    public void setStoneResources(int stoneResources) {
        this.stoneResources = stoneResources;
    }

    public int getSteelResources() {
        return steelResources;
    }

    public void setSteelResources(int steelResources) {
        this.steelResources = steelResources;
    }
}
