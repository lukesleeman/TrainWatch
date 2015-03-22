package au.com.lukesleeman.trainwatch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

import au.com.lukesleeman.trainwatch.domain.Train;

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

//    static final int[] BG_IMAGES = new int[] {
//            R.drawable.debug_background_1, ...
//    R.drawable.debug_background_5
//};

//
//// Create a static set of pages in a 2D array
//private final Page[][] PAGES = { ... };
//
//        // Override methods in FragmentGridPagerAdapter
//        ...
//        }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {

        /*
        Page page = PAGES[row][col];
        String title =
        page.titleRes != 0 ? context.getString(page.titleRes) : null;
        String text =
        page.textRes != 0 ? context.getString(page.textRes) : null;
        */
        Train train = trainList.get(row);

        CardFragment fragment = CardFragment.create(train.getDestination(), train.getMinutesToArrive() + " " + train.getPaltform());

        // Advanced settings (card gravity, card expansion/scrolling)
//        fragment.setCardGravity(page.cardGravity);
//        fragment.setExpansionEnabled(page.expansionEnabled);
//        fragment.setExpansionDirection(page.expansionDirection);
//        fragment.setExpansionFactor(page.expansionFactor);
        return fragment;
    }

    // Obtain the background image for the page at the specified position
//    @Override
//    public ImageReference getBackground(int row, int column) {
//        return ImageReference.forDrawable(BG_IMAGES[row % BG_IMAGES.length]);
//    }

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
