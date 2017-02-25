package mastero.opto.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private final StringProperty userName;
	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty emailAddress;

	/**
	 * Constructor that sets only the username
	 */
	public User(String userName) {
		this.userName = new SimpleStringProperty(userName);
		firstName = new SimpleStringProperty(null);
		lastName = new SimpleStringProperty(null);
		emailAddress = new SimpleStringProperty(null);

	}

	/**
	 * Constructor that takes four arguments
	 * @param user  The given user name.
	 * @param first The given first name.
	 * @param last	The given last name.
	 * @param email	The given email.
	 */
	public User(String user, String first, String last, String email)
	{
		this.userName = new SimpleStringProperty(user);
		firstName = new SimpleStringProperty(first);
		lastName = new SimpleStringProperty(last);
		emailAddress = new SimpleStringProperty(email);
	}



	// Accessor methods

	public String getUserName()
	{
		return userName.get();
	}

	public String getFirstName()
	{
		return firstName.get();
	}

	public String getLastName()
	{
		return lastName.get();
	}

	public String getEmailAddress()
	{
		return emailAddress.get();
	}

	// Mutators

	public void setUserName(String name)
	{
		userName.set(name);
	}

	public void setFirstName(String name)
	{
		firstName.set(name);
	}

	public void setLastName(String name)
	{
		lastName.set(name);
	}

	public void setEmailAddress(String email)
	{
		emailAddress.set(email);
	}

	// Methods that return the property objects

	public StringProperty userNameProperty() {
        return userName;
    }

	public StringProperty firstNameProperty() {
        return firstName;
    }

	public StringProperty lastNameProperty() {
        return lastName;
    }

	public StringProperty emailAddressProperty(){
		return emailAddress;
	}

}
