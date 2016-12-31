package com.quaap.primary.partsofspeech;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.StdGameActivity;
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
        SubjectBaseActivity.AnswerTypedListener{


    private List<String> words;


    private String word;
    private String answer;

    private String [] unpluralMap;
    private Map<String,String> pluralsMap;


    public PluralActivity() {
        super(R.layout.std_plural_prob);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        words = Arrays.asList(getResources().getStringArray(R.array.common_nouns));

        unpluralMap = getResources().getStringArray(R.array.unplural);

        pluralsMap = arrayPairsToMap(getResources().getStringArray(R.array.plurals));


    }

    private Map<String,String> arrayPairsToMap(String [] array) {

        if (array.length%2!=0) {
            throw new IllegalArgumentException("array to map must have even number of elements");
        }
        Map<String,String> map = new TreeMap<>();
        for (int j=0; j<array.length; j+=2) {
            map.put(array[j], array[j+1]);
        }
        return map;
    }

    @Override
    protected void onPause() {


        saveValue("word", word);
        super.onPause();
    }


    @Override
    protected void onResume() {

        super.onResume();


    }


    @Override
    protected void onShowLevel() {
        super.onShowLevel();

        if (((PluralLevel)getLevel()).getInputMode()==InputMode.Buttons) {
            setFasttimes(800, 1600, 3000);
        } else {
            setFasttimes(1500, 2200, 5000);
        }

    }

    @Override
    protected void showProbImpl() {

        PluralLevel level = (PluralLevel)getLevel();
        word = getSavedValue("word", (String)null);
        if (word==null) {
            int tries = 0;
            do {
                do {
                    word = words.get(getRand(words.size() - 1));
                } while (!pluralsMap.containsKey(word) );
            } while ( tries++ < 100 && ( word.length() > level.getMaxWordLength() || seenProblem(word) ) );
        } else {
            deleteSavedValue("word");
        }

        answer = pluralsMap.get(word);
        Log.d("plural", word + " -> " + answer);

        TextView plural = (TextView)findViewById(R.id.txtplural);
        plural.setText(word);



        if (level.getInputMode() == InputMode.Buttons) {
            List<String> answers = getAnswerChoices(answer);

            makeChoiceButtons(getAnswerArea(), answers, this);

        } else if (level.getInputMode() == InputMode.Input) {

            makeInputBox(getAnswerArea(), getKeysArea(), this, INPUTTYPE_TEXT, 5, 0);
        } else {
            throw new IllegalArgumentException("Unknown inputMode! " + level.getInputMode());
        }


    }

    @Override
    public boolean answerTyped(String answer) {
        return answerGiven(answer);
    }

    @Override
    public boolean answerGiven(String answer) {

        int points = 0;
        boolean isright = answer.toLowerCase().trim().equals(this.answer.toLowerCase());
        if (isright) {
            points = (int)(1 + word.length() * (levelnum+1) );
        }
        answerDone(isright, points, word, this.answer, answer.trim());

        if (isright) {

        }
        return isright;
    }



    private final int numanswers = 4;



    protected List<String> getAnswerChoices(String realanswer) {
        List<String> answers = new ArrayList<>();
        answers.add(realanswer);
        int maxtries = unpluralMap.length;
        int tries = 0;
        do {
            String badspell;
            tries = 0;
            do {
                badspell = unplural(word);
            } while (tries++<maxtries && answers.contains(badspell));
            if (tries<maxtries) {
                answers.add(badspell);
            }

        } while (answers.size()<numanswers && tries<maxtries);

        Collections.shuffle(answers);
        return answers;
    }



    public String unplural(String word) {
        List<String> words = new ArrayList<>();

        for (int j=0; j<1; j++) {
            int i = ((int) (Math.random() * ((unpluralMap.length-1) / 2 )) * 2);
            word = word.replaceFirst(unpluralMap[i], unpluralMap[i + 1]);
        }
        return word;
    }


}
