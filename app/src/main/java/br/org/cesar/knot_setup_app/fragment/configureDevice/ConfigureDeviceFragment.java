package br.org.cesar.knot_setup_app.fragment.configureDevice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.fragment.configureDevice.ConfigureDeviceContract.Presenter;
import br.org.cesar.knot_setup_app.fragment.configureDevice.ConfigureDeviceContract.ViewModel;

public class ConfigureDeviceFragment extends Fragment implements ViewModel {

    private final String CHANNEL = "channel";
    private final String NET_NAME = "net_name";
    private final String PAN_ID = "pan_id";
    private final String XPAN_ID = "xpan_id";
    private final String MASTERKEY = "masterkey";
    private final String IP = "ip";
    private final int CHANNEL_CASE = 0;
    private final int NET_NAME_CASE = 1;
    private final int PAN_ID_CASE = 2;
    private final int XPAN_ID_CASE = 3;
    private final int MASTERKEY_CASE = 4;
    private final int IP_CASE = 5;

    private View mView;

    private Presenter presenter;
    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configure_thing, container, false);
        mView = view;

        int gatewayID = getActivity().getIntent().getIntExtra(Constants.GATEWAY_ID,
                Constants.DEFAULT_GATEWAY_ID);

        int operation = getActivity().getIntent().getIntExtra(Constants.OPERATION,
                Constants.CONFIGURE_THING_OPENTHREAD);

        this.presenter = new ConfigureDevicePresenter(this, gatewayID, operation,
                getActivity());
        handler = new Handler(Looper.getMainLooper());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.onFocus();
    }

    @Override
    public void onDisconnected(){
        getActivity().finish();
    }

    @Override
    public void onConnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.thing_pairing_succeeded), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onErrorHandler(Throwable throwable) {
        getActivity().finish();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.thing_get_openthread_request_failed),
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWriteSucceeded(int val) {
        handler.post(new Runnable() {

            @Override
            public void run() {

                switch (val){
                    case CHANNEL_CASE:
                        removeProgressBar(CHANNEL);
                        addCheck(CHANNEL);
                        break;
                    case NET_NAME_CASE:
                        removeProgressBar(NET_NAME);
                        addCheck(NET_NAME);
                        break;
                    case PAN_ID_CASE:
                        removeProgressBar(PAN_ID);
                        addCheck(PAN_ID);
                        break;
                    case XPAN_ID_CASE:
                        removeProgressBar(XPAN_ID);
                        addCheck(XPAN_ID);
                        break;
                    case MASTERKEY_CASE:
                        removeProgressBar(MASTERKEY);
                        addCheck(MASTERKEY);
                        break;
                    case IP_CASE:
                        removeProgressBar(IP);
                        addCheck(IP);
                }

            }
        });

    }

    @Override
    public void onWriteFailed(int val) {
        handler.post(new Runnable() {

            @Override
            public void run() {

                switch (val) {
                    case CHANNEL_CASE:
                        addFail(CHANNEL);
                        break;
                    case NET_NAME_CASE:
                        addFail(NET_NAME);
                        break;
                    case PAN_ID_CASE:
                        addFail(PAN_ID);
                        break;
                    case XPAN_ID_CASE:
                        addFail(XPAN_ID);
                        break;
                    case MASTERKEY_CASE:
                        addFail(MASTERKEY);
                        break;
                    case IP_CASE:
                        addFail(IP);
                }

            }
        });

        removeProgressBar(CHANNEL);
        removeProgressBar(NET_NAME);
        removeProgressBar(PAN_ID);
        removeProgressBar(XPAN_ID);
        removeProgressBar(MASTERKEY);
        removeProgressBar(IP);
    }

    public void removeProgressBar(String progressBarID) {
        int resID = getResources().getIdentifier(progressBarID +
                "_progress_bar","id",getActivity().getPackageName());
        ProgressBar progressBar = mView.findViewById(resID);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void addCheck(String settingID) {
        int resID = getResources().getIdentifier(settingID +
                "_check","id",getActivity().getPackageName());

        ImageView imageView = mView.findViewById(resID);
        imageView.setVisibility(View.VISIBLE);
    }

    public void addFail(String settingID) {
        int resID = getResources().getIdentifier(settingID +
                "_fail","id",getActivity().getPackageName());
        ImageView imageView = mView.findViewById(resID);
        imageView.setVisibility(View.VISIBLE);
    }

}
