package fr.bourgmapper.tub.data.repository;

import java.io.InputStream;
import java.util.List;

import fr.bourgmapper.tub.TubApp;
import fr.bourgmapper.tub.data.entity.LineEntity;
import fr.bourgmapper.tub.data.entity.StopEntity;
import fr.bourgmapper.tub.data.entity.mapper.LineDataMapper;
import fr.bourgmapper.tub.data.entity.mapper.StopDataMapper;
import fr.bourgmapper.tub.data.manager.ApiManager;
import fr.bourgmapper.tub.data.manager.DBFlowManager;
import fr.bourgmapper.tub.data.manager.DownloadManager;
import fr.bourgmapper.tub.presentation.model.LineModel;
import fr.bourgmapper.tub.presentation.model.StopModel;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by axell on 04/11/2016.
 */

//TODO : Add TTL to cache

public class DataRepositoryImpl implements DataRepository {


    private final long ttl_incrementation = 60000;
    private long TTL_STOP;
    private long TTL_LINE;

    private ApiManager apiManager;
    private DBFlowManager dbFlowManager;
    private DownloadManager downloadManager;
    private LineDataMapper lineDataMapper;
    private StopDataMapper stopDataMapper;

    public DataRepositoryImpl(ApiManager apiManager, DBFlowManager dbFlowManager, DownloadManager downloadManager, LineDataMapper lineDataMapper, StopDataMapper stopDataMapper) {
        this.apiManager = apiManager;
        this.dbFlowManager = dbFlowManager;
        this.downloadManager = downloadManager;
        this.lineDataMapper = lineDataMapper;
        this.stopDataMapper = stopDataMapper;

        resetTTLs();
    }

    @Override
    public Observable<List<LineModel>> getLineListCall() {
        Observable<List<LineModel>> lineModelListObservable = null;

        if (TubApp.app().isNetworkAvailable() && lineTTLExpired()) {
            lineModelListObservable = apiManager.getAllLines().map(new Func1<List<LineEntity>, List<LineModel>>() {
                @Override
                public List<LineModel> call(List<LineEntity> lineEntities) {
                    List<LineModel> lineModels = lineDataMapper.transform(lineEntities);
                    dbFlowManager.saveLineEntityList(lineEntities);
                    updateTTLLine();
                    return lineModels;
                }
            });
        } else {
            lineModelListObservable = Observable.just(
                    lineDataMapper.transform(dbFlowManager.getLineEntityList())
            );
        }

        return lineModelListObservable;
    }

    @Override
    public Observable<LineModel> getLineCall(String lineId) {
        Observable<LineModel> lineModelObservable = null;

        if (TubApp.app().isNetworkAvailable() && lineTTLExpired()) {
            lineModelObservable = apiManager.getLine(lineId).map(new Func1<LineEntity, LineModel>() {
                @Override
                public LineModel call(LineEntity lineEntity) {
                    LineModel lineModel = lineDataMapper.transform(lineEntity);
                    dbFlowManager.saveLineEntity(lineEntity);
                    updateTTLLine();
                    return lineModel;
                }
            });
        } else {
            lineModelObservable = Observable.just(
                    lineDataMapper.transform(dbFlowManager.getLineEntity(lineId))
            );
        }
        return lineModelObservable;
    }

    @Override
    public Observable<List<LineModel>> getLineListFromStop(String stopId) {
        return apiManager.getLineListFromStop(stopId).map(new Func1<List<LineEntity>, List<LineModel>>() {
            @Override
            public List<LineModel> call(List<LineEntity> lineEntityList) {
                List<LineModel> lineModelList = lineDataMapper.transform(lineEntityList);
                dbFlowManager.saveLineEntityList(lineEntityList);
                return lineModelList;
            }
        });
    }

    @Override
    public Observable<List<StopModel>> getStopListCall() {
        Observable<List<StopModel>> stopModelListObservable = null;
        if (TubApp.app().isNetworkAvailable() && stopTTLExpired()) {
            stopModelListObservable = apiManager.getAllStops().map(new Func1<List<StopEntity>, List<StopModel>>() {
                @Override
                public List<StopModel> call(List<StopEntity> stopEntityList) {
                    List<StopModel> stopModelList = stopDataMapper.transform(stopEntityList);
                    dbFlowManager.saveStopEntityList(stopEntityList);
                    updateTTLStop();
                    return stopModelList;
                }
            });
        } else {
            stopModelListObservable = Observable.just(
                    stopDataMapper.transform(dbFlowManager.getStopEntityList())
            );
        }
        return stopModelListObservable;
    }

    @Override
    public Observable<StopModel> getStopCall(String stopId) {
        Observable<StopModel> stopModelObservable = null;

        if (TubApp.app().isNetworkAvailable() && stopTTLExpired()) {
            stopModelObservable = apiManager.getStop(stopId).map(new Func1<StopEntity, StopModel>() {
                @Override
                public StopModel call(StopEntity stopEntity) {
                    StopModel stopModel = stopDataMapper.transform(stopEntity);
                    dbFlowManager.saveStopEntity(stopEntity);
                    updateTTLStop();
                    return stopModel;
                }
            });
        } else {
            stopModelObservable = Observable.just(
                    stopDataMapper.transform(dbFlowManager.getStopEntity(stopId))
            );
        }
        return stopModelObservable;
    }

    @Override
    public Observable<List<StopModel>> getStopListFromLine(String lineId) {
        return apiManager.getStopsFromLine(lineId).map(new Func1<List<StopEntity>, List<StopModel>>() {
            @Override
            public List<StopModel> call(List<StopEntity> stopEntities) {
                List<StopModel> stopModels = stopDataMapper.transform(stopEntities);
                dbFlowManager.saveStopEntityList(stopEntities);
                return stopModels;
            }
        });
    }

    @Override
    public Observable<InputStream> getLineKMLCall(String lineId) {
        return this.downloadManager.getLineKmlFile(lineId).map(new Func1<ResponseBody, InputStream>() {
            @Override
            public InputStream call(ResponseBody responseBody) {
                return responseBody.byteStream();
            }
        });
    }

    private boolean lineTTLExpired() {
        return TTL_LINE < getTimeStamp();
    }

    private boolean stopTTLExpired() {
        return TTL_STOP < getTimeStamp();
    }

    private void updateTTLLine() {
        TTL_LINE = getTimeStamp() + ttl_incrementation;
    }

    private void updateTTLStop() {
        TTL_STOP = getTimeStamp() + ttl_incrementation;
    }

    private long getTimeStamp() {
        long tsLong = System.currentTimeMillis() / 1000;
        return tsLong;
    }

    public void resetTTLs() {
        TTL_LINE = 0;
        TTL_STOP = 0;
    }
}
