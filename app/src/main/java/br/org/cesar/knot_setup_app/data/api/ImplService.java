package br.org.cesar.knot_setup_app.data.api;
import java.util.List;

import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.model.State;
import br.org.cesar.knot_setup_app.model.User;
import br.org.cesar.knot_setup_app.model.Openthread;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ImplService {

    @GET
    Observable<State> getState(@Url String url);

    @GET
    Observable<List<Gateway>> getDevices(@Url String url ,@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST
    Observable<User> login(@Url String url,@Field("email") String email, @Field("password") String password);

    @GET
    Observable<Openthread> getOpenthreadConfig(@Url String url, @Header("Authorization") String auth);

}
