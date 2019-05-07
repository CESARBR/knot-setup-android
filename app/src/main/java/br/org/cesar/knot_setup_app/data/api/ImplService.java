package br.org.cesar.knot_setup_app.data.api;
import java.util.List;

import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.model.State;
import br.org.cesar.knot_setup_app.model.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ImplService {

    @GET("api/state")
    Observable<State> getState();

    @GET("api/devices")
    Observable<List<Gateway>> getDevices(@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("/api/auth")
    Observable<User> login(@Field("email") String email, @Field("password") String password);

}
