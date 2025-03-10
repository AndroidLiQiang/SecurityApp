/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mobiletest.ui.test5g;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.mobiletest.App;
import com.example.mobiletest.R;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This is a sample APDU Service which demonstrates how to interface with the card emulation support
 * added in Android 4.4, KitKat.
 *
 * <p>This sample replies to any requests sent with the string "Hello World". In real-world
 * situations, you would need to modify this code to implement your desired communication
 * protocol.
 *
 * <p>This sample will be invoked for any terminals selecting AIDs of 0xF11111111, 0xF22222222, or
 * 0xF33333333. See src/main/res/xml/aid_list.xml for more details.
 *
 * <p class="note">Note: This is a low-level interface. Unlike the NdefMessage many developers
 * are familiar with for implementing Android Beam in apps, card emulation only provides a
 * byte-array based communication channel. It is left to developers to implement higher level
 * protocol support as needed.
 */
public class CardService extends HostApduService {
    private static final String TAG = "CardService";
    // AID for our loyalty card service.
    private static final String SAMPLE_LOYALTY_CARD_AID = "A0000002471001";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String GET_DATA_APDU_HEADER = "00CA0000";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = HexStringToByteArray("9000");
    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
    private static final byte[] UNKNOWN_CMD_SW = HexStringToByteArray("0000");
    private static final byte[] SELECT_APDU = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
    private static final byte[] GET_DATA_APDU = BuildGetDataApdu();

    private static final String WRITE_DATA_APDU_HEADER = "00DA0000";
    private static final String READ_DATA_APDU_HEADER = "00EA0000";
    private static final byte[] WRITE_DATA_APDU = BuildWriteDataApdu();
    private static final byte[] READ_DATA_APDU = BuildReadDataApdu();
    private static String dataStr = null;

    /*File IO Stuffs*/
    File sdcard = Environment.getExternalStorageDirectory();
    File file = new File(sdcard,"file.txt");
    StringBuilder text = new StringBuilder();
    int pointer;

    /**
     * Called if the connection to the NFC card is lost, in order to let the application know the
     * cause for the disconnection (either a lost link, or another AID being selected by the
     * reader).
     *
     * @param reason Either DEACTIVATION_LINK_LOSS or DEACTIVATION_DESELECTED
     */
    @Override
    public void onDeactivated(int reason) {
        App.setReason(reason);
    }

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     *
     * <p class="note">If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has explicitly selected your
     * service, either as a default or just for the next tap.
     *
     * <p class="note">This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the {@link
     * #sendResponseApdu(byte[])} method later.
     *
     * @param commandApdu The APDU that received from the remote device
     * @param extras A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response APDU can be sent
     * at this point.
     */

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        App.setReason(1);
        Log.i(TAG, "Received APDU: " + ByteArrayToHexString(commandApdu));
        byte[] cmd = null;
        //length 6 is define by WRITE_DATA_APDU from read and emulatior
        //长度由reader端及卡端的命令WRITE_DATA_APDU来协商的
        if(commandApdu.length >= 6){
            cmd = Arrays.copyOf(commandApdu,6);
        }
        // If the APDU matches the SELECT AID command for this service,
        // send the loyalty card account number, followed by a SELECT_OK status trailer (0x9000).
        if (Arrays.equals(SELECT_APDU, commandApdu)) {
            String account = "some string random data";
            byte[] accountBytes = account.getBytes();
            Log.i(TAG, "Sending account number1: " + account);
            readFromFile();
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else if ((Arrays.equals(GET_DATA_APDU, commandApdu))) {
            String stringToSend;
            try {
                stringToSend = text.toString().substring(pointer, pointer + 200);
            } catch (IndexOutOfBoundsException e) {
//                Toast.makeText(this, "Reached the end of the file", Toast.LENGTH_SHORT).show();
                stringToSend = "END";
            }
            pointer += 200;byte[] accountBytes = stringToSend.getBytes();
            Log.i(TAG, "Sending substring, pointer : " + pointer + " , " + stringToSend);
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else if (cmd != null && Arrays.equals(WRITE_DATA_APDU, cmd)){
            //length 6 is define by WRITE_DATA_APDU from read and emulatior
            //长度由reader端及卡端的命令WRITE_DATA_APDU来协商的
            byte[] data = Arrays.copyOfRange(commandApdu,6,commandApdu.length);
            try {
                dataStr = new String(data, StandardCharsets.UTF_8);
                Log.i(TAG, "dataStr:" + dataStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            String account = "write success";
            byte[] accountBytes = account.getBytes();
            Log.i(TAG, "Sending account number2: " + account);
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else if (Arrays.equals(READ_DATA_APDU, cmd)){
            if(dataStr!=null) {
                //接收读卡设备的数据
                byte[] accountBytes = dataStr.getBytes();
                Intent intent = new Intent(this, PayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", getResources().getString(R.string.nfc_pay_test));
                intent.putExtra("data", dataStr);
                startActivity(intent);
                Log.i(TAG, "Sending account number3: " + dataStr);
                return ConcatArrays(accountBytes, SELECT_OK_SW);
            }else {
                byte[] accountBytes = "data error".getBytes();
                Log.i(TAG, "Sending account number4: " + dataStr);
                return ConcatArrays(accountBytes, SELECT_OK_SW);
            }
        } else {
                return UNKNOWN_CMD_SW;

        }
    }

    /**
     * Build APDU for SELECT AID command. This command indicates which service a reader is
     * interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X",
                aid.length() / 2) + aid);
    }

    /**
     * Build APDU for GET_DATA command. See ISO 7816-4.
     *
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildGetDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(GET_DATA_APDU_HEADER + "0FFF");
    }

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

    /**
     * Utility method to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        Log.i(TAG,"DATA " + data[0] + " ," +data[1]);
        return data;
    }

    /**
     * Utility method to concatenate two byte arrays.
     * @param first First array
     * @param rest Any remaining arrays
     * @return Concatenated copy of input arrays
     */
    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    private void readFromFile() {
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                text.append(line);
//                text.append('\n');
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        text.append("some string random data some string random data some string random data some string random data some string random data \n");
        text.append("some string random data some string random data some string random data some string random data some string random data \n");
        text.append("some string random data some string random data some string random data some string random data some string random data \n");
        text.append("some string random data some string random data some string random data some string random data some string random data \n");
        text.append("some string random data some string random data some string random data some string random data some string random data \n");
        text.append("some string random data some string random data some string random data some string random data some string random data \n");
    }

    public static byte[] BuildWriteDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(WRITE_DATA_APDU_HEADER + "0FFF");
    }
    public static byte[] BuildReadDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(READ_DATA_APDU_HEADER + "0FFF");
    }

}
