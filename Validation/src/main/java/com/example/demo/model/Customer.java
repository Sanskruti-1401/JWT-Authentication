package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
@Entity
@Table(name = "customers")
public class Customer {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
@NotNull
@Size(min = 3, message = "Customer name must have at least 3 characters")
private String customerName;
@Email(message = "Please enter valid email")
@NotBlank(message = "Email cannot be blank")
private String email;
@NotBlank(message = "Address cannot be blank")
private String address;
@NotNull
@Size(min = 10, max = 10, message = "Phone number must be 10 digits")
private String phoneNumber;
public Customer() {
}
public Long getId() {
return id;
}
public void setId(Long id) {

this.id = id;
}
public String getCustomerName() {
return customerName;
}
public void setCustomerName(String customerName) {
this.customerName = customerName;
}
public String getEmail() {
return email;
}
public void setEmail(String email) {
this.email = email;
}
public String getAddress() {
return address;
}
public void setAddress(String address) {
this.address = address;
}
public String getPhoneNumber() {
return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
this.phoneNumber = phoneNumber;
}
}
