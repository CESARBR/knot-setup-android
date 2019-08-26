package br.org.cesar.knot_setup_app.fragment.thingRegistered;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.org.cesar.knot_setup_app.R;

import br.org.cesar.knot_setup_app.domain.adapter.ThingAdapter;
import br.org.cesar.knot_setup_app.fragment.thingRegistered.ThingContract.Presenter;
import br.org.cesar.knot_setup_app.fragment.thingRegistered.ThingContract.ViewModel;
import br.org.cesar.knot_setup_app.model.Gateway;

public class ThingFragment extends Fragment implements ViewModel {

    private ListView deviceListView;

    private ThingAdapter adapter;
    private Presenter presenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        presenter = new ThingPresenter(this, getActivity());
        deviceListView = view.findViewById(R.id.list);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.onFocus();
    }



    /**
     * Setup current devices list with smart devices saved on database
     * @param deviceList
     */

    @Override
    public void onThingsFound(List<Gateway> deviceList) {

        adapter = new ThingAdapter(getActivity(), R.layout.item_thing, deviceList);

        deviceListView.setAdapter(adapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

}
