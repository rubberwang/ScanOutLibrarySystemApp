package cn.shenzhenlizuosystemapp.Common.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitMaterialModeBean;
import cn.shenzhenlizuosystemapp.R;

public class QuitMaterialModeAdapter extends BaseAdapter {

    private List<QuitMaterialModeBean> data;
    private Context context;

    public QuitMaterialModeAdapter(List<QuitMaterialModeBean> itemData, Context context) {
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
        view = LayoutInflater.from(context).inflate(R.layout.quit_spinner, null);
        TextView textView = view.findViewById(R.id.TV_QuitSpinner);
        TextView TV_BarCodeConunt = view.findViewById(R.id.TV_BarCodeConunt);
        textView.setText(data.get(i).getFName());
        TV_BarCodeConunt.setText("  /" + data.get(i).getFBarCoeeCount() + "个码");
        return view;
    }
}