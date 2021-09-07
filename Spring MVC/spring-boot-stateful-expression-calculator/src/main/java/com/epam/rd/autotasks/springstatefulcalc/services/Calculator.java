package com.epam.rd.autotasks.springstatefulcalc.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Calculator {

    private static final Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private final HashMap<String, String> variables;
    private final String expression;
    private final String result;

    public Calculator(HashMap<String, String> data) {
        this.expression = data.remove("expression").replace(" ", "");
        this.variables = fixVariables(data);
        result = calcLoop(getNumericExpression(), false);
    }

    public String getResult() {
        return result;
    }

    private HashMap<String, String> fixVariables(HashMap<String, String> variables) {
        while (true) {
            List<Map.Entry<String, String>> list = variables.entrySet().stream().filter(pair -> !isNumeric(pair.getValue())).collect(Collectors.toList());
            if (list.isEmpty()) break;
            list.forEach(pair -> variables.put(pair.getKey(), variables.get(pair.getValue())));
        }
        return variables;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return numberPattern.matcher(strNum).matches();
    }

    private String getNumericExpression() {
        String expression = this.expression;
        for (Map.Entry<String, String> pair : variables.entrySet()) {
            expression = expression.replace(pair.getKey(), pair.getValue());
        }
        return expression;
    }

    private String calcLoop(String exp, boolean isSimple) {
        if (isSimple) {
            Matcher matcher = Pattern.compile("\\d[\\*\\/\\+\\-]").matcher(exp);
            matcher.find();
            int leftValue = Integer.parseInt(exp.substring(0, matcher.start() + 1));
            int rightValue = Integer.parseInt(exp.substring(matcher.start() + 2));
            int result = 0;
            switch (exp.charAt(matcher.start() + 1)) {
                case '*':
                    result = leftValue * rightValue;
                    break;

                case '/':
                    result = leftValue / rightValue;
                    break;

                case '+':
                    result = leftValue + rightValue;
                    break;

                case '-':
                    result = leftValue - rightValue;
                    break;
            }
            return String.valueOf(result);
        } else {
            Matcher matcher = Pattern.compile("[\\(\\)]").matcher(exp);
            while (matcher.find()) {
                int opBr = matcher.start();
                int enBr = calcEndingBracket(matcher);
                exp = exp.substring(0, opBr) + calcLoop(exp.substring(opBr + 1, enBr), false) + exp.substring(enBr + 1);
                matcher = Pattern.compile("[\\(\\)]").matcher(exp);
            }

            matcher = Pattern.compile("\\-?\\d+\\s*[\\*\\/]\\s*\\-?\\d+").matcher(exp);
            while (matcher.find()) {
                exp = matcher.replaceFirst(calcLoop(matcher.group(), true));
                matcher = Pattern.compile("\\-?\\d+\\s*[\\*\\/]\\s*\\-?\\d+").matcher(exp);
            }

            matcher = Pattern.compile("\\-?\\d+\\s*[\\+\\-]\\s*\\-?\\d+").matcher(exp);
            while (matcher.find()) {
                exp = matcher.replaceFirst(calcLoop(matcher.group(), true));
                matcher = Pattern.compile("\\-?\\d+\\s*[\\+\\-]\\s*\\-?\\d+").matcher(exp);
            }
        }
        return exp;
    }

    private int calcEndingBracket(Matcher matcher) {
        int bracketFlag = 1;
        while (bracketFlag != 0) {
            matcher.find();
            if (matcher.group().equals("(")) {
                ++bracketFlag;
            } else {
                --bracketFlag;
            }
        }
        return matcher.start();
    }
}
