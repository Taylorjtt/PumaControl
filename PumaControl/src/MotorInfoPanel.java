import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MotorInfoPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel throttlePercent;
	JLabel motorTemp;
	
	public MotorInfoPanel()
	{
		super(new FlowLayout(FlowLayout.CENTER,20,10));
		setBorder(BorderFactory.createTitledBorder("Motor Info"));
		throttlePercent = new JLabel("Throttle Percent:    %");
		motorTemp = new JLabel("Motor Temperature:    °C");
		this.add(throttlePercent);
		this.add(motorTemp);
		this.setVisible(true);
	}
	
	private void updateThrottlePercent(double throttlePercent)
	{
		this.throttlePercent.setText("Throttle Percent: "+throttlePercent+"%");
	}
	private void updatemotorTemp(double motorTemp)
	{
		this.motorTemp.setText("Motor Temperature: "+motorTemp+"°C");
	}
	
	public void updateData(double motorTemp, double throttlePercent)
	{
		updatemotorTemp(motorTemp);
		updateThrottlePercent(throttlePercent);
	}
	
}
