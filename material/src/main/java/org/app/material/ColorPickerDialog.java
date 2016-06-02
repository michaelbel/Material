package org.app.material;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerDialog extends DialogFragment {

    private final static String ARG_INITIAL_COLOR = "arg_initial_color";
    private final static String ARG_COLOR_MODE_ID = "arg_color_mode_id";
    private final static String ARG_INDICATOR_MODE = "arg_indicator_mode";

    private ChromaView chromaView;

    public enum IndicatorMode {
        DECIMAL, HEX
    }







    private static ColorPickerDialog newInstance(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode) {
        ColorPickerDialog fragment = new ColorPickerDialog();
        fragment.setArguments(makeArgs(initialColor, colorMode, indicatorMode));
        return fragment;
    }

    private static Bundle makeArgs(@ColorInt int initialColor, ColorMode colorMode, IndicatorMode indicatorMode) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_COLOR, initialColor);
        args.putInt(ARG_COLOR_MODE_ID, colorMode.ordinal());
        args.putInt(ARG_INDICATOR_MODE, indicatorMode.ordinal());
        return args;
    }

    public static class Builder {

        private @ColorInt
        int initialColor = ChromaView.DEFAULT_COLOR;
        private ColorMode colorMode = ChromaView.DEFAULT_MODE;
        private IndicatorMode indicatorMode = IndicatorMode.DECIMAL;

        public Builder initialColor(@ColorInt int initialColor) {
            this.initialColor = initialColor;
            return this;
        }

        public Builder colorMode(ColorMode colorMode) {
            this.colorMode = colorMode;
            return this;
        }

        public Builder indicatorMode(IndicatorMode indicatorMode) {
            this.indicatorMode = indicatorMode;
            return this;
        }

        public ColorPickerDialog create() {
            ColorPickerDialog fragment;
            fragment = newInstance(initialColor, colorMode, indicatorMode);
            return fragment;
        }
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            chromaView = new ChromaView(
                    getArguments().getInt(ARG_INITIAL_COLOR),
                    ColorMode.values()[getArguments().getInt(ARG_COLOR_MODE_ID)],
                    IndicatorMode.values()[getArguments().getInt(ARG_INDICATOR_MODE)],
                    getActivity());
        } else {
            chromaView = new ChromaView(
                    savedInstanceState.getInt(ARG_INITIAL_COLOR, ChromaView.DEFAULT_COLOR),
                    ColorMode.values()[savedInstanceState.getInt(ARG_COLOR_MODE_ID)],
                    IndicatorMode.values()[savedInstanceState.getInt(ARG_INDICATOR_MODE)],
                    getActivity());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(chromaView);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), Integer.toHexString(ChromaView.getCurrentColor()), Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(makeArgs(ChromaView.getCurrentColor(), chromaView.getColorMode(), chromaView.getIndicatorMode()));
        super.onSaveInstanceState(outState);
    }

    public static class ChromaView extends RelativeLayout {

        public final static int DEFAULT_COLOR = Color.GRAY;
        public final static ColorMode DEFAULT_MODE = ColorMode.RGB;
        public final static IndicatorMode DEFAULT_INDICATOR = IndicatorMode.DECIMAL;
        private final ColorMode colorMode;
        private @ColorInt
        static int currentColor;
        private IndicatorMode indicatorMode;

        public ChromaView(Context context) {
            this(DEFAULT_COLOR, DEFAULT_MODE, DEFAULT_INDICATOR, context);
        }

        public ChromaView(@ColorInt int initialColor, final ColorMode colorMode, IndicatorMode indicatorMode, Context context) {
            super(context);

            this.indicatorMode = indicatorMode;
            this.colorMode = colorMode;
            currentColor = initialColor;

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
                params.topMargin = AndroidUtilities.dp(getContext(), 16);
                params.bottomMargin = AndroidUtilities.dp(getContext(), 4);
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
                    throw new IllegalArgumentException(
                            "Initial progress for channel: " + channel.getClass().getSimpleName() + " must be between " + channel.getMin() + " and " + channel.getMax());
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

        private final int min;
        private final int max;
        private int progress = 0;
        private final String mode;
        private final ColorExtractor extractor;

        public Channel(String mode, int min, int max, ColorExtractor extractor) {
            this.mode = mode;
            this.min = min;
            this.max = max;
            this.extractor = extractor;
        }

        //public Channel(String mode, int min, int max, int progress, ColorExtractor extractor) {
        //    this.mode = mode;
        //    this.min = min;
        //    this.max = max;
        //    this.extractor = extractor;
        //    this.progress = progress;
        //}

        public interface ColorExtractor {
            int extract(int color);
        }

        public String getNameResourceId() {
            return mode;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public ColorExtractor getExtractor() {
            return extractor;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    public interface OnProgressChangedListener {
        void onProgressChanged();
    }
}