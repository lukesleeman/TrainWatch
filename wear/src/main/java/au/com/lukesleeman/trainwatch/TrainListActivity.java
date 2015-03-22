package au.com.lukesleeman.trainwatch;

import android.app.Activity;
import android.support.wearable.view.GridViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import au.com.lukesleeman.trainwatch.domain.Train;

/**
 * Displays a list of trains, which were loaded by the LoadingActivity.
 */
@EActivity(R.layout.activity_train_list)
public class TrainListActivity extends Activity{

    @ViewById(R.id.train_list_pager)
    protected GridViewPager pager;

    @Extra
    protected List<Train> trainList;

    @AfterViews
    protected void setupUi(){
        pager.setAdapter(new TrainListPagerAdapter(this, getFragmentManager(), trainList));
    }

}
