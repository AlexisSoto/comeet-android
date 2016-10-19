package today.comeet.android.comeet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import today.comeet.android.comeet.R;

public class EventDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            int id = extras.getInt("id");
            Log.d("test", "id recupere: " + id);
        }
    }
}
