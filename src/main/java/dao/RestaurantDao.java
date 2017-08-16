package dao;

import models.Restaurant;

import java.util.List;


    public interface RestaurantDao {

        //create -------------
        void add (Restaurant restaurant);

        //read -------------
        List<Restaurant> getAll();

        Restaurant findById(int id);


        //update -------------
          void update(int id, String name, int cuisineId);


        //delete -------------
        void deleteById(int id);

        void clearAllRestaurants();

    }

