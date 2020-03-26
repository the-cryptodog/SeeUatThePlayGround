package com.app.myteammanager.ui.myteam.teamdatafragments.Finance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myteammanager.R;
import com.app.myteammanager.TeamActivity;
import com.app.myteammanager.utils.Global;
import com.app.myteammanager.utils.NetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AddOrEditFinanceRecordActivity extends AppCompatActivity {

    private FinanceRecordModel financeRecordModel;
    private int groupId;
    private TextView recordDate;
    private EditText cashFlow;
    private EditText tradeDetail;
    private ImageButton btn_Plus;
    private ImageButton btn_Minus;
    private Button btn_Yes;
    private Button btn_No;
    private int tmpYear,tmpMonth,tmpDate;
    private boolean addRecord;
    private int currentAmount;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_finance_record);
        addRecord = true;
        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getInt("groupId");

        if(bundle.getParcelable("walletForEdit")!=null) {
            addRecord = false;
            financeRecordModel = bundle.getParcelable("walletForEdit");
            System.out.println("錢包日期"+financeRecordModel.getDate());
        }else{
            currentAmount = 0;
        }




        init();
        initDefaultData(financeRecordModel);
        setButton();

    }

    private void init() {

        recordDate = findViewById(R.id.tv_addrecord_date);
        cashFlow = findViewById(R.id.addrecord_et_cashFlow);
        tradeDetail = findViewById(R.id.addrecord_et_tradeDetail);
        btn_Minus = findViewById(R.id.addrecord_btn_Minus);
        btn_Plus = findViewById(R.id.addrecord_btn_Plus);
        btn_Yes = findViewById(R.id.addrecord_btn_confirmed);
        if(addRecord){
            btn_Yes.setText("新增");
        }
        btn_No = findViewById(R.id.addrecord_btn_cancel);

    }

    private void initDefaultData(FinanceRecordModel frm) {  //建立編輯預設資料
        if (frm != null) {
            tmpYear = frm.getRyear();
            tmpMonth =frm.getRmonth();
            tmpDate = frm.getRdate();
            recordDate.setText(frm.getDate());
            cashFlow.setText(String.valueOf(frm.getCashFlow()));
            tradeDetail.setText(frm.getWalletDescribe());
        }else{
            Calendar calendar = Calendar.getInstance();
            tmpYear = calendar.get(Calendar.YEAR);
            tmpMonth = calendar.get(Calendar.MONTH)+1;
            tmpDate= calendar.get(Calendar.DAY_OF_MONTH);
            currentAmount = 0;
            cashFlow.setText(String.valueOf(currentAmount));
            tradeDetail.setText(" ");
        }
    }

    private void setButton() {

        recordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditFinanceRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                AddOrEditFinanceRecordActivity.this.tmpYear = year;
                                AddOrEditFinanceRecordActivity.this.tmpMonth = month +1;
                                AddOrEditFinanceRecordActivity.this.tmpDate = dayOfMonth;
                                //暫存本次的設定於本Activity
                                recordDate.setText(timeFormat(year, month+1, dayOfMonth));
                            }
                        }, tmpYear, tmpMonth - 1, tmpDate);
                datePickerDialog.show();

            }
        });


        btn_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cashFlow.getText().toString().equals("")){
                    System.out.println("數字錯誤成立");
                    cashFlow.setText(""+0);
                }
                currentAmount = Integer.valueOf(cashFlow.getText().toString());

//              currentAmount -=5;
                cashFlow.setText(String.valueOf(currentAmount - 5));
            }
        });

        btn_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cashFlow.getText().toString().equals("")){
                    cashFlow.setText(""+0);
                }
                currentAmount = Integer.valueOf(cashFlow.getText().toString());
//                currentAmount +=5;
                cashFlow.setText(String.valueOf(currentAmount + 5));
            }
        });

        btn_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cashFlow.getText().toString().equals("")){
                    cashFlow.setText(""+0);
                    showToast(R.string.no_Cashflow);
                }
//                if(Integer.valueOf(cashFlow.getText().toString())==0 ||
//                        cashFlow.getText().toString() == ""){
//                    showToast(R.string.no_Cashflow);
//                    return;
//                }
                editAlertDialog( );
            }
        });

        btn_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10);
                finish();

            }
        });
    }


    private String timeFormat(int year, int month, int date) {
        String tmp = year + "-" + ((month < 10) ? "0" + month : month) + "-" + (
                (date < 10) ? "0" + date : date);
        return tmp;
    }

    private void uploadDataToBackend() {

        JSONObject jsonObject = new JSONObject();

        String recordDate = timeFormat(tmpYear,tmpMonth,tmpDate);

        int editedCashFlow = Integer.valueOf(cashFlow.getText().toString());


        try {
            jsonObject.put("MoneyTrack", editedCashFlow)
                    .put("WalletDescribe",tradeDetail.getText().toString())
                    .put("RecordDate",recordDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String apiType = addRecord ? "" : String.valueOf(financeRecordModel.getWalletId());
        String editWalletAPI = Global.API_EDIT_FINANCE_RECORD
                + this.groupId +"/"
                + apiType;

        System.out.println("呼叫修改API="+editWalletAPI);
        System.out.println("上傳日期="+recordDate);
        System.out.println("紀錄描述="+tradeDetail.getText().toString());

        NetworkController.getInstance().post(editWalletAPI, jsonObject.toString()
                , new NetworkController.CCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        System.out.println("修改失敗 = " + errorMsg);
                    }

                    @Override
                    public void onResponse(JSONObject data) throws JSONException {
                        System.out.println("修改成功 = " + data.toString());
                        setResult(9);
                        finish();
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

    }

    private void editAlertDialog( ) {
        final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(this);
        altDlgBuilder.setTitle(R.string.Warning);
        altDlgBuilder.setMessage(R.string.Are_you_sure);
        altDlgBuilder.setIcon(R.drawable.ic_error_outline_black_24dp);
        altDlgBuilder.setCancelable(true);
        altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadDataToBackend();
                dialog.dismiss();


            }
        });
        altDlgBuilder.setNegativeButton("等等", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


    }

    private void  backToFinanceFragment() {
        Intent intent = new Intent(this, TeamActivity.class);
        startActivity(intent);
    }

    private void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }


}
