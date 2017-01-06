package com.quaap.primary.partsofspeech.plurals;

/**
 * Created by tom on 12/15/16.
 * <p>
 * Copyright (C) 2016   Tom Kliethermes
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.StdGameActivity;
import com.quaap.primary.base.StdLevel;
import com.quaap.primary.base.SubjectBaseActivity;
import com.quaap.primary.base.component.InputMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PluralActivity extends StdGameActivity
        implements SubjectBaseActivity.AnswerGivenListener<String>,
        SubjectBaseActivity.AnswerTypedListener {


    private final int numanswers = 4;

    private List<String> words;
    private String word;
    private String answer;
    private String[] unpluralMap;
    private Map<String, String> pluralsMap;
    private String[] wordScores;

    public PluralActivity() {
        super(R.layout.std_plural_prob);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        words = Arrays.asList(getResources().getStringArray(R.array.common_nouns));

        unpluralMap = getResources().getStringArray(R.array.unplural);

        wordScores = getResources().getStringArray(R.array.plural_word_scores);

        pluralsMap = arrayPairsToMap(getResources().getStringArray(R.array.plurals));


    }

    private Map<String, String> arrayPairsToMap(String[] array) {

        if (array.length % 2 != 0) {
            throw new IllegalArgumentException("array to map must have even number of elements");
        }
        Map<String, String> map = new TreeMap<>();
        for (int j = 0; j < array.length; j += 2) {
            map.put(array[j], array[j + 1]);
        }
        return map;
    }

    @Override
    protected void onPause() {


        saveLevelValue("word", word);
        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (isLandscape() && ((StdLevel) getLevel()).getInputMode() == InputMode.Input) {
            LinearLayout problem_area = (LinearLayout) findViewById(R.id.problem_area);
            problem_area.setOrientation(LinearLayout.HORIZONTAL);
        }

    }

    @Override
    protected void onShowLevel() {
        super.onShowLevel();


    }

    @Override
    protected void onShowProbImpl() {

        PluralLevel level = (PluralLevel) getLevel();
        word = getSavedLevelValue("word", (String) null);
        if (word == null) {
            int tries = 0;
            do {
                int score;
                do {
                    word = words.get(getRand(words.size() - 1));
                    score = scoreWord(word);
                    //System.out.println(word + " " + score + " " + pluralsMap.containsKey(word));
                }
                while (!pluralsMap.containsKey(word) || (getRand(10) > 2 && score < 2) || (getRand(10) > 3 && score < 3)); //try to get tricky words

            }
            while (tries++ < 100 && (word.length() < level.getMinWordLength() || word.length() > level.getMaxWordLength() || seenProblem(word)));
        } else {
            deleteSavedLevelValue("word");
        }

        int score = scoreWord(word);
        if (((PluralLevel) getLevel()).getInputMode() == InputMode.Buttons) {
            setFasttimes(800 + score*100, 900 + score*250, 1000 + score*320);
        } else {
            setFasttimes(900 + score*100, 1100 + score*300, 1400 + score*500);
        }


        answer = pluralsMap.get(word);
        Log.d("plural", word + " -> " + answer);

        TextView plural = (TextView) findViewById(R.id.txtplural);
        plural.setText(capitalize(word));


        final TextView hint = (TextView) findViewById(R.id.plurHint);
        hint.setText("");

        if (level.getInputMode() == InputMode.Buttons) {
            List<String> answers = getAnswerChoices(answer);

            makeChoiceButtons(getAnswerArea(), answers, this);

        } else if (level.getInputMode() == InputMode.Input) {

            makeInputBox(getAnswerArea(), getKeysArea(), this, INPUTTYPE_TEXT, 5, 0, word);

            startHint(word.length());

        } else {
            throw new IllegalArgumentException("Unknown inputMode! " + level.getInputMode());
        }

    }

    @Override
    public boolean onAnswerTyped(String answer) {
        return onAnswerGiven(answer);
    }

    @Override
    public boolean onAnswerGiven(String answer) {

        boolean isright = answer.toLowerCase().trim().equals(this.answer.toLowerCase());

        answerDone(isright, word, this.answer, answer.trim());

        return isright;
    }

    /**
     *  Calculate the points from the current problem
     *  By default this is based on simply the level number.
     *
     *  Override this in each activity.
     *
     *  Range, in general:
     *    difficulty 1:   1 - 50
     *    difficulty 2:   up to 100
     *    difficulty 3:   up to 200
     *    difficulty 4:   up to 500
     *    difficulty 5:   up to 1000
     *
     * @return the points for the current problem
     */
    @Override
    protected int onCalculatePoints() {
        float scoremult = answer.length() - getHintTicks();

        if (scoremult<=0) {  //if the hint is fully shown, give partial credit.
            scoremult=.3f;
        }
        return super.onCalculatePoints() + (int) (1 + word.length() * scoreWord(word) * scoremult);
    }


    protected List<String> getAnswerChoices(String realanswer) {
        List<String> answers = new ArrayList<>();
        answers.add(realanswer);
        int maxtries = unpluralMap.length;
        int tries;
        do {
            String badspell;
            tries = 0;
            do {
                badspell = unplural(word);
            } while (tries++ < maxtries && answers.contains(badspell));
            if (tries < maxtries) {
                answers.add(badspell);
            }

        } while (answers.size() < numanswers && tries < maxtries);

        Collections.shuffle(answers);
        return answers;
    }

    @Override
    protected void onPerformHint(int hintTick) {
        final TextView hint = (TextView) findViewById(R.id.plurHint);
        if (hintTick < answer.length()) {

            hint.setText(answer.substring(0, hintTick+1));

        } else {
            cancelHint();
        }
        super.onPerformHint(hintTick);

    }

    public String unplural(String word) {

        for (int j = 0; j < 1; j++) {
            int i = ((int) (Math.random() * ((unpluralMap.length - 1) / 2)) * 2);
            word = word.replaceFirst(unpluralMap[i], unpluralMap[i + 1]);
        }
        return word;
    }

    private int scoreWord(String candidate) {

        for (int difflevel = wordScores.length - 1; difflevel >= 0; difflevel--) {
            if (candidate.matches(wordScores[difflevel])) {
                return difflevel + 1;
            }

        }

        return 1;
    }

}
