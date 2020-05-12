package importer.suppliers.skyjacker;

import importer.entities.FitmentAttribute;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class LiftBuilder {
    private static final Logger logger = LogManager.getLogger(LiftBuilder.class.getName());
    private Double rear_lift_start = null;
    private Double rear_lift_finish = null;
    private Double front_lift_start = null;
    private Double front_lift_finish = null;

    private boolean isFront = false;
    private boolean isRear = false;

    private void processAttribute(FitmentAttribute att) {
        boolean curPositionRear = false;
        String value = att.getFitmentAttValue();
        if (value.contains("Rear")){
            curPositionRear = true;
        }
        else if (!value.contains("Front")){
            return;
        }
        double[] curLift = getLift(value);
        checkLiftSize(curLift[0], curPositionRear);
        if (curLift.length==2){
            checkLiftSize(curLift[1], curPositionRear);
        }
    }

    private void checkLiftSize(double curLift, boolean curPositionRear) {
        if (curPositionRear){
            isRear = true;
            //start and finish always null together and always get their values together
            if (rear_lift_start==null){
                rear_lift_start = curLift;
                rear_lift_finish = curLift;
            }
            else {
                if (rear_lift_start>curLift){
                    rear_lift_start = curLift;
                }
                if (rear_lift_finish<curLift){
                    rear_lift_finish = curLift;
                }
            }
        }
        else {
            isFront = true;
            if (front_lift_start==null){
                front_lift_start = curLift;
                front_lift_finish = curLift;
            }
            else {
                if (front_lift_start>curLift){
                    front_lift_start = curLift;
                }
                if (front_lift_finish<curLift){
                    front_lift_finish = curLift;
                }
            }
        }
    }

    private double[] getLift(String value) {
        double[] result;
        String lift = StringUtils.substringBetween(value, "with ", " in.");
        if (lift.contains("-")){
            String[] split = lift.split("-");
            result = new double[2];
            try {
                result[0] = Double.parseDouble(split[0]);
                result[1] = Double.parseDouble(split[1]);
                return result;
            }
            catch (NumberFormatException e){
                logger.error("Wrong lift attribute: " + value);
                System.exit(1);
            }
        }
        double res = 0d;
        try {
            res = Double.parseDouble(lift);
        }
        catch (NumberFormatException e){
            logger.error("Wrong lift attribute: " + value);
            System.exit(1);
        }
        result = new double[1];
        result[0] = res;

        return result;
    }

    public boolean buildLifts(Set<FitmentAttribute> attributes) {
        if (attributes.size()==0){
            return false;
        }
       attributes.forEach(this::processAttribute);
        if (isFront){
            attributes.add(new FitmentAttribute("Position", "Front"));
            attributes.add(new FitmentAttribute("f_Lift_s", front_lift_start+""));
            attributes.add(new FitmentAttribute("f_Lift_f", front_lift_finish+""));
        }
        if (isRear){
            attributes.add(new FitmentAttribute("Position", "Rear"));
            attributes.add(new FitmentAttribute("r_Lift_s", rear_lift_start+""));
            attributes.add(new FitmentAttribute("r_Lift_f", rear_lift_finish+""));
        }

        return isFront&&isRear;
    }
}
