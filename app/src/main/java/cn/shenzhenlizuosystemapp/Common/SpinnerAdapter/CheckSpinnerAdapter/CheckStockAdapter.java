package cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.CheckSpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckDataAnalysis.CheckStockBean;
import cn.shenzhenlizuosystemapp.R;

public class CheckStockAdapter extends BaseAdapter {

    List<CheckStockBean> data;
    Context context;

    public CheckStockAdapter(List<CheckStockBean> itemData, Context context) {
        this.data = itemData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.check_spinner, null);
        TextView textView = view.findViewById(R.id.TV_CheckSpinner);
        textView.setText(data.get(i).getFName());
        return view;
    }
}