package cn.shenzhenlizuosystemapp.Common.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vise.log.ViseLog;

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
            String[] NameRoot = datas.get(position).getTV_nameRoot().split("\\.");
            String[] ShouldSend = datas.get(position).getTV_shouldSend().split("\\.");
            String[] AlreadySend = datas.get(position).getTV_alreadySend().split("\\.");
            String[] ThisSend = datas.get(position).getTV_thisSend().split("\\.");

            int NoSend = Integer.parseInt(ShouldSend[0]) - Integer.parseInt(AlreadySend[0]);

            ((ViewHoders) holder).TV_materID.setText(datas.get(position).getTV_materID());
            ((ViewHoders) holder).TV_nameRoot.setText(NameRoot[0]);
            ((ViewHoders) holder).TV_statistics.setText(datas.get(position).getTV_statistics());
            ((ViewHoders) holder).TV_size.setText(datas.get(position).getTV_size());
            ((ViewHoders) holder).TV_commonunit.setText(datas.get(position).getTV_commonunit());
            ((ViewHoders) holder).TV_alreadySend.setText(AlreadySend[0]);
            ((ViewHoders) holder).TV_thisSend.setText(ThisSend[0]);
            ((ViewHoders) holder).TV_shouldSend.setText(ShouldSend[0]);
            ((ViewHoders) holder).TV_noSend.setText(NoSend + "");

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

        private TextView TV_materID;
        private TextView TV_nameRoot;
        private TextView TV_statistics;
        private TextView TV_shouldSend;
        private TextView TV_alreadySend;
        private TextView TV_thisSend;
        private TextView TV_noSend;
        private TextView TV_size;
        private TextView TV_commonunit;

        public ViewHoders(View itemView) {
            super(itemView);
            TV_materID = (TextView) itemView.findViewById(R.id.TV_materID);
            TV_nameRoot = (TextView) itemView.findViewById(R.id.TV_nameRoot);
            TV_statistics = (TextView) itemView.findViewById(R.id.TV_statistics);
            TV_shouldSend = (TextView) itemView.findViewById(R.id.TV_shouldSend);
            TV_alreadySend = (TextView) itemView.findViewById(R.id.TV_alreadySend);
            TV_thisSend = (TextView) itemView.findViewById(R.id.TV_thisSend);
            TV_noSend = (TextView) itemView.findViewById(R.id.TV_noSend);
            TV_size = (TextView) itemView.findViewById(R.id.TV_size);
            TV_commonunit = (TextView) itemView.findViewById(R.id.TV_commonunit);
        }
    }

    public void setSelection(int position) {
        this.selected = position;
    }

    public int getselection() {
        return selected;
    }
}
