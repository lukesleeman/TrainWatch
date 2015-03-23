package au.com.lukesleeman.trainwatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import au.com.lukesleeman.trainwatch.domain.Train;
import au.com.lukesleeman.trainwatch.service.TrainWatchService;

/**
 * Displays a loading screen while we do all the stuff - get a location, fetch trains, etc
 */
@EActivity(R.layout.activity_loading)
public class LoadingActivity extends Activity {

    @Bean
    protected TrainWatchService service;

    @AfterViews
    protected void setupUi(){
//        loadStuff();

        service.sendGetTrainsMessage();

    }

    @Background
    protected void loadStuff(){
        showResults(service.getNextTrains());
    }

    @UiThread
    protected void showResults(List<Train> trains){
        TrainListActivity_.intent(this).trainList(trains).start();
        finish();
    }
}
