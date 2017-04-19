import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import com.yoctopuce.YoctoAPI.YAPI;
import com.yoctopuce.YoctoAPI.YAPI_Exception;
import com.yoctopuce.YoctoAPI.YSerialPort;

public class PumaController 
{
	private String URL = "127.0.0.1";
	private String header = "2323";
	private String throttleCommand = "BC0C8157FB00";
	private boolean motorPowerEnabled = true;
	private double commandedThrottle = 0;
	YSerialPort pumaComms;
	

	
	public PumaController()
	{
		try
		{
			System.out.println("Trying to connect to " + URL);
			YAPI.RegisterHub("127.0.0.1");
		}
		catch(YAPI_Exception e)
		{
			System.out.println("Cannot contact hub");
			System.exit(1);
		}
		
		pumaComms = YSerialPort.FirstSerialPort();
		if(pumaComms == null)
		{
			System.out.println("No module connected");
			System.exit(1);
		}
		else
		{
			System.out.println("Connected!");
			System.out.println(pumaComms.toString());
			TimerTask readData = new TimerTask()
			{
				
				@Override
				public void run()
				{
					try
					{
						String nextLine;
						String beginning = "";
						do
						{
							nextLine = pumaComms.readLine();
							if(nextLine.length() == 22)
							{
								beginning = nextLine.substring(0, 6);
							}
							
						}
						while(!beginning.equals("2323BF"));
						
						String actualThrottle = nextLine.substring(10,14);
						//System.out.println(actualThrottle);
						String temperature = nextLine.substring(14,18);
						//System.out.println(temperature);
						temperature = "0x"+temperature;
						int numericTemperature = Integer.decode(temperature);
						double decimalTemperature = (double)numericTemperature/100;
						System.out.print("Motor Temperature: "+ decimalTemperature);
					} catch (YAPI_Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			};
			TimerTask motorCommand = new TimerTask()
			{

				@Override
				public void run()
				{
					
					
					int throttleInt = (int) (5.074*commandedThrottle+1250);
					
					String throttlePercentString = String.format("%04X", throttleInt);
					String command = throttleCommand+throttlePercentString;
					byte[] motorCommandBytes = DatatypeConverter.parseHexBinary(command);
					String CRC = XModemCRC.calculateCRC(motorCommandBytes);
					String fullCommand = header + command + CRC;
					try
					{
						pumaComms.writeHex(fullCommand);
					} catch (YAPI_Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					// TODO Auto-generated method stub
					
				}
				
			};

			TimerTask batteryCommand = new TimerTask() 
			{
				@Override
				public void run() 
				{
					
					if(motorPowerEnabled)
					{
						try
						{
							pumaComms.writeHex("2323BC0A8257BA039C4A");
						} catch (YAPI_Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						try
						{
							pumaComms.writeHex("2323BC0A8257BA01BC08");
							
						} catch (YAPI_Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			};
			new Timer().scheduleAtFixedRate(batteryCommand, 0, 500);
			new Timer().scheduleAtFixedRate(motorCommand, 2000, 100);
			new Timer().scheduleAtFixedRate(readData, 2000, 100);
		}

	}
	void setCommandedThrottle(double newPercent)
	{
		commandedThrottle = newPercent;
	}
	
	void enableMotorPower(boolean isOn)
	{
		if(isOn)
		{
			motorPowerEnabled = true;
		}
		else
		{
			motorPowerEnabled = false;
		}
	}
}
