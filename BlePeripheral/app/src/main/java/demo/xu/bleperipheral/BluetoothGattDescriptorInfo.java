package demo.xu.bleperipheral;

import java.util.UUID;

/**
 * Created by 601042 on 2018/6/28.
 *
 * ble的service的Characteristic的Descriptor信息
 */

public class BluetoothGattDescriptorInfo {

    //描述者的UUID
    private UUID uuid;
    //描述者的权限
    int permissions;

    public BluetoothGattDescriptorInfo(UUID uuid,int permissions){
        this.uuid = uuid;
        this.permissions = permissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }
}
