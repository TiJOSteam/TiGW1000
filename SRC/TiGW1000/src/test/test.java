package test;

import java.io.IOException;

import tigateway.TiGW1000;
import tigateway.modbus.rtu.ModbusRTU;
import tigateway.serialport.TiSerialPort;
import tijos.framework.util.Delay;

public class test {

	public static void main(String[] args) throws IOException {

		TiGW1000 gw1000 = TiGW1000.getInstance();
		TiSerialPort rs485 = gw1000.getRS485(9600, 8, 1, 0);
		
		gw1000.relayControl(0, 1);
		Delay.msDelay(1000);
		gw1000.relayControl(0, 0);
		Delay.msDelay(1000);
		gw1000.relayControl(1, 1);
		Delay.msDelay(1000);
		gw1000.relayControl(1, 0);
		
		
		TiSerialPort rs232 = gw1000.getRS232(9600, 8, 1,0);
				
		rs232.write("test".getBytes(), 0, 4);
		
		byte [] data = rs232.read(1000);
		
		if(data != null) {
		System.out.println(new String(data));
		}
		
		System.out.println("di0 " + gw1000.digitalInput(0));
		System.out.println("di1 " + gw1000.digitalInput(1));
		

		
		
		// MODBUS RTU
		// 通讯超时500 ms
//		ModbusRTU modbusRtu = new ModbusRTU(rs485, 500);
//
//		// MODBUS 数据处理
//		// 每5秒进行一次数据处理同时绿灯亮一次
//		while (true) {
//			gw1000.greenLED().turnOn();
//			MonitorProcess(modbusRtu);
//			gw1000.greenLED().turnOff();
//			Delay.msDelay(5000);
//
//		}
	}

	/**
	 * 通过RS485基于MODBUS协议读取设备数据
	 *
	 * @param modbusRtu
	 */
	public static void MonitorProcess(ModbusRTU modbusRtu) {
		try {
			// MODBUS device id 设备地址
			int deviceId = 1;
			// Input Register 开始地址
			int startAddr = 0;

			// Read 2 registers from start address 读取个数
			int count = 2;

			// 读取Holding Register
			modbusRtu.initReadHoldingsRequest(deviceId, startAddr, count);

			// 下发并获取响应
			int result = modbusRtu.execRequest();

			// 成功
			if (result == ModbusRTU.RESULT_OK) {

				int humdity = modbusRtu.getResponseInt16(startAddr, false);
				int temperature = modbusRtu.getResponseInt16(startAddr + 1, false);

				System.out.println("temp = " + temperature + " humdity = " + humdity);
			} else {
				System.out.println("Modbus error " + modbusRtu.getResultAsString());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
