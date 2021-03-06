package com.newdjk.doctor.ui.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newdjk.doctor.R;
import com.newdjk.doctor.tools.Contants;
import com.newdjk.doctor.ui.activity.IM.ChatActivity;
import com.newdjk.doctor.ui.entity.AllRecordForDoctorEntity;
import com.newdjk.doctor.ui.entity.DoctorPatientRelationEntity;
import com.newdjk.doctor.ui.entity.InquiryRecordListDataEntity;
import com.newdjk.doctor.ui.entity.OnlineRenewalDataEntity;
import com.newdjk.doctor.ui.entity.PrescriptionMessageEntity;
import com.newdjk.doctor.ui.entity.RecordDataEntity;
import com.newdjk.doctor.utils.GlideMediaLoader;
import com.newdjk.doctor.utils.SpUtils;

import java.lang.reflect.Type;
import java.util.List;

public class VideoInterrogationAdapter1 extends BaseQuickAdapter<AllRecordForDoctorEntity, BaseViewHolder> {

    private Gson mGson;
    private int mType;
    public VideoInterrogationAdapter1(@Nullable List<AllRecordForDoctorEntity> data,int type) {
        super(R.layout.interrogation_item,data);
        mType = type;
        mGson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, final AllRecordForDoctorEntity item) {
        try {
           //  DoctorPatientRelationEntity DoctorPatientRelationEntity = item.getDoctorPatientRelation();
            long unReadNum = item.getUnReadNum();
            if (unReadNum > 0) {
                helper.setVisible(R.id.video_unread_num_layout, true);
                helper.setText(R.id.video_unread_num, unReadNum + "");
            } else {
                helper.setVisible(R.id.video_unread_num_layout, false);
            }
            int IsDrKey = 0;
                IsDrKey = item.getIsDrKey();
            switch (IsDrKey) {
                case 0 :

                    break;
                case 1 :
                    helper.setText(R.id.signature_type,"重点关注");
                    break;
            }
            helper.setText(R.id.patient_name,item.getPatientName());
            helper.setText(R.id.dynamic,item.getContent());
            ImageView avatar = helper.getView(R.id.avatar);
            GlideMediaLoader.load(mContext,avatar,item.getApplicantHeadImgUrl());
            int sexType = item.getPatientSex();
            String createTime = item.getCreateTime();
            if (createTime != null ) {
                helper.setText(R.id.create_time,createTime);
            }
            else {
                helper.setText(R.id.create_time,"");
            }
            String areaName = item.getAreaName();
            if (TextUtils.isEmpty(areaName)) {
                helper.setText(R.id.address,areaName);
            }
            else {
                helper.setText(R.id.address,"");
            }
            String sex = "";
            switch (sexType) {
                case 1:
                    sex = "男"  ;
                    break;
                case 2:
                    sex = "女"  ;
                    break;
                case 3:
                    sex = "未知"  ;
                    break;
            }
            helper.setText(R.id.sex,sex);
            helper.setText(R.id.age,item.getAge());
            helper.setText(R.id.appointment_time,item.getReExaminationDate()+"  "+item.getReExaminationStartTime()+"-"+item.getReExaminationEndTime());
        } catch (Exception e) {
            Log.e("VideoAdapter","errorMsg="+e.toString());
            e.printStackTrace();
        }
        helper.getView(R.id.consult_message_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json ;
                if (mType == 3) {
                    Type jsonType = new TypeToken<PrescriptionMessageEntity<InquiryRecordListDataEntity>>() {
                    }.getType();
                    InquiryRecordListDataEntity recordDataEntity = mGson.fromJson(item.getRecordData(),InquiryRecordListDataEntity.class);
                    PrescriptionMessageEntity<InquiryRecordListDataEntity> prescriptionMessageEntity = mGson.fromJson(SpUtils.getString(Contants.LoginJson), jsonType);
                    prescriptionMessageEntity.setPatient(recordDataEntity);
                    json= mGson.toJson(prescriptionMessageEntity);
                }
                else {
                    Type jsonType = new TypeToken<PrescriptionMessageEntity<RecordDataEntity>>() {
                    }.getType();
                    RecordDataEntity recordDataEntity = mGson.fromJson(item.getRecordData(),RecordDataEntity.class);
                    PrescriptionMessageEntity<RecordDataEntity> prescriptionMessageEntity = mGson.fromJson(SpUtils.getString(Contants.LoginJson), jsonType);
                    prescriptionMessageEntity.setPatient(recordDataEntity);
                    json= mGson.toJson(prescriptionMessageEntity);
                }

                Log.i("zdp", "json=" + json);
                Intent intentTalk = new Intent(mContext, ChatActivity.class);
                intentTalk.putExtra(Contants.FRIEND_NAME, item.getPatientName());
                intentTalk.putExtra(Contants.FRIEND_IDENTIFIER,  item.getApplicantIMId());
                intentTalk.putExtra("inquiryRecordListDataEntity",item);
                intentTalk.putExtra("action", "videoInterrogation");
                intentTalk.putExtra("accountId",item.getApplicantId());
                intentTalk.putExtra("prescriptionMessage", json);
                mContext.startActivity(intentTalk);
            }
        });
    }
}
