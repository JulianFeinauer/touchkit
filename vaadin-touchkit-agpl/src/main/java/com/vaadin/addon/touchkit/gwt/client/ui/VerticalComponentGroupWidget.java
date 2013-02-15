package com.vaadin.addon.touchkit.gwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class VerticalComponentGroupWidget extends ComplexPanel {

    static class WidgetWrapper extends DivElement {
        
        protected WidgetWrapper() {}
        
        public static final String ROW_CLASSNAME = SHORT_CLASSNAME + "-row";
        public static final String ROW_WITH_CAPTION_STYLENAME = ROW_CLASSNAME
                + "-cap";
        public static final String ROW_WITHOUT_CAPTION_STYLENAME = ROW_CLASSNAME
                + "-nocap";
        public static final String ROW_WITH_FULLSIZE_WIDGET_STYLENAME = ROW_CLASSNAME
                + "-full";
        public static final String CELL_CLASSNAME = SHORT_CLASSNAME + "-cell";

        static final WidgetWrapper create() {
            WidgetWrapper w = Document.get().createDivElement().cast();
            w.init();
            return w;
        }

        public final void init() {
            setClassName(ROW_CLASSNAME + " " + ROW_WITHOUT_CAPTION_STYLENAME);
            setInnerHTML("<div class=\"v-caption\"></div><div class=\"v-touchkit-componentgroup-cell\"></div>");
        }

        public final Element getWidgetCell() {
            return getLastChild().cast();
        }

        public final boolean setCaption(String caption, String iconUrl) {
            DivElement captionElement = getFirstChildElement().cast();

            boolean hasIcon = iconUrl != null;
            boolean needsCaption = hasIcon
                    || (caption != null && !caption.isEmpty());

            if (needsCaption) {
                String captionHtml = hasIcon ? "<img class=\"v-icon\" src=\""
                        + iconUrl + "\"/>" : "";
                captionHtml += caption == null ? "" : caption;
                captionElement.setInnerHTML(captionHtml);
                captionElement.getStyle().setProperty("display", null);
                setClassName(ROW_CLASSNAME + " " + ROW_WITH_CAPTION_STYLENAME);
            } else {
                setClassName(ROW_CLASSNAME + " " + ROW_WITHOUT_CAPTION_STYLENAME);
            }
            return needsCaption;
        }

        /**
         * TODO verify it this is needed!!
         * 
         * @param fullSizeWidget
         */
        public final void setFullSizeWidget(boolean fullSizeWidget) {
            UIObject.setStyleName(this, ROW_WITH_FULLSIZE_WIDGET_STYLENAME, fullSizeWidget);
        }
    }

    private static final String CLASSNAME = "v-touchkit-verticalcomponentgroup";
    private static final String SHORT_CLASSNAME = "v-touchkit-componentgroup";

    protected List<Widget> widgets = new ArrayList<Widget>();

    public VerticalComponentGroupWidget() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
    }

    private WidgetWrapper getRowElement(Widget widget) {
        com.google.gwt.dom.client.Element parentElement = widget.getElement()
                .getParentElement();
        return (WidgetWrapper) (parentElement == null ? null : parentElement
                .getParentElement().cast());
    }

    /**
     * Add or move widget to given position
     * 
     * @param widget
     * @param index
     * @param componentWidth
     * @param caption
     * @param iconUrl
     */
    public void addOrMove(final Widget widget, int index) {
        if (widgets.contains(widget)) {
            if (widgets.indexOf(widget) == index) {
                return;
            } else {
                remove(widget);
                addWidget(widget, index);
            }
        } else {
            addWidget(widget, index);
        }
    }

    /**
     * Add widget to given position
     * 
     * @param widget
     * @param index
     * @param componentWidth
     * @param caption
     * @param iconUrl
     */
    public void addWidget(final Widget widget, int index) {
        if (widgets.contains(widget)) {
            return;
        }

        WidgetWrapper row = WidgetWrapper.create();

        if (index < 0 || index >= widgets.size()) {
            getElement().appendChild(row);
            widgets.add(widget);
        } else {
            getElement().insertBefore(row, getRowElement(widgets.get(index)));
            widgets.add(index, widget);
        }

        add(widget, row.getWidgetCell());
    }
    
    

    /**
     * Adds Widget to group
     * 
     * @param widget
     */
    public void add(final Widget widget) {
        addWidget(widget, -1);
    }

    public boolean remove(Widget widget) {
        if (!widgets.contains(widget)) {
            return false;
        }
        DivElement element = getRowElement(widget);

        boolean ret = super.remove(widget);

        if (ret) {
            element.removeFromParent();
            widgets.remove(widget);
        }

        return ret;
    }

    @Override
    public void clear() {
        for (Widget child : widgets) {
            remove(child);
        }
    }

    public void updateCaption(Widget child, String caption, String url,
            String width) {
        WidgetWrapper row = getRowElement(child);
        boolean hasCaptionOrIcon = row.setCaption(caption, url);
        row.setFullSizeWidget(hasCaptionOrIcon && "100.0%".equals(width));
    }

}
