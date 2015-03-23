package au.com.lukesleeman.trainwatch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

import au.com.lukesleeman.utils.domain.Train;

/**
 * Adapter for creating a list of trains leaving a station.
 */
public class TrainListPagerAdapter extends FragmentGridPagerAdapter {

    private final Context context;
    private List<Train> trainList;

    public TrainListPagerAdapter(Context ctx, FragmentManager fm, List<Train> trainList) {
        super(fm);
        context = ctx;
        this.trainList = trainList;
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {

        Train train = trainList.get(row);

        String title = train.getMinutesToArrive() + " - " + train.getDestination();
        String message = train.getTime()  + " " + train.getDestination();
        if(train.isExpress()){
            message += "Running express";
        }
        else{
            message += "Stopping all stations";
        }

        CardFragment fragment = CardFragment.create(title, message);
        return fragment;
    }

    // Obtain the background image for the page at the specified position
    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        String destination = trainList.get(row).getDestination().toLowerCase();
        if(destination.indexOf("flinders") != -1){
            return context.getDrawable(R.raw.flinders_bg);
        }
        else if(destination.indexOf("werribee") != -1){
            return context.getDrawable(R.raw.werribee_bg);
        }
        else if(destination.indexOf("williamstown") != -1){
            return context.getDrawable(R.raw.williamstown_bg);
        }
        else {
            return context.getDrawable(R.raw.train_bg);
        }
    }

    @Override
    public int getRowCount() {
        return trainList.size();
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return 1;
    }
}
