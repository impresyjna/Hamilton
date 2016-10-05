import java.util.ArrayList;

/**
 * Created by Jakub on 05.10.2016.
 */
public class HalfTSPResult {
    private int distance;
    private ArrayList<Integer> path;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }
}
