package cn.shenzhenlizuosystemapp.Common.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vise.log.ViseLog;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectCheck_FullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.Tools;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.CheckLibraryBill;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.UI.CheckLibraryActivity;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.Common.WebBean.CheckAllBean;
import cn.shenzhenlizuosystemapp.Common.Xml.CheckXmlAnalysis;
import cn.shenzhenlizuosystemapp.R;

public class Select_CheckLibrary_Fragment extends Fragment {

    public static final String ARGS_PAGE = "SelectOutLibrary_Page";
   private RecyclerView RV_InitSelectFull;
   private WebService webService;
   private Tools tools;
   private ProgressDialog PD;
   private List<CheckLibraryBill> checkLibraryBills;

    public static Select_CheckLibrary_Fragment newInstance() {
        Select_CheckLibrary_Fragment fragment = new Select_CheckLibrary_Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.init_select_quitlibrary, container, false);
        RV_InitSelectFull = rootView.findViewById(R.id.RV_InitSelectFull);
        tools = new Tools();
        webService = new WebService(this.getActivity());
        PD = new ProgressDialog(this.getActivity());
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setCancelable(false);
        GetOutLibraryBills();
        return rootView;
    }

    private void GetOutLibraryBills() {
        GetOutLibraryBillsAsyncTask getOutLibraryBillsAsyncTask = new GetOutLibraryBillsAsyncTask(RV_InitSelectFull);
        getOutLibraryBillsAsyncTask.execute();
    }

    public class GetOutLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<CheckLibraryBill>> {

        private RecyclerView recyclerView;

        public GetOutLibraryBillsAsyncTask(RecyclerView recyclerView) {
            super();
            this.recyclerView = recyclerView;
        }

        @Override
        protected List<CheckLibraryBill> doInBackground(Integer... params) {
            String CheckBills = "";
            try {
                InputStream in_withcode = null;
                CheckBills = webService.GetCheckLibraryNote(ConnectStr.ConnectionToString);
                ViseLog.i("CheckBills = " + CheckBills);
                in_withcode = new ByteArrayInputStream(CheckBills.getBytes("UTF-8"));
                List<CheckAllBean> ResultXmlList = CheckXmlAnalysis.getSingleton().GetAllCheckList(in_withcode);
                in_withcode.close();
                if (ResultXmlList.get(0).getFStatus().equals("1")) {
                    InputStream inputInfoStream = new ByteArrayInputStream(ResultXmlList.get(0).getFInfo().getBytes("UTF-8"));
                    checkLibraryBills = CheckXmlAnalysis.getSingleton().GetCheckInfoXml(inputInfoStream);
                    inputInfoStream.close();
                } else {
                    checkLibraryBills.clear();
                }
            } catch (Exception e) {
                ViseLog.d("SelectOutLibraryGetOutLibraryBillsException " + e);
            }
            return checkLibraryBills;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(final List<CheckLibraryBill> result) {
            try {
                PD.dismiss();
                if (result.size() >= 0) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.addItemDecoration(new RvLinearManageDivider(getActivity(), LinearLayoutManager.VERTICAL));
                    recyclerView.setLayoutManager(layoutManager);
                    SelectCheck_FullAdapter adapter = new SelectCheck_FullAdapter(getActivity(), result);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickLitener(new SelectCheck_FullAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = (new Intent(getActivity(),CheckLibraryActivity.class));
                            intent.putExtra("FGUID", result.get(position).getFGuid());
                            ViseLog.i("FGUID" + result.get(position).getFGuid());
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                        }
                    });
                }
            } catch (Exception e) {
                ViseLog.d("Select适配RV数据错误" + e);
                tools.showshort(getActivity(), "出库数据加载错误，请重新打开");
            }
            ViseLog.i("出库单返回数据" + result);
        }

        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            PD.setTitle("正在获取数据请稍后...");
            PD.show();
        }
    }

    public List getOutLibraryFromXMl(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        SAXHandler handler = new SAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<CheckLibraryBill> OutLibrary = handler.getBills();
        return OutLibrary;
    }

    public class SAXHandler extends DefaultHandler {
        private List<CheckLibraryBill> Checkbills;
        private CheckLibraryBill Bill;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<CheckLibraryBill> getBills() {
            if (Checkbills != null) {
                return Checkbills;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            Checkbills = new ArrayList<CheckLibraryBill>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Table")) {
                Bill = new CheckLibraryBill();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Table")) {
                Checkbills.add(Bill);
                Bill = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("FGuid")) {
                    Bill.setFGuid(data);
                } else if (tag.equals("FCode")) {
                    Bill.setFCode(data);
                } else if (tag.equals("FStock")) {
                    Bill.setFStock(data);
                } else if (tag.equals("FStock_Name")) {
                    Bill.setFStock_Name(data);
                } else if (tag.equals("FTransactionType")) {
                    Bill.setFTransactionType(data);
                } else if (tag.equals("FTransactionType_Name")) {
                    Bill.setFTransactionType_Name(data);
                } else if (tag.equals("FDate")) {
                    Bill.setFDate(data);
                }
            }

        }
    }

    public  List<CheckLibraryBill> GetSelectBills() {
        return this.checkLibraryBills;
    }
}
