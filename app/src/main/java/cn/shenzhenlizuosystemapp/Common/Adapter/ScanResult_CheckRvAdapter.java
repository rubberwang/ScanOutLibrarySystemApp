package cn.shenzhenlizuosystemapp.Common.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ChildCheckTag;
import cn.shenzhenlizuosystemapp.R;

public class ScanResult_CheckRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ChildCheckTag> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public ScanResult_CheckRvAdapter(Context context, List<ChildCheckTag> data) {
        this.context = context;
        this.datas = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.scaning_result_item, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((ViewHoders) holder).TV_scanData.setText(datas.get(position).getValue());
            ((ViewHoders) holder).Tv_TypeResult.setText(datas.get(position).getName() + ":");
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
        private TextView Tv_TypeResult;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_scanData = (TextView) itemView.findViewById(R.id.TV_scanData);
            Tv_TypeResult = (TextView) itemView.findViewById(R.id.Tv_TypeResult);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }


    public int getselection() {
        return selected;
    }
}
