package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.QuitLibraryDetail;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.ItemData;
import cn.shenzhenlizuosystemapp.Common.LoginSpinnerAdapter.LoginAdapter;
import cn.shenzhenlizuosystemapp.R;

public class InputLibraryActivity extends BaseActivity {

    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private TextView TV_Time;
    private Spinner Sp_house;
    private TextView TV_BusType;
    private TextView TV_Unit;
    private String FGUID = "";

    private InputLibraryObServer inputLibraryObServer;
    private WebService webService;
    private List<ItemData> SpStrList;
    private List<QuitLibraryDetail> quitLibraryDetails;

    @Override
    protected int inflateLayout() {
        return R.layout.scaning_input_layout;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        inputLibraryObServer = new InputLibraryObServer();
        getLifecycle().addObserver(inputLibraryObServer);
        SpStrList = new ArrayList<>();
        webService = WebService.getSingleton();
        EventBus.getDefault().register(this);
        BackFinish();

        GetOutLibraryBills();
        InitSp();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        TV_DeliverGoodsNumber = $(R.id.TV_DeliverGoodsNumber);
        TV_Time = $(R.id.TV_Time);
        Sp_house = $(R.id.Sp_house);
        TV_BusType = $(R.id.TV_BusType);
        TV_Unit = $(R.id.TV_Unit);
    }

    public void BackFinish() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(InputLibraryActivity.this);
            }

        });
    }

    private void InitSp(){
        if (quitLibraryDetails.size()>=0){
            for (QuitLibraryDetail quitLibraryDetail:quitLibraryDetails){
                ItemData itemData = new ItemData();
                itemData.setData(quitLibraryDetail.getFStock_Name());
                SpStrList.add(itemData);
            }
        }
        LoginAdapter loginAdapter = new LoginAdapter(SpStrList, InputLibraryActivity.this);
        Sp_house.setAdapter(loginAdapter);
    }

    class InputLibraryObServer implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void ON_CREATE() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void ON_START() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void ON_RESUME() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void ON_PAUSE() {
            EventBus.getDefault().unregister(this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void ON_STOP() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void ON_DESTROY() {
        }
    }

    private void GetOutLibraryBills() {
        GetInputLibraryBillsAsyncTask getOutLibraryBillsAsyncTask = new GetInputLibraryBillsAsyncTask();
        getOutLibraryBillsAsyncTask.execute();
    }

    public class GetInputLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<QuitLibraryDetail>> {

        private RecyclerView recyclerView;

        @Override
        protected List<QuitLibraryDetail> doInBackground(Integer... params) {
            String OutBills = "";
            List<QuitLibraryDetail> outLibraryBills = new ArrayList<>();
            InputStream in_withcode = null;
            try {
                OutBills = webService.GetWareHouseData(ConnectStr.ConnectionToString, FGUID);
                ViseLog.i("OutBills = " + OutBills);
                in_withcode = new ByteArrayInputStream(OutBills.getBytes("UTF-8"));
                outLibraryBills = GetInputArray(in_withcode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return outLibraryBills;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(final List<QuitLibraryDetail> result) {
            try {
                if (result.size() >= 0) {
                    TV_DeliverGoodsNumber.setText(result.get(0).getFCode());
                    TV_Time.setText(result.get(0).getFDate());
                    //TV_house.setDropDownHorizontalOffset(result.get(0).getFStock_Name());
                    TV_BusType.setText(result.get(0).getFTransactionType_Name());
                    TV_Unit.setText(result.get(0).getFPartner_Name());
                    quitLibraryDetails = result;
                }
            } catch (Exception e) {
                ViseLog.d("Select适配RV数据错误" + e);
            }
            ViseLog.i("出库单返回数据" + result);
        }

        @Override
        protected void onPreExecute() {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(EventBusScanDataMsg event) {
        ViseLog.i("EventBus = " + event.ScanDataMsg);
    }

    public List GetInputArray(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();//创建SAX解析工厂
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();//创建SAX解析器
        BodySAXHandler handler = new BodySAXHandler();//创建处理函数
        parser.parse(stream, handler);//开始解析
        List<QuitLibraryDetail> outbodys = handler.getBody();
        return outbodys;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<QuitLibraryDetail> OutBodys;
        private QuitLibraryDetail outbody;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<QuitLibraryDetail> getBody() {
            if (OutBodys != null) {
                return OutBodys;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            OutBodys = new ArrayList<QuitLibraryDetail>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("Table") || localName.equals("Table1")) {
                outbody = new QuitLibraryDetail();
                ViseLog.i("创建outbody");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("Table") || localName.equals("Table1")) {
                OutBodys.add(outbody);
                outbody = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String data = new String(ch, start, length);
            if (data != null && tag != null) {
                if (tag.equals("FGuid")) {
                    outbody.setFGuid(data);
                    ViseLog.i(data);
                } else if (tag.equals("FCode")) {
                    outbody.setFCode(data);
                } else if (tag.equals("FStock")) {
                    outbody.setFStock(data);
                } else if (tag.equals("FStock_Name")) {
                    outbody.setFStock_Name(data);
                } else if (tag.equals("FTransactionType")) {
                    outbody.setFTransactionType(data);
                } else if (tag.equals("FTransactionType_Name")) {
                    outbody.setFTransactionType_Name(data);
                } else if (tag.equals("FPartner")) {
                    outbody.setFPartner(data);
                } else if (tag.equals("FPartner_Name")) {
                    outbody.setFPartner_Name(data);
                } else if (tag.equals("FDate")) {
                    outbody.setFDate(data);
                }
            }

        }
    }
}
