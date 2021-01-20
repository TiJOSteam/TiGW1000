import java.io.IOException;

import tigateway.TiGW1000;
import tijos.framework.platform.lan.TiLAN;
import tijos.framework.platform.network.INetworkEventListener;
import tijos.framework.platform.network.NetworkInterface;
import tijos.framework.util.Delay;

/**
 * 连接事件
 * 
 * @author Administrator
 *
 */
class NetworkEventListener implements INetworkEventListener {

	/**
	 * 基站连接成功
	 */
	@Override
	public void onConnected() {
		System.out.println("connected");

	}

	@Override
	public void onDisconnected(int code, String message) {

		System.out.println("connect failed " + message);

	}

}

/**
 * LTE 4G 网络例程
 * 
 * @author Administrator
 *
 */
public class NetworkSample {

	public static void main(String[] args)  {

		// 启动lan网络,30秒超时, startup执行完成即连接成功
		// 网络事件通过事件通知
		TiLAN.getInstance().startup(30, new NetworkEventListener());

		if (TiLAN.getInstance().getNetworkStatus() != NetworkInterface.NETSTATUS_CONNECTED) {
			System.out.println("Failed to startup LAN network.");
		}
		else
		{
			System.out.println("Connected to LAN.");		
		}

		TiGW1000 gw1000 = TiGW1000.getInstance();
		
		// 注网成功 蓝灯亮
		gw1000.blueLED().turnOn();

		//获取网络信息
		try {
			System.out.println("ip " + TiLAN.getInstance().getLocalIP());

			System.out.println("gateway " + TiLAN.getInstance().getGateway());

			System.out.println("subnet" + TiLAN.getInstance().getNetMask());

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		gw1000.blueLED().turnOff();

		Delay.msDelay(10000);

		System.out.println("Exiting ...");

	}

}
