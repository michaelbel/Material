package org.app.material.FabSheet.java;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import org.app.material.R;

import java.lang.reflect.Method;

import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;

public class MaterialSheetFab<FAB extends View & AnimatedFab> {

	//private static final boolean IS_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	private static final int ANIMATION_SPEED = 1;
	private static final int SHEET_ANIM_DURATION = 400 * ANIMATION_SPEED;
	private static final int SHOW_SHEET_COLOR_ANIM_DURATION = (int) (SHEET_ANIM_DURATION * 0.75);
	private static final int HIDE_SHEET_COLOR_ANIM_DURATION = (int) (SHEET_ANIM_DURATION * 1.5);
	private static final int FAB_ANIM_DURATION = 300 * ANIMATION_SPEED;
	private static final int SHOW_OVERLAY_ANIM_DURATION = MaterialSheetFab.SHOW_SHEET_ANIM_DELAY + SHEET_ANIM_DURATION;
	private static final int HIDE_OVERLAY_ANIM_DURATION = SHEET_ANIM_DURATION;
	private static final int SHOW_SHEET_ANIM_DELAY = (int) (FAB_ANIM_DURATION * 0.5);
	private static final int MOVE_FAB_ANIM_DELAY = (int) (SHEET_ANIM_DURATION * 0.3);
	private static final float FAB_SCALE_FACTOR = 0.6f;
	private static final int FAB_ARC_DEGREES = 0;
	protected FAB fab;
	protected FabAnimation fabAnimation;
	protected MaterialSheetAnimation sheetAnimation;
	protected OverlayAnimation overlayAnimation;
	protected int anchorX;
	protected int anchorY;
	private boolean isShowing;
	private boolean isHiding;
	private boolean hideSheetAfterSheetIsShown;
	private MaterialSheetFabEventListener eventListener;

	public enum RevealXDirection {
		LEFT, RIGHT
	}

	public enum RevealYDirection {
		UP, DOWN
	}

	public MaterialSheetFab(FAB fab, View sheet, View overlay, int sheetColor, int fabColor) {
		Interpolator interpolator = AnimationUtils.loadInterpolator(sheet.getContext(), R.interpolator.msf_interpolator);
		this.fab = fab;
		fabAnimation = new FabAnimation(fab, interpolator);
		sheetAnimation = new MaterialSheetAnimation(sheet, sheetColor, fabColor, interpolator);
		overlayAnimation = new OverlayAnimation(overlay, interpolator);
		sheet.setVisibility(View.INVISIBLE);
		overlay.setVisibility(View.GONE);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showSheet();
			}
		});
		overlay.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (isSheetVisible() && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					hideSheet();
				}
				return true;
			}
		});

		fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						MaterialSheetFab.this.fab.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						updateFabAnchor();
					}
				});
	}

	public void showFab() {
		showFab(0, 0);
	}

	public void showFab(float translationX, float translationY) {
		setFabAnchor(translationX, translationY);
		if (!isSheetVisible()) {
			fab.show(translationX, translationY);
		}
	}

	public void showSheet() {
		if (isAnimating()) {
			return;
		}

		isShowing = true;
		overlayAnimation.show(SHOW_OVERLAY_ANIM_DURATION, null);

		morphIntoSheet(new AnimationListener() {
			@Override
			public void onEnd() {
				if (eventListener != null) {
					eventListener.onSheetShown();
				}

				isShowing = false;

				if (hideSheetAfterSheetIsShown) {
					hideSheet();
					hideSheetAfterSheetIsShown = false;
				}
			}
		});

		if (eventListener != null) {
			eventListener.onShowSheet();
		}
	}

	public void hideSheet() {
		hideSheet(null);
	}

	protected void hideSheet(final AnimationListener endListener) {
		if (isAnimating()) {
			if (isShowing) {
				hideSheetAfterSheetIsShown = true;
			}

			return;
		}

		isHiding = true;
		overlayAnimation.hide(HIDE_OVERLAY_ANIM_DURATION, null);

		morphFromSheet(new AnimationListener() {
			@Override
			public void onEnd() {
				if (endListener != null) {
					endListener.onEnd();
				}
				if (eventListener != null) {
					eventListener.onSheetHidden();
				}

				isHiding = false;
			}
		});

		if (eventListener != null) {
			eventListener.onHideSheet();
		}
	}

	public void hideSheetThenFab() {
		AnimationListener listener = new AnimationListener() {
			@Override
			public void onEnd() {
				fab.hide();
			}
		};

		if (isSheetVisible()) {
			hideSheet(listener);
		} else {
			listener.onEnd();
		}
	}

	protected void morphIntoSheet(final AnimationListener endListener) {
		updateFabAnchor();
		sheetAnimation.alignSheetWithFab(fab);

		fabAnimation.morphIntoSheet(sheetAnimation.getSheetRevealCenterX(),
				sheetAnimation.getSheetRevealCenterY(fab),
				getFabArcSide(sheetAnimation.getRevealXDirection()), FAB_ARC_DEGREES,
				FAB_SCALE_FACTOR, FAB_ANIM_DURATION, null);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				fab.setVisibility(View.INVISIBLE);
				sheetAnimation.morphFromFab(fab, SHEET_ANIM_DURATION, SHOW_SHEET_COLOR_ANIM_DURATION, endListener);
			}
		}, SHOW_SHEET_ANIM_DELAY);
	}

	protected void morphFromSheet(final AnimationListener endListener) {
		sheetAnimation.morphIntoFab(fab, SHEET_ANIM_DURATION, HIDE_SHEET_COLOR_ANIM_DURATION, null);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				sheetAnimation.setSheetVisibility(View.INVISIBLE);
				fabAnimation.morphFromSheet(anchorX, anchorY, getFabArcSide(sheetAnimation.getRevealXDirection()), FAB_ARC_DEGREES, -FAB_SCALE_FACTOR, FAB_ANIM_DURATION, endListener);
			}
		}, MOVE_FAB_ANIM_DELAY);
	}

	protected void updateFabAnchor() {
		setFabAnchor(fab.getTranslationX(), fab.getTranslationY());
	}

	protected void setFabAnchor(float translationX, float translationY) {
		anchorX = Math.round(fab.getX() + (fab.getWidth() / 2) + (translationX - fab.getTranslationX()));
		anchorY = Math.round(fab.getY() + (fab.getHeight() / 2) + (translationY - fab.getTranslationY()));
	}

	private Side getFabArcSide(RevealXDirection revealXDirection) {
		if (revealXDirection == RevealXDirection.LEFT) {
			return Side.LEFT;
		} else {
			return Side.RIGHT;
		}
	}

	private boolean isAnimating() {
		return isShowing || isHiding;
	}

	public boolean isSheetVisible() {
		return sheetAnimation.isSheetVisible();
	}

	public void setEventListener(MaterialSheetFabEventListener eventListener) {
		this.eventListener = eventListener;
	}

    public class OverlayAnimation {

        protected View overlay;
        protected Interpolator interpolator;

        public OverlayAnimation(View overlay, Interpolator interpolator) {
            this.overlay = overlay;
            this.interpolator = interpolator;
        }

        public void show(long duration, final AnimationListener listener) {
            overlay.animate().alpha(1).setDuration(duration).setInterpolator(interpolator)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            overlay.setVisibility(View.VISIBLE);

                            if (listener != null) {
                                listener.onStart();
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (listener != null) {
                                listener.onEnd();
                            }
                        }
                    }).start();
        }

        public void hide(long duration, final AnimationListener listener) {
            overlay.animate().alpha(0).setDuration(duration).setInterpolator(interpolator)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (listener != null) {
                                listener.onStart();
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            overlay.setVisibility(View.GONE);
                            if (listener != null) {
                                listener.onEnd();
                            }
                        }
                    }).start();
        }
    }

    public class FabAnimation {

        private View fab;
        private Interpolator interpolator;

        public FabAnimation(View fab, Interpolator interpolator) {
            this.fab = fab;
            this.interpolator = interpolator;
        }

        public void morphIntoSheet(int endX, int endY, Side side, int arcDegrees, float scaleFactor, long duration, AnimationListener listener) {
            morph(endX, endY, side, arcDegrees, scaleFactor, duration, listener);
        }

        public void morphFromSheet(int endX, int endY, Side side, int arcDegrees, float scaleFactor, long duration, AnimationListener listener) {
            fab.setVisibility(View.VISIBLE);
            morph(endX, endY, side, arcDegrees, scaleFactor, duration, listener);
        }

        protected void morph(float endX, float endY, Side side, float arcDegrees, float scaleFactor, long duration, AnimationListener listener) {
            startArcAnim(fab, endX, endY, arcDegrees, side, duration, interpolator, listener);
            fab.animate().scaleXBy(scaleFactor).scaleYBy(scaleFactor).setDuration(duration).setInterpolator(interpolator).start();
        }

        protected void startArcAnim(View view, float endX, float endY, float degrees, Side side, long duration, Interpolator interpolator, final AnimationListener listener) {
            ArcAnimator anim = ArcAnimator.createArcAnimator(view, (int) endX, (int) endY, degrees, side);
            anim.setDuration(duration);
            anim.setInterpolator(interpolator);

            anim.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                    if (listener != null) {
                        listener.onStart();
                    }
                }

                @Override
                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                    if (listener != null) {
                        listener.onEnd();
                    }
                }
            });

            anim.start();
        }
    }

    public class MaterialSheetAnimation {

        private static final String SUPPORT_CARDVIEW_CLASSNAME = "android.support.v7.widget.CardView";
        private static final int SHEET_REVEAL_OFFSET_Y = 5;

        private View sheet;
        private int sheetColor;
        private int fabColor;
        private Interpolator interpolator;
        private MaterialSheetFab.RevealXDirection revealXDirection;
        private MaterialSheetFab.RevealYDirection revealYDirection;
        private Method setCardBackgroundColor;
        private boolean isSupportCardView;

        public MaterialSheetAnimation(View sheet, int sheetColor, int fabColor, Interpolator interpolator) {
            this.sheet = sheet;
            this.sheetColor = sheetColor;
            this.fabColor = fabColor;
            this.interpolator = interpolator;
            revealXDirection = MaterialSheetFab.RevealXDirection.LEFT;
            revealYDirection = MaterialSheetFab.RevealYDirection.UP;
            isSupportCardView = sheet.getClass().getName().equals(SUPPORT_CARDVIEW_CLASSNAME);

            if (isSupportCardView) {
                try {
                    setCardBackgroundColor = sheet.getClass().getDeclaredMethod("setCardBackgroundColor", int.class);
                } catch (Exception e) {
                    setCardBackgroundColor = null;
                }
            }
        }

        public void alignSheetWithFab(View fab) {
            int[] fabCoords = new int[2];
            fab.getLocationOnScreen(fabCoords);

            int[] sheetCoords = new int[2];
            sheet.getLocationOnScreen(sheetCoords);

            int leftDiff = sheetCoords[0] - fabCoords[0];
            int rightDiff = (sheetCoords[0] + sheet.getWidth()) - (fabCoords[0] + fab.getWidth());
            int topDiff = sheetCoords[1] - fabCoords[1];
            int bottomDiff = (sheetCoords[1] + sheet.getHeight()) - (fabCoords[1] + fab.getHeight());

            ViewGroup.MarginLayoutParams sheetLayoutParams = (ViewGroup.MarginLayoutParams) sheet.getLayoutParams();

            if (rightDiff != 0) {
                float sheetX = sheet.getX();

                if (rightDiff <= sheetX) {
                    sheet.setX(sheetX - rightDiff - sheetLayoutParams.rightMargin);
                    revealXDirection = MaterialSheetFab.RevealXDirection.LEFT;
                } else if (leftDiff != 0 && leftDiff <= sheetX) {
                    sheet.setX(sheetX - leftDiff + sheetLayoutParams.leftMargin);
                    revealXDirection = MaterialSheetFab.RevealXDirection.RIGHT;
                }
            }

            if (bottomDiff != 0) {
                float sheetY = sheet.getY();

                if (bottomDiff <= sheetY) {
                    sheet.setY(sheetY - bottomDiff - sheetLayoutParams.bottomMargin);
                    revealYDirection = MaterialSheetFab.RevealYDirection.UP;
                } else if (topDiff != 0 && topDiff <= sheetY) {
                    sheet.setY(sheetY - topDiff + sheetLayoutParams.topMargin);
                    revealYDirection = MaterialSheetFab.RevealYDirection.DOWN;
                }
            }
        }

        public void morphFromFab(View fab, long showSheetDuration, long showSheetColorDuration, AnimationListener listener) {
            sheet.setVisibility(View.VISIBLE);
            revealSheetWithFab(fab, getFabRevealRadius(fab), getSheetRevealRadius(), showSheetDuration, fabColor, sheetColor, showSheetColorDuration, listener);
        }

        public void morphIntoFab(View fab, long hideSheetDuration, long hideSheetColorDuration, AnimationListener listener) {
            revealSheetWithFab(fab, getSheetRevealRadius(), getFabRevealRadius(fab), hideSheetDuration, sheetColor, fabColor, hideSheetColorDuration, listener);
        }

        protected void revealSheetWithFab(View fab, float startRadius, float endRadius, long sheetDuration, int startColor, int endColor, long sheetColorDuration, AnimationListener listener) {
            if (listener != null) {
                listener.onStart();
            }

            AnimationListener revealListener = (sheetDuration >= sheetColorDuration) ? listener : null;
            AnimationListener colorListener = (sheetColorDuration > sheetDuration) ? listener : null;
            startCircularRevealAnim(sheet, getSheetRevealCenterX(), getSheetRevealCenterY(fab), startRadius, endRadius, sheetDuration, interpolator, revealListener);
            startColorAnim(sheet, startColor, endColor, sheetColorDuration, interpolator, colorListener);
        }

        protected void startCircularRevealAnim(View view, int centerX, int centerY, float startRadius, float endRadius, long duration, Interpolator interpolator, final AnimationListener listener) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int relativeCenterX = (int) (centerX - view.getX());
                int relativeCenterY = (int) (centerY - view.getY());
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(view, relativeCenterX, relativeCenterY, startRadius, endRadius);
                anim.setDuration(duration);
                anim.setInterpolator(interpolator);

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (listener != null) {
                            listener.onStart();
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (listener != null) {
                            listener.onEnd();
                        }
                    }
                });

                anim.start();
            } else {
                SupportAnimator anim = org.app.material.FabSheet.java.ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
                anim.setDuration((int) duration);
                anim.setInterpolator(interpolator);

                anim.addListener(new SupportAnimator.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                        if (listener != null) {
                            listener.onStart();
                        }
                    }

                    @Override
                    public void onAnimationEnd() {
                        if (listener != null) {
                            listener.onEnd();
                        }
                    }
                });

                anim.start();
            }
        }

        protected void startColorAnim(final View view, final int startColor, final int endColor, long duration, Interpolator interpolator, final AnimationListener listener) {
            ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
            anim.setDuration(duration);
            anim.setInterpolator(interpolator);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (listener != null) {
                        listener.onStart();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null) {
                        listener.onEnd();
                    }
                }
            });
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    Integer color = (Integer) animator.getAnimatedValue();

                    if (isSupportCardView) {
                        if (setCardBackgroundColor != null) {
                            try {
                                setCardBackgroundColor.invoke(sheet, color);
                            } catch (Exception ignored) {}
                        }
                    } else {
                        view.setBackgroundColor(color);
                    }
                }
            });

            anim.start();
        }

        public void setSheetVisibility(int visibility) {
            sheet.setVisibility(visibility);
        }

        public boolean isSheetVisible() {
            return sheet.getVisibility() == View.VISIBLE;
        }

        public int getSheetRevealCenterX() {
            return (int) (sheet.getX() + (sheet.getWidth() / 2));
        }

        public int getSheetRevealCenterY(View fab) {
            if (revealYDirection == MaterialSheetFab.RevealYDirection.UP) {
                return (int) (sheet.getY() + (sheet.getHeight() * (SHEET_REVEAL_OFFSET_Y - 1) / SHEET_REVEAL_OFFSET_Y) - (fab.getHeight() / 2));
            }

            return (int) (sheet.getY() + (sheet.getHeight() / SHEET_REVEAL_OFFSET_Y) + (fab.getHeight() / 2));
        }

        protected float getSheetRevealRadius() {
            return Math.max(sheet.getWidth(), sheet.getHeight());
        }

        protected float getFabRevealRadius(View fab) {
            return Math.max(fab.getWidth(), fab.getHeight()) / 2;
        }

        public MaterialSheetFab.RevealXDirection getRevealXDirection() {
            return revealXDirection;
        }

        public MaterialSheetFab.RevealYDirection getRevealYDirection() {
            return revealYDirection;
        }
    }
}