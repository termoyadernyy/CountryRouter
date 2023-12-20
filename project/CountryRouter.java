/*
 Created by termoyadernyy
 */

// Insert your package or remove it ⤵
package project;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

// insert your file name ⤵
public class CountryRouter {

    private static final String COUNTRIES_URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";

    public static void main(String[] args) {
        try {
            List<String> route = findRoute("CZK", "ITA");
            if (route != null) {
                System.out.println("The shortest way: " + route);
            } else {
                System.out.println("There is no way.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> findRoute(String startCountryCode, String endCountryCode) throws IOException {
        String countriesData = loadCountriesData();
        Country[] countries = Country.fromJson(countriesData);
        Country startCountry = findCountryByCode(startCountryCode, countries);
        Country endCountry = findCountryByCode(endCountryCode, countries);

        if (startCountry == null || endCountry == null) {
            System.out.println("The specified countries could not be found.");
            return null;
        }

        List<String> path = bfs(startCountry, endCountry, countries);

        if (path != null) {
            return path;
        } else {
            System.out.println("There is no way.");
            return null;
        }
    }

    private static String loadCountriesData() throws IOException {
        InputStream inputStream = new URL(COUNTRIES_URL).openStream();
        try (Scanner scanner = new Scanner(inputStream).useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    private static List<String> bfs(Country startCountry, Country endCountry, Country[] countries) {
        Set<String> visited = new HashSet<>();
        Queue<List<Country>> queue = new LinkedList<>();
        queue.add(Collections.singletonList(startCountry));

        while (!queue.isEmpty()) {
            List<Country> currentPath = queue.poll();
            Country currentCountry = currentPath.get(currentPath.size() - 1);

            if (currentCountry.getCCA3().equals(endCountry.getCCA3())) {
                List<String> resultPath = new ArrayList<>();
                for (Country country : currentPath) {
                    resultPath.add(country.getCCA3());
                }
                return resultPath;
            }

            if (!visited.contains(currentCountry.getCCA3())) {
                visited.add(currentCountry.getCCA3());

                // neighbor
                List<Country> neighbors = getNeighbors(currentCountry, countries);
                for (Country neighbor : neighbors) {
                    List<Country> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }

        return null;
    }

    private static List<Country> getNeighbors(Country country, Country[] countries) {
        List<Country> neighbors = new ArrayList<>();

        for (String border : country.getBorders()) {
            Country neighbor = findCountryByCode(border, countries);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private static Country findCountryByCode(String countryCode, Country[] countries) {
        for (Country country : countries) {
            if (country.getCCA3().equals(countryCode)) {
                return country;
            }
        }
        return null;
    }

    static class Country {
        private String cca3;
        private List<String> borders;

        public String getCCA3() {
            return cca3;
        }

        public List<String> getBorders() {
            return borders;
        }

        public static Country[] fromJson(String json) {
  
            return new Country[0];
        }
    }
}
