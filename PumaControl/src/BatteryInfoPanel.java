import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BatteryInfoPanel extends JPanel
{
	JLabel batteryVoltage;
	JLabel batteryCurrent;
	JLabel batteryPower;
	JLabel batteryLevel;
	JLabel batteryTemp;
	JLabel batteryPCBATemp;
	
	public BatteryInfoPanel()
	{
		super(new FlowLayout(FlowLayout.CENTER,20,10));
		setBorder(BorderFactory.createTitledBorder("Battery Info"));
		batteryVoltage = new JLabel("Batt Voltage:    V");
		batteryCurrent = new JLabel("Batt Current:    A");
		batteryPower = new JLabel("Batt Power:    W");
		batteryLevel = new JLabel("Batt Level:    %");
		batteryTemp = new JLabel("Batt Temp:    °C");
		batteryPCBATemp = new JLabel("Batt PCBA Temp:    °C");
		this.add(batteryVoltage);
		this.add(batteryCurrent);
		this.add(batteryPower);
		this.add(batteryLevel);
		this.add(batteryTemp);
		this.add(batteryPCBATemp);
		
		this.setVisible(true);
	}
	
	public void updateBatteryVoltage(double voltage)
	{
		batteryVoltage.setText("Batt Voltage: " + voltage+"V");
	}
	public void updateBatteryCurrent(double current)
	{
		batteryCurrent.setText("Batt Current: " + current+"A");
	}
	public void updateBatteryPower(double power)
	{
		batteryPower.setText("Batt Power: " + power+"W");
	}
	public void updateBatteryLevel(double level)
	{
		batteryLevel.setText("Batt Level: " + level+"%");
	}
	public void updateBatteryTemp(double temp)
	{
		batteryTemp.setText("Batt Temp: " + temp+"°C");
	}
	public void updateBatteryPCBATemp(double temp)
	{
		batteryPCBATemp.setText("Batt PCBA Temp: " + temp+"°C");
	}
	public void updateData(double voltage, double current, double power, double level, double temp, double pcbaTemp)
	{
		updateBatteryVoltage(voltage);
		updateBatteryCurrent(current);
		updateBatteryPower(power);
		updateBatteryLevel(level);
		updateBatteryTemp(temp);
		updateBatteryPCBATemp(pcbaTemp);
	}
}
