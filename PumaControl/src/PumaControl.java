

public class PumaControl {

	public static void main(String[] args) 
	{
		PumaController pumaController = new PumaController();
		PumaInterface pumaInterface = new PumaInterface(pumaController);
		

		while(true)
		{
			pumaInterface.doControl();
		}
		
	}

}
