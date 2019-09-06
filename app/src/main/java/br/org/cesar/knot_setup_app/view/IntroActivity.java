package br.org.cesar.knot_setup_app.view;
         
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(IntroActivity.this, GatewayActivity.class );
        startActivity(i);
        finish();
    }

}
