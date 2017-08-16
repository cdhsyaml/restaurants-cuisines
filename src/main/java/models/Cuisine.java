package models;

/**
 * Created by Guest on 8/16/17.
 */
public class Cuisine {
    private String type;
    private int id;

    public Cuisine(String type) {
        this.type = type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cuisine cuisine = (Cuisine) o;

        if (id != cuisine.id) return false;
        return type.equals(cuisine.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + id;
        return result;
    }

}
