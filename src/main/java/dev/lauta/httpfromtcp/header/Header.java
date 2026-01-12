package dev.lauta.httpfromtcp.header;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Header extends HashMap<String, ArrayList<String>> {


    public Header() {
        super();
    }

    public void parse(String request) {
        String fieldname;
        String token;

        if (request.contains(":")) {
            String[] parts = request.split(":", 2);
            fieldname = parts[0].trim();
            token = parts[1].trim();

            if (fieldname.isEmpty()) {
                throw new IllegalArgumentException("Empty header");
            }
            if (fieldname.contains(" ")) {
                throw new IllegalArgumentException("No spaces allowed in fieldname");
            }

            fieldname = fieldname.toLowerCase();

            for (int i = 0; i < fieldname.length(); i++) {
                char ch = fieldname.charAt(i);
                String character = "" + ch;
                boolean isUpper = character.matches(".*[A-Z].*");
                boolean isLower = character.matches(".*[a-z].*");
                boolean isDigit = character.matches(".*[0-9].*");
                boolean isSpecialChar = character.matches(".*[!#$%&'*+\\-\\.\\^_`|~].*");
                if (!isUpper && !isLower && !isDigit && !isSpecialChar) {
                    throw new IllegalArgumentException("Invalid character in fieldName");
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid format");
        }

        if (containsKey(fieldname)) {
           ArrayList<String> tokens = get(fieldname);
           tokens.add(token);
           put(fieldname, tokens);
        }
        else {
            ArrayList<String> tokens = new ArrayList<>();
            tokens.add(token);
            put(fieldname, tokens);
        }

    }

    public Header getHeader() {
        return this;
    }

}
