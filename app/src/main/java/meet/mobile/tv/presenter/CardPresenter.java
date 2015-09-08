package meet.mobile.tv.presenter;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import meet.mobile.R;
import meet.mobile.application.MeetMobileTvApplication;
import meet.mobile.model.Image;

public class CardPresenter extends Presenter {

    private static int CARD_WIDTH = 150;
    private static int CARD_HEIGHT = 150;

    private static Context mContext;
    protected static final DisplayImageOptions sDisplayImageOptions;

    static {
        sDisplayImageOptions = MeetMobileTvApplication.getDefaultImageOptions()
                .showImageOnLoading(R.color.accent)
                .displayer(new FadeInBitmapDisplayer(150))
                .build();
    }

    static class ViewHolder extends Presenter.ViewHolder {

        private ImageCardView mCardView;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
        }


        public ImageCardView getCardView() {
            return mCardView;
        }

        protected void updateCardViewImage(String url, ImageView imageView) {
            ImageLoader.getInstance().cancelDisplayTask(imageView);
            ImageLoader.getInstance().displayImage(url, imageView, sDisplayImageOptions);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        ((TextView) cardView.findViewById(R.id.content_text)).setTextColor(Color.LTGRAY);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        Image image = (Image) o;
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.mCardView.setTitleText(image.getTitle());
        holder.mCardView.setContentText(image.getArtist());
        holder.mCardView.setMainImageDimensions(CARD_WIDTH * 2, CARD_HEIGHT * 2);
        holder.updateCardViewImage(
                image.getDisplayByType(Image.DisplaySizeType.THUMB).getUri(),
                holder.getCardView().getMainImageView()
        );
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

}