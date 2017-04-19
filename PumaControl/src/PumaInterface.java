import java.util.Scanner;

public class PumaInterface
{
	private Scanner scanner;
	private PumaController pumaController;
	
	public PumaInterface(PumaController pumaController)
	{
		scanner = new Scanner(System.in);
		this.pumaController = pumaController;
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
