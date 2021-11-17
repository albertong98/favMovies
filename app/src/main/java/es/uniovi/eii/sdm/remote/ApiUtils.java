package es.uniovi.eii.sdm.remote;

import retrofit2.Retrofit;

public class ApiUtils {
    public static final String LANGUAGE = "es-ES";
    public static final String API_KEY = "b234563888662f585de403a70a6ba54f";

    public static ThemoviedbApi createThemoviedbApi() {
        Retrofit retrofit= RetrofitClient.getClient(ThemoviedbApi.BASE_URL);

        return retrofit.create(ThemoviedbApi.class);
    }


}
