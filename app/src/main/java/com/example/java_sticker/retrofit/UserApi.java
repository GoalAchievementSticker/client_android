package com.example.java_sticker.retrofit;

import com.example.java_sticker.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*어떤 파라미터와 함꼐 어떤 메소드로 요청을 보내고 어떤 형태로 응답을 받을 것인지 정의
 * @GET: 서버에서 데이터를 받아올 때 사용
 * @POST: 서버에 데이터를 보낼 때 사용
 * */
public interface UserApi {

    @GET("/users/get-all")
    /*받아온 데이터 리스트*/
    Call<List<User>> listUser();

//    @FormUrlEncoded
    @POST("/users/insert")
    /*@Header 에 보내는 데이터가 json 임을 명시하고
    User 객체를 파라미터 타입으로 @Body annotation 을 사용한다.
     @Body annotation 내용은 HTTP 요청 본문에 들어간다. */
    Call<User> addUser(@Body User user);
//                        @Field("nickname") String nickname,
//                       @Field("email") String email,
//                       @Field("password") String password);

    @FormUrlEncoded
    @PATCH("/users/update/{productCd}")
    Call<User> updateUser(@Path("productCd") String productCd, @FieldMap Map<String, String> fields);

    @DELETE("/users/delete/{productCd}")
    Call<User> deleteUser(@Path("productCd") String productCd);
}
