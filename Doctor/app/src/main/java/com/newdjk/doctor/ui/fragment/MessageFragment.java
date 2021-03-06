package com.newdjk.doctor.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.lxq.okhttp.response.GsonResponseHandler;
import com.newdjk.doctor.R;
import com.newdjk.doctor.basic.BasicFragment;
import com.newdjk.doctor.model.HttpUrl;
import com.newdjk.doctor.sort.CharacterParser;
import com.newdjk.doctor.sort.MyLetterSortView;
import com.newdjk.doctor.tools.CommonMethod;
import com.newdjk.doctor.tools.Contants;
import com.newdjk.doctor.ui.activity.DoctorHomeCardActivity;
import com.newdjk.doctor.ui.activity.NewlyBuildGroupActivity;
import com.newdjk.doctor.ui.activity.PatientActivity;
import com.newdjk.doctor.ui.adapter.PatientAdapter;
import com.newdjk.doctor.ui.entity.DoctorInfoByIdEntity;
import com.newdjk.doctor.ui.entity.PatientListDataEntity;
import com.newdjk.doctor.ui.entity.PatientListEntity;
import com.newdjk.doctor.ui.entity.ResponseEntity;
import com.newdjk.doctor.ui.entity.UpdatePatientViewEntity;
import com.newdjk.doctor.utils.SpUtils;
import com.newdjk.doctor.views.ClearEditText;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 消息中心
 */
public class MessageFragment extends BasicFragment {


    @BindView(R.id.top_left)
    ImageView topLeft;
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
    @BindView(R.id.message_recycler_view)
    RecyclerView messageRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.newly_reported_patients)
    TextView newlyReportedPatients;
    @BindView(R.id.focus_patient)
    TextView focusPatient;
    @BindView(R.id.grouping)
    TextView grouping;
    private PatientAdapter mPatientAdapter;
    @BindView(R.id.et_patient_msg)
    ClearEditText etPatientMsg;
    @BindView(R.id.right_letter)
    MyLetterSortView rightLetter;
    @BindView(R.id.easylayout)
    EasyRefreshLayout mEasylayout;
    @BindView(R.id.mNodataContainer)
    RelativeLayout mNodataContainer;
    @BindView(R.id.mSearchTv)
    TextView mSearchTv;

    private InputMethodManager inputMethodManager;
    private CharacterParser characterParser;
    private String mPatientNameContent = "";
    private int mIndex = 1;

    public static MessageFragment getFragment() {
        return new MessageFragment();
    }

    private List<PatientListDataEntity> mPaintList = new ArrayList<>();
    private final static String TAG = "Test";

    @Override
    protected int initViewResId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {
        topTitle.setTextColor(getResources().getColor(R.color.black));
        tvRight.setTextColor(getResources().getColor(R.color.black));
        tvLeft.setTextColor(getResources().getColor(R.color.theme));

        topTitle.setText("我的患者");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("群发助手");
        tvLeft.setText("新增患者");
        tvRight.setVisibility(View.VISIBLE);
        //tvLeft.setTextColor(getResources().getColor(R.color.white));
        tvLeft.setVisibility(View.VISIBLE);
        topLeft.setVisibility(View.GONE);
        /*addFriends.setOnClickListener(this);*/
        mPatientAdapter = new PatientAdapter(getActivity());
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        messageRecyclerView.setAdapter(mPatientAdapter);
        messageRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        tvRight.setOnClickListener(this);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void initListener() {
        newlyReportedPatients.setOnClickListener(this);
        focusPatient.setOnClickListener(this);
        grouping.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
        messageRecyclerViewEvent();

        messageRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        if (inputMethodManager != null)
                            inputMethodManager.hideSoftInputFromWindow(
                                    getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        rightLetter.setOnTouchingLetterChangedListener(new MyLetterSortView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mPatientAdapter.getPositionForSection(s.charAt(0));
                Log.e(TAG, "onTouchingLetterChanged: " + position);
                if (position != -1) {
                    messageRecyclerView.smoothScrollToPosition(position);
                }

            }
        });

        etPatientMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //搜索
                    String content = etPatientMsg.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        toast("请输入患者信息,然后进行搜索");
                        return true;
                    }
                    //搜索
                    mPatientNameContent = etPatientMsg.getText().toString().trim();
                    mIndex = 1;
                    requestAllPaientMessage();
                }
                return false;
            }
        });

        etPatientMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    mPatientNameContent = "";
                    requestAllPaientMessage();
                }
            }
        });

        mEasylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mIndex++;
                requestAllPaientMessage();
            }

            @Override
            public void onRefreshing() {
                mIndex = 1;
                mEasylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                requestAllPaientMessage();
            }
        });
    }

    private void messageRecyclerViewEvent() {
        //  messageRecyclerView
    }

    @Override
    protected void initData() {
        if (SpUtils.getInt(Contants.DocType, 0) == 1) {
            //实例化汉字转拼音类
            characterParser = CharacterParser.getInstance();
            requestAllPaientMessage();
        }
    }

    @Override
    protected void otherViewClick(View view) {

    }

    /**
     * 注册用户名(*托管模式，独立模式下请向自己私有服务器注册)
     */
 /*   private void regist(String account, String password) {
        if (bTLSAccount) {
            ILiveLoginManager.getInstance().tlsRegister(account, password, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    Toast.makeText(mContext, "regist success!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    Toast.makeText(mContext, "regist failed:" + module + "|" + errCode + "|" + errMsg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAccountMgr.regist(account, password, new AccountMgr.RequestCallBack() {
                @Override
                public void onResult(int error, String response) {
                    Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newly_reported_patients:
                Intent reportedIntent = new Intent(getContext(), PatientActivity.class);
                reportedIntent.putExtra("action", 1);
                startActivity(reportedIntent);
                break;
            case R.id.focus_patient:
                Intent focusIntent = new Intent(getContext(), PatientActivity.class);
                focusIntent.putExtra("action", 2);
                startActivity(focusIntent);
                break;
            case R.id.grouping:
                Intent groupIntent = new Intent(getContext(), PatientActivity.class);
                groupIntent.putExtra("action", 3);
                startActivity(groupIntent);
                break;
            case R.id.tv_right:
                Intent newLyBuildIntent = new Intent(getContext(), NewlyBuildGroupActivity.class);
                startActivity(newLyBuildIntent);
                break;
            case R.id.tv_left:
                Intent doctorHomeCard = new Intent(getContext(), DoctorHomeCardActivity.class);
                doctorHomeCard.putExtra("title", "新增患者");
                if (mDoctorInfoByIdEntity != null) {
                    doctorHomeCard.putExtra("doctorInfoByIdEntity", mDoctorInfoByIdEntity);
                    startActivity(doctorHomeCard);
                }
                break;
            case R.id.mSearchTv:
                //搜索
                String content = etPatientMsg.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    toast("请输入患者信息,然后进行搜索");
                    return;
                }
                mPatientNameContent = etPatientMsg.getText().toString().trim();
                mIndex = 1;
                requestAllPaientMessage();
                break;
        }
    }


    private void getFriendsList() {
        TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.e("zdp", "getFriendList failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result) {
                for (TIMUserProfile res : result) {
                    Log.e("zdp", "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
                            + " remark: " + res.getRemark());
                }
            }
        });
    }

    public static String getAlphabet(String str) {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String pinyin = null;
        try {
            pinyin = (String) PinyinHelper.toHanyuPinyinStringArray(str.charAt(0),
                    defaultFormat)[0];
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return pinyin.substring(0, 1).toUpperCase();
    }


    private void requestAllPaientMessage() {
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", SpUtils.getString(Contants.Token));
        Map<String, String> map = new HashMap<>();
        map.put("DrId", String.valueOf(SpUtils.getInt(Contants.Id, -1)));
        map.put("PatientId", "-1");
        map.put("RelationStatus", "1");
        map.put("IsPatientMain", "-1");
        map.put("IsDrKey", "-1");
        map.put("PatientName", mPatientNameContent);
        map.put("PageIndex", mIndex + "");
        map.put("PageSize", "20");
        Log.i("MessageFragment", "mIndex=" + mIndex);
        mMyOkhttp.post().url(HttpUrl.QueryDoctorPatientPage).headers(headMap).params(map).tag(this).enqueue(new GsonResponseHandler<ResponseEntity<PatientListEntity>>() {
            @Override
            public void onSuccess(int statusCode, final ResponseEntity<PatientListEntity> entity) {
                if (mEasylayout == null) return;
                if (mEasylayout.isRefreshing()) mEasylayout.refreshComplete();
                mEasylayout.setVisibility(View.VISIBLE);
                if (entity.getCode() == 0) {
                    if (mIndex == 1) {
                        mPaintList.clear();
                    } else {
                        mEasylayout.loadMoreComplete();
                    }
                    List<PatientListDataEntity> list = entity.getData().getReturnData();
                    int total = list.size();

                    if (total < 20) {
                        mEasylayout.setLoadMoreModel(LoadModel.NONE);
                    } else {
                        mEasylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                    }

                    final int len = list.size();


                    for (int i = 0; i < len; i++) {
                        PatientListDataEntity patientListDataEntity = list.get(i);
                        String patientName = patientListDataEntity.getPatientName();
                        if (patientName.length() == 0) continue;
                        String alphabet = patientName.substring(0, 1);
                        /*判断首字符是否为中文，如果是中文便将首字符拼音的首字母和&符号加在字符串前面*/
                        if (alphabet.matches("[\\u4e00-\\u9fa5]+")) {
                            if (!patientName.contains("&")) {
                                patientName = new StringBuffer(getAlphabet(patientName)).append("&").append(patientName).toString();
                                patientListDataEntity.setPatientName(patientName);
                                list.set(i, patientListDataEntity);
                            }

                        } else {
                            if (!patientName.contains("&")) {
                                if (isStartWithNumber(patientName)){
                                    patientName = new StringBuffer("#").append("&").append(patientName).toString();
                                    patientListDataEntity.setPatientName(patientName);
                                    list.set(i, patientListDataEntity);
                                }else {
                                    patientName = new StringBuffer(patientName.substring(0, 1).toUpperCase()).append("&").append(patientName).toString();
                                    patientListDataEntity.setPatientName(patientName);
                                    list.set(i, patientListDataEntity);
                                }

                            }

                        }
                        mPaintList.add(patientListDataEntity);
                    }
                    if (mPaintList.size() > 0 && len == 0) {
                        toast("没有更多数据");
                    }
                    // mPaintList.addAll(list);
                    if (mPaintList.size() <= 0) {
                        Log.i("MessageFragment", "lenError");
                        mEasylayout.setVisibility(View.GONE);
                        mNodataContainer.setVisibility(View.VISIBLE);
                    } else {
                        mEasylayout.setVisibility(View.VISIBLE);
                       mNodataContainer.setVisibility(View.GONE);
                    }
                    Log.i("MessageFragment", "size=" + mPaintList.size());
                    mPatientAdapter.setDatalist(mPaintList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            topTitle.setText("我的患者(" + entity.getData().getTotal() + ")");
                        }
                    });
                } else {
                    toast(entity.getMessage());
                    Log.i("MessageFragment", "toastError");
                    mEasylayout.setVisibility(View.GONE);
                    mNodataContainer.setVisibility(View.VISIBLE);
                }
                if (mPaintList.size() == 0) {
                    rightLetter.setVisibility(View.GONE);
                } else {
                    rightLetter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailures(int statusCode, String errorMsg) {
                //mEasylayout.setVisibility(View.GONE);
                Log.i("MessageFragment", "onFailures");
                CommonMethod.requestError(statusCode, errorMsg);
                if (mEasylayout != null) {
                    mEasylayout.setVisibility(View.GONE);
                }
                if (mNodataContainer != null) {
                    mNodataContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(UpdatePatientViewEntity userEvent) {
        mIndex = 1;
        requestAllPaientMessage();
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.i(TAG, "AAAAAA");
        super.onDestroy();
    }


    private DoctorInfoByIdEntity mDoctorInfoByIdEntity;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userEventBus(DoctorInfoByIdEntity doctorInfoByIdEntity) {
        mDoctorInfoByIdEntity = doctorInfoByIdEntity;
    }

    @Override
    public void onResume() {
        super.onResume();
        // requestAllPaientMessage();
    }

    //判断字符串是不是以数字开头
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0)+"");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
