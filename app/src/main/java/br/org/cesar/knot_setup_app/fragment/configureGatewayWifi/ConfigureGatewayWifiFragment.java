package br.org.cesar.knot_setup_app.fragment.configureGatewayWifi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.fragment.configureGatewayWifi.ConfigureGatewayWifiContract.ViewModel;
import br.org.cesar.knot_setup_app.fragment.configureGatewayWifi.ConfigureGatewayWifiContract.Presenter;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

public class ConfigureGatewayWifiFragment extends Fragment implements ViewModel {

    private EditText wifiField;
    private EditText pwdField;
    private Button   sendButton;

    private Presenter presenter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configure_gateway_wifi, container,false);


        wifiField = view.findViewById(R.id.wifi_editText);
        pwdField = view.findViewById(R.id.password_editText);
        sendButton = view.findViewById(R.id.buttonSendWifiSettings);


        presenter = new ConfigureGatewayWifiPresenter(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.setEnabled(false);
                if(!(TextUtils.isEmpty(wifiField.getText()) ||
                        TextUtils.isEmpty(pwdField.getText()))) {
                    LogWrapper.Log("text not empty", Log.DEBUG);
                    presenter.onSetWifiClicked(wifiField.getText().toString(),
                            pwdField.getText().toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.onFocus();
    }

    @Override
    public void onDisconnected() {
        getActivity().finish();
    }

    @Override
    public void onWriteFailed() {
        sendButton.setEnabled(true);
        Toast.makeText(getActivity(),getString(R.string.failed_write), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setWifiSSID(String wifiName) {
        String wifiNameWithoutQuotationMarks = wifiName.replaceAll("\"","");
        this.wifiField.setText(wifiNameWithoutQuotationMarks);
    }


}
