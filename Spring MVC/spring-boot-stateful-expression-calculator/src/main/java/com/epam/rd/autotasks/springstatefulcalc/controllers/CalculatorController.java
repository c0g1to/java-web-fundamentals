package com.epam.rd.autotasks.springstatefulcalc.controllers;

import com.epam.rd.autotasks.springstatefulcalc.services.Calculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/calc")
public class CalculatorController {

    private int statusCode;

    @PutMapping("/expression")
    public ResponseEntity<String> setExpression(HttpSession session, @RequestBody String body) {
        if (Pattern.compile("[a-zA-Z]{2,}").matcher(body).find()) {
            statusCode = 400;
        } else {
            if (session.getAttribute("expression") == null) {
                statusCode = 201;
            } else {
                statusCode = 200;
            }
            session.setAttribute("expression", body);
        }
        return ResponseEntity.status(statusCode).build();
    }

    @PutMapping("/{variable}")
    public ResponseEntity<String> setVariable(HttpSession session, @PathVariable String variable, @RequestBody String body) {
        if (!variable.equals("expression") && !variable.equals("result")) {
            if (Calculator.isNumeric(body)) {
                if (Integer.parseInt(body) >= -10000 && Integer.parseInt(body) <= 10000) {
                    if (session.getAttribute(variable) == null) {
                        statusCode = 201;
                    } else {
                        statusCode = 200;
                    }
                } else {
                    return ResponseEntity.status(403).build();
                }
            } else {
                statusCode = 400;
            }
            session.setAttribute(variable, body);
            return ResponseEntity.status(statusCode).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/result")
    public ResponseEntity<String> getResult(HttpSession session) {
        String result = "bad result";
        if (session.getAttribute("expression") == null) {
            statusCode = 409;
        } else {
            HashMap<String, String> data = new HashMap<>();
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String key = attributeNames.nextElement();
                data.put(key, session.getAttribute(key).toString());
            }
            result = new Calculator(data).getResult();
            if (Calculator.isNumeric(result)) {
                statusCode = 200;
            } else {
                statusCode = 409;
            }
        }
        return ResponseEntity.status(statusCode).body(result);
    }

    @DeleteMapping("/{object}")
    public ResponseEntity<String> setVariable(HttpSession session, @PathVariable String object) {
        session.removeAttribute(object);
        return ResponseEntity.status(204).build();
    }
}
