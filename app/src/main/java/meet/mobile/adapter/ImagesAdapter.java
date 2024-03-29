package meet.mobile.adapter;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import meet.mobile.R;
import meet.mobile.model.Image;

import static meet.mobile.utils.Utils.isLandscape;


/**
 * Created by Filip Zymek on 2015-06-22.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageCard> {

    final List<Image> images = new ArrayList<>();
    final TimeInterpolator interpolator = new DecelerateInterpolator(2);
    final SparseBooleanArray animatedPositions = new SparseBooleanArray();
    final Point windowSize = new Point();
    final DisplayImageOptions options;
    Context context;
    int lastPosition = 0;
    private OnImageClickListener clickListener;
    public ImagesAdapter(Context context, DisplayImageOptions options) {
        this.context = context;
        this.options = options;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(windowSize);

    }

    public void setClickListener(OnImageClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ImageCard onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.image_card, parent, false);
        return new ImageCard(v);
    }

    @Override
    public void onBindViewHolder(final ImageCard holder, int position) {
        holder.artist.setText(getItem(position).getArtist());
        holder.title.setText(getItem(position).getTitle());
        holder.clickListener = this.clickListener;
        ImageLoader.getInstance().cancelDisplayTask(holder.image);
        ImageLoader.getInstance().displayImage(getItem(position).getDisplayByType(Image.DisplaySizeType.PREVIEW).getUri(), holder.image, options);

        if (position > lastPosition && !animatedPositions.get(position)) {
            lastPosition = position;
            animatedPositions.put(position, true);
            startItemAnimation(holder, position);
        }
    }

    private void startItemAnimation(ImageCard holder, int position) {
        setupCommonItemProperties(holder);

        if (isLandscape(context)) {
            setupHorizontalItemProperties(holder, position);
        } else {
            setupVerticalItemProperties(holder);
        }

        holder.itemView.animate().translationX(0)
                .translationY(0)
                .rotationX(0)
                .rotationY(0)
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setDuration(500)
                .setInterpolator(interpolator)
                .setStartDelay(0)
                .start();
    }

    private Image getItem(int position) {
        return images.get(position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
        notifyDataSetChanged();
    }

    public void clear() {
        this.images.clear();
        lastPosition = 0;
        animatedPositions.clear();
        notifyDataSetChanged();
    }

    private void setupCommonItemProperties(ImageCard holder) {
        holder.itemView.setTranslationX(0);
        holder.itemView.setTranslationY(windowSize.y);
        holder.itemView.setScaleX(0.6f);
        holder.itemView.setScaleY(0.6f);
        holder.itemView.setAlpha(0);
    }

    private void setupHorizontalItemProperties(ImageCard holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setRotationY(45.0f);
        } else {
            holder.itemView.setRotationY(-45.0f);
        }
    }

    private void setupVerticalItemProperties(ImageCard holder) {
        holder.itemView.setRotationX(45.0f);
    }

    public interface OnImageClickListener {
        void onClick(int position, Image image);
    }

    protected class ImageCard extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.image)
        ImageView image;
        @InjectView(R.id.artist)
        TextView artist;
        @InjectView(R.id.title)
        TextView title;
        OnImageClickListener clickListener;

        public ImageCard(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                int pos = getPosition();
                clickListener.onClick(pos, getItem(pos));
            }

        }
    }
}
