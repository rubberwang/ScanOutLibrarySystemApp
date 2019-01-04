package cn.shenzhenlizuosystemapp.Common.Adapter.CheckAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckSubBody;
import cn.shenzhenlizuosystemapp.R;

public class Subbody_CheckRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CheckSubBody> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public Subbody_CheckRvAdapter(Context context, List<CheckSubBody> data) {
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
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.check_subbody_datas, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            if (datas.get(position).getFCheckStockStatus().equalsIgnoreCase("124164D2-6B47-4614-8B0D-9212A459D1E2")){
                ((ViewHoders) holder).TV_CheckStatus.setText("盘亏");
            }else if (datas.get(position).getFCheckStockStatus().equalsIgnoreCase("108A8304-083C-4370-AE5C-D2E43C91CE21")){
                ((ViewHoders) holder).TV_CheckStatus.setText("未盘");
            }else {
                ((ViewHoders) holder).TV_CheckStatus.setText("盘盈");
            }
            //((ViewHoders) holder).TV_CheckStatus.setText(datas.get(position).getFCheckStockStatus());
            ((ViewHoders) holder).TV_AccountQty.setText(datas.get(position).getFAccountQty());
            ((ViewHoders) holder).TV_FCheckQty.setText(datas.get(position).getFCheckQty());
            ((ViewHoders) holder).TV_FDiffQty.setText(datas.get(position).getFDiffQty());

            if (selected == position) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.Thin_Bule));
            } else {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.BarTextColor));
            }

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
            ViseLog.i("ScanTaskException = " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHoders extends RecyclerView.ViewHolder {

        private TextView TV_CheckStatus;
        private TextView TV_AccountQty;
        private TextView TV_FCheckQty;
        private TextView TV_FDiffQty;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_CheckStatus = (TextView) itemView.findViewById(R.id.TV_CheckStatus);
            TV_AccountQty = (TextView) itemView.findViewById(R.id.TV_AccountQty);
            TV_FCheckQty = (TextView) itemView.findViewById(R.id.TV_FCheckQty);
            TV_FDiffQty = (TextView) itemView.findViewById(R.id.TV_FDiffQty);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }

    public int getselection() {
        return selected;
    }


}
