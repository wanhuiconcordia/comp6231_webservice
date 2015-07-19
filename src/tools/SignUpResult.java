package tools;
import java.io.Serializable;

/**
 *  If result is true, the customerReferenceNumber and password are generated properly on server
 * Otherwise failed to register. 
 * @author comp6231.team5
 *
 */
public class SignUpResult implements Serializable{
	private static final long serialVersionUID = 8371860298743889873L;
	public boolean result;
	public int customerReferenceNumber;
	public String message;	//Server will give a message about the sign up status
	
	/**
	 * constructor
	 * @param result
	 * @param customerReferenceNumber
	 * @param message
	 */
	public SignUpResult(boolean result, int customerReferenceNumber, String message){
		this.result = result;
		this.customerReferenceNumber = customerReferenceNumber;
		this.message = message;
	}
}
