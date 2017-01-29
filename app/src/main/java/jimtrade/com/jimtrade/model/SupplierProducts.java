package jimtrade.com.jimtrade.model;

/**
 * Created by welborn on 4/11/2015.
 */
public class SupplierProducts {
    private String ProductName, thumbnailUrl,Catname;
    private int ProductID;
    private int CategoryID;
    private String CategoryIndex,Count,Category_count,Supplier_product_count;
    private String SupplierName,SupplierID;
    private String Supp_addr,Supp_email,Supp_phone;

    public SupplierProducts()
    {}

    public SupplierProducts(String ProductName, String thumbnailUrl,String Catname, int ProductID, int CategoryID, String CategoryIndex,String Count,String Category_count,String Supplier_product_count,String SupplierName,String SupplierID,String Supp_addr,String Supp_email,String Supp_phone) {
        this.ProductName = ProductName;
        this.thumbnailUrl = thumbnailUrl;
        this.Catname = Catname;
        this.ProductID = ProductID;
        this.CategoryID = CategoryID;
        this.CategoryIndex = CategoryIndex;
        this.Count = Count;
        this.Category_count = Category_count;
        this.Supplier_product_count = Supplier_product_count;
        this.Supp_addr = Supp_addr;
        this.Supp_email = Supp_email;
        this.Supp_phone = Supp_phone;
        this.SupplierName = SupplierName;
        this.SupplierID = SupplierID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCatname(){return Catname; }

    public void setCatname(String Catname) { this.Catname = Catname; }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getCategoryIndex() { return CategoryIndex;}

    public void setCategoryIndex(String CategoryIndex) { this.CategoryIndex = CategoryIndex; }

    public String getCount() { return Count;}

    public void setCount(String Count) { this.Count = Count; }

    public String getCategory_count() { return Category_count;}

    public void setCategory_count(String Category_count) { this.Category_count = Category_count; }

    public String getSupplier_product_count() { return Supplier_product_count;}

    public void setSupplier_product_count(String Supplier_product_count) { this.Supplier_product_count = Supplier_product_count; }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String SupplierID) {
        this.SupplierID = SupplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String SupplierName) {
        this.SupplierName= SupplierName;
    }

    public String getSupp_addr() { return Supp_addr; }

    public void setSupp_addr(String Supp_addr) { this.Supp_addr = Supp_addr; }

    public String getSupp_email() { return Supp_email; }

    public void setSupp_email(String Supp_email) { this.Supp_email = Supp_email; }

    public String getSupp_phone() { return Supp_phone; }

    public void setSupp_phone(String Supp_phone) { this.Supp_phone = Supp_phone; }

}
