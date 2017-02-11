package fr.bourgmapper.tub.presentation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

import fr.bourgmapper.tub.presentation.internal.di.components.ApplicationComponent;

/**
 * Android Main Application
 */
public class AndroidApplication extends MultiDexApplication {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeDBFlow();
        this.initializeLeakDetection();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }


    private void initializeDBFlow() {
        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(DBFlowModuleGeneratedDataBaseHolder.class)
                .build());
        // add for verbose logging
        // FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
