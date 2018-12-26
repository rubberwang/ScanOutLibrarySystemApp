package cn.shenzhenlizuosystemapp.Common.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ZebarTools {

    private static ZebarTools zebarTools = null;

    public static ZebarTools getZebarTools() {
        if (zebarTools == null) {
            synchronized (ZebarTools.class) {
                if (zebarTools == null) {
                    zebarTools = new ZebarTools();
                }
            }
        }
        return zebarTools;
    }

    public void SetZebarDWConfig(Context context, String ScanTagSum, String scanning_mode) {
        String profileName = "LiZuoProfile";
        Bundle bMain = new Bundle();
        bMain.putString("PROFILE_NAME", profileName);            // <- "Profile12" is a bundle
        bMain.putString("PROFILE_ENABLED", "true");              // <- that will be enabled
        bMain.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");   // <- or created if necessary.
        Bundle bConfig = new Bundle();
        bConfig.putString("PLUGIN_NAME", "BARCODE");
        bConfig.putString("RESET_CONFIG", "false");
        Bundle bParams = new Bundle();
        bParams.putString("scanner_selection", "auto");
        bParams.putString("scanner_input_enabled", "true");
        bParams.putString("picklist", "0");
        bParams.putString("scanning_mode", scanning_mode);
        bParams.putString("multi_barcode_count", ScanTagSum);
        bConfig.putBundle("PARAM_LIST", bParams);
        bMain.putBundle("PLUGIN_CONFIG", bConfig);
        Intent i = new Intent();
        i.setAction("com.symbol.datawedge.api.ACTION");
        i.putExtra("com.symbol.datawedge.api.SET_CONFIG", bMain);
        context.sendBroadcast(i);
//      BarCode 嵌套的捆绑包
    }

}
