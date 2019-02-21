package cn.shenzhenlizuosystemapp.Common.Adapter.InputAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vise.log.ViseLog;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.InputTaskRvData;
import cn.shenzhenlizuosystemapp.R;

public class ScanTask_RFID_InputRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<InputTaskRvData> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public ScanTask_RFID_InputRvAdapter(Context context, List<InputTaskRvData> data) {
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
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_input_datas_rfid, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((ViewHoders) holder).TV_Material_Code.setText(datas.get(position).getFMaterial_Code());
            ((ViewHoders) holder).TV_AuxQty.setText(datas.get(position).getFAuxQty().split("\\.")[0]);
            ((ViewHoders) holder).TV_ThisAuxQty.setText(datas.get(position).getFThisAuxQty().split("\\.")[0]);
            
            if (selected == position) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.Thin_Bule));

            } else {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.BarTextColor));
            }

            if (((ViewHoders) holder).TV_AuxQty.getText().toString().equals(((ViewHoders) holder).TV_ThisAuxQty.getText().toString())){
                ((ViewHoders) holder).TV_ThisAuxQty.setTextColor(context.getResources().getColor(R.color.ToastRed));
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
        private TextView TV_AuxQty;
        private TextView TV_ThisAuxQty;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_Material_Code = (TextView) itemView.findViewById(R.id.TV_Material_Code);
            TV_AuxQty = (TextView) itemView.findViewById(R.id.TV_AuxQty);
            TV_ThisAuxQty = (TextView) itemView.findViewById(R.id.TV_ThisAuxQty);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }

    public int getselection() {
        return selected;
    }
    
    
}
