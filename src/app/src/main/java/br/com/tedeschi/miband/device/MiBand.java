package br.com.tedeschi.miband.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MiBand {
    private static final String TAG = MiBand.class.getName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;

    public void connect(Context context, String address) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
        mBluetoothGatt = mBluetoothDevice.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG, "STATE_CONNECTED");

                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d(TAG, "STATE_DISCONNECTED");

                    try {
                        gatt.close();
                    } catch (Exception e) {
                        Log.d(TAG, "close ignoring: " + e);
                    }
                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.d(TAG, "onServicesDiscovered " + getStatusString(status));
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.d(TAG, "onCharacteristicWrite " + characteristic.getUuid() + " " + Arrays.toString(characteristic.getValue()) + " " + getStatusString(status));
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.d(TAG, "onCharacteristicChanged " + characteristic.getUuid() + " " + Arrays.toString(characteristic.getValue()));
            }
        });
    }

    public void sendNotification(String title, String message, byte customIconId) {
        for (byte[] chunk : encodeNotification(title, message, customIconId)) {
            BluetoothGattService service = mBluetoothGatt.getService(Gatt.UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(Gatt.UUID_CHARACTERISTIC);
            characteristic.setValue(chunk);
            //characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            mBluetoothGatt.writeCharacteristic(characteristic);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<byte[]> encodeNotification(String title, String message, byte customIconId) {
        String appName = "\0" + "UNKNOWN" + "\0";

        if (title != null) {
            appName = "\0" + title + "\0";
        }

        int maxLength = 230;
        int prefixlength = 3;
        byte[] appSuffix = appName.getBytes();
        int suffixlength = appSuffix.length;
        byte[] rawmessage = message.getBytes();
        int length = Math.min(rawmessage.length, maxLength - prefixlength);
        byte[] command = new byte[length + prefixlength + suffixlength];

        command[0] = (byte) -6;
        command[1] = 1;
        command[2] = 7;

        System.arraycopy(rawmessage, 0, command, prefixlength, length);
        System.arraycopy(appSuffix, 0, command, prefixlength + length, appSuffix.length);

        Log.d(TAG, "[Raw] " + Arrays.toString(command));

        List<byte[]> chunked = chunk(command);

        return chunked;
    }

    private List<byte[]> chunk(byte[] data) {
        int type = 0;
        final int MAX_CHUNKLENGTH = 17;
        int remaining = data.length;
        byte count = 0;

        List<byte[]> list = new ArrayList<byte[]>();

        while (remaining > 0) {
            int copybytes = Math.min(remaining, MAX_CHUNKLENGTH);
            byte[] chunk = new byte[copybytes + 3];

            byte flags = 0;
            if (remaining <= MAX_CHUNKLENGTH) {
                flags |= 0x80; // last chunk
                if (count == 0) {
                    flags |= 0x40; // weird but true
                }
            } else if (count > 0) {
                flags |= 0x40; // consecutive chunk
            }

            chunk[0] = 0;
            chunk[1] = (byte) (flags | type);
            chunk[2] = (byte) (count & 0xff);

            System.arraycopy(data, count++ * MAX_CHUNKLENGTH, chunk, 3, copybytes);

            list.add(chunk);

            Log.d(TAG, "[Chunk] " + Arrays.toString(chunk));

            remaining -= copybytes;
        }

        return list;
    }

    private String getStatusString(int status) {
        return status == BluetoothGatt.GATT_SUCCESS ? " (success)" : " (failed: " + status + ")";
    }
}
