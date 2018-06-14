package dgk.prototype.game.tile;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Pathfinder class. This is used to help find the fastest route
 * from Tile A to Tile B. Basically parkour simulated by a computer.
 */
public class Pathfinder {

    // The Closed set is the 'final path'
    // The Open set is the 'potential' Nodes that we check to see
    // if they are eligible for constructing the fastest route.

    private TileMap tileMap;

    private ArrayList<Node> openSet;
    private ArrayList<Node> closedSet;

    private ArrayList<Node> path;

    private Node endNode;

    public Pathfinder(TileMap tileMap, Tile start, Tile end) {
        this.tileMap = tileMap;

        Node startNode = new Node(start, end, 0, false);
        endNode = new Node(end, start, 0, true);

        this.openSet = new ArrayList<Node>();
        this.closedSet = new ArrayList<Node>();
        this.path = new ArrayList<Node>();

        this.openSet.add(startNode);
    }

    public void constructFastestPath() {

        int count = 0;

        while(openSet.size() > 0) {

            count++;
            System.out.println(count);

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

                ArrayList<Node> adjacentNodes = getAdjacentTiles(lowestScoreNode);

                System.out.println("Adjacent Node Size: " + adjacentNodes.size());

                for(Node node : adjacentNodes) {
                    if(isInClosedSet(node))
                        continue;

                    if(!isInOpenSet(node)) {
                        openSet.add(node);
                    }

                    if(lowestScoreNode.getFScore() >= node.getFScore())
                        continue;

                    path.add(lowestScoreNode);
                }

                closedSet.add(lowestScoreNode);
            }else{
                System.out.println("Error finding path!");
            }
        }
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

    public ArrayList<Node> getAdjacentTiles(Node node) {
        int gridX = node.getGridX();
        int gridY = node.getGridY();

        ArrayList<Node> adjacentTiles = new ArrayList<Node>();

        // RIGHT TILE
        if(tileMap.getTile(gridX + 1, gridY) != null) {
            Tile tile = tileMap.getTile(gridX + 1, gridY);

            Node right = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles.add(right);
        }
        // LEFT TILE
        if(tileMap.getTile(gridX - 1, gridY) != null) {
            Tile tile = tileMap.getTile(gridX - 1, gridY);

            Node left = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles.add(left);
        }
        // TOP TILE
        if(tileMap.getTile(gridX, gridY + 1) != null) {
            Tile tile = tileMap.getTile(gridX, gridY + 1);

            Node top = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles.add(top);
        }
        // BOTTOM TILE
        if(tileMap.getTile(gridX, gridY - 1) != null) {
            Tile tile = tileMap.getTile(gridX, gridY - 1);

            Node bottom = new Node(tile, endNode.getTile(), node.getGScore() + 1, false);
            adjacentTiles.add(bottom);
        }

        return adjacentTiles;
    }
}
