package au.com.lukesleeman.trainwatch;

import android.app.Activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import au.com.lukesleeman.utils.domain.Train;
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
        loadStuff();
    }

    @Background
    protected void loadStuff(){
        service.sendGetTrainsMessage();
//        showResults(service.getNextTrains());
    }

    @UiThread
    protected void showResults(List<Train> trains){
        TrainListActivity_.intent(this).trainList(trains).start();
        finish();
    }
}
