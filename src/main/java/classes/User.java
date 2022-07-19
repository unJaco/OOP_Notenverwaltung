package classes;

public abstract class User {
    private Integer id;
    private String firstname;
    private String lastname;

    private Role role;

    public User(Integer id, String firstname, String lastname, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return String return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return String return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return String return the email
     */
    /*public String getEmail() {
        return email;
    }

    /**
     * @param email the lastname to set
     */
    /*public void setEmail(String email) {
        this.email = email;
    }*/
    public Role getRole() {
        return role;
    }

    /**
     * @param role the lastname to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role=" + role +
                '}';
    }
}

