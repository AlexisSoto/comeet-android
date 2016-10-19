package today.comeet.android.comeet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import today.comeet.android.comeet.R;
import today.comeet.android.comeet.listener.RecyclerItemClickListener;
import today.comeet.android.comeet.adapter.RecyclerSimpleViewAdapter;

/**
 * Created by Annick on 18/10/2016.
 */

public class EventRecyclerViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.myListSimple);

        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            // new item
            items.add("test " + i);
        }
        recyclerView.setAdapter(new RecyclerSimpleViewAdapter(items, android.R.layout.simple_list_item_1));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.d("test", "position :" + position);
                    }
                })
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
