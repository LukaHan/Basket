package com.first.basket.http

import com.first.basket.base.HttpResult
import com.first.basket.bean.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import rx.Observable
import java.io.File

/**
 * Created by hanshaobo on 05/09/2017.
 */
interface ApiService {
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getProducts(@Field("action") action: String, @Field("channel") channel: String, @Field("leveltwoid") leveltwoid: String, @Field("productname") productname: String, @Field("productid") productid: String): Observable<HttpResult<ClassifyContentBean>>

    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getClassify(@Field("action") action: String, @Field("channel") channel: String): Observable<HttpResult<ClassifyBean>>

    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getDetail(@Field("action") action: String, @Field("productid") id: String?, @Field("productocr") productocr: String?): Observable<HttpResult<GoodsDetailBean>>

    //猜你想要
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getRecommend(@Field("action") action: String): Observable<RecommendBean>


    //热门推荐
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getHotRecommend(@Field("action") action: String, @Field("channel") channel: String): Observable<HotRecommendBean>

    //获取订单列表
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getOrderList(@Field("action") action: String, @Field("userid") userid: String): Observable<HttpResult<OrderListBean>>

    //首页
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getMainpage(@Field("action") action: String): Observable<HttpResult<HomeBean>>

    //计算商品价格
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getPrice(@Field("action") action: String, @Field("productids") productids: String, @Field("productnum") productnum: String, @Field("addressid") addressid: String): Observable<HttpResult<PriceBean>>


//    //立即下单
//    @FormUrlEncoded
//    @POST("ClientAPI.php")
//    fun doPlaceOrder(@Field("action") action: String, @Field("productsid") productsid: String, @Field("productsNumber") productsNumber: String, @Field("userid") userid: String, @Field("addressid") addressid: String): Observable<HttpResult<CodeBean>>


    //发送验证码
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getCode(@Field("action") action: String, @Field("phonenumber") phonenumber: String): Observable<HttpResult<CodeBean>>


    //注册，修改密码
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doRegister(@Field("action") action: String, @Field("phonenumber") phonenumber: String, @Field("code") code: String, @Field("password") password: String): Observable<HttpResult<LoginBean>>


    //登录
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doLogin(@Field("action") action: String, @Field("phonenumber") phonenumber: String, @Field("code") code: String, @Field("password") password: String): Observable<HttpResult<LoginBean>>

    //添加地址
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun addAddress(@Field("action") action: String, @FieldMap map: HashMap<String, String?>): Observable<HttpResult<LoginBean>>

    //修改地址
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun ModifyAddress(@Field("action") action: String, @FieldMap map: HashMap<String, String>): Observable<HttpResult<LoginBean>>

    //获取街道/镇
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getSubdistrict(@Field("action") action: String, @Field("districtname") districtname: String): Observable<HttpResult<DistrictBean>>

    //获取地址列表
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getAddressList(@Field("action") action: String, @Field("userid") userid: String): Observable<HttpResult<AddressListBean>>

    //设置默认地址
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doDefaultAddress(@Field("action") action: String, @Field("userid") userid: String, @Field("addressid") addressid: String): Observable<HttpResult<CodeBean>>

    //删除地址
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doDeleteAddress(@Field("action") action: String, @Field("userid") userid: String, @Field("addressid") addressid: String): Observable<HttpResult<CodeBean>>

    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doPayforwechat(@Field("action") action: String, @FieldMap map: HashMap<String, String>): Observable<HttpResult<WechatBean>>

    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doPayforAli(@Field("action") action: String, @FieldMap map: HashMap<String, String>): Observable<HttpResult<AliBean>>


    //捐赠
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getDonateList(@Field("action") action: String): Observable<DonateBean>

    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getActivePage(@Field("action") action: String): Observable<HttpResult<ActiveBean>>


    //修改昵称
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doModifyUsername(@Field("action") action: String,@Field("userid") userid: String,@Field("username") username: String): Observable<HttpResult<LoginBean>>

    //上传头像、身份证
    /**
     * type
     * photo  头像
     * poscard  身份证正面
     * oppcard  身份证反面
     */
    @Multipart
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doUploadimage(@Field("action") action: String, @Field("userid") userid: String, @Field("type") type: String, @Part("upload") file: RequestBody): Observable<HttpResult<LoginBean>>

    //实名认证
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun doCheckrealname(@Field("action") action: String,@Field("userid") userid: String,@Field("realname") realname: String,@Field("cardid") cardid: String): Observable<HttpResult<LoginBean>>


    //获取用户信息
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getUserInfo(@Field("action") action: String,@Field("userid") userid: String): Observable<HttpResult<LoginBean>>

    //配送范围
    @FormUrlEncoded
    @POST("ClientAPI.php")
    fun getDeliverare(@Field("action") action: String): Observable<HttpResult<DeliverBean>>
}