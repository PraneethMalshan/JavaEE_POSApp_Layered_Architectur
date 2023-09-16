package lk.ijse.pos.entity;

public class Customer {
    private String id;
    private String name;
    private String address;
    private double salary;

    public Customer(String id, String name, String address, String salary) {
    }

    public Customer(String id, String name, String address, double salary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalary() {
        return String.valueOf(salary);
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}

