package cn.shenzhenlizuosystemapp.Common.Adapter.InputAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.util.List;


import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.R;

public class ScanTask_InputRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<InputTaskRvData> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public ScanTask_InputRvAdapter(Context context, List<InputTaskRvData> data) {
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
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_input_datas, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((ViewHoders) holder).TV_noSend.setText(datas.get(position).getNoInput());
            ((ViewHoders) holder).TV_Material_Code.setText(datas.get(position).getFMaterial_Code());
            ((ViewHoders) holder).TV_Model.setText(datas.get(position).getFModel() + "" + datas.get(position).getFMaterial_Name());
            ((ViewHoders) holder).TV_BaseUnit_Name.setText(datas.get(position).getFBaseUnit_Name());
            ((ViewHoders) holder).TV_Unit_Name.setText(datas.get(position).getFUnit_Name());
            ((ViewHoders) holder).TV_AuxQty.setText(datas.get(position).getFAuxQty());
            ((ViewHoders) holder).TV_FExecutedAuxQty.setText(datas.get(position).getFExecutedAuxQty());
            ((ViewHoders) holder).TV_ThisAuxQty.setText(datas.get(position).getFThisAuxQty());
            
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
        private TextView TV_Unit_Name;
        private TextView TV_AuxQty;
        private TextView TV_FExecutedAuxQty;
        private TextView TV_ThisAuxQty;
        private TextView TV_noSend;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_Material_Code = (TextView) itemView.findViewById(R.id.TV_Material_Code);
            TV_Model = (TextView) itemView.findViewById(R.id.TV_Model);
            TV_BaseUnit_Name = (TextView) itemView.findViewById(R.id.TV_BaseUnit_Name);
            TV_Unit_Name = (TextView) itemView.findViewById(R.id.TV_Unit_Name);
            TV_AuxQty = (TextView) itemView.findViewById(R.id.TV_AuxQty);
            TV_FExecutedAuxQty = (TextView) itemView.findViewById(R.id.TV_FExecutedAuxQty);
            TV_ThisAuxQty = (TextView) itemView.findViewById(R.id.TV_ThisAuxQty);
            TV_noSend = (TextView) itemView.findViewById(R.id.TV_noSend);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }

    public int getselection() {
        return selected;
    }
    
    
}
