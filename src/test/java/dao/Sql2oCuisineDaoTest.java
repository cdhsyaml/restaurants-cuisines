package dao;

import models.Cuisine;
import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class Sql2oCuisineDaoTest {
    private Sql2oCuisineDao cuisineDao;
    private Connection conn;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        cuisineDao = new Sql2oCuisineDao(sql2o); //ignore me for now
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        //keep connection open through entire test so it does not get erased.
        conn = sql2o.open();
    }


    @After
    public void tearDown() throws Exception {
        conn.close();
    }


    @Test
    public void addingCourseSetsId() throws Exception {
        Cuisine cuisine = setupNewCuisine();
        int originalCuisineId = cuisine.getId();
        cuisineDao.add(cuisine);
        assertNotEquals(originalCuisineId, cuisine.getId()); //how does this work?
    }

    @Test
    public void existingCuisineCanBeFoundById() throws Exception {
        Cuisine cuisine = setupNewCuisine();
        cuisineDao.add(cuisine);
        Cuisine foundCuisine = cuisineDao.findById(cuisine.getId());
        assertEquals(cuisine, foundCuisine);
    }

    @Test
    public void addedCuisinesAreReturnedFromGetAll() throws Exception {
        Cuisine cuisine = setupNewCuisine();
        cuisineDao.add(cuisine);
        assertEquals(1, cuisineDao.getAll().size());
    }

    @Test
    public void noCuisinesReturnsEmptyList() throws Exception {
        assertEquals(0, cuisineDao.getAll().size());
    }
    @Test
    public void updateChangesCuisineContent() throws Exception {
        String initialType = "Thai";
        Cuisine cuisine = new Cuisine(initialType);
        cuisineDao.add(cuisine);

        cuisineDao.update(cuisine.getId(), "American");
        Cuisine updatedCuisine = cuisineDao.findById(cuisine.getId());
        assertNotEquals(initialType, updatedCuisine.getType());
    }
    @Test
    public void deleteByIdDeletesCorrectCuisine() throws Exception {
        Cuisine cuisine = setupNewCuisine();
        cuisineDao.add(cuisine);
        cuisineDao.deleteCuisineById(cuisine.getId());
        assertEquals(0, cuisineDao.getAll().size());
    }

    @Test
    public void clearAllClearsAll() throws Exception {
        Cuisine cuisine = setupNewCuisine();
        Cuisine cuisine2 = setupNewCuisine2();
        cuisineDao.add(cuisine);
        cuisineDao.add(cuisine2);
        int daoSize = cuisineDao.getAll().size();
        cuisineDao.clearAllCuisines();
        assertTrue(daoSize > 0 && daoSize > cuisineDao.getAll().size());
    }
    @Test
    public void getAllRestaurantsByCuisineReturnsRestaurantsCorrectly() throws Exception {
        Cuisine cuisine = new Cuisine ("American");
        cuisineDao.add(cuisine);
        int cuisineId = cuisine.getId();
        Restaurant newRestaurant = new Restaurant("Olive Garden", cuisineId);
        Restaurant newRestaurant2 = new Restaurant("Chili's", cuisineId);
        Restaurant newRestaurant3 = new Restaurant("Sizzlers", cuisineId);
        restaurantDao.add(newRestaurant);
        restaurantDao.add(newRestaurant2); //we are not adding task 3 so we can test things precisely.


        assertTrue(cuisineDao.getAllRestaurantsByCuisine(cuisineId).size() == 2);
        assertTrue(cuisineDao.getAllRestaurantsByCuisine(cuisineId).contains(newRestaurant));
        assertTrue(cuisineDao.getAllRestaurantsByCuisine(cuisineId).contains(newRestaurant2));
        assertFalse(cuisineDao.getAllRestaurantsByCuisine(cuisineId).contains(newRestaurant3)); //things are accurate!
    }
    public Cuisine setupNewCuisine() { return new Cuisine("Thai");}
    public Cuisine setupNewCuisine2() { return new Cuisine("American");}
}
