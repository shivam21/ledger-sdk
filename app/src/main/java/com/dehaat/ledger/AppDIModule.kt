package com.dehaat.ledger

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.TextUtils
import com.dehaat.androidbase.coroutine.Dispatchers
import com.dehaat.androidbase.coroutine.IDispatchers
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lib.dehaat.ledger.presentation.LibLedgerAnalytics
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.NetworkInterface
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val ANIL_POST_MAN_MOCK_API_URL = "https://b7bcaa8f-c878-4f8c-8e3f-4a63f40e45c8.mock.pstmn.io"

@Module
@InstallIn(SingletonComponent::class)
class AppDIModule {


    @Named("BaseUrl")
    @Provides
    fun provideLedgerBaseURL(): String {
        return ANIL_POST_MAN_MOCK_API_URL
    }

    private fun provideVPNInterceptor(context: Context) =
        Interceptor { chain ->
            if (checkVPNStatus(context)) {
                Response.Builder()
                    .code(404)
                    .body(ResponseBody.create(null, ""))
                    .protocol(Protocol.HTTP_2)
                    .message("Please turn-off VPN service for security reason.")
                    .request(chain.request())
                    .build()
            } else {
                chain.proceed(chain.request())
            }
        }

    private fun checkVPNStatus(context: Context): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } else {
            val networkList: MutableList<String> = ArrayList()
            try {
                for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (networkInterface.isUp) networkList.add(networkInterface.name)
                }
            } catch (ex: Exception) {
//                FirebaseCrashlytics.getInstance().recordException(ex)
            }
            networkList.contains("ppp0") || networkList.contains("tunX") || networkList.contains("pptpX")
        }
    }

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext appContext: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerAndQueryInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
//            .cache(AppRestClient.cache)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .dispatcher(Dispatcher().apply { maxRequests = 1 })
        .retryOnConnectionFailure(true)
        .apply {
            addNetworkInterceptor(httpLoggingInterceptor)
            addInterceptor(headerAndQueryInterceptor)
            addInterceptor(provideVPNInterceptor(context = appContext))
            if (BuildConfig.DEBUG) {
                addInterceptor(ChuckInterceptor(appContext))
            }
//            authenticator(AimsAuthenticator())
        }
        .build()


    @Provides
    fun provideHeaderAndQueryInterceptor() = Interceptor { chain ->
        val original: Request = chain.request()
        val url = original.url
            .newBuilder()
            .addQueryParameter("app_code", "aims")
            .build()
        var authToken = "AppPreference.getInstance().authToken"
        if (TextUtils.isEmpty(authToken)) authToken = ""
        var request = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $authToken")
            .method(original.method, original.body)
            .url(url)
            .build()
        if (true) {
            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                .build()
            chain.proceed(request)
        } else {
            request = request.newBuilder()
                .header("Cache-Control", "public, , max-stale=" + 60 * 60 * 24 * 7).build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideMoshConverterFactory(mosh: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(mosh)

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory,
        @Named("BaseUrl") apiBaseUrl: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @Provides
    fun provideDispatchers(): IDispatchers = Dispatchers()

    @Provides
    fun provideAnalytics(libLedgerAnalyticsImpl:LibLedgerAnalyticsImpl): LibLedgerAnalytics = libLedgerAnalyticsImpl
}
