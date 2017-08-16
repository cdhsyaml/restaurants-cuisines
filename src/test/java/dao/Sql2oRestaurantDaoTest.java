package dao;


import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class Sql2oRestaurantDaoTest {

    private Sql2oRestaurantDao restaurantDao; //ignore me for now. We'll create this soon.
    private Connection conn; //must be sql2o class conn

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o); //ignore me for now

        //keep connection open through entire test so it does not get erased.
        conn = sql2o.open();
    }
    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Restaurant setupNewRestaurant() { return new Restaurant("Olive Garden",  1);}
    public Restaurant setupNewRestaurant2() { return new Restaurant("Chili's", 2);}

    @Test
    public void addingCourseSetsId() throws Exception {
       Restaurant restaurant = setupNewRestaurant();
        int originalRestaurantId = restaurant.getId();
        restaurantDao.add(restaurant);
        assertNotEquals(originalRestaurantId, restaurant.getId()); //how does this work?
    }


    @Test
    public void existingRestaurantCanBeFoundById() throws Exception {
        Restaurant restaurant = setupNewRestaurant();
        restaurantDao.add(restaurant);
        Restaurant foundRestaurant = restaurantDao.findById(restaurant.getId());
        assertEquals(restaurant, foundRestaurant);
    }

    @Test
    public void addedRestaurantsAreReturnedFromgetAll() throws Exception {
        Restaurant restaurant = setupNewRestaurant();
        restaurantDao.add(restaurant);
        assertEquals(1, restaurantDao.getAll().size());
    }

    @Test
    public void noRestaurantsReturnsEmptyList() throws Exception {
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void updateChangesRestaurantContent() throws Exception {
        String initialName = "Olive Garden";
        Restaurant restaurant = new Restaurant(initialName, 1);
        restaurantDao.add(restaurant);

        restaurantDao.update(restaurant.getId(), "Outback Steakhouse", 1);
        Restaurant updatedRestaurant = restaurantDao.findById(restaurant.getId());
        assertNotEquals(initialName, updatedRestaurant.getName());
    }

    @Test
    public void deleteByIdDeletesCorrectRestaurant() throws Exception {
        Restaurant restaurant = setupNewRestaurant();
       restaurantDao.add(restaurant);
       restaurantDao.deleteById(restaurant.getId());
       assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Restaurant restaurant = setupNewRestaurant();
        Restaurant restaurant2 = setupNewRestaurant2();
        restaurantDao.add(restaurant);
        restaurantDao.add(restaurant2);
        int daoSize = restaurantDao.getAll().size();
        restaurantDao.clearAllRestaurants();
        assertTrue(daoSize > 0 && daoSize > restaurantDao.getAll().size());
    }
    @Test
    public void categoryIdIsReturnedCorrectly() throws Exception {
        Restaurant restaurant = setupNewRestaurant();
        int originalCuisineId = restaurant.getCuisineId();
        restaurantDao.add(restaurant);
        assertEquals(originalCuisineId, restaurantDao.findById(restaurant.getId()).getCuisineId());
    }

}