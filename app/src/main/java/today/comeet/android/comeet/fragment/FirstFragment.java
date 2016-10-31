package today.comeet.android.comeet.fragment;

/**
 * Created by Vincent on 11/10/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.activity.CreationEventActivity;

public class FirstFragment extends Fragment {
    FloatingActionButton buttonEventCreate;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FirstFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        FirstFragment firstFragment = new FirstFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onStart() {
        super.onStart();
        mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (R.id.Events_Fragment != 0)
        transaction.replace(R.id.Events_Fragment, new EventRecyclerViewFragment());
        transaction.replace(R.id.Map_Fragment, new GoogleMapFragment());
        transaction.commit();

        /*buttonEventCreate = (FloatingActionButton) getView().findViewById(R.id.Button_CreateEvent);
        buttonEventCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), CreationEventActivity.class));
                    }
                });*/
    }
}