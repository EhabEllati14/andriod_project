package model;

public class Ticket {
    // Fields for ViewTicketsAdapter
    private int ticketId;
    private String serviceName;
    private String submittedDate;
    private String customerName;
    private String emergencyLevel;
    private String status;

    // Fields for com.example.car_maintenance_project.login.CompletedTicketsAdapter
    private String employeeName;
    private String completedDate;

    // Constructor for com.example.car_maintenance_project.login.CompletedTicketsAdapter
    public Ticket(String serviceName, String employeeName, String completedDate) {
        this.serviceName = serviceName;
        this.employeeName = employeeName;
        this.completedDate = completedDate;
    }

    // Constructor for ViewTicketsAdapter
    public Ticket(int ticketId, String serviceName, String submittedDate, String customerName, String emergencyLevel, String status) {
        this.ticketId = ticketId;
        this.serviceName = serviceName;
        this.submittedDate = submittedDate;
        this.customerName = customerName;
        this.emergencyLevel = emergencyLevel;
        this.status = status;
    }

    // Getters and setters for ViewTicketsAdapter fields
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters and setters for com.example.car_maintenance_project.login.CompletedTicketsAdapter fields
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }
}