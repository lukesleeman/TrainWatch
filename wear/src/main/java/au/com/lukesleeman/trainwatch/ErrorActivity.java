package au.com.lukesleeman.trainwatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_error)
public class ErrorActivity extends Activity {

    @ViewById(R.id.error_text)
    protected TextView errorText;

    @Extra
    protected String error;

    @AfterViews
    protected void setupUi(){
        errorText.setText(error);
    }
}
