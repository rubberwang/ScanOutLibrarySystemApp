package cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.treelist.Node;
import cn.shenzhenlizuosystemapp.Common.TreeFormList.TreeData.treelist.TreeListViewAdapter;
import cn.shenzhenlizuosystemapp.R;

/**
 * Created by xiaoyehai on 2018/7/12 0012.
 */

public class ListViewAdapter extends TreeListViewAdapter {

    private Context context;
    private String name;

    public ListViewAdapter(ListView listView, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(listView, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
        this.context = context;
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (!node.getName().equals(name)) {
            node.setSelection(-1);
        }
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(node.getName());
        if (node.getIcon() == -1) {
            holder.ivExpand.setVisibility(View.INVISIBLE);
        } else {
            holder.ivExpand.setVisibility(View.VISIBLE);
            holder.ivExpand.setImageResource(node.getIcon());
        }
        if (position == node.getselection()) {
            holder.RL_TreeFromItem.setBackgroundColor(context.getResources().getColor(R.color.Thin_Bule));
        } else {
            holder.RL_TreeFromItem.setBackgroundColor(context.getResources().getColor(R.color.White));
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView tvName;
        private ImageView ivExpand;
        private RelativeLayout RL_TreeFromItem;

        public ViewHolder(View convertView) {
            tvName = convertView.findViewById(R.id.tv_name);
            ivExpand = convertView.findViewById(R.id.iv_expand);
            RL_TreeFromItem = convertView.findViewById(R.id.RL_TreeFromItem);
        }
    }

    public void SetName(String name) {
        this.name = name;
    }
}
