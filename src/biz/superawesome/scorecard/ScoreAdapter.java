package biz.superawesome.scorecard;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import biz.superawesome.scorecard.model.Score;

class ScoreAdapter extends ArrayAdapter<Score> {

	OnItemSelectedListener activity;
	
	public ScoreAdapter(Context context, Score[] scores) {
		super(context, R.layout.item_score, Arrays.asList(scores));
		activity = (OnItemSelectedListener) context; // callback for the spinner#onItemSelected is in the HoleActivity
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_score, null);
		}
		Score score = getItem(position);

		TextView playerName = (TextView) convertView
				.findViewById(R.id.name_textview);
		playerName.setText(score.player.toString());

		Spinner scoreSpinner = (Spinner) convertView
				.findViewById(R.id.score_spinner);
		List<Integer> possibleScores = Arrays.asList(new Integer[] { 0, 1, 2,
				3, 4, 5, 6, 7 });
		ArrayAdapter adapter = new ArrayAdapter(parent.getContext(),
				android.R.layout.simple_spinner_item, possibleScores);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		scoreSpinner.setAdapter(adapter);
		Integer currentScore = possibleScores.indexOf(score.score);
		scoreSpinner.setSelection(currentScore);
		scoreSpinner.setOnItemSelectedListener(activity);		// callback for selecting score
		scoreSpinner.setTag(score); // so we have the score to update in the callback
		return convertView;
	}

}