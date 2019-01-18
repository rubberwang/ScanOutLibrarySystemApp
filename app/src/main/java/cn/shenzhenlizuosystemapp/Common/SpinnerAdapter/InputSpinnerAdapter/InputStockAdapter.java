package cn.shenzhenlizuosystemapp.Common.SpinnerAdapter.InputSpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.InputDataAnalysis.StockBean;
import cn.shenzhenlizuosystemapp.R;

public class InputStockAdapter extends BaseAdapter {

    List<StockBean> data;
    Context context;

    public InputStockAdapter(List<StockBean> itemData, Context context) {
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
        view = LayoutInflater.from(context).inflate(R.layout.input_spinner, null);
        TextView textView = view.findViewById(R.id.TV_InputSpinner);
        textView.setText(data.get(i).getFName());
        return view;
    }
}