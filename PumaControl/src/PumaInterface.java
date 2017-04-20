import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class PumaInterface
{
	private Scanner scanner;
	private PumaController pumaController;
	private PumaInfoPanel gui;
	public PumaInterface(PumaController pumaController)
	{
		scanner = new Scanner(System.in);
		this.pumaController = pumaController;
		gui = new PumaInfoPanel();
		
		TimerTask guiUpdateTask = new TimerTask()
		{
			
			@Override
			public void run()
			{
				gui.updateMotorData(pumaController.getActualThrottle(), pumaController.getMotorTemperature());
				gui.updateBatteryData(pumaController.getBatteryVoltage(), pumaController.getBatteryCurrent(), 
						pumaController.getBatteryCapacity(), pumaController.getBatteryCapacityRemaining(), 
						pumaController.getBatteryTemp(), pumaController.getBatteryPCBATemp());
				gui.pack();
				
			}
		};
		
		new Timer().scheduleAtFixedRate(guiUpdateTask, 0, 1000);
	}
	
	public void doControl()
	{
		if(scanner.hasNext())
		{
			String next = scanner.next();
			switch (next)
			{
				case "throttle":
					double throttlePercent = scanner.nextDouble();
					pumaController.setCommandedThrottle(throttlePercent);
					break;
				case "motor-power":
					String command = scanner.next();
					if(command.equals("on"))
					{
						pumaController.enableMotorPower(true);
					}
					else if (command.equals("off"))
					{
						pumaController.enableMotorPower(false);
					}
					break;

				default:
					break;
			}
		}
	}
}
