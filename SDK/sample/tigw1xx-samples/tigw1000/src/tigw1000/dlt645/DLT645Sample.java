package tigw1000.dlt645;

import tigateway.TiGW1000;
import tigateway.protocol.dlt645.TiDLT645_2007;
import tigateway.serialport.TiSerialPort;
import tigateway.utils.Helper;
import tijos.framework.util.Formatter;

public class DLT645Sample {

	TiSerialPort serialPort;
	TiDLT645_2007 dlt645;

	MeterData meterData = new MeterData();

	public DLT645Sample()  {
		try {
			serialPort = TiGW1000.getInstance().getRS485(2400, 8, 1, 2); // 2400 8 1 鍋舵牎楠�
			dlt645 = new TiDLT645_2007(serialPort);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void loadElemeterData() {

		try {
			byte[] sn = dlt645.readMeterAddress();
			meterData.meterSN = Formatter.toHexString(sn);

			System.out.println("sn " + meterData.meterSN);

			dlt645.setMeterAddress(sn);

			System.out.println("DLT645_TAG_TOTAL_ENERGY_POWER");

			byte[] data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_TOTAL_ENERGY_POWER);
			meterData.energy = Helper.BCD2Double(data, 2);

			System.out.println("DLT645_TAG_GRID_PHASE_VOLTAGE");

			// 琛ㄨ鏁�-鐢靛帇
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_VOLTAGE_A);
			meterData.voltage = Helper.BCD2Double(data, 1);

			System.out.println("DLT645_TAG_GRID_PHASE_CURRENT");

			// 琛ㄨ鏁�-鐢垫祦
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_CURRENT_A);
			meterData.current = Helper.BCD2Double(data, 3);

			System.out.println("DLT645_TAG_GRID_PHASE_POWER_TOTAL");

			// 琛ㄨ鏁�-鐬椂鎬绘湁鍔熷姛鐜�
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_POWER_TOTAL);
			meterData.power = Helper.BCD2Double(data, 4);

			System.out.println("DLT645_TAG_INTERVAL_TEMPERATURE");

			// 娓╁害, 琛ㄥ唴娓╁害鏈�楂樹綅0琛ㄧず闆朵笂锛�1琛ㄧず闆朵笅銆傚彇鍊艰寖鍥达細0.0锝�799.9銆�
			int sign = 1;
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_INTERVAL_TEMPERATURE);
			if ((data[1] & 0x80) > 0) {
				sign = -1;
				data[1] &= (~0x80);
			}
			meterData.temperature = Helper.BCD2Double(data, 1) * sign;

			System.out.println("DLT645_TAG_STATUS_ACTIVE_POWER_4");

			// 鐢佃〃杩愯鐘舵�佸瓧4锛圓鐩告晠闅滅姸鎬侊級
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_STATUS_ACTIVE_POWER_4);
			meterData.phaseState = data[0];

			// B鐩告晠闅滅姸鎬�
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_STATUS_ACTIVE_POWER_5);
			meterData.phaseState += data[0];

			// C鐩告晠闅滅姸鎬�
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_STATUS_ACTIVE_POWER_6);
			meterData.phaseState += data[0];

			// 鍚堢浉鏁呴殰鐘舵��
			data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_STATUS_ACTIVE_POWER_7);
			meterData.phaseState += data[0];

			// 濡傛灉鏄崟鐩歌〃锛� 鍦ㄨ鍙朆C鐢靛帇鐢垫祦鏃朵細鍑洪敊
			try {
				data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_VOLTAGE_B);
				meterData.voltageB = Helper.BCD2Double(data, 1);

				data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_VOLTAGE_C);
				meterData.voltageC = Helper.BCD2Double(data, 1);

				data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_CURRENT_B);
				meterData.currentB = Helper.BCD2Double(data, 3);

				data = this.dlt645.readMeterData(TiDLT645_2007.DLT645_TAG_GRID_PHASE_CURRENT_C);
				meterData.currentC = Helper.BCD2Double(data, 3);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("meterData " + this.meterData);

	}

	public static void main(String[] args) {

		DLT645Sample dlt645sample = new DLT645Sample();
		dlt645sample.loadElemeterData();

	}

}
