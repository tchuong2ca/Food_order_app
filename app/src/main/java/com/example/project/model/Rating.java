package com.example.project.model;
//
public class Rating {
    private String phone,foodId,value,comment;
    public Rating(){}
        public Rating(String phone, String foodId, String value, String comment) {
        this.phone = phone;
        this.foodId = foodId;
        this.value = value;
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
