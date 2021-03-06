package com.newdjk.doctor.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lxq.okhttp.response.GsonResponseHandler;
import com.newdjk.doctor.BuildConfig;
import com.newdjk.doctor.MyApplication;
import com.newdjk.doctor.R;
import com.newdjk.doctor.basic.BasicFragment;
import com.newdjk.doctor.model.HttpUrl;
import com.newdjk.doctor.tools.CommonMethod;
import com.newdjk.doctor.tools.Contants;
import com.newdjk.doctor.ui.activity.AllApplicationActivity;
import com.newdjk.doctor.ui.activity.AllReportsActivity;
import com.newdjk.doctor.ui.activity.ArchivesActivity;
import com.newdjk.doctor.ui.activity.ChickUnitActivity;
import com.newdjk.doctor.ui.activity.DoctorPraiseActivity;
import com.newdjk.doctor.ui.activity.MedicalServiceActivity;
import com.newdjk.doctor.ui.activity.MyCertActivity;
import com.newdjk.doctor.ui.activity.MyPointsActivity;
import com.newdjk.doctor.ui.activity.MyPointsRewardActivity;
import com.newdjk.doctor.ui.activity.MyPrescriptionActivity;
import com.newdjk.doctor.ui.activity.OnlineConsultingActivity;
import com.newdjk.doctor.ui.activity.OnlineRenewalPartyActivity;
import com.newdjk.doctor.ui.activity.PrescriptionListActivity;
import com.newdjk.doctor.ui.activity.ServiceSettingActivity;
import com.newdjk.doctor.ui.activity.SystemSettingActivity;
import com.newdjk.doctor.ui.activity.TreatActivity;
import com.newdjk.doctor.ui.activity.VideoInterrogationActivity;
import com.newdjk.doctor.ui.activity.WebViewActivity;
import com.newdjk.doctor.ui.activity.min.PersonalDataActivity;
import com.newdjk.doctor.ui.entity.AdBannerInfo;
import com.newdjk.doctor.ui.entity.DoctorInfoByIdEntity;
import com.newdjk.doctor.ui.entity.DoctorSignEntity;
import com.newdjk.doctor.ui.entity.DoctorSummaryEntity;
import com.newdjk.doctor.ui.entity.PrescriptionDataEntity;
import com.newdjk.doctor.ui.entity.ResponseEntity;
import com.newdjk.doctor.ui.entity.TranslateToPatientView;
import com.newdjk.doctor.ui.entity.UpdateImageView;
import com.newdjk.doctor.ui.entity.UpdatePatientCountEntity;
import com.newdjk.doctor.ui.entity.UpdatePushView;
import com.newdjk.doctor.utils.AppUtils;
import com.newdjk.doctor.utils.AuthenticationCommentUtil;
import com.newdjk.doctor.utils.GlideMediaLoader;
import com.newdjk.doctor.utils.SpUtils;
import com.newdjk.doctor.views.CircleImageView;
import com.newdjk.doctor.views.ItemView;
import com.newdjk.doctor.views.LoadDialog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.values.EnvType;
import lib_zxing.activity.CodeUtils;

/**
 * 个人中心.
 */
public class MinFragment extends BasicFragment implements IWXAPIEventHandler {
    private static final String TAG = "MinFragment";
    @BindView(R.id.civImg)
    CircleImageView civImg;
    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.liear_titlebar)
    LinearLayout liearTitlebar;
    @BindView(R.id.top_left)
    ImageView topLeft;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.top_right)
    ImageView topRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.relat_titlebar)
    RelativeLayout relatTitlebar;
    @BindView(R.id.picture_record)
    TextView pictureRecord;
    @BindView(R.id.renewal_record)
    TextView renewalRecord;
    @BindView(R.id.video_record)
    TextView videoRecord;
    @BindView(R.id.mFunReport)
    ItemView mFunReport;
    @BindView(R.id.mFunSignature)
    ItemView mFunSignature;
    @BindView(R.id.mFunSetting)
    ItemView mFunSetting;
    Unbinder unbinder;
    @BindView(R.id.chick_unit)
    ItemView chickUnit;
    @BindView(R.id.doctor_score)
    TextView doctorScore;
    @BindView(R.id.doctor_income)
    TextView doctorIncome;
    @BindView(R.id.total_patient)
    TextView totalPatient;
    @BindView(R.id.doctor_score_layout)
    LinearLayout doctorScoreLayout;
    @BindView(R.id.doctor_income_layout)
    LinearLayout doctorIncomeLayout;
    @BindView(R.id.total_patient_layout)
    LinearLayout totalPatientLayout;
    @BindView(R.id.all_reports)
    RelativeLayout allReports;
    @BindView(R.id.my_prescription)
    ItemView myPrescription;
    @BindView(R.id.mBanner)
    Banner mBanner;
    @BindView(R.id.mFunContainer)
    LinearLayout mFunContainer;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mServiceRecord)
    TextView mServiceRecord;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.tv_refuse_count)
    TextView tvRefuseCount;
    @BindView(R.id.mShenfangRecode)
    TextView mShenfangRecode;
    @BindView(R.id.share_app)
    ItemView shareApp;
    @BindView(R.id.seeing)
    TextView seeing;
    @BindView(R.id.amount_of_attention)
    TextView amountOfAttention;
    @BindView(R.id.mAudio)
    ItemView mAudio;
    @BindView(R.id.mdiagnoseRecord)
    TextView mdiagnoseRecord;
    @BindView(R.id.tv_authentication)
    TextView tvAuthentication;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.mPoints)
    ItemView mPoints;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.mypoint)
    LinearLayout mypoint;
    @BindView(R.id.mVideoContainer)
    LinearLayout mVideoContainer;
    @BindView(R.id.mkefu)
    ItemView mkefu;
    private DoctorInfoByIdEntity mDoctorInfoByIdEntity;
    public static String doctorPath;
    private int mDoctype;
    private String mPath;

    private Dialog mDialog;

    private View mInflate;

    private LinearLayout mFriend, mZoom;

    private Bitmap mBitmap;

    private String APP_ID = "wx55eb72942a9dad20";

    private IWXAPI iwxapi;

    private TextView mTvCancel;
    private final String YUNAPPLOAD = "https://doctor.newstartcare.com/yunAppLoad.html";
    private int mStatus;
    private Gson mGson;

    enum SHARE_TYPE {Type_WXSceneSession, Type_WXSceneTimeline}

    public static MinFragment getFragment() {
        return new MinFragment();
    }

    @Override
    protected int initViewResId() {
        return R.layout.fragment_min;
    }

    @Override
    protected void initView() {
        topTitle.setText("个人中心");
        mGson = new Gson();
        mStatus = SpUtils.getInt(Contants.Status, 0);
      /*  topRight.setImageResource(R.mipmap.ic_wx_share);
        topRight.setVisibility(View.VISIBLE);*/
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), APP_ID, false);
        iwxapi.handleIntent(getActivity().getIntent(), this);
        tvName.setText(SpUtils.getString(Contants.Name));
        topTitle.setTextColor(getResources().getColor(R.color.white));
        liearTitlebar.setBackgroundResource(R.color.theme);
        mDoctype = SpUtils.getInt(Contants.DocType, 0);


        if (mDoctype == 3) {
            mFunContainer.setVisibility(View.VISIBLE);
            mFunReport.setVisibility(View.GONE);
            chickUnit.setVisibility(View.GONE);
            myPrescription.setVisibility(View.GONE);
            allReports.setVisibility(View.VISIBLE);
            tvPoint.setText("审方通过");
            tvTotalCount.setText("审方总量");
            tvRefuseCount.setText("审方驳回");
            mShenfangRecode.setVisibility(View.VISIBLE);
            pictureRecord.setVisibility(View.GONE);
            renewalRecord.setVisibility(View.GONE);
            videoRecord.setVisibility(View.GONE);
            mServiceRecord.setVisibility(View.GONE);
            mdiagnoseRecord.setVisibility(View.GONE);
            mypoint.setVisibility(View.GONE);
            totalPatientLayout.setVisibility(View.VISIBLE);

            shareApp.setVisibility(View.GONE);
        } else if (mDoctype == 2) {
            //护士展示
            pictureRecord.setText("咨询记录");
            videoRecord.setText("远程记录");
            videoRecord.setVisibility(View.GONE);
            // mServiceRecord.setVisibility(View.GONE);
            renewalRecord.setVisibility(View.GONE);
            mdiagnoseRecord.setVisibility(View.GONE);
            mFunSignature.setVisibility(View.GONE);
            chickUnit.setVisibility(View.GONE);
            myPrescription.setVisibility(View.GONE);
            mypoint.setVisibility(View.VISIBLE);
            totalPatientLayout.setVisibility(View.GONE);
            shareApp.setVisibility(View.VISIBLE);
        } else if (mDoctype == 1) {
            mFunContainer.setVisibility(View.VISIBLE);
            if (BuildConfig.IS_DEBUG) {
                mVideoContainer.setVisibility(View.VISIBLE);
            }
            mypoint.setVisibility(View.VISIBLE);
        }

        changeAuthentication();
    }

    /**
     * 医生状态(0-未审核，1-审核通过，2-审核失败，3-审核中)
     */
    private void changeAuthentication() {
        int status = SpUtils.getInt(Contants.Status, -1);
        if (status == 0) {
            tvAuthentication.setText("未认证");
        } else if (status == 2) {
            tvAuthentication.setText("认证失败");
        } else if (status == 3) {
            tvAuthentication.setText("审核中");
        } else if (status == 1) {
            tvAuthentication.setText("已认证");
        } else {
            tvAuthentication.setText("未认证");
        }
    }

    @Override
    protected void initListener() {
        shareApp.setOnClickListener(this);
        topRight.setOnClickListener(this);
        chickUnit.setOnClickListener(this);
        myPrescription.setOnClickListener(this);
        pictureRecord.setOnClickListener(this);
        renewalRecord.setOnClickListener(this);
        videoRecord.setOnClickListener(this);
        mFunSignature.setOnClickListener(this);
        mFunSetting.setOnClickListener(this);
        mFunReport.setOnClickListener(this);
        doctorScoreLayout.setOnClickListener(this);
        doctorIncomeLayout.setOnClickListener(this);
        totalPatientLayout.setOnClickListener(this);
        allReports.setOnClickListener(this);
        mServiceRecord.setOnClickListener(this);
        mShenfangRecode.setOnClickListener(this);
        mAudio.setOnClickListener(this);
        mdiagnoseRecord.setOnClickListener(this);
        tvAuthentication.setOnClickListener(this);
        mPoints.setOnClickListener(this);
        mypoint.setOnClickListener(this);
        mkefu.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        queryDoctorInfoByDrId();
        if (BuildConfig.DEBUG) {
            BJCASDK.getInstance().setServerUrl(EnvType.INTEGRATE);
        } else {
            BJCASDK.getInstance().setServerUrl(EnvType.PUBLIC);
        }
        if (mDoctype == 3) {
            //获取审方师数据统计
            getPrescriptionData();
        } else {
            QueryDoctorSummary();
        }

        loadBanner();
        getPoint();
    }


    private void loadBanner() {
        String bannerUrl = HttpUrl.QueryAdBannerInfo + "?classId=" + 6;
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        mMyOkhttp.get().url(bannerUrl).headers(headMap).tag(this).enqueue(new GsonResponseHandler<AdBannerInfo>() {
            @Override
            public void onSuccess(int statusCode, AdBannerInfo entituy) {
                final List<AdBannerInfo.DataBean> data = entituy.getData();
                if (data == null || data.size() == 0) return;
                List<String> images = new ArrayList<>();
                for (AdBannerInfo.DataBean datum : data) {
                    images.add(datum.getContent());
                }
                dealBannerData(images);

            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }


    public void showBottomDialog() {
        mDialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        mInflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share, null);
        //初始化控件
        mFriend = (LinearLayout) mInflate.findViewById(R.id.mWechatFriend);
        mZoom = (LinearLayout) mInflate.findViewById(R.id.mWechatZone);
        mTvCancel = mInflate.findViewById(R.id.mCancel);
        mFriend.setOnClickListener(this);
        mZoom.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        //将布局设置给Dialog
        mDialog.setContentView(mInflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = mDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 10;//设置Dialog距离底部的距离
//       将属性设置给窗体
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        mDialog.show();//显示对话框
    }


  /*  private void getImagePath(String imgUrl) {
        FutureTarget<File> future = Glide.with(this)
                .load(imgUrl)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        try {
            File cacheFile = future.get();
            mSdPath = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }*/


    private void share(SHARE_TYPE type) {
      /*  FileInputStream fis = null;
        try {
            fis = new FileInputStream(mSdPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis == null){
            return;
        }
        Bitmap thumb  = BitmapFactory.decodeStream(fis);*/

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = YUNAPPLOAD;
        //用WXWebpageObject对象初始化一个WXMediaMessage，天下标题，描述
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "点击下载芸医生APP";
        msg.description = "芸医生为用户提供咨询、续方、复诊、康复护理、诊后随访等医疗健康服务";
        //这块需要注意，图片的像素千万不要太大，不然的话会调不起来微信分享，
        //我在做的时候和我们这的UIMM说随便给我一张图，她给了我一张1024*1024的图片
        //当时也不知道什么原因，后来在我的机智之下换了一张像素小一点的图片好了！
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_icon);
        msg.setThumbImage(thumb);

         /* WXImageObject imageObject = new WXImageObject(bitmap);
          WXMediaMessage msg = new WXMediaMessage();
          msg.mediaObject = imageObject;*/
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        switch (type) {
            case Type_WXSceneSession:
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case Type_WXSceneTimeline:
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
        }

        iwxapi.sendReq(req);


    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

  /*  @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent,this);
    }*/


    @Override
    public void onResp(BaseResp baseResp) {
        String result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(UpdatePushView userEvent) {
        int action = userEvent.action;
        switch (action) {

            case 7:
                changeAuthentication();
                break;
        }

    }


    private void dealBannerData(List<String> images) {
        if (images == null || images.size() == 0) return;
        //设置banner样式
//                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideMediaLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//                mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()) {
            case R.id.picture_record:
                Intent pictureIntent = new Intent(getContext(), OnlineConsultingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("unReadList", (Serializable) HomeFragment.mPictureUnreadMessageList);
                bundle.putSerializable("allUnReadList", (Serializable) HomeFragment.mAllUnreadMessageList);
                pictureIntent.putExtras(bundle);
                startActivity(pictureIntent);
                break;
            case R.id.renewal_record:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    Intent renewalIntent = new Intent(getContext(), OnlineRenewalPartyActivity.class);
                    Bundle renewalBundle = new Bundle();
                    renewalBundle.putSerializable("allUnReadList", (Serializable) HomeFragment.mAllUnreadMessageList);
                    renewalBundle.putSerializable("unReadList", (Serializable) HomeFragment.mRenewalUnreadMessageList);
                    renewalIntent.putExtras(renewalBundle);
                    startActivity(renewalIntent);
                }
                break;
            case R.id.video_record:
                Intent videoIntent = new Intent(getContext(), VideoInterrogationActivity.class);
                Bundle videoBundle = new Bundle();
                videoBundle.putSerializable("unReadList", (Serializable) HomeFragment.mVideoUnreadMessageList);
                videoBundle.putSerializable("allUnReadList", (Serializable) HomeFragment.mAllUnreadMessageList);
                videoIntent.putExtras(videoBundle);
                startActivity(videoIntent);
                break;
            case R.id.mFunSignature:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                 /*   boolean isExists = BJCASDK.getInstance().existsCert(getContext());
                    final   boolean ExistStamp = BJCASDK.getInstance().existsStamp(getContext());
                    if (!isExists) {
                        BJCASDK.getInstance().certDown(mActivity, Contants.clientId, SpUtils.getString(Contants.userName), new YWXListener() {
                            @Override
                            public void callback(String s) {
                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(s, YWXListenerEntity.class);
                                String status = yWXListenerEntity.getStatus();
                                String message = yWXListenerEntity.getMessage();
                                if (status != null && status.equals("0")) {
                                    if (!ExistStamp) {
                                        BJCASDK.getInstance().drawStamp(mActivity,	Contants.clientId,	new	YWXListener()	{
                                            @Override
                                            public	void callback(String msg)	{
                                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                                String status = yWXListenerEntity.getStatus();
                                                String message = yWXListenerEntity.getMessage();
                                                if (status != null && status.equals("0")) {
                                                    String path=	BJCASDK.getInstance().getStampPic(mContext);
                                                    Intent intent = new Intent(mContext, ShowOriginPictureActivity.class);
                                                    intent.putExtra("base64", path);
                                                    mContext.startActivity(intent);
                                                }
                                                else {
                                                    Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        String path=	BJCASDK.getInstance().getStampPic(mContext);
                                        Intent intent = new Intent(mContext, ShowOriginPictureActivity.class);
                                        intent.putExtra("base64", path);
                                        mContext.startActivity(intent);
                                    }
                                }
                                else {
                                    Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        //   BJCASDK.getInstance().clearCert(ChatActivity.this);
                        Log.i("zdp", "证书已下载");
                    }
                    if (!ExistStamp) {
                        BJCASDK.getInstance().drawStamp(mActivity,	Contants.clientId,	new	YWXListener()	{
                            @Override
                            public	void callback(String msg)	{
                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                String status = yWXListenerEntity.getStatus();
                                String message = yWXListenerEntity.getMessage();
                                if (status != null && status.equals("0")) {
                                    String path=	BJCASDK.getInstance().getStampPic(mContext);
                                    Intent intent = new Intent(mContext, ShowOriginPictureActivity.class);
                                    intent.putExtra("base64", path);
                                    mContext.startActivity(intent);
                                }
                                else {
                                    Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {

                        String path=	BJCASDK.getInstance().getStampPic(mContext);
                        Intent intent = new Intent(mContext, ShowOriginPictureActivity.class);
                        intent.putExtra("base64", path);
                        mContext.startActivity(intent);
                    }*/
                    Intent intent = new Intent(mContext, MyCertActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.mFunSetting:
                Intent systemSettingIntent = new Intent(getContext(), SystemSettingActivity.class);
                startActivity(systemSettingIntent);


                break;
            case R.id.mFunReport:
                Intent serviceSettingIntent = new Intent(getContext(), ServiceSettingActivity.class);
                startActivity(serviceSettingIntent);
                break;
            case R.id.chick_unit:
                Intent chickIntent = new Intent(getContext(), ChickUnitActivity.class);
                startActivity(chickIntent);
                break;
            case R.id.doctor_score_layout:
                if (mDoctype != 3) {
                    Intent doctorPraiseIntent = new Intent(getContext(), DoctorPraiseActivity.class);
                    startActivity(doctorPraiseIntent);
                }
                break;
            case R.id.all_reports:
                if (mDoctype != 3) {
                    Intent allReportsIntent = new Intent(getContext(), AllReportsActivity.class);
                    startActivity(allReportsIntent);
                }

                break;
            case R.id.doctor_income_layout:
                if (mDoctype != 3) {
                    Intent inComeIntent = new Intent(getContext(), ArchivesActivity.class);
                    inComeIntent.putExtra("action", "income");
                    inComeIntent.putExtra("prescriptionMessage", SpUtils.getString(Contants.LoginJson));
                    startActivity(inComeIntent);
                }

                break;
            case R.id.total_patient_layout:
                if (mDoctype != 3) {
                    EventBus.getDefault().post(new TranslateToPatientView(true));
                }
                break;
            case R.id.my_prescription:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    Intent myPrescriptionIntent = new Intent(getContext(), MyPrescriptionActivity.class);
                    startActivity(myPrescriptionIntent);

                }
                break;
            case R.id.mServiceRecord:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    //定制服务包
                    Intent medicalServiceIntent = new Intent(getContext(), MedicalServiceActivity.class);
                    medicalServiceIntent.putExtra("type", 1);
                    startActivity(medicalServiceIntent);
                }

                break;

            case R.id.share_app:
                //  new translateTask().execute("");
                // showBottomDialog();
                Intent pointintent = new Intent(getContext(), MyPointsRewardActivity.class);
                startActivity(pointintent);
                break;
            case R.id.mWechatFriend:
                share(SHARE_TYPE.Type_WXSceneSession);
                mDialog.dismiss();
                break;
            case R.id.mWechatZone:
                share(SHARE_TYPE.Type_WXSceneTimeline);
                mDialog.dismiss();
                break;
            case R.id.mCancel:
                if (mDialog.isShowing())
                    mDialog.dismiss();
                break;


            case R.id.mShenfangRecode:
                Intent intentPrescription = PrescriptionListActivity.newIntent(getContext());
                startActivity(intentPrescription);
                break;
            case R.id.mAudio:
                Intent audio = new Intent(getContext(), AllApplicationActivity.class);
                audio.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                startActivity(audio);
                break;

            case R.id.mdiagnoseRecord:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    Intent treatIntent = new Intent(getActivity(), TreatActivity.class);
                    startActivity(treatIntent);

                }
                break;
            case R.id.tv_authentication:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                }

                break;
            case R.id.mPoints:
                Intent point = new Intent(getContext(), MyPointsActivity.class);
                point.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                startActivity(point);
                break;
            case R.id.mypoint:
                Intent jifen = new Intent(getContext(), MyPointsActivity.class);
                jifen.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                startActivity(jifen);
                break;
            case R.id.mkefu:
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("Name",MyApplication.mDoctorInfoByIdEntity.getDrName()+"");
                intent.putExtra("AccountId",MyApplication.mDoctorInfoByIdEntity.getDrId()+"");
                intent.putExtra("Sex",MyApplication.mDoctorInfoByIdEntity.getDrSex()+"");
                intent.putExtra("Mobile",MyApplication.mDoctorInfoByIdEntity.getMobile()+"");
                startActivity(intent);
                break;
        }
    }

    /**
     * 当界面重新展示时（fragment.show）,调用onrequest刷新界面
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDialog != null) {
            mDialog.cancel();
            mDialog = null;
        }
        unbinder.unbind();
    }

    @OnClick(R.id.civImg)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civImg:
                //   toActivity(new Intent(getContext(), PersonalDataActivity.class));
                Intent intent = new Intent(getContext(), PersonalDataActivity.class);
                intent.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                startActivity(intent);
                //toActivity(intent);
                break;

        }
    }

    private void QueryDoctorSummary() {
        String url = HttpUrl.QueryDoctorSummary + "?DrId=" + SpUtils.getInt(Contants.Id, 0);
        loading(true);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        mMyOkhttp.get().url(url).headers(headMap).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<DoctorSummaryEntity>>() {
            @Override
            public void onSuccess(int statusCode, final ResponseEntity<DoctorSummaryEntity> entity) {
                LoadDialog.clear();
                if (entity.getCode() == 0) {
                    DoctorSummaryEntity DoctorSummaryEntity = entity.getData();
                    if (getActivity() == null) return;
                    if (DoctorSummaryEntity != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doctorScore.setText(String.valueOf(entity.getData().getPraiseRate()));
                                doctorIncome.setText(String.valueOf(entity.getData().getTotalIncome()));
                                totalPatient.setText(String.valueOf(entity.getData().getPatientCount()));

                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doctorScore.setText("0");
                                doctorIncome.setText("0");
                                totalPatient.setText("0");

                            }
                        });
                    }
                } else {
                    toast(entity.getMessage());
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                CommonMethod.requestError(statusCode, errorMsg);
            }
        });
    }

    private void queryDoctorInfoByDrId() {
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        String url = HttpUrl.QueryDoctorInfoByDrId + "?DrId=" + SpUtils.getInt(Contants.Id, 0);
        loading(true);
        mMyOkhttp.get().url(url).headers(headMap).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<DoctorInfoByIdEntity>>() {
            @Override
            public void onSuccess(int statusCode, final ResponseEntity<DoctorInfoByIdEntity> entity) {
                if (entity.getCode() == 0) {
                    LoadDialog.clear();

                    mDoctorInfoByIdEntity = entity.getData();
                    MyApplication.mDoctorInfoByIdEntity = mDoctorInfoByIdEntity;
                    if (mDoctorInfoByIdEntity != null) {
                        EventBus.getDefault().postSticky(mDoctorInfoByIdEntity);
                        String positionName = mDoctorInfoByIdEntity.getPositionName();
                        final int seeCount = mDoctorInfoByIdEntity.getConsultCount() + mDoctorInfoByIdEntity.getConsultVolume() + mDoctorInfoByIdEntity.getOnPrescription() + mDoctorInfoByIdEntity.getTelenursing() + mDoctorInfoByIdEntity.getVolumeVideo();
                        final int PatientAttentDrNum = mDoctorInfoByIdEntity.getPatientAttentDrNum();
                        doctorPath = mDoctorInfoByIdEntity.getPicturePath();
                        if (positionName != null) {
                            title.setText(positionName);
                            SpUtils.put(Contants.Position, mDoctorInfoByIdEntity.getPosition());
                        }
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String picturePath = mDoctorInfoByIdEntity.getPicturePath();
                                seeing.setText("问诊量：" + seeCount);
                                amountOfAttention.setText("关注量：" + PatientAttentDrNum);
                                if (picturePath != null) {
                                    Glide.with(MyApplication.getContext())
                                            .load(picturePath)
                                            .into(civImg);
                                } else {
                                    civImg.setBackgroundResource(R.drawable.doctor_default_img);
                                }


                            }
                        });
                    }

                } else {
                    toast(entity.getMessage());
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                CommonMethod.requestError(statusCode, errorMsg);
            }
        });
    }

    private class translateTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_logo, options);

            mBitmap = CodeUtils.createImage(YUNAPPLOAD, 400, 400, bitmap2);
            return null;
        }

        @Override
        protected void onPreExecute() {
            loading(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            LoadDialog.clear();
            showBottomDialog();
            super.onPostExecute(bitmap);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(UpdatePatientCountEntity userEvent) {
        if (mDoctype != 3) {
            QueryDoctorSummary();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(UpdateImageView userEvent) {
        queryDoctorInfoByDrId();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void getPrescriptionData() {
        Map<String, String> map = new HashMap<>();
        map.put("AuditorId", String.valueOf(SpUtils.getInt(Contants.Id, 0))); //状态（-1：全部，0：待审核，1：已审核，2：被驳回）（必填）
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", MyApplication.token);
        mMyOkhttp.post().url(HttpUrl.getPrescriptionDetail).headers(headMap).params(map).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<PrescriptionDataEntity>>() {
            @Override
            public void onSuccess(int statusCode, ResponseEntity<PrescriptionDataEntity> entity) {
                LoadDialog.clear();

                if (entity.getCode() == 0) {
                    PrescriptionDataEntity prescriptionDataEntity = entity.getData();
                    if (prescriptionDataEntity != null) {
                        doctorScore.setText(prescriptionDataEntity.getPassCount() + "");
                        doctorIncome.setText(prescriptionDataEntity.getTotal() + "");
                        totalPatient.setText(prescriptionDataEntity.getRejectCount() + "");
                    } else {
                        doctorScore.setText("0");
                        doctorIncome.setText("0");
                        totalPatient.setText("0");
                    }
                } else {
                    doctorScore.setText("0");
                    doctorIncome.setText("0");
                    totalPatient.setText("0");
                    toast(entity.getMessage());
                }

            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                CommonMethod.requestError(statusCode, errorMsg);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(PrescriptionDataEntity dataEntity) {
        Log.d(TAG, "收到数据统计");
        doctorScore.setText(dataEntity.getPassCount() + "");
        doctorIncome.setText(dataEntity.getTotal() + "");
        totalPatient.setText(dataEntity.getRejectCount() + "");

    }


    /**
     * 改变医生认证状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void authEvent(String event) {
        if (event.equals(AuthenticationCommentUtil.AUTH)) {
            changeAuthentication();
        }
    }

    private void getPoint() {

        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        HashMap<String, String> params = new HashMap<>();
        params.put("DrId", String.valueOf(SpUtils.getInt(Contants.Id, -1)));
        mMyOkhttp.get().url(HttpUrl.isDoctorSign).headers(headMap).params(params).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<DoctorSignEntity>>() {
            @Override
            public void onSuccess(int statusCode, ResponseEntity<DoctorSignEntity> response) {

                if (response.getCode() == 0) {
                    try {
                        if (response.getData() != null) {
                            tvJifen.setText(response.getData().getCurrentIntegral() + "");
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPoint();
    }
}
