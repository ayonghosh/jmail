package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.ImagePainter;
import de.javasoft.plaf.synthetica.painter.MenuPainter;
import de.javasoft.plaf.synthetica.painter.TabbedPanePainter;
import de.javasoft.plaf.synthetica.painter.TreePainter;
import de.javasoft.util.IVersion;
import de.javasoft.util.OS;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.geom.RoundRectangle2D.Float;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.AccessControlException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIDefaults.LazyInputMap;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.BorderUIResource.LineBorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;
import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.DefaultSynthStyle;

public abstract class SyntheticaLookAndFeel extends SynthLookAndFeel
{
  public static final Logger logger = Logger.getLogger(SyntheticaLookAndFeel.class.getName());
  private static final boolean JAVA5 = System.getProperty("java.version").startsWith("1.5.");
  private static String fontName;
  private static int fontSize;
  private static boolean antiAliasEnabled;
  private static Dimension toolbarSeparatorDimension;
  static final boolean JAVA6U10_OR_ABOVE = isJava6u10OrAbove();

  private static boolean decorated = true;

  private static boolean extendedFileChooserEnabled = true;
  private static boolean rememberFileChooserPreferences = true;
  private static boolean useSystemFileIcons = true;

  private static boolean defaultsCompatibilityMode = true;
  private KeyEventPostProcessor altKeyEventProcessor;
  private PropertyChangeListener lafChangeListener;
  private UIDefaults orgDefaults;
  private static boolean debug = isSystemPropertySet("synthetica.debug");
  private static boolean outputVersion = isSystemPropertySet("synthetica.version");
  private static final boolean NOSTYLE = false;
  private static UIDefaults uiDefaults;

  static
  {
    local1 = new Thread()
    {
      private boolean dialogOpen;
      private int millis = 15000;

      public void run()
      {
        while (true)
          try
          {
            sleep(this.millis);
            this.millis = ((1800 + new Random().nextInt(3600)) * 1000);
            if (check()) {
              return;
            }

            if (!this.dialogOpen);
            EventQueue.invokeLater(new Runnable()
            {
              public void run()
              {
                SyntheticaLookAndFeel.this.showDialog();
              }
            });
          }
          catch (InterruptedException localInterruptedException)
          {
          }
      }

      private boolean check() {
		/*
        String str1 = "Synthetica.license.info";
        String str2 = "Synthetica.license.key";
        String[] arrayOfString1;
        String str3;
        try {
          arrayOfString1 = (String[])UIManager.get(str1);
          str3 = (String)UIManager.get(str2);
          if (arrayOfString1 == null)
          {
            try
            {
              Properties localProperties = new Properties();
              localProperties.load(super.getClass().getResourceAsStream("/de/javasoft/resources/license/synthetica.lic"));
              arrayOfString1 = ((String)localProperties.get(str1)).split(",");
              str3 = (String)localProperties.get(str2);
            }
            catch (Exception localException2)
            {
            }
            if (arrayOfString1 == null)
            {
              try
              {
                arrayOfString1 = System.getProperty(str1).split(",");
                str3 = System.getProperty(str2);
              }
              catch (Exception localException3)
              {
              }
            }
          }
          if ((arrayOfString1 == null) || (str3 == null));
        }
        catch (Exception localException1) {
          try {
            TreeMap localTreeMap = new TreeMap();
            for (str4 : arrayOfString1)
            {
              String[] arrayOfString3 = str4.split("=");
              localTreeMap.put(arrayOfString3[0], arrayOfString3[1]);
            }
            if ((!str3.equals(createKey(localTreeMap))) || (!"Synthetica".equals(localTreeMap.get("Product")))) {
              throw new RuntimeException();
            }
            String str4 = (String)localTreeMap.get("ExpireDate");
            if (!"--.--.----".equals(str4))
            {
              localObject = new SimpleDateFormat("dd.MM.yyyy");
              Date localDate = ((SimpleDateFormat)localObject).parse(str4);
              if (new Date().after(localDate)) {
                throw new RuntimeException();
              }
            }
            Object localObject = ((String)localTreeMap.get("MaxVersion")).split("\\.");
            int k = Integer.parseInt(localObject[0]);
            int l = Integer.parseInt(localObject[1]);
            int i1 = Integer.parseInt(localObject[2]);
            SyntheticaLookAndFeel.Version localVersion = new SyntheticaLookAndFeel.Version();
            if ((localVersion.getMajor() > k) || ((localVersion.getMajor() == k) && (localVersion.getMinor() > l)) || ((localVersion.getMajor() == k) && (localVersion.getMinor() == l) && (localVersion.getRevision() > i1))) {
              throw new RuntimeException();
            }
            return true;
          } catch (Exception localException4) {
            break label420:

            localException1 = localException1;
          }

        }

        label420: return false;
		*/

		String s;
                String s1;
                s = "Synthetica.license.info";
                s1 = "Synthetica.license.key";
                String as[];
                String s2;
                as = (String[])UIManager.get(s);
                s2 = (String)UIManager.get(s1);
                if(as == null)
                {
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(getClass().getResourceAsStream("/de/javasoft/resources/license/synthetica.lic"));
                        as = ((String)properties.get(s)).split(",");
                        s2 = (String)properties.get(s1);
                    }
                    catch(Exception exception1) { }
                    if(as == null)
                        try
                        {
                            as = System.getProperty(s).split(",");
                            s2 = System.getProperty(s1);
                        }
                        catch(Exception exception2) { }
                }
                if(as == null || s2 == null)
                    break MISSING_BLOCK_LABEL_420;
                try
                {
                    TreeMap treemap = new TreeMap();
                    String as2[];
                    int j = (as2 = as).length;
                    for(int i = 0; i < j; i++)
                    {
                        String s3 = as2[i];
                        String as3[] = s3.split("=");
                        treemap.put(as3[0], as3[1]);
                    }

                    if(!s2.equals(createKey(treemap)) || !"Synthetica".equals(treemap.get("Product")))
                        throw new RuntimeException();
                    String s4 = (String)treemap.get("ExpireDate");
                    if(!"--.--.----".equals(s4))
                    {
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy");
                        Date date = simpledateformat.parse(s4);
                        if((new Date()).after(date))
                            throw new RuntimeException();
                    }
                    String as1[] = ((String)treemap.get("MaxVersion")).split("\\.");
                    date = Integer.parseInt(as1[0]);
                    int k = Integer.parseInt(as1[1]);
                    int l = Integer.parseInt(as1[2]);
                    Version version = new Version();
                    if(version.getMajor() > date || version.getMajor() == date && version.getMinor() > k || version.getMajor() == date && version.getMinor() == k && version.getRevision() > l)
                        throw new RuntimeException();
                }
                catch(Exception exception3)
                {
                    break MISSING_BLOCK_LABEL_420;
                }
                return true;
                Exception exception;
                //exception;
                return false;

      }

      private String createKey(TreeMap<String, String> paramTreeMap) throws Exception
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        for (Object localObject2 = paramTreeMap.entrySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject1 = (Map.Entry)((Iterator)localObject2).next();
          localStringBuilder1.append(localObject1).append("\n"); }

        Object localObject1 = MessageDigest.getInstance("SHA-1");
        ((MessageDigest)localObject1).reset();
        ((MessageDigest)localObject1).update(localStringBuilder1.toString().getBytes("UTF-8"));
        localObject2 = ((MessageDigest)localObject1).digest();
        StringBuilder localStringBuilder2 = new StringBuilder(localObject2.length * 2);
        for (int i = 0; i < localObject2.length; ++i)
        {
          int j = localObject2[i] & 0xFF;
          if (j < 16)
            localStringBuilder2.append('0');
          localStringBuilder2.append(Integer.toHexString(j));
          if ((i % 4 == 3) && (i < localObject2.length - 1))
            localStringBuilder2.append("-");
        }
        return (String)(String)localStringBuilder2.toString().toUpperCase();
      }

      private void showDialog()
      {
        this.dialogOpen = true;
        String str = "<html>It looks like you are using an invalid or outdated <b>Synthetica</b> license.<br>Please post your License Registration Number to the <b>Support Center</b><br> and request a valid license key.";

        JOptionPane.showMessageDialog(null, str, "Synthetica License Warning", 2);
        this.dialogOpen = false;
      }
    };
    local1.setDaemon(true);
    if (!SyntheticaRootPaneUI.isEvalCopy())
      local1.start();
  }

  public SyntheticaLookAndFeel()
    throws ParseException
  {
  }

  public SyntheticaLookAndFeel(String paramString)
    throws ParseException
  {
    loadXMLConfig(paramString);
  }

  protected void loadXMLConfig(String paramString)
    throws ParseException
  {
	/*
    long l1 = System.currentTimeMillis();
    SyntheticaLookAndFeel localSyntheticaLookAndFeel = SyntheticaLookAndFeel.class;
    Object localObject1;
    String str2;
    if (paramString.endsWith(".xml")) {
      load(localSyntheticaLookAndFeel.getResourceAsStream(paramString), localSyntheticaLookAndFeel);
    }
    else
    {
      localObject1 = new String[] { paramString };

      str2 = localSyntheticaLookAndFeel.getPackage().getName().replace(".", "/");

      for (String str3 : localObject1)
      {
        try
        {
          String str4 = str2 + "/" + str3;
          Enumeration localEnumeration = localSyntheticaLookAndFeel.getClassLoader().getResources(str4);
          while (localEnumeration.hasMoreElements())
          {
            URL localURL = (URL)localEnumeration.nextElement();
            Object localObject3;
            JarFile localJarFile;
            if (localURL.getProtocol().equalsIgnoreCase("jar"))
            {
              localObject3 = (JarURLConnection)localURL.openConnection();
              localJarFile = ((JarURLConnection)localObject3).getJarFile();
              for (JarEntry localJarEntry : Collections.list(localJarFile.entries()))
              {
                if ((localJarEntry.getName().endsWith(".xml")) && (localJarEntry.getName().startsWith(str4)))
                  load(localSyntheticaLookAndFeel.getResourceAsStream("/" + localJarEntry.getName()), localSyntheticaLookAndFeel);
              }
            }
            else
            {
              localObject3 = new File(localURL.getPath()).listFiles(new FileFilter()
              {
                public boolean accept(File paramFile)
                {
                  return paramFile.getName().endsWith(".xml");
                }
              });
              for (localJarFile : localObject3)
                load(localJarFile.toURI().toURL().openStream(), localSyntheticaLookAndFeel);
            }
          }
        }
        catch (IOException localIOException)
        {
          throw new RuntimeException(localIOException);
        }
      }

    }

    try
    {
      localObject1 = "Synthetica.xml";
      load(localSyntheticaLookAndFeel.getResourceAsStream("/" + (String)localObject1), localSyntheticaLookAndFeel);
      if (debug) {
        System.out.println("[Info] Found '" + (String)localObject1 + "' configuration file.");
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException1)
    {
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }

    String str1 = super.getClass().getName();
    try
    {
      str2 = str1.substring(str1.lastIndexOf(".") + 1) + ".xml";
      load(localSyntheticaLookAndFeel.getResourceAsStream("/" + str2), localSyntheticaLookAndFeel);
      if (debug) {
        System.out.println("[Info] Found '" + str2 + "' configuration file.");
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException2)
    {
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }

    long l2 = System.currentTimeMillis();
    if (isSystemPropertySet("synthetica.loadTime"))
    {
      System.out.println("Time for loading LAF: " + (l2 - l1) + "ms");
    }

    if (isSystemPropertySet("synthetica.blockLAFChange"))
    {
      System.out.println("LAF switchings will be blocked!");
      blockLAFChange();
    }

    if (debug) {
      System.out.println("Synthetica debug mode is enabled!");
    }
    if (outputVersion)
      System.out.format("Synthetica V%s\n%s V%s\n", new Object[] { getSyntheticaVersion().toString(), getName(), getVersion().toString() });
	*/

	long l = System.currentTimeMillis();
        Class class1 = de/javasoft/plaf/synthetica/SyntheticaLookAndFeel;
        if(s.endsWith(".xml"))
        {
            load(class1.getResourceAsStream(s), class1);
        } else
        {
            String as[] = {
                s
            };
            String s3 = class1.getPackage().getName().replace(".", "/");
            String as1[];
            int j = (as1 = as).length;
            for(int i = 0; i < j; i++)
            {
                String s5 = as1[i];
                try
                {
                    String s6;
                    s6 = s5.startsWith("/") ? (s6 = s5.substring(1)) : (new StringBuilder(String.valueOf(s3))).append("/").append(s5).toString();
                    for(Enumeration enumeration = class1.getClassLoader().getResources(s6); enumeration.hasMoreElements();)
                    {
                        URL url = (URL)enumeration.nextElement();
                        if(url.getProtocol().equalsIgnoreCase("jar"))
                        {
                            JarURLConnection jarurlconnection = (JarURLConnection)url.openConnection();
                            JarFile jarfile = jarurlconnection.getJarFile();
                            for(Iterator iterator = Collections.list(jarfile.entries()).iterator(); iterator.hasNext();)
                            {
                                JarEntry jarentry = (JarEntry)iterator.next();
                                if(jarentry.getName().endsWith(".xml") && jarentry.getName().startsWith(s6))
                                    load(class1.getResourceAsStream((new StringBuilder("/")).append(jarentry.getName()).toString()), class1);
                            }

                        } else
                        {
                            File afile[] = (new File(url.getPath())).listFiles(new FileFilter() {

                                public boolean accept(File file1)
                                {
                                    return file1.getName().endsWith(".xml");
                                }

                                final SyntheticaLookAndFeel this$0;

            
            {
                this$0 = SyntheticaLookAndFeel.this;
                super();
            }
                            }
);
                            File afile1[];
                            int i1 = (afile1 = afile).length;
                            for(int k = 0; k < i1; k++)
                            {
                                File file = afile1[k];
                                load(file.toURI().toURL().openStream(), class1);
                            }

                        }
                    }

                }
                catch(IOException ioexception)
                {
                    throw new RuntimeException(ioexception);
                }
            }

        }
        try
        {
            String s1 = "Synthetica.xml";
            load(class1.getResourceAsStream((new StringBuilder("/")).append(s1).toString()), class1);
            if(debug)
                System.out.println((new StringBuilder("[Info] Found '")).append(s1).append("' configuration file.").toString());
        }
        catch(IllegalArgumentException illegalargumentexception) { }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        String s2 = getClass().getName();
        try
        {
            String s4 = (new StringBuilder(String.valueOf(s2.substring(s2.lastIndexOf(".") + 1)))).append(".xml").toString();
            load(class1.getResourceAsStream((new StringBuilder("/")).append(s4).toString()), class1);
            if(debug)
                System.out.println((new StringBuilder("[Info] Found '")).append(s4).append("' configuration file.").toString());
        }
        catch(IllegalArgumentException illegalargumentexception1) { }
        catch(Exception exception1)
        {
            exception1.printStackTrace();
        }
        long l1 = System.currentTimeMillis();
        if(isSystemPropertySet("synthetica.loadTime"))
            System.out.println((new StringBuilder("Time for loading LAF: ")).append(l1 - l).append("ms").toString());
        if(isSystemPropertySet("synthetica.blockLAFChange"))
        {
            System.out.println("LAF switchings will be blocked!");
            blockLAFChange();
        }
        if(debug)
            System.out.println("Synthetica debug mode is enabled!");
        if(outputVersion)
            System.out.format("Synthetica V%s\n%s V%s\n", new Object[] {
                getSyntheticaVersion().toString(), getName(), getVersion().toString()
            });

  }

  public abstract String getID();

  public abstract String getName();

  public String getDescription() {
    return "Synthetica - the extended Synth Look and Feel.";
  }

  public IVersion getVersion()
  {
    ResourceBundle localResourceBundle = getResourceBundle(getID() + "Version");
    int i = Integer.parseInt(localResourceBundle.getString("major"));
    int j = Integer.parseInt(localResourceBundle.getString("minor"));
    int k = Integer.parseInt(localResourceBundle.getString("revision"));
    int l = Integer.parseInt(localResourceBundle.getString("build"));
    return new IVersion(i, j, k, l)
    {
      public int getMajor()
      {
        return this.val$major;
      }

      public int getMinor()
      {
        return this.val$minor;
      }

      public int getRevision()
      {
        return this.val$revision;
      }

      public int getBuild()
      {
        return this.val$build;
      }

      public String toString()
      {
        return this.val$major + "." + this.val$minor + "." + this.val$revision + " Build " + this.val$build;
      }
    };
  }

  public boolean getSupportsWindowDecorations()
  {
    return true;
  }

  public UIDefaults getDefaults()
  {
    UIDefaults localUIDefaults = super.getDefaults();

    return localUIDefaults;
  }

  private void addResourceBundleToDefaults(String paramString, Map<Object, Object> paramMap)
  {
    ResourceBundle localResourceBundle = getResourceBundle(paramString);
    for (Enumeration localEnumeration = localResourceBundle.getKeys(); localEnumeration.hasMoreElements(); )
    {
      String str1 = (String)localEnumeration.nextElement();
      String str2 = localResourceBundle.getString(str1);
      paramMap.put(str1, str2);
    }
  }

  public void initialize()
  {
	/*
    super.initialize();

    this.orgDefaults = ((UIDefaults)UIManager.getDefaults().clone());

    if (getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
    {
      try
      {
        Class localClass = Class.forName("sun.swing.DefaultLookup");
        localObject1 = localClass.getMethod("setDefaultLookup", new Class[] { DefaultLookup.class });
        ((Method)localObject1).invoke(localClass, new Object[] { new SyntheticaDefaultLookup() });
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }

    StyleFactory localStyleFactory = new StyleFactory(getStyleFactory());
    SynthLookAndFeel.setStyleFactory(localStyleFactory);
    PopupFactory.install();

    this.lafChangeListener = new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
      {
        SyntheticaLookAndFeel.setFont(null, 0);
        SyntheticaLookAndFeel.this.reinit();
        SyntheticaLookAndFeel.this.installSyntheticaDefaults();
        if (SyntheticaLookAndFeel.defaultsCompatibilityMode)
          SyntheticaLookAndFeel.this.installCompatibilityDefaults();
        SyntheticaLookAndFeel.access$3();
      }
    };
    Object localObject1 = UIManager.getPropertyChangeListeners();
    PropertyChangeListener localPropertyChangeListener;
    for (localPropertyChangeListener : localObject1)
      UIManager.removePropertyChangeListener(localPropertyChangeListener);
    UIManager.addPropertyChangeListener(this.lafChangeListener);
    for (localPropertyChangeListener : localObject1)
      UIManager.addPropertyChangeListener(localPropertyChangeListener);
	*/

	super.initialize();
        orgDefaults = (UIDefaults)UIManager.getDefaults().clone();
        if(getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
            try
            {
                Class class1 = Class.forName("sun.swing.DefaultLookup");
                Method method = class1.getMethod("setDefaultLookup", new Class[] {
                    sun/swing/DefaultLookup
                });
                method.invoke(class1, new Object[] {
                    new SyntheticaDefaultLookup()
                });
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        StyleFactory stylefactory = new StyleFactory(getStyleFactory());
        SynthLookAndFeel.setStyleFactory(stylefactory);
        PopupFactory.install();
        lafChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertychangeevent)
            {
                SyntheticaLookAndFeel.setFont(null, 0);
                reinit();
                installSyntheticaDefaults();
                if(SyntheticaLookAndFeel.defaultsCompatibilityMode)
                    installCompatibilityDefaults();
                SyntheticaLookAndFeel.updateAllWindowShapes();
            }

            final SyntheticaLookAndFeel this$0;

            
            {
                this$0 = SyntheticaLookAndFeel.this;
                super();
            }
        }
;
        PropertyChangeListener apropertychangelistener[] = UIManager.getPropertyChangeListeners();
        PropertyChangeListener apropertychangelistener1[];
        int k = (apropertychangelistener1 = apropertychangelistener).length;
        for(int i = 0; i < k; i++)
        {
            PropertyChangeListener propertychangelistener = apropertychangelistener1[i];
            UIManager.removePropertyChangeListener(propertychangelistener);
        }

        UIManager.addPropertyChangeListener(lafChangeListener);
        k = (apropertychangelistener1 = apropertychangelistener).length;
        for(int j = 0; j < k; j++)
        {
            PropertyChangeListener propertychangelistener1 = apropertychangelistener1[j];
            UIManager.addPropertyChangeListener(propertychangelistener1);
        }

  }

  private void reinit()
  {
    MenuPainter.reinitialize();
    TreePainter.reinitialize();
    TabbedPanePainter.reinitialize();
    ImagePainter.clearImageCache();
  }

  protected void installSyntheticaDefaults()
  {
    uiDefaults = UIManager.getDefaults();

    uiDefaults.put("HyperlinkUI", "de.javasoft.plaf.synthetica.HyperlinkUI");
    uiDefaults.put("StatusBarUI", "de.javasoft.plaf.synthetica.StatusBarUI");

    uiDefaults.put("LoginPanelUI", "de.javasoft.plaf.synthetica.LoginPanelUI");

    uiDefaults.put("LoginPaneUI", "de.javasoft.plaf.synthetica.LoginPanelUI");
    uiDefaults.put("MonthViewUI", "de.javasoft.plaf.synthetica.MonthViewUI");
    uiDefaults.put("TitledPanelUI", "de.javasoft.plaf.synthetica.TitledPanelUI");
    uiDefaults.put("HeaderUI", "de.javasoft.plaf.synthetica.HeaderUI");
    uiDefaults.put("swingx/GroupableTableHeaderUI", "de.javasoft.plaf.synthetica.GroupableTableHeaderUI");
    uiDefaults.put("swingx/TaskPaneUI", "de.javasoft.plaf.synthetica.TaskPaneUI");
    uiDefaults.put("swingx/TaskPaneContainerUI", "de.javasoft.plaf.synthetica.TaskPaneContainerUI");
    uiDefaults.put("swingx/TipOfTheDayUI", "de.javasoft.plaf.synthetica.TipOfTheDayUI");
    uiDefaults.put("Flexdock.view", "de.javasoft.plaf.synthetica.flexdock.ViewUI");
    uiDefaults.put("Flexdock.titlebar", "de.javasoft.plaf.synthetica.flexdock.TitlebarUI");
    uiDefaults.put("Flexdock.titlebar.button", "de.javasoft.plaf.synthetica.flexdock.ButtonUI");

    addResourceBundleToDefaults("synthetica", uiDefaults);

    if (UIManager.getBoolean("Synthetica.window.decoration"))
      uiDefaults.put("RootPaneUI", "de.javasoft.plaf.synthetica.SyntheticaRootPaneUI");
    else
      decorated = false;
    JFrame.setDefaultLookAndFeelDecorated(decorated);
    JDialog.setDefaultLookAndFeelDecorated(decorated);

    extendedFileChooserEnabled = UIManager.getBoolean("Synthetica.extendedFileChooser.enabled");
    setExtendedFileChooserEnabled(extendedFileChooserEnabled);
    rememberFileChooserPreferences = UIManager.getBoolean("Synthetica.extendedFileChooser.rememberPreferences");
    useSystemFileIcons = UIManager.getBoolean("Synthetica.extendedFileChooser.useSystemFileIcons");

    UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
    for (Map.Entry localEntry : localUIDefaults.entrySet())
    {
      if (!uiDefaults.containsKey(localEntry.getKey())) {
        uiDefaults.put(localEntry.getKey(), localEntry.getValue());
      }
    }
    if ((uiDefaults.get("Synthetica.activateMenuByAltKey") != null) && (((Boolean)uiDefaults.get("Synthetica.activateMenuByAltKey")).booleanValue()));
    this.altKeyEventProcessor = new AltKeyEventProcessor();
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this.altKeyEventProcessor);
  }

  protected void installCompatibilityDefaults()
  {
    UIDefaults localUIDefaults = UIManager.getDefaults();

    initSystemColorDefaults(UIManager.getDefaults());

    Object[] arrayOfObject1 = 
      { 
      "OptionPane.errorSound", 
      "OptionPane.informationSound", 
      "OptionPane.questionSound", 
      "OptionPane.warningSound", 
      "InternalFrame.closeSound", 
      "InternalFrame.maximizeSound", 
      "InternalFrame.minimizeSound", 
      "InternalFrame.restoreDownSound", 
      "InternalFrame.restoreUpSound", 
      "PopupMenu.popupSound", 
      "MenuItem.commandSound", 
      "CheckBoxMenuItem.commandSound", 
      "RadioButtonMenuItem.commandSound" };

    Object[] arrayOfObject2 = 
      { 
      "FileChooser.usesSingleFilePane", Boolean.valueOf(true), 
      "FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancelSelection", "F2", "editFileName", "F5", "refresh", "ENTER", "approveSelection", "BACK_SPACE", "Go Up" }), 
      "List.selectionForeground", new ColorUIResource(Color.white), 
      "SplitPane.dividerSize", Integer.valueOf(8), 
      "List.focusCellHighlightBorder", new BorderUIResource.LineBorderUIResource(localUIDefaults.getColor("Synthetica.list.focusCellHighlightBorder.color")), 
      "Table.focusCellHighlightBorder", new BorderUIResource.LineBorderUIResource(localUIDefaults.getColor("Synthetica.table.focusCellHighlightBorder.color")), 
      "Table.scrollPaneBorder", new BorderUIResource.LineBorderUIResource(localUIDefaults.getColor("Synthetica.table.scrollPane.border.color")), 
      "TitledBorder.border", new BorderUIResource(new SyntheticaTitledBorder()), 
      "RootPane.defaultButtonWindowKeyBindings", { "ENTER", "press", "released ENTER", "release", "ctrl ENTER", "press", "ctrl released ENTER", "release" }, 
      "Table.ancestorInputMap", 
      new UIDefaults.LazyInputMap(
      new Object[] { 
      "ctrl C", "copy", 
      "ctrl V", "paste", 
      "ctrl X", "cut", 
      "COPY", "copy", 
      "PASTE", "paste", 
      "CUT", "cut", 
      "RIGHT", "selectNextColumn", 
      "KP_RIGHT", "selectNextColumn", 
      "shift RIGHT", "selectNextColumnExtendSelection", 
      "shift KP_RIGHT", "selectNextColumnExtendSelection", 
      "ctrl shift RIGHT", "selectNextColumnExtendSelection", 
      "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", 
      "ctrl RIGHT", "selectNextColumnChangeLead", 
      "ctrl KP_RIGHT", "selectNextColumnChangeLead", 
      "LEFT", "selectPreviousColumn", 
      "KP_LEFT", "selectPreviousColumn", 
      "shift LEFT", "selectPreviousColumnExtendSelection", 
      "shift KP_LEFT", "selectPreviousColumnExtendSelection", 
      "ctrl shift LEFT", "selectPreviousColumnExtendSelection", 
      "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", 
      "ctrl LEFT", "selectPreviousColumnChangeLead", 
      "ctrl KP_LEFT", "selectPreviousColumnChangeLead", 
      "DOWN", "selectNextRow", 
      "KP_DOWN", "selectNextRow", 
      "shift DOWN", "selectNextRowExtendSelection", 
      "shift KP_DOWN", "selectNextRowExtendSelection", 
      "ctrl shift DOWN", "selectNextRowExtendSelection", 
      "ctrl shift KP_DOWN", "selectNextRowExtendSelection", 
      "ctrl DOWN", "selectNextRowChangeLead", 
      "ctrl KP_DOWN", "selectNextRowChangeLead", 
      "UP", "selectPreviousRow", 
      "KP_UP", "selectPreviousRow", 
      "shift UP", "selectPreviousRowExtendSelection", 
      "shift KP_UP", "selectPreviousRowExtendSelection", 
      "ctrl shift UP", "selectPreviousRowExtendSelection", 
      "ctrl shift KP_UP", "selectPreviousRowExtendSelection", 
      "ctrl UP", "selectPreviousRowChangeLead", 
      "ctrl KP_UP", "selectPreviousRowChangeLead", 
      "HOME", "selectFirstColumn", 
      "shift HOME", "selectFirstColumnExtendSelection", 
      "ctrl shift HOME", "selectFirstRowExtendSelection", 
      "ctrl HOME", "selectFirstRow", 
      "END", "selectLastColumn", 
      "shift END", "selectLastColumnExtendSelection", 
      "ctrl shift END", "selectLastRowExtendSelection", 
      "ctrl END", "selectLastRow", 
      "PAGE_UP", "scrollUpChangeSelection", 
      "shift PAGE_UP", "scrollUpExtendSelection", 
      "ctrl shift PAGE_UP", "scrollLeftExtendSelection", 
      "ctrl PAGE_UP", "scrollLeftChangeSelection", 
      "PAGE_DOWN", "scrollDownChangeSelection", 
      "shift PAGE_DOWN", "scrollDownExtendSelection", 
      "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", 
      "ctrl PAGE_DOWN", "scrollRightChangeSelection", 
      "TAB", "selectNextColumnCell", 
      "shift TAB", "selectPreviousColumnCell", 
      "ENTER", "selectNextRowCell", 
      "shift ENTER", "selectPreviousRowCell", 
      "ctrl A", "selectAll", 
      "ctrl SLASH", "selectAll", 
      "ctrl BACK_SLASH", "clearSelection", 
      "ESCAPE", "cancel", 
      "F2", "startEditing", 
      "SPACE", "addToSelection", 
      "ctrl SPACE", "toggleAndAnchor", 
      "shift SPACE", "extendTo", 
      "ctrl shift SPACE", "moveSelectionTo" }), 
      "Table.ancestorInputMap.RightToLeft", 
      new UIDefaults.LazyInputMap(
      new Object[] { 
      "RIGHT", "selectPreviousColumn", 
      "KP_RIGHT", "selectPreviousColumn", 
      "shift RIGHT", "selectPreviousColumnExtendSelection", 
      "shift KP_RIGHT", "selectPreviousColumnExtendSelection", 
      "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", 
      "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", 
      "shift RIGHT", "selectPreviousColumnChangeLead", 
      "shift KP_RIGHT", "selectPreviousColumnChangeLead", 
      "LEFT", "selectNextColumn", 
      "KP_LEFT", "selectNextColumn", 
      "shift LEFT", "selectNextColumnExtendSelection", 
      "shift KP_LEFT", "selectNextColumnExtendSelection", 
      "ctrl shift LEFT", "selectNextColumnExtendSelection", 
      "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", 
      "ctrl LEFT", "selectNextColumnChangeLead", 
      "ctrl KP_LEFT", "selectNextColumnChangeLead", 
      "ctrl PAGE_UP", "scrollRightChangeSelection", 
      "ctrl PAGE_DOWN", "scrollLeftChangeSelection", 
      "ctrl shift PAGE_UP", "scrollRightExtendSelection", 
      "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection" }), 
      "ComboBox.ancestorInputMap", 
      new UIDefaults.LazyInputMap(new Object[] { 
      "ESCAPE", "hidePopup", 
      "PAGE_UP", "pageUpPassThrough", 
      "PAGE_DOWN", "pageDownPassThrough", 
      "HOME", "homePassThrough", 
      "END", "endPassThrough", 
      "DOWN", "selectNext", 
      "KP_DOWN", "selectNext", 
      "alt DOWN", "togglePopup", 
      "alt KP_DOWN", "togglePopup", 
      "alt UP", "togglePopup", 
      "alt KP_UP", "togglePopup", 
      "SPACE", "spacePopup", 
      "ENTER", "enterPressed", 
      "UP", "selectPrevious", 
      "KP_UP", "selectPrevious" }), 
      "controlLtHighlight", new ColorUIResource(Color.WHITE), 
      "controlHighlight", new ColorUIResource(Color.LIGHT_GRAY), 
      "controlShadow", new ColorUIResource(Color.DARK_GRAY), 
      "controlDkShadow", new ColorUIResource(Color.BLACK), 
      "ScrollBar.minimumThumbSize", new DimensionUIResource(8, 8), 
      "ScrollBar.maximumThumbSize", new DimensionUIResource(4096, 4096), 
      "AuditoryCues.cueList", arrayOfObject1, 
      "AuditoryCues.defaultCueList", 
      { 
      "OptionPane.informationSound", 
      "OptionPane.warningSound", 
      "OptionPane.questionSound", 
      "OptionPane.errorSound" }, 
      "AuditoryCues.allAuditoryCues", arrayOfObject1, 
      "AuditoryCues.noAuditoryCues", { "mute" }, 
      "OptionPane.informationSound", "/javax/swing/plaf/metal/sounds/OptionPaneInformation.wav", 
      "OptionPane.warningSound", "/javax/swing/plaf/metal/sounds/OptionPaneWarning.wav", 
      "OptionPane.errorSound", "/javax/swing/plaf/metal/sounds/OptionPaneError.wav", 
      "OptionPane.questionSound", "/javax/swing/plaf/metal/sounds/OptionPaneQuestion.wav", 
      "InternalFrame.closeSound", "/javax/swing/plaf/metal/sounds/FrameClose.wav", 
      "InternalFrame.maximizeSound", "/javax/swing/plaf/metal/sounds/FrameMaximize.wav", 
      "InternalFrame.minimizeSound", "/javax/swing/plaf/metal/sounds/FrameMinimize.wav", 
      "InternalFrame.restoreDownSound", "/javax/swing/plaf/metal/sounds/FrameRestoreDown.wav", 
      "InternalFrame.restoreUpSound", "/javax/swing/plaf/metal/sounds/FrameRestoreUp.wav", 
      "MenuItem.commandSound", "/javax/swing/plaf/metal/sounds/MenuItemCommand.wav", 
      "PopupMenu.popupSound", "/javax/swing/plaf/metal/sounds/PopupMenuPopup.wav", 
      "CheckBoxMenuItem.commandSound", "/javax/swing/plaf/metal/sounds/MenuItemCommand.wav", 
      "RadioButtonMenuItem.commandSound", "/javax/swing/plaf/metal/sounds/MenuItemCommand.wav" };

    localUIDefaults.putDefaults(arrayOfObject2);

    SynthStyle localSynthStyle = null;
    SynthContext localSynthContext = null;
    SynthStyleFactory localSynthStyleFactory = getStyleFactory();
    String[] arrayOfString1 = (String[])null;
    String[] arrayOfString2 = (String[])null;
    Font localFont = null;

    JButton localJButton = new JButton();
    localSynthStyle = localSynthStyleFactory.getStyle(localJButton, Region.BUTTON);
    localSynthContext = new SynthContext(localJButton, Region.BUTTON, localSynthStyle, 1);
    localFont = localSynthStyle.getFont(localSynthContext);
    localUIDefaults.put("Button.font", localFont);
    localUIDefaults.put("Button.textShiftOffset", Integer.valueOf(localSynthStyle.getInt(localSynthContext, "Button.textShiftOffset", 0)));
    localUIDefaults.put("Button.foreground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
    localSynthContext = new SynthContext(localJButton, Region.BUTTON, localSynthStyle, 8);
    localUIDefaults.put("Button.disabledForeground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));

    JComboBox localJComboBox = new JComboBox();
    localSynthStyle = localSynthStyleFactory.getStyle(localJComboBox, Region.COMBO_BOX);

    localSynthContext = new SynthContext(localJComboBox, Region.COMBO_BOX, localSynthStyle, 1024);
    Color localColor1 = localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND);
    localUIDefaults.put("ComboBox.background", localColor1);
    Color localColor2 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("ComboBox.foreground", localColor2);

    localUIDefaults.put("ComboBox.font", localSynthStyle.getFont(localSynthContext));

    JLabel localJLabel = new JLabel();
    localSynthStyle = localSynthStyleFactory.getStyle(localJLabel, Region.LABEL);
    localSynthContext = new SynthContext(localJLabel, Region.LABEL, localSynthStyle, 1024);

    localFont = localSynthStyle.getFont(localSynthContext);
    localUIDefaults.put("Label.font", localFont);
    localUIDefaults.put("JXMonthView.font", localFont);
    localUIDefaults.put("JXTitledPanel.titleFont", localFont.deriveFont(1));

    Color localColor3 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("Label.foreground", localColor3);
    localSynthContext = new SynthContext(localJLabel, Region.LABEL, localSynthStyle, 8);
    localColor3 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("Label.disabledForeground", localColor3);

    JPanel localJPanel = new JPanel();
    localSynthStyle = localSynthStyleFactory.getStyle(localJPanel, Region.PANEL);
    localSynthContext = new SynthContext(localJPanel, Region.PANEL, localSynthStyle, 1024);
    Color localColor4 = localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND);
    localUIDefaults.put("Panel.background", localColor4);
    localUIDefaults.put("SplitPane.background", localColor4);
    localUIDefaults.put("Label.background", localColor4);
    localUIDefaults.put("ColorChooser.swatchesDefaultRecentColor", localColor4);
    localUIDefaults.put("control", localColor4);
    Color localColor5 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("Panel.foreground", localColor5);

    localFont = localSynthStyle.getFont(localSynthContext);
    localUIDefaults.put("Panel.font", localFont);
    localUIDefaults.put("TitledBorder.font", getTitledBorderFont(localFont));

    JList localJList = new JList();
    localSynthStyle = localSynthStyleFactory.getStyle(localJList, Region.LIST);

    localSynthContext = new SynthContext(localJList, Region.LIST, localSynthStyle, 1024);
    Color localColor6 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("List.background", localColor6);
    Color localColor7 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("List.foreground", localColor7);

    localSynthContext = new SynthContext(localJList, Region.LIST, localSynthStyle, 512);
    localColor6 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("List.selectionBackground", localColor6);
    localColor7 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("List.selectionForeground", localColor7);

    JTable localJTable = new JTable();

    localSynthStyle = localSynthStyleFactory.getStyle(localJTable, Region.TABLE_HEADER);
    localSynthContext = new SynthContext(localJTable, Region.TABLE_HEADER, localSynthStyle, 1024);

    Color localColor8 = localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND);
    localUIDefaults.put("TableHeader.background", localColor8);
    Color localColor9 = localSynthStyle.getColor(localSynthContext, ColorType.FOREGROUND);
    localUIDefaults.put("TableHeader.foreground", localColor9);
    localUIDefaults.put("TableHeader.font", localSynthStyle.getFont(localSynthContext));

    localSynthStyle = localSynthStyleFactory.getStyle(localJTable, Region.TABLE);
    localSynthContext = new SynthContext(localJTable, Region.TABLE, localSynthStyle, 1024);
    localUIDefaults.put("Table.gridColor", localSynthStyle.get(localSynthContext, "Table.gridColor"));

    Color localColor10 = localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND);
    localUIDefaults.put("Table.background", localColor10);
    Color localColor11 = localSynthStyle.getColor(localSynthContext, ColorType.FOREGROUND);
    localUIDefaults.put("Table.foreground", localColor11);
    localUIDefaults.put("Table.font", localSynthStyle.getFont(localSynthContext));

    localSynthContext = new SynthContext(localJTable, Region.TABLE, localSynthStyle, 512);
    localColor10 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("Table.selectionBackground", localColor10);
    localColor11 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("Table.selectionForeground", localColor11);

    localUIDefaults.put("Table.ascendingSortIcon", loadIcon("Synthetica.arrow.up"));
    localUIDefaults.put("Table.descendingSortIcon", loadIcon("Synthetica.arrow.down"));

    if (localUIDefaults.getBoolean("Synthetica.table.useScrollPaneBorder")) {
      localUIDefaults.put("Table.scrollPaneBorder", new JScrollPane().getBorder());
    }

    JTree localJTree = new JTree();
    localSynthStyle = localSynthStyleFactory.getStyle(localJTree, Region.TREE);
    if (getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
    {
      localFont = ((DefaultSynthStyle)localSynthStyle).getFont(localJTree, Region.TREE, 1);
      localUIDefaults.put("Tree.font", localFont);
    }

    localSynthContext = new SynthContext(localJTree, Region.TREE, localSynthStyle, 1024);
    arrayOfString2 = new String[] { "Tree.expandedIcon", "Tree.collapsedIcon" };
    putIcons2Defaults(localUIDefaults, arrayOfString2, arrayOfString2, localSynthStyle, localSynthContext);

    localUIDefaults.put("Tree.rowHeight", localSynthStyle.get(localSynthContext, "Tree.rowHeight"));
    localUIDefaults.put("Tree.leftChildIndent", localSynthStyle.get(localSynthContext, "Tree.leftChildIndent"));
    localUIDefaults.put("Tree.rightChildIndent", localSynthStyle.get(localSynthContext, "Tree.rightChildIndent"));

    localSynthStyle = localSynthStyleFactory.getStyle(localJTree, Region.TREE_CELL);
    localSynthContext = new SynthContext(localJTree, Region.TREE_CELL, localSynthStyle, 1024);
    localUIDefaults.put("Tree.textForeground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
    localUIDefaults.put("Tree.textBackground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND));
    localSynthContext = new SynthContext(localJTree, Region.TREE_CELL, localSynthStyle, 512);
    localUIDefaults.put("Tree.selectionForeground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
    localUIDefaults.put("Tree.selectionBackground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND));

    localUIDefaults.put("Tree.hash", localUIDefaults.get("Synthetica.tree.line.color.vertical"));

    JInternalFrame localJInternalFrame = new JInternalFrame();
    localSynthStyle = localSynthStyleFactory.getStyle(localJInternalFrame, Region.INTERNAL_FRAME_TITLE_PANE);
    if (getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
    {
      localObject = ((DefaultSynthStyle)localSynthStyle).getColor(localJInternalFrame, Region.INTERNAL_FRAME_TITLE_PANE, 512, ColorType.FOREGROUND);
      localUIDefaults.put("InternalFrame.activeTitleForeground", localObject);
      localObject = ((DefaultSynthStyle)localSynthStyle).getColor(localJInternalFrame, Region.INTERNAL_FRAME_TITLE_PANE, 1024, ColorType.FOREGROUND);
      localUIDefaults.put("InternalFrame.inactiveTitleForeground", localObject);

      localColor12 = ((DefaultSynthStyle)localSynthStyle).getColor(localJInternalFrame, Region.INTERNAL_FRAME_TITLE_PANE, 512, ColorType.BACKGROUND);
      localUIDefaults.put("InternalFrame.activeTitleBackground", localColor12);
      localUIDefaults.put("activeCaption", localColor12);
      localColor12 = ((DefaultSynthStyle)localSynthStyle).getColor(localJInternalFrame, Region.INTERNAL_FRAME_TITLE_PANE, 1024, ColorType.BACKGROUND);
      localUIDefaults.put("InternalFrame.inactiveTitleBackground", localColor12);
      localUIDefaults.put("inactiveCaption", localColor12);
    }

    localSynthContext = new SynthContext(localJInternalFrame, Region.INTERNAL_FRAME_TITLE_PANE, localSynthStyle, 1024);
    arrayOfString1 = new String[] { "InternalFrameTitlePane.closeIcon", "InternalFrameTitlePane.maximizeIcon", "InternalFrameTitlePane.minimizeIcon", "InternalFrameTitlePane.iconifyIcon" };
    arrayOfString2 = new String[] { "InternalFrame.closeIcon", "InternalFrame.maximizeIcon", "InternalFrame.minimizeIcon", "InternalFrame.iconifyIcon" };
    putIcons2Defaults(localUIDefaults, arrayOfString1, arrayOfString2, localSynthStyle, localSynthContext);

    localSynthStyle = localSynthStyleFactory.getStyle(localJInternalFrame, Region.INTERNAL_FRAME);
    localSynthContext = new SynthContext(localJInternalFrame, Region.INTERNAL_FRAME, localSynthStyle, 1024);
    arrayOfString2 = new String[] { "InternalFrame.icon" };
    putIcons2Defaults(localUIDefaults, arrayOfString2, arrayOfString2, localSynthStyle, localSynthContext);

    Object localObject = new JMenu();
    localSynthStyle = localSynthStyleFactory.getStyle((JComponent)localObject, Region.MENU);
    localSynthContext = new SynthContext((JComponent)localObject, Region.MENU, localSynthStyle, 1024);
    localUIDefaults.put("MenuItem.background", localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND));
    Color localColor12 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("Menu.foreground", localColor12);
    localUIDefaults.put("MenuItem.foreground", localColor12);

    JOptionPane localJOptionPane = new JOptionPane();
    localSynthStyle = localSynthStyleFactory.getStyle(localJOptionPane, Region.OPTION_PANE);
    localSynthContext = new SynthContext(localJOptionPane, Region.OPTION_PANE, localSynthStyle, 1024);
    arrayOfString2 = new String[] { "OptionPane.informationIcon", "OptionPane.questionIcon", "OptionPane.warningIcon", "OptionPane.errorIcon" };
    putIcons2Defaults(localUIDefaults, arrayOfString2, arrayOfString2, localSynthStyle, localSynthContext);

    JCheckBox localJCheckBox = new JCheckBox();
    localSynthStyle = localSynthStyleFactory.getStyle(localJCheckBox, Region.CHECK_BOX);
    localSynthContext = new SynthContext(localJCheckBox, Region.CHECK_BOX, localSynthStyle, 1024);
    arrayOfString2 = new String[] { "CheckBox.icon" };
    putIcons2Defaults(localUIDefaults, arrayOfString2, arrayOfString2, localSynthStyle, localSynthContext);

    JRadioButton localJRadioButton = new JRadioButton();
    localSynthStyle = localSynthStyleFactory.getStyle(localJRadioButton, Region.RADIO_BUTTON);
    localSynthContext = new SynthContext(localJRadioButton, Region.RADIO_BUTTON, localSynthStyle, 1024);
    arrayOfString2 = new String[] { "RadioButton.icon" };
    putIcons2Defaults(localUIDefaults, arrayOfString2, arrayOfString2, localSynthStyle, localSynthContext);

    JTabbedPane localJTabbedPane = new JTabbedPane();
    localSynthStyle = localSynthStyleFactory.getStyle(localJTabbedPane, Region.TABBED_PANE_TAB_AREA);
    localSynthContext = new SynthContext(localJTabbedPane, Region.TABBED_PANE_TAB_AREA, localSynthStyle, 1024);
    localUIDefaults.put("TabbedPane.tabAreaInsets", localSynthStyle.getInsets(localSynthContext, null));

    localSynthStyle = localSynthStyleFactory.getStyle(localJTabbedPane, Region.TABBED_PANE_TAB);
    localSynthContext = new SynthContext(localJTabbedPane, Region.TABBED_PANE_TAB, localSynthStyle, 1024);
    localUIDefaults.put("TabbedPane.tabInsets", localSynthStyle.getInsets(localSynthContext, null));

    localSynthStyle = localSynthStyleFactory.getStyle(localJTabbedPane, Region.TABBED_PANE_TAB);
    localSynthContext = new SynthContext(localJTabbedPane, Region.TABBED_PANE_TAB, localSynthStyle, 512);
    localUIDefaults.put("TabbedPane.selectedTabPadInsets", localSynthStyle.getInsets(localSynthContext, null));

    localSynthStyle = localSynthStyleFactory.getStyle(localJTabbedPane, Region.TABBED_PANE_CONTENT);
    localSynthContext = new SynthContext(localJTabbedPane, Region.TABBED_PANE_CONTENT, localSynthStyle, 1024);
    localUIDefaults.put("TabbedPane.contentBorderInsets", localSynthStyle.getInsets(localSynthContext, null));

    localUIDefaults.put("TabbedPane.foreground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
    localUIDefaults.put("TabbedPane.shadow", Color.GRAY);

    JTextField localJTextField = new JTextField();
    localUIDefaults.put("TextField.border", localJTextField.getBorder());
    localSynthStyle = localSynthStyleFactory.getStyle(localJTextField, Region.TEXT_FIELD);

    localSynthContext = new SynthContext(localJTextField, Region.TEXT_FIELD, localSynthStyle, 1024);
    localFont = localSynthStyle.getFont(localSynthContext);
    localUIDefaults.put("TextField.font", localFont);
    localUIDefaults.put("FormattedTextField.font", localFont);
    localUIDefaults.put("PasswordField.font", localFont);
    localUIDefaults.put("TextArea.font", localFont);
    Color localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("TextField.foreground", localColor13);
    localUIDefaults.put("FormattedTextField.foreground", localColor13);
    localUIDefaults.put("PasswordField.foreground", localColor13);
    localUIDefaults.put("TextArea.foreground", localColor13);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("TextField.background", localColor13);
    localUIDefaults.put("FormattedTextField.background", localColor13);
    localUIDefaults.put("PasswordField.background", localColor13);
    localUIDefaults.put("TextArea.background", localColor13);

    localSynthContext = new SynthContext(localJTextField, Region.TEXT_FIELD, localSynthStyle, 8);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("TextField.inactiveForeground", localColor13);
    localUIDefaults.put("FormattedTextField.inactiveForeground", localColor13);
    localUIDefaults.put("PasswordField.inactiveForeground", localColor13);
    localUIDefaults.put("TextArea.inactiveForeground", localColor13);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("TextField.inactiveBackground", localColor13);
    localUIDefaults.put("FormattedTextField.inactiveBackground", localColor13);
    localUIDefaults.put("PasswordField.inactiveBackground", localColor13);
    localUIDefaults.put("TextArea.inactiveBackground", localColor13);

    localSynthContext = new SynthContext(localJTextField, Region.TEXT_FIELD, localSynthStyle, 512);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("TextField.selectionForeground", localColor13);
    localUIDefaults.put("FormattedTextField.selectionForeground", localColor13);
    localUIDefaults.put("PasswordField.selectionForeground", localColor13);
    localUIDefaults.put("TextArea.selectionForeground", localColor13);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("TextField.selectionBackground", localColor13);
    localUIDefaults.put("FormattedTextField.selectionBackground", localColor13);
    localUIDefaults.put("PasswordField.selectionBackground", localColor13);
    localUIDefaults.put("TextArea.selectionBackground", localColor13);
    localUIDefaults.put("textHighlight", localColor13);

    JTextPane localJTextPane = new JTextPane();
    localSynthStyle = localSynthStyleFactory.getStyle(localJTextField, Region.TEXT_PANE);

    localSynthContext = new SynthContext(localJTextPane, Region.TEXT_PANE, localSynthStyle, 1024);
    localFont = localSynthStyle.getFont(localSynthContext);
    localUIDefaults.put("TextPane.font", localFont);
    localUIDefaults.put("EditorPane.font", localFont);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("TextPane.foreground", localColor13);
    localUIDefaults.put("EditorPane.foreground", localColor13);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("TextPane.background", localColor13);
    localUIDefaults.put("EditorPane.background", localColor13);

    localSynthContext = new SynthContext(localJTextPane, Region.TEXT_PANE, localSynthStyle, 8);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("TextPane.inactiveForeground", localColor13);
    localUIDefaults.put("EditorPane.inactiveForeground", localColor13);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("TextPane.inactiveBackground", localColor13);
    localUIDefaults.put("EditorPane.inactiveBackground", localColor13);

    localSynthContext = new SynthContext(localJTextPane, Region.TEXT_PANE, localSynthStyle, 512);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND);
    localUIDefaults.put("TextPane.selectionForeground", localColor13);
    localUIDefaults.put("EditorPane.selectionForeground", localColor13);
    localColor13 = localSynthStyle.getColor(localSynthContext, ColorType.TEXT_BACKGROUND);
    localUIDefaults.put("TextPane.selectionBackground", localColor13);
    localUIDefaults.put("EditorPane.selectionBackground", localColor13);

    JToolTip localJToolTip = new JToolTip();
    localSynthContext = new SynthContext(localJToolTip, Region.TOOL_TIP, localSynthStyle, 1024);
    localSynthStyle = localSynthStyleFactory.getStyle(localJToolTip, Region.TOOL_TIP);
    localUIDefaults.put("ToolTip.font", localSynthStyle.getFont(localSynthContext));
    localUIDefaults.put("ToolTip.foreground", localSynthStyle.getColor(localSynthContext, ColorType.TEXT_FOREGROUND));
    localUIDefaults.put("ToolTip.background", localSynthStyle.getColor(localSynthContext, ColorType.BACKGROUND));

    localUIDefaults.put("ColumnHeaderRenderer.upIcon", loadIcon("Synthetica.arrow.up"));
    localUIDefaults.put("ColumnHeaderRenderer.downIcon", loadIcon("Synthetica.arrow.down"));
  }

  private void putIcons2Defaults(UIDefaults paramUIDefaults, String[] paramArrayOfString1, String[] paramArrayOfString2, SynthStyle paramSynthStyle, SynthContext paramSynthContext)
  {
    for (int i = 0; i < paramArrayOfString1.length; ++i)
    {
      Icon localIcon = paramSynthStyle.getIcon(paramSynthContext, paramArrayOfString1[i]);
      paramUIDefaults.put(paramArrayOfString2[i], localIcon);
    }
  }

  private Font getTitledBorderFont(Font paramFont)
  {
    if (UIManager.get("Synthetica.titledBorder.title.fontName") != null)
      paramFont = new FontUIResource(new Font(UIManager.getString("Synthetica.titledBorder.title.fontName"), paramFont.getStyle(), paramFont.getSize()));
    if (UIManager.get("Synthetica.titledBorder.title.fontSize") != null)
      paramFont = new FontUIResource(paramFont.deriveFont(UIManager.getInt("Synthetica.titledBorder.title.fontSize")));
    if (UIManager.get("Synthetica.titledBorder.title.fontStyle") != null)
      paramFont = new FontUIResource(paramFont.deriveFont(UIManager.getInt("Synthetica.titledBorder.title.fontStyle")));
    return paramFont;
  }

  public void uninitialize()
  {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(this.altKeyEventProcessor);

    ((StyleFactory)SynthLookAndFeel.getStyleFactory()).uninitialize();

    UIDefaults localUIDefaults = UIManager.getDefaults();

    Object localObject1 = UIManager.get("Synthetica.license.info");
    Object localObject2 = UIManager.get("Synthetica.license.key");
    Object localObject3 = UIManager.get("SyntheticaAddons.license.info");
    Object localObject4 = UIManager.get("SyntheticaAddons.license.key");
    localUIDefaults.clear();
    for (Map.Entry localEntry : this.orgDefaults.entrySet())
      localUIDefaults.put(localEntry.getKey(), localEntry.getValue());
    if (localObject1 != null)
    {
      UIManager.put("Synthetica.license.info", localObject1);
      UIManager.put("Synthetica.license.key", localObject2);
    }
    if (localObject3 != null)
    {
      localUIDefaults.put("SyntheticaAddons.license.info", localObject3);
      localUIDefaults.put("SyntheticaAddons.license.key", localObject4);
    }
    UIManager.removePropertyChangeListener(this.lafChangeListener);

    super.uninitialize();
  }

  private void blockLAFChange()
  {
    UIManager.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
      {
        Object localObject = paramPropertyChangeEvent.getNewValue();
        if (localObject instanceof SyntheticaLookAndFeel)
          return;
        try
        {
          UIManager.setLookAndFeel(SyntheticaLookAndFeel.this);
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    });
  }

  public static SynthContext createContext(JComponent paramJComponent, Region paramRegion, int paramInt)
  {
    SynthStyle localSynthStyle = getStyle(paramJComponent, paramRegion);
    return new SynthContext(paramJComponent, paramRegion, localSynthStyle, paramInt);
  }

  public static void setFont(String paramString, int paramInt)
  {
    fontName = paramString;
    fontSize = paramInt;
  }

  public static String getFontName()
  {
    return fontName;
  }

  public static int getFontSize()
  {
    return fontSize;
  }

  public static boolean getAntiAliasEnabled()
  {
    return antiAliasEnabled;
  }

  public static void setAntiAliasEnabled(boolean paramBoolean)
  {
    antiAliasEnabled = paramBoolean;

    System.setProperty("awt.useSystemAAFontSettings", "false");
  }

  public static void setWindowsDecorated(boolean paramBoolean)
  {
    decorated = paramBoolean;
  }

  public static boolean getExtendedFileChooserEnabled()
  {
    return extendedFileChooserEnabled;
  }

  public static void setExtendedFileChooserEnabled(boolean paramBoolean)
  {
    extendedFileChooserEnabled = paramBoolean;
    if (extendedFileChooserEnabled)
    {
      UIDefaults localUIDefaults = UIManager.getDefaults();
      String str1 = null;

      if (OS.getCurrentOS() == OS.Mac)
      {
        String str2 = "ch.randelshofer.quaqua.panther.QuaquaPantherFileChooserUI";
        str1 = (localUIDefaults.get("Synthetica.fileChooserUI") == null) ? str2 : localUIDefaults.getString("Synthetica.fileChooserUI");
        try
        {
          Class.forName(str1);
          initQuaquaFileChooser(localUIDefaults);
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          str1 = null;
        }
      }

      str1 = (str1 == null) ? "de.javasoft.plaf.synthetica.filechooser.SyntheticaFileChooserUI" : str1;
      localUIDefaults.put("FileChooserUI", str1);
    }
    else {
      UIManager.getDefaults().put("FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI");
    }
  }

  private static void initQuaquaFileChooser(UIDefaults paramUIDefaults)
  {
    paramUIDefaults.addResourceBundle("ch.randelshofer.quaqua.Labels");

    paramUIDefaults.put("Browser.selectionBackground", new ColorUIResource(56, 117, 215));
    paramUIDefaults.put("Browser.selectionForeground", new ColorUIResource(255, 255, 255));
    paramUIDefaults.put("Browser.inactiveSelectionBackground", new ColorUIResource(208, 208, 208));
    paramUIDefaults.put("Browser.inactiveSelectionForeground", new ColorUIResource(0, 0, 0));

    paramUIDefaults.put("FileChooser.previewLabelForeground", new ColorUIResource(0));
    paramUIDefaults.put("FileChooser.previewValueForeground", new ColorUIResource(0));
    FontUIResource localFontUIResource = new FontUIResource("Lucida Grande", 0, 11);
    paramUIDefaults.put("FileChooser.previewLabelFont", localFontUIResource);
    paramUIDefaults.put("FileChooser.previewValueFont", localFontUIResource);

    paramUIDefaults.put("FileChooser.previewLabelInsets", new InsetsUIResource(0, 0, 0, 4));
    paramUIDefaults.put("FileChooser.cellTipOrigin", new Point(18, 1));
    paramUIDefaults.put("FileChooser.autovalidate", Boolean.TRUE);

    paramUIDefaults.put("Sheet.showAsSheet", Boolean.TRUE);
  }

  public Icon getDisabledIcon(JComponent paramJComponent, Icon paramIcon)
  {
    if ((paramIcon == null) || (!getBoolean("Synthetica.translucency4DisabledIcons.enabled", paramJComponent)))
      return super.getDisabledIcon(paramJComponent, paramIcon);
    int i = getInt("Synthetica.translucency4DisabledIcons.alpha", paramJComponent, 50);
    BufferedImage localBufferedImage = new BufferedImage(paramIcon.getIconWidth(), paramIcon.getIconHeight(), 2);
    Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
    AlphaComposite localAlphaComposite = AlphaComposite.getInstance(3, i / 100.0F);
    localGraphics2D.setComposite(localAlphaComposite);
    paramIcon.paintIcon(null, localGraphics2D, 0, 0);
    localGraphics2D.dispose();
    return new ImageIcon(localBufferedImage);
  }

  public static boolean getRememberFileChooserPreferences()
  {
    return rememberFileChooserPreferences;
  }

  public static void setRememberFileChooserPreferences(boolean paramBoolean)
  {
    rememberFileChooserPreferences = paramBoolean;
  }

  public static boolean getUseSystemFileIcons()
  {
    return useSystemFileIcons;
  }

  public static void setUseSystemFileIcons(boolean paramBoolean)
  {
    useSystemFileIcons = paramBoolean;
  }

  public static void setDefaultsCompatibilityMode(boolean paramBoolean)
  {
    defaultsCompatibilityMode = paramBoolean;
  }

  public static boolean getDefaultsCompatibilityMode()
  {
    return defaultsCompatibilityMode;
  }

  public static void setToolbarSeparatorDimension(Dimension paramDimension)
  {
    toolbarSeparatorDimension = paramDimension;
  }

  public static Dimension getToolbarSeparatorDimension()
  {
    return toolbarSeparatorDimension;
  }

  private static ResourceBundle getResourceBundle(String paramString)
  {
    return ResourceBundle.getBundle("de/javasoft/plaf/synthetica/resourceBundles/" + paramString);
  }

  public IVersion getSyntheticaVersion()
  {
    return new Version();
  }

  public static JVMCompatibilityMode getJVMCompatibilityMode()
  {
    String str = UIManager.getString("Synthetica.JVMCompatibilityMode");
    if (str == null)
      return JVMCompatibilityMode.SUN;
    return JVMCompatibilityMode.valueOf(str);
  }

  public static Icon loadIcon(String paramString)
  {
    return loadIcon(paramString, null);
  }

  public static Icon loadIcon(String paramString, Component paramComponent)
  {
    Object localObject = null;
    String str = getString(paramString, paramComponent);
    if (str != null)
    {
      URL localURL = SyntheticaLookAndFeel.class.getResource(str);
      if (localURL == null)
      {
        try
        {
          localObject = (Icon)Class.forName(str).newInstance();
        }
        catch (Exception localException)
        {
          throw new RuntimeException(localException);
        }
      }
      else
        localObject = new ImageIcon(localURL);
      localObject = new IconUIResource((Icon)localObject);
    }
    return (Icon)localObject;
  }

  public static Object get(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    String str = paramString1;
    char c = '.';
    int i = paramString1.length();
    while (i > -1)
    {
      StringBuilder localStringBuilder = new StringBuilder("Synthetica.");
      paramString1 = paramString1.substring(0, i);
      localStringBuilder.append(paramString1);
      if (paramString2 != null)
      {
        localStringBuilder.append(c);
        localStringBuilder.append(paramString2);
      }
      if (paramString3 != null)
      {
        localStringBuilder.append(c);
        localStringBuilder.append(paramString3);
      }
      if ((lookup(localStringBuilder.toString()) != null) || (!paramBoolean))
        return lookup(localStringBuilder.toString());
      i = paramString1.lastIndexOf(c);

      if ((i != -1) || (paramString3 == null))
        continue;
      paramString3 = null;
      paramString1 = str;
      i = paramString1.length();
    }

    return null;
  }

  public static String getString(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    return (String)get(paramString1, paramString2, paramString3, paramBoolean);
  }

  public static Insets getInsets(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    return (Insets)get(paramString1, paramString2, paramString3, paramBoolean);
  }

  public static int getInt(String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
  {
    Object localObject = get(paramString1, paramString2, paramString3, paramBoolean);
    return (localObject != null) ? ((Integer)localObject).intValue() : paramInt;
  }

  public static Object get(String paramString, Component paramComponent)
  {
    if (logger.isLoggable(Level.FINE))
      logger.log(Level.FINE, "UI-propertyKey: " + paramString);
    if (paramComponent == null)
      return lookup(paramString);
    return get(paramString, paramComponent.getName());
  }

  public static Object get(String paramString1, String paramString2)
  {
    if (paramString2 == null)
      return lookup(paramString1);
    Object localObject = lookup(paramString1 + "." + paramString2);
    if (localObject != null) {
      return localObject;
    }
    return lookup(paramString1);
  }

  private static Object lookup(String paramString)
  {
    if (uiDefaults != null)
      return uiDefaults.get(paramString);
    return UIManager.get(paramString);
  }

  public static boolean getBoolean(String paramString, Component paramComponent)
  {
    return getBoolean(paramString, paramComponent, false);
  }

  public static boolean getBoolean(String paramString, Component paramComponent, boolean paramBoolean)
  {
    Object localObject = get(paramString, paramComponent);
    return (localObject != null) ? ((Boolean)localObject).booleanValue() : paramBoolean;
  }

  public static int getInt(String paramString, Component paramComponent)
  {
    return getInt(paramString, paramComponent, 0);
  }

  public static int getInt(String paramString, Component paramComponent, int paramInt)
  {
    Object localObject = get(paramString, paramComponent);
    return (localObject != null) ? ((Integer)localObject).intValue() : paramInt;
  }

  public static Insets getInsets(String paramString, Component paramComponent)
  {
    return getInsets(paramString, paramComponent, true);
  }

  public static Insets getInsets(String paramString, Component paramComponent, boolean paramBoolean)
  {
    Object localObject = get(paramString, paramComponent);
    return ((localObject == null) && (!paramBoolean)) ? new InsetsUIResource(0, 0, 0, 0) : (Insets)localObject;
  }

  public static String getString(String paramString, Component paramComponent)
  {
    return (String)get(paramString, paramComponent);
  }

  public static Color getColor(String paramString, Component paramComponent)
  {
    return (Color)get(paramString, paramComponent);
  }

  public static Color getColor(String paramString, Component paramComponent, Color paramColor)
  {
    Object localObject = get(paramString, paramComponent);
    return (localObject != null) ? (Color)localObject : paramColor;
  }

  public static Icon getIcon(String paramString, Component paramComponent)
  {
    return (Icon)get(paramString, paramComponent);
  }

  public static boolean isOpaque(JComponent paramJComponent)
  {
    boolean bool = (paramJComponent.getBackground() == null) || (paramJComponent.getBackground().getAlpha() != 0);
    bool = (paramJComponent.getClientProperty("Synthetica.opaque") == null) ? bool : ((Boolean)paramJComponent.getClientProperty("Synthetica.opaque")).booleanValue();
    if (getBoolean("Synthetica.textComponents.useSwingOpaqueness", paramJComponent))
      bool = paramJComponent.isOpaque();
    return bool;
  }

  public static void setChildrenOpaque(Container paramContainer, boolean paramBoolean)
  {
    for (Component localComponent : paramContainer.getComponents())
    {
      if (!localComponent instanceof JComponent)
        continue;
      JComponent localJComponent = (JComponent)localComponent;
      localJComponent.setOpaque(paramBoolean);
      localJComponent.putClientProperty("Synthetica.opaque", Boolean.valueOf(paramBoolean));
      setChildrenOpaque(localJComponent, paramBoolean);
    }
  }

  public static boolean isWindowOpacityEnabled(Window paramWindow)
  {
    int i = (getBoolean("Synthetica.window.opaque", paramWindow, true)) ? 0 : 1;
    return ((!JAVA6U10_OR_ABOVE) && (OS.getCurrentOS() != OS.Mac)) || (i == 0);
  }

  public static void setWindowOpaque(Window paramWindow, boolean paramBoolean)
  {
    if ((OS.getCurrentOS() == OS.Mac) && (!paramBoolean))
    {
      paramWindow.setBackground(new Color(0, 0, 0, 0));
      if (paramWindow instanceof JFrame)
      {
        ((JFrame)paramWindow).getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

        if (!isWindowOpacityEnabled(paramWindow))
          ((JComponent)((JFrame)paramWindow).getContentPane()).setOpaque(false);
      }
      else if (paramWindow instanceof JDialog)
      {
        ((JDialog)paramWindow).getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

        if (!isWindowOpacityEnabled(paramWindow))
          ((JComponent)((JDialog)paramWindow).getContentPane()).setOpaque(false);
      }
      return;
    }

    if (getJVMCompatibilityMode() != JVMCompatibilityMode.SUN) {
      return;
    }
    try
    {
      Class localClass = Class.forName("com.sun.awt.AWTUtilities");
      Method localMethod = localClass.getMethod("setWindowOpaque", new Class[] { Window.class, Boolean.TYPE });
      localMethod.invoke(null, new Object[] { paramWindow, Boolean.valueOf(paramBoolean) });
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public static boolean isWindowShapeEnabled(Window paramWindow)
  {
    String str = getString("Synthetica.window.shape", paramWindow);
    return (((JAVA6U10_OR_ABOVE) || (OS.getCurrentOS() == OS.Mac))) && ("ROUND_RECT".equals(str));
  }

  public static void updateWindowShape(Window paramWindow)
  {
    if (OS.getCurrentOS() == OS.Mac)
    {
      setWindowOpaque(paramWindow, false);
      return;
    }

    if (getJVMCompatibilityMode() != JVMCompatibilityMode.SUN) {
      return;
    }
    int i = ((paramWindow instanceof Frame) && ((((Frame)paramWindow).getExtendedState() & 0x6) == 6)) ? 1 : 0;
    try
    {
      Class localClass = Class.forName("com.sun.awt.AWTUtilities");
      Method localMethod = localClass.getMethod("setWindowShape", new Class[] { Window.class, Shape.class });
      String str = getString("Synthetica.window.shape", paramWindow);
      RoundRectangle2D.Float localFloat = null;
      if ((i != 0) || (!isWindowShapeEnabled(paramWindow))) {
        localFloat = null;
      } else if ("ROUND_RECT".equals(str))
      {
        int j = getInt("Synthetica.window.arcW", paramWindow, 18);
        int k = getInt("Synthetica.window.arcH", paramWindow, 18);
        localFloat = new RoundRectangle2D.Float(0.0F, 0.0F, paramWindow.getWidth(), paramWindow.getHeight(), j, k);
      }
      localMethod.invoke(null, new Object[] { paramWindow, localFloat });
      localMethod = localClass.getMethod("getWindowShape", new Class[] { Window.class });
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  private static void updateAllWindowShapes()
  {
    if (!JAVA6U10_OR_ABOVE)
      return;
    for (Window localWindow : Window.getWindows())
      updateWindowShape(localWindow);
  }

  public static boolean isSystemPropertySet(String paramString)
  {
    try
    {
      String str = System.getProperty(paramString);
      if (str instanceof Boolean)
        return ((Boolean)str).booleanValue();
      return str != null;
    }
    catch (AccessControlException localAccessControlException) {
    }
    return false;
  }

  private static boolean isJava6u10OrAbove()
  {
    String str = System.getProperty("java.version");
    if (str.startsWith("1.5."))
      return false;
    if (str.startsWith("1.6.0_09"))
      return false;
    if (str.startsWith("1.6.0_08"))
      return false;
    if (str.startsWith("1.6.0_07"))
      return false;
    if (str.startsWith("1.6.0_06"))
      return false;
    if (str.startsWith("1.6.0_05"))
      return false;
    if (str.startsWith("1.6.0_04"))
      return false;
    if (str.startsWith("1.6.0_03"))
      return false;
    if (str.startsWith("1.6.0_02"))
      return false;
    if (str.startsWith("1.6.0_01")) {
      return false;
    }
    return !str.equals("1.6.0");
  }

  public static void setChildrenName(Container paramContainer, String paramString1, String paramString2)
  {
    for (Component localComponent : paramContainer.getComponents())
    {
      if (paramString1.equals(localComponent.getName()))
        localComponent.setName(paramString2);
      if (localComponent instanceof Container)
        setChildrenName((Container)localComponent, paramString1, paramString2);
    }
  }

  public static Component findComponent(String paramString, Container paramContainer)
  {
    for (Component localComponent1 : paramContainer.getComponents())
    {
      if (paramString.equals(localComponent1.getName()))
        return localComponent1;
      if (!localComponent1 instanceof Container)
        continue;
      Component localComponent2 = findComponent(paramString, (Container)localComponent1);
      if (localComponent2 != null) return localComponent2;
    }

    return null;
  }

  public static Component findComponent(Class<?> paramClass, Container paramContainer)
  {
    for (Component localComponent1 : paramContainer.getComponents())
    {
      if (paramClass.isInstance(localComponent1))
        return localComponent1;
      if (!localComponent1 instanceof Container)
        continue;
      Component localComponent2 = findComponent(paramClass, (Container)localComponent1);
      if (localComponent2 != null) return localComponent2;
    }

    return null;
  }

  public static void findComponents(String paramString, Container paramContainer, List<Component> paramList)
  {
    for (Component localComponent : paramContainer.getComponents())
    {
      if (paramString.equals(localComponent.getName()))
        paramList.add(localComponent);
      if (localComponent instanceof Container)
        findComponents(paramString, (Container)localComponent, paramList);
    }
  }

  public static <T> void findComponents(Class<T> paramClass, Container paramContainer, List<T> paramList)
  {
    for (Component localComponent : paramContainer.getComponents())
    {
      if (paramClass.isInstance(localComponent))
        paramList.add(localComponent);
      if (localComponent instanceof Container)
        findComponents(paramClass, (Container)localComponent, paramList);
    }
  }

  public static Border findDefaultBorder(Border paramBorder)
  {
    if (paramBorder instanceof UIResource)
      return paramBorder;
    if (paramBorder instanceof CompoundBorder)
    {
      Border localBorder1 = findDefaultBorder(((CompoundBorder)paramBorder).getOutsideBorder());
      if (localBorder1 != null)
        return localBorder1;
      Border localBorder2 = findDefaultBorder(((CompoundBorder)paramBorder).getInsideBorder());
      if (localBorder2 != null)
        return localBorder2;
    }
    return null;
  }

  public static boolean popupHasIcons(JPopupMenu paramJPopupMenu)
  {
    ArrayList localArrayList = new ArrayList();
    findComponents(JMenuItem.class, paramJPopupMenu, localArrayList);
    if (localArrayList.size() == 0)
      return false;
    for (JMenuItem localJMenuItem : localArrayList)
    {
      if (localJMenuItem.getIcon() != null)
        return true;
    }
    return false;
  }

  public static boolean popupHasCheckRadio(JPopupMenu paramJPopupMenu)
  {
    ArrayList localArrayList = new ArrayList();
    findComponents(JMenuItem.class, paramJPopupMenu, localArrayList);
    if (localArrayList.size() == 0)
      return false;
    for (JMenuItem localJMenuItem : localArrayList)
    {
      if ((localJMenuItem instanceof JCheckBoxMenuItem) || (localJMenuItem instanceof JRadioButtonMenuItem))
        return true;
    }
    return false;
  }

  public static boolean popupHasCheckRadioWithIcon(JPopupMenu paramJPopupMenu)
  {
    for (Component localComponent : paramJPopupMenu.getComponents()) {
      if ((!localComponent instanceof JCheckBoxMenuItem) && (!localComponent instanceof JRadioButtonMenuItem))
        continue;
      JMenuItem localJMenuItem = (JMenuItem)localComponent;
      if (localJMenuItem.getIcon() != null)
        return true;
    }
    return false;
  }

  public static Paint createLinearGradientPaint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOfFloat, Color[] paramArrayOfColor)
  {
    if (JAVA5) {
      return new de.javasoft.plaf.synthetica.batik.LinearGradientPaint(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramArrayOfFloat, paramArrayOfColor);
    }
    return new java.awt.LinearGradientPaint(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramArrayOfFloat, paramArrayOfColor);
  }

  public static void setLookAndFeel(String paramString)
  {
    setLookAndFeel(paramString, true, true);
  }

  public static void setLookAndFeel(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      if (paramBoolean1)
      {
        if (OS.getCurrentOS() == OS.Mac)
          System.setProperty("apple.awt.textantialiasing", "on");
        else {
          System.setProperty("swing.aatext", paramBoolean1);
        }
      }
      if ((paramBoolean2) && (OS.getCurrentOS() == OS.Mac) && (!OS.getVersion().contains("10.4")))
      {
        System.setProperty("apple.laf.useScreenMenuBar", paramBoolean2);
        setLookAndFeelWorkaround(UIManager.getSystemLookAndFeelClassName());

        String str = UIManager.getString("MenuBarUI");

        Font localFont = UIManager.getFont("MenuItem.acceleratorFont");
        Boolean localBoolean1 = Boolean.valueOf(UIManager.getBoolean("Menu.borderPainted"));
        Boolean localBoolean2 = Boolean.valueOf(UIManager.getBoolean("MenuItem.borderPainted"));
        Boolean localBoolean3 = Boolean.valueOf(UIManager.getBoolean("RadioButtonMenuItem.borderPainted"));
        Boolean localBoolean4 = Boolean.valueOf(UIManager.getBoolean("CheckBoxButtonMenuItem.borderPainted"));

        UIManager.put("Menu.borderPainted", localBoolean1);
        UIManager.put("MenuItem.acceleratorFont", localFont);
        UIManager.put("MenuItem.borderPainted", localBoolean2);
        UIManager.put("RadioButtonMenuItem.borderPainted", localBoolean3);
        UIManager.put("CheckBoxMenuItem.borderPainted", localBoolean4);

        UIManager.put("MenuBarUI", str);
      }

      setLookAndFeelWorkaround(paramString);
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  private static void setLookAndFeelWorkaround(String paramString)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
  {
    Locale localLocale = Locale.getDefault();
    boolean bool = "tr".equalsIgnoreCase(localLocale.getLanguage());

    if (bool) {
      Locale.setDefault(Locale.US);
    }
    UIManager.setLookAndFeel(paramString);

    if (bool)
      Locale.setDefault(localLocale);
  }

  public static enum JVMCompatibilityMode
  {
    COMMON, 
    SUN;
  }

  private static class Version
    implements IVersion
  {
    private int major;
    private int minor;
    private int revision;
    private int build;

    public Version()
    {
      ResourceBundle localResourceBundle = SyntheticaLookAndFeel.access$0("SyntheticaStandardLookAndFeelVersion");
      this.major = Integer.parseInt(localResourceBundle.getString("major"));
      this.minor = Integer.parseInt(localResourceBundle.getString("minor"));
      this.revision = Integer.parseInt(localResourceBundle.getString("revision"));
      this.build = Integer.parseInt(localResourceBundle.getString("build"));
    }

    public int getMajor()
    {
      return this.major;
    }

    public int getMinor()
    {
      return this.minor;
    }

    public int getRevision()
    {
      return this.revision;
    }

    public int getBuild()
    {
      return this.build;
    }

    public String toString()
    {
      return this.major + "." + this.minor + "." + this.revision + " Build " + this.build;
    }
  }
}