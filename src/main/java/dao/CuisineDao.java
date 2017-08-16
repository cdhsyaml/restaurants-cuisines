package dao;

import models.Cuisine;
import models.Restaurant;
import java.util.List;


public interface CuisineDao {

    //create -------------
    void add (Cuisine cuisine);
    List<Restaurant> getAllRestaurantsByCuisine(int cuisineId);

    //read -------------
    List<Cuisine> getAll();
//
    Cuisine findById(int id);
//
//
    //update -------------
    void update(int id, String type);
//
//
    //delete -------------
    void deleteCuisineById(int id);
//
    void clearAllCuisines();

}
