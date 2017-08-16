import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.Sql2oCuisineDao;
import dao.Sql2oRestaurantDao;
import models.Cuisine;
import org.sql2o.Sql2o;
import models.Restaurant;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
    public static void main(String[] args) { //type “psvm + tab” to autocreate this
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/todolist.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oRestaurantDao restaurantDao = new Sql2oRestaurantDao(sql2o);
        Sql2oCuisineDao cuisineDao = new Sql2oCuisineDao(sql2o);

//show new cuisine form
        get("/cuisines/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Cuisine> cuisines = cuisineDao.getAll(); //refresh list of links for navbar.
            model.put("cuisines", cuisines);
            return new ModelAndView(model, "cuisine-form.hbs"); //new
        }, new HandlebarsTemplateEngine());

//post: process new cuisine form
        post("/cuisines", (request, response) -> { //new
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            Cuisine newCuisine = new Cuisine(name);
            cuisineDao.add(newCuisine);

            List<Cuisine> cuisines = cuisineDao.getAll(); //refresh list of links for navbar.
            model.put("cuisines", cuisines);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

//get: show an individual cuisine and restaurants it contains
        get("/cuisines/:cuisineId", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfCuisineToFind = Integer.parseInt(req.params("cuisineId")); //new

            List<Cuisine> cuisines = cuisineDao.getAll(); //refresh list of links for navbar.
            model.put("cuisines", cuisines);

            Cuisine foundCuisine = cuisineDao.findById(idOfCuisineToFind);
            model.put("cuisine", foundCuisine);
            List<Restaurant> allRestaurantsByCuisine = cuisineDao.getAllRestaurantsByCuisine(idOfCuisineToFind);
            model.put("restaurants", allRestaurantsByCuisine);

            return new ModelAndView(model, "cuisine-detail.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //get: show all restaurants
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Cuisine> allCuisines = cuisineDao.getAll();
            model.put("cuisines", allCuisines);

            List<Restaurant> restaurants = restaurantDao.getAll();
            model.put("restaurants", restaurants);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());//get: delete all restaurants

        get("/restaurants/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            restaurantDao.clearAllRestaurants();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show new restaurant form
        get("/restaurants/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Cuisine> allCuisines = cuisineDao.getAll();
            model.put("cuisines", allCuisines);
            return new ModelAndView(model, "restaurant-form.hbs");
        }, new HandlebarsTemplateEngine());

        //task: process new restaurant form
        post("/restaurants/new", (request, response) -> { //URL to make new task on POST route
            Map<String, Object> model = new HashMap<>();
            List<Cuisine> allCuisines = cuisineDao.getAll();
            model.put("cuisines", allCuisines);

            String name = request.queryParams("name");
            int cuisineId = Integer.parseInt(request.queryParams("cuisineId"));
            Restaurant newRestaurant = new Restaurant(name, cuisineId);
            restaurantDao.add(newRestaurant);
            model.put("restaurant", newRestaurant);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

//get: show a form to update a cuisine
        get("/cuisines/:cuisine_id/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int thisId = Integer.parseInt(req.params("cuisine_id"));
            model.put("editCuisine", true);
            List<Cuisine> allCuisines = cuisineDao.getAll();
            model.put("cuisines", allCuisines);
            return new ModelAndView(model, "cuisine-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process a form to update a cuisine and restaurants it contains
        post("/cuisines/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfCuisineToEdit = Integer.parseInt(req.queryParams("editCuisineId"));
            String newName = req.queryParams("newCuisineName");
            cuisineDao.update(cuisineDao.findById(idOfCuisineToEdit).getId(), newName);
            List<Cuisine> allCuisines = cuisineDao.getAll(); //refresh list of links for navbar.
            model.put("cuisines", allCuisines);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete an individual cuisine
        get("cuisines/:cuisine_id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfCuisineToDelete = Integer.parseInt(req.params("cuisine_id")); //pull id - must match route segment
            Cuisine deleteCuisine = cuisineDao.findById(idOfCuisineToDelete); //use it to find task
            cuisineDao.deleteCuisineById(idOfCuisineToDelete);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all cuisines and all restaurants
        get("/cuisines/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            restaurantDao.clearAllRestaurants();
            cuisineDao.clearAllCuisines();

            List<Cuisine> allCuisines = cuisineDao.getAll();
            model.put("cuisines", allCuisines);

            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show an individual restaurant
        get("/cuisines/:cuisine_id/restaurants/:restaurant_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfRestaurantToFind = Integer.parseInt(req.params("restaurant_id")); //pull id - must match route segment
            Restaurant foundRestaurant = restaurantDao.findById(idOfRestaurantToFind); //use it to find task
            model.put("restaurant", foundRestaurant); //add it to model for template to display
            return new ModelAndView(model, "restaurant-detail.hbs"); //individual task page.
        }, new HandlebarsTemplateEngine());

        //get: show a form to update a restaurant
        get("/cuisines/:cuisine_id/restaurants/:restaurant_id/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfRestaurantToEdit = Integer.parseInt(req.params("restaurant_id"));
            Restaurant editRestaurant = restaurantDao.findById(idOfRestaurantToEdit);
            model.put("editRestaurant", editRestaurant);
            List<Restaurant>allRestaurants = restaurantDao.getAll();//add all restaurants to model
            List<Cuisine> allCuisines = cuisineDao.getAll();
            model.put("restaurants", allRestaurants);
            model.put("cuisines", allCuisines);
            //add all cuisines to model
            return new ModelAndView(model, "restaurant-form.hbs");
        }, new HandlebarsTemplateEngine());

        //task: process a form to update a restaurant
        post("/cuisines/:cuisine_id/restaurants/:id/update", (req, res) -> { //URL to make new task on POST route
            Map<String, Object> model = new HashMap<>();
            String newName = req.queryParams("name");
            int idOfRestaurantToEdit = Integer.parseInt(req.params("id"));
            int idCuisineOfRestaurantToEdit = Integer.parseInt(req.params("cuisine_id"));
            Restaurant editRestaurant = restaurantDao.findById(idOfRestaurantToEdit);
            restaurantDao.update(idOfRestaurantToEdit,newName, idCuisineOfRestaurantToEdit);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete an individual restaurant
        get("/cuisines/:cuisine_id/restaurants/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfRestaurantToDelete = Integer.parseInt(req.params("id")); //pull id - must match route segment
            Restaurant deleteRestaurant = restaurantDao.findById(idOfRestaurantToDelete); //use it to find task
            restaurantDao.deleteById(idOfRestaurantToDelete);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //CURRENTLY WE CANNOT POST OR UPDATE - get/post CHECK THEM !!!!! //



    }
}
