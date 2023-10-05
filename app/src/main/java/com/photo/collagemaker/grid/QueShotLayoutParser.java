package com.photo.collagemaker.grid;

import android.graphics.RectF;

import com.photo.collagemaker.layer.slant.SlantCollageLayout;
import com.photo.collagemaker.layer.straight.StraightCollageLayout;

public class QueShotLayoutParser {
    private QueShotLayoutParser() {
    }

    public static QueShotLayout parse(final QueShotLayout.Info info) {
        QueShotLayout quShotLayout;
        if (info.type == 0) {
            quShotLayout = new StraightCollageLayout() {
                public QueShotLayout clone(QueShotLayout quShotLayout) {
                    return null;
                }

                public void layout() {
                    int size = info.steps.size();
                    int i = 0;
                    for (int i2 = 0; i2 < size; i2++) {
                        Step step = info.steps.get(i2);
                        switch (step.type) {
                            case 0:
                                addLine(step.position, step.lineDirection(), info.lines.get(i).getStartRatio());
                                break;
                            case 1:
                                addCross(step.position, step.hRatio, step.vRatio);
                                break;
                            case 2:
                                cutAreaEqualPart(step.position, step.hSize, step.vSize);
                                break;
                            case 3:
                                cutAreaEqualPart(step.position, step.part, step.lineDirection());
                                break;
                            case 4:
                                cutSpiral(step.position);
                                break;
                        }
                        i += step.numOfLine;
                    }
                }
            };
        } else {
            quShotLayout = new SlantCollageLayout() {
                public QueShotLayout clone(QueShotLayout quShotLayout) {
                    return null;
                }

                public void layout() {
                    int size = info.steps.size();
                    for (int i = 0; i < size; i++) {
                        Step step = info.steps.get(i);
                        switch (step.type) {
                            case 0:
                                addLine(step.position, step.lineDirection(), info.lines.get(i).getStartRatio(), info.lines.get(i).getEndRatio());
                                break;
                            case 1:
                                addCross(step.position, 0.5f, 0.5f, 0.5f, 0.5f);
                                break;
                            case 2:
                                cutArea(step.position, step.hSize, step.vSize);
                                break;
                        }
                    }
                }
            };
        }
        quShotLayout.setOuterBounds(new RectF(info.left, info.top, info.right, info.bottom));
        quShotLayout.layout();
        quShotLayout.setColor(info.color);
        quShotLayout.setRadian(info.radian);
        quShotLayout.setPadding(info.padding);
        int size = info.lineInfos.size();
        for (int i = 0; i < size; i++) {
            QueShotLayout.LineInfo lineInfo = info.lineInfos.get(i);
            QueShotLine line = quShotLayout.getLines().get(i);
            line.startPoint().x = lineInfo.startX;
            line.startPoint().y = lineInfo.startY;
            line.endPoint().x = lineInfo.endX;
            line.endPoint().y = lineInfo.endY;
        }
        quShotLayout.sortAreas();
        quShotLayout.update();
        return quShotLayout;
    }
}
