package dao;


import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    public Restaurant setupNewRestaurant() { return new Restaurant("Olive Garden");}

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

}