import Packages.Chihab.Models.Category;
import Packages.Chihab.Services.AssociationService;
import Packages.Chihab.Services.CategoryService;

public class CRUDTester{
    public static void main(String[] args) {
        System.out.println(AssociationService.getInstace().read());
    }
}
