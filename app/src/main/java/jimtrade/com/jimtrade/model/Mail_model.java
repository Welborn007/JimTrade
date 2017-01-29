package jimtrade.com.jimtrade.model;

/**
 * Created by welborn on 7/13/2015.
 */
public class Mail_model {
    private String name,subject,message,date,product,address,designation,email,telephone,contactperson;

    public Mail_model()
    {
    }

    public Mail_model(String name, String subject, String message, String date, String product, String address, String designation, String email, String telephone, String contactperson)
    {
        this.name=name;
        this.subject=subject;
        this.message=message;
        this.date=date;
        this.product=product;
        this.address=address;
        this.designation=designation;
        this.email=email;
        this.telephone=telephone;
        this.contactperson=contactperson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }
}
