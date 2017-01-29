package jimtrade.com.jimtrade.model;


public class Category {

    private String category_name,category_id,category_index,product_count,category_count;
    
    public Category()
    {}

    public Category(String category_name,String category_id,String category_index,String product_count,String category_count)
    {
        this.category_name = category_name;
        this.category_id = category_id;
        this.category_index = category_index;
        this.product_count = product_count;
        this.category_count = category_count;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name= category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_index() {
        return category_index;
    }

    public void setCategory_index(String category_index) {
        this.category_index = category_index;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public String getCategory_count() {
        return category_count;
    }

    public void setCategory_count(String category_count) {
        this.category_count = category_count;
    }
}
