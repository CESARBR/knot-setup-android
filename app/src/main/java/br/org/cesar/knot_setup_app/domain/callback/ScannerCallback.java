package br.org.cesar.knot_setup_app.domain.callback;

import android.bluetooth.le.ScanResult;

public interface ScannerCallback {
    /**
     * Whenever we scan for a device, we must return it's result
     * @param result scan result
     */
    void onScanComplete(ScanResult result);

    /**
     * If the scan fails for some reason,
     */
    void onScanFail();

}
