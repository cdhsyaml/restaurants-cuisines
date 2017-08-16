package dao;

import models.Cuisine;

import models.Cuisine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class Sql2oCuisineDaoTest {
    private Sql2oCuisineDao cuisineDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        cuisineDao = new Sql2oCuisineDao(sql2o); //ignore me for now
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


    public Cuisine setupNewCuisine() { return new Cuisine("Thai");}
    public Cuisine setupNewCuisine2() { return new Cuisine("American");}
}
