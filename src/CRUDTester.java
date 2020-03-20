import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.CategoryService;

public class CRUDTester{
    public static void main(String[] args) {
        Category c = new Category();
        c.setId(77);
        CategoryService.getInstace().delete(c);
    }
}
