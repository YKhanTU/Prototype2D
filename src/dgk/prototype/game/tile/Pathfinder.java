package dgk.prototype.game.tile;

import dgk.prototype.game.Entity;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.entities.Person;
import dgk.prototype.util.Vec2D;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

/**
 * The Pathfinder class. This is used to help find the fastest route
 * from Tile A to Tile B. Basically parkour simulated by a computer.
 */
public class Pathfinder {

    // The Closed set is the 'final path'
    // The Open set is the 'potential' Nodes that we check to see
    // if they are eligible for constructing the fastest route.

    /*

    TODO: Look for diagonal tiles - #1 improvement
    TODO: Remove the currentNode (lowestScoreNode) from the Open Set

     */

    private TileMap tileMap;
    private Entity entity;

    private ArrayList<Node> openSet;
    private ArrayList<Node> closedSet;

    private ArrayList<Node> path;

    private Node endNode;

    private boolean isDiagonal;

    public Pathfinder(TileMap tileMap, Entity e, Tile start, Tile end, boolean isDiagonal) {
        this.tileMap = tileMap;
        this.entity = e;

        Node startNode = new Node(start, end, 0, false);
        endNode = new Node(end, start, 0, true);

        this.openSet = new ArrayList<Node>();
        this.closedSet = new ArrayList<Node>();
        this.path = new ArrayList<Node>();

        this.openSet.add(startNode);

        this.isDiagonal = isDiagonal;
    }

    public void constructFastestPath() {
        while(openSet.size() > 0) {

            int lowestScore = Integer.MAX_VALUE;
            Node lowestScoreNode = null;

            for(int i = 0; i < openSet.size(); i++) {
                Node currentNode = openSet.get(i);

                if(currentNode.getFScore() < lowestScore) {
                    lowestScore = currentNode.getFScore();
                    lowestScoreNode = currentNode;
                }
            }

            if(lowestScoreNode.equals(endNode)) {
                System.out.println("Path found!");
                break;
            }

            if(lowestScoreNode != null) {
                openSet.remove(lowestScoreNode);

                //ArrayList<Node> adjacentNodes = getAdjacentTiles(lowestScoreNode);

                Node[] adjacentNodes = getAdjacentTiles(lowestScoreNode);

                for(Node node : adjacentNodes) {
                    if(node == null)
                        continue;

                    if(isInClosedSet(node))
                        continue;

                    if(!isInOpenSet(node)) {
                        openSet.add(node);
                    }

                    if(lowestScoreNode.getFScore() >= node.getFScore())
                        continue;
                }

                closedSet.add(lowestScoreNode);
            }else{
                System.out.println("Error finding path!");
            }
        }

        for(int i = 0; i < closedSet.size(); i++) {
            path.add(closedSet.get(i));
        }

        path.add(endNode);

        for(int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            //System.out.print("[" + n.getGridX() + ", " + n.getGridY() + "], ");
        }

        System.out.println();
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    /**
     * Gives the found 'path' in a stack, allowing for a more efficient way to
     * use the path.
     * @return
     */
    public Stack<Node> getPathAsStack() {
        if(path != null) {
            Stack<Node> nodeStack = new Stack<Node>();

            Collections.reverse(path);

            for(Node n : path) {
                nodeStack.push(n);
            }

            return nodeStack;
        }

        return null;
    }

    public int getPathSize() {
        if(closedSet.size() > 0) {
            return closedSet.size();
        }

        return -1;
    }

    public int getOpenSetSize() {
        return openSet.size();
    }

    private boolean isInClosedSet(Node node) {
        if(closedSet.contains(node)) {
            return true;
        }

        return false;
    }

    private boolean isInOpenSet(Node node) {
        if(openSet.contains(node)) {
            return true;
        }

        return false;
    }

    /**
     * Returns an array of Nodes of the adjacent tiles. (Not diagonal)
     * @param node
     * @return Array of length 4 containing an instance of a Node, a length of 8
     * if the Pathfinder isDiagonal, or Null (if no adjacent tiles are found!)
     */
    public Node[] getAdjacentTiles(Node node) {
        int gridX = node.getGridX();
        int gridY = node.getGridY();

        Node[] adjacentTiles;

        if(isDiagonal) {
            adjacentTiles = new Node[8];
        }else {
            adjacentTiles = new Node[4];
        }

        // RIGHT TILE
        if(tileMap.getTile(gridX + 1, gridY) != null) {
            Tile tile = tileMap.getTile(gridX + 1, gridY);

            Node right = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles[0] = right;
        }
        // LEFT TILE
        if(tileMap.getTile(gridX - 1, gridY) != null) {
            Tile tile = tileMap.getTile(gridX - 1, gridY);

            Node left = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles[1] = left;
        }
        // BOTTOM TILE
        if(tileMap.getTile(gridX, gridY + 1) != null) {
            Tile tile = tileMap.getTile(gridX, gridY + 1);

            Node top = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles[2] = top;
        }
        // TOP TILE
        if(tileMap.getTile(gridX, gridY - 1) != null) {
            Tile tile = tileMap.getTile(gridX, gridY - 1);

            Node bottom = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles[3] = bottom;
        }

        if(isDiagonal) {
            // TOP LEFT
            if(tileMap.getTile(gridX - 1, gridY - 1) != null) {
                Tile tile = tileMap.getTile(gridX - 1, gridY - 1);

                Node topLeft = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
                adjacentTiles[4] = topLeft;
            }
            // TOP RIGHT
            if(tileMap.getTile(gridX + 1, gridY - 1) != null) {
                Tile tile = tileMap.getTile(gridX + 1, gridY - 1);

                Node topRight = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
                adjacentTiles[5] = topRight;
            }
            // BOTTOM LEFT
            if(tileMap.getTile(gridX - 1, gridY + 1) != null) {
                Tile tile = tileMap.getTile(gridX - 1, gridY + 1);

                Node bottomLeft = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
                adjacentTiles[6] = bottomLeft;
            }
            // BOTTOM RIGHT
            if(tileMap.getTile(gridX + 1, gridY + 1) != null) {
                Tile tile = tileMap.getTile(gridX + 1, gridY + 1);

                Node topLeft = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
                adjacentTiles[7] = topLeft;
            }
        }

        ArrayList<GameObject> gameObjects = tileMap.getGameObjects();

        for(GameObject go : gameObjects) {
            // Eventually remove this. VERY dangerous.
            if(!go.isInCameraView()) {
                continue;
            }

            if(go.getPosition().getDistance(new Vec2D(gridX * TileMap.TILE_SIZE, gridY * TileMap.TILE_SIZE)) > 128) {
                continue;
            }

            if(go instanceof Tile) {
                Tile tile = (Tile) go;

                Pair<Vec2D, Vec2D> vec2DPair = tile.getCoveredTiles();

                Vec2D min = vec2DPair.getKey();
                Vec2D max = vec2DPair.getValue();

                for(int i = 0; i < adjacentTiles.length; i++) {
                    Node n = adjacentTiles[i];

                    if(n == null) {
                        continue;
                    }

                    Tile t = n.getTile();

                    if((t.getGridX() == min.getX() && t.getGridY() == min.getY())
                            || (t.getGridX() == max.getX() && t.getGridY() == max.getY())) {
                        adjacentTiles[i] = null;
                        System.out.println("Removed tile from pathfinder");
                    }
                }
            }else{
                continue;
            }
        }

        for(Entity e : GameWindow.getInstance().world.getEntities()) {
            if(e.equals(entity)) {
                continue;
            }

            if(e instanceof Person) {
                Vec2D bottom = e.getBottomOfEntity();

                int bgX = (int) Math.floor(bottom.getX() / TileMap.TILE_SIZE);
                int bgY = (int) Math.floor(bottom.getY() / TileMap.TILE_SIZE);

                for(int i = 0; i < adjacentTiles.length; i++) {
                    Node n = adjacentTiles[i];

                    if(n == null) {
                        continue;
                    }

                    Tile t = n.getTile();

                    if((t.getGridX() == bgX && t.getGridY() == bgY)
                            || (t.getGridX() == bgX && t.getGridY() == bgY)) {
                        adjacentTiles[i] = null;
                        System.out.println("Removed Entity collision from pathfinder.");
                    }
                }

            }else{
                // TEMPORARY
                continue;
            }
        }

        return adjacentTiles;
    }

    public boolean isDiagonal() {
        return isDiagonal;
    }

    public Node getEndNode() {
        return endNode;
    }
}
