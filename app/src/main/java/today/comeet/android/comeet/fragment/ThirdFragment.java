package today.comeet.android.comeet.fragment;

/**
 * Created by Vincent on 11/10/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import today.comeet.android.comeet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {

    public static ThirdFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ThirdFragment thirdFragment = new ThirdFragment();
        thirdFragment.setArguments(args);
        return thirdFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

}