import java.util.ArrayList;

/**
 * Created by jakub on 14.10.2016.
 */
public class HalfTSPResult_LocalSearch extends HalfTSPResult {
    private HalfTSPResult initialResult;

    public HalfTSPResult_LocalSearch(int distance, ArrayList<Integer> cycle, HalfTSPResult initialResult){
        super(distance, cycle);
        this.initialResult = initialResult;
    }

    public HalfTSPResult getInitialResult() {
        return initialResult;
    }

    public void setInitialResult(HalfTSPResult initialResult) {
        this.initialResult = initialResult;
    }
}
