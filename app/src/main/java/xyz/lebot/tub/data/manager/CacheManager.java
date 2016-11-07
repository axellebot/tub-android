package xyz.lebot.tub.data.manager;

import xyz.lebot.tub.data.model.LineModel;
import xyz.lebot.tub.data.model.StopModel;

import java.util.List;

/**
 * Created by axell on 04/11/2016.
 */

public interface CacheManager {
    List<LineModel> getLines();

    void setLines(List<LineModel> lineModels);

    LineModel getLine(String id);

    void setLine(LineModel lineModel);

    List<StopModel> getStops();

    void setStops(List<StopModel> stopModels);

    StopModel getStop(String id);

    void setStop(StopModel stopModel);
}