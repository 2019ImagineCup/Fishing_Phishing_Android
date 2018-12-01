package ensharp.imagincup2019.fishingphishing;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import ensharp.imagincup2019.fishingphishing.Common.Model.CallHistory;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .initialData(realm -> {
                    List<CallHistory> histories = loadHistories();
                    if (histories != null) {
                        realm.insertOrUpdate(histories);
                    }
                })
                .deleteRealmIfMigrationNeeded()
                .build()
        );
    }

    private List<CallHistory> loadHistories() {
        InputStream stream;
        try {
            stream = getAssets().open("cities.json");
        } catch (IOException e) {
            return null;
        }

        Gson gson = new GsonBuilder().create();

        JsonElement json = new JsonParser().parse(new InputStreamReader(stream));

        return gson.fromJson(json, new TypeToken<List<CallHistory>>() {
        }.getType());
    }
}
