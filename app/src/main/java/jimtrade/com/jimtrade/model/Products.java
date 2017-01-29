package jimtrade.com.jimtrade.model;

public class Products {
    private String ProductName, thumbnailUrl;
    private String SupplierName,CategoryName;
    private int ProductID;
    private int SupplierID;
    private int CategoryID;
    private String CategoryIndex,Count,Category_Count;
    private String Features;
    private String Supp_addr,Supp_email,Supp_phone;

    public Products() {
    }

    public Products(String ProductName, String thumbnailUrl, String SupplierName, int ProductID, int SupplierID, int CategoryID, String CategoryName, String CategoryIndex, String Count, String Features, String Category_count, String Supp_addr, String Supp_email, String Supp_phone) {
        this.ProductName = ProductName;
        this.thumbnailUrl = thumbnailUrl;
        this.SupplierName = SupplierName;
        this.ProductID = ProductID;
        this.SupplierID = SupplierID;
        this.CategoryID = CategoryID;
        this.CategoryName = CategoryName;
        this.CategoryIndex = CategoryIndex;
        this.Count = Count;
        this.Features = Features;
        this.Category_Count = Category_count;
        this.Supp_addr = Supp_addr;
        this.Supp_email = Supp_email;
        this.Supp_phone = Supp_phone;
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

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String SupplierName) {
        this.SupplierName= SupplierName;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int SupplierID) {
        this.SupplierID = SupplierID;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getCategoryIndex() {
        return CategoryIndex;
    }

    public void setCategoryIndex(String CategoryIndex) {
        this.CategoryIndex = CategoryIndex;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }

    public String getFeatures() {
        return Features;
    }

    public void setFeatures(String Features) {
        this.Features = Features;
    }

    public String getCategory_Count() {
        return Category_Count;
    }

    public void setCategory_Count(String Category_count) {
        this.Category_Count = Category_count;
    }

    public String getSupp_addr() { return Supp_addr; }

    public void setSupp_addr(String Supp_addr) { this.Supp_addr = Supp_addr; }

    public String getSupp_email() { return Supp_email; }

    public void setSupp_email(String Supp_email) { this.Supp_email = Supp_email; }

    public String getSupp_phone() { return Supp_phone; }

    public void setSupp_phone(String Supp_phone) { this.Supp_phone = Supp_phone; }

}
