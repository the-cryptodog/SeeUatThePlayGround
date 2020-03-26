package com.app.myteammanager.ui.myteam.teamdatafragments.Finance;



import android.os.Parcel;
import android.os.Parcelable;

import com.app.myteammanager.recycleview.BaseModel;

public class FinanceRecordModel implements BaseModel , Parcelable {

    public static final String TYPE = "FinanceRecordModel";

    int walletId;
    String recordDate;
    String walletDescribe;
    int cashFlow;
    int sum;

    public FinanceRecordModel(int walletId, String recordDate, String walletDescribe, int cashflow, int sum){
        this.walletId = walletId;
        this.recordDate=recordDate;
        this.walletDescribe=walletDescribe;
        this.cashFlow =cashflow;
        this.sum=sum;

    }
    public int getRmonth() {
        String tmp[] = this.recordDate.split("-");
        int tmpInt= Integer.valueOf(tmp[1]);
        return tmpInt;
    }
    public int getRyear() {
        String tmp[] = this.recordDate.split("-");
        int tmpInt= Integer.valueOf(tmp[0]);
        return tmpInt;
    }

    public int getRdate() {
        String tmp[] = this.recordDate.split("-");
        int tmpInt= Integer.valueOf(tmp[2]);
        return tmpInt;
    }


    protected FinanceRecordModel(Parcel in) {
        walletId = in.readInt();
        recordDate = in.readString();
        walletDescribe = in.readString();
        cashFlow = in.readInt();
        sum = in.readInt();
    }

    public static final Creator<FinanceRecordModel> CREATOR = new Creator<FinanceRecordModel>() {
        @Override
        public FinanceRecordModel createFromParcel(Parcel in) {
            return new FinanceRecordModel(in);
        }

        @Override
        public FinanceRecordModel[] newArray(int size) {
            return new FinanceRecordModel[size];
        }
    };

    public String getDate() {
        return recordDate;
    }

    public void setDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getWalletDescribe() {
        return walletDescribe;
    }

    public void setWalletDescribe(String walletDescribe) {
        this.walletDescribe = walletDescribe;
    }

    public int getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(int cashFlow) {
        this.cashFlow = cashFlow;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }


    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(walletId);
        dest.writeString(recordDate);
        dest.writeString(walletDescribe);
        dest.writeInt(cashFlow);
        dest.writeInt(sum);
    }
}
