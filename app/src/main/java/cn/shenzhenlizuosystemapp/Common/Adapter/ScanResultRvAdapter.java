package cn.shenzhenlizuosystemapp.Common.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.OutLibraryBill;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ScanResultData;
import cn.shenzhenlizuosystemapp.R;

public class ScanResultRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ScanResultData> datas;
    private ScanResultRvAdapter.OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public ScanResultRvAdapter(Context context, List<ScanResultData> data) {
        this.context = context;
        this.datas = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(ScanResultRvAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ScanResultRvAdapter.ViewHoders viewHoders = new ScanResultRvAdapter.ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.get_result_rv_item_full, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((ScanResultRvAdapter.ViewHoders) holder).TV_scanData.setText(datas.get(position).getScanData());
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

        private TextView TV_scanData;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_scanData = (TextView) itemView.findViewById(R.id.TV_scanData);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }


    public int getselection() {
        return selected;
    }
}
