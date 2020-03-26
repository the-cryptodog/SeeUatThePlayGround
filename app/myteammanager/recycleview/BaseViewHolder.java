package com.app.myteammanager.recycleview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.util.HashMap;
import java.util.Map;

public abstract class BaseViewHolder<T extends BaseModel> extends RecyclerView.ViewHolder {

    public interface ClickHandler<T>{ //點擊方法介面
        void onClick(int position);
        void reTurnData( int position, int integerData);
    }


    public abstract static class Factory implements BaseModel{ //點擊方法介面工廠

        protected Map<Integer, ClickHandler> clickFuncMap;

        public Factory(@Nullable ClickFuncBuilder clickFuncBuilder){  //建構子需要一個自帶點擊事件的Map，由 ClickFuncBuilder創建
            if(clickFuncBuilder != null)
                this.clickFuncMap = clickFuncBuilder.getMap();
        }
        @NonNull
        public abstract BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType); //回傳客製化BaseViewHolder

    }

     public static class ClickFuncBuilder{ //建立點擊方法的builer

        private Map<Integer, ClickHandler> clickFuncMap;//自有一點擊事件的Map<觸發元件的id,處理觸發事件的介面>
        public ClickFuncBuilder(){
            this.clickFuncMap = new HashMap<>();
        }
        public ClickFuncBuilder add(Integer resourceId, ClickHandler clickHandler){ //新增點擊事件<觸發元件的id,處理觸發事件的介面>
            this.clickFuncMap.put(resourceId, clickHandler);
            return this;
        }//可以重複加入方法
        private Map<Integer, ClickHandler> getMap(){ //取得其Map
            return this.clickFuncMap;
        }
    }

    protected BaseViewHolder(View itemView) {//建構子
        super(itemView);
    }

    public abstract void bind(T baseModel);
}
