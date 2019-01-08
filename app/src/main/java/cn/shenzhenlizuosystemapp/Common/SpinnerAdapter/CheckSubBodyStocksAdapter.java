package cn.shenzhenlizuosystemapp.Common.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.R;

public class CheckSubBodyStocksAdapter extends BaseAdapter {

    List<ItemData> data;
    Context context;

    public CheckSubBodyStocksAdapter(List<ItemData> itemData, Context context) {
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
        view = LayoutInflater.from(context).inflate(R.layout.check_stocks_spinner, null);
        TextView textView = view.findViewById(R.id.TV_SpStocks);
        textView.setText(data.get(i).getData());
        return view;
    }
}