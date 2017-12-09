package rw.sd.otobox;

import android.app.Application;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.parse.Parse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rw.sd.otobox.Bus.RxBus;

/**
 * Created by mucyo miller on 8/30/17.
 */

public class App  extends Application{
    private RxBus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new RxBus();
        //Parse.initialize(this);
        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.parse_app_id)) // should correspond to APP_ID env variable
                .clientBuilder(builder)
                .server(getResources().getString(R.string.parse_server_url)).build());
    }

    public RxBus bus() {
        return bus;
    }
}
