package com.example.project.common;
import com.example.project.model.User;
public class common {
   public static String convertCodeToStatus(String status) {
      if (status.equals("0"))
         return "Placed";
      else if (status.equals("1"))
         return "On my way";
      else
         return "Shipped";
   }
   public static User currentUser;
   public  static  final String INTENT_FOOD_ID = "FoodID";
   public static final String UPDATE="UPDATE";
   public static final String DELETE="DELETE";
}
