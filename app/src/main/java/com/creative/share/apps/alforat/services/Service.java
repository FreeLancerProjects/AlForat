package com.creative.share.apps.alforat.services;


import com.creative.share.apps.alforat.models.AreaDataModel;
import com.creative.share.apps.alforat.models.BillDataModel;
import com.creative.share.apps.alforat.models.ChargeReportDataModel;
import com.creative.share.apps.alforat.models.ClientsDataModel;
import com.creative.share.apps.alforat.models.ItemCartModel;
import com.creative.share.apps.alforat.models.OfferDataModel;
import com.creative.share.apps.alforat.models.ProductDataModel;
import com.creative.share.apps.alforat.models.ReportDataModel;
import com.creative.share.apps.alforat.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("Api/login")
    Call<UserModel> login(@Field("user_name") String user_name,
                          @Field("user_pass") String password);


    @GET("Api/setting")
    Call<AreaDataModel> getAreas(@Header("Authorization") String user_token);


    @Multipart
    @POST("Api/client")
    Call<ResponseBody> addWithImageClient(@Header("Authorization") String user_token,
                                          @Part("client_name") RequestBody client_name,
                                          @Part("client_past_amount") RequestBody client_past_amount,
                                          @Part("area_id_fk") RequestBody area_id_fk,
                                          @Part("cleint_address") RequestBody cleint_address,
                                          @Part("client_phone") RequestBody client_phone,
                                          @Part("cleint_tax_card") RequestBody cleint_tax_card,
                                          @Part("client_commercial_register") RequestBody client_commercial_register,
                                          @Part("client_debt_limit") RequestBody client_debt_limit,
                                          @Part List<MultipartBody.Part> images

    );

    @Multipart
    @POST("Api/client")
    Call<ResponseBody> addWithoutImageClient(@Header("Authorization") String user_token,
                                             @Part("client_name") RequestBody client_name,
                                             @Part("area_id_fk") RequestBody area_id_fk,
                                             @Part("cleint_address") RequestBody cleint_address,
                                             @Part("client_phone") RequestBody client_phone

    );

    @GET("Api/clients")
    Call<ClientsDataModel> getClients(@Header("Authorization") String user_token,
                                      @Query("page") int page,
                                      @Query("limit_per_page") int limit_per_page
    );


    @GET("Api/clients")
    Call<ClientsDataModel> searchClientByeName(@Header("Authorization") String user_token,
                                               @Query("search_name") String search_name,
                                               @Query("page") int page,
                                               @Query("limit_per_page") int limit_per_page
    );

    @GET("Api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String user_token);


    @GET("Api/storeStock")
    Call<ProductDataModel> getStoreProducts(@Header("Authorization") String user_token);

    @GET("Api/clientReport")
    Call<ReportDataModel> getClientReport(@Header("Authorization") String user_token,
                                          @Query("client_acc_code") String client_acc_code,
                                          @Query("from_date") String from_date,
                                          @Query("to_date") String to_date
    );

    @GET("Api/myReport")
    Call<ReportDataModel> getMyReport(@Header("Authorization") String user_token,
                                      @Query("from_date") String from_date,
                                      @Query("to_date") String to_date
    );

    @GET("Api/offers")
    Call<OfferDataModel> getOffers(@Header("Authorization") String user_token
    );

    @GET("Api/bills")
    Call<BillDataModel> getBills(@Header("Authorization") String user_token,
                                 @Query("page") int page
    );


    @FormUrlEncoded
    @POST("Api/sales")
    Call<ProductDataModel> getSalesProducts(@Header("Authorization") String user_token,
                                            @Field("client_id") String client_id,
                                            @Field("delegate_id_fk") String delegate_id_fk,
                                            @Field("date") String date
    );

    @POST("Api/addBill")
    Call<ResponseBody> addBill(@Header("Authorization") String user_token,
                               @Body ItemCartModel itemCartModel
    );

    @GET("Api/shipping")
    Call<ChargeReportDataModel> getChargeData(@Header("Authorization") String user_token,
                                              @Query("page") int page
    );


    @FormUrlEncoded
    @POST("Api/updateToken")
    Call<ResponseBody> updateToken(@Header("Authorization") String user_token,
                                   @Field("firebase_token") String firebase_token
    );


}