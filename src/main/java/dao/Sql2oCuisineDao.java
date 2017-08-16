package dao;

import models.Cuisine;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;


public class Sql2oCuisineDao implements CuisineDao{
    private final Sql2o sql2o;

    public Sql2oCuisineDao(Sql2o sql2o){
        this.sql2o = sql2o;

    }
    @Override
    public void add(Cuisine cuisine) {
        String sql = "INSERT INTO cuisines (type) VALUES (:type)";
        try(Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .addParameter("type", cuisine.getType())
                    .addColumnMapping("TYPE", "type")
                    .executeUpdate()
                    .getKey();
            cuisine.setId(id);
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
    @Override
    public Cuisine findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM cuisines WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Cuisine.class);
        }

    }

}
