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
        String message = train.getTime(context)  + " " + train.getDestination();
        if(train.isExpress()){
            message += ", Running express.";
        }
        else{
            message += ", Stopping all stations.";
        }
        message += " Departing " + train.getDepartingFrom() + " in " + train.getMinutesToArrive() + ".";

        CardFragment fragment = CardFragment.create(title, message);
        fragment.setExpansionEnabled(true);
        fragment.setExpansionFactor(1);
        fragment.setExpansionDirection(CardFragment.EXPAND_UP);
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
        else if(destination.indexOf("southern cross") != -1){
            return context.getDrawable(R.raw.southern_cross_bg);
        }
        else if(destination.indexOf("sandringham") != -1){
            return context.getDrawable(R.raw.sandringham_bg);
        }
        else if(destination.indexOf("frankston") != -1){
            return context.getDrawable(R.raw.frankston_bg);
        }
        else if(destination.indexOf("stony point") != -1){
            return context.getDrawable(R.raw.stony_point_bg);
        }
        else if(destination.indexOf("pakenham") != -1){
            return context.getDrawable(R.raw.pakenham_bg);
        }
        else if(destination.indexOf("cranbourne") != -1){
            return context.getDrawable(R.raw.crainbourne_bg);
        }
        else if(destination.indexOf("glen waverley") != -1){
            return context.getDrawable(R.raw.glen_waverly_bg);
        }
        else if(destination.indexOf("alamein") != -1){
            return context.getDrawable(R.raw.alamein_bg);
        }
        else if(destination.indexOf("belgrave") != -1){
            return context.getDrawable(R.raw.belgrave_bg);
        }
        else if(destination.indexOf("lilydale") != -1){
            return context.getDrawable(R.raw.lilydale_bg);
        }
        else if(destination.indexOf("hurstbridge") != -1){
            return context.getDrawable(R.raw.hurstbridge_bg);
        }
        else if(destination.indexOf("south morang") != -1){
            return context.getDrawable(R.raw.south_morang_bg);
        }
        else if(destination.indexOf("upfield") != -1){
            return context.getDrawable(R.raw.upfield_bg);
        }
        else if(destination.indexOf("craigieburn") != -1){
            return context.getDrawable(R.raw.craigieburn_bg);
        }
        else if(destination.indexOf("sunbury") != -1){
            return context.getDrawable(R.raw.sunbury_bg);
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
