package cn.shenzhenlizuosystemapp.Common.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ScanResultData;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitTaskRvData;
import cn.shenzhenlizuosystemapp.R;

public class ScanTask_QuitRvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<QuitTaskRvData> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public ScanTask_QuitRvAdapter(Context context, List<QuitTaskRvData> data) {
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
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_quit_datas, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            String NoSendQty = "0";
            int AuxQty = 0;
            int ExecutedAuxQty = 0;
            if (!TextUtils.isEmpty(datas.get(position).getFAuxQty()) && !TextUtils.isEmpty(datas.get(position).getFExecutedAuxQty())) {
                AuxQty = Integer.parseInt(datas.get(position).getFAuxQty().split("\\.")[0]);
                ExecutedAuxQty = Integer.parseInt(datas.get(position).getFExecutedAuxQty().split("\\.")[0]);
                NoSendQty = String.valueOf(AuxQty - ExecutedAuxQty);
            }
            ((ViewHoders) holder).TV_noSend.setText(NoSendQty);
            ((ViewHoders) holder).TV_Material_Code.setText(datas.get(position).getFMaterial_Code());
            ((ViewHoders) holder).TV_Model.setText(datas.get(position).getFModel() + "" + datas.get(position).getFMaterial_Name());
            ((ViewHoders) holder).TV_BaseUnit_Name.setText(datas.get(position).getFBaseUnit_Name());
            ((ViewHoders) holder).TV_Unit_Name.setText(datas.get(position).getFUnit_Name());
            ((ViewHoders) holder).TV_AuxQty.setText(String.valueOf(AuxQty));
            ((ViewHoders) holder).TV_FExecutedAuxQty.setText(String.valueOf(ExecutedAuxQty));
            ((ViewHoders) holder).TV_ThisAuxQty.setText(datas.get(position).getFThisAuxQty().split("\\.")[0]);

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
