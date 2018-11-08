package cn.shenzhenlizuosystemapp.Common.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.OutLibraryBill;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.WmsSelectData;
import cn.shenzhenlizuosystemapp.R;

public class SelectOutFullAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<OutLibraryBill> datas;
    private SelectOutFullAdapter.OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public SelectOutFullAdapter(Context context, List<OutLibraryBill> data) {
        this.context = context;
        this.datas = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(SelectOutFullAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectOutFullAdapter.ViewHoders viewHoders = new SelectOutFullAdapter.ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_rv_item_full, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((SelectOutFullAdapter.ViewHoders) holder).TV_BillSum.setText(datas.get(position).getFCode());
            ((SelectOutFullAdapter.ViewHoders) holder).TV_WarehouseName.setText(datas.get(position).getFStock_Name());
            ((SelectOutFullAdapter.ViewHoders) holder).TV_OutType.setText(datas.get(position).getFTransactionType_Name());
            ((SelectOutFullAdapter.ViewHoders) holder).TV_CreateTime.setText(datas.get(position).getFDate());
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    
    class ViewHoders extends RecyclerView.ViewHolder {

        private TextView TV_BillSum;
        private TextView TV_WarehouseName;
        private TextView TV_OutType;
        private TextView TV_CreateTime;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_BillSum = (TextView) itemView.findViewById(R.id.TV_BillSum);
            TV_WarehouseName = (TextView) itemView.findViewById(R.id.TV_WarehouseName);
            TV_OutType = (TextView) itemView.findViewById(R.id.TV_OutType);
            TV_CreateTime = (TextView) itemView.findViewById(R.id.TV_CreateTime);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }


    public int getselection() {
        return selected;
    }
}
