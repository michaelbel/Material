package org.app.material.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.app.material.AndroidUtilities;
import org.app.material.R;

import java.util.ArrayList;
import java.util.List;

public class ColorView extends FrameLayout {

    private final ColorMode colorMode;
    private IndicatorMode indicatorMode;
    private @ColorInt static int currentColor;
    public static int DEFAULT_COLOR;
    public final static ColorMode DEFAULT_MODE = ColorMode.RGB;
    public final static IndicatorMode DEFAULT_INDICATOR = IndicatorMode.DECIMAL;

    public ColorView(Context context) {
        this(DEFAULT_COLOR, DEFAULT_MODE, DEFAULT_INDICATOR, context);
    }

    public ColorView(@ColorInt int initialColor, final ColorMode colorMode, IndicatorMode indicatorMode, Context context) {
        super(context);

        AndroidUtilities.bind(context);

        this.indicatorMode = indicatorMode;
        this.colorMode = colorMode;
        currentColor = initialColor;

        DEFAULT_COLOR = AndroidUtilities.getThemeColor(R.attr.colorAccent);

        inflate(getContext(), R.layout.views, this);
        setClipToPadding(false);

        final View colorView = findViewById(R.id.color_view);
        colorView.setBackgroundColor(currentColor);

        List<Channel> channels = colorMode.getColorMode().getChannels();

        final List<ChannelView> channelViews = new ArrayList<>();

        for (Channel c : channels) {
            channelViews.add(new ChannelView(c, currentColor, indicatorMode, getContext()));
        }

        OnProgressChangedListener seekBarChangeListener = new OnProgressChangedListener() {
            @Override
            public void onProgressChanged() {
                List<Channel> channels = new ArrayList<>();

                for (ChannelView chan : channelViews) {
                    channels.add(chan.getChannel());
                }

                currentColor = colorMode.getColorMode().evaluateColor(channels);
                colorView.setBackgroundColor(currentColor);
            }
        };

        ViewGroup channelContainer = (ViewGroup) findViewById(R.id.channel_container);

        for (ChannelView c : channelViews) {
            channelContainer.addView(c);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) c.getLayoutParams();
            params.topMargin = AndroidUtilities.dp(16);
            params.bottomMargin = AndroidUtilities.dp(4);
            c.registerListener(seekBarChangeListener);
        }
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public static int getCurrentColor() {
        return currentColor;
    }

    public IndicatorMode getIndicatorMode() {
        return indicatorMode;
    }

    public class ChannelView extends RelativeLayout {

        private final Channel channel;
        private final IndicatorMode indicatorMode;
        private OnProgressChangedListener listener;

        public ChannelView(Channel channel, @ColorInt int color, IndicatorMode indicatorMode, Context context) {
            super(context);

            this.channel = channel;
            this.indicatorMode = indicatorMode;

            channel.setProgress(channel.getExtractor().extract(color));

            if (channel.getProgress() < channel.getMin() || channel.getProgress() > channel.getMax()) {
                throw new IllegalArgumentException("Initial progress for channel: " + channel.getClass().getSimpleName() + " must be between " + channel.getMin() + " and " + channel.getMax());
            }

            View rootView = inflate(context, R.layout.channel_row, this);
            bindViews(rootView);
        }

        private void bindViews(View rootView) {
            TextView label = (TextView) rootView.findViewById(R.id.label);
            label.setText(channel.getNameResourceId());
            final TextView progressView = (TextView) rootView.findViewById(R.id.progress_text);
            setProgress(progressView, channel.getProgress());
            SeekBar seekbar = (SeekBar) rootView.findViewById(R.id.seekbar);
            seekbar.setMax(channel.getMax());
            seekbar.setProgress(channel.getProgress());

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    channel.setProgress(progress);
                    setProgress(progressView, progress);

                    if (listener != null) {
                        listener.onProgressChanged();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        }

        private void setProgress(TextView view, int progress) {
            view.setText(indicatorMode == IndicatorMode.HEX ? Integer.toHexString(progress) : String.valueOf(progress));
        }

        public void registerListener(OnProgressChangedListener listener) {
            this.listener = listener;
        }

        public Channel getChannel() {
            return channel;
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            listener = null;
        }
    }

    public enum ColorMode {
        RGB, HSV, ARGB, CMYK, CMYK255, HSL;

        public AbstractColorMode getColorMode() {
            switch (this) {
                case RGB:
                    return new RGB();
                case HSV:
                    return new HSV();
                case ARGB:
                    return new ARGB();
                case CMYK:
                    return new CMYK();
                case CMYK255:
                    return new CMYK255();
                case HSL:
                    return new HSL();
                default:
                    return new RGB();
            }
        }

        public interface AbstractColorMode {
            int evaluateColor(List<Channel> channels);
            List<Channel> getChannels();
        }

        public class RGB implements AbstractColorMode {
            @Override
            public List<Channel> getChannels() {
                List<Channel> list = new ArrayList<>();

                list.add(new Channel("R", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.red(color);
                    }
                }));

                list.add(new Channel("G", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.green(color);
                    }
                }));

                list.add(new Channel("B", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.blue(color);
                    }
                }));

                return list;
            }

            @Override
            public int evaluateColor(List<Channel> channels) {
                return Color.rgb(
                        channels.get(0).getProgress(),
                        channels.get(1).getProgress(),
                        channels.get(2).getProgress());
            }
        }

        public class ARGB implements AbstractColorMode {
            @Override
            public List<Channel> getChannels() {
                List<Channel> list = new ArrayList<>();

                list.add(new Channel("A", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.alpha(color);
                    }
                }));

                list.add(new Channel("R", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.red(color);
                    }
                }));

                list.add(new Channel("G", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.green(color);
                    }
                }));

                list.add(new Channel("B", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Color.blue(color);
                    }
                }));

                return list;
            }

            @Override
            public int evaluateColor(List<Channel> channels) {
                return Color.argb(
                        channels.get(0).getProgress(),
                        channels.get(1).getProgress(),
                        channels.get(2).getProgress(),
                        channels.get(3).getProgress());
            }
        }

        public class HSL implements AbstractColorMode {
            float[] color2hsl(int color) {
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                return hsv2hsl(hsv);
            }

            float[] hsv2hsl(float[] hsv) {
                float a = hsv[0];
                float b = hsv[1];
                float c = hsv[2];
                return new float[] { a, b * c / ((a = (2 - b) * c) < 1 ? a : 2), a/2 };
            }

            float[] hsl2hsv(float[] hsl) {
                float a = hsl[0];
                float b = hsl[1];
                float c = hsl[2];
                b *= c < 0.5 ? c : 1 - c;
                return new float[] { a, 2 * b / (c + b), c + b };
            }

            @Override
            public List<Channel> getChannels() {
                List<Channel> list = new ArrayList<>();

                list.add(new Channel("H", 0, 360, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return (int) color2hsl(color)[0];
                    }
                }));

                list.add(new Channel("S", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 100 - (int) color2hsl(color)[1] * 100;
                    }
                }));

                list.add(new Channel("L", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return Math.abs(50 - (int) color2hsl(color)[2] * 100);
                    }
                }));

                return list;
            }

            @Override
            public int evaluateColor(List<Channel> channels) {

                float[] hsv = hsl2hsv(new float[]{
                        ((float) channels.get(0).getProgress()),
                        ((float) channels.get(1).getProgress()) / 100,
                        ((float) channels.get(2).getProgress()) / 100
                });

                return Color.HSVToColor(hsv);
            }
        }

        public class HSV implements AbstractColorMode {

            float[] colorToHSV(int color) {
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                return hsv;
            }

            @Override
            public List<Channel> getChannels() {
                List<Channel> list = new ArrayList<>();

                list.add(new Channel("H", 0, 360, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return (int) colorToHSV(color)[0];
                    }
                }));

                list.add(new Channel("S", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return (int) colorToHSV(color)[1] * 100;
                    }
                }));

                list.add(new Channel("V", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return (int) colorToHSV(color)[2] * 100;
                    }
                }));

                return list;
            }

            @Override
            public int evaluateColor(List<Channel> channels) {
                return Color.HSVToColor(new float[]{
                        ((float) channels.get(0).getProgress()),
                        ((float) channels.get(1).getProgress()) / 100,
                        ((float) channels.get(2).getProgress()) / 100
                });
            }
        }

        public class CMYK implements AbstractColorMode {
            @Override
            public List<Channel> getChannels() {
                List<Channel> list = new ArrayList<>();

                list.add(new Channel("C", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 100 - (int) (Color.red((int) (color * 2.55)) / 2.55);
                    }
                }));

                list.add(new Channel("M", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 100 - (int) (Color.green((int) (color * 2.55)) / 2.55);
                    }
                }));

                list.add(new Channel("Y", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 100 - (int) (Color.blue((int) (color * 2.55)) / 2.55);
                    }
                }));

                list.add(new Channel("K", 0, 100, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 100 - (int) (Color.alpha((int) (color * 2.55)) / 2.55);
                    }
                }));

                return list;
            }

            @Override
            public int evaluateColor(List<Channel> channels) {
                return Color.rgb(
                        convertToRGB(channels.get(0), channels.get(3)),
                        convertToRGB(channels.get(1), channels.get(3)),
                        convertToRGB(channels.get(2), channels.get(3)));
            }

            private int convertToRGB(Channel colorChan, Channel blackChan) {
                return (int)((255 - colorChan.getProgress() * 2.55) * (255 - blackChan.getProgress() * 2.55)) / 255;
            }
        }

        public class CMYK255 implements AbstractColorMode {
            @Override
            public List<Channel> getChannels() {
                List<Channel> list = new ArrayList<>();

                list.add(new Channel("C", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 255 - Color.red(color);
                    }
                }));

                list.add(new Channel("M", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 255 - Color.green(color);
                    }
                }));

                list.add(new Channel("Y", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 255 -  Color.blue(color);
                    }
                }));

                list.add(new Channel("K", 0, 255, new Channel.ColorExtractor() {
                    @Override
                    public int extract(int color) {
                        return 255 - Color.alpha(color);
                    }
                }));

                return list;
            }

            @Override
            public int evaluateColor(List<Channel> channels) {
                return Color.rgb(
                        convertToRGB(channels.get(0), channels.get(3)),
                        convertToRGB(channels.get(1), channels.get(3)),
                        convertToRGB(channels.get(2), channels.get(3)));
            }

            private int convertToRGB(Channel colorChan, Channel blackChan) {
                return ((255 - colorChan.getProgress()) * (255 - blackChan.getProgress())) / 255;
            }
        }
    }

    public static final class Channel {

        private final int mMin;
        private final int mMax;
        private int mProgress = 0;
        private final String mMode;
        private final ColorExtractor mExtractor;

        public Channel(String mode, int min, int max, ColorExtractor extractor) {
            this.mMode = mode;
            this.mMin = min;
            this.mMax = max;
            this.mExtractor = extractor;
        }

        public interface ColorExtractor {
            int extract(int color);
        }

        public String getNameResourceId() {
            return mMode;
        }

        public int getMin() {
            return mMin;
        }

        public int getMax() {
            return mMax;
        }

        public ColorExtractor getExtractor() {
            return mExtractor;
        }

        public int getProgress() {
            return mProgress;
        }

        public void setProgress(int progress) {
            this.mProgress = progress;
        }
    }

    public interface OnProgressChangedListener {
        void onProgressChanged();
    }

    public enum IndicatorMode {
        DECIMAL, HEX
    }
}