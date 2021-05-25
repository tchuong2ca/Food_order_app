//package com.example.project.model;
//
//import java.util.List;
//
//public class Address {
//    private String Name, Phone, Address, total, orderstt;
//    private List<Order> foods;
//
//
//    public Address(){}
//
//    public Address(String name, String phone, String address, String total, List<Order> foods) {
//        Name = name;
//        Phone = phone;
//        Address = address;
//        this.total = total;
//        this.foods = foods;
//        this.orderstt = "0";//đã đặt:0 - đang ship:1 - đã giao thành công: 2
//    }
//
//    public String getOrderstt() {
//        return orderstt;
//    }
//
//    public void setOrderstt(String orderstt) {
//        this.orderstt = orderstt;
//    }
//
//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public String getPhone() {
//        return Phone;
//    }
//
//    public void setPhone(String phone) {
//        Phone = phone;
//    }
//
//    public String getAddress() {
//        return Address;
//    }
//
//    public void setAddress(String address) {
//        Address = address;
//    }
//
//    public String getTotal() {
//        return total;
//    }
//
//    public void setTotal(String total) {
//        this.total = total;
//    }
//
//    public List<Order> getFoods() {
//        return foods;
//    }
//
//    public void setFoods(List<Order> foods) {
//        this.foods = foods;
//    }
//}







package com.example.project.model;

import java.util.List;

public class Address {
    private String Name, Phone, Address, total, Order_status, Note;
    private List<Order> foods;
    public Address(){}
    public Address(String name, String phone, String address, String total, String order_status, String note, List<Order> foods) {
        Name = name;
        Phone = phone;
        Address = address;
        this.total = total;
        this.Order_status = order_status;
        this.Note = note;
        this.foods = foods;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrder_status() {
        return Order_status;
    }

    public void setOrder_status(String order_status) {
        this.Order_status = order_status;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        this.Note = note;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}

