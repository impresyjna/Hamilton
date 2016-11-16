import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Created by jakub on 15.11.2016.
 */
public class HalfTSPResult_HybridEvolutionAlgorithm extends HalfTSPResult {

    private Dictionary<Integer, Pair<Long, Integer>> chartData;

    public HalfTSPResult_HybridEvolutionAlgorithm(int distance, ArrayList<Integer> path, Dictionary<Integer, Pair<Long, Integer>> chartData) {
        super(distance, path);
        this.chartData = chartData;
    }

    public Dictionary<Integer, Pair<Long, Integer>> getChartData() {
        return chartData;
    }

    public void setChartData(Dictionary<Integer, Pair<Long, Integer>> chartData) {
        this.chartData = chartData;
    }
}
