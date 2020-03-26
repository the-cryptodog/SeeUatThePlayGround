package com.app.myteammanager.recycleview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private List<? extends BaseViewHolder.Factory> factories;
    private Map<String, Integer> typeMap;
    private List<BaseModel> modelList; //所要呈現的資料List

    public CommonAdapter(List<? extends BaseViewHolder.Factory> factories){ //傳入從BaseViewHolder那裡獲得的ViewHolder的工廠清單
        this.factories = factories;
        this.typeMap = new HashMap<>();
        /* type mapping */
        int count = factories.size();
        for(int i=0;i<count;i++){
            BaseViewHolder.Factory factory = factories.get(i);// 這是對應位置的工廠
            typeMap.put(factory.getType(), i);// 工廠的TYPE 對應到 Array的位置 (工廠字串,array的位置(新增順序))
        }
        /* type mapping end*/
    }

    @Override
    public int getItemViewType(int position) {
        return typeMap.get(modelList.get(position).getType());
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        BaseViewHolder.Factory factory = factories.get(i);
        return factory.onCreateViewHolder(parent, i);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(modelList.get(position));

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    /* data binding */
    public void bindDataSource(List<BaseModel> models) {
        this.modelList = models;
        notifyDataSetChanged();
    }
    public void addData(BaseModel model){
        this.modelList.add(model);
        notifyItemInserted(this.modelList.size() - 1);
    }
    public void removeData(int position){
        this.modelList.remove(position);
        notifyItemRemoved(position);
    }
    public void refresh(List<BaseModel> models) {
        this.modelList.clear();
        this.modelList = models;
        notifyDataSetChanged();
    }


}
