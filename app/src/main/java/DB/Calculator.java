package DB;

import java.util.*;

public final class Calculator {
    private static HashMap<String, Integer> calorieHash = new HashMap<>();

    static {
        calorieHash.put("걷기", Integer.parseInt("200"));
        calorieHash.put("달리기", Integer.parseInt("500"));
        calorieHash.put("자전거", Integer.parseInt("400"));
        calorieHash.put("줄넘기", Integer.parseInt("700"));
        calorieHash.put("수영", Integer.parseInt("500"));
    }

    public static List<List<Map.Entry<String, Integer>>> getExercisePerformCounts(int dayLength, int totalCalories, List<String> exerciseNames) {
        int firstExerciseIndex = 0;
        int secondExerciseIndex = 1;

        List<List<Map.Entry<String, Integer>>> exercisePerformCounts = new ArrayList<>();
        List<Integer> minutesLossCalories = new ArrayList<>();

        for (String exerciseName : exerciseNames) {
            minutesLossCalories.add(calorieHash.get(exerciseName) / 60);
        }

        for (int elapsedDay = 0; elapsedDay < dayLength; ++elapsedDay) {
            exercisePerformCounts.add(new ArrayList<Map.Entry<String, Integer>>());

            Map.Entry<Integer, Integer> exercisePerformCount
                    = solveIndeterminateEquation(minutesLossCalories.get(firstExerciseIndex), minutesLossCalories.get(secondExerciseIndex), totalCalories);

            exercisePerformCounts.get(elapsedDay).add(new AbstractMap.SimpleEntry<>(exerciseNames.get(firstExerciseIndex), exercisePerformCount.getKey()));

            if (exerciseNames.size() > 1) {
                exercisePerformCounts.get(elapsedDay).add(new AbstractMap.SimpleEntry<>(exerciseNames.get(secondExerciseIndex), exercisePerformCount.getValue()));
            }

            firstExerciseIndex = (firstExerciseIndex + 1) % exerciseNames.size();
            secondExerciseIndex = (secondExerciseIndex + 1) % exerciseNames.size();
        }

        return exercisePerformCounts;
    }

    /**
     * Solve an equation "ax + by = c".
     * @param a coefficient of x.
     * @param b coefficient of y.
     * @param c equation constant.
     * @return a solution that has the smallest difference between x and y.
     */
    public static Map.Entry<Integer, Integer> solveIndeterminateEquation(final int a, final int b, final int c) {
        double maxX = Math.ceil((double) c / a);

        List<Map.Entry<Integer, Integer>> solutions = new ArrayList<>();

        for (int x = 0; x < maxX; ++x) {
            int y = (int) Math.ceil((c - a * x) / (double) b);

            if (y < 0) {
                break;
            }

            solutions.add(new AbstractMap.SimpleEntry<>(x, y));
        }

        Collections.sort(solutions, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> left, Map.Entry<Integer, Integer> right) {
                int differenceCompareValue = Integer.valueOf(Math.abs(left.getKey() - left.getValue()))
                        .compareTo(Math.abs(right.getKey() - right.getValue()));

                if (differenceCompareValue != 0) {
                    return differenceCompareValue;
                }

                return Integer.valueOf(Math.abs(c - (a * left.getKey() + b * left.getValue())))
                        .compareTo(Math.abs(c - (a * right.getKey() + b * right.getValue())));
            }
        });

        return solutions.get(0);
    }

    public static double getLossCaloriesPerDay(String gender, String age, String height, String currentWeight, String targetWeight) {
        int convertedAge = Integer.parseInt(age);
        double convertedHeight = Double.parseDouble(height);
        double convertedCurrentWeight = Double.parseDouble(currentWeight);
        double convertedTargetWeight = Double.parseDouble(targetWeight);

        double lossCalories;
        if (gender.equals("남자")) {
            lossCalories = 662 - (9.53 * convertedAge) + 1.185 + (15.91 * convertedCurrentWeight) + (539.6 * convertedHeight * 0.0328) * 2.54 / 100;
        } else {
            lossCalories = 354 - (6.91 * convertedAge) + 1.185 + (9.36 * convertedCurrentWeight) + (726 * convertedHeight * 0.0328) * 2.54 / 100;
        }

        double weightDifference = convertedCurrentWeight - convertedTargetWeight;

        return Math.floor((0.85 * lossCalories) / (weightDifference * 3500) * (weightDifference * 3500));
    }

    private Calculator() {

    }
}
