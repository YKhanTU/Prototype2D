package dgk.prototype.game;

import dgk.prototype.game.tile.*;
import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public abstract class Entity implements IEntity, Serializable {

    protected static final double DEFAULT_MOVEMENT_SPEED = 2.0;

    private int textureId;

    private String name;

    protected Vec2D position;
    protected Vec2D velocity;

    private Direction direction;
    private CharacterState state;

    private Inventory inventory;

    private int healthPoints;
    private int armorPoints;

    private double movementSpeed;

    private transient IEntity target;

    public boolean isMoving = false;

    /**
     * This tells us whether or not this Entity is controlled by the Player.
     */
    protected boolean hasController = false;

    /**
     * The assigned pathfinder instance for this Entity.
     */
    protected Pathfinder pathFinder = null;

    /**
     * Current path given by the Pathfinder.
     */
    public Stack<Node> currentPath = null;
    /**
     * For when the player is pathfinding. The current node is the node they
     * are currently walking towards for a task, or simply given a 'Move' command.
     */
    public Node currentNode = null;

    /**
     * This is for remembering/storing the last position of the player temporarily in order
     * to resolve collisions
     */
    public boolean isPathComplete = false;

    /**
     * This value just tells us if we are colliding with an object or not.
     */
    public boolean isCollidingWithSomething = false;

    protected Vec2D lastPosition;

    /**
     * Represents if this Entity has been selected or not by the user.
     */
    protected boolean isSelected;

    public Entity(int textureId, String name, double x, double y) {
        this.textureId = textureId;
        this.position = new Vec2D(x, y);
        this.velocity = new Vec2D();
        this.isSelected = false;

        this.name = name;

        this.direction = Direction.SOUTH;

        this.inventory = new Inventory(24, false);

        this.state = CharacterState.IDLE;

        this.healthPoints = 100;
        this.armorPoints = 0;

        this.movementSpeed = DEFAULT_MOVEMENT_SPEED;

        this.target = null;

        this.lastPosition = getPosition();
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public Vec2D getVelocity() {
        return this.velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getHealthPoints() {
        return this.healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if(healthPoints < 0) {
            this.healthPoints = 0;
            return;
        }

        this.healthPoints = healthPoints;
    }

    public boolean isDead() {
        return (healthPoints <= 0);
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public void setArmorPoints(int armorPoints) {
        if(armorPoints < 0) {
            this.armorPoints = 0;
            return;
        }

        this.armorPoints = armorPoints;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isIdle() {
        return !isMoving;
    }

    public IEntity getTarget() {
        return target;
    }

    public void setTarget(IEntity target) {
        this.target = target;
    }

    public boolean hasTarget() {
        return (target == null) ? false : true;
    }

    private boolean isInCombat() {
        return hasTarget();
    }

    public boolean hasArmor() {
        return this.getArmorPoints() > 0;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Checks for collision between Entities, then checks for Collision between this Entity and Tiles.
     * @param world
     */
    protected void checkForCollision(World world) {
        List<Entity> entityList = world.getEntities();
        List<GameObject> gameObjects = world.getTileMap().getGameObjects();

        for(Entity e : entityList) {
            if(e.equals(this))
                continue;

            if(this.getPosition().getDistance(e.getPosition()) > (128))
                continue;

            if(this.getAABB().isIntersecting(e.getAABB())) {
                this.position = lastPosition;

                isCollidingWithSomething = true;

                return;
            }
        }

        for(GameObject gameObject : gameObjects) {
            if(gameObject instanceof Tile) {
                Tile tile = (Tile) gameObject;

                if(tile.isPassable()) {
                    continue;
                }
            }

            if(this.getAABB().isIntersecting(gameObject.getAABB())) {
                this.position = lastPosition;

                return;
            }
        }

        isCollidingWithSomething = false;
    }

    private void walkToNode(Tile tile) {
        if(currentNode == null) {
            return;
        }

        //int tX = tile.getGridX();
        //int tY = tile.getGridY();
        int tX = this.getGridX();
        int tY = this.getGridY();
        int nX = currentNode.getGridX();
        int nY = currentNode.getGridY();

        // Move Up if it is higher
        if(nY < tY) {
            setDirection(Direction.NORTH);
            getVelocity().setX(0);
            getVelocity().setY(-2D);
        }
        // Move Down
        else if(nY > tY) {
            setDirection(Direction.SOUTH);
            getVelocity().setX(0);
            getVelocity().setY(2D);
        }
        // Move Left
        else if(nX < tX) {
            setDirection(Direction.WEST);
            getVelocity().setX(-2D);
            getVelocity().setY(0);
        }
        // Move Right
        else if(nX > tX) {
            setDirection(Direction.EAST);
            getVelocity().setX(2D);
            getVelocity().setY(0);
        }

        isMoving = true;
    }

    @Override
    public void onUpdate() {
        int gridX = this.getGridX();
        int gridY = this.getGridY();

        Tile currentTile = GameWindow.getInstance().world.getTileMap().getTile(gridX, gridY);

        if (currentNode != null) {
            if (currentNode.getGridX() == gridX && currentNode.getGridY() == gridY) {

                Node endNode = pathFinder.getEndNode();

                if (currentTile.equals(endNode.getTile())) {
                    if (!isPathComplete) {
                        System.out.println("We have completed the path.");
                        isPathComplete = true;
                    }
                }else{
                    currentNode = currentPath.pop();
                }
            }
        }

        if (currentPath != null) {
            if (currentPath.empty() && isPathComplete) {
                currentPath = null;
                currentNode = null;
                isMoving = false;

                System.out.println("TileMap Selections Reset");
                GameWindow.getInstance().world.getTileMap().resetTileSelections();
            } else {
                if(isCollidingWithSomething) {
                    pathFinder = new Pathfinder(GameWindow.getInstance().world.getTileMap(), currentNode.getTile(), pathFinder.getEndNode().getTile(), false);
                    pathFinder.constructFastestPath();
                    currentPath = pathFinder.getPathAsStack();
                    currentNode = currentPath.pop();
                    System.out.println("Re-routing!");

                    isCollidingWithSomething = false;
                }

                if(currentPath != null) {
                    for(Node n : currentPath) {
                        GameWindow.getInstance().world.getTileMap().onTileSelection(n.getTile());
                    }
                }

                walkToNode(currentTile);
            }
        }
    }

    public void goToTile(Tile goal) {
        if(currentNode != null) {
            return;
        }

        int gridX = this.getGridX();
        int gridY = this.getGridY();

        TileMap tileMap = GameWindow.getInstance().world.getTileMap();
        Tile tile = tileMap.getTile(gridX, gridY);

        if(tile == null) {
            System.out.println("Invalid tile (or your Entity is outside of the proper World bounds!");

            return;
        }

        setNewPath(tile, goal);
    }

    protected Vec2D getBottomOfEntity() {
        AABB aabb = this.getAABB();

        return new Vec2D(aabb.getMin().getX() + 32, aabb.getMax().getY());
    }

    public int getGridX() {
        return ((int) Math.floor(getBottomOfEntity().getX() / TileMap.TILE_SIZE));
    }

    public int getGridY() {
        return ((int) Math.floor(getBottomOfEntity().getY() / TileMap.TILE_SIZE));
    }

    /**
     * Sets the path of the Entity to a new one, and makes them walk that path.
     * This pathfinding is done with the A* algorithm.
     * @param start
     * @param end
     */
    private void setNewPath(Tile start, Tile end) {
        pathFinder = new Pathfinder(GameWindow.getInstance().world.getTileMap(), start, end, false);

        pathFinder.constructFastestPath();
        currentPath = pathFinder.getPathAsStack();
        currentNode = currentPath.pop();

        isPathComplete = false;
    }

}
