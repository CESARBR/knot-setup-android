package br.org.cesar.knot_setup_app.data.api;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.model.State;
import br.org.cesar.knot_setup_app.utils.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

        private static ApiService mApiService;
        private static ImplService mService;

        public ApiService() {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

            mService = retrofit.create(ImplService.class);

        }

        public static ApiService getInstance() {
            if (mApiService == null) {
                mApiService = new ApiService();
            }
            return mApiService;
        }

        public ImplService getImplService() {
            return mService;
        }

        public String getState(){
            String state = mApiService.getState();
            return state;
        }

}
