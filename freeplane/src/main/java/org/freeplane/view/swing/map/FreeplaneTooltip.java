package org.freeplane.view.swing.map;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import org.freeplane.core.resources.ResourceController;
import org.freeplane.features.mode.Controller;
import org.freeplane.view.swing.features.filepreview.IViewerFactory;
import org.freeplane.view.swing.features.filepreview.ImageTooltipRendererFactory;
import org.freeplane.view.swing.features.filepreview.ViewerController;

@SuppressWarnings("serial")
public class FreeplaneTooltip extends JToolTip {
	public static final String TEXT_HTML = "text/html";

	private GraphicsConfiguration graphicsConfiguration;
	private String contentType;
	private URL baseUrl;

	public FreeplaneTooltip(GraphicsConfiguration graphicsConfiguration, String contentType){
		this.graphicsConfiguration = graphicsConfiguration;
		this.contentType = contentType;
	}

	@Override
    public void setTipText(String tipText) {
		final Dimension tooltipSize = tooltipSize(graphicsConfiguration);
		try {
			final URI uri = new URI(tipText);
			final IViewerFactory viewerFactory = Controller.getCurrentModeController().getExtension(ViewerController.class).getViewerFactory();
			if (viewerFactory.accept(uri)) {
				final URI absoluteUri = uri.isAbsolute() ? uri : baseUrl.toURI().resolve(uri);
				final JComponent imageViewer = new ImageTooltipRendererFactory (viewerFactory, absoluteUri, tooltipSize).getTooltipRenderer();
				if(imageViewer != null)
					add(imageViewer);
				return;
			}

		}
		catch (URISyntaxException e) {
			// fall through
		}
		final TextualTooltipRendererFactory tooltipScrollPaneFactory = new TextualTooltipRendererFactory(graphicsConfiguration, contentType, baseUrl, tipText, getComponent(), tooltipSize);
		add(tooltipScrollPaneFactory.getTooltipRenderer());
	}

	private Dimension tooltipSize(GraphicsConfiguration graphicsConfiguration) {
		final Rectangle screenBounds = graphicsConfiguration.getBounds();
		final int screenHeigth = screenBounds.height - 80;
		final int screenWidth = screenBounds.width - 80;
		final int maximumHeight = Math.min(screenHeigth, getIntProperty("toolTipManager.max_tooltip_height"));
		int maximumWidth = Math.min(screenWidth, getIntProperty("toolTipManager.max_tooltip_width"));
		final Dimension maximumSize = new Dimension(maximumWidth, maximumHeight);
		return maximumSize;
	}
	private int getIntProperty(String propertyName) {
		return ResourceController.getResourceController().getIntProperty(propertyName, Integer.MAX_VALUE);
	}

	@Override
    public Dimension getPreferredSize() {
	    final Component renderer = getComponent(0);
		return renderer.getPreferredSize();
    }

	@Override
    public void layout() {
		final Component renderer = getComponent(0);
		renderer.setSize(getPreferredSize());
	    super.layout();
    }

	public void setBase(URL url) {
		this.baseUrl = url;
	}

}
