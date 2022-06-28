package importer.suppliers.summit;

import importer.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AppNoteManualBreaker {
    private static final Logger logger = LogManager.getLogger(AppNoteManualBreaker.class.getName());

    private ProductionCar car;
    private ProductionFitment fit;
    private ProductionItem prodItem;
    private String appNote;
    private Map<String, Map<String, String>> appMap;

    public AppNoteManualBreaker(ProductionCar car, ProductionFitment fit, ProductionItem prodItem, String appNote) {
        this.car = car;
        this.fit = fit;
        this.prodItem = prodItem;
        this.appNote = appNote;
        appMap = initMap();
    }

    /**
     * k = application note to break
     * v = map, where K is note to use and V is marker to get where to use
     * @return
     */
    private Map<String, Map<String, String>> initMap() {
        Map<String, Map<String, String>> result = new HashMap<>();
        Map<String, String> currentMap = new HashMap<>();
        currentMap.put("Rear Air Springs", "i");
        currentMap.put("Air hose kit included", "i");
        result.put("Rear Air Springs Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Straight Front Axle", "f");
        currentMap.put("Air hose kit included", "i");
        result.put("Exc. HD Straight Front Axle Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Air Springs","f");
        currentMap.put("Air hose kit included","i");
        result.put("Rear Air Springs Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Up through 8500# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Up through 8500# GVW Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Auto Leveling System","f");
        currentMap.put("Strut Mount","i");
        result.put("Auto Leveling System Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("RWD","DRIVE");
        currentMap.put("Supercab","f");
        result.put("RWD Supercab", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Straight Front Axle","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. HD Straight Front Axle Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 8/73","f");
        currentMap.put("Air hose kit included","i");
        result.put("Mfg. from 8/73 Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("RWD","DRIVE");
        currentMap.put("air hose kit included","i");
        result.put("RWD air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mojave","f");
        currentMap.put("Air hose kit included","i");
        result.put("Mojave Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("4WD","DRIVE");
        currentMap.put("w/ Torsion Bar","f");
        result.put("4WD w/ Torsion Bar", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Mount w/ Retainer","i");
        result.put("Exc. Electronic Adjustable Suspension Mount w/ Retainer", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Electronic Adjustable Suspension Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 5/96","f");
        currentMap.put("Mount w/ Retainer","i");
        result.put("Mfg. to 5/96 Mount w/ Retainer", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 5/96","f");
        currentMap.put("Strut Mount","i");
        result.put("Mfg. to 5/96 Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Auto-Leveling System","f");
        currentMap.put("Mount w/ Retainer","i");
        result.put("Exc. Auto-Leveling System Mount w/ Retainer", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Auto-Leveling System","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Auto-Leveling System Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Coil(RearSpringType)","f");
        currentMap.put("Air hose kit included","i");
        result.put("Coil(RearSpringType) Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Ride Control","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Electronic Ride Control air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Solid Rear Axle Product","f");
        currentMap.put("is designed to match original equipment performance level","i");
        currentMap.put("B14","i");
        result.put("B14 Solid Rear Axle Product is designed to match original equipment performance level", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Package","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. HD Package Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Heavy Duty Package","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Heavy Duty Package Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Wagon","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Wagon Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 6/05","f");
        currentMap.put("w/ 7 Passenger Seating","f");
        result.put("Mfg. to 6/05 w/ 7 Passenger Seating", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 6/05","f");
        currentMap.put("w/7 Passenger Seating","f");
        result.put("Mfg to 06/05 w/7 Passenger Seating", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mount Kit","i");
        currentMap.put("w/ Mount & Retainers","i");
        result.put("Mount Kit w/ Mount & Retainers", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad Suspension","f");
        currentMap.put("Long Shock","f");
        result.put("Quad Suspension Long Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad Suspension","f");
        currentMap.put("Long Shock","f");
        currentMap.put("Full Size","f");
        result.put("Full Size Quad Suspension Long Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Kit w/ Mount and Bearing","i");
        result.put("Exc. Electronic Adjustable Suspension Kit w/ Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Adaptive Variable Suspension","f");
        currentMap.put("Kit w/ Mount and Bearing","i");
        result.put("Exc. Adaptive Variable Suspension Kit w/ Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Adaptive Variable Suspension","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Adaptive Variable Suspension Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Thru Serial #830000","f");
        currentMap.put("Air hose kit included","i");
        result.put("Thru Serial #830000 Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Extended Cab & Chassis Cab","f");
        currentMap.put("air hose kit included","i");
        result.put("Exc. Extended Cab & Chassis Cab air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Adjustable Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Adjustable Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Electronic Adjustable Suspension air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hybrid & Si Models","f");
        currentMap.put("w/ Mount and Bearing","i");
        result.put("Exc. Hybrid & Si Models Kit w/ Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hybrid & Si Models","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Hybrid & Si Models Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Spring Seat","i");
        currentMap.put("Shock Absorber","i");
        result.put("Spring Seat Shock Absorber", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("Exc. Electronic Adjustable Suspension Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Late Design","i");
        currentMap.put("Front Strut without Spring Guard Kit w/ Mount and Bearing","i");
        result.put("Late Design Front Strut without Spring Guard Kit w/ Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("CCR Computer Command Ride Kit","f");
        currentMap.put("w/ Mount and Bearing","i");
        result.put("CCR Computer Command Ride Kit w/ Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Front Struts","i");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("Front Struts Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 12/02","f");
        currentMap.put("Kit w/ Mount and Bearing","i");
        result.put("Mfg. to 12/02 Kit w/ Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Crew Cab","f");
        currentMap.put("w/ Desert Runner Package","f");
        result.put("Crew Cab w/ Desert Runner Package", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Crew Cab","f");
        currentMap.put("w/ Desert Runner Package","f");
        result.put("Crew Cab with Desert Runner", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mount Kit","i");
        currentMap.put("w/ Strut Mount & Retainer","i");
        result.put("Mount Kit w/ Strut Mount & Retainer", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mount Kit","i");
        currentMap.put("w/ Mount, Spring Isolator, Nuts, Spacer, Upper Spring Seat","i");
        result.put("Mount Kit w/ Mount, Spring Isolator, Nuts, Spacer, Upper Spring Seat", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mount Kit","i");
        currentMap.put("w/ Strut Mount","i");
        result.put("Mount Kit w/ Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Lift Kit","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Lift Kit air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Standard Cab Pickup","f");
        currentMap.put("Air hose kit included","i");
        result.put("Standard Cab Pickup air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Quad Suspension and Lift Kit","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Quad Suspension and Lift Kit air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Quad Suspension and Lift Kit","f");
        currentMap.put("Air hose kit included","i");
        currentMap.put("Full Size","f");
        result.put("Full Size Exc. Quad Suspension and Lift Kit Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Standard Suspension","f");
        currentMap.put("Exc. Lift Kit","f");
        result.put("Standard Suspension Exc. Lift Kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("4WD","DRIVE");
        currentMap.put("Exc. Lift Kit","f");
        result.put("4WD Exc. Lift Kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Quad Suspension and Lift Kit","f");
        currentMap.put("Full Size","f");
        result.put("Full Size Exc. Quad Suspension and Lift Kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 10/75","f");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("Mfg. from 10/75 Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 8/74","f");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("Mfg. from 8/74 Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 3/86","f");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("Mfg. to 3/86 Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ Front and Rear","f");
        currentMap.put("Engine OEM # 1316546","f");
        result.put("w/ Front and Rear Engine OEM # 1316546", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 8800# GVW","f");
        currentMap.put("Air hose kit included","f");
        result.put("Exc. 8800# GVW air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Solid Front Axle","f");
        currentMap.put("Air hose kit included","f");
        result.put("Exc. Solid Front Axle Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 3/00","f");
        currentMap.put("Exc XC Cross Country","f");
        result.put("Mfg. to 3/00 Exc XC Cross Country", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 3/00","f");
        currentMap.put("Exc. XC70","f");
        result.put("Mfg. to 3/00 and Exc. XC70", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("9200# GVW","f");
        currentMap.put("with O.E","f");
        result.put("9200# GVW with O.E", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Second Design","i");
        currentMap.put("with Rear Strut Lower Mounting holes at 2.4\" apart","i");
        result.put("Second Design with Rear Strut Lower Mounting holes at 2.4\" apart", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Shock Mount","i");
        result.put("Exc. Electronic Adjustable Suspension Shock Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("SSS Speed Sensitive Suspension","f");
        currentMap.put("CCR Computer Command Ride","f");
        result.put("SSS Speed Sensitive Suspension or CCR Computer Command Ride", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Computer Active Technology Suspension","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Computer Active Technology Suspension Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Models with Tokico OE Struts","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Models with Tokico OE Struts Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("From Chassis # 08774","f");
        currentMap.put("VR6 Style","f");
        result.put("From Chassis # 08774 VR6 Style", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("VR6","f");
        currentMap.put("from Chassis # 08774","f");
        result.put("VR6 from Chassis # 08774", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("From Chassis # 08774","f");
        currentMap.put("VR6","f");
        result.put("From Chassis # 08774 VR6", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad Suspension","f");
        currentMap.put("Short Shock","f");
        currentMap.put("Full Size","f");
        result.put("Full Size Quad Suspension Short Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Full Size","f");
        currentMap.put("Air hose kit included","i");
        result.put("Full Size air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 8600# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 8600# GVW air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Old Body Style","f");
        currentMap.put("Air hose kit included","i");
        result.put("Old Body Style air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("4WD","DRIVE");
        currentMap.put("Up through Serial No Y2000","f");
        result.put("4WD Up through Serial No Y2000", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 2/89","f");
        currentMap.put("Vehicles with 12mm bolt in rear lower mount","i");
        result.put("Mfg. from 2/89 Vehicles with 12mm bolt in rear lower mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Coil Spring Suspension","f");
        currentMap.put("Steering Stabilizer","i");
        result.put("Coil Spring Suspension Steering Stabilizer", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 3/00","f");
        currentMap.put("Exc. XC70","f");
        result.put("Mfg. from 3/00 and Exc. XC70", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg from 07/05","f");
        currentMap.put("w/7 Passenger Seating","f");
        result.put("Mfg from 07/05 w/7Passenger Seating", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg from 07/05","f");
        currentMap.put("w/7 Passenger Seating","f");
        result.put("Mfg from 07/05 w/7 Passenger Seating", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("w/ Upper Spring Seat","f");
        currentMap.put("Mount Kit","i");
        result.put("Exc. Hydraulic Self-Leveling System Mount Kit w/ Upper Spring Seat", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("w/ Upper Spring Seat","f");
        currentMap.put("Mount Kit","i");
        result.put("Exc. Hydraulic Self Leveling System Mount Kit w/ Upper Spring Seat", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Supercab","f");
        currentMap.put("Air hose kit included","i");
        result.put("Supercab Air hose kit included", currentMap);

       currentMap = new HashMap<>();
        currentMap.put("Motorhome Chassis","f");
        currentMap.put("Independent Front Suspension","f");
        result.put("Motorhome Chassis with Independent Front Suspension", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Full Size","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Full Size Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Full Size","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Full Size Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("With OE Style air fitting","f");
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        result.put("With OE Style air fitting Exc. Electronic Adjustable Suspension", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Edge w/ Torsion Bar","f");
        currentMap.put("Front Spring Suspension","f");
        result.put("Exc. Edge w/ Torsion Bar (FrontSpringType)", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("3 Stud Top Mount","i");
        currentMap.put("from 07/01/2002","f");
        result.put("3 Stud Top Mount from 07/01/2002", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad & Non Quad Suspensions","f");
        currentMap.put("Rearward of Front Axle","POS");
        result.put("Quad & Non Quad Suspensions Rearward of Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Lift Kit","f");
        currentMap.put("Non Gas Charged","i");
        result.put("Exc. Lift Kit Non Gas Charged", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rearward of Front Axle","POS");
        currentMap.put("Exc. Lift Kit","f");
        result.put("Rearward of Front Axle Exc. Lift Kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("With 10mm Bolt In Rear Lower Mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("With 10mm Bolt In Rear Lower Mount Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vehicles with 10mm & 12mm bolt in rear lower mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vehicles with 10mm & 12mm bolt in rear lower mount air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vehicles with 10mm bolt in rear lower mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vehicles with 10mm bolt in rear lower mount air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Heavy Duty","f");
        currentMap.put("Exc. Solid Front Axle","f");
        result.put("Exc. Heavy Duty Exc. Solid Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Heavy Duty","f");
        currentMap.put("w/ Solid Front Axle","f");
        result.put("Exc Heavy Duty w/ Solid Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Heavy Duty","f");
        currentMap.put("Exc. Solid Front Axle","f");
        result.put("Exc Heavy Duty Exc. Solid Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Ride Control","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Exc. Electronic Ride Control Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Ride Control","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Exc. Electronic Ride Control Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("9200# GVW","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("9200# GVW Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("9200# GVW","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("9200# GVW Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Heavy Duty","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Heavy Duty Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Heavy Duty","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Heavy Duty Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ Taperleaf Rear suspension","f");
        currentMap.put("OEM # 15185643","f");
        result.put("w/ Taperleaf Rear suspension OEM # 15185643", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("All w/o OE Style air fitting","f");
        currentMap.put("Must be used with provided air kit","i");
        result.put("All w/o OE Style air fitting Must be used with provided air kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Must be used with provided air kit","i");
        result.put("Exc. Electronic Adjustable Suspension Must be used with provided air kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("RWD","DRIVE");
        currentMap.put("w/Front Coil Springs","f");
        result.put("RWD w/Front Coil Springs", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Leaf(RearSpringType)","f");
        currentMap.put("Air hose kit included","i");
        result.put("Leaf(RearSpringType) air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 146\" Wheelbase","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 146\" Wheelbase air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Nivomat Self-Leveling Option","f");
        currentMap.put("Mount Kit w/ Upper Spring Seat","i");
        result.put("Exc. Nivomat Self-Leveling Option Mount Kit w/ Upper Spring Seat", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Nivomat Self-Leveling Option","f");
        currentMap.put("Mount Kit w/ Upper Spring Seat","i");
        result.put("Exc. Nivomat Self- Leveling Option Mount Kit w/ Upper Spring Seat", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Limo","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Limo Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Cutaway Chassis","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Cutaway Chassis Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Motorhome Conversion","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Motorhome Conversion air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Air Assist Rear Suspension Standard Equipment","f");
        currentMap.put("ReadyMount Strut","i");
        currentMap.put("does not include springs due to vehicle desig","i");
        result.put("Air Assist Rear Suspension Standard Equipment ReadyMount Strut does not include springs due to vehicle design", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("CCR Computer Command Ride","f");
        currentMap.put("ReadyMount Strut","i");
        currentMap.put("does not include springs due to vehicle desig","i");
        result.put("CCR Computer Command Ride ReadyMount Strut does not include springs due to vehicle design", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("New Body Style","f");
        currentMap.put("Exc. Z55 Auto Ride Suspension","f");
        result.put("New Body Style Exc. Z55 Auto Ride Suspension", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Old Body Style","f");
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        result.put("Old Body Style Exc. Electronic Adjustable Suspension", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ OEM 52038276","f");
        currentMap.put("Solid Front Axle","f");
        result.put("w/ OEM 52038276 Solid Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 7000# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 7000# GVW air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 4000# Front Axle","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 4000# Front Axle air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Over 8300# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Over 8300# GVW Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("8500 or 9000 Front Axle","f");
        currentMap.put("OEM #'s 3U2Z18124MAA","f");
        result.put("8500 or 9000 Front Axle OEM #'s 3U2Z18124MAA", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("2 Bolt Flange-Type Lower Mount","i");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("2 Bolt Flange-Type Lower Mount Notice: The level of difficulty to install this product is moderately high", currentMap);
        currentMap = new HashMap<>();
        currentMap.put("2 Bolt Flange-Type Lower Mount","i");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("2 Bolt Lower Mount Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. FE3 and Electronic Ride Control","f");
        currentMap.put("Mount Kit w/ Suspension Bearing","i");
        result.put("Exc. FE3 and Electronic Ride Control Mount Kit w/ Suspension Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("FE3 Suspension","f");
        currentMap.put("Mount Kit w/ Suspension Bearing","i");
        result.put("FE3 Suspension Mount Kit w/ Suspension Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Mount Kit w/ Suspension Bearing","i");
        result.put("Exc. Electronic Adjustable Suspension Mount Kit w/ Suspension Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("The Nivomat leveling shocks are on the rear only","f");
        result.put("Exc. Hydraulic Self Leveling System The Nivomat leveling shocks are on the rear only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("The Nivomat leveling shocks are on the rear only","f");
        result.put("Exc. Hydraulic Self-Leveling System The Nivomat leveling shocks are on the rear only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Travelette with 166\" Wheelbase","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Travelette with 166\" Wheelbase air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 166\" Wheelbase","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 166\" Wheelbase Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Independent Front Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Independent Front Suspension air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 8/86","f");
        currentMap.put("Air hose kit included","i");
        result.put("Mfg. from 8/86 Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Desert Runner","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Desert Runner air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 1 Ton","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 1 Ton air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Non-Gas Charged Unit","i");
        currentMap.put("is non-gas charged by design","i");
        result.put("Non-Gas Charged Unit This unit is non-gas charged by design", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Conventional Type Replacement for Air Assist Rear Suspension","i");
        currentMap.put("Strut with Upper Mount Only","i");
        result.put("Conventional Type Replacement for Air Assist Rear Suspension Strut with Upper Mount Only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Strut with Upper Mount Only","i");
        result.put("Exc. Electronic Adjustable Suspension Strut with Upper Mount Only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Conventional Type Replacement","i");
        currentMap.put("Strut with Upper Mount Only","i");
        result.put("Conventional Type Replacement Strut with Upper Mount Only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Conventional Type Replacement","i");
        currentMap.put("for Air Assist Rear Suspension","f");
        result.put("Conventional Type Replacement for Air Assist Rear Suspension", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Ride Control","f");
        currentMap.put("NON-GAS-CHARGED","i");
        result.put("Exc. Electronic Ride Control NON-GAS-CHARGED", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("NON-GAS-CHARGED","i");
        result.put("Exc. Electronic Adjustable Suspension NON-GAS-CHARGED", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Z55 Auto Ride Suspension","f");
        currentMap.put("NON-GAS-CHARGED","i");
        result.put("Exc. Z55 Auto Ride Suspension NON-GAS-CHARGED", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Independent Rear Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Independent Rear Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Air-Leveling Suspension & Independent Rear Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Air-Leveling Suspension & Independent Rear Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad Suspension","f");
        currentMap.put("Forward of Front Axle","POS");
        result.put("Quad Suspension Forward of Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("3 Bolt Flange-Type Lower Mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("3 Bolt Flange-Type Lower Mount air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Strut Only","i");
        currentMap.put("Must exercise air bladder","i");
        result.put("Strut Only Must exercise air bladder", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Hydraulic Self Leveling System","f");
        currentMap.put("Air hose kit included","i");
        result.put("Hydraulic Self Leveling System Must exercise air bladder", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Optional OE Hydraulic Self-Leveling System","f");
        currentMap.put("Air hose kit included","i");
        result.put("Optional OE Hydraulic Self-Leveling System Must exercise air bladder", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("CCR Computer Command Ride","f");
        currentMap.put("Air hose kit included","i");
        result.put("CCR Computer Command Ride Must exercise air bladder", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("HD Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("HD Suspension air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Multi-Leaf Rear Spring","f");
        currentMap.put("Air hose kit included","i");
        result.put("Multi-Leaf Rear Spring Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 6/01","f");
        currentMap.put("Air hose kit included","i");
        result.put("Mfg. to 6/01 Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Sportwagon","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Sportwagon air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vertically Mounted","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vertically Mounted Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single Leaf Rear Springs","f");
        currentMap.put("Air hose kit included","i");
        result.put("Single Leaf Rear Springs Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self Leveling System","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Hydraulic Self Leveling System Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Hydraulic Self-Leveling System Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Active Suspension","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Active Suspension Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Single System Steering Stabilizer _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with I-Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with I-Beam Axle __Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Coil Spring Front Suspension","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Coil Spring Front Suspension _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Vacuum Servo Mounted on Drivers Side of Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Vacuum Servo Mounted on Drivers Side of Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Sway Bar","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Sway Bar _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Twin I-Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Twin I-Beam Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with I Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with I Beam Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","i");
        currentMap.put("with Twin I Beam Axle","f");
        result.put("Single System Steering Stabilizer with Twin I Beam Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","i");
        currentMap.put("with Twin I Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Single System Steering Stabilizer with Twin I Beam Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);
        currentMap = new HashMap<>();

        currentMap.put("with Twin Traction Beam","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Twin Traction Beam _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","i");
        currentMap.put("with Twin I Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","f");
        result.put("Single System Steering Stabilizer with I Beam Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Twin I Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Twin I Beam Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Dual System Steering Stabilizer","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Dual System Steering Stabilizer _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Anti-Sway Bar","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Anti-Sway Bar _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Cooler","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Cooler _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Motorhome","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Motorhome _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Superduty","f");
        currentMap.put("Motorhome","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Superduty Motorhome _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("K5","f");
        currentMap.put("Dual System Steering Stabilizer","i");
        result.put("K5 Dual System Steering Stabilizer", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("AN49","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("AN49 _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

       currentMap = new HashMap<>();
        currentMap.put("3/4 Ton & Travelall","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("3/4 Ton & Travelall _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("1/2 and 3/4 Ton","f");
        currentMap.put("with Twin I Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("1/2 and 3/4 Ton I-Beam Axle _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("800","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("800 _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Independent Front Suspension","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Independent Front Suspension _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Dual System","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Dual System _Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("RWD","DRIVE");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("RWD Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("RWD","DRIVE");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("RWD Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("4WD","DRIVE");
        currentMap.put("Exc. Quad Suspension and Lift Kit","i");
        result.put("4WD Exc. Quad Suspension and Lift Kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Straight Front Axle","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Exc. HD Straight Front Axle Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Straight Front Axle","f");
        currentMap.put("Non Gas Charged Shock","i");
        result.put("Exc. HD Straight Front Axle Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ Desert Runner Package","f");
        currentMap.put("Air hose kit included","i");
        result.put("w/ Desert Runner Package Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Crew Cab","f");
        currentMap.put("Air hose kit included","i");
        currentMap.put("w/ Desert Runner Package","f");
        result.put("Crew Cab w/ Desert Runner Package Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Crew Cab","f");
        currentMap.put("Air hose kit included","i");
        currentMap.put("w/ Desert Runner Package","f");
        result.put("Crew Cab with Desert Runner air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ Desert Runner Package","f");
        currentMap.put("Air hose kit included","i");
        result.put("with Desert Runner air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Wagon","f");
        currentMap.put("Air hose kit included","i");
        result.put("Wagon air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 1/75","f");
        currentMap.put("Air hose kit included","i");
        result.put("Mfg. from 1/75 Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("1 Ton","f");
        currentMap.put("Air hose kit included","i");
        result.put("1 Ton Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. HD Suspension air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Heavy Duty Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Police and Taxi Models","f");
        currentMap.put("Air hose kit included","i");
        result.put("Police and Taxi Models air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vehicles with 12mm bolt in rear lower mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vehicles with 12mm bolt in rear lower mount air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. from 2/89","f");
        currentMap.put("Air hose kit included","i");
        currentMap.put("Vehicles with 12mm bolt in rear lower mount","i");
        result.put("Mfg. from 2/89 Vehicles with 12mm bolt in rear lower mount Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. M-Sport Pack II","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. M-Sport Pack II Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Sport Suspension M-Sport Pack II","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Sport Suspension M-Sport Pack II Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Suspension","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. HD Suspension Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mfg. to 6/02","f");
        currentMap.put("Exc. Computer Active Technology Suspension","f");
        result.put("Mfg. to 6/02 Exc. Computer Active Technology Suspension", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("Shock Mount","i");
        result.put("Exc. Hydraulic Self-Leveling System Shock Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("Shock Mount","i");
        result.put("Exc. Hydraulic Self Leveling System Shock Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self-Leveling System","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Hydraulic Self Leveling System Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Auto Rear Leveling","f");
        currentMap.put("Shock Mount","i");
        result.put("Exc. Auto Rear Leveling Shock Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Auto Rear Leveling","f");
        currentMap.put("Strut Mount","i");
        result.put("Exc. Auto Rear Leveling Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rebuildable Front Struts","i");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high","i");
        result.put("Rebuildable Front Struts Notice: The level of difficulty to install this product is moderately high", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 7000# GVW","f");
        currentMap.put("Non Gas-Charged Shock","i");
        result.put("Exc. 7000# GVW Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 7000# GVW","f");
        currentMap.put("Non Gas-Charged Shock","i");
        result.put("Exc. 7000# GVW Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 4000# Front Axle","f");
        currentMap.put("Non Gas-Charged Shock","i");
        result.put("Exc. 4000# Front Axle Non Gas Charged Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 4000# Front Axle","f");
        currentMap.put("Non Gas-Charged Shock","i");
        result.put("Exc. 4000# Front Axle Non Gas Charged Shock Shock", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Drive and Tag Axle Shocks","f");
        currentMap.put("OEM # 630253","f");
        result.put("Drive and Tag Axle Shocks OEM # 630253", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. M Body","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. M Body Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad Suspension","f");
        currentMap.put("with 3-4\" Lift Kit","f");
        currentMap.put("Forward of Front Axle","POS");
        result.put("Quad Suspension with 3 - 4\" Lift Kit Forward of Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Quad Suspension","f");
        currentMap.put("with 3-4\" Lift Kit","f");
        currentMap.put("Forward of Front Axle","POS");
        result.put("Quad Suspension with 3-4\" Lift Kit Forward of Front Axle", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Buses w/ Hendrickson Parasteer Suspensions","f");
        currentMap.put("OEM #'s 0015222, 0040015, 481700131438, 59483000, 59483-000, 60665005, 60665-005, 60665005L, 60665-005L60998002, 60998-002, 60998002L, 60998-002L","f");
        result.put("Buses w/ Hendrickson Parasteer Suspensions OEM #'s 0015222, 0040015, 481700131438, 59483000, 59483-000, 60665005, 60665-005, 60665005L, 60665-005L60998002, 60998-002, 60998002L, 60998-002L", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Models w/ Hendrickson Air Suspension","f");
        currentMap.put("(Stamping number 47902-29)","f");
        currentMap.put("OEM #'s 12474917, 15740554","f");
        result.put("Models w/ Hendrickson Air Suspension (Stamping number 47902-29) OEM #'s 12474917, 15740554", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 19.5\" or 22.5\" Wheels","f");
        currentMap.put("22,000 lb GVW","f");
        currentMap.put("OEM #'s 471700011530, ASH24658, ASH-24658, ASH24659, ASH-24659, FC4418045BB, FC44-18045-BB, FC4Z18124A, FC4Z-18124-A, FC4Z18124B, FC4Z-18124-B","f");
        result.put("w/ 19.5\" or 22.5\" Wheels, 22,000 lb GVW OEM #'s 471700011530, ASH24658, ASH-24658, ASH24659, ASH-24659, FC4418045BB, FC44-18045-BB, FC4Z18124A, FC4Z-18124-A, FC4Z18124B, FC4Z-18124-B", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ Hendrickson HAS - 230 Rear Air Suspension","f");
        currentMap.put("OEM #'s 4C4Z18125CA, 4C4Z-18125-CA, 60675003, 60675-003, 60675003L, 60675-003L, ASH1105, ASH-1105, ASH722, ASH-722","f");
        result.put("w/ Hendrickson HAS - 230 Rear Air Suspension OEM #'s 4C4Z18125CA, 4C4Z-18125-CA, 60675003, 60675-003, 60675003L, 60675-003L, ASH1105, ASH-1105, ASH722, ASH-722", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 21,000 lb Rear Air Suspension","f");
        currentMap.put("OEM #'s 3578858C1, 3578858C2, 4C4Z18125DA, 4C4Z-18125-DA","f");
        result.put("w/ 21,000 lb Rear Air Suspension OEM #'s 3578858C1, 3578858C2, 4C4Z18125DA, 4C4Z-18125-DA", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 20,000 lb IROS Air Suspension","f");
        currentMap.put("OEM # 3578858C2","f");
        result.put("w/ 20,000 lb IROS Air Suspension OEM # 3578858C2", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 6,000 to 8,000 Front Axles","f");
        currentMap.put("OEM # 3533356C3","f");
        result.put("w/ 6,000 to 8,000 Front Axles OEM # 3533356C3", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Coil","f");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        result.put("Rear Spring Type: Coil. Optional O.E. Hydraulic Self Leveling System", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("ReadyMount Strut does not include springs due to vehicle design","i");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation","i");
        result.put("Optional O.E. Hydraulic Self Leveling System ReadyMount Strut does not include springs due to vehicle design. Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Air Assist Rear Suspension Standard Equipment","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation","i");
        result.put("Air Assist Rear Suspension Standard Equipment Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle)","f");
        currentMap.put("OEM #'S 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684","f");
        result.put("w/ 19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle) OEM #'S 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle)","f");
        currentMap.put("OEM #'s 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684","f");
        result.put("w/ 19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle) OEM #'s 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Models with Tokico O.E. Struts","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Models with Tokico O.E. Struts Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Models with Tokico O.E. Struts","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Models with Tokico O.E. Struts air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Elec. Adj. Susp.","f");
        currentMap.put("Strut with Upper Mount Only","i");
        result.put("Exc. Elec. Adj. Susp. Strut with Upper Mount Only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Non-Gas Charged Unit","i");
        currentMap.put("This unit is non-gas charged by design","i");
        currentMap.put("The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to pr","i");
        result.put("Non-Gas Charged Unit This unit is non-gas charged by design. The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to pr", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("ReadyMount Strut does not include springs due to vehicle design","i");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation.","i");
        result.put("Exc. Electronic Adjustable Suspension ReadyMount Strut does not include springs due to vehicle design. Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air","f");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        result.put("Rear Spring Type: Air. Optional O.E. Hydraulic Self Leveling System", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation","i");
        result.put("Exc. Electronic Adjustable Suspension Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Front Spring Type: Coil","f");
        currentMap.put("Rear Spring Type: Coil","f");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        result.put("Front Spring Type: Coil, Rear Spring Type: Coil. Optional O.E. Hydraulic Self Leveling System", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation","i");
        result.put("Optional O.E. Hydraulic Self Leveling System Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air","f");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        result.put("Rear Spring Type: Air. Optional O.E. Hydraulic Self Leveling System", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Auto-Leveling System","f");
        currentMap.put("Strut Mount","i");
        result.put("Auto-Leveling System Strut Mount", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Lift Kit","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Lift Kit Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("RWD","POS");
        currentMap.put("Air hose kit included","i");
        result.put("RWD Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Supercab","f");
        currentMap.put("Air hose kit included","i");
        result.put("Supercab air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ Independent Front Suspension","f");
        currentMap.put("OEM #'s 481700999377, 630163, 630136","f");
        result.put("w/ Independent Front Suspension OEM #'s 481700999377, 630163, 630136", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. HD Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Police and Taxi Models","f");
        currentMap.put("Air hose kit included","i");
        result.put("Police and Taxi Models Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Air. Conventional Replacement","f");
        currentMap.put("for Air Assist Susp","f");
        result.put("Air. Conventional Replacement for Air Assist Susp", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air. Conventional Replacement","f");
        currentMap.put("for Air Assist Susp","f");
        result.put("Rear Spring Type: Air. Conventional Replacement for Air Assist Susp", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Electronic Adjustable Suspension Strut","f");
        currentMap.put("with upper mount only","i");
        result.put("Exc. Electronic Adjustable Suspension Strut with upper mount only", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Multi-Leaf Rear Spring","f");
        currentMap.put("Air hose kit included","i");
        result.put("Multi-Leaf Rear Spring air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("HD Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("HD Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with I-Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with I-Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Sway Bar","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Sway Bar Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with I Beam Axle ","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with I Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Anti-Sway Bar ","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Anti-Sway Bar Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Cooler ","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Cooler Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Motorhome ","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Motorhome Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Superduty Motorhome ","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Superduty Motorhome Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("AN49 ","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("AN49 Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("3/4 Ton & Travelall ","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("3/4 Ton & Travelall Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("800","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("800 Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Dual System ","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Dual System Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Air Springs","f");
        currentMap.put("Air hose kit included","i");
        result.put("Rear Air Springs air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Leaf(RearSpringType)","f");
        currentMap.put("Air hose kit included","i");
        result.put("Leaf(RearSpringType) Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 11,000# Rear Axle","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 11,000# Rear Axle Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 11,000# Rear Axle","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 11,000# Rear Axle air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 146\" Wheelbase","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 146\" Wheelbase Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Mojave","f");
        currentMap.put("Air hose kit included","i");
        result.put("Mojave air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 1 Ton","f");
        currentMap.put("Kit with Mount and Bearing","i");
        result.put("Exc. 1 Ton Kit with Mount and Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("School Bus Chassis w/15000, 19000, 21000 lb Multi Leaf Springs","f");
        currentMap.put("OEM #'s 15174902, 22064232","f");
        result.put("School Bus Chassis w/15000, 19000, 21000 lb Multi Leaf Springs OEM #'s 15174902, 22064232", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Tilt Cab w/15000, 19000, 21000 Multileaf Springs","f");
        currentMap.put("OEM #'s 15174902, 22064232","f");
        result.put("Tilt Cab w/15000, 19000, 21000 Multileaf Springs OEM #'s 15174902, 22064232", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Sportwagon","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Sportwagon Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vehicles with 12mm bolt in rear lower mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vehicles with 12mm bolt in rear lower mount Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Standard Cab Pickup","f");
        currentMap.put("Air hose kit included","i");
        result.put("Standard Cab Pickup Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Quad Suspension and Lift Kit","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Quad Suspension and Lift Kit Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 20,000 lb Rear Air Suspension","f");
        currentMap.put("OEM # 3601600C3","f");
        result.put("w/ 20,000 lb Rear Air Suspension OEM # 3601600C3", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Straight Front Axle","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. HD Straight Front Axle air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Cutaway Chassis","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Cutaway Chassis air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Up through 8500# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Up through 8500# GVW air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Independent Front Suspension","f");
        currentMap.put("Air hose kit included","i");
        result.put("Independent Front Suspension Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Desert Runner","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Desert Runner Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Wagon","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Wagon air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 1 Ton","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 1 Ton Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Travelette with 166\" Wheelbase","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Travelette with 166\" Wheelbase Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Coil(RearSpringType)","f");
        currentMap.put("Air hose kit included","i");
        result.put("Coil(RearSpringType) air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Front","POS");
        currentMap.put("Mount Kit w/ Mount, Retainer, Bearing, Nuts","i");
        result.put("Front. Mount Kit w/ Mount, Retainer, Bearing, Nuts", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Front","POS");
        currentMap.put("Mount Kit w/ Mount, Retainer, Bearing, Nuts","i");
        result.put("Position: Front. Mount Kit w/ Mount, Retainer, Bearing, Nuts", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("With 10mm Bolt In Rear Lower Mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("With 10mm Bolt In Rear Lower Mount air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vehicles with 10mm & 12mm bolt in rear lower mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vehicles with 10mm & 12mm bolt in rear lower mount Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vehicles with 10mm bolt in rear lower mount","i");
        currentMap.put("Air hose kit included","i");
        result.put("Vehicles with 10mm bolt in rear lower mount Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Front","POS");
        currentMap.put("Mount Kit w/ Strut Mount, Bearing & 3 Nuts","i");
        result.put("Front. Mount Kit w/ Strut Mount, Bearing & 3 Nuts", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Front","POS");
        currentMap.put("Mount Kit w/ Strut Mount, Bearing & 3 Nuts","i");
        result.put("Position: Front. Mount Kit w/ Strut Mount, Bearing & 3 Nuts", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Hydraulic Self Leveling System","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. Hydraulic Self Leveling System air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. M Body","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. M Body air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 8600# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 8600# GVW Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 7000# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 7000# GVW Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. 4000# Front Axle","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. 4000# Front Axle Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Over 8300# GVW","f");
        currentMap.put("Air hose kit included","i");
        result.put("Over 8300# GVW air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. HD Package","f");
        currentMap.put("Air hose kit included","i");
        result.put("Exc. HD Package air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Vertically Mounted","f");
        currentMap.put("Air hose kit included","i");
        result.put("Vertically Mounted air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Full Size","f");
        currentMap.put("Air hose kit included","i");
        result.put("Full Size Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Old Body Style","f");
        currentMap.put("Air hose kit included","i");
        result.put("Old Body Style Air hose kit included", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("8500 or 9000 Front Axle","f");
        currentMap.put("OEM #'s 3U2Z18124MAA, 3U2Z-18124-MAA, AA494, AA-494, ASH397, ASH-397, XC3518045AB, XC35-18045AB, XC3518045AD, XC35-18045-AD, XC3518045JA, XC35-18045-JA, XC3Z18124JA, XC3Z-18124-JA","f");
        result.put("8500 or 9000 Front Axle OEM #'s 3U2Z18124MAA, 3U2Z-18124-MAA, AA494, AA-494, ASH397, ASH-397, XC3518045AB, XC35-18045AB, XC3518045AD, XC35-18045-AD, XC3518045JA, XC35-18045-JA, XC3Z18124JA, XC3Z-18124-JA", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Motorhome Chassis with 20,500 lb GVW to 22,000 lb GVW","f");
        currentMap.put("OEM #'s : 9U94-18080-AA , 9U9Z-18125-A , ASH-1023 , ASH-1206 , ASH-1230 , ASH-137 , ASH-25824 , AT-621","f");
        result.put("Motorhome Chassis with 20,500 lb GVW to 22,000 lb GVW OEM #'s : 9U94-18080-AA , 9U9Z-18125-A , ASH-1023 , ASH-1206 , ASH-1230 , ASH-137 , ASH-25824 , AT-621", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Single System Steering Stabilizer Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Coil Spring Front Suspension","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Coil Spring Front Suspension Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Vacuum Servo Mounted on Drivers Side of Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Vacuum Servo Mounted on Drivers Side of Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Twin I-Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Twin I-Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        currentMap.put("with Twin I Beam Axle","f");
        result.put("Single System Steering Stabilizer with Twin I Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Twin Traction Beam","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Twin Traction Beam Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Single System Steering Stabilizer","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        currentMap.put("with I Beam Axle","f");
        result.put("Single System Steering Stabilizer with I Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("with Twin I Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("with Twin I Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Dual System Steering Stabilizer","i");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Dual System Steering Stabilizer Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("1/2 and 3/4 Ton I-Beam Axle","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("1/2 and 3/4 Ton I-Beam Axle Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Exc. Independent Front Suspension","f");
        currentMap.put("Replacement damper for vehicles with the Gabriel Aftermarket mounting kit","i");
        result.put("Exc. Independent Front Suspension Replacement damper for vehicles with the Gabriel Aftermarket mounting kit", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Non-Gas Charged Unit This unit is non-gas charged by design.","i");
        currentMap.put("The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to protect it. Use","i");
        result.put("Non-Gas Charged Unit This unit is non-gas charged by design. The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to protect it. Use", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 12,000 and 13,200 LB Front Springs","f");
        currentMap.put("OEM #'s 3584118C2, 4C4Z18124JB, 4C4Z-18124-JB, 4C4Z18124RA, 4C4Z-18124-RA, AA617, AA-617, AT812, AT-812","f");
        result.put("w/ 12,000 and 13,200 LB Front Springs OEM #'s 3584118C2, 4C4Z18124JB, 4C4Z-18124-JB, 4C4Z18124RA, 4C4Z-18124-RA, AA617, AA-617, AT812, AT-812", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air.","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation.","i");
        currentMap.put("w/Air assist Rear Susp. Std. Equip. Strut Only","f");
        result.put("Rear Spring Type: Air. w/Air assist Rear Susp. Std. Equip. Strut Only Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air. ","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation.","i");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        result.put("Rear Spring Type: Air. Optional O.E. Hydraulic Self Leveling System Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air. ","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation.","i");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        result.put("Rear Spring Type: Air. Optional OE Hydraulic Self-Leveling System Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air. ","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation.","i");
        currentMap.put("Exc. Electronic Adjustable Suspension","f");
        result.put("Rear Spring Type: Air. Exc. Electronic Adjustable Suspension Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Non-Gas Charged Unit This unit is non-gas charged by design.","i");
        currentMap.put("The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to protect it. Use","i");
        result.put("Non-Gas Charged Unit This unit is non-gas charged by design. The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to protect it. Use", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Conventional Cab w/ 15000, 19000, 210000 lb Multileaf Springs","f");
        currentMap.put("OEM #'s 15174902, 22064232, 88982686","f");
        result.put("Conventional Cab w/ 15000, 19000, 210000 lb Multileaf Springs OEM #'s 15174902, 22064232, 88982686", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("w/ 19000, 21000, 23000 lb Taperleaf Springs","f");
        currentMap.put("(EXC Low Profile Vehicle)","f");
        currentMap.put("OEM #'s 22064233, 15123676, 15588554, 15887728, 19152843, 22064230, 88982682, 88982684","f");
        result.put("w/ 19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle) OEM #'s 22064233, 15123676, 15588554, 15887728, 19152843, 22064230, 88982682, 88982684", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("L3 Tilt Cab w/ 14575 lb Axle","f");
        currentMap.put("OEM #'s 22064804, 22064805, 22165187, 22165188, 22064807, 22166442, 22064806, 22165189, 22064233","f");
        result.put("FL3 Tilt Cab w/ 14575 lb Axle OEM #'s 22064804, 22064805, 22165187, 22165188, 22064807, 22166442, 22064806, 22165189, 22064233", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Conventional Cab w/19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle)","f");
        currentMap.put("OEM #'s 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684","f");
        result.put("Conventional Cab w/19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle) OEM #'s 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Conventional Cab w/19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle)","f");
        currentMap.put("OEM #'S 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684","f");
        result.put("Conventional Cab w/19000, 21000, 23000 lb Taperleaf Springs (EXC Low Profile Vehicle) OEM #'S 15123676, 15174898, 15588554, 15887728, 19152843, 22064230, 88982682, 22064233, 88982684", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Position: Front.","POS");
        currentMap.put("Exc. Soft Ride Susp (FE1) and Elec. Ride & Handling (FW1)","f");
        currentMap.put("Mount Kit w/ Suspension Bearing","i");
        result.put("Position: Front. Exc. Soft Ride Susp (FE1) and Elec. Ride & Handling (FW1) Mount Kit w/ Suspension Bearing", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rebuildable O.E. Front Struts","i");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high.","i");
        currentMap.put("Please review the installation instructions carefully before proceeding","i");
        result.put("Rebuildable O.E. Front Struts Notice: The level of difficulty to install this product is moderately high. Please review the installation instructions carefully before proceeding", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rebuildable O.E. Front Struts","i");
        currentMap.put("Notice: The level of difficulty to install this product is moderately high.","i");
        currentMap.put("Please review the installation instructions carefully before proceeding","i");
        result.put("Rebuildable OE Front Struts Notice: The level of difficulty to install this product is moderately high. Please review the installation instructions carefully before proceeding", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("B14","i");
        currentMap.put("Solid Rear Axle","f");
        currentMap.put("Product is designed to match original equipment performance level, not exceed.","i");
        result.put("B14 Solid Rear Axle Product is designed to match original equipment performance level, not exceed.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("B14","i");
        currentMap.put("Solid Rear Axle","f");
        currentMap.put("Product is designed to match original equipment performance level, not exceed.","i");
        result.put("B14 Solid Rear Axle Product is designed to match original equipment performance level, not exceed.", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Non-Gas Charged Unit This unit is non-gas charged by design.","i");
        currentMap.put("The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to protect it. Use","i");
        result.put("Non-Gas Charged Unit This unit is non-gas charged by design. The piston rod must be extended manually for installation. Carefully grasp the rod end and extend to full travel. (Note: Tools may damage the rod surface, so it is imperative to protect it. Use", currentMap);

        currentMap = new HashMap<>();
        currentMap.put("Rear Spring Type: Air.","f");
        currentMap.put("Must exercise air bladder, cycle strut by hand prior to installation.","i");
        currentMap.put("Optional O.E. Hydraulic Self Leveling System","f");
        currentMap.put("ReadyMount Strut does not include springs due to vehicle design.","i");
        result.put("Rear Spring Type: Air. Optional O.E. Hydraulic Self Leveling System ReadyMount Strut does not include springs due to vehicle design. Must exercise air bladder, cycle strut by hand prior to installation.", currentMap);


        return result;
    }

    public void processNote() {
        Map<String, String> curMap = appMap.get(appNote);
        if (curMap==null){
            logger.info("Unknown application note for break-up: " + appNote);
        }
        else {
            curMap.forEach((k,v)->{
                switch (v){
                    case "DRIVE": car.setDrive(k); break;
                    case "f": fit.getFitmentAttributes().add(new FitmentAttribute("Note", appNote)); break;
                    case "i": prodItem.getItemAttributes().add(new ItemAttribute("Note", appNote)); break;
                    case "POS": processPosition(k); break;
                    default: logger.info("Unknown break-up attribute: " + v + " for appNote: " + k);
                }

            });
        }
    }

    private void processPosition(String posNote) {
        String pos = "";
        if (posNote.startsWith("Front")){
            pos = "Front";
        }
        else {
            pos = "Rear";
        }
        posNote = posNote.replace(pos, "").trim();
        if (posNote.length()>0){
            if (posNote.startsWith("Left")){
                pos = pos + " Left";
            }
            if (posNote.startsWith("Right")){
                pos = pos + " Right";
            }
        }
        fit.getFitmentAttributes().add(new FitmentAttribute("Position", pos));
    }
}
