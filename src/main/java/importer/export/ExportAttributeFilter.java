package importer.export;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ExportAttributeFilter {
    private Set<String> wrongCarFullValues;
    private Set<String> wrongFitStarterValues;
    private Set<String> wrongFitFullValues;
    private Set<String> wrongItemFullNames;
    boolean carValueIsValid(String value) {
        if (wrongCarFullValues.contains(value)){
            return false;
        }

        return true;
    }


    ExportAttributeFilter() {
        initiateItemFullNames();
        initiateCarFullValues();
        initiateFitStartValues();
        initiateFitFullValues();
    }

    private void initiateItemFullNames() {
        wrongItemFullNames = new HashSet<>();
        wrongItemFullNames.add("Description");
    }

    private void initiateFitFullValues() {
        wrongFitFullValues = new HashSet<>();
        wrongFitFullValues.add("Available From Your Local Dealer");
    }

    private void initiateFitStartValues() {
        wrongFitStarterValues = new HashSet<>();
        wrongFitStarterValues.add("Description");
    }

    private void initiateCarFullValues() {
        wrongCarFullValues = new HashSet<>();
    }

    boolean fitValueIsValid(String value) {
        for (String s : wrongFitStarterValues) {
            if (value.startsWith(s)) {
                return false;
            }
        }
        if (wrongFitFullValues.contains(value)){
            return false;
        }

        return true;
    }

    Map<String, Set<String>> filterCarAttributes(Map<String, Set<String>> carAttributesMap) {
        Map<String, Set<String>> result = new HashMap<>();
        carAttributesMap.forEach((k,v)-> v.forEach(value->{
            if (carValueIsValid(value)){
                Set<String> curValues = result.computeIfAbsent(k, k1 -> new HashSet<>());
                curValues.add(value);
            }
        }));

        return result;
    }

    Map<String, Set<String>> filterFitAttributes(Map<String, Set<String>> fitAttributeMap) {
        Map<String, Set<String>> result = new HashMap<>();
        fitAttributeMap.forEach((k,v)-> v.forEach(value->{
            if (fitValueIsValid(value)){
                Set<String> curValues = result.computeIfAbsent(k, k1 -> new HashSet<>());
                curValues.add(value);
            }
        }));

        return result;
    }

    boolean itemValueIsValid(String name, String value) {
        if (wrongItemFullNames.contains(name)){
            return false;
        }


        return true;
    }
}
