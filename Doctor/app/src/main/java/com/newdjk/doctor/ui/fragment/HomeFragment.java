package com.newdjk.doctor.ui.fragment;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxq.okhttp.response.GsonResponseHandler;
import com.newdjk.doctor.MyApplication;
import com.newdjk.doctor.R;
import com.newdjk.doctor.basic.BasicFragment;
import com.newdjk.doctor.model.HttpUrl;
import com.newdjk.doctor.tools.CommonMethod;
import com.newdjk.doctor.tools.Contants;
import com.newdjk.doctor.ui.activity.AllApplicationActivity;
import com.newdjk.doctor.ui.activity.BannerDetailActivity;
import com.newdjk.doctor.ui.activity.DoctorHomeCardActivity;
import com.newdjk.doctor.ui.activity.FenjiCooperationActivity;
import com.newdjk.doctor.ui.activity.FenjiZhuanzhenActivity;
import com.newdjk.doctor.ui.activity.IM.ChatActivity;
import com.newdjk.doctor.ui.activity.MyCheckCenterActivity;
import com.newdjk.doctor.ui.activity.MypharmacyActivity;
import com.newdjk.doctor.ui.activity.OnlineConsultingActivity;
import com.newdjk.doctor.ui.activity.OnlineRenewalPartyActivity;
import com.newdjk.doctor.ui.activity.PrescriptionListActivity;
import com.newdjk.doctor.ui.activity.PushListActivity;
import com.newdjk.doctor.ui.activity.TreatActivity;
import com.newdjk.doctor.ui.activity.VideoInterrogationActivity;
import com.newdjk.doctor.ui.adapter.FunctionAdapter;
import com.newdjk.doctor.ui.entity.AdBannerInfo;
import com.newdjk.doctor.ui.entity.AllDoctorCheckEntity;
import com.newdjk.doctor.ui.entity.AllRecordForDoctorEntity;
import com.newdjk.doctor.ui.entity.AppEntity;
import com.newdjk.doctor.ui.entity.AppLicationEntity;
import com.newdjk.doctor.ui.entity.DoctorInfoByIdEntity;
import com.newdjk.doctor.ui.entity.DoctorMedicalRecordsEntity;
import com.newdjk.doctor.ui.entity.Entity;
import com.newdjk.doctor.ui.entity.FunctionDataEntity;
import com.newdjk.doctor.ui.entity.JpushDataEntity;
import com.newdjk.doctor.ui.entity.JpushDataListEntity;
import com.newdjk.doctor.ui.entity.PrescriptionEntity;
import com.newdjk.doctor.ui.entity.PushDataDaoEntity;
import com.newdjk.doctor.ui.entity.ResponseEntity;
import com.newdjk.doctor.ui.entity.ServiceTypeOfPatientDoctorEntity;
import com.newdjk.doctor.ui.entity.UnreadMessageEntity;
import com.newdjk.doctor.ui.entity.UpdateImMessageEntity;
import com.newdjk.doctor.ui.entity.UpdatePushView;
import com.newdjk.doctor.ui.entity.YWXListenerEntity;
import com.newdjk.doctor.utils.AppLicationUtils;
import com.newdjk.doctor.utils.AppUpdateUtils;
import com.newdjk.doctor.utils.AppUtils;
import com.newdjk.doctor.utils.GlideMediaLoader;
import com.newdjk.doctor.utils.HomeUtils;
import com.newdjk.doctor.utils.SQLiteUtils;
import com.newdjk.doctor.utils.SpUtils;
import com.newdjk.doctor.utils.ToastUtil;
import com.newdjk.doctor.views.CircleImageView;
import com.newdjk.doctor.views.DialogProgress;
import com.newdjk.doctor.views.GroupButtonDialog;
import com.newdjk.doctor.views.LoadDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.kit.YWXListener;
import lib_zxing.activity.CaptureActivity;
import lib_zxing.activity.CodeUtils;


/**
 * 首页
 */
public class HomeFragment extends BasicFragment {


    private static final String TAG = "HomeFragment---1";
    LinearLayout mission;
    Unbinder unbinder;
    @BindView(R.id.sb_default)
    SwitchButton sbDefault;
    @BindView(R.id.iv_switch_button)
    ImageView ivSwitchButton;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_right)
    ImageView topRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.relat_titlebar)
    RelativeLayout relatTitlebar;
    @BindView(R.id.liear_titlebar)
    LinearLayout liearTitlebar;
    @BindView(R.id.mBanner)
    Banner mBanner;
    @BindView(R.id.im_picture)
    ImageView imPicture;
    @BindView(R.id.picture_unread_num)
    TextView pictureUnreadNum;
    @BindView(R.id.picture_unread_num_layout)
    RelativeLayout pictureUnreadNumLayout;
    @BindView(R.id.mFuncOne)
    TextView mFuncOne;
    @BindView(R.id.online_consult)
    LinearLayout onlineConsult;
    @BindView(R.id.im_video)
    ImageView imVideo;
    @BindView(R.id.video_unread_num)
    TextView videoUnreadNum;
    @BindView(R.id.video_unread_num_layout)
    RelativeLayout videoUnreadNumLayout;
    @BindView(R.id.mFuncTwo)
    TextView mFuncTwo;
    @BindView(R.id.my_physician_visits)
    LinearLayout myPhysicianVisits;
    @BindView(R.id.im_renewal)
    ImageView imRenewal;
    @BindView(R.id.renewal_unread_num)
    TextView renewalUnreadNum;
    @BindView(R.id.renewal_unread_num_layout)
    RelativeLayout renewalUnreadNumLayout;
    @BindView(R.id.mFuncThree)
    TextView mFuncThree;
    @BindView(R.id.online_renewal_party)
    LinearLayout onlineRenewalParty;
    @BindView(R.id.mFuncFour)
    TextView mFuncFour;
    @BindView(R.id.my_pharmacy)
    LinearLayout myPharmacy;
    @BindView(R.id.doctor_layout)
    LinearLayout doctorLayout;
    @BindView(R.id.im_heart)
    ImageView imHeart;
    @BindView(R.id.tv_unread_num_heart)
    TextView tvUnreadNumHeart;
    @BindView(R.id.rv_heart_unread_num)
    RelativeLayout rvHeartUnreadNum;
    @BindView(R.id.tv_heartcheck)
    TextView tvHeartcheck;
    @BindView(R.id.lv_heartcheck)
    LinearLayout lvHeartcheck;
    @BindView(R.id.im_chufang)
    ImageView imChufang;
    @BindView(R.id.tv_unread_num_chufang)
    TextView tvUnreadNumChufang;
    @BindView(R.id.rv_chufang_unread_num)
    RelativeLayout rvChufangUnreadNum;
    @BindView(R.id.lv_chufangcheck)
    LinearLayout lvChufangcheck;
    @BindView(R.id.invitate_patient_icon)
    ImageView invitatePatientIcon;
    @BindView(R.id.tv_chufang_check)
    TextView tvChufangCheck;
    @BindView(R.id.invitate_patient)
    LinearLayout invitatePatient;
    @BindView(R.id.performance_record_icon)
    ImageView performanceRecordIcon;
    @BindView(R.id.tv_performance_record)
    TextView tvPerformanceRecord;
    @BindView(R.id.performance_record)
    LinearLayout performanceRecord;
    @BindView(R.id.treatment)
    ImageView treatment;
    @BindView(R.id.tv_unread_num_treatment)
    TextView tvUnreadNumTreatment;
    @BindView(R.id.rv_treatment_unread_num)
    RelativeLayout rvTreatmentUnreadNum;
    @BindView(R.id.tv_diagnosis_and_treatment)
    TextView tvDiagnosisAndTreatment;
    @BindView(R.id.diagnosis_and_treatment)
    LinearLayout diagnosisAndTreatment;
    @BindView(R.id.scan)
    LinearLayout scan;
    @BindView(R.id.lv_empty1)
    LinearLayout lvEmpty1;
    @BindView(R.id.lv_empty2)
    LinearLayout lvEmpty2;
    @BindView(R.id.lv_function2)
    LinearLayout lvFunction2;
    @BindView(R.id.call_test)
    TextView callTest;
    @BindView(R.id.system_avatar)
    CircleImageView systemAvatar;
    @BindView(R.id.system_unread_num)
    TextView systemUnreadNum;
    @BindView(R.id.system_unread_num_layout)
    LinearLayout systemUnreadNumLayout;
    @BindView(R.id.system_avatar_layout)
    RelativeLayout systemAvatarLayout;
    @BindView(R.id.system_name)
    TextView systemName;
    @BindView(R.id.system_content)
    TextView systemContent;
    @BindView(R.id.system_message_layout)
    RelativeLayout systemMessageLayout;
    @BindView(R.id.reports_avatar)
    CircleImageView reportsAvatar;
    @BindView(R.id.reports_unread_num)
    TextView reportsUnreadNum;
    @BindView(R.id.reports_unread_num_layout)
    LinearLayout reportsUnreadNumLayout;
    @BindView(R.id.reports_avatar_layout)
    RelativeLayout reportsAvatarLayout;
    @BindView(R.id.reports_name)
    TextView reportsName;
    @BindView(R.id.reports_content)
    TextView reportsContent;
    @BindView(R.id.reports_message_layout)
    RelativeLayout reportsMessageLayout;
    @BindView(R.id.chufang_avatar)
    CircleImageView chufangAvatar;
    @BindView(R.id.chufang_unread_num)
    TextView chufangUnreadNum;
    @BindView(R.id.chufang_unread_num_layout)
    LinearLayout chufangUnreadNumLayout;
    @BindView(R.id.chufang_avatar_layout)
    RelativeLayout chufangAvatarLayout;
    @BindView(R.id.chufang_name)
    TextView chufangName;
    @BindView(R.id.chufang_content)
    TextView chufangContent;
    @BindView(R.id.chufang_message_layout)
    RelativeLayout chufangMessageLayout;
    @BindView(R.id.function_list)
    RecyclerView functionList;
    @BindView(R.id.function_layout)
    LinearLayout functionLayout;
    @BindView(R.id.lv_emptyA)
    LinearLayout lvEmptyA;
    @BindView(R.id.lv_emptyB)
    LinearLayout lvEmptyB;
    @BindView(R.id.lv_emptyC)
    LinearLayout lvEmptyC;


    // private MessageAdapter mAdapter;
    private int mDoctype = 0;
    private Gson mGson;
    public static List<UnreadMessageEntity> mAllUnreadMessageList;
    public static List<UnreadMessageEntity> mRenewalUnreadMessageList;
    public static List<UnreadMessageEntity> mPictureUnreadMessageList;
    public static List<UnreadMessageEntity> mVideoUnreadMessageList;
    private long mPictureUnRead = 0;
    private long mVideoUnRead = 0;
    private long mRenewalUnRead = 0;
    private DoctorInfoByIdEntity mDoctorInfoByIdEntity;
    private int mId = 0;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int mIsOnLine;
    List<ServiceTypeOfPatientDoctorEntity> mServiceList;
    public static String serviceTime;
    private GroupButtonDialog mDialog;
    private DialogProgress mProgress;
    private int intTotalYaoshi = 0;
    private int mStatus;
    List<AppLicationEntity> listuse = new ArrayList<>();
    FunctionAdapter mFunctionAdapter;
    private FunctionDataEntity mFunctionDataEntity;
    List<AdBannerInfo.DataBean> bannerdata = new ArrayList<>();
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Notification notification;
    private int mMustupdate = 0;
    private int channelID = 1;
    private AppEntity appentity;

    public static HomeFragment getFragment() {
        return new HomeFragment();
    }

    @Override
    protected int initViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mStatus = SpUtils.getInt(Contants.Status, 0);
        topTitle.setText(SpUtils.getString(Contants.Name));
        topRight.setVisibility(View.VISIBLE);
        topRight.setImageResource(R.mipmap.doctor_home_card);
        mProgress = new DialogProgress(getContext());
        mId = SpUtils.getInt(Contants.Id, 0);
        //SQLiteUtils.getInstance().deleteAllPushData();
        mAllUnreadMessageList = new ArrayList<>();
        mRenewalUnreadMessageList = new ArrayList<>();
        mPictureUnreadMessageList = new ArrayList<>();
        mVideoUnreadMessageList = new ArrayList<>();
        mGson = new Gson();
        mDoctype = SpUtils.getInt(Contants.DocType, 0);
        //贪心判读红点显示
        //普通
        if (mDoctype == 1) {
            getConsultRecordList();

            //药师
        } else if (mDoctype == 3) {
            getOnlinePartyList(1, "0");

        }
        if (checkPermission()) {
            checkUpdate();
        }

        queryDoctorMedicalRecords();
    }

    @Override
    protected void initListener() {
        diagnosisAndTreatment.setOnClickListener(this);
        performanceRecord.setOnClickListener(this);
        invitatePatient.setOnClickListener(this);
        systemMessageLayout.setOnClickListener(this);
        reportsMessageLayout.setOnClickListener(this);
        onlineConsult.setOnClickListener(this);
        myPharmacy.setOnClickListener(this);
        callTest.setOnClickListener(this);
        topRight.setOnClickListener(this);
        onlineRenewalParty.setOnClickListener(this);
        myPhysicianVisits.setOnClickListener(this);
        scan.setOnClickListener(this);
        lvHeartcheck.setOnClickListener(this);
        lvChufangcheck.setOnClickListener(this);
        // topLeft.setOnCheckedChangeListener(new );

        ivSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOnLine == 1) {
                    if (mDialog == null) {
                        mDialog = new GroupButtonDialog(getContext());
                    }
                    mDialog.show("", "", new GroupButtonDialog.DialogListener() {
                        @Override
                        public void cancel() {
                            mDialog = null;
                        }

                        @Override
                        public void confirm() {
                            mDialog = null;
                            updateOnline(0);
                        }
                    });
                } else {
                    updateOnline(1);
                }

            }
        });

    }

    private void updateOnline(final int flag) {
        loading(true);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        HashMap<String, String> params = new HashMap<>();
        params.put("DrId", String.valueOf(SpUtils.getInt(Contants.Id, -1)));
        params.put("IsOnline", String.valueOf(flag));
        mMyOkhttp.get().url(HttpUrl.UpdateDoctorOnline).headers(headMap).params(params).tag(this).enqueue(new GsonResponseHandler<Entity>() {
            @Override
            public void onSuccess(int statusCode, Entity response) {
                LoadDialog.clear();
                if (response.getCode() == 0) {
                    if (flag == 1) {
                        mIsOnLine = 1;
                        setOnLineStatus(1);
                    } else {
                        mIsOnLine = 0;
                        setOnLineStatus(0);
                    }

                } else {
                    toast(response.getMessage());
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                LoadDialog.clear();
                toast(errorMsg);
            }
        });
    }

    @Override
    protected void initData() {
        if (mDoctype == 3) {
            functionList.setVisibility(View.GONE);
            functionLayout.setVisibility(View.VISIBLE);
            systemMessageLayout.setVisibility(View.VISIBLE);
            reportsMessageLayout.setVisibility(View.GONE);
            doctorLayout.setVisibility(View.GONE);
            ivSwitchButton.setVisibility(View.GONE);
            scan.setVisibility(View.VISIBLE);
            lvHeartcheck.setVisibility(View.GONE);
            chufangMessageLayout.setVisibility(View.GONE);
            lvChufangcheck.setVisibility(View.VISIBLE);
            lvEmpty2.setVisibility(View.INVISIBLE);
            performanceRecord.setVisibility(View.GONE);
            invitatePatient.setVisibility(View.GONE);
            diagnosisAndTreatment.setVisibility(View.GONE);
            myPharmacy.setVisibility(View.GONE);

        } else if (mDoctype == 2) {
            functionList.setVisibility(View.GONE);
            functionLayout.setVisibility(View.VISIBLE);
            //护士展示
            myPhysicianVisits.setVisibility(View.GONE);
            lvFunction2.setVisibility(View.GONE);
            mFuncOne.setText("护理咨询");
            mFuncTwo.setText("远程护理");
         /*   myPhysicianVisits.setVisibility(View.VISIBLE);
            videoUnreadNumLayout.setVisibility(View.VISIBLE);*/
            lvEmptyA.setVisibility(View.VISIBLE);
            lvEmptyB.setVisibility(View.VISIBLE);
            lvEmptyC.setVisibility(View.VISIBLE);
            onlineRenewalParty.setVisibility(View.INVISIBLE);
            myPharmacy.setVisibility(View.INVISIBLE);
            lvHeartcheck.setVisibility(View.GONE);
            diagnosisAndTreatment.setVisibility(View.GONE);

        } else if (mDoctype == 1) {
            functionList.setVisibility(View.VISIBLE);
            functionLayout.setVisibility(View.GONE);
            //  getNewReportUnreadUnm();
            //   requestAllNewReportPaientMessage();
            doctorLayout.setVisibility(View.VISIBLE);
            scan.setVisibility(View.GONE);
            getAllUseFunction();


        }
        getAllPushList();
        if (mFunctionAdapter != null) {


            mFunctionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Log.d(TAG, "常用" + position);
                    int type = listuse.get(position).getAppID();
                    switch (type) {
                        case 0:
                            Intent audio = new Intent(getContext(), AllApplicationActivity.class);
                            startActivity(audio);
                            break;
                        case 1:
                            Intent intent = new Intent(getActivity(), OnlineConsultingActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("unReadList", (Serializable) mPictureUnreadMessageList);
                            bundle.putSerializable("allUnReadList", (Serializable) mAllUnreadMessageList);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case 2:
                            mStatus = SpUtils.getInt(Contants.Status, 0);
                            if (mStatus != 1) {
                                AppUtils.checkAuthenStatus(mStatus, getContext());
                            } else {
                                Intent onlineRenewalPartyIntent = new Intent(mContext, OnlineRenewalPartyActivity.class);
                                Bundle renewalBundle = new Bundle();
                                renewalBundle.putSerializable("allUnReadList", (Serializable) mAllUnreadMessageList);
                                renewalBundle.putSerializable("unReadList", (Serializable) mRenewalUnreadMessageList);
                                onlineRenewalPartyIntent.putExtras(renewalBundle);
                                startActivity(onlineRenewalPartyIntent);
                            }
                            break;
                        case 3:
                            mStatus = SpUtils.getInt(Contants.Status, 0);
                            if (mStatus != 1) {
                                AppUtils.checkAuthenStatus(mStatus, getContext());
                            } else {
                                Intent treatIntent = new Intent(getActivity(), TreatActivity.class);
                                startActivity(treatIntent);
                            }
                            break;
                        case 4:
                            mStatus = SpUtils.getInt(Contants.Status, 0);
                            //toast("胎心判读");
                            if (mStatus != 1) {
                                AppUtils.checkAuthenStatus(mStatus, getContext());
                            } else {
                                //toast("胎心判读");
                                Intent intentCheckCenter = MyCheckCenterActivity.newIntent(getContext());
                                startActivity(intentCheckCenter);

                            }
                            break;
                        case 5:
                            Intent performanceIntent = new Intent(getActivity(), MypharmacyActivity.class);
                            performanceIntent.putExtra("action", 2);
                            startActivity(performanceIntent);
                            break;
                        case 6:
                            Intent pharmacyIntent = new Intent(getActivity(), MypharmacyActivity.class);
                            pharmacyIntent.putExtra("action", 1);
                            startActivity(pharmacyIntent);
                            break;
                        case 7:
                            Intent idCardIntent = new Intent(getContext(), DoctorHomeCardActivity.class);
                            idCardIntent.putExtra("title", "我的名片");
                            idCardIntent.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                            startActivity(idCardIntent);
                            break;
                        case 8:
                            Intent videoInterrogationIntent = new Intent(mContext, VideoInterrogationActivity.class);
                            Bundle videoBundle = new Bundle();
                            videoBundle.putSerializable("unReadList", (Serializable) mVideoUnreadMessageList);
                            videoBundle.putSerializable("allUnReadList", (Serializable) mAllUnreadMessageList);
                            videoInterrogationIntent.putExtras(videoBundle);
                            startActivity(videoInterrogationIntent);
                            break;

                        case 9:
                            ExistDoctorDisGroup();

                            break;
                    }
                }
            });
        }
        loadBanner();
    }

    private void ExistDoctorDisGroup() {
        loading(true);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        String url = HttpUrl.ExistDoctorDisGroup + "?DrId=" + SpUtils.getInt(Contants.Id, 0);
        mMyOkhttp.get().url(url).headers(headMap).tag(this).enqueue(new GsonResponseHandler<Entity>() {
            @Override
            public void onSuccess(int statusCode, Entity entituy) {
                loading(false);
                if (entituy.getCode() == 0) {

                    boolean isExistDoctorDisGroup = (boolean) entituy.getData();
                    if (isExistDoctorDisGroup) {
                        Intent fenjizhuanzhen = new Intent(mContext, FenjiZhuanzhenActivity.class);
                        startActivity(fenjizhuanzhen);
                    } else {

                        Intent fenjizhuanzhen = new Intent(mContext, FenjiCooperationActivity.class);
                        startActivity(fenjizhuanzhen);
                    }
                } else {
                    toast(entituy.getMessage());
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                toast(errorMsg);
            }
        });
    }

    private void loadBanner() {
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        String bannerUrl = HttpUrl.QueryAdBannerInfo + "?classId=" + 4;
        mMyOkhttp.get().url(bannerUrl).headers(headMap).tag(this).enqueue(new GsonResponseHandler<AdBannerInfo>() {
            @Override
            public void onSuccess(int statusCode, AdBannerInfo entituy) {
                final List<AdBannerInfo.DataBean> data = entituy.getData();
                if (data == null || data.size() == 0) return;
                List<String> images = new ArrayList<>();
                bannerdata.clear();
                bannerdata.addAll(data);
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

    private void dealBannerData(List<String> images) {
        if (images == null || images.size() == 0) return;
        if (getActivity() == null) return;
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
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                // toast("点击了图片");
                Intent intent = new Intent(getContext(), BannerDetailActivity.class);
                intent.putExtra("banner", bannerdata.get(position).getLinkContent());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()) {
            case R.id.online_consult:
                Intent intent = new Intent(getActivity(), OnlineConsultingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("unReadList", (Serializable) mPictureUnreadMessageList);
                bundle.putSerializable("allUnReadList", (Serializable) mAllUnreadMessageList);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.my_pharmacy:
                Intent pharmacyIntent = new Intent(getActivity(), MypharmacyActivity.class);
                pharmacyIntent.putExtra("action", 1);
                startActivity(pharmacyIntent);
                break;
            case R.id.call_test:
                Intent callIntent = new Intent(getActivity(), ChatActivity.class);
                callIntent.putExtra(Contants.FRIEND_IDENTIFIER, "mike");
                startActivity(callIntent);
                break;
            case R.id.top_right:
                if (mDoctorInfoByIdEntity == null) {
                    return;
                } else {
                    Intent idCardIntent = new Intent(getContext(), DoctorHomeCardActivity.class);
                    idCardIntent.putExtra("title", "我的名片");
                    idCardIntent.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                    startActivity(idCardIntent);
                }
                break;
            case R.id.scan:
                //
                ToastUtil.setToast("扫一扫");
                Intent intent2 = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent2, REQUEST_CODE);
                break;
            case R.id.system_message_layout:
                Intent systemIntent = new Intent(getContext(), PushListActivity.class);
                systemIntent.putExtra("action", "system");
                startActivity(systemIntent);
                break;
            case R.id.reports_message_layout:
                Intent reportedIntent = new Intent(getContext(), PushListActivity.class);
                reportedIntent.putExtra("action", "newReport");
                startActivity(reportedIntent);
              /*  Intent reportedIntent = new Intent(getContext(), PatientActivity.class);
                reportedIntent.putExtra("action", 1);
                startActivity(reportedIntent);*/
                break;
            case R.id.my_physician_visits:
                Intent videoInterrogationIntent = new Intent(mContext, VideoInterrogationActivity.class);
                Bundle videoBundle = new Bundle();
                videoBundle.putSerializable("unReadList", (Serializable) mVideoUnreadMessageList);
                videoBundle.putSerializable("allUnReadList", (Serializable) mAllUnreadMessageList);
                videoInterrogationIntent.putExtras(videoBundle);
                startActivity(videoInterrogationIntent);
                break;
            case R.id.online_renewal_party:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    Intent onlineRenewalPartyIntent = new Intent(mContext, OnlineRenewalPartyActivity.class);
                    Bundle renewalBundle = new Bundle();
                    renewalBundle.putSerializable("allUnReadList", (Serializable) mAllUnreadMessageList);
                    renewalBundle.putSerializable("unReadList", (Serializable) mRenewalUnreadMessageList);
                    onlineRenewalPartyIntent.putExtras(renewalBundle);
                    startActivity(onlineRenewalPartyIntent);
                }
                break;

            case R.id.lv_heartcheck:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                //toast("胎心判读");
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    //toast("胎心判读");
                    Intent intentCheckCenter = MyCheckCenterActivity.newIntent(getContext());
                    startActivity(intentCheckCenter);

                }
                break;

            case R.id.lv_chufangcheck:
                //toast("药师");
                Intent intentPrescription = PrescriptionListActivity.newIntent(getContext());
                startActivity(intentPrescription);
                break;
            case R.id.invitate_patient:
                Intent idCardIntent = new Intent(getContext(), DoctorHomeCardActivity.class);
                idCardIntent.putExtra("title", "我的名片");
                idCardIntent.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                startActivity(idCardIntent);
                break;
            case R.id.performance_record:
                Intent performanceIntent = new Intent(getActivity(), MypharmacyActivity.class);
                performanceIntent.putExtra("action", 2);
                startActivity(performanceIntent);
                break;
            case R.id.diagnosis_and_treatment:
                mStatus = SpUtils.getInt(Contants.Status, 0);
                if (mStatus != 1) {
                    AppUtils.checkAuthenStatus(mStatus, getContext());
                } else {
                    Intent treatIntent = new Intent(getActivity(), TreatActivity.class);
                    startActivity(treatIntent);
                }
                break;
        }
    }

    private void getAllUseFunction() {
        listuse.clear();
        mFunctionDataEntity = SQLiteUtils.getInstance().selectFunctionDataById(SpUtils.getInt(Contants.Id, 0));
        String use = null;
        if (mFunctionDataEntity != null) {
            use = mFunctionDataEntity.getData();
        }
        //   String all = SpUtils.getString(Contants.ALLJSON);
        Log.d(TAG, "常用" + use);
        //  Log.d(TAG, "所有" + all);
        if (TextUtils.isEmpty(use)) {
            listuse.addAll(AppLicationUtils.getListuse());
        } else {
            try {
                listuse = mGson.fromJson(use, new TypeToken<List<AppLicationEntity>>() {
                }.getType());
            } catch (Exception e) {
                Log.e("HomeFragment", "e = " + e.toString());
            }
        }
        AppLicationEntity appLicationEntity = new AppLicationEntity();
        appLicationEntity.setAppDesc("定制桌面");
        appLicationEntity.setImageID(0);
        appLicationEntity.setAppID(0);
        listuse.add(appLicationEntity);
        if (mFunctionAdapter != null) {
            mFunctionAdapter.setNewData(listuse);
            showUnreadTip();
            queryDoctorMedicalRecords();
            getConsultRecordList();
            // mFunctionAdapter.notifyDataSetChanged();
        } else {
            GridLayoutManager mManagerLayout = new GridLayoutManager(mContext, 4);
            functionList.setLayoutManager(mManagerLayout);
            mFunctionAdapter = new FunctionAdapter(listuse);
            functionList.setAdapter(mFunctionAdapter);
        }

    }

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            checkUpdate();
        }
    }

    /**
     * 按钮点击事件处理逻辑
     *
     * @param
     */
    private void onClick() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    final boolean isExists = BJCASDK.getInstance().existsCert(getContext());
                    boolean ExistStamp = BJCASDK.getInstance().existsStamp(getContext());
                    if (!isExists) {
                        BJCASDK.getInstance().certDown(mActivity, Contants.clientId, SpUtils.getString(Contants.userName), new YWXListener() {
                            @Override
                            public void callback(String s) {
                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(s, YWXListenerEntity.class);
                                String status = yWXListenerEntity.getStatus();
                                String message = yWXListenerEntity.getMessage();
                                if (status != null && status.equals("0")) {
                                    boolean ExistStamp1 = BJCASDK.getInstance().existsStamp(getContext());
                                    if (!ExistStamp1) {
                                        BJCASDK.getInstance().drawStamp(mActivity, Contants.clientId, new YWXListener() {
                                            @Override
                                            public void callback(String msg) {
                                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                                String status = yWXListenerEntity.getStatus();
                                                String message = yWXListenerEntity.getMessage();
                                                if (status != null && status.equals("0")) {
                                                    BJCASDK.getInstance().qrDispose(mActivity, Contants.clientId, result, new YWXListener() {
                                                        @Override
                                                        public void callback(String msg) {
                                                            YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                                            String status = yWXListenerEntity.getStatus();
                                                            String message = yWXListenerEntity.getMessage();
                                                            if (status != null && status.equals("0")) {
                                                                Toast.makeText(mActivity, "调用成功", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        BJCASDK.getInstance().qrDispose(mActivity, Contants.clientId, result, new YWXListener() {
                                            @Override
                                            public void callback(String msg) {
                                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                                String status = yWXListenerEntity.getStatus();
                                                String message = yWXListenerEntity.getMessage();
                                                if (status != null && status.equals("0")) {
                                                    Toast.makeText(mActivity, "调用成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        //   BJCASDK.getInstance().clearCert(ChatActivity.this);
                        Log.i("zdp", "证书已下载");
                    }
                    if (!ExistStamp) {
                        BJCASDK.getInstance().drawStamp(mActivity, Contants.clientId, new YWXListener() {
                            @Override
                            public void callback(String msg) {
                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                String status = yWXListenerEntity.getStatus();
                                String message = yWXListenerEntity.getMessage();
                                if (status != null && status.equals("0")) {
                                    BJCASDK.getInstance().qrDispose(mActivity, Contants.clientId, result, new YWXListener() {
                                        @Override
                                        public void callback(String msg) {
                                            YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                            String status = yWXListenerEntity.getStatus();
                                            String message = yWXListenerEntity.getMessage();
                                            if (status != null && status.equals("0")) {
                                                Toast.makeText(mActivity, "调用成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {

                        BJCASDK.getInstance().qrDispose(mActivity, Contants.clientId, result, new YWXListener() {
                            @Override
                            public void callback(String msg) {
                                YWXListenerEntity yWXListenerEntity = mGson.fromJson(msg, YWXListenerEntity.class);
                                String status = yWXListenerEntity.getStatus();
                                String message = yWXListenerEntity.getMessage();
                                if (status != null && status.equals("0")) {
                                    Toast.makeText(mActivity, "调用成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                  /*  if (!isExists) {
                        BJCASDK.getInstance().startUrl(getContext(), Contants.clientId, 1, SpUtils.getString(Contants.userName));
                    } else {
                        Log.i("zdp", "证书已下载");
                    }
                    if (!ExistStamp) {
                        BJCASDK.getInstance().startUrl(getContext(), Contants.clientId, 3);
                    } else {
                        int code = BJCASDK.getInstance().qrDispose(getActivity(), Contants.clientId, result);
                        Log.i("HomeFragment", "code =" + code);
                    }*/
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        unbinder.unbind();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(UpdateImMessageEntity userEvent) {

        // GetAllRecordForDoctor();
        //  refreshView(userEvent.ImId);
        showUnreadTip();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(UpdatePushView userEvent) {
        int action = userEvent.action;
        switch (action) {
            case 1:
                getUnreadUnm();
                break;
            case 2:
                // requestAllNewReportPaientMessage();
                getNewReportUnreadUnm();
                break;
            case 3:
                //胎心判读
                getConsultRecordList();
                break;
            case 4:
                //药师
                Log.d(TAG, "收到更新药师通知消息");
                intTotalYaoshi = intTotalYaoshi - 1;
                if (intTotalYaoshi > 0) {
                    rvChufangUnreadNum.setVisibility(View.VISIBLE);
                    tvUnreadNumChufang.setText(intTotalYaoshi + "");
                } else {
                    rvChufangUnreadNum.setVisibility(View.GONE);

                }

                break;

            case 5:
                getOnlinePartyList(1, "0");
                break;
            case 6:
                queryDoctorMedicalRecords();
                break;
            case 11:
                getAllUseFunction();
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(DoctorInfoByIdEntity doctorInfoByIdEntity) {
        mDoctorInfoByIdEntity = doctorInfoByIdEntity;
        MyApplication.mDoctorInfoByIdEntity = doctorInfoByIdEntity;
        mIsOnLine = mDoctorInfoByIdEntity.getIsOnline();
        Log.e("tag", "userEventBus: " + mIsOnLine);
        setOnLineStatus(mIsOnLine);
    }

    private void showUnreadTip() {
        mPictureUnRead = 0;
        mVideoUnRead = 0;
        mRenewalUnRead = 0;
        List<AllRecordForDoctorEntity> pictureList = SQLiteUtils.getInstance().selectAllUnreadImDataByServiceCode("1101");
        List<AllRecordForDoctorEntity> onRenewalList = SQLiteUtils.getInstance().selectAllUnreadImDataByServiceCode("1103");
        List<AllRecordForDoctorEntity> videoList = SQLiteUtils.getInstance().selectAllUnreadImDataByServiceCode("1102");
        for (int i = 0; i < pictureList.size(); i++) {
            mPictureUnRead = mPictureUnRead + 1;
        }
        for (int i = 0; i < videoList.size(); i++) {
            mVideoUnRead = mVideoUnRead + 1;
        }
        for (int i = 0; i < onRenewalList.size(); i++) {
            mRenewalUnRead = mRenewalUnRead + 1;
        }
        Log.i("HomeFragment","mVideoUnRead="+mVideoUnRead);
        if (getActivity() == null) return;
        for (int k = 0; k < listuse.size(); k++) {
            int type = listuse.get(k).getAppID();
            switch (type) {
                case 1:
                    listuse.get(k).setUnReadNum(mPictureUnRead);
                    mFunctionAdapter.notifyItemChanged(k);
                    break;
                case 2:
                    listuse.get(k).setUnReadNum(mRenewalUnRead);
                    mFunctionAdapter.notifyItemChanged(k);
                    break;
                case 8:
                    listuse.get(k).setUnReadNum(mVideoUnRead);
                    mFunctionAdapter.notifyItemChanged(k);
                    break;
            }
        }
        if (mPictureUnRead > 0) {
            Log.i("OnlineConsulting", "come here");
            pictureUnreadNumLayout.setVisibility(View.VISIBLE);
            pictureUnreadNum.setText(String.valueOf(mPictureUnRead));
        } else {
            pictureUnreadNumLayout.setVisibility(View.GONE);
        }
        if (mVideoUnRead > 0) {
            videoUnreadNumLayout.setVisibility(View.VISIBLE);
            videoUnreadNum.setText(String.valueOf(mVideoUnRead));
        } else {
            videoUnreadNumLayout.setVisibility(View.GONE);
        }
        if (mRenewalUnRead > 0) {
            renewalUnreadNumLayout.setVisibility(View.VISIBLE);
            renewalUnreadNum.setText(String.valueOf(mRenewalUnRead));
        } else {
            renewalUnreadNumLayout.setVisibility(View.GONE);
        }

    }


    private void getAllPushList() {
        List<PushDataDaoEntity> list = SQLiteUtils.getInstance().selectAllContactsByDoctorId(mId);

        if (list.size() > 0) {
            Date time = list.get(0).getTime();
            String dateTime = mFormatter.format(time);
            int msgId = list.get(0).getMsgId();
            QueryDoctorAppMessageByPage(String.valueOf(SpUtils.getInt(Contants.Id, 0)), dateTime, msgId);
        } else {
            QueryDoctorAppMessageByPage(String.valueOf(SpUtils.getInt(Contants.Id, 0)), null, 0);
        }

    }

    private void getUnreadUnm() {
        List<PushDataDaoEntity> systemList = SQLiteUtils.getInstance().selectAllSystemContactsByDoctorId(mId);
        if (getActivity() == null) return;
        int sysUnreadNum = 0;
        for (int i = 0; i < systemList.size(); i++) {
            boolean isread = systemList.get(i).isRead;
            if (!isread) {
                sysUnreadNum = sysUnreadNum + 1;
            }
        }
        if (sysUnreadNum > 0) {
            systemUnreadNumLayout.setVisibility(View.VISIBLE);
            if (sysUnreadNum > 99) {
                systemUnreadNum.setText("99+");
            } else {
                systemUnreadNum.setText(String.valueOf(sysUnreadNum));
            }

        } else {
            systemUnreadNumLayout.setVisibility(View.GONE);
            systemUnreadNum.setText(String.valueOf(0));
        }


    }

    private void getNewReportUnreadUnm() {
        List<PushDataDaoEntity> systemList = SQLiteUtils.getInstance().selectAllReportsContactsByDoctorId(mId);
        if (getActivity() == null) return;
        int sysUnreadNum = 0;
        for (int i = 0; i < systemList.size(); i++) {
            boolean isread = systemList.get(i).isRead;
            if (!isread) {
                sysUnreadNum = sysUnreadNum + 1;
            }
        }

        if (sysUnreadNum > 0) {
            reportsUnreadNumLayout.setVisibility(View.VISIBLE);
            if (sysUnreadNum > 99) {
                reportsUnreadNum.setText("99+");
            } else {
                reportsUnreadNum.setText(String.valueOf(sysUnreadNum));
            }

        } else {
            reportsUnreadNumLayout.setVisibility(View.GONE);
            reportsUnreadNum.setText(String.valueOf(0));
        }


    }


    private void QueryDoctorAppMessageByPage(String id, final String time, final int messageId) {
        loading(true);
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("DoctorId", id);
        requestMap.put("IsRead", "0");
        requestMap.put("PageSize", "10000");
        requestMap.put("AppId", "2d77c7dfc65d033c8c3f5f89");
        if (time != null) {
            requestMap.put("BeginTime", time);
        }
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        mMyOkhttp.post().url(HttpUrl.QueryDoctorAppMessageByPage).headers(headMap).params(requestMap).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<JpushDataListEntity>>() {
            @Override
            public void onSuccess(int statusCode, ResponseEntity<JpushDataListEntity> entity) {
                //    mConsultMessageAdapter.setDatalist( entity.getData());
                if (entity.getCode() == 0) {

                    if (entity.getData().getReturnData() != null && entity.getData().getReturnData().size() > 0) {
                        for (int i = 0; i < entity.getData().getReturnData().size(); i++) {
                            Log.i("HomeFragment", "title=" + time + ",t=" + entity.getData().getReturnData().get(i).getTitle());
                            /*   Log.i("HomeFragment","isequal="+time.equals(entity.getData().getReturnData().get(i).getSendTime()));*/
                            if (entity.getData().getReturnData().get(i).getSendTime() != null && !entity.getData().getReturnData().get(i).getSendTime().equals("") && messageId == entity.getData().getReturnData().get(i).getId()) {

                            } else {
                                JpushDataEntity jpushDataEntity = entity.getData().getReturnData().get(i);
                                String extras = jpushDataEntity.getExtras();
                                Log.i("HomeFragment", "extras=" + extras);
                                //  ExtrasDataEntity JpushData = mGson.fromJson(extras, ExtrasDataEntity.class);
                                String title = jpushDataEntity.getTitle();
                                String content = jpushDataEntity.getMessage();
                                String time = jpushDataEntity.getSendTime();
                                Date dateTime = null;
                                try {
                                    dateTime = mFormatter.parse(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                int isRead = jpushDataEntity.getStatus();
                                int msgId = jpushDataEntity.getId();
                                Log.i("HomeFragment", "time=" + time);
                                //  String data = mGson.toJson(extras);
                                SQLiteUtils.getInstance().addPushData(new PushDataDaoEntity(null, title, content, extras, dateTime, isRead == 1 ? true : false, mId, msgId));
                            }

                        }
                    }
                    getUnreadUnm();
                    getNewReportUnreadUnm();

                } else {
                    toast(entity.getMessage() + "aaa");
                }
                LoadDialog.clear();
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                Log.i("HomeFragment", "2222");
                CommonMethod.requestError(statusCode, errorMsg);
            }
        });
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();


    }


    private void setOnLineStatus(int flag) {
        if (getActivity() == null) return;
        if (flag == 1) {
            ivSwitchButton.setImageResource(R.mipmap.ic_switch_open);

        } else {
            ivSwitchButton.setImageResource(R.mipmap.ic_switch_off);
        }

    }


    private void getConsultRecordList() {
        Map<String, String> map = new HashMap<>();
        map.put("DoctorId", String.valueOf(SpUtils.getInt(Contants.Id, 0)));
        map.put("Status", "0");
        map.put("PageIndex", "1");
        map.put("PageSize", "10000");
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", MyApplication.token);
        mMyOkhttp.post().url(HttpUrl.getAllReadData).headers(headMap).params(map).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<AllDoctorCheckEntity>>() {
            @Override
            public void onSuccess(int statusCode, ResponseEntity<AllDoctorCheckEntity> entity) {

                if (entity.getCode() == 0) {
                    if (getActivity() == null) return;
                    AllDoctorCheckEntity consultDataEntity = entity.getData();

                    if (consultDataEntity != null && consultDataEntity.getTotal() > 0) {
                        //需要显示红点
                        rvHeartUnreadNum.setVisibility(View.VISIBLE);
                        tvUnreadNumHeart.setText(consultDataEntity.getTotal() + "");
                        Log.d("xxxxxx", "111111111111");
                        for (int i = 0; i < listuse.size(); i++) {
                            int type = listuse.get(i).getAppID();
                            if (type == 4) {
                                listuse.get(i).setUnReadNum(consultDataEntity.getTotal());
                                mFunctionAdapter.notifyItemChanged(i);
                            }
                        }

                    } else {
                        //不需要显示胎心判读红点
                        rvHeartUnreadNum.setVisibility(View.GONE);
                        Log.d("xxxxxx", "22222222");
                    }
                } else {
                    rvHeartUnreadNum.setVisibility(View.GONE);
                    Log.d("xxxxxx", "33333333");
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                Log.d("aaaaaaaaaa", "TOKEN失效");
                CommonMethod.requestError(statusCode, errorMsg);
            }
        });
    }

    private void checkUpdate() {

        HomeUtils.INSTANCE.checkVersion(new HomeUtils.UpdateListener() {
            @Override
            public void success(final AppEntity entity) {
                AppUpdateUtils.mAppLicationUtils = null;
                AppUpdateUtils.getInstance().update(entity, getContext(), getActivity());

            }


            @Override
            public void failed(int statusCode, String errorMsg) {
                CommonMethod.requestError(statusCode, errorMsg);

            }
        });
    }


    private boolean checkPermission() {

        List<String> permissions = new ArrayList<>();
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) {
            permissions.add(Manifest.permission.CAMERA);
        }

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        int len = permissions.size();
        if (len != 0) {
            String[] per = new String[len];
            for (int i = 0; i < len; i++) {
                per[i] = permissions.get(i);
            }
            requestPermissions(
                    per,
                    REQUEST_CODE);
            return false;
        }
        return true;

    }

    protected void getOnlinePartyList(int index, String s) {
        Map<String, String> map = new HashMap<>();
        map.put("DoctorId", String.valueOf(SpUtils.getInt(Contants.Id, 0)));
        map.put("OrgId", String.valueOf(SpUtils.getInt(Contants.OrgId, 0)));
        //  map.put("DoctorName", SpUtils.getString(Contants.Name));
        map.put("Diagnoses", "");
        map.put("PrescriptionNo", "");
        map.put("Status", "0"); //状态（-1：全部，0：待审核，1：已审核，2：被驳回）（必填）
        map.put("AuditorId", String.valueOf(SpUtils.getInt(Contants.Id, 0))); //状态（-1：全部，0：待审核，1：已审核，2：被驳回）（必填）
        map.put("PageIndex", index + "");
        map.put("PageSize", "10");
        loading(true);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", MyApplication.token);
        mMyOkhttp.post().url(HttpUrl.PrescriptionCheckList).headers(headMap).params(map).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<PrescriptionEntity>>() {
            @Override
            public void onSuccess(int statusCode, ResponseEntity<PrescriptionEntity> entity) {
                LoadDialog.clear();
                Log.d("xxxxxx", entity.toString());
                if (entity.getCode() == 0) {
                    PrescriptionEntity onlineRenewalMessageEntity = entity.getData();
                    Log.d("xxxxxx", onlineRenewalMessageEntity.toString());
                    if (onlineRenewalMessageEntity != null) {
                        intTotalYaoshi = onlineRenewalMessageEntity.getTotal();
                        if (onlineRenewalMessageEntity.getTotal() > 0) {
                            rvChufangUnreadNum.setVisibility(View.VISIBLE);
                            tvUnreadNumChufang.setText(onlineRenewalMessageEntity.getTotal() + "");
                            Log.d("xxxxxx", "111111111111");
                        } else {
                            rvChufangUnreadNum.setVisibility(View.GONE);
                            Log.d("xxxxxx", "2222222222");
                        }
                    } else {
                        rvChufangUnreadNum.setVisibility(View.GONE);
                        Log.d("xxxxxx", "2222222222");
                    }
                } else {
                    rvChufangUnreadNum.setVisibility(View.GONE);
                    Log.d("xxxxxx", "33333333333");
                    toast(entity.getMessage());
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                CommonMethod.requestError(statusCode, errorMsg);
            }
        });
    }

    private void queryDoctorMedicalRecords() {
        HashMap<String, String> params = new HashMap<>();
        params.put("DrId", String.valueOf(SpUtils.getInt(Contants.Id, -1)));
        params.put("MedicalType", "1");
        params.put("MedicalStatus", "1");
        params.put("PageIndex", "1");
        params.put("PageSize", "1");
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        mMyOkhttp.post().url(HttpUrl.QueryDoctorMedicalRecords).headers(headMap).params(params).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<DoctorMedicalRecordsEntity>>() {
            @Override
            public void onSuccess(int statusCode, ResponseEntity<DoctorMedicalRecordsEntity> entity) {

                final DoctorMedicalRecordsEntity doctorMedicalRecordsEntity = entity.getData();
                Log.i("DiagnosisAndTreat", "entity=" + doctorMedicalRecordsEntity.toString());
                if (entity.getCode() == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int unReadNum = doctorMedicalRecordsEntity.getTotal();
                            if (unReadNum > 0) {
                                rvTreatmentUnreadNum.setVisibility(View.VISIBLE);
                                tvUnreadNumTreatment.setText(String.valueOf(unReadNum));
                                for (int i = 0; i < listuse.size(); i++) {
                                    int type = listuse.get(i).getAppID();
                                    if (type == 3) {
                                        listuse.get(i).setUnReadNum(unReadNum);
                                        mFunctionAdapter.notifyItemChanged(i);
                                    }
                                }
                            } else {
                                rvTreatmentUnreadNum.setVisibility(View.GONE);
                            }
                        }
                    });
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


}
