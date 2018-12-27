package cn.shenzhenlizuosystemapp.Common.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.DirectAllotNotificationBean;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputLibraryBill;
import cn.shenzhenlizuosystemapp.R;

public class DirectAllotNotification_Adapter extends RecyclerView.Adapter {

    private Context context;
    private List<DirectAllotNotificationBean> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public DirectAllotNotification_Adapter(Context context, List<DirectAllotNotificationBean> data) {
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
        ViewHoders viewHoders = new ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.direct_allot_notification_item, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((ViewHoders) holder).TV_DirectAllotNotification_FCode.setText(datas.get(position).getFCode());
            ((ViewHoders) holder).TV_DirectAllotNotification_FData.setText(datas.get(position).getFDate());
            ((ViewHoders) holder).TV_DirectAllotNotification_FOutStockName.setText(datas.get(position).getFOutStock_Name());
            ((ViewHoders) holder).TV_DirectAllotNotification_FInStockName.setText(datas.get(position).getFInStock_Name());

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

        private TextView TV_DirectAllotNotification_FCode;
        private TextView TV_DirectAllotNotification_FData;
        private TextView TV_DirectAllotNotification_FOutStockName;
        private TextView TV_DirectAllotNotification_FInStockName;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_DirectAllotNotification_FCode = (TextView) itemView.findViewById(R.id.TV_DirectAllotNotification_FCode);
            TV_DirectAllotNotification_FData = (TextView) itemView.findViewById(R.id.TV_DirectAllotNotification_FData);
            TV_DirectAllotNotification_FOutStockName = (TextView) itemView.findViewById(R.id.TV_DirectAllotNotification_FOutStockName);
            TV_DirectAllotNotification_FInStockName = (TextView) itemView.findViewById(R.id.TV_DirectAllotNotification_FInStockName);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }


    public int getselection() {
        return selected;
    }
}
