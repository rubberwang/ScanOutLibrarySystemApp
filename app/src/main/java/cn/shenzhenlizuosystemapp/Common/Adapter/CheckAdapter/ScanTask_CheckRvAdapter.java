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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckTaskRvData;
import cn.shenzhenlizuosystemapp.R;

public class ScanTask_CheckRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CheckTaskRvData> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public ScanTask_CheckRvAdapter(Context context, List<CheckTaskRvData> data) {
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
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_check_datas, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((ViewHoders) holder).TV_Material_Code.setText(datas.get(position).getFMaterial_Code());
            ((ViewHoders) holder).TV_Model.setText(datas.get(position).getFMaterial_Name()+ " "+datas.get(position).getFModel());
            ((ViewHoders) holder).TV_BaseUnit_Name.setText(datas.get(position).getFBaseUnit_Name());
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

        private TextView TV_Material_Code;
        private TextView TV_Model;
        private TextView TV_BaseUnit_Name;
        private TextView TV_AccountQty;
        private TextView TV_FCheckQty;
        private TextView TV_FDiffQty;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_Material_Code = (TextView) itemView.findViewById(R.id.TV_Material_Code);
            TV_Model = (TextView) itemView.findViewById(R.id.TV_Model);
            TV_BaseUnit_Name = (TextView) itemView.findViewById(R.id.TV_BaseUnit_Name);
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
