package dgk.prototype.game.tile;

public class Node {

    // TODO: When implementing the HashSet, override the hashCode();

    private Tile tile;

    /**
     * The distance (in grid coordinates) from this Tile
     * to the Start tile.
     */
    private int gScore;
    /**
     * The calculated distance from this Tile to the goal Tile.
     */
    private int hScore;

    /**
     * The total score, weighted by: fScore = gScore + hScore
     */
    private int fScore;

    public Node(Tile tile, Tile end, int gScore, boolean isGoal) {
        this.tile = tile;
        this.gScore = gScore;

        if(isGoal) {
            this.hScore = (int) end.getPosition().getDistance(tile.getPosition());
        }else {
            this.hScore = (int) tile.getPosition().getDistance(end.getPosition());
        }

        calculateFScore();
    }

    public Tile getTile() {
        return this.tile;
    }

    public int getGridX() {
        return getTile().getGridX();
    }

    public int getGridY() {
        return getTile().getGridY();
    }

    public void calculateFScore() {
        this.fScore = getGScore() + getHScore();
    }

    public int getGScore() {
        return gScore;
    }

    private int getHScore() {
        return hScore;
    }

    public int getFScore() {
        return fScore;
    }

    @Override
    public boolean equals(Object o1) {
        if(!(o1 instanceof Node))
            return false;

        Node n = (Node) o1;

        return n.getTile().equals(this.getTile());
    }
}
