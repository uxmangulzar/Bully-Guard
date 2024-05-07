package com.example.codesteembullyguard.network;

/**
 * Created by Usman on 11/18/2019
 */


import com.example.codesteembullyguard.models.LoginResponseModel;
import com.example.codesteembullyguard.models.MainResponseModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {


    @FormUrlEncoded
    @POST("parent_signup.php")
    Call<LoginResponseModel> registerParent(
            @Field("first_name") String first_name
            , @Field("last_name") String last_name
            , @Field("phone") String phone
            , @Field("date_time") String date_time
            , @Field("email") String email
            , @Field("password") String password
    );
    @FormUrlEncoded
    @POST("add_moderation_rules.php")
    Call<LoginResponseModel> add_moderation_rules(
            @Field("child_id") String child_id
            , @Field("insults") String insults
            , @Field("racism") String racism
            , @Field("homophobie") String homophobie
            , @Field("moral") String moral
            , @Field("body_shame") String body_shame
            , @Field("sexual_harassment") String sexual_harassment
    );

    @FormUrlEncoded
    @POST("getchild_onparentid.php")
    Call<MainResponseModel> getChildOnParent(
            @Field("parent_id") String parent_id
            , @Field("page") int page
            , @Field("limit") int limit
    );

    @FormUrlEncoded
    @POST("get_all_notification_by_type.php")
    Call<MainResponseModel> get_all_notification_by_type(
            @Field("parent_id") String parent_id
            , @Field("notifications_type") String notifications_type
            , @Field("page") int page
            , @Field("limit") int limit
    );

    @FormUrlEncoded
    @POST("get_all_locations_parent.php")
    Call<MainResponseModel> get_all_locations_parent(
            @Field("parent_id") String parent_id
            , @Field("date_time") String date_time
    );
    @FormUrlEncoded
    @POST("get_moderations.php")
    Call<MainResponseModel> get_moderations(
            @Field("page") int page
            , @Field("limit") int limit
            , @Field("child_id") String child_id
    );

    @FormUrlEncoded
    @POST("update_app_time.php")
    Call<MainResponseModel> update_app_time(
            @Field("app_id") String app_id
            , @Field("set_time") String set_time
            , @Field("from_time") String from_time
            , @Field("to_time") String to_time
    );
    @FormUrlEncoded
    @POST("block_all.php")
    Call<MainResponseModel> block_all(
            @Field("child_id") String child_id
            , @Field("blocked") String blocked
    );

    @FormUrlEncoded
    @POST("get_locations.php")
    Call<MainResponseModel> get_locations_on_child(
            @Field("page") int page
            , @Field("limit") int limit
            , @Field("child_id") String child_id
            , @Field("date_time") String date_time
    );

    @FormUrlEncoded
    @POST("get_app_stats.php")
    Call<MainResponseModel> get_app_stats(
            @Field("child_id") String child_id
            , @Field("date_time") String date_time
    );

    @FormUrlEncoded
    @POST("update_battery_percent.php")
    Call<MainResponseModel> update_battery_percent(
            @Field("child_id") String child_id
            , @Field("battery_percentage") String battery_percentage
    );

    @FormUrlEncoded
    @POST("get_all_child_apps.php")
    Call<MainResponseModel> get_all_child_apps(
            @Field("page") int page
            , @Field("limit") int limit
            , @Field("child_id") String child_id
    );

    @FormUrlEncoded
    @POST("get_notificationsbytype.php")
    Call<MainResponseModel> get_notificationsForChild(
            @Field("page") int page
            , @Field("limit") int limit
            , @Field("child_id") String child_id
            , @Field("notifications_type") String notifications_type
    );

    @FormUrlEncoded
    @POST("get_notificationsbyparent.php")
    Call<MainResponseModel> get_notificationsbyparent(
            @Field("parent_id") String parent_id
    );

    @FormUrlEncoded
    @POST("child_signup.php")
    Call<LoginResponseModel> registerChild(
            @Field("first_name") String first_name
            , @Field("last_name") String last_name
            , @Field("gender") String gender
            , @Field("address") String address
            , @Field("age") String age
            , @Field("school") String school
            , @Field("parent_id") String parent_id
            , @Field("phone") String phone
            , @Field("date_time") String date_time
            , @Field("user_image") String user_image
    );

    @FormUrlEncoded
    @POST("login_parent.php")
    Call<LoginResponseModel> login(
            @Field("email") String email
            , @Field("password") String password
    );

    @FormUrlEncoded
    @POST("add_apps_stats.php")
    Call<MainResponseModel> add_apps_stats(
            @Field("child_id") String child_id
            , @Field("commas_app_name") String commas_app_name
            , @Field("commas_time_spent") String commas_time_spent
            , @Field("commas_app_image") String commas_app_image
            , @Field("date_time") String date_time
            , @Field("commas_package_names") String commas_package_names
    );

    @FormUrlEncoded
    @POST("add_notifications.php")
    Call<MainResponseModel> add_notifications(
            @Field("child_id") String child_id
            , @Field("date_times") String date_times
            , @Field("device_types") String device_types
            , @Field("app_icons") String app_icons
            , @Field("notifications_types") String notifications_types
            , @Field("titles") String titles
    );

    @FormUrlEncoded
    @POST("add_location.php")
    Call<MainResponseModel> add_location(
            @Field("child_id") String child_id
            , @Field("user_lat") String user_lat
            , @Field("user_lon") String user_lon
            , @Field("date_time") String date_time
    );

    @Multipart
    @POST("upload_file.php")
    Call<MainResponseModel> uploadFile(@Part MultipartBody.Part file);


}
