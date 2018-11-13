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

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.WmsSelectData;
import cn.shenzhenlizuosystemapp.R;

public class Wms_RvAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<WmsSelectData> datas;
    private Wms_RvAdapter.OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public Wms_RvAdapter(Context context, List<WmsSelectData> data) {
        this.context = context;
        this.datas = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(Wms_RvAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Wms_RvAdapter.ViewHoders viewHoders = new Wms_RvAdapter.ViewHoders(LayoutInflater.from(parent.getContext()).inflate(R.layout.wms, parent, false));
        return viewHoders;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            ((Wms_RvAdapter.ViewHoders) holder).SelectRvItemImg.setImageResource(datas.get(position).getR_Img());
            ((Wms_RvAdapter.ViewHoders) holder).SelectRvItemTv.setText(datas.get(position).getDescribeStr());
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

        private ImageView SelectRvItemImg;
        private TextView SelectRvItemTv;

        public ViewHoders(View itemView) {
            super(itemView);
            SelectRvItemImg = (ImageView) itemView.findViewById(R.id.SelectRvItemImg);
            SelectRvItemTv = (TextView) itemView.findViewById(R.id.SelectRvItemTv);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }


    public int getselection() {
        return selected;
    }
}
