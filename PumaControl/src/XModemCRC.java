
public class XModemCRC
{
    private static int crc = 0x0000;          // initial value
    private static int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12) 
    
    public XModemCRC()
	{
		
	}
    
   static String calculateCRC(byte[] bytes)
    {
	   crc = 0x0000;
        for (byte b : bytes) 
        {
            for (int i = 0; i < 8; i++) 
            {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                {
                	crc ^= polynomial;
                }	
            }
		
    	
        }
        crc &= 0xffff;
        return String.format("%04X", crc );
    }
}
