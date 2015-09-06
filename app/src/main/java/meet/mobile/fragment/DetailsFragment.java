package meet.mobile.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import meet.mobile.R;
import meet.mobile.activity.BaseActivity;
import meet.mobile.model.Image;

/**
 * Created by Filip on 2015-09-06.
 */
public class DetailsFragment extends Fragment {

	private static final String TAG = DetailsFragment.class.getSimpleName();

	private static final String ARG_IMAGE = "image";

	private Image image;

	@InjectView(R.id.play)
	ImageView play;
	@InjectView(R.id.image)
	ImageView img;
	@InjectView(R.id.details)
	TextView details;
	@InjectView(R.id.author)
	TextView author;
	@InjectView(R.id.date_created)
	TextView dateCreated;

	public static DetailsFragment newInstance(Image image) {
		DetailsFragment fragment = new DetailsFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_IMAGE, image);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		readArguments();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_details, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.inject(this, view);
		setupDetailsView();
	}

	@Override
	public void onResume() {
		super.onResume();
		setupActionBar();
	}

	private void setupActionBar() {
		ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		if (supportActionBar != null) {
			supportActionBar.setDisplayShowHomeEnabled(false);
			supportActionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void setupDetailsView() {
		author.setText(image.getArtist());
		details.setText(image.getCaption());

		String dateFormat = formatDateCreated();
		dateCreated.setText(dateFormat);

		ImageLoader.getInstance().cancelDisplayTask(img);
		ImageLoader.getInstance().displayImage(image.getDisplayByType(Image.DisplaySizeType.PREVIEW).getUri(), img, ((BaseActivity) getActivity()).getDisplayImageOptions());

		img.setOnClickListener(view -> Toast.makeText(getActivity(), "Playing", Toast.LENGTH_SHORT).show());
	}

	@NonNull
	private String formatDateCreated() {
		SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
		SimpleDateFormat toFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault());
		String dateFormat;
		try {
			Date date = fromFormat.parse(image.getDateCreated());
			dateFormat = toFormat.format(date);
		} catch (ParseException e) {
			Log.d(TAG, "Error parsing date, using default");
			dateFormat = toFormat.format(new Date());
		}
		return dateFormat;
	}

	private void readArguments() {
		image = getArguments().getParcelable(ARG_IMAGE);
	}
}
