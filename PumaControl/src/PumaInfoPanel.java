import javax.swing.BoxLayout;
import javax.swing.JFrame;


public class PumaInfoPanel extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MotorInfoPanel motorInfo;
	BatteryInfoPanel battInfo;
	
	public PumaInfoPanel()
	{
		super();
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		motorInfo = new MotorInfoPanel();
		battInfo = new BatteryInfoPanel();
		this.add(motorInfo);
		this.add(battInfo);
		this.pack();
		this.setVisible(true);
	}
	
	public void updateMotorData(double throttle, double temp)
	{
		motorInfo.updateData(temp, throttle);
	}
	public void updateBatteryData(double voltage, double current, double totalCapacity, double actualCapacity, double temp, double pcbaTemp )
	{
		double power = -Math.round(voltage*current);
		double level = Math.round(100*actualCapacity/totalCapacity);
		battInfo.updateData(voltage, current, power, level, temp, pcbaTemp);
	}

}
