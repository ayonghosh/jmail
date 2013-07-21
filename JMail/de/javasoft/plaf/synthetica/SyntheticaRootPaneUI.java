package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.SyntheticaPainter;
import de.javasoft.util.OS;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

public class SyntheticaRootPaneUI extends BasicRootPaneUI
{
  private Window window;
  private JRootPane rootPane;
  private LayoutManager layoutManager;
  private LayoutManager oldLayoutManager;
  private MouseInputListener mouseInputListener;
  private WindowListener windowListener;
  private ComponentListener windowResizeListener;
  private JComponent titlePane;
  public static final boolean EVAL_COPY = false;
  public static final int EVAL_HEIGHT = 0;
  public static final String EVAL_TEXT = "";

  public static ComponentUI createUI(JComponent paramJComponent)
  {
    return new SyntheticaRootPaneUI();
  }

  public static final boolean isEvalCopy()
  {
    return true;
  }

  public void installUI(JComponent paramJComponent)
  {
    super.installUI(paramJComponent);
    this.rootPane = ((JRootPane)paramJComponent);
    if (isDecorated(this.rootPane))
      installClientDecorations(this.rootPane);
  }

  public void uninstallUI(JComponent paramJComponent)
  {
    super.uninstallUI(paramJComponent);
    uninstallClientDecorations(this.rootPane);
    this.rootPane = null;
  }

  private void installClientDecorations(JRootPane paramJRootPane)
  {
    SyntheticaTitlePane localSyntheticaTitlePane = new SyntheticaTitlePane(paramJRootPane, this);
    setTitlePane(paramJRootPane, localSyntheticaTitlePane);
    installBorder(paramJRootPane);
    installWindowListeners(paramJRootPane, paramJRootPane.getParent());
    installLayout(paramJRootPane);
  }

  private void uninstallClientDecorations(JRootPane paramJRootPane)
  {
    if ((this.titlePane != null) && (this.titlePane instanceof SyntheticaTitlePane))
      ((SyntheticaTitlePane)this.titlePane).uninstallListeners(paramJRootPane);
    setTitlePane(paramJRootPane, null);
    uninstallBorder(paramJRootPane);
    uninstallWindowListeners(paramJRootPane);
    uninstallLayout(paramJRootPane);
  }

  public JComponent getTitlePane()
  {
    return this.titlePane;
  }

  void installBorder(JRootPane paramJRootPane)
  {
    if (!isDecorated(paramJRootPane))
      return;
    paramJRootPane.setBorder(new Border()
    {
      public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        SynthContext localSynthContext = SyntheticaLookAndFeel.createContext((JComponent)paramComponent, Region.ROOT_PANE, 0);
        SyntheticaPainter.getInstance().paintRootPaneBorder(localSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
      }

      public Insets getBorderInsets(Component paramComponent)
      {
        if ((SyntheticaRootPaneUI.this.window instanceof Frame) && 
          ((((Frame)SyntheticaRootPaneUI.this.window).getExtendedState() & 0x6) == 6))
          return new Insets(0, 0, 0, 0);
        Insets localInsets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.size", SyntheticaRootPaneUI.this.window);
        if (localInsets == null) {
          localInsets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.insets", SyntheticaRootPaneUI.this.window);
        }

        localInsets = (Insets)localInsets.clone();
        localInsets.bottom += 16;

        return localInsets;
      }

      public boolean isBorderOpaque()
      {
        return false;
      }
    });
  }

  private void uninstallBorder(JRootPane paramJRootPane)
  {
    paramJRootPane.setBorder(null);
  }

  private void installWindowListeners(JRootPane paramJRootPane, Component paramComponent)
  {
    this.window = ((paramComponent instanceof Window) ? (Window)paramComponent : SwingUtilities.getWindowAncestor(paramComponent));
    if (this.window == null)
      return;
    if (this.mouseInputListener == null)
      this.mouseInputListener = new MouseInputHandler();
    this.window.addMouseListener(this.mouseInputListener);
    this.window.addMouseMotionListener(this.mouseInputListener);

    if ((this.windowListener == null) && (!SyntheticaLookAndFeel.isWindowOpacityEnabled(this.window)))
    {
      if (OS.getCurrentOS() == OS.Mac) {
        SyntheticaLookAndFeel.setWindowOpaque(this.window, false);
      }
      this.windowListener = new WindowAdapter()
      {
        public void windowOpened(WindowEvent paramWindowEvent)
        {
          Window localWindow = paramWindowEvent.getWindow();
          if (SyntheticaLookAndFeel.isWindowOpacityEnabled(SyntheticaRootPaneUI.this.window)) {
            return;
          }
          SyntheticaLookAndFeel.setWindowOpaque(localWindow, false);

          if (!SyntheticaLookAndFeel.getBoolean("Synthetica.window.contentPane.opaque", SyntheticaRootPaneUI.this.window, true))
            return;
          if ((localWindow instanceof JDialog) && (((JDialog)localWindow).getContentPane() instanceof JComponent))
            ((JComponent)((JDialog)localWindow).getContentPane()).setOpaque(true);
          else if ((localWindow instanceof JFrame) && (((JFrame)localWindow).getContentPane() instanceof JComponent))
            ((JComponent)((JFrame)localWindow).getContentPane()).setOpaque(true);
        }
      };
      this.window.addWindowListener(this.windowListener);
    }

    if ((this.windowResizeListener != null) || (!SyntheticaLookAndFeel.isWindowShapeEnabled(this.window)))
      return;
    if (OS.getCurrentOS() == OS.Mac) {
      SyntheticaLookAndFeel.updateWindowShape(this.window);
    }
    else {
      this.windowResizeListener = new ComponentAdapter()
      {
        public void componentResized(ComponentEvent paramComponentEvent)
        {
          Window localWindow = (Window)paramComponentEvent.getComponent();
          SyntheticaLookAndFeel.updateWindowShape(localWindow);
        }
      };
      this.window.addComponentListener(this.windowResizeListener);
    }
  }

  private void uninstallWindowListeners(JRootPane paramJRootPane)
  {
    if (this.window != null)
    {
      this.window.removeMouseListener(this.mouseInputListener);
      this.window.removeMouseMotionListener(this.mouseInputListener);
      this.window.removeWindowListener(this.windowListener);
      this.window.removeComponentListener(this.windowResizeListener);
    }
    this.mouseInputListener = null;
    this.windowListener = null;
    this.windowResizeListener = null;
    this.window = null;
  }

  private void installLayout(JRootPane paramJRootPane)
  {
    if (this.layoutManager == null)
      this.layoutManager = new SyntheticaRootLayout();
    this.oldLayoutManager = paramJRootPane.getLayout();
    paramJRootPane.setLayout(this.layoutManager);
  }

  private void uninstallLayout(JRootPane paramJRootPane)
  {
    if (this.oldLayoutManager != null)
      paramJRootPane.setLayout(this.oldLayoutManager);
    this.oldLayoutManager = null;
    this.layoutManager = null;
  }

  private void setTitlePane(JRootPane paramJRootPane, JComponent paramJComponent)
  {
    JLayeredPane localJLayeredPane = paramJRootPane.getLayeredPane();
    if (this.titlePane != null)
    {
      this.titlePane.setVisible(false);
      localJLayeredPane.remove(this.titlePane);
    }
    if (paramJComponent != null)
    {
      localJLayeredPane.add(paramJComponent, JLayeredPane.FRAME_CONTENT_LAYER);
      paramJComponent.setVisible(true);
    }
    this.titlePane = paramJComponent;
  }

  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
  {
    super.propertyChange(paramPropertyChangeEvent);
    String str = paramPropertyChangeEvent.getPropertyName();
    if (str == null) return;

    if (str.equals("windowDecorationStyle"))
    {
      uninstallClientDecorations(this.rootPane);
      if (isDecorated(this.rootPane))
        installClientDecorations(this.rootPane);
    } else {
      if (!str.equals("ancestor"))
        return;
      uninstallWindowListeners(this.rootPane);
      if (isDecorated(this.rootPane))
        installWindowListeners(this.rootPane, this.rootPane.getParent());
    }
  }

  public void setMaximizedBounds(Frame paramFrame)
  {
    if (SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.fullscreen")) {
      return;
    }
    GraphicsConfiguration localGraphicsConfiguration = paramFrame.getGraphicsConfiguration();
    Rectangle localRectangle1 = localGraphicsConfiguration.getBounds();
    if (!SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.respectScreenBoundsX")) {
      localRectangle1.x = 0;
    }
    if (!SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.respectScreenBoundsY")) {
      localRectangle1.y = 0;
    }
    Insets localInsets = Toolkit.getDefaultToolkit().getScreenInsets(localGraphicsConfiguration);

    if ((!SyntheticaLookAndFeel.isSystemPropertySet("synthetica.frame.disableAutoHideTaskBarCorrection")) && (localInsets.bottom == 0)) {
      localInsets.bottom += 1;
    }
    Rectangle localRectangle2 = new Rectangle(localRectangle1.x + localInsets.left, 
      localRectangle1.y + localInsets.top, 
      localRectangle1.width - (localInsets.left + localInsets.right), 
      localRectangle1.height - (localInsets.top + localInsets.bottom));

    paramFrame.setMaximizedBounds(localRectangle2);
  }

  private boolean isDecorated(JRootPane paramJRootPane)
  {
    return paramJRootPane.getWindowDecorationStyle() != 0;
  }

  private class MouseInputHandler
    implements MouseInputListener
  {
    private static final int WINDOW_MOVE = 1;
    private static final int WINDOW_RESIZE = 2;
    private int windowAction;
    private int dragXOffset;
    private int dragYOffset;
    private Dimension dragDimension;
    private int resizeType;
    private int minimumYPos;
    private Frame frame;
    private Dialog dialog;

    private MouseInputHandler()
    {
      this.frame = null;
      this.dialog = null;

      if (SyntheticaRootPaneUI.this.window instanceof Frame)
        this.frame = ((Frame)SyntheticaRootPaneUI.this.window);
      else if (SyntheticaRootPaneUI.this.window instanceof Dialog)
        this.dialog = ((Dialog)SyntheticaRootPaneUI.this.window);
    }

    public void mousePressed(MouseEvent paramMouseEvent)
    {
      if (SyntheticaRootPaneUI.this.isDecorated(SyntheticaRootPaneUI.this.rootPane))
        return;
      SyntheticaRootPaneUI.this.window.toFront();

      this.minimumYPos = SyntheticaRootPaneUI.this.window.getGraphicsConfiguration().getBounds().y;

      Point localPoint1 = paramMouseEvent.getPoint();
      Point localPoint2 = SwingUtilities.convertPoint(SyntheticaRootPaneUI.this.window, localPoint1, SyntheticaRootPaneUI.this.titlePane);
      int i = position2Cursor(SyntheticaRootPaneUI.this.window, paramMouseEvent.getX(), paramMouseEvent.getY());

      if ((i == 0) && (SyntheticaRootPaneUI.this.titlePane != null) && 
        (SyntheticaRootPaneUI.this.titlePane.contains(localPoint2)) && ((
        (this.dialog != null) || ((this.frame != null) && (this.frame.getExtendedState() != 6)))))
      {
        this.windowAction = 1;
        this.dragXOffset = localPoint1.x;
        this.dragYOffset = localPoint1.y;
      }
      else {
        if (!isWindowResizable())
          return;
        this.windowAction = 2;
        this.dragXOffset = localPoint1.x;
        this.dragYOffset = localPoint1.y;
        this.dragDimension = new Dimension(SyntheticaRootPaneUI.this.window.getWidth(), SyntheticaRootPaneUI.this.window.getHeight());
        this.resizeType = position2Cursor(SyntheticaRootPaneUI.this.window, localPoint1.x, localPoint1.y);
      }
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if ((this.windowAction == 2) && (!SyntheticaRootPaneUI.this.window.isValid()))
      {
        SyntheticaRootPaneUI.this.window.validate();
        SyntheticaRootPaneUI.this.rootPane.repaint();
      }
      this.windowAction = -1;
      SyntheticaRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
    }

    public void mouseMoved(MouseEvent paramMouseEvent)
    {
      if (SyntheticaRootPaneUI.this.isDecorated(SyntheticaRootPaneUI.this.rootPane)) {
        return;
      }
      int i = position2Cursor(SyntheticaRootPaneUI.this.window, paramMouseEvent.getX(), paramMouseEvent.getY());

      if ((i != 0) && (isWindowResizable()))
        SyntheticaRootPaneUI.this.window.setCursor(Cursor.getPredefinedCursor(i));
      else
        SyntheticaRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
    }

    public void mouseEntered(MouseEvent paramMouseEvent)
    {
      mouseMoved(paramMouseEvent);
    }

    public void mouseExited(MouseEvent paramMouseEvent)
    {
      SyntheticaRootPaneUI.this.window.setCursor(Cursor.getDefaultCursor());
    }

    public void mouseDragged(MouseEvent paramMouseEvent)
    {
      GraphicsConfiguration localGraphicsConfiguration = SyntheticaRootPaneUI.this.window.getGraphicsConfiguration();
      Insets localInsets = Toolkit.getDefaultToolkit().getScreenInsets(localGraphicsConfiguration);
      this.minimumYPos = (localGraphicsConfiguration.getBounds().y + localInsets.top);
      boolean bool = SyntheticaLookAndFeel.isSystemPropertySet("synthetica.window.respectMinimumYPos");
      Point localPoint;
      if (this.windowAction == 1)
      {
        localPoint = paramMouseEvent.getPoint();
        SwingUtilities.convertPointToScreen(localPoint, (Component)paramMouseEvent.getSource());
        localPoint.x -= this.dragXOffset;
        localPoint.y -= this.dragYOffset;

        if ((bool) && (localPoint.y < this.minimumYPos))
          localPoint.y = this.minimumYPos;
        SyntheticaRootPaneUI.this.window.setLocation(localPoint);
      }
      else {
        if (this.windowAction != 2) {
          return;
        }
        localPoint = paramMouseEvent.getPoint();
        Dimension localDimension = (Dimension)SyntheticaLookAndFeel.get("Synthetica.rootPane.minimumWindowSize", SyntheticaRootPaneUI.this.window);
        if (localDimension == null)
          localDimension = SyntheticaRootPaneUI.this.window.getMinimumSize();
        Rectangle localRectangle1 = SyntheticaRootPaneUI.this.window.getBounds();
        Rectangle localRectangle2 = new Rectangle(localRectangle1);

        if ((this.resizeType == 11) || 
          (this.resizeType == 7) || 
          (this.resizeType == 5))
        {
          localRectangle1.width = Math.max(localDimension.width, this.dragDimension.width + localPoint.x - this.dragXOffset);
        }

        if ((this.resizeType == 9) || 
          (this.resizeType == 4) || 
          (this.resizeType == 5))
        {
          localRectangle1.height = Math.max(localDimension.height, this.dragDimension.height + localPoint.y - this.dragYOffset);
        }
        int i;
        if ((this.resizeType == 8) || 
          (this.resizeType == 6) || 
          (this.resizeType == 7))
        {
          i = localPoint.y - this.dragYOffset;
          localRectangle1.y += i;
          localRectangle1.height -= i;
          if (localRectangle1.height < localDimension.height)
          {
            localRectangle1.y += localRectangle1.height - localDimension.height;
            localRectangle1.height = localDimension.height;
          }
        }

        if ((this.resizeType == 10) || 
          (this.resizeType == 6) || 
          (this.resizeType == 4))
        {
          i = localPoint.x - this.dragXOffset;
          localRectangle1.x += i;
          localRectangle1.width -= i;
          if (localRectangle1.width < localDimension.width)
          {
            localRectangle1.x += localRectangle1.width - localDimension.width;
            localRectangle1.width = localDimension.width;
          }

        }

        if ((bool) && (localRectangle1.y < this.minimumYPos)) {
          localRectangle1.y = this.minimumYPos;
        }
        if (localRectangle1.equals(localRectangle2))
        {
          return;
        }

        SyntheticaRootPaneUI.this.window.setBounds(localRectangle1);
      }
    }

    public void mouseClicked(MouseEvent paramMouseEvent)
    {
      if (this.frame == null) return;
      Point localPoint = SwingUtilities.convertPoint(SyntheticaRootPaneUI.this.window, paramMouseEvent.getPoint(), SyntheticaRootPaneUI.this.titlePane);
      if ((SyntheticaRootPaneUI.this.titlePane == null) || (!SyntheticaRootPaneUI.this.titlePane.contains(localPoint)) || 
        (paramMouseEvent.getClickCount() != 2) || 
        ((paramMouseEvent.getModifiers() & 0x10) != 16)) {
        return;
      }
      if ((this.frame.isResizable()) && (isFrameResizable()))
      {
        ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).maximize();
      }
      else
      {
        if ((!this.frame.isResizable()) || (isFrameResizable()))
          return;
        ((SyntheticaTitlePane)SyntheticaRootPaneUI.this.titlePane).restore();
      }
    }

    private int position2Cursor(Window paramWindow, int paramInt1, int paramInt2)
    {
      Insets localInsets = SyntheticaRootPaneUI.this.rootPane.getBorder().getBorderInsets(SyntheticaRootPaneUI.this.rootPane);
      int i = paramWindow.getWidth();
      int j = paramWindow.getHeight();

      if ((paramInt1 < localInsets.left) && (paramInt2 < localInsets.top))
        return 6;
      if ((paramInt1 > i - localInsets.right) && (paramInt2 < localInsets.top))
        return 7;
      if ((paramInt1 < localInsets.left) && (paramInt2 > j - localInsets.bottom))
        return 4;
      if ((paramInt1 > i - localInsets.right) && (paramInt2 > j - localInsets.bottom)) {
        return 5;
      }
      if (paramInt1 < localInsets.left)
        return 10;
      if (paramInt1 > i - localInsets.right)
        return 11;
      if (paramInt2 < localInsets.top)
        return 8;
      if (paramInt2 > j - localInsets.bottom) {
        return 9;
      }
      return 0;
    }

    private boolean isFrameResizable()
    {
      return (this.frame != null) && (this.frame.isResizable()) && ((this.frame.getExtendedState() & 0x6) == 0);
    }

    private boolean isDialogResizable()
    {
      return (this.dialog != null) && (this.dialog.isResizable());
    }

    private boolean isWindowResizable()
    {
      return (isFrameResizable()) || (isDialogResizable());
    }
  }

  private static class SyntheticaRootLayout
    implements LayoutManager2
  {
    public void addLayoutComponent(String paramString, Component paramComponent)
    {
    }

    public void removeLayoutComponent(Component paramComponent)
    {
    }

    public void addLayoutComponent(Component paramComponent, Object paramObject)
    {
    }

    public float getLayoutAlignmentX(Container paramContainer)
    {
      return 0.0F; } 
    public float getLayoutAlignmentY(Container paramContainer) { return 0.0F; }

    public void invalidateLayout(Container paramContainer)
    {
    }

    public Dimension preferredLayoutSize(Container paramContainer)
    {
      Insets localInsets = paramContainer.getInsets();
      JRootPane localJRootPane = (JRootPane)paramContainer;
      JComponent localJComponent = ((SyntheticaRootPaneUI)localJRootPane.getUI()).titlePane;

      Dimension localDimension1 = new Dimension(0, 0);
      if (localJRootPane.getContentPane() != null)
        localDimension1 = localJRootPane.getContentPane().getPreferredSize();
      else
        localDimension1 = localJRootPane.getSize();
      localDimension1 = (localDimension1 == null) ? new Dimension(0, 0) : localDimension1;

      Dimension localDimension2 = new Dimension(0, 0);
      if (localJRootPane.getJMenuBar() != null)
        localDimension2 = localJRootPane.getJMenuBar().getPreferredSize();
      localDimension2 = (localDimension2 == null) ? new Dimension(0, 0) : localDimension2;

      Dimension localDimension3 = localJComponent.getPreferredSize();
      localDimension3 = (localDimension3 == null) ? new Dimension(0, 0) : localDimension3;

      int i = Math.max(localDimension1.width, Math.max(localDimension2.width, localDimension3.width)) + localInsets.left + localInsets.right;
      int j = localDimension1.height + localDimension2.height + localDimension3.height + localInsets.top + localInsets.bottom;
      return new Dimension(i, j);
    }

    public Dimension minimumLayoutSize(Container paramContainer)
    {
      Insets localInsets = paramContainer.getInsets();
      JRootPane localJRootPane = (JRootPane)paramContainer;
      JComponent localJComponent = ((SyntheticaRootPaneUI)localJRootPane.getUI()).titlePane;

      Dimension localDimension1 = new Dimension(0, 0);
      if (localJRootPane.getContentPane() != null)
        localDimension1 = localJRootPane.getContentPane().getMinimumSize();
      else
        localDimension1 = localJRootPane.getSize();
      localDimension1 = (localDimension1 == null) ? new Dimension(0, 0) : localDimension1;

      Dimension localDimension2 = new Dimension(0, 0);
      if (localJRootPane.getJMenuBar() != null)
        localDimension2 = localJRootPane.getJMenuBar().getMinimumSize();
      localDimension2 = (localDimension2 == null) ? new Dimension(0, 0) : localDimension2;

      Dimension localDimension3 = localJComponent.getMinimumSize();
      localDimension3 = (localDimension3 == null) ? new Dimension(0, 0) : localDimension3;

      int i = Math.max(localDimension1.width, Math.max(localDimension2.width, localDimension3.width)) + localInsets.left + localInsets.right;
      int j = localDimension1.height + localDimension2.height + localDimension3.height + localInsets.top + localInsets.bottom;
      return new Dimension(i, j);
    }

    public Dimension maximumLayoutSize(Container paramContainer)
    {
      Insets localInsets = paramContainer.getInsets();
      JRootPane localJRootPane = (JRootPane)paramContainer;
      JComponent localJComponent = ((SyntheticaRootPaneUI)localJRootPane.getUI()).titlePane;

      Dimension localDimension1 = new Dimension(0, 0);
      if (localJRootPane.getContentPane() != null)
        localDimension1 = localJRootPane.getContentPane().getMaximumSize();
      else
        localDimension1 = localJRootPane.getSize();
      localDimension1 = (localDimension1 == null) ? new Dimension(2147483647, 2147483647) : localDimension1;

      Dimension localDimension2 = new Dimension(0, 0);
      if (localJRootPane.getJMenuBar() != null)
        localDimension2 = localJRootPane.getJMenuBar().getMaximumSize();
      localDimension2 = (localDimension2 == null) ? new Dimension(2147483647, 2147483647) : localDimension2;

      Dimension localDimension3 = localJComponent.getMaximumSize();
      localDimension3 = (localDimension3 == null) ? new Dimension(2147483647, 2147483647) : localDimension3;

      int i = Math.max(localDimension1.width, Math.max(localDimension2.width, localDimension3.width));
      if (i != 2147483647) {
        i += localInsets.left + localInsets.right;
      }
      int j = Math.max(localDimension1.height, Math.max(localDimension2.height, localDimension3.height));
      if (j != 2147483647)
        j += localInsets.top + localInsets.bottom;
      return new Dimension(i, j);
    }

    public void layoutContainer(Container paramContainer)
    {
      JRootPane localJRootPane = (JRootPane)paramContainer;
      Rectangle localRectangle = localJRootPane.getBounds();
      Insets localInsets = (localJRootPane.getInsets() != null) ? localJRootPane.getInsets() : new Insets(0, 0, 0, 0);
      int i = localRectangle.width - localInsets.right - localInsets.left;
      int j = localRectangle.height - localInsets.top - localInsets.bottom;

      int k = 0;

      if (localJRootPane.getLayeredPane() != null)
        localJRootPane.getLayeredPane().setBounds(localInsets.left, localInsets.top, i, 
          j);
      if (localJRootPane.getGlassPane() != null) {
        localJRootPane.getGlassPane().setBounds(localInsets.left, localInsets.top, i, 
          j);
      }

      JComponent localJComponent = ((SyntheticaRootPaneUI)localJRootPane.getUI()).titlePane;
      if (localJComponent.isEnabled())
      {
        if (localJComponent.getPreferredSize() != null)
        {
          localJComponent.setBounds(0, 0, i, ((Dimension)localJComponent.getPreferredSize()).height);
          k += ((Dimension)localJComponent.getPreferredSize()).height;
        }
      }

      Object localObject1 = localJRootPane.getJMenuBar();
      if (localObject1 != null)
      {
        Object localObject2 = ((JMenuBar)localObject1).getPreferredSize();
        ((JMenuBar)localObject1).setBounds(0, k, i, ((Dimension)localObject2).height);
        k += ((Dimension)localObject2).height;
      }

      Object localObject2 = localJRootPane.getContentPane();
      if (localObject2 == null) {
        return;
      }
      ((Container)localObject2).setBounds(0, k, i, (j < k) ? 0 : j - k);
    }
  }
}