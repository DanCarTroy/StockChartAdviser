package mastero.opto.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty emailAddress;

	/**
	 * Default Constructor. Has to be explicitly set because other constructors are defined.
	 */
	public User() {
		this(null, null, null);
	}

	/**
	 * Constructor that takes first name, last name and email
	 * @param first The given first name.
	 * @param last	The given last name.
	 * @param email	The given email.
	 */
	public User(String first, String last, String email)
	{
		firstName = new SimpleStringProperty(first);
		lastName = new SimpleStringProperty(last);
		emailAddress = new SimpleStringProperty(email);
	}



	// Accessor methods

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
