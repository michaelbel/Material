package org.michaelbel.material.widget.ColorPicker;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public enum ColorMode {
    RGB,
    HSV,
    ARGB,
    CMYK,
    CMYK255,
    HSL;

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
                    channels.get(2).getProgress()
            );
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
                    channels.get(3).getProgress()
            );
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
                    convertToRGB(channels.get(2), channels.get(3))
            );
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
                    convertToRGB(channels.get(2), channels.get(3))
            );
        }

        private int convertToRGB(Channel colorChan, Channel blackChan) {
            return ((255 - colorChan.getProgress()) * (255 - blackChan.getProgress())) / 255;
        }
    }
}