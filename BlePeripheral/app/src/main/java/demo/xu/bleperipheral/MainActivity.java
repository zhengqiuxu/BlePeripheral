package demo.xu.bleperipheral;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

import android.bluetooth.BluetoothGattService;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    public final static UUID UUID_SERVER = UUID.fromString("0000181C-0000-1000-8000-00805F9B34FB");
    public final static UUID UUID_SERVER1 = UUID.fromString("0000181C-0000-1000-8000-00805F9B34FA");
    public final static UUID UUID_CHARREAD = UUID.fromString("0000C101-0000-1000-8000-00805F9B3401");
    public final static UUID UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARWRITE = UUID.fromString("0000C101-0000-1000-8000-00805F9B3402");
    public final static UUID UUID_WRITE = UUID.fromString("0000C101-0000-1000-8000-00805F9B3403");

    BluetoothGattCharacteristic characteristicnotify;


    BlePeripheralUtils blePeripheralUtils;


    BlePeripheralCallback callback = new BlePeripheralCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {

        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] requestBytes) {

        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button open = (Button) findViewById(R.id.open);
        Button send = (Button) findViewById(R.id.send);

        //实例化工具类
        blePeripheralUtils = new BlePeripheralUtils(MainActivity.this);
        //初始化一下
        blePeripheralUtils.init();
        //设置一个结果callback 方便把某些结果传到前面来
        blePeripheralUtils.setBlePeripheralCallback(callback);


        final byte[] serviceData = "1111111".getBytes();

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先打开广播
                blePeripheralUtils.startBluetoothLeAdvertiser("xuge", serviceData, UUID_SERVER1);
                //实例化需要添加的service信息
                BluetoothGattCharacteristicInfo[] bluetoothGattCharacteristicInfos = new BluetoothGattCharacteristicInfo[3];
                BluetoothGattDescriptorInfo descriptorInfo = new BluetoothGattDescriptorInfo(UUID_DESCRIPTOR, BluetoothGattCharacteristic.PERMISSION_WRITE);
                bluetoothGattCharacteristicInfos[0] = new BluetoothGattCharacteristicInfo(UUID_CHARREAD, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ,null);
                bluetoothGattCharacteristicInfos[1] = new BluetoothGattCharacteristicInfo(UUID_WRITE, BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE,null);
                bluetoothGattCharacteristicInfos[2] = new BluetoothGattCharacteristicInfo(UUID_CHARWRITE, BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PROPERTY_NOTIFY,descriptorInfo);
                BluetoothGattServiceInfo bluetoothGattServiceInfo1 = new BluetoothGattServiceInfo(UUID_SERVER,BluetoothGattService.SERVICE_TYPE_PRIMARY,bluetoothGattCharacteristicInfos);
                //添加需要的service
                blePeripheralUtils.addServices(bluetoothGattServiceInfo1);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //看看characteristic是否为空  为空就获取一下
                if(characteristicnotify == null){
                    characteristicnotify = blePeripheralUtils.getCharacteristic(UUID_SERVER,UUID_CHARWRITE);
                }
                //发送出去  这里的device是我随便拿的 实际情况一般都是用BlePeripheralCallback返回的
                blePeripheralUtils.notify(blePeripheralUtils.getDeviceArrayList().get(0),characteristicnotify,"11111".getBytes());
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        blePeripheralUtils.stopBluetoothLeAdvertiser();
    }
}
