package dev.lauta.httpfromtcp.header;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Header  extends HashMap<String, String> {

    public Header() {
        super();
    }

    public void parse(String request) {
        String variable;
        String value;

        //System.err.println("DEBUG: header.Parse(request)='" + request + "'");
        if (request.contains(":")) {
            String[] parts = request.split(":", 2);
            variable = parts[0].trim();
            value = parts[1].trim();

            if (variable.isEmpty()) {
                throw new IllegalArgumentException("Empty header");
            }

        }

        else {
            throw new IllegalArgumentException("Invalid format");
        }


       this.put(variable, value);
    }

    public String findKey(String keyToFind) {
       for(Map.Entry<String, String> entry : this.entrySet()) {
           if (entry.getValue().equals(keyToFind)) {
               return entry.getKey();
           }
       }
        throw new NoSuchElementException("Value not found: " + keyToFind);
    }

    public String findValue(String valueToFind) {
        //value to find is the key
        String value = this.get(valueToFind);
        if (value == null) {
            throw new NoSuchElementException("Value not found: " + valueToFind);
        }
        else {
            return value;
        }
    }

    public Header getHeader() {
        return this;
    }

}
