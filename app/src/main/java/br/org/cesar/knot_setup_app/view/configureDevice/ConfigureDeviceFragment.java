package br.org.cesar.knot_setup_app.view.configureDevice;

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
import br.org.cesar.knot_setup_app.view.configureDevice.ConfigureDeviceContract.Presenter;
import br.org.cesar.knot_setup_app.view.configureDevice.ConfigureDeviceContract.ViewModel;

public class ConfigureDeviceFragment extends Fragment implements ViewModel {

    private View mView;

    private Presenter presenter;
    private Handler mHandler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configure_thing, container, false);
        mView = view;

        int gatewayID = getActivity().getIntent().getIntExtra("gatewayID",0);
        int operation = getActivity().getIntent().getIntExtra("operation",
                Constants.CONFIGURE_THING_OPENTHREAD);

        this.presenter = new ConfigureDevicePresenter(this,gatewayID, operation
                ,getActivity());
        mHandler = new Handler(Looper.getMainLooper());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.onResume();
    }

    @Override
    public void onDisconnected(){
        getActivity().finish();
    }

    @Override
    public void onConnected() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), "Successfully paired" +
                        " with device!!!", Toast.LENGTH_LONG).show();            }
        });
    }

    @Override
    public void onErrorHandler(Throwable throwable) {
        getActivity().finish();
        mHandler.post(new Runnable() {
            @Override
            public void run() {Toast.makeText(getActivity().getApplicationContext(),
                    "Something is wrong with the gateway, please try again later.",
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWriteSucceeded(int val) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {

                switch (val){
                    case 0:
                        removeProgressBar("channel");
                        addCheck("channel");
                        break;
                    case 1:
                        removeProgressBar("net_name");
                        addCheck("net_name");
                        break;
                    case 2:
                        removeProgressBar("pan_id");
                        addCheck("pan_id");
                        break;
                    case 3:
                        removeProgressBar("xpan_id");
                        addCheck("xpan_id");
                        break;
                    case 4:
                        removeProgressBar("masterkey");
                        addCheck("masterkey");
                        break;
                    case 5:
                        removeProgressBar("ip");
                        addCheck("ip");
                }

            }
        });

    }

    @Override
    public void onWriteFailed(int val) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {

                switch (val) {
                    case 0:
                        addFail("channel");
                        break;
                    case 1:
                        addFail("net_name");
                        break;
                    case 2:
                        addFail("pan_id");
                        break;
                    case 3:
                        addFail("xpan_id");
                        break;
                    case 4:
                        addFail("masterkey");
                        break;
                    case 5:
                        addFail("ip");
                }

            }
        });

        removeProgressBar("channel");
        removeProgressBar("net_name");
        removeProgressBar("pan_id");
        removeProgressBar("xpan_id");
        removeProgressBar("masterkey");
        removeProgressBar("ip");
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
