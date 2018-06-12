package dgk.prototype.game;

import java.awt.*;

public class WallComponent extends BuildingComponent {

    private static final int MAX_SUB_COMPONENTS = 2;

    private Tile[] subComponents;

    public WallComponent(ComponentType type, byte renderLayer, int x, int y, int size, Direction initDirection) {
        super(0, renderLayer, x, y, size, initDirection);

        this.subComponents = new Tile[MAX_SUB_COMPONENTS];

        this.subComponents[0] = new Tile(0, renderLayer, x, y + 48, size);
        this.subComponents[1] = new Tile(0, renderLayer, x, y - 48, size);

        setType(type);
        onDirectionChange();
    }

    @Override
    public void render() {
        super.render();

        for(Tile t : subComponents) {
            if(t != null) {
                t.render();
            }
        }
    }

    @Override
    void onDirectionChange() {
        if(getType() == ComponentType.WOOD) {
            switch (getDirection()) {
                case NORTH:
                    this.textureId = 22;
                    subComponents[0].textureId = 23;
                    subComponents[1].textureId = 24;
                    break;
                case SOUTH:
                    System.out.println("Not supported yet D:");
                    break;
                case EAST:
                    System.out.println("Not supported yet D:");
                    break;
                case WEST:
                    System.out.println("Not supported yet D:");
                    break;
            }
        }else if(getType() == ComponentType.WOOD) {
            switch (getDirection()) {
                case NORTH:
                    System.out.println("Not supported yet D:");
                    break;
                case SOUTH:
                    System.out.println("Not supported yet D:");
                    break;
                case EAST:
                    System.out.println("Not supported yet D:");
                    break;
                case WEST:
                    System.out.println("Not supported yet D:");
                    break;
            }
        }
    }
}
