package br.org.cesar.knot_setup_app.view;

import android.os.Bundle;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.view.login.LoginFragment;
import br.org.cesar.knot_setup_app.model.Tab;
import br.org.cesar.knot_setup_app.utils.Constants;

public class LoginActivity extends BaseActivity {

    private String gatewayName;
    private ArrayList<Tab> tabs;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            tabs = new ArrayList<>();
            gatewayName = getIntent().getStringExtra(Constants.GATEWAY_ID);

            Tab tab = new Tab(gatewayName, new LoginFragment());
            tabs.add(tab);

            setupHeader(getString(R.string.login_header_title),tabs);
    }

}
