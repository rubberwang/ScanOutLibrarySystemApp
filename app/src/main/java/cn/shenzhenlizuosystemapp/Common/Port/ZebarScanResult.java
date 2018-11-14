package cn.shenzhenlizuosystemapp.Common.Port;

public interface ZebarScanResult {
    
    void OnBad(String e);
    
    void OnSuccess(String Data);
}
