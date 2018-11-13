package cn.shenzhenlizuosystemapp.Common.UI;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

import cn.shenzhenlizuosystemapp.Common.Adapter.SelectOutFullAdapter;
import cn.shenzhenlizuosystemapp.Common.Base.BaseActivity;
import cn.shenzhenlizuosystemapp.Common.Base.ViewManager;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.ConnectStr;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.EventBusScanDataMsg;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.OutLibraryBill;
import cn.shenzhenlizuosystemapp.Common.DataAnalysis.OutLibraryData;
import cn.shenzhenlizuosystemapp.Common.Fragment.SelectInputLibraryFragment;
import cn.shenzhenlizuosystemapp.Common.HttpConnect.WebService;
import cn.shenzhenlizuosystemapp.Common.View.RvLinearManageDivider;
import cn.shenzhenlizuosystemapp.R;

public class InputLibraryActivity extends BaseActivity {

    private TextView Back;
    private TextView TV_DeliverGoodsNumber;
    private String FGUID = "";

    private InputLibraryObServer inputLibraryObServer;
    private WebService webService;
    private List<OutLibraryData> outLibraryBills;

    @Override
    protected int inflateLayout() {
        return R.layout.scaninputlibrary_layout;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        FGUID = intent.getStringExtra("FGUID");
        ViseLog.i("FGUID" + FGUID);
        outLibraryBills = new ArrayList<OutLibraryData>();
        inputLibraryObServer = new InputLibraryObServer();
        getLifecycle().addObserver(inputLibraryObServer);
        webService = WebService.getSingleton();
        EventBus.getDefault().register(this);
        BackFinish();
        GetOutLibraryBills();
    }

    @Override
    public void initView() {
        Back = $(R.id.Back);
        TV_DeliverGoodsNumber = $(R.id.TV_DeliverGoodsNumber);
    }


    public void BackFinish() {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ViewManager.getInstance().finishActivity(InputLibraryActivity.this);
            }

        });
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

    public class GetInputLibraryBillsAsyncTask extends AsyncTask<Integer, Integer, List<OutLibraryData>> {

        private RecyclerView recyclerView;

        @Override
        protected List<OutLibraryData> doInBackground(Integer... params) {
            String OutBills = "";
            try {
                InputStream in_withcode = null;
                OutBills = webService.GetInListData(ConnectStr.ConnectionToString, FGUID);
                ViseLog.i("OutBills = " + OutBills);
                in_withcode = new ByteArrayInputStream(OutBills.getBytes("UTF-8"));
                outLibraryBills = GetInputArray(in_withcode);
            } catch (Exception e) {
                ViseLog.d("SelectOutLibraryGetOutLibraryBillsException " + e);
            }
            return outLibraryBills;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(final List<OutLibraryData> result) {
            try {
                if (result.size() >= 0) {
                    ViseLog.i("大于0" + result.get(0).getFCode()
                    );
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
        List<OutLibraryData> outbodys = handler.getBody();
        return outbodys;
    }

    public class BodySAXHandler extends DefaultHandler {
        private List<OutLibraryData> OutBodys;
        private OutLibraryData outbody;// 当前解析的student
        private String tag;// 当前解析的标签

        public List<OutLibraryData> getBody() {
            if (OutBodys != null) {
                return OutBodys;
            }
            return null;
        }

        @Override
        public void startDocument() throws SAXException {
            // 文档开始
            OutBodys = new ArrayList<OutLibraryData>();
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tag = localName;
            if (localName.equals("T_Body")) {
                outbody = new OutLibraryData();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 节点结束
            if (localName.equals("T_Body")) {
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
                if (tag.equals("FMaterial")) {
                    outbody.(data);
                } else if (tag.equals("FMaterial_Name")) {
                    outbody.setFMaterial_Name(data);
                } else if (tag.equals("FBaseQty")) {
                    outbody.setFBaseQty(data);
                } else if (tag.equals("FBoxUnitRate")) {
                    outbody.setFBoxUnitRate(data);
                } else if (tag.equals("FBoxQty")) {
                    outbody.setFBoxQty(data);
                } else if (tag.equals("FExecutedQty")) {
                    outbody.setFExecutedQty(data);
                } else if (tag.equals("FThisQty")) {
                    outbody.setFThisQty(data);
                }
            }

        }
    }
}
