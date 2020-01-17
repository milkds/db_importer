package importer;

import importer.dao.CarDAO;
import importer.dao.ItemDAO;
import importer.entities.*;
import importer.suppliers.fox.FoxHibernateUtil;
import importer.suppliers.fox.dao.FoxCarDAO;
import importer.suppliers.fox.entities.FoxCar;
import importer.suppliers.skyjacker.SkyConverter;
import importer.suppliers.skyjacker.SkyDAO;
import importer.suppliers.skyjacker.SkyHibernateUtil;
import importer.suppliers.skyjacker.sky_entities.SkyFitment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class TestClass {

    public static void TestItemSave(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            ProductionItem item = getNewItem();
            ItemDAO.saveItem(item, session);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        HibernateUtil.shutdown();
    }

    private static ProductionItem getNewItem() {
        ProductionItem item  = new ProductionItem();
        item.setItemType("Shock");
        item.setItemManufacturer("Bilstein");
        item.setItemPartNo("24-777777");

        ItemAttribute itemAtt = new ItemAttribute();
        itemAtt.setItemAttName("Color");
        itemAtt.setItemAttValue("Yellow");
        item.getItemAttributes().add(itemAtt);

        ProductionFitment fitment = new ProductionFitment();
        FitmentAttribute fitAtt = new FitmentAttribute();
        fitAtt.setFitmentAttName("Position");
        fitAtt.setFitmentAttValue("Forward");
        fitment.getFitmentAttributes().add(fitAtt);

        ProductionCar car = new ProductionCar();
        car.setYearStart(2015);
        car.setYearFinish(2019);
        car.setMake("Ford");
        car.setModel("F-150");
        car.setSubModel("Laramie");
        car.setDrive("4WD");

        CarAttribute carAttribute = new CarAttribute();
        carAttribute.setCarAttName("Engine");
        carAttribute.setCarAttValue("4.0L");
        car.getAttributes().add(carAttribute);

        fitment.setCar(car);
        item.getProductionFitments().add(fitment);

        return item;
    }

    public static void testSplit(){
        String spl = "https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_1.jpg\n" +
                " https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_2.jpg\n" +
                " https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_3.jpg\n" +
                " https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_4.jpg";
        String split[] = spl.split("\n");
        System.out.println(split.length);
        System.out.println(split[0].length());
    }

    public static void testFoxProdCarMerge(){
        Set<FoxCar> allCars = FoxCarDAO.getAllCars();
         allCars.forEach(foxCar->{
           ProductionCar testProdCar = new ProductionCar();
           String drive = foxCar.getDrive();
           testProdCar.setMake(foxCar.getMake());
           testProdCar.setModel(foxCar.getModel().trim());
           testProdCar.setDrive(foxCar.getDrive());
           List<ProductionCar> similarCars = CarDAO.getSimilarCarsByMMY(testProdCar, foxCar.getYear());
           if (similarCars.size()==0){
               System.out.println(foxCar.getYear() + " " + foxCar.getMake() + " " + foxCar.getModel());
           }
           /*boolean carExists = false;
           for (ProductionCar simCar: similarCars){
               String simCarDrive = simCar.getDrive();
               if (simCarDrive!=null){
                   if (simCarDrive.equals(drive)){
                       carExists = true;
                       break;
                   }
               }
           }
           if (!carExists){
               System.out.println(foxCar);
           }*/
       });
         HibernateUtil.shutdown();
        FoxHibernateUtil.shutdown();
    }

    public static void testSkyBilCarMerge(){
        Session skySession = SkyHibernateUtil.getSession();
        Set<String> allSkyFitLines = SkyDAO.getAllFitLines(skySession);
        for (String fitLine: allSkyFitLines){
            ProductionCar car = new SkyConverter().buildProductionCar(fitLine, skySession);
            int year = Integer.parseInt(fitLine.split(" ")[0]);
            List<ProductionCar> similarCars = CarDAO.getSimilarCarsByMMY(car, year );
            if (similarCars.size()==0){
                //System.out.println(year + " " + car.getMake() + " " + car.getModel());
                System.out.println(car.getModel());
            }
        }
        skySession.close();
        HibernateUtil.shutdown();
        SkyHibernateUtil.shutdown();
    }
}
