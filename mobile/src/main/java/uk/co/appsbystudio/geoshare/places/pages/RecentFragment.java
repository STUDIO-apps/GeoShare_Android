package uk.co.appsbystudio.geoshare.places.pages;

import android.os.Bundle;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mypopsy.maps.StaticMap;

import java.util.ArrayList;

import uk.co.appsbystudio.geoshare.R;
import uk.co.appsbystudio.geoshare.places.placesadapter.RecentAdapter;

public class RecentFragment extends Fragment {

    public RecentFragment() {}

    private ArrayList<String> cityName = new ArrayList<>();
    private ArrayList<String> countryName = new ArrayList<>();
    private ArrayList<String> locationImageURL = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recent, container, false);

        RecyclerView recentRecyclerView = (RecyclerView) view.findViewById(R.id.recentList);
        recentRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recentRecyclerView.setLayoutManager(layoutManager);

        cityName.add("London");
        countryName.add("England");
        cityName.add("Paris");
        countryName.add("France");
        cityName.add("New-York City");
        countryName.add("United States of America");

        for (String city : cityName) {
            StaticMap staticMap = new StaticMap().center(String.valueOf(city)).size(512, 512);
            locationImageURL.add(staticMap.toString());
        }

        final RecentAdapter recentAdapter = new RecentAdapter(getActivity(), cityName, countryName, locationImageURL);

        recentRecyclerView.setAdapter(recentAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                cityName.remove(viewHolder.getAdapterPosition());
                locationImageURL.remove(viewHolder.getAdapterPosition());
                recentAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        itemTouchHelper.attachToRecyclerView(recentRecyclerView);

        return view;
    }

}
