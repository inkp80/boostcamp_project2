package com.inkp.boostcamp.Boostme.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.DetailScheduleAdapter;
import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleParcel;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;
import com.inkp.boostcamp.Boostme.receiver.AlarmReceiver;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by macbook on 2017. 2. 11..
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_title)
    TextView detail_titleView;
    @BindView(R.id.detail_date)
    TextView detail_dateView;
    @BindView(R.id.detail_location)
    TextView detail_locationView;
    @BindView(R.id.detail_recyclerview)
    RecyclerView detail_recyclerView;

    @BindView(R.id.toolbar_detail_delete)
    Button mDeleteButton;
    @BindView(R.id.toolbar_detail_edit)
    ImageButton mEditButton;


    Realm realm;
    ScheduleRealm mScheduleObject;
    RealmResults<SmallScheduleRealm> mSmallScheduleObjectList;
    int targetId;
    public static int REQUEST_CODE = 134;
    public static int RESULT_CODE = 431;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        targetId = intent.getIntExtra(Utills.access_Schedule_id, -1);
        //Bundle bundle = getIntent().getExtras();
        //ScheduleParcel scheduleParcel = bundle.getParcelable("scheduleParcel");

        mScheduleObject = realm.where(ScheduleRealm.class).equalTo("id", targetId).findFirst();
        mSmallScheduleObjectList = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();

        detail_titleView.setText(mScheduleObject.getTitle());
        detail_dateView.setText(Utills.format_yymmdd_hhmm_a.format(mScheduleObject.getDate()));
        //RealmList<SmallScheduleRealm> result = new RealmList<>();
        //result = Schedules.getSmall_schedule();

        RealmResults<SmallScheduleRealm> result = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
        result = result.sort("order_value", Sort.ASCENDING);


        DetailScheduleAdapter detailScheduleAdapter = new DetailScheduleAdapter(getBaseContext(), result);
        detail_recyclerView.hasFixedSize();
        detail_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        detail_recyclerView.setAdapter(detailScheduleAdapter);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        /*
                        for(int i=0; i<mSmallScheduleObjectList.size(); i++) {
                            int ALARM_ID = Utills.alarmIdBuilder(targetId, i);
                            Intent intentForCancle = new Intent(getBaseContext(), AlarmReceiver.class);
                            PendingIntent pendingIntentForCancle
                                    = PendingIntent.getBroadcast(getBaseContext(), ALARM_ID, intentForCancle, PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmReceiver.cancelAlarm(pendingIntentForCancle, getBaseContext());
                        }*/
                        Utills.cancleAlarm(getBaseContext(), targetId, mSmallScheduleObjectList);
                        mScheduleObject.deleteFromRealm(); // 비 직접적인 객체 삭제
                        mSmallScheduleObjectList.deleteAllFromRealm();
                        finish();
                    }
                });

            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddTaskActivity.class);
                Utills.cancleAlarm(getBaseContext(), targetId, mSmallScheduleObjectList);
                intent.putExtra("intentAction", Utills.INTENT_ACTION_EDIT_SCHEDULE);
                intent.putExtra(Utills.ALARM_intent_scheduleId, mScheduleObject.getId());
                intent.putExtra(Utills.ALARM_intent_title, mScheduleObject.getTitle());
                intent.putExtra(Utills.ALARM_intent_date, mScheduleObject.getDate_in_long());
                intent.putExtra(Utills.ALARM_intent_weekofday, mScheduleObject.getWeek_of_day_repit());

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("intente", "received");
        if (resultCode == RESULT_CODE) {
            //mScheduleObject = realm.where(ScheduleRealm.class).equalTo("id", targetId).findFirst();
            mSmallScheduleObjectList = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
            Log.d("intente", "result in");
            detail_titleView.setText(data.getStringExtra(Utills.ALARM_intent_title));
            detail_dateView.setText(Utills.format_yymmdd_hhmm_a.format(new Date(data.getLongExtra(Utills.ALARM_intent_date,0))));
            //RealmList<SmallScheduleRealm> result = new RealmList<>();
            //result = Schedules.getSmall_schedule();

            RealmResults<SmallScheduleRealm> result = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
            result = result.sort("order_value", Sort.ASCENDING);


            DetailScheduleAdapter detailScheduleAdapter = new DetailScheduleAdapter(getBaseContext(), result);
            detail_recyclerView.hasFixedSize();
            detail_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            detail_recyclerView.setAdapter(detailScheduleAdapter);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void setWeekdayOnView(int val, TextView tv) {
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(val, i);
            if (flag != 0) {
                switch (i) {
                    case 1:
                        tv.append("일 ");
                        break;
                    case 2:
                        tv.append("월 ");
                        break;
                    case 3:
                        tv.append("화 ");
                        break;
                    case 4:
                        tv.append("수 ");
                        break;
                    case 5:
                        tv.append("목 ");
                        break;
                    case 6:
                        tv.append("금 ");
                        break;
                    case 7:
                        tv.append("토 ");
                        break;
                }
            }
        }
    }

}