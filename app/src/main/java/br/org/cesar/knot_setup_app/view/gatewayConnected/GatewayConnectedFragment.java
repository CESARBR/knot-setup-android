package br.org.cesar.knot_setup_app.view.gatewayConnected;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.view.splash.SplashActivity;
import br.org.cesar.knot_setup_app.domain.adapter.GatewayAdapter;
import br.org.cesar.knot_setup_app.view.gatewayConnected.GatewayConnectedContract.ViewModel;
import br.org.cesar.knot_setup_app.view.gatewayConnected.GatewayConnectedContract.Presenter;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

public class GatewayConnectedFragment extends Fragment implements ViewModel {

    private View mView;
    private ListView deviceListView;

    private Presenter mPresenter;
    private ArrayList<NsdServiceInfo> deviceList;
    private GatewayAdapter adapter;
    private Handler handler;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container,false);

        mView = view;
        mPresenter = new GatewayConnectedPresenter(this,
                (NsdManager) getActivity().getSystemService(Context.NSD_SERVICE), getContext());
        deviceList = new ArrayList<>();
        adapter = new GatewayAdapter(getActivity(), R.layout.item_gateway, deviceList);
        handler = new Handler(Looper.getMainLooper());

        deviceListView = view.findViewById(R.id.list);
        setAdapter();

        return view;
    }

    @Override
    public void onGatewaysFound(ArrayList<NsdServiceInfo> deviceListReceived) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                deviceList.clear();
                deviceList.addAll(deviceListReceived);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setAdapter() {
        deviceListView.setAdapter(adapter);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onGatewayClicked(deviceList.get(position));
                LogWrapper.Log("Service name: " + deviceList.get(position).getServiceName(), Log.DEBUG);
                Intent i = new Intent(getActivity(), SplashActivity.class);
                i.putExtra(Constants.GATEWAY_ID, deviceList.get(position).getServiceName());
                startActivity(i);
            }
        });
    }

    @Override
    public void setSearchingFeedback(int visibilty) {
        TextView lookingForGateway = mView.findViewById(R.id.feedback_message);
        ProgressBar progressBar = mView.findViewById(R.id.feedback_spinner);
        handler.post(new Runnable() {
            @Override
            public void run() {
                lookingForGateway.setVisibility(visibilty);
                progressBar.setVisibility(visibilty);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onFocusLost();
    }


}
