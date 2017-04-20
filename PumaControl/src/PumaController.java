import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import com.yoctopuce.YoctoAPI.YAPI;
import com.yoctopuce.YoctoAPI.YAPI_Exception;
import com.yoctopuce.YoctoAPI.YSerialPort;

public class PumaController 
{
	YSerialPort pumaComms;
	
	private String URL = "127.0.0.1";
	private String header = "2323";
	private String throttleCommand = "BC0C8157FB00";
	
	/*
	 * Motor Board 
	 */
	private boolean motorPowerEnabled = true;
	private double commandedThrottle = 0;
	private double actualThrottle = 0.0;
	private double motorBoardTemperature = 0.0;
	
	/*
	 * Battery 
	 */
	private double batteryVoltage = 0.0;
	private double batteryTemperature = 0.0;
	private double batteryCapacity = 0.0;
	private double batteryCapacityRemaining = 0.0;
	private double batteryCurrent = 0.0;
	private double batteryPCBATemp = 0.0;

	
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
			TimerTask readBatteryData = new TimerTask()
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
							
							if(nextLine.length() == 40)
							{
								beginning = nextLine.substring(0, 8);
								
								
							}
							
						}
						while(!beginning.equals("2323BB14"));

						short numericBatteryTemp = (short) Integer.parseInt(nextLine.substring(12,16),16);
						batteryTemperature = (double)numericBatteryTemp/100;
						
						int batteryVoltsmV = hexStringToInt(nextLine.substring(16,20));
						batteryVoltage = (double)batteryVoltsmV/1000;
						
						
						short batteryCurrentmA = (short) Integer.parseInt(nextLine.substring(20,24),16);
						batteryCurrent = (double)batteryCurrentmA/100;
						
						
						int batteryCapacityRemainingInt = hexStringToInt(nextLine.substring(28,32));
						batteryCapacityRemaining = (double) batteryCapacityRemainingInt/1000;
						
						
						int batteryCapacityInt = hexStringToInt(nextLine.substring(24,28));
						batteryCapacity = (double) batteryCapacityInt/1000;
						
						
						int  pcbaTempInt = (short) Integer.parseInt(nextLine.substring(32,36),16);
						batteryPCBATemp = (double) pcbaTempInt/100;

						
						
						
					} catch (YAPI_Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			};
			TimerTask readMotorData = new TimerTask()
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
						
						String actualThrottleString = nextLine.substring(10,14);
						int numericActualThrottle = hexStringToInt(actualThrottleString);
						actualThrottle = ((double)numericActualThrottle - 1250)/5.074;
						actualThrottle = Math.round(actualThrottle);
						String motorTemperatureString = nextLine.substring(14,18);
						motorTemperatureString = "0x"+motorTemperatureString;
						int numericTemperature = Integer.decode(motorTemperatureString);
						motorBoardTemperature = (double)numericTemperature/100;

						
						
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
			new Timer().scheduleAtFixedRate(readMotorData, 2000, 100);
			new Timer().scheduleAtFixedRate(readBatteryData, 2000, 100);
		}

	}
	void setCommandedThrottle(double newPercent)
	{
		commandedThrottle = newPercent;
	}
	
	private int hexStringToInt(String hexString)
	{
		hexString = "0x"+hexString;
		return  Integer.decode(hexString);
	}
	
	public double getActualThrottle()
	{
		return actualThrottle;
	}
	public double getMotorTemperature()
	{
		return motorBoardTemperature;
	}
	public double getBatteryTemp()
	{
		return batteryTemperature;
	}
	public double getBatteryVoltage()
	{
		return batteryVoltage;
	}
	public double getBatteryCurrent()
	{
		return batteryCurrent;
	}
	public double getBatteryCapacity()
	{
		return batteryCapacity;
	}
	public double getBatteryCapacityRemaining()
	{
		return batteryCapacityRemaining;
	}
	public double getBatteryPCBATemp()
	{
		return batteryPCBATemp;
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
